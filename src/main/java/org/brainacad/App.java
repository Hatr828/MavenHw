package org.brainacad;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.brainacad.currency.Currency;
import org.brainacad.currency.CurrencyConverter;

public class App {
    public static void main(String[] args) {
        CurrencyConverter converter = new CurrencyConverter();

        if (args.length != 3) {
            printUsage(converter);
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(args[0]);
            Currency from = Currency.valueOf(args[1].toUpperCase());
            Currency to = Currency.valueOf(args[2].toUpperCase());

            BigDecimal result = converter.convert(amount, from, to);
            System.out.printf(
                    "%s %s = %s %s%n",
                    amount.setScale(4, RoundingMode.HALF_UP).toPlainString(),
                    from,
                    result.toPlainString(),
                    to);
        } catch (IllegalArgumentException ex) {
            System.err.println("Invalid input: " + ex.getMessage());
        }
    }

    private static void printUsage(CurrencyConverter converter) {
        System.out.println("Usage: <amount> <fromCurrency> <toCurrency>");
        System.out.println("Available currencies: USD, EUR, GBP, JPY");
        System.out.println("Example: 100 usd eur");

        BigDecimal demoResult = converter.convert(BigDecimal.valueOf(100), Currency.USD, Currency.EUR);
        System.out.printf("Demo: 100 USD -> %s EUR%n", demoResult.toPlainString());
    }
}
