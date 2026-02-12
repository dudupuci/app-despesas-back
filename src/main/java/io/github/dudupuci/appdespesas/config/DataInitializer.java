package io.github.dudupuci.appdespesas.config;

import io.github.dudupuci.appdespesas.models.entities.Administrador;
import io.github.dudupuci.appdespesas.models.entities.Role;
import io.github.dudupuci.appdespesas.repositories.AdministradorRepository;
import io.github.dudupuci.appdespesas.repositories.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * Script de inicialização que roda automaticamente ao iniciar a aplicação.
 * Cria apenas roles e super admin do sistema.
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    @Transactional
    public CommandLineRunner initDatabase(
            AdministradorRepository administradorRepository,
            RoleRepository roleRepository,
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

}

