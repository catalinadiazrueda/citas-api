package co.com.fcv.training.citas.config;

import co.com.fcv.training.citas.adapter.web.ProblemWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
class AuthRequestGuard extends OncePerRequestFilter {
    private final String allowedOrigin;
    private final ProblemWriter problems;

    AuthRequestGuard(@Value("${app.cors.frontend-origin}") String allowedOrigin, ProblemWriter problems) {
        this.allowedOrigin = allowedOrigin;
        this.problems = problems;
    }

    @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        boolean guarded = req.getMethod().equals("POST") &&
                (path.equals("/api/v1/auth/login") || path.equals("/api/v1/auth/refresh") || path.equals("/api/v1/auth/logout"));
        if (guarded && (!"XMLHttpRequest".equals(req.getHeader("X-Requested-With")) ||
                (req.getHeader("Origin") != null && !allowedOrigin.equals(req.getHeader("Origin"))))) {
            problems.write(res, 403, "Origen o encabezado no permitido");
            return;
        }
        chain.doFilter(req, res);
    }
}
