package io.github.JRojowski.currency_recruitment.application;

import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import io.github.JRojowski.currency_recruitment.core.port.ExchangeRateProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ExchangeRateFacade {

    private final ExchangeRateProvider exchangeRateProvider;

    public BigDecimal getExchangeRate(Currency currency) {
        return exchangeRateProvider.getExchangeRate(currency);
    }
}
