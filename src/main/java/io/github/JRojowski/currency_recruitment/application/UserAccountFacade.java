package io.github.JRojowski.currency_recruitment.application;

import io.github.JRojowski.currency_recruitment.api.dto.CreateUserAccountDto;
import io.github.JRojowski.currency_recruitment.api.dto.ExchangeRequestDto;
import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@NoArgsConstructor
public class UserAccountFacade {
    public UserAccountDto createUserAccount(CreateUserAccountDto createUserAccountDto) {
        return null;
    }

    public List<UserAccountDto> getUserAccounts() {
        return new ArrayList<>();
    }

    public UserAccountDto getUserAccount(UUID id) {
        return null;
    }

    public UserAccountDto exchangeCurrency(UUID id, ExchangeRequestDto exchangeRequestDto) {
        return null;
    }
}
