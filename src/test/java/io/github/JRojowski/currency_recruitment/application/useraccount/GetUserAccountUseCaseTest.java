package io.github.JRojowski.currency_recruitment.application.useraccount;

import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import io.github.JRojowski.currency_recruitment.core.port.AccountRepository;
import io.github.JRojowski.currency_recruitment.infrastructure.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static io.github.JRojowski.currency_recruitment.core.domain.Currency.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class GetUserAccountUseCaseTest {

    private static final UUID ACCOUNT_ID = UUID.randomUUID();
    private static final String PERSONAL_ID = "personalId";

    @Mock
    AccountRepository accountRepository;
    @InjectMocks
    GetUserAccountUseCase getUserAccountUseCase;


    @Test
    void givenNoAccount_whenExecute_thenThrowNotFoundException() {
        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> getUserAccountUseCase.execute(ACCOUNT_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account not found.");
    }

    @Test
    void givenOtherUserAccount_whenExecute_thenThrowAccessDenied() {
        BankUser bankUser = new BankUser();
        bankUser.setPersonalId("Other");
        Account account = new Account(ACCOUNT_ID, USD, BigDecimal.valueOf(500), BigDecimal.valueOf(100), bankUser);

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(account));

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getLoggedUserPersonalId).thenReturn(PERSONAL_ID);

            assertThatThrownBy(() -> getUserAccountUseCase.execute(ACCOUNT_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Access denied!");
        }
    }

    @Test
    void givenAccount_whenExecute_thenReturnCorrectResponse() {
        BankUser bankUser = new BankUser();
        bankUser.setPersonalId(PERSONAL_ID);
        Account account = new Account(ACCOUNT_ID, USD, BigDecimal.valueOf(500), BigDecimal.valueOf(100), bankUser);

        when(accountRepository.findById(ACCOUNT_ID))
                .thenReturn(Optional.of(account));

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getLoggedUserPersonalId).thenReturn(PERSONAL_ID);

            UserAccountDto result = getUserAccountUseCase.execute(ACCOUNT_ID);

            assertThat(result).hasNoNullFieldsOrProperties();
            assertThat(result.getId()).isEqualTo(account.getId());
            assertThat(result.getCurrency()).isEqualTo(account.getCurrency());
            assertThat(result.getBalancePln()).isEqualTo(account.getBalancePln());
            assertThat(result.getBalanceCurrency()).isEqualTo(account.getBalanceCurrency());
        }
    }
}