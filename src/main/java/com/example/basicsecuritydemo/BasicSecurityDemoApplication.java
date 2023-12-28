package com.example.basicsecuritydemo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
public class BasicSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicSecurityDemoApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}

@Component
class InitApp implements CommandLineRunner {

    private final AccountRepository repository;

    InitApp(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        List.of("darshana,sampath", "hasaru,welikala", "piumine,kuruppu")
                .stream().map(s -> s.split(","))
                .forEach(tuple -> {
                    repository.save(new Account(tuple[0], tuple[1], true));
                });
    }
}


@RestController
class UserController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/hi")
    public Map<String, String> hello(Principal p) {
        return Map.of("content", "Hello " + p.getName());
    }

    @GetMapping(value = "/accounts", produces = "application/json")
    public Iterable<Account> allAccounts() {
        return accountRepository.findAll();
    }
}

@Service
class AccountUserDetailsService implements UserDetailsService {

    private final AccountRepository userRepository;
    private final PasswordEncoder encoder;

    AccountUserDetailsService(AccountRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findAccountByUserName(username).map(user -> new User(user.getUserName(),
                        encoder.encode(user.getPassword()),
                        user.getActive(),
                        user.getActive(),
                        user.getActive(),
                        user.getActive(),
                        AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER")))
                .orElseThrow(() -> new UsernameNotFoundException("User name or password not found"));
    }
}

interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findAccountByUserName(String username);
    List<Account> findAll();
}

@Entity
class Account {

    @Id
    @GeneratedValue
    private Long id;
    private String userName;
    private String password;
    private Boolean isActive;

    public Account(String userName, String password, Boolean isActive) {
        this.userName = userName;
        this.password = password;
        this.isActive = isActive;
    }

    public Account() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
