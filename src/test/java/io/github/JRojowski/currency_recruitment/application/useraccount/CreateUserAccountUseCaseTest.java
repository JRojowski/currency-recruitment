package io.github.JRojowski.currency_recruitment.application.useraccount;

import io.github.JRojowski.currency_recruitment.api.dto.CreateUserAccountDto;
import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import io.github.JRojowski.currency_recruitment.core.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CreateUserAccountUseCaseTest {

    private static final String PERSONAL_ID = "97051032562";
    private static final String FORENAME = "Forename";
    private static final String SURNAME = "Surname";
    private static final Currency CURRENCY = Currency.USD;
    private static final BigDecimal DEPOSIT = BigDecimal.valueOf(200);

    @Mock
    UserRepository userRepository;
    @InjectMocks
    CreateUserAccountUseCase createUserAccountUseCase;

    @Test
    void givenNewUserAndNewAccount_whenExecute_thenSaveNewUserAndNewAccount() {
        CreateUserAccountDto createUserAccountDto = createUserAccountDto();

        when(userRepository.save(any()))
                .thenReturn(new BankUser());

        UserAccountDto result = createUserAccountUseCase.execute(createUserAccountDto);

        verify(userRepository, times(1)).save(any(BankUser.class));

        assertThat(result.getCurrency()).isEqualTo(createUserAccountDto.getCurrency());
        assertThat(result.getBalancePln()).isEqualTo(createUserAccountDto.getDeposit());
        assertThat(result.getBalanceCurrency()).isZero();
    }

    @Test
    void givenExistingUserAndNewAccount_whenExecute_thenSaveOnlyNewAccount() {
        CreateUserAccountDto createUserAccountDto = createUserAccountDto();
        when(userRepository.findByPersonalId(any()))
                .thenReturn(Optional.of(new BankUser()));

        UserAccountDto result = createUserAccountUseCase.execute(createUserAccountDto);

        assertThat(result.getCurrency()).isEqualTo(createUserAccountDto.getCurrency());
        assertThat(result.getBalancePln()).isEqualTo(createUserAccountDto.getDeposit());
        assertThat(result.getBalanceCurrency()).isZero();
    }

    @Test
    void givenExistingUserWithCurrencyAccount_whenExecuteWithSameCurrency_thenThrowBadRequestException() {
        CreateUserAccountDto createUserAccountDto = createUserAccountDto();
        BankUser existingUser = new BankUser();
        Account existingAccount = new Account(UUID.randomUUID(), CURRENCY, null, null, existingUser);
        existingUser.setAccounts(List.of(existingAccount));

        when(userRepository.findByPersonalId(any()))
                .thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> createUserAccountUseCase.execute(createUserAccountDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account with currency USD already exists.");
    }

    private CreateUserAccountDto createUserAccountDto() {
        return CreateUserAccountDto.builder()
                .personalId(PERSONAL_ID)
                .name(FORENAME)
                .surname(SURNAME)
                .currency(CURRENCY)
                .deposit(DEPOSIT)
                .build();
    }
}