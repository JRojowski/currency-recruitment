package io.github.JRojowski.currency_recruitment.api.dto;

import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateUserAccountDto {
    @NotBlank(message = "PersonalId must not be empty!")
    private String personalId;
    @NotBlank(message = "Name must not be empty!")
    private String name;
    @NotBlank(message = "Surname must not be empty!")
    private String surname;
    private Currency currency;
    @DecimalMin(value = "0.00", message = "Deposit must not be less than 0.00!")
    private BigDecimal deposit;
}
