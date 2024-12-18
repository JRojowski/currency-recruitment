package io.github.JRojowski.currency_recruitment.core.domain;

import io.github.JRojowski.currency_recruitment.api.dto.CreateUserAccountDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankUser {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    private String personalId;
    private String name;
    private String surname;

    @OneToMany(mappedBy = "bankUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    public static BankUser fromCreateDto(CreateUserAccountDto createUserAccountDto) {
        BankUser bankUser = new BankUser();
        bankUser.setId(UUID.randomUUID());
        bankUser.setPersonalId(createUserAccountDto.getPersonalId());
        bankUser.setName(createUserAccountDto.getName());
        bankUser.setSurname(createUserAccountDto.getSurname());
        return bankUser;
    }

}
