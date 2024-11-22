package io.github.JRojowski.currency_recruitment.application;

import io.github.JRojowski.currency_recruitment.api.dto.CreateUserAccountDto;
import io.github.JRojowski.currency_recruitment.api.dto.ExchangeRequestDto;
import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAccountFacade {

    private final CreateUserAccountUseCase createUserAccountUseCase;
    private final ExchangeCurrencyUseCase exchangeCurrencyUseCase;
    private final GetUserAccountsUseCase getUserAccountsUseCase;
    private final GetUserAccountUseCase getUserAccountUseCase;

    public UserAccountDto createUserAccount(CreateUserAccountDto createUserAccountDto) {
        return createUserAccountUseCase.execute(createUserAccountDto);
    }

    public List<UserAccountDto> getUserAccounts() {
        return getUserAccountsUseCase.execute();
    }

    public UserAccountDto getUserAccount(UUID id) {
        return getUserAccountUseCase.execute(id);
    }

    public UserAccountDto exchangeCurrency(UUID id, ExchangeRequestDto exchangeRequestDto) {
        return exchangeCurrencyUseCase.execute(id, exchangeRequestDto);
    }
}
