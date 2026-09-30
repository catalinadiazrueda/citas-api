package co.com.fcv.training.citas.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

/** Authenticates narrowly scoped server-to-server automation requests using a shared secret. */
@Component
class AutomationKeyFilter extends OncePerRequestFilter {
    private final byte[] expected;

    AutomationKeyFilter(@Value("${app.automation.api-key:}") String key) {
        this.expected = key == null ? new byte[0] : key.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith(request.getContextPath() + "/api/v1/automation/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        byte[] supplied = request.getHeader("X-Automation-Key") == null
                ? new byte[0]
                : request.getHeader("X-Automation-Key").getBytes(StandardCharsets.UTF_8);
        if (expected.length < 32 || !MessageDigest.isEqual(expected, supplied)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/problem+json");
            response.getWriter().write("{\"title\":\"No autenticado\",\"status\":401}");
            return;
        }
        var authentication = new UsernamePasswordAuthenticationToken(
                "n8n-automation", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
