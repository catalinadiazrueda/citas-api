package co.com.fcv.training.citas.adapter.web;

import co.com.fcv.training.citas.application.AuthFailure;
import co.com.fcv.training.citas.application.AuthService;
import co.com.fcv.training.citas.domain.Account;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {
    record RegisterRequest(@NotBlank @Size(max = 120) String firstName,
                           @NotBlank @Size(max = 120) String lastName,
                           @NotBlank @Size(max = 30) String documentType,
                           @NotBlank @Size(max = 80) String documentNumber,
                           @NotBlank @Size(max = 254) String email,
                           @NotBlank @Size(max = 40) String phone,
                           @NotBlank String password, Long insurancePlanId) {}
    record RegisterResponse(Long id, String firstName, String lastName, String documentType,
                            String documentNumber, String email, String phone, String role) {}
    record LoginRequest(@NotBlank String email, @NotBlank String password) {}
    record AccessResponse(String accessToken, String tokenType, long expiresIn) {}

    private final AuthService auth;
    private final boolean secureCookie;
    private final String sameSite;
    private final long refreshDays;

    AuthController(AuthService auth, @Value("${app.cookie.secure}") boolean secureCookie,
                   @Value("${app.cookie.same-site}") String sameSite,
                   @Value("${app.jwt.refresh-days}") long refreshDays) {
        this.auth = auth;
        this.secureCookie = secureCookie;
        this.sameSite = sameSite;
        this.refreshDays = refreshDays;
    }

    @PostMapping("/register")
    ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        Account account = auth.register(new AuthService.Registration(request.firstName(), request.lastName(),
                request.documentType(), request.documentNumber(), request.email(), request.phone(), request.password(), request.insurancePlanId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse(account.id(), account.firstName(),
                account.lastName(), account.documentType(), account.documentNumber(), account.email(), account.phone(), "USER"));
    }

    @PostMapping("/login")
    ResponseEntity<AccessResponse> login(@Valid @RequestBody LoginRequest request) {
        return tokenResponse(auth.login(request.email(), request.password()));
    }

    @PostMapping("/refresh")
    ResponseEntity<AccessResponse> refresh(@CookieValue(name = "refresh_token", required = false) String token) {
        if (token == null) throw new AuthFailure();
        return tokenResponse(auth.refresh(token));
    }

    @PostMapping("/logout")
    ResponseEntity<Void> logout(@CookieValue(name = "refresh_token", required = false) String token) {
        auth.logout(token);
        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie("", Duration.ZERO)).build();
    }

    private ResponseEntity<AccessResponse> tokenResponse(AuthService.Tokens tokens) {
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        cookie(tokens.refreshToken(), Duration.ofDays(refreshDays)))
                .body(new AccessResponse(tokens.accessToken(), "Bearer", tokens.expiresIn()));
    }

    private String cookie(String value, Duration age) {
        return ResponseCookie.from("refresh_token", value).httpOnly(true).secure(secureCookie)
                .sameSite(sameSite).path("/api/v1/auth").maxAge(age).build().toString();
    }
}
