package io.github.JRojowski.currency_recruitment.application.useraccount;

import io.github.JRojowski.currency_recruitment.api.dto.CreateUserAccountDto;
import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import io.github.JRojowski.currency_recruitment.core.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CreateUserAccountUseCase {

    private final UserRepository userRepository;

    UserAccountDto execute(CreateUserAccountDto createUserAccountDto) {
        BankUser bankUser = userRepository
                .findByPersonalId(createUserAccountDto.getPersonalId())
                .orElseGet(() -> BankUser.fromCreateDto(createUserAccountDto));

        if (hasAccountWithCurrency(bankUser, createUserAccountDto.getCurrency())) {
            throw new IllegalArgumentException("Account with currency " + createUserAccountDto.getCurrency() + " already exists.");
        }

        Account account = Account.fromCreateDto(createUserAccountDto);
        account.setBankUser(bankUser);
        bankUser.getAccounts().add(account);
        userRepository.save(bankUser);

        return UserAccountDto.fromAccount(account);
    }

    private boolean hasAccountWithCurrency(BankUser bankUser, Currency currency) {
        return bankUser.getAccounts()
                .stream()
                .anyMatch(account -> account.getCurrency().equals(currency));
    }

}
