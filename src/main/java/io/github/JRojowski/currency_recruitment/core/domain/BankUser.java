package io.github.JRojowski.currency_recruitment.core.domain;

import io.github.JRojowski.currency_recruitment.api.dto.CreateUserAccountDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    private String personalId;
    private String name;
    private String surname;

    @OneToMany(mappedBy = "bankUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    public static BankUser fromCreateDto(CreateUserAccountDto createUserAccountDto) {
        BankUser bankUser = new BankUser();
        bankUser.setPersonalId(createUserAccountDto.getPersonalId());
        bankUser.setName(createUserAccountDto.getName());
        bankUser.setSurname(createUserAccountDto.getSurname());
        return bankUser;
    }

}
