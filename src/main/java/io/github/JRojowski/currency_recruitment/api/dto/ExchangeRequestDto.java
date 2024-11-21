package io.github.JRojowski.currency_recruitment.api.dto;

import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRequestDto {
    private Currency currency;
    private BigDecimal amount;
}
