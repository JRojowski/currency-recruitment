package io.github.JRojowski.currency_recruitment.core.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Currency {
    PLN("PLN"),
    USD("USD"),
    EUR("EUR"),
    CHF("CHF");

    private final String code;
}
