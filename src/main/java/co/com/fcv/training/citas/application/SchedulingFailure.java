package co.com.fcv.training.citas.application;
public class SchedulingFailure extends RuntimeException {
    public enum Kind { NOT_FOUND, CONFLICT }
    private final Kind kind;
    public SchedulingFailure(Kind kind, String message) { super(message); this.kind = kind; }
    public Kind kind() { return kind; }
}
