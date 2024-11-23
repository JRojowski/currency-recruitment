package io.github.JRojowski.currency_recruitment.infrastructure.adapter.exchangerate;

import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import io.github.JRojowski.currency_recruitment.core.port.ExchangeRateProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Primary
class NbpExchangeRateProvider implements ExchangeRateProvider {

    private final NbpFeignClient nbpFeignClient;

    @Override
    public BigDecimal getExchangeRate(Currency currency) {
        var response = nbpFeignClient.getExchangeRate(currency);
        if (response.rates() == null || response.rates.length == 0) {
            throw new IllegalStateException("No rates available for currency %s".formatted(currency));
        }
        return BigDecimal.valueOf(response.rates()[0].mid());
    }

    @FeignClient(name = "nbpClient", url = "${NBP_URL}")
    interface NbpFeignClient {
        @GetMapping("/exchangerates/rates/a/{currency}?format=json")
        NbpResponse getExchangeRate(@PathVariable("currency") Currency currency);

        record NbpResponse(Rate[] rates) {
            record Rate(Double mid) {}
        }
    }
}
