package co.com.fcv.training.citas.config;

import co.com.fcv.training.citas.adapter.web.ProblemWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import static org.assertj.core.api.Assertions.*;

class AuthRequestGuardTest {
    @Test void rejectsCookieOperationWithoutCustomHeader() throws Exception {
        var guard = new AuthRequestGuard("https://web.example", new ProblemWriter(new ObjectMapper()));
        var request = new MockHttpServletRequest("POST", "/api/v1/auth/refresh");
        var response = new MockHttpServletResponse();
        guard.doFilter(request, response, (req, res) -> fail("No debe continuar"));
        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test void acceptsAllowedOriginAndHeader() throws Exception {
        var guard = new AuthRequestGuard("https://web.example", new ProblemWriter(new ObjectMapper()));
        var request = new MockHttpServletRequest("POST", "/api/v1/auth/refresh");
        request.addHeader("Origin", "https://web.example");
        request.addHeader("X-Requested-With", "XMLHttpRequest");
        var response = new MockHttpServletResponse();
        guard.doFilter(request, response, (req, res) -> ((MockHttpServletResponse) res).setStatus(204));
        assertThat(response.getStatus()).isEqualTo(204);
    }
}
