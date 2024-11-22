package io.github.JRojowski.currency_recruitment.application;

import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import io.github.JRojowski.currency_recruitment.core.port.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static io.github.JRojowski.currency_recruitment.core.domain.Currency.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Account not found.");
    }

    @Test
    void givenAccount_whenExecute_thenReturnCorrectResponse() {
        Account account = new Account(ACCOUNT_ID, USD, new BigDecimal(500), new BigDecimal(100), new BankUser());

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(account));

        UserAccountDto result = getUserAccountUseCase.execute(ACCOUNT_ID);

        assertThat(result).hasNoNullFieldsOrProperties();
        assertThat(result.getId()).isEqualTo(account.getId());
        assertThat(result.getCurrency()).isEqualTo(account.getCurrency());
        assertThat(result.getBalancePln()).isEqualTo(account.getBalancePln());
        assertThat(result.getBalanceCurrency()).isEqualTo(account.getBalanceCurrency());
    }

}