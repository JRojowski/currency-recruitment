package io.github.JRojowski.currency_recruitment.application;

import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import io.github.JRojowski.currency_recruitment.core.port.UserRepository;
import io.github.JRojowski.currency_recruitment.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class GetUserAccountsUseCase {

    private final UserRepository userRepository;

    List<UserAccountDto> execute() {
        BankUser bankUser = userRepository.findByPersonalId(SecurityUtils.getLoggedUserPersonalId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        return bankUser.getAccounts()
                .stream()
                .map(UserAccountDto::fromAccount)
                .toList();
    }
}
