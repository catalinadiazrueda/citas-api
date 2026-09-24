package co.com.fcv.training.citas.application;

public class DuplicateIdentity extends RuntimeException {
    public DuplicateIdentity() { super("Email o documento ya registrado"); }
}
