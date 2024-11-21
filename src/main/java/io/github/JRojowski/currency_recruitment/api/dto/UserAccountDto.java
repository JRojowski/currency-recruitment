package io.github.JRojowski.currency_recruitment.api.dto;

import io.github.JRojowski.currency_recruitment.core.domain.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public record UserAccountDto(UUID id, Currency currency, BigDecimal balancePln, BigDecimal balanceCurrency) {
}
