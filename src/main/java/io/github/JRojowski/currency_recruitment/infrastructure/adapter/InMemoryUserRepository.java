package io.github.JRojowski.currency_recruitment.infrastructure.adapter;

import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import io.github.JRojowski.currency_recruitment.core.port.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class InMemoryUserRepository implements UserRepository {
    @Override
    public BankUser save(BankUser bankUser) {
        return null;
    }

    @Override
    public Optional<BankUser> findByPersonalId(String personalId) {
        return Optional.empty();
    }
}
