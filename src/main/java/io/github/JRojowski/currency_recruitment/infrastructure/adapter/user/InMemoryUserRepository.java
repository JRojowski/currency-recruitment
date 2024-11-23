package io.github.JRojowski.currency_recruitment.infrastructure.adapter.user;

import io.github.JRojowski.currency_recruitment.core.domain.BankUser;
import io.github.JRojowski.currency_recruitment.core.port.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Repository
class InMemoryUserRepository implements UserRepository {

    private final HashMap<UUID, BankUser> hashMap = new HashMap<>();

    @Override
    public BankUser save(BankUser bankUser) {
        hashMap.put(bankUser.getId(), bankUser);
        return bankUser;
    }

    @Override
    public Optional<BankUser> findByPersonalId(String personalId) {
        return hashMap.values().stream()
                .filter(usr -> usr.getPersonalId().equals(personalId))
                .findFirst();
    }

    @Override
    public boolean existsByPersonalId(String personalId) {
        return hashMap.values().stream()
                .anyMatch(usr -> usr.getPersonalId().equals(personalId));
    }

    @Override
    public void deleteAll() {
        hashMap.clear();
    }
}
