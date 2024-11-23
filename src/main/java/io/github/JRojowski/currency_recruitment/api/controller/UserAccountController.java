package io.github.JRojowski.currency_recruitment.api.controller;

import io.github.JRojowski.currency_recruitment.api.dto.CreateUserAccountDto;
import io.github.JRojowski.currency_recruitment.api.dto.ExchangeRequestDto;
import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.application.useraccount.UserAccountFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/accounts")
@RequiredArgsConstructor
class UserAccountController {

    private final UserAccountFacade userAccountFacade;

    @PostMapping("/create")
    @Operation(summary = "Add new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful response"),
            @ApiResponse(responseCode = "400", description = "BadRequest")
    })
    public ResponseEntity<UserAccountDto> createUserAccount(@RequestBody @Valid CreateUserAccountDto createUserAccountDto) {
        UserAccountDto userAccount = userAccountFacade.createUserAccount(createUserAccountDto);
        return ResponseEntity.created(URI.create(userAccount.getId().toString())).body(userAccount);
    }

    @GetMapping()
    @Operation(summary = "Get the accounts for the currently logged-in user")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Successful response"))
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<List<UserAccountDto>> getUserAccounts() {
        List<UserAccountDto> userAccounts = userAccountFacade.getUserAccounts();
        return ResponseEntity.ok(userAccounts);
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get the account by its id for the currently logged-in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NotFoundException")
    })
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<UserAccountDto> getUserAccountById(@PathVariable UUID accountId) {
        UserAccountDto userAccount = userAccountFacade.getUserAccount(accountId);
        return ResponseEntity.ok(userAccount);
    }

    @PutMapping("/{accountId}/exchange")
    @Operation(summary = "Exchange the currency for the currently logged-in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "400", description = "BadRequest"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NotFoundException")
    })
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<UserAccountDto> exchangeCurrency(
            @PathVariable UUID accountId,
            @RequestBody @Valid ExchangeRequestDto exchangeRequestDto
    ) {
        UserAccountDto userAccount = userAccountFacade.exchangeCurrency(accountId, exchangeRequestDto);
        return ResponseEntity.ok(userAccount);
    }

}
