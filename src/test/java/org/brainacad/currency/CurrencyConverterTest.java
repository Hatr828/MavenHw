package org.brainacad.currency;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import org.junit.Test;

public class CurrencyConverterTest {

    private final CurrencyConverter converter = new CurrencyConverter();

    @Test
    public void convertsUsdToEur() {
        BigDecimal result = converter.convert(BigDecimal.valueOf(100), Currency.USD, Currency.EUR);
        assertEquals(new BigDecimal("90.9091"), result);
    }

    @Test
    public void convertsEurToUsd() {
        BigDecimal result = converter.convert(BigDecimal.valueOf(50), Currency.EUR, Currency.USD);
        assertEquals(new BigDecimal("55.0000"), result);
    }

    @Test
    public void convertsGbpToJpy() {
        BigDecimal result = converter.convert(BigDecimal.TEN, Currency.GBP, Currency.JPY);
        assertEquals(new BigDecimal("1857.1429"), result);
    }

    @Test
    public void keepsAmountWhenCurrencyMatches() {
        BigDecimal result = converter.convert(BigDecimal.valueOf(42), Currency.USD, Currency.USD);
        assertEquals(new BigDecimal("42.0000"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsOnNegativeAmount() {
        converter.convert(BigDecimal.valueOf(-10), Currency.USD, Currency.EUR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsOnNullCurrency() {
        converter.convert(BigDecimal.ONE, null, Currency.USD);
    }
}
