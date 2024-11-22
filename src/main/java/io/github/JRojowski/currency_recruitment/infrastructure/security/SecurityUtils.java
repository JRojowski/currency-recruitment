package io.github.JRojowski.currency_recruitment.infrastructure.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

    public static String getLoggedUserPersonalId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
