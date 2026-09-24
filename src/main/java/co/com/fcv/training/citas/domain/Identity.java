package co.com.fcv.training.citas.domain;

import java.util.Locale;
import java.util.regex.Pattern;

public final class Identity {
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private Identity() {}

    public static String email(String value) {
        String normalized = required(value).toLowerCase(Locale.ROOT);
        if (!EMAIL.matcher(normalized).matches()) throw new IllegalArgumentException("Email inválido");
        return normalized;
    }

    public static String documentType(String value) {
        return required(value).toUpperCase(Locale.ROOT);
    }

    public static String required(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Campo obligatorio");
        return value.trim();
    }
}
