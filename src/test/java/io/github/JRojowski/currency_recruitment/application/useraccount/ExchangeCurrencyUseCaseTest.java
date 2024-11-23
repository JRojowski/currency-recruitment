package io.github.JRojowski.currency_recruitment.application.useraccount;

import io.github.JRojowski.currency_recruitment.api.dto.ExchangeRequestDto;
import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.application.exchangerate.ExchangeRateFacade;
import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import io.github.JRojowski.currency_recruitment.core.port.AccountRepository;
import io.github.JRojowski.currency_recruitment.infrastructure.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ExchangeCurrencyUseCaseTest {
    private static final UUID ACCOUNT_ID = UUID.randomUUID();
    private static final Currency CURRENCY_USD = Currency.USD;
    private static final BigDecimal USD_TO_PLN_CURRENCY = BigDecimal.valueOf(4);
    private static final Currency CURRENCY_PLN = Currency.PLN;
    private static final String PERSONAL_ID = "personalId";

    @Mock
    AccountRepository accountRepository;
    @Mock
    ExchangeRateFacade exchangeRateFacade;
    @InjectMocks
    ExchangeCurrencyUseCase exchangeCurrencyUseCase;

    BankUser bankUser;

    @BeforeEach
    void setUp() {
        bankUser = new BankUser();
        bankUser.setPersonalId(PERSONAL_ID);
    }

    @Test
    void givenNoAccount_whenExecute_thenThrowNotFoundException() {
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(Currency.EUR, BigDecimal.valueOf(200));

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> exchangeCurrencyUseCase.execute(ACCOUNT_ID, exchangeRequestDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Account not found.");
    }

    @Test
    void givenOtherUserCurrencyAccount_whenExecute_thenThrowAccessDenied() {
        Account existingAccount = new Account(ACCOUNT_ID, CURRENCY_USD, null, null, bankUser);
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(Currency.EUR, BigDecimal.valueOf(200));

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(existingAccount));

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getLoggedUserPersonalId).thenReturn("Other");

            assertThatThrownBy(() -> exchangeCurrencyUseCase.execute(ACCOUNT_ID, exchangeRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Access denied!");
        }
    }

    @Test
    void givenInvalidExchangeRate_whenExecute_thenThrowAccessDenied() {
        Account existingAccount = new Account(ACCOUNT_ID, CURRENCY_USD, BigDecimal.valueOf(200), null, bankUser);
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(CURRENCY_PLN, BigDecimal.valueOf(200));

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(existingAccount));
        when(exchangeRateFacade.getExchangeRate(CURRENCY_USD))
                .thenReturn(BigDecimal.ZERO);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getLoggedUserPersonalId).thenReturn(PERSONAL_ID);

            assertThatThrownBy(() -> exchangeCurrencyUseCase.execute(ACCOUNT_ID, exchangeRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Invalid exchange rate: must be greater than zero.");
        }
    }

    @Test
    void givenCurrencyAccount_whenExecuteWithWrongCurrency_thenThrowBadRequestException() {
        Account existingAccount = new Account(ACCOUNT_ID, CURRENCY_USD, null, null, bankUser);
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(Currency.EUR, BigDecimal.valueOf(200));

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(existingAccount));

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getLoggedUserPersonalId).thenReturn(PERSONAL_ID);

            assertThatThrownBy(() -> exchangeCurrencyUseCase.execute(ACCOUNT_ID, exchangeRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Account currency mismatch.");
        }
    }

    @Test
    void givenCurrencyAccount_whenExecuteWithNotEnoughBalancePLN_thenThrowBadRequestException() {
        Account existingAccount = new Account(ACCOUNT_ID, CURRENCY_USD, BigDecimal.valueOf(100), null, bankUser);
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(Currency.PLN, BigDecimal.valueOf(200));

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(existingAccount));

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getLoggedUserPersonalId).thenReturn(PERSONAL_ID);

            assertThatThrownBy(() -> exchangeCurrencyUseCase.execute(ACCOUNT_ID, exchangeRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Not enough money (PLN).");
        }
    }

    @Test
    void givenCurrencyAccount_whenExecuteWithNotEnoughBalanceCurrency_thenThrowBadRequestException() {
        Account existingAccount = new Account(ACCOUNT_ID, CURRENCY_USD, BigDecimal.ZERO, BigDecimal.valueOf(100), bankUser);
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(CURRENCY_USD, BigDecimal.valueOf(200));

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(existingAccount));

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getLoggedUserPersonalId).thenReturn(PERSONAL_ID);

            assertThatThrownBy(() -> exchangeCurrencyUseCase.execute(ACCOUNT_ID, exchangeRequestDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Not enough money (USD).");
        }
    }

    @Test
    void givenCurrencyAccount_whenExecuteExchangeOfPln_thenExchangeSuccessful() {
        Account existingAccount = new Account(ACCOUNT_ID, CURRENCY_USD, BigDecimal.valueOf(200), BigDecimal.ZERO, bankUser);
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(Currency.PLN, BigDecimal.valueOf(100));

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(existingAccount));
        when(exchangeRateFacade.getExchangeRate(CURRENCY_USD))
                .thenReturn(USD_TO_PLN_CURRENCY);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getLoggedUserPersonalId).thenReturn(PERSONAL_ID);

            UserAccountDto result = exchangeCurrencyUseCase.execute(ACCOUNT_ID, exchangeRequestDto);

            assertThat(result).hasNoNullFieldsOrProperties();
            assertThat(result.getId()).isEqualTo(existingAccount.getId());
            assertThat(result.getCurrency()).isEqualTo(existingAccount.getCurrency());
            assertThat(result.getBalancePln()).isEqualByComparingTo(BigDecimal.valueOf(100));
            assertThat(result.getBalanceCurrency()).isEqualByComparingTo(BigDecimal.valueOf(25));
        }
    }

    @Test
    void givenCurrencyAccount_whenExecuteExchangeOfUsd_thenExchangeSuccessful() {
        Account existingAccount = new Account(ACCOUNT_ID, CURRENCY_USD, BigDecimal.ZERO, BigDecimal.valueOf(200), bankUser);
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(CURRENCY_USD, BigDecimal.valueOf(100));

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(existingAccount));
        when(exchangeRateFacade.getExchangeRate(CURRENCY_USD))
                .thenReturn(USD_TO_PLN_CURRENCY);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getLoggedUserPersonalId).thenReturn(PERSONAL_ID);

            UserAccountDto result = exchangeCurrencyUseCase.execute(ACCOUNT_ID, exchangeRequestDto);

            assertThat(result).hasNoNullFieldsOrProperties();
            assertThat(result.getId()).isEqualTo(existingAccount.getId());
            assertThat(result.getCurrency()).isEqualTo(existingAccount.getCurrency());
            assertThat(result.getBalancePln()).isEqualByComparingTo(BigDecimal.valueOf(400));
            assertThat(result.getBalanceCurrency()).isEqualByComparingTo(BigDecimal.valueOf(100));
        }
    }
}