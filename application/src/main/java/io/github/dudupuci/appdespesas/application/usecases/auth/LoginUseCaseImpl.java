package io.github.dudupuci.appdespesas.application.usecases.auth;

import io.github.dudupuci.appdespesas.application.ports.repositories.UsuarioRepositoryPort;
import io.github.dudupuci.appdespesas.application.ports.services.JwtPort;
import io.github.dudupuci.appdespesas.application.ports.services.PasswordEncoderPort;
import io.github.dudupuci.appdespesas.application.responses.auth.AuthResult;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.exceptions.CredenciaisInvalidasException;
import io.github.dudupuci.appdespesas.domain.exceptions.UsuarioInativoException;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasMessages;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class LoginUseCaseImpl extends LoginUseCase {

    private final UsuarioRepositoryPort usuariosRepository;
    private final PasswordEncoderPort passwordEncoderPort;
    private final JwtPort jwtPort;

    public LoginUseCaseImpl(UsuarioRepositoryPort usuariosRepository, PasswordEncoderPort passwordEncoderPort, JwtPort jwtPort) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoderPort = passwordEncoderPort;
        this.jwtPort = jwtPort;
    }

    @Override
    public AuthResult executar(LoginCommand cmd) {
        Optional<UsuarioSistema> usuario = usuariosRepository.buscarPorEmail(cmd.email());
        if (usuario.isEmpty() || !this.passwordEncoderPort.matches(cmd.senha(), usuario.get().getSenha())) {
            throw new CredenciaisInvalidasException(AppDespesasMessages.getMessage("auth.credenciais.invalidas"));
        }
        if (!usuario.get().getAtivo()) {
            throw new UsuarioInativoException(AppDespesasMessages.getMessage("auth.usuario.inativo"));
        }
        return new AuthResult(
                jwtPort.generateAccessToken(usuario.get()),
                jwtPort.generateRefreshToken(usuario.get()),
                usuario.get()
        );
    }
}

