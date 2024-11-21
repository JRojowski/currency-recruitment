package io.github.JRojowski.currency_recruitment.application;

import io.github.JRojowski.currency_recruitment.api.dto.ExchangeRequestDto;
import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import io.github.JRojowski.currency_recruitment.core.port.AccountRepository;
import io.github.JRojowski.currency_recruitment.core.port.ExchangeRateProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class ExchangeCurrencyUseCase {

    private final AccountRepository accountRepository;
    private final ExchangeRateProvider exchangeRateProvider;

    public UserAccountDto execute(UUID id, ExchangeRequestDto exchangeRequestDto) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found."));

        boolean isPlnTransaction = exchangeRequestDto.getCurrency().equals(Currency.PLN);

        if (!isPlnTransaction && !account.getCurrency().equals(exchangeRequestDto.getCurrency())) {
            throw new RuntimeException("Account currency mismatch.");
        }

        BigDecimal currentBalance = isPlnTransaction ? account.getBalancePln() : account.getBalanceCurrency();

        if (currentBalance.compareTo(exchangeRequestDto.getAmount()) < 0) {
            throw new RuntimeException("Not enough money (%s).".formatted(exchangeRequestDto.getCurrency()));
        }

        BigDecimal exchangeRate = exchangeRateProvider.getCurrencyExchangeRate(
                isPlnTransaction ? Currency.PLN : exchangeRequestDto.getCurrency(),
                isPlnTransaction ? account.getCurrency() : Currency.PLN
        );

        BigDecimal exchangedAmount = exchangeRequestDto.getAmount().multiply(exchangeRate);

        if (isPlnTransaction) {
            account.setBalancePln(account.getBalancePln().subtract(exchangeRequestDto.getAmount()));
            account.setBalanceCurrency(account.getBalanceCurrency().add(exchangedAmount));
        } else {
            account.setBalanceCurrency(account.getBalanceCurrency().subtract(exchangeRequestDto.getAmount()));
            account.setBalancePln(account.getBalancePln().add(exchangedAmount));
        }

        accountRepository.save(account);

        return UserAccountDto.fromAccount(account);
    }
}
