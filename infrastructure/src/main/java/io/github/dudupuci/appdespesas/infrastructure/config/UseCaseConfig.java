package io.github.dudupuci.appdespesas.infrastructure.config;

import io.github.dudupuci.appdespesas.application.ports.repositories.*;
import io.github.dudupuci.appdespesas.application.ports.services.JwtPort;
import io.github.dudupuci.appdespesas.application.services.*;
import io.github.dudupuci.appdespesas.application.services.generators.UsernameGenerator;
import io.github.dudupuci.appdespesas.application.services.strategies.TratadorCobrancaRegistry;
import io.github.dudupuci.appdespesas.application.services.webservices.AsaasService;
import io.github.dudupuci.appdespesas.application.services.webservices.EmailService;
import io.github.dudupuci.appdespesas.application.usecases.assinatura.*;
import io.github.dudupuci.appdespesas.application.usecases.auth.*;
import io.github.dudupuci.appdespesas.application.usecases.categoria.*;
import io.github.dudupuci.appdespesas.application.usecases.cobranca.*;
import io.github.dudupuci.appdespesas.application.usecases.compromisso.*;
import io.github.dudupuci.appdespesas.application.usecases.cor.*;
import io.github.dudupuci.appdespesas.application.usecases.movimentacao.*;
import io.github.dudupuci.appdespesas.application.usecases.usuario.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Beans dos UseCases da camada application.
 * Nenhum UseCase tem @Service — o Spring os gerencia aqui, em infrastructure.
 */
@Configuration
public class UseCaseConfig {

    // ─── AUTH ────────────────────────────────────────────────────────────────

    @Bean
    public LoginUseCase loginUseCase(UsuarioRepositoryPort usuariosRepository,
                                     PasswordEncoder passwordEncoder, JwtPort jwtPort) {
        return new LoginUseCaseImpl(usuariosRepository, passwordEncoder, jwtPort);
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(UsuarioRepositoryPort usuariosRepository, JwtPort jwtPort) {
        return new RefreshTokenUseCaseImpl(usuariosRepository, jwtPort);
    }

    @Bean
    public RegistrarUsuarioUseCase registrarUsuarioUseCase(UsuarioRepositoryPort usuariosRepository,
                                                           RoleRepositoryPort rolesRepository,
                                                           PasswordEncoder passwordEncoder,
                                                           JwtPort jwtPort,
                                                           UsernameGenerator usernameGenerator,
                                                           CorRepositoryPort corRepository,
                                                           CategoriaRepositoryPort categoriasRepository,
                                                           AssinaturaRepositoryPort assinaturaRepository) {
        return new RegistrarUsuarioUseCaseImpl(usuariosRepository, rolesRepository, passwordEncoder,
                jwtPort, usernameGenerator, corRepository, categoriasRepository, assinaturaRepository);
    }

    // ─── CATEGORIA ───────────────────────────────────────────────────────────

    @Bean
    public CriarCategoriaUseCase criarCategoriaUseCase(CategoriaRepositoryPort repository,
                                                       UsuarioService usuarioService,
                                                       CorService corService) {
        return new CriarCategoriaUseCaseImpl(repository, usuarioService, corService);
    }

    @Bean
    public AtualizarCategoriaUseCase atualizarCategoriaUseCase(CategoriaRepositoryPort repository,
                                                               CategoriaService categoriaService,
                                                               CorService corService) {
        return new AtualizarCategoriaUseCaseImpl(repository, categoriaService, corService);
    }

    @Bean
    public DeletarCategoriaUseCase deletarCategoriaUseCase(CategoriaRepositoryPort repository,
                                                           CategoriaService categoriaService) {
        return new DeletarCategoriaUseCaseImpl(repository, categoriaService);
    }

    // ─── MOVIMENTAÇÃO ────────────────────────────────────────────────────────

    @Bean
    public CriarMovimentacaoUseCase criarMovimentacaoUseCase(MovimentacaoRepositoryPort repository,
                                                             CategoriaService categoriaService,
                                                             UsuarioRepositoryPort usuariosRepository) {
        return new CriarMovimentacaoUseCaseImpl(repository, categoriaService, usuariosRepository);
    }

    @Bean
    public DeletarMovimentacaoUseCase deletarMovimentacaoUseCase(MovimentacaoRepositoryPort repository,
                                                                 MovimentacaoService movimentacaoService) {
        return new DeletarMovimentacaoUseCaseImpl(repository, movimentacaoService);
    }

    // ─── COR ─────────────────────────────────────────────────────────────────

    @Bean
    public CriarCorUseCase criarCorUseCase(CorRepositoryPort corRepository, UsuarioService usuarioService) {
        return new CriarCorUseCaseImpl(corRepository, usuarioService);
    }

    @Bean
    public AtualizarCorUseCase atualizarCorUseCase(CorRepositoryPort corRepository,
                                                   CorService corService,
                                                   UsuarioService usuarioService) {
        return new AtualizarCorUseCaseImpl(corRepository, corService, usuarioService);
    }

    @Bean
    public DeletarCorUseCase deletarCorUseCase(CorRepositoryPort corRepository, CorService corService) {
        return new DeletarCorUseCaseImpl(corRepository, corService);
    }

    // ─── COMPROMISSO ─────────────────────────────────────────────────────────

    @Bean
    public CriarCompromissoUseCase criarCompromissoUseCase(CompromissoRepositoryPort compromissoRepository,
                                                           UsuarioService usuarioService) {
        return new CriarCompromissoUseCaseImpl(compromissoRepository, usuarioService);
    }

    @Bean
    public AtualizarCompromissoUseCase atualizarCompromissoUseCase(CompromissoRepositoryPort compromissoRepository,
                                                                   CompromissoService compromissoService) {
        return new AtualizarCompromissoUseCaseImpl(compromissoRepository, compromissoService);
    }

    @Bean
    public ConcluirCompromissoUseCase concluirCompromissoUseCase(CompromissoRepositoryPort compromissoRepository,
                                                                 CompromissoService compromissoService) {
        return new ConcluirCompromissoUseCaseImpl(compromissoRepository, compromissoService);
    }

    @Bean
    public DeletarCompromissoUseCase deletarCompromissoUseCase(CompromissoRepositoryPort compromissoRepository,
                                                               CompromissoService compromissoService) {
        return new DeletarCompromissoUseCaseImpl(compromissoRepository, compromissoService);
    }

    // ─── USUARIO ─────────────────────────────────────────────────────────────

    @Bean
    public AtualizarPerfilUseCase atualizarPerfilUseCase(UsuarioService usuarioService) {
        return new AtualizarPerfilUseCaseImpl(usuarioService);
    }

    @Bean
    public EditarConfiguracoesUseCase editarConfiguracoesUseCase(UsuarioService usuarioService) {
        return new EditarConfiguracoesUseCaseImpl(usuarioService);
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

    @Bean
    public AssinarPlanoUseCase assinarPlanoUseCase(UsuarioService usuarioService,
                                                   AssinaturaService assinaturaService,
                                                   CobrancaService cobrancaService,
                                                   AsaasService asaasService) {
        return new AssinarPlanoUseCaseImpl(usuarioService, assinaturaService, cobrancaService, asaasService);
    }

    @Bean
    public PrepararAssinaturaPlanoUseCase prepararAssinaturaPlanoUseCase(UsuarioService usuarioService,
                                                                         AssinaturaService assinaturaService) {
        return new PrepararAssinaturaPlanoUseCaseImpl(usuarioService, assinaturaService);
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
