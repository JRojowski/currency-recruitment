package io.github.JRojowski.currency_recruitment.application;

import io.github.JRojowski.currency_recruitment.api.dto.ExchangeRequestDto;
import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import io.github.JRojowski.currency_recruitment.core.port.AccountRepository;
import io.github.JRojowski.currency_recruitment.core.port.ExchangeRateProvider;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ExchangeCurrencyUseCaseTest {
    private static final UUID ACCOUNT_ID = UUID.randomUUID();
    private static final Currency CURRENCY_USD = Currency.USD;
    private static final BigDecimal PLN_TO_USD_CURRENCY = new BigDecimal("0.5");
    private static final BigDecimal USD_TO_PLN_CURRENCY = new BigDecimal(4);
    public static final Currency CURRENCY_PLN = Currency.PLN;

    @Mock
    AccountRepository accountRepository;
    @Mock
    ExchangeRateProvider exchangeRateProvider;
    @InjectMocks
    ExchangeCurrencyUseCase exchangeCurrencyUseCase;

    @Test
    void givenNoAccount_whenExecute_thenThrowNotFoundException() {
        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> exchangeCurrencyUseCase.execute(ACCOUNT_ID, null))
                .isInstanceOf(ClassNotFoundException.class)
                .hasMessage("Account not found.");
    }

    @Test
    void givenCurrencyAccount_whenExecuteWithWrongCurrency_thenThrowBadRequestException() {
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(Currency.EUR, new BigDecimal(200));
        Account existingAccount = new Account(ACCOUNT_ID, CURRENCY_USD, null, null, null);

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(existingAccount));

        assertThatThrownBy(() -> exchangeCurrencyUseCase.execute(ACCOUNT_ID, exchangeRequestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Account is of different currency.");
    }

    @Test
    void givenCurrencyAccount_whenExecuteWithNotEnoughBalancePLN_thenThrowBadRequestException() {
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(Currency.PLN, new BigDecimal(200));
        Account existingAccount = new Account(ACCOUNT_ID, CURRENCY_USD, new BigDecimal(100), null, null);

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(existingAccount));

        assertThatThrownBy(() -> exchangeCurrencyUseCase.execute(ACCOUNT_ID, exchangeRequestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Not enough money (PLN).");
    }

    @Test
    void givenCurrencyAccount_whenExecuteWithNotEnoughBalanceCurrency_thenThrowBadRequestException() {
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(CURRENCY_USD, new BigDecimal(200));
        Account existingAccount = new Account(ACCOUNT_ID, CURRENCY_USD, BigDecimal.ZERO, new BigDecimal(100), null);

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(existingAccount));

        assertThatThrownBy(() -> exchangeCurrencyUseCase.execute(ACCOUNT_ID, exchangeRequestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Not enough money (USD).");
    }

    @Test
    void givenCurrencyAccount_whenExecuteExchangeOfPln_thenExchangeSuccessful() {
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(Currency.PLN, new BigDecimal(100));
        Account existingAccount = new Account(ACCOUNT_ID, CURRENCY_USD, new BigDecimal(200), BigDecimal.ZERO, null);

        BigDecimal expectedAmountPln = existingAccount.getBalancePln().subtract(exchangeRequestDto.getAmount());
        BigDecimal expectedAmountCurrency = new BigDecimal(200).multiply(PLN_TO_USD_CURRENCY);

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(existingAccount));
        when(exchangeRateProvider.getCurrencyExchangeRate(Currency.PLN, CURRENCY_USD))
                .thenReturn(PLN_TO_USD_CURRENCY);

        UserAccountDto result = exchangeCurrencyUseCase.execute(ACCOUNT_ID, exchangeRequestDto);

        assertThat(result).hasNoNullFieldsOrProperties();
        assertThat(result.id()).isEqualTo(existingAccount.getId());
        assertThat(result.currency()).isEqualTo(existingAccount.getCurrency());
        assertThat(result.balancePln()).isEqualTo(expectedAmountPln);
        assertThat(result.balanceCurrency()).isEqualTo(expectedAmountCurrency);
    }

    @Test
    void givenCurrencyAccount_whenExecuteExchangeOfUsd_thenExchangeSuccessful() {
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(CURRENCY_USD, new BigDecimal(100));
        Account existingAccount = new Account(ACCOUNT_ID, CURRENCY_USD, BigDecimal.ZERO, new BigDecimal(200), null);

        BigDecimal expectedAmountPln = new BigDecimal(200).multiply(USD_TO_PLN_CURRENCY);
        BigDecimal expectedAmountCurrency = existingAccount.getBalanceCurrency().subtract(exchangeRequestDto.getAmount());

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(existingAccount));
        when(exchangeRateProvider.getCurrencyExchangeRate(CURRENCY_USD, CURRENCY_PLN))
                .thenReturn(USD_TO_PLN_CURRENCY);

        UserAccountDto result = exchangeCurrencyUseCase.execute(ACCOUNT_ID, exchangeRequestDto);

        assertThat(result).hasNoNullFieldsOrProperties();
        assertThat(result.id()).isEqualTo(existingAccount.getId());
        assertThat(result.currency()).isEqualTo(existingAccount.getCurrency());
        assertThat(result.balancePln()).isEqualTo(expectedAmountPln);
        assertThat(result.balanceCurrency()).isEqualTo(expectedAmountCurrency);
    }
}