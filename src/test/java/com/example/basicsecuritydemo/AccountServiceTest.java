package com.example.basicsecuritydemo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountUserDetailsService service;

    @Mock
    private PasswordEncoder encoder;
    @Mock
    private AccountRepository repository;

    @Test
    void loadUserByUsername() {
        when(repository.findAccountByUserName(anyString()))
                .thenReturn(Optional.of(new Account("user", "password", true)));
        when(encoder.encode(any())).thenReturn("password");

        UserDetails userDetails = service.loadUserByUsername("darshana");

        assertNotNull(userDetails);

    }

}
