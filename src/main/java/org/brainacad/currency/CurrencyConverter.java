package org.brainacad.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {

    private static final int SCALE = 4;
    private final Map<Currency, BigDecimal> toUsdRates;

    public CurrencyConverter() {
        Map<Currency, BigDecimal> rates = new HashMap<>();
        rates.put(Currency.USD, BigDecimal.ONE);
        rates.put(Currency.EUR, BigDecimal.valueOf(1.10));
        rates.put(Currency.GBP, BigDecimal.valueOf(1.30));
        rates.put(Currency.JPY, BigDecimal.valueOf(0.007));
        this.toUsdRates = Collections.unmodifiableMap(rates);
    }

    public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (from == null || to == null) {
            throw new IllegalArgumentException("Currency is required");
        }

        if (from == to) {
            return amount.setScale(SCALE, RoundingMode.HALF_DOWN);
        }

        BigDecimal fromRate = toUsdRates.get(from);
        BigDecimal toRate = toUsdRates.get(to);
        BigDecimal usdAmount = amount.multiply(fromRate);

        return usdAmount.divide(toRate, SCALE, RoundingMode.HALF_DOWN);
    }
}
