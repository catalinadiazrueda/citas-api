package co.com.fcv.training.citas.adapter.web;

import co.com.fcv.training.citas.application.AuthFailure;
import co.com.fcv.training.citas.application.DuplicateIdentity;
import co.com.fcv.training.citas.application.SchedulingFailure;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ApiErrors {
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, IllegalArgumentException.class})
    ResponseEntity<ProblemDetail> invalid(Exception ignored) {
        return problem(HttpStatus.BAD_REQUEST, "Datos inválidos");
    }

    @ExceptionHandler({DuplicateIdentity.class, DataIntegrityViolationException.class})
    ResponseEntity<ProblemDetail> duplicate(Exception ignored) {
        return problem(HttpStatus.CONFLICT, "Email o documento ya registrado");
    }

    @ExceptionHandler(AuthFailure.class)
    ResponseEntity<ProblemDetail> unauthorized(AuthFailure ignored) {
        return problem(HttpStatus.UNAUTHORIZED, "Credenciales o sesión inválidas");
    }

    @ExceptionHandler(SchedulingFailure.class)
    ResponseEntity<ProblemDetail> scheduling(SchedulingFailure failure) {
        return problem(failure.kind() == SchedulingFailure.Kind.NOT_FOUND ? HttpStatus.NOT_FOUND : HttpStatus.CONFLICT, failure.getMessage());
    }

    private ResponseEntity<ProblemDetail> problem(HttpStatus status, String detail) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(status, detail);
        body.setTitle(detail);
        return ResponseEntity.status(status).body(body);
    }
}
