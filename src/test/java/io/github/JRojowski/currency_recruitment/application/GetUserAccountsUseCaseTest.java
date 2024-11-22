package io.github.JRojowski.currency_recruitment.application;

import io.github.JRojowski.currency_recruitment.core.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class GetUserAccountsUseCaseTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    GetUserAccountsUseCase getUserAccountsUseCase;

    @Test
    void givenUserWithAccounts_whenExecute_thenReturnUserAccounts() {
        // TODO -> implement after adding security
    }

    @Test
    void givenUserWithoutAccounts_whenExecute_thenReturnEmptyList() {
        // TODO -> implement after adding security
    }
}