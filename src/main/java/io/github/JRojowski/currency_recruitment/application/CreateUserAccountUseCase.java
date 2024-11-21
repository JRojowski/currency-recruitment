package io.github.JRojowski.currency_recruitment.application;

import io.github.JRojowski.currency_recruitment.api.dto.CreateUserAccountDto;
import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import io.github.JRojowski.currency_recruitment.core.port.AccountRepository;
import io.github.JRojowski.currency_recruitment.core.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class CreateUserAccountUseCase {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public UserAccountDto execute(CreateUserAccountDto createUserAccountDto) {
        BankUser bankUser = userRepository
                .findByPersonalId(createUserAccountDto.getPersonalId())
                .orElseGet(() -> userRepository.save(BankUser.fromCreateDto(createUserAccountDto)));

        boolean accountAlreadyExists = bankUser.getAccounts()
                .stream()
                .anyMatch(acc -> acc.getCurrency().equals(createUserAccountDto.getCurrency()));

        if (!accountAlreadyExists) {
            Account account = Account.fromCreateDto(createUserAccountDto);
            account.setBankUser(bankUser);
            return UserAccountDto.fromAccount(accountRepository.save(account));
        }

        throw new RuntimeException("Account with such currency already exists.");
    }

}
