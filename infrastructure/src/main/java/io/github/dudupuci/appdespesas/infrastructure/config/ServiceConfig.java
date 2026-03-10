package io.github.dudupuci.appdespesas.infrastructure.config;

import io.github.dudupuci.appdespesas.application.ports.repositories.*;
import io.github.dudupuci.appdespesas.application.ports.services.MailtrapPort;
import io.github.dudupuci.appdespesas.application.ports.services.ViaCepPort;
import io.github.dudupuci.appdespesas.application.services.*;
import io.github.dudupuci.appdespesas.application.services.ai.ConversationService;
import io.github.dudupuci.appdespesas.application.services.generators.UsernameGenerator;
import io.github.dudupuci.appdespesas.application.services.webservices.EmailService;
import io.github.dudupuci.appdespesas.application.services.webservices.ViaCepService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de beans dos Services da camada application.
 * Como application não tem Spring, os beans são registrados aqui em infrastructure.
 */
@Configuration
public class ServiceConfig {

    // ─── CORE SERVICES ───────────────────────────────────────────────────────

    @Bean
    public UsuarioService usuarioService(UsuarioRepositoryPort usuariosRepository) {
        return new UsuarioService(usuariosRepository);
    }

    @Bean
    public AssinaturaService assinaturaService(AssinaturaRepositoryPort assinaturaRepository) {
        return new AssinaturaService(assinaturaRepository);
    }

    @Bean
    public CategoriaService categoriaService(CategoriaRepositoryPort categoriaRepository,
                                             UsuarioService usuarioService) {
        return new CategoriaService(categoriaRepository, usuarioService);
    }

    @Bean
    public CorService corService(CorRepositoryPort corRepository, UsuarioService usuarioService) {
        return new CorService(corRepository, usuarioService);
    }

    @Bean
    public MovimentacaoService movimentacaoService(MovimentacaoRepositoryPort movimentacaoRepository) {
        return new MovimentacaoService(movimentacaoRepository);
    }

    @Bean
    public CobrancaService cobrancaService(CobrancaRepositoryPort cobrancaRepository) {
        return new CobrancaService(cobrancaRepository);
    }

    @Bean
    public CompromissoService compromissoService(CompromissoRepositoryPort compromissoRepository,
                                                 UsuarioService usuarioService) {
        return new CompromissoService(compromissoRepository, usuarioService);
    }

    @Bean
    public NotificacaoService notificacaoService(NotificacaoRepositoryPort notificacaoRepository) {
        return new NotificacaoService(notificacaoRepository);
    }

    @Bean
    public RoleService roleService(RoleRepositoryPort rolesRepository) {
        return new RoleService(rolesRepository);
    }

    @Bean
    public CalendarioService calendarioService(CompromissoRepositoryPort compromissoRepository,
                                               MovimentacaoRepositoryPort movimentacaoRepository,
                                               UsuarioService usuarioService) {
        return new CalendarioService(compromissoRepository, movimentacaoRepository, usuarioService);
    }

    @Bean
    public UsuarioConfigService usuarioConfigService(UsuarioService usuarioService) {
        return new UsuarioConfigService(usuarioService);
    }

    @Bean
    public UsernameGenerator usernameGenerator(UsuarioService usuarioService) {
        return new UsernameGenerator(usuarioService);
    }

    // ─── AI / CONVERSA ───────────────────────────────────────────────────────

    @Bean
    public ConversationService conversationService() {
        return new ConversationService();
    }

    // ─── WEBSERVICES ─────────────────────────────────────────────────────────

    @Bean
    public EmailService emailService(MailtrapPort MailtrapPort,
                                     NotificacaoService notificacaoService,
                                     @Value("${integrations.mailtrap.api.token}") String token,
                                     @Value("${integrations.mailtrap.api.from}") String fromEmail,
                                     @Value("${integrations.mailtrap.api.name}") String fromName) {
        return new EmailService(MailtrapPort, notificacaoService, token, fromEmail, fromName);
    }

    @Bean
    public ViaCepService viaCepService(ViaCepPort viaCepPort) {
        return new ViaCepService(viaCepPort);
    }
}

