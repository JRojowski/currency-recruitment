package io.github.JRojowski.currency_recruitment.infrastructure.security;

import io.github.JRojowski.currency_recruitment.core.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BasicAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String personalId = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (!userRepository.existsByPersonalId(personalId)) {
           throw new AuthenticationCredentialsNotFoundException("User does not exist!");
       }

        UserDetails userDetails = User
                .withUsername(personalId)
                .password(password)
                .roles("USER")
                .build();

       return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
