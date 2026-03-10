package io.github.dudupuci.appdespesas.infrastructure.config;

import io.github.dudupuci.appdespesas.domain.entities.Administrador;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.domain.entities.Role;
import io.github.dudupuci.appdespesas.infrastructure.repositories.AdministradorJpaRepository;
import io.github.dudupuci.appdespesas.infrastructure.repositories.AssinaturaJpaRepository;
import io.github.dudupuci.appdespesas.infrastructure.repositories.RoleJpaRepository;
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
            AdministradorJpaRepository administradorJpaRepository,
            RoleJpaRepository roleJpaRepository,
            AssinaturaJpaRepository assinaturaJpaRepository,
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
            createDefaultRoles(roleJpaRepository);

            // Criar assinaturas padrão
            createDefaultAssinaturas(assinaturaJpaRepository);

            // Buscar ou criar administrador do sistema
            getOrCreateSuperAdmin(
                    administradorJpaRepository,
                    roleJpaRepository,
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

    private void createDefaultRoles(RoleJpaRepository roleJpaRepository) {
        log.info("🔐 Criando roles padrão...");
        createRoleIfNotExists(roleJpaRepository, "USER", "Papel de Usuário", 1);
        createRoleIfNotExists(roleJpaRepository, "ADMIN", "Papel de Administrador", 2);
        createRoleIfNotExists(roleJpaRepository, "MASTER_ADMIN", "Papel de Administrador Master", 3);
        log.info("✓ Roles padrão criadas!");
    }

    private void createDefaultAssinaturas(AssinaturaJpaRepository assinaturaJpaRepository) {
        log.info("📄 Verificando assinaturas padrão...");
        for (String plano : ASSINATURAS) {
            createAssinaturaIfNotExists(assinaturaJpaRepository, plano);
        }
        log.info("✓ Assinaturas padrão verificadas/criadas!");
    }


    private static void createRoleIfNotExists(
            RoleJpaRepository roleJpaRepository,
            String nome,
            String descricao,
            Integer poder
    ) {
        Role existente = roleJpaRepository.buscarPorNome(nome);

        if (existente == null) {
            Role role = new Role();
            role.setNome(nome);
            role.setDescricao(descricao);
            role.setPoder(poder);
            role.setDataCriacao(new Date());
            role.setDataAtualizacao(new Date());
            roleJpaRepository.save(role);
            log.info("✓ Role criada: {} (poder: {})", nome, poder);
        } else {
            log.info("- Role já existe: {}", nome);
        }
    }

    private static void getOrCreateSuperAdmin(
            AdministradorJpaRepository administradorJpaRepository,
            RoleJpaRepository roleJpaRepository,
            UUID superAdmId,
            String nome,
            String sobrenome,
            String email,
            String username,
            String password
    ) {
        // Verificar se o administrador já existe pelo ID fixo
        Administrador superAdmin = administradorJpaRepository.findById(superAdmId).orElse(null);

        if (superAdmin != null) {
            log.info("👤 Super Admin já existe (ID: {})", superAdmin.getId());
            return;
        }

        log.info("👤 Criando Super Admin...");

        // Buscar a role MASTER_ADMIN
        Role masterAdminRole = roleJpaRepository.buscarPorNome("ADMIN");

        if (masterAdminRole == null) {
            throw new RuntimeException("Role ADMIN não encontrada. Certifique-se de que as roles foram criadas antes.");
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

        Administrador savedAdmin = administradorJpaRepository.save(superAdmin);
        log.info("✅ Super Admin criado com ID fixo: {}", savedAdmin.getId());
        log.info("📧 Email: {}", savedAdmin.getEmail());

    }

    private static void createAssinaturaIfNotExists(
            AssinaturaJpaRepository assinaturaJpaRepository,
            String nomePlano
    ) {
        Assinatura existente = assinaturaJpaRepository.buscarPorNomePlano(nomePlano);

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
        assinaturaJpaRepository.save(assinatura);
        log.info("✓ Assinatura criada: {}", nomePlano);
    }
}
