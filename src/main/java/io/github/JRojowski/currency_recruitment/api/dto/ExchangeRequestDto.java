package io.github.JRojowski.currency_recruitment.api.dto;

import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExchangeRequestDto {
    private Currency currency;
    private BigDecimal amount;
}
