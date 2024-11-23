package io.github.JRojowski.currency_recruitment.application.useraccount;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.JRojowski.currency_recruitment.api.dto.ExchangeRequestDto;
import io.github.JRojowski.currency_recruitment.application.exchangerate.ExchangeRateFacade;
import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import io.github.JRojowski.currency_recruitment.core.domain.Currency;
import io.github.JRojowski.currency_recruitment.core.port.AccountRepository;
import io.github.JRojowski.currency_recruitment.core.port.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ExchangeCurrencyIntegrationTests {

    private static final UUID BANK_USER_ID = UUID.randomUUID();
    private static final UUID ACCOUNT_ID = UUID.randomUUID();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExchangeRateFacade exchangeRateFacade;

    @BeforeEach
    void setUp() {
        BankUser bankUser = new BankUser(BANK_USER_ID, "personalId", null, null, new ArrayList<>());
        Account account = new Account(ACCOUNT_ID, Currency.USD, BigDecimal.valueOf(100), BigDecimal.valueOf(100), bankUser);

        userRepository.save(bankUser);
        accountRepository.save(account);

        when(exchangeRateFacade.getExchangeRate(Currency.USD)).thenReturn(BigDecimal.valueOf(10));
    }

    @Test
    @DisplayName("Exchange PLN to USD")
    void givenPLN_whenExchangeToUSD_thenExchangeAndSaveAccount() throws Exception {
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(Currency.PLN, BigDecimal.valueOf(100));

        mockMvc.perform(put("/user/accounts/{accountId}/exchange", ACCOUNT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("personalId:password".getBytes()))
                    .content(objectMapper.writeValueAsString(exchangeRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ACCOUNT_ID.toString()))
                .andExpect(jsonPath("$.currency").value(Currency.USD.toString()))
                .andExpect(jsonPath("$.balancePln").value(BigDecimal.valueOf(0.0)))
                .andExpect(jsonPath("$.balanceCurrency").value(BigDecimal.valueOf(110.0)));

        Optional<Account> updatedAccountOptional = accountRepository.findById(ACCOUNT_ID);
        Assertions.assertThat(updatedAccountOptional).isPresent();
        Account updatedAccount = updatedAccountOptional.get();

        Assertions.assertThat(updatedAccount.getId()).isEqualTo(ACCOUNT_ID);
        Assertions.assertThat(updatedAccount.getCurrency()).isEqualTo(Currency.USD);
        Assertions.assertThat(updatedAccount.getBalancePln()).isEqualByComparingTo(BigDecimal.ZERO);
        Assertions.assertThat(updatedAccount.getBalanceCurrency()).isEqualByComparingTo(BigDecimal.valueOf(110));
    }

    @Test
    @DisplayName("Exchange USD to PLN")
    void givenUSD_whenExchangeToPLN_thenExchangeAndSaveAccount() throws Exception {
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(Currency.USD, BigDecimal.valueOf(100));

        mockMvc.perform(put("/user/accounts/{accountId}/exchange", ACCOUNT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("personalId:password".getBytes()))
                        .content(objectMapper.writeValueAsString(exchangeRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ACCOUNT_ID.toString()))
                .andExpect(jsonPath("$.currency").value(Currency.USD.toString()))
                .andExpect(jsonPath("$.balancePln").value(BigDecimal.valueOf(1100.0)))
                .andExpect(jsonPath("$.balanceCurrency").value(BigDecimal.valueOf(0.0)));

        Optional<Account> updatedAccountOptional = accountRepository.findById(ACCOUNT_ID);
        Assertions.assertThat(updatedAccountOptional).isPresent();
        Account updatedAccount = updatedAccountOptional.get();

        Assertions.assertThat(updatedAccount.getId()).isEqualTo(ACCOUNT_ID);
        Assertions.assertThat(updatedAccount.getCurrency()).isEqualTo(Currency.USD);
        Assertions.assertThat(updatedAccount.getBalancePln()).isEqualByComparingTo(BigDecimal.valueOf(1100));
        Assertions.assertThat(updatedAccount.getBalanceCurrency()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @AfterEach
    void teardown() {
        userRepository.deleteAll();
        accountRepository.deleteAll();
    }

}
