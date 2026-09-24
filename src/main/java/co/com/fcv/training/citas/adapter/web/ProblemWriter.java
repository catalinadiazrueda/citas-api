package co.com.fcv.training.citas.adapter.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

@Component
public class ProblemWriter {
    private final ObjectMapper mapper;
    public ProblemWriter(ObjectMapper mapper) { this.mapper = mapper; }

    public void write(HttpServletResponse response, int status, String detail) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), Map.of(
                "type", "about:blank", "title", detail, "status", status, "detail", detail));
    }
}
