package io.github.JRojowski.currency_recruitment.api.dto;

import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateUserAccountDto {
    private String personal_id;
    private String name;
    private String surname;
    private Currency currency;
    private BigDecimal deposit;
}
