package co.com.fcv.training.citas.application;

public class AuthFailure extends RuntimeException {
    public AuthFailure() { super("Credenciales o sesión inválidas"); }
}
