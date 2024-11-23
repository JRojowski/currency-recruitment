package io.github.JRojowski.currency_recruitment.application.useraccount;

import io.github.JRojowski.currency_recruitment.api.dto.ExchangeRequestDto;
import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.application.exchangerate.ExchangeRateFacade;
import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import io.github.JRojowski.currency_recruitment.core.port.AccountRepository;
import io.github.JRojowski.currency_recruitment.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class ExchangeCurrencyUseCase {

    private final AccountRepository accountRepository;
    private final ExchangeRateFacade exchangeRateFacade;

    UserAccountDto execute(UUID id, ExchangeRequestDto exchangeRequestDto) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account not found."));

        boolean isPlnTransaction = exchangeRequestDto.getCurrency().equals(Currency.PLN);

        validateTransaction(exchangeRequestDto, account, isPlnTransaction);

        BigDecimal exchangeRate = exchangeRateFacade.getExchangeRate(account.getCurrency());

        if (exchangeRate.compareTo(BigDecimal.ZERO)  <= 0) {
            throw new IllegalArgumentException("Invalid exchange rate: must be greater than zero.");
        }

        BigDecimal exchangedAmount = calculateExchangedAmount(
                exchangeRequestDto.getAmount(),
                exchangeRate,
                isPlnTransaction
        );

        if (isPlnTransaction) {
            // from PLN to Currency
            account.setBalancePln(account.getBalancePln().subtract(exchangeRequestDto.getAmount()).setScale(2, RoundingMode.DOWN));
            account.setBalanceCurrency(account.getBalanceCurrency().add(exchangedAmount).setScale(2, RoundingMode.DOWN));
        } else {
            // from Currency to PLN
            account.setBalanceCurrency(account.getBalanceCurrency().subtract(exchangeRequestDto.getAmount()).setScale(2, RoundingMode.DOWN));
            account.setBalancePln(account.getBalancePln().add(exchangedAmount).setScale(2, RoundingMode.DOWN));
        }

        accountRepository.save(account);
        return UserAccountDto.fromAccount(account);
    }

    private void validateTransaction(ExchangeRequestDto exchangeRequestDto, Account account, boolean isPlnTransaction) {
        if (!account.getBankUser().getPersonalId().equals(SecurityUtils.getLoggedUserPersonalId())) {
            throw new IllegalArgumentException("Access denied!");
        }

        if (!isPlnTransaction && !account.getCurrency().equals(exchangeRequestDto.getCurrency())) {
            throw new IllegalArgumentException("Account currency mismatch.");
        }

        BigDecimal currentBalance = isPlnTransaction ? account.getBalancePln() : account.getBalanceCurrency();

        if (currentBalance.compareTo(exchangeRequestDto.getAmount()) < 0) {
            throw new IllegalArgumentException("Not enough money (%s).".formatted(exchangeRequestDto.getCurrency()));
        }
    }

    private BigDecimal calculateExchangedAmount(BigDecimal amount, BigDecimal rate, boolean isMultiplication) {
        if (isMultiplication) {
            return amount.divide(rate, 2, RoundingMode.DOWN);
        } else {
            return amount.multiply(rate).setScale(2, RoundingMode.DOWN);
        }
    }
}
