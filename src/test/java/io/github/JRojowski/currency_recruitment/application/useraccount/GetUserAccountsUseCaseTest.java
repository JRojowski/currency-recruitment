package io.github.JRojowski.currency_recruitment.application.useraccount;

import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import io.github.JRojowski.currency_recruitment.core.port.UserRepository;
import io.github.JRojowski.currency_recruitment.infrastructure.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.github.JRojowski.currency_recruitment.core.domain.Currency.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class GetUserAccountsUseCaseTest {

    private static final String PERSONAL_ID = "personalId";
    private static final UUID ACCOUNT_ID = UUID.randomUUID();

    @Mock
    UserRepository userRepository;
    @InjectMocks
    GetUserAccountsUseCase getUserAccountsUseCase;


    @Test
    void givenUserWithAccounts_whenExecute_thenReturnUserAccounts() {
        BankUser bankUser = new BankUser();
        bankUser.setPersonalId(PERSONAL_ID);
        Account account = new Account(ACCOUNT_ID, USD, BigDecimal.valueOf(500), BigDecimal.valueOf(100), bankUser);
        bankUser.setAccounts(List.of(account));

        when(userRepository.findByPersonalId(PERSONAL_ID))
                .thenReturn(Optional.of(bankUser));

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getLoggedUserPersonalId).thenReturn(PERSONAL_ID);

            List<UserAccountDto> result = getUserAccountsUseCase.execute();

            assertThat(result).isNotNull().hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(account.getId());
            assertThat(result.get(0).getCurrency()).isEqualTo(account.getCurrency());
            assertThat(result.get(0).getBalancePln()).isEqualTo(account.getBalancePln());
            assertThat(result.get(0).getBalanceCurrency()).isEqualTo(account.getBalanceCurrency());
        }
    }

    @Test
    void givenUserWithoutAccounts_whenExecute_thenReturnEmptyList() {
        when(userRepository.findByPersonalId(PERSONAL_ID))
                .thenReturn(Optional.of(new BankUser()));

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getLoggedUserPersonalId).thenReturn(PERSONAL_ID);

            List<UserAccountDto> result = getUserAccountsUseCase.execute();

            assertThat(result).isNotNull().isEmpty();
        }
    }
}