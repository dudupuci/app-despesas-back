package io.github.dudupuci.appdespesas.config;

import io.github.dudupuci.appdespesas.models.entities.Administrador;
import io.github.dudupuci.appdespesas.models.entities.Assinatura;
import io.github.dudupuci.appdespesas.models.entities.Role;
import io.github.dudupuci.appdespesas.repositories.AdministradorRepository;
import io.github.dudupuci.appdespesas.repositories.AssinaturaRepository;
import io.github.dudupuci.appdespesas.repositories.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Script de inicialização que roda automaticamente ao iniciar a aplicação.
 * Cria apenas roles e super admin do sistema.
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private static final String GRATUITO = "Gratuito";
    private static final String TUDIN_PRO = "Tudin Pro";
    private static final List<String> ASSINATURAS = List.of(GRATUITO, TUDIN_PRO);

    @Bean
    @Transactional
    public CommandLineRunner initDatabase(
            AdministradorRepository administradorRepository,
            RoleRepository roleRepository,
            AssinaturaRepository assinaturaRepository,
            ApplicationConfig applicationConfig
    ) {
        return args -> {
            UUID superAdmId = applicationConfig.getSuperAdmId();
            String superAdminName = applicationConfig.getSuperAdminName();
            String superAdminMiddleName = applicationConfig.getSuperAdminMiddleName();
            String superAdminEmail = applicationConfig.getSuperAdminEmail();
            String superAdminUsername = applicationConfig.getSuperAdminUsername();
            String superAdminPassword = applicationConfig.getSuperAdminPassword();

            log.info("🚀 Iniciando criação de dados padrão do sistema...");

            // Criar roles padrão
            createDefaultRoles(roleRepository);

            // Criar assinaturas padrão
            createDefaultAssinaturas(assinaturaRepository);

            // Buscar ou criar administrador do sistema
            getOrCreateSuperAdmin(
                    administradorRepository,
                    roleRepository,
                    superAdmId,
                    superAdminName,
                    superAdminMiddleName,
                    superAdminEmail,
                    superAdminUsername,
                    superAdminPassword
            );

            log.info("✅ Dados padrão do sistema criados com sucesso!");
            log.info("💡 Cores e categorias padrão serão criadas automaticamente para cada usuário ao se cadastrar.");
        };
    }

    private void createDefaultRoles(RoleRepository roleRepository) {
        log.info("🔐 Criando roles padrão...");
        createRoleIfNotExists(roleRepository, "USER", "Papel de Usuário", 1);
        createRoleIfNotExists(roleRepository, "ADMIN", "Papel de Administrador", 2);
        createRoleIfNotExists(roleRepository, "MASTER_ADMIN", "Papel de Administrador Master", 3);
        log.info("✓ Roles padrão criadas!");
    }

    private void createDefaultAssinaturas(AssinaturaRepository assinaturaRepository) {
        log.info("📄 Verificando assinaturas padrão...");
        for (String plano : ASSINATURAS) {
            createAssinaturaIfNotExists(assinaturaRepository, plano);
        }
        log.info("✓ Assinaturas padrão verificadas/criadas!");
    }


    private static void createRoleIfNotExists(
            RoleRepository roleRepository,
            String nome,
            String descricao,
            Integer poder
    ) {
        Role existente = roleRepository.buscarPorNome(nome);

        if (existente == null) {
            Role role = new Role();
            role.setNome(nome);
            role.setDescricao(descricao);
            role.setPoder(poder);
            role.setDataCriacao(new Date());
            role.setDataAtualizacao(new Date());
            roleRepository.save(role);
            log.info("✓ Role criada: {} (poder: {})", nome, poder);
        } else {
            log.info("- Role já existe: {}", nome);
        }
    }

    private static void getOrCreateSuperAdmin(
            AdministradorRepository administradorRepository,
            RoleRepository roleRepository,
            UUID superAdmId,
            String nome,
            String sobrenome,
            String email,
            String username,
            String password
    ) {
        // Verificar se o administrador já existe pelo ID fixo
        Administrador superAdmin = administradorRepository.findById(superAdmId).orElse(null);

        if (superAdmin != null) {
            log.info("👤 Super Admin já existe (ID: {})", superAdmin.getId());
            return;
        }

        log.info("👤 Criando Super Admin...");

        // Buscar a role MASTER_ADMIN
        Role masterAdminRole = roleRepository.buscarPorNome("MASTER_ADMIN");

        if (masterAdminRole == null) {
            throw new RuntimeException("Role MASTER_ADMIN não encontrada. Certifique-se de que as roles foram criadas antes.");
        }

        superAdmin = new Administrador();
        superAdmin.setId(superAdmId);  // ✅ Setando ID fixo manualmente
        superAdmin.setNome(nome);
        superAdmin.setSobrenome(sobrenome);
        superAdmin.setEmail(email);
        superAdmin.setUsername(username);  // ✅ Username para login futuro
        superAdmin.setPassword(password);     // ✅ Senha já criptografada (BCrypt)
        superAdmin.setDescricao("Super Administrador do sistema - Criador de dados padrão");
        superAdmin.setAtivo(true);
        superAdmin.setRole(masterAdminRole);
        superAdmin.setDataCriacao(new Date());
        superAdmin.setDataAtualizacao(new Date());

        Administrador savedAdmin = administradorRepository.save(superAdmin);
        log.info("✅ Super Admin criado com ID fixo: {}", savedAdmin.getId());
        log.info("📧 Email: {}", savedAdmin.getEmail());

    }

    private static void createAssinaturaIfNotExists(
            AssinaturaRepository assinaturaRepository,
            String nomePlano
    ) {
        Assinatura existente = assinaturaRepository.buscarPorNomePlano(nomePlano);

        if (existente != null) {
            log.info("- Assinatura já existe: {}", nomePlano);
            return;
        }

        log.info("📄 Criando assinatura: {}...", nomePlano);
        Assinatura assinatura = new Assinatura();
        assinatura.setNomePlano(nomePlano);

        switch (nomePlano) {
            case GRATUITO:
                assinatura.setValor(BigDecimal.ZERO);
                assinatura.setDescricao("Plano Gratuito com recursos básicos");
                assinatura.setBeneficios(
                        List.of(
                                "Recursos básicos",
                                "Sem custos",
                                "Operação somente via dashboard",
                                "Suporte via email com tempo de resposta de até 1 dia útil",
                                "Limite de 3 categorias e 3 cores personalizadas"
                        ));
                break;
            case TUDIN_PRO:
                assinatura.setValor(new BigDecimal("14.90"));
                assinatura.setDescricao("Plano Tudin Pro com funcionalidades avançadas");
                assinatura.setBeneficios(
                        List.of(
                                "Recursos avançados",
                                "ChatBot IA para automatização de tarefas",
                                "Relatórios avançados e insights personalizados",
                                "Suporte prioritário via Whatsapp",
                                "Categorias e cores ilimitadas"
                        ));
                break;
            default:
                throw new IllegalArgumentException("Plano de assinatura não mapeado: " + nomePlano);
        }

        assinatura.setDataCriacao(new Date());
        assinatura.setDataAtualizacao(new Date());
        assinaturaRepository.save(assinatura);
        log.info("✓ Assinatura criada: {}", nomePlano);
    }
}
