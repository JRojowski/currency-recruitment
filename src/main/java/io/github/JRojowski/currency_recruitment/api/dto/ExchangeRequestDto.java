package io.github.JRojowski.currency_recruitment.api.dto;

import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExchangeRequestDto {
    private Currency currency;
    @DecimalMin(value = "0.00", message = "Exchanged amount must not be less than 0.00!")
    private BigDecimal amount;
}
