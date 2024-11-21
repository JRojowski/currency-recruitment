package io.github.JRojowski.currency_recruitment.application;

import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import io.github.JRojowski.currency_recruitment.core.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class GetUserAccountsUseCase {

    private final UserRepository userRepository;

    public List<UserAccountDto> execute(String personalId) {
        BankUser bankUser = userRepository.findByPersonalId(personalId)
                .orElseThrow(() -> new RuntimeException("Account not found."));

        return bankUser.getAccounts()
                .stream()
                .map(UserAccountDto::fromAccount)
                .toList();
    }
}
