package io.github.JRojowski.currency_recruitment.application.useraccount;

import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.port.AccountRepository;
import io.github.JRojowski.currency_recruitment.infrastructure.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class GetUserAccountUseCase {

    private final AccountRepository accountRepository;

    UserAccountDto execute(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));

        if (!account.getBankUser().getPersonalId().equals(SecurityUtils.getLoggedUserPersonalId())) {
            throw new IllegalArgumentException("Access denied!");
        }

        return UserAccountDto.fromAccount(account);
    }
}
