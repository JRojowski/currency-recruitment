package io.github.JRojowski.currency_recruitment.api.controller;

import io.github.JRojowski.currency_recruitment.api.dto.CreateUserAccountDto;
import io.github.JRojowski.currency_recruitment.api.dto.UserAccountDto;
import io.github.JRojowski.currency_recruitment.api.dto.ExchangeRequestDto;
import io.github.JRojowski.currency_recruitment.application.UserAccountFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
            @ApiResponse(responseCode = "400", description = "BadRequestException")
    })
    public ResponseEntity<UserAccountDto> createUserAccount(@RequestBody @Valid CreateUserAccountDto createUserAccountDto) {
        UserAccountDto userAccount = userAccountFacade.createUserAccount(createUserAccountDto);
        return ResponseEntity.created(URI.create(userAccount.getId().toString())).body(userAccount);
    }

    @GetMapping()
    @Operation(summary = "Get users account")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Successful response"))
    public ResponseEntity<List<UserAccountDto>> getUserAccounts() {
        List<UserAccountDto> userAccounts = userAccountFacade.getUserAccounts();
        return ResponseEntity.ok(userAccounts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user account by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NotFoundException")
    })
    public ResponseEntity<UserAccountDto> getUserAccount(@PathVariable UUID id) {
        UserAccountDto userAccount = userAccountFacade.getUserAccount(id);
        return ResponseEntity.ok(userAccount);
    }

    @PutMapping("/{id}/exchange")
    @Operation(summary = "Get user account by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "NotFoundException")
    })
    public ResponseEntity<UserAccountDto> getUseAccount(
            @PathVariable UUID id,
            @RequestBody @Valid ExchangeRequestDto exchangeRequestDto
    ) {
        UserAccountDto userAccount = userAccountFacade.exchangeCurrency(id, exchangeRequestDto);
        return ResponseEntity.ok(userAccount);
    }

}
