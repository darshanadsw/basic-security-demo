package com.example.basicsecuritydemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@PropertySource("classpath:application-test.properties")
@DataJpaTest
@Sql(scripts = "classpath:insert.sql")
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository repository;

    @Test
    void findUser(){
        Optional<Account> account = repository.findAccountByUserName("darshana");
        assertTrue(account.isPresent());
        assertEquals("darshana", account.get().getUserName());
        assertEquals("password", account.get().getPassword());
        assertTrue(account.get().getActive());
    }

    @Test
    void findAll() {
        List<Account> accounts = repository.findAll();
        assertNotNull(accounts);
        assertEquals(1, accounts.size());
    }

}
