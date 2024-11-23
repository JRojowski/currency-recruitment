package io.github.JRojowski.currency_recruitment.core.port;

import io.github.JRojowski.currency_recruitment.core.domain.BankUser;

import java.util.Optional;

public interface UserRepository {

    BankUser save(BankUser entity);
    Optional<BankUser> findByPersonalId(String personalId);
    boolean existsByPersonalId(String personalId);
    void deleteAll();
}
