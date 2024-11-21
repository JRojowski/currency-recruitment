package io.github.JRojowski.currency_recruitment.infrastructure.adapter;

import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import io.github.JRojowski.currency_recruitment.core.port.ExchangeRateProvider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
class InMemoryExchangeRateProvider implements ExchangeRateProvider {
    @Override
    public BigDecimal getCurrencyExchangeRate(Currency from, Currency to) {
        return new BigDecimal(2);
    }
}
