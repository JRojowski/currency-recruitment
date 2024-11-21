package io.github.JRojowski.currency_recruitment.api.dto;

import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserAccountDto {
    private UUID id;
    private Currency currency;
    private BigDecimal balancePln;
    private BigDecimal balanceCurrency;

    public static UserAccountDto fromAccount(Account account) {
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setId(account.getId());
        userAccountDto.setCurrency(account.getCurrency());
        userAccountDto.setBalancePln(account.getBalancePln());
        userAccountDto.setBalanceCurrency(account.getBalanceCurrency());
        return userAccountDto;
    }


}
