package io.github.dudupuci.appdespesas.infrastructure.config;

import io.github.dudupuci.appdespesas.application.ports.repositories.*;
import io.github.dudupuci.appdespesas.application.ports.services.JwtPort;
import io.github.dudupuci.appdespesas.application.ports.services.PasswordEncoderPort;
import io.github.dudupuci.appdespesas.application.services.*;
import io.github.dudupuci.appdespesas.application.services.generators.UsernameGenerator;
import io.github.dudupuci.appdespesas.application.services.strategies.TratadorCobrancaRegistry;
import io.github.dudupuci.appdespesas.application.services.webservices.AsaasService;
import io.github.dudupuci.appdespesas.application.services.webservices.EmailService;
import io.github.dudupuci.appdespesas.application.usecases.assinatura.*;
import io.github.dudupuci.appdespesas.application.usecases.auth.*;
import io.github.dudupuci.appdespesas.application.usecases.cobranca.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuração dos beans de UseCases que NÃO dependem de contexto de requisição (usuarioId, id, etc.).
 * UseCases contextuais (Criar/Atualizar/Deletar com usuarioId) são instanciados diretamente nos controllers.
 */
@Configuration
public class UseCaseConfig {

    // ─── AUTH ────────────────────────────────────────────────────────────────

    @Bean
    public LoginUseCase loginUseCase(UsuarioRepositoryPort usuariosRepository,
                                     PasswordEncoderPort passwordEncoderPort,
                                     JwtPort jwtPort) {
        return new LoginUseCaseImpl(usuariosRepository, passwordEncoderPort, jwtPort);
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(UsuarioRepositoryPort usuariosRepository,
                                                   JwtPort jwtPort) {
        return new RefreshTokenUseCaseImpl(usuariosRepository, jwtPort);
    }

    @Bean
    public RegistrarUsuarioUseCase registrarUsuarioUseCase(UsuarioRepositoryPort usuariosRepository,
                                                           RoleRepositoryPort rolesRepository,
                                                           PasswordEncoderPort passwordEncoderPort,
                                                           JwtPort jwtPort,
                                                           UsernameGenerator usernameGenerator,
                                                           CorRepositoryPort corRepository,
                                                           CategoriaRepositoryPort categoriasRepository,
                                                           AssinaturaRepositoryPort assinaturaRepository) {
        return new RegistrarUsuarioUseCaseImpl(usuariosRepository, rolesRepository, passwordEncoderPort,
                jwtPort, usernameGenerator, corRepository, categoriasRepository, assinaturaRepository);
    }

    // ─── ASSINATURA ──────────────────────────────────────────────────────────

    @Bean
    public BuscarAssinaturaUsuarioUseCase buscarAssinaturaUsuarioUseCase(AssinaturaService assinaturaService) {
        return new BuscarAssinaturaUsuarioUseCaseImpl(assinaturaService);
    }

    @Bean
    public BuscarOutrasAssinaturasUsuarioUseCase buscarOutrasAssinaturasUsuarioUseCase(AssinaturaService assinaturaService) {
        return new BuscarOutrasAssinaturasUsuarioUseCaseImpl(assinaturaService);
    }

    // ─── COBRANÇA ────────────────────────────────────────────────────────────

    @Bean
    public ConfirmarCobrancaUseCase confirmarCobrancaUseCase(CobrancaService cobrancaService) {
        return new ConfirmarCobrancaUseCaseImpl(cobrancaService);
    }

    @Bean
    public DirecionaTratamentoCobrancaUseCase direcionaTratamentoCobrancaUseCase(TratadorCobrancaRegistry registry) {
        return new DirecionaTratamentoCobrancaUseCaseImpl(registry);
    }

    @Bean
    public TratarCobrancaUseCase tratarCobrancaUseCase(CobrancaService cobrancaService,
                                                       UsuarioService usuarioService,
                                                       AssinaturaService assinaturaService,
                                                       EmailService emailService) {
        return new TratarCobrancaUseCaseImpl(cobrancaService, usuarioService, assinaturaService, emailService);
    }
}
