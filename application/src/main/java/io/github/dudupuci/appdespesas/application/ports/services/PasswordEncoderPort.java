package io.github.dudupuci.appdespesas.application.ports.services;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
