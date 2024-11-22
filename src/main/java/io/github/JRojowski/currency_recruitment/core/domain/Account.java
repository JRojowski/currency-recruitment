package io.github.JRojowski.currency_recruitment.core.domain;

import io.github.JRojowski.currency_recruitment.api.dto.CreateUserAccountDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Currency currency;
    private BigDecimal balancePln;
    private BigDecimal balanceCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private BankUser bankUser;


    public static Account fromCreateDto(CreateUserAccountDto createUserAccountDto) {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setCurrency(createUserAccountDto.getCurrency());
        account.setBalancePln(createUserAccountDto.getDeposit());
        account.setBalanceCurrency(BigDecimal.ZERO);
        return account;
    }
}
