package io.github.JRojowski.currency_recruitment.core.port;

import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import org.apache.catalina.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public interface UserRepository {

    BankUser save(BankUser bankUser);
    Optional<BankUser> findByPersonalId(String personalId);
}
