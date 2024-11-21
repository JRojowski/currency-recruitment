package io.github.JRojowski.currency_recruitment.application;

import io.github.JRojowski.currency_recruitment.api.dto.ExchangeRequestDto;
import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.core.port.AccountRepository;
import io.github.JRojowski.currency_recruitment.core.port.ExchangeRateProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class ExchangeCurrencyUseCase {

    private final AccountRepository accountRepository;
    private final ExchangeRateProvider exchangeRateProvider;

    public UserAccountDto execute(UUID id, ExchangeRequestDto exchangeRequestDto) {
        return null;
    }
}
