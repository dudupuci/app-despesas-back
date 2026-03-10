package io.github.dudupuci.appdespesas.application.usecases.auth;

import io.github.dudupuci.appdespesas.application.ports.repositories.UsuarioRepositoryPort;
import io.github.dudupuci.appdespesas.application.ports.services.JwtPort;
import io.github.dudupuci.appdespesas.application.responses.auth.RefreshTokenResult;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.exceptions.CredenciaisInvalidasException;
import io.github.dudupuci.appdespesas.domain.exceptions.UsuarioInativoException;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasMessages;

import java.util.Optional;

public class RefreshTokenUseCaseImpl extends RefreshTokenUseCase {

    private final UsuarioRepositoryPort usuariosRepository;
    private final JwtPort jwtPort;

    public RefreshTokenUseCaseImpl(UsuarioRepositoryPort usuariosRepository, JwtPort jwtPort) {
        this.usuariosRepository = usuariosRepository;
        this.jwtPort = jwtPort;
    }

    @Override
    public RefreshTokenResult executar(String refreshToken) {
        try {
            String email = jwtPort.extractEmail(refreshToken);
            Optional<UsuarioSistema> usuario = usuariosRepository.buscarPorEmail(email);
            if (usuario.isEmpty()) throw new CredenciaisInvalidasException(AppDespesasMessages.getMessage("auth.token.invalido"));
            if (!usuario.get().getAtivo()) throw new UsuarioInativoException(AppDespesasMessages.getMessage("auth.usuario.inativo"));
            return new RefreshTokenResult(jwtPort.generateAccessToken(usuario.get()));
        } catch (CredenciaisInvalidasException | UsuarioInativoException e) {
            throw e;
        } catch (Exception e) {
            throw new CredenciaisInvalidasException(AppDespesasMessages.getMessage("auth.token.invalido"));
        }
    }
}

