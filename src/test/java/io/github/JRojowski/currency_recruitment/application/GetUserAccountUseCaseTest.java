package io.github.JRojowski.currency_recruitment.application;

import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import io.github.JRojowski.currency_recruitment.core.port.AccountRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static io.github.JRojowski.currency_recruitment.core.domain.Currency.USD;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class GetUserAccountUseCaseTest {

    private static final UUID ACCOUNT_ID = UUID.randomUUID();

    @Mock
    AccountRepository accountRepository;
    @InjectMocks
    GetUserAccountUseCase getUserAccountUseCase;

    @Test
    void givenNoAccount_whenExecute_thenThrowNotFoundException() {
        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> getUserAccountUseCase.execute(ACCOUNT_ID))
                .isInstanceOf(ClassNotFoundException.class)
                .hasMessage("Account not found.");
    }

    @Test
    void givenAccount_whenExecute_thenReturnCorrectResponse() {
        Account account = new Account(ACCOUNT_ID, USD, new BigDecimal(500), new BigDecimal(100), new BankUser());

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(account));

        UserAccountDto result = getUserAccountUseCase.execute(ACCOUNT_ID);

        assertThat(result).hasNoNullFieldsOrProperties();
        assertThat(result.id()).isEqualTo(account.getId());
        assertThat(result.currency()).isEqualTo(account.getCurrency());
        assertThat(result.balancePln()).isEqualTo(account.getBalancePln());
        assertThat(result.balanceCurrency()).isEqualTo(account.getBalanceCurrency());
    }

}