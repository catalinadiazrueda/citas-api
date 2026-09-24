package co.com.fcv.training.citas.config;

import co.com.fcv.training.citas.application.AuthService;
import co.com.fcv.training.citas.application.Ports;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;
import java.time.Clock;

@Configuration
class ApplicationConfig {
    @Bean Clock clock() { return Clock.systemUTC(); }

    @Bean Ports.Passwords passwords() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return new Ports.Passwords() {
            public String hash(String raw) { return encoder.encode(raw); }
            public boolean matches(String raw, String hash) { return encoder.matches(raw, hash); }
        };
    }

    @Bean Ports.Transactions transactions(TransactionTemplate template) {
        return new Ports.Transactions() {
            public <T> T run(java.util.function.Supplier<T> work) {
                return template.execute(status -> work.get());
            }
        };
    }

    @Bean AuthService authService(Ports.Accounts accounts, Ports.Sessions sessions, Ports.Passwords passwords,
                                  Ports.Tokens tokens, Ports.Transactions transactions, Clock clock, Ports.Affiliations affiliations) {
        return new AuthService(accounts, sessions, passwords, tokens, transactions, clock, affiliations);
    }
}
