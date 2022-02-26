package converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Main {

    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        System.out.println("Enter two numbers in format: {source base} {target base} (To quit type /exit)");
        String basesInput = readInput();
        switch (basesInput) {
            case "/exit":
                break;
            default:
                String[] bases = basesInput.split(" ");
                int sourceBase = Integer.parseInt(bases[0]);
                int targetBase = Integer.parseInt(bases[1]);
                System.out.printf("Enter number in base %d to convert to base %d (To go back type /back)\n", sourceBase, targetBase);
                String numInput = readInput();
                while (!"/back".equals(numInput)) {
                    String[] numParts = numInput.split("\\.");
                    String integer = numParts[0];
                    BigInteger number = new BigInteger(integer, sourceBase);
                    System.out.print("Conversion result: ");
                    String result = convertInteger(number, sourceBase, targetBase);
                    if (numParts.length > 1) {
                        String fraction = numParts[1];
                        while (fraction.length() > 0 && fraction.charAt(fraction.length() - 1) == '0') {
                            fraction = fraction.substring(0, fraction.length() - 1);
                        }
                        if (fraction.length() == 0) {
                            result = result + ".00000";
                        } else {
                            result = result + "." + convertFraction(fraction, sourceBase, targetBase);
                        }
                    }
                    System.out.println(result);
                    System.out.printf("Enter number in base %d to convert to base %d (To go back type /back)\n", sourceBase, targetBase);
                    numInput = readInput();
                }
                start();
        }
    }

    public static String readInput() {
        String input = " ";
        try {
            input = reader.readLine();
        } catch (IOException exc) {
            System.out.println();
        }
        return input;
    }

    public static String convertInteger(BigInteger number, int sourceBase, int targetBase) {
        StringBuilder decimal = new StringBuilder();
        int remainder;
        do {
            remainder = number.remainder(BigInteger.valueOf(targetBase)).intValue();
            if (remainder > 9) {
                decimal.append((char) ('A' + remainder - 10));
            } else {
                decimal.append(remainder);
            }
            number = number.divide(new BigInteger(String.valueOf(targetBase)));
        } while (!number.equals(BigInteger.ZERO));
        decimal.reverse();
        return new BigInteger(decimal.toString(), targetBase).toString(targetBase);
    }

    public static String convertFraction(String strFraction, int sourceBase, int targetBase) {
        BigDecimal fraction = BigDecimal.ZERO;
        if (sourceBase != 10) {
            for (int i = 1; i <= strFraction.length(); i++) {
                char ch = strFraction.charAt(i - 1);
                int intChar = ch > 96 ? ch - 87 : ch - 48;
                fraction = fraction.add(new BigDecimal(String.valueOf(Math.pow(sourceBase, -i)))
                        .multiply(new BigDecimal(String.valueOf(intChar))));
            }
        } else {
            fraction = new BigDecimal("0." + strFraction);
        }
        BigDecimal base = new BigDecimal(String.valueOf(targetBase));
        StringBuilder result = new StringBuilder();
        for (int i = 0; !fraction.equals(BigDecimal.ZERO) && i < 5; i++) {
            fraction = fraction.multiply(base);
            int remainder = fraction.divideToIntegralValue(BigDecimal.ONE).intValue();
            if (remainder > 9) {
                result.append((char) ('A' + remainder - 10));
            } else {
                result.append(remainder);
            }
            fraction = fraction.remainder(BigDecimal.ONE);

        }
        if (result.length() < 5) {
            while (result.length() != 5) {
                result.append("0");
            }
        }
        return result.substring(0, 5);
    }
}
