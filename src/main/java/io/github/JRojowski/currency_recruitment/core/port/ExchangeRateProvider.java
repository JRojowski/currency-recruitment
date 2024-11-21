package io.github.JRojowski.currency_recruitment.core.port;

import io.github.JRojowski.currency_recruitment.core.domain.Currency;

import java.math.BigDecimal;

public interface ExchangeRateProvider {
    BigDecimal getCurrencyExchangeRate(Currency from, Currency to);
}
