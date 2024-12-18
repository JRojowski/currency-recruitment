package io.github.JRojowski.currency_recruitment.infrastructure.adapter.account;

import io.github.JRojowski.currency_recruitment.core.domain.Account;
import io.github.JRojowski.currency_recruitment.core.port.AccountRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Primary
@SuppressWarnings("unused")
interface AccountRepositoryImpl extends AccountRepository, JpaRepository<Account, UUID> {
}
