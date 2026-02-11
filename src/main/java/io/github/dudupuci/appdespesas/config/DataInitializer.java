package io.github.dudupuci.appdespesas.config;

import io.github.dudupuci.appdespesas.models.entities.Administrador;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.entities.Cor;
import io.github.dudupuci.appdespesas.models.entities.Role;
import io.github.dudupuci.appdespesas.models.enums.Status;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.repositories.AdministradorRepository;
import io.github.dudupuci.appdespesas.repositories.CategoriasRepository;
import io.github.dudupuci.appdespesas.repositories.CorRepository;
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
 * Cria cores e categorias padrão do sistema que estarão disponíveis para todos os usuários.
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    @Transactional
    public CommandLineRunner initDatabase(
            CorRepository corRepository,
            CategoriasRepository categoriasRepository,
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
            log.info("📋 Super Admin ID: {}", superAdmId);

            // Criar roles padrão
            createDefaultRoles(roleRepository);

            // Buscar ou criar administrador do sistema
            Administrador superAdmin = getOrCreateSuperAdmin(
                    administradorRepository,
                    roleRepository,
                    superAdmId,
                    superAdminName,
                    superAdminMiddleName,
                    superAdminEmail,
                    superAdminUsername,
                    superAdminPassword
            );

            // Criar cores padrão
            createDefaultColors(corRepository, superAdmin);

            // Criar categorias padrão
            createDefaultCategories(categoriasRepository, superAdmin);

            log.info("✅ Dados padrão do sistema criados com sucesso!");
        };
    }

    private void createDefaultRoles(RoleRepository roleRepository) {
        log.info("- Criando roles padrão...");

        createRoleIfNotExists(roleRepository, "USER", "Papel de Usuário", 1);
        createRoleIfNotExists(roleRepository, "ADMIN", "Papel de Administrador", 2);
        createRoleIfNotExists(roleRepository, "MASTER_ADMIN", "Papel de Administrador Master", 3);

        log.info("✓ Roles padrão criadas!");
    }

    private void createDefaultColors(CorRepository corRepository, Administrador superAdmin) {
        log.info("🎨 Criando cores padrão...");
        createColorIfNotExists(corRepository, superAdmin, "Vermelho", "#FF0000");
        createColorIfNotExists(corRepository, superAdmin, "Azul", "#0000FF");
        createColorIfNotExists(corRepository, superAdmin, "Verde", "#00FF00");
        createColorIfNotExists(corRepository, superAdmin, "Amarelo", "#FFFF00");
        log.info("✅ Cores padrão criadas!");
    }

    private void createDefaultCategories(CategoriasRepository categoriasRepository, Administrador superAdmin) {
        log.info("📁 Criando categorias padrão...");
        createCategoriasDespesas(categoriasRepository, superAdmin);
        createCategoriasReceitas(categoriasRepository, superAdmin);
        log.info("✅ Categorias padrão criadas!");
    }


    private void createColorIfNotExists(
            CorRepository corRepository,
            Administrador superAdmin,
            String nome,
            String codigoHex
    ) {
        if (!corRepository.existsByNomeAndAdministradorId(nome, superAdmin.getId())) {
            Cor cor = new Cor();
            cor.setNome(nome);
            cor.setCodigoHexadecimal(codigoHex);
            cor.setAdministradorId(superAdmin.getId());  // ✅ Vincula ao administrador
            cor.setDataCriacao(new Date());
            cor.setDataAtualizacao(new Date());
            corRepository.save(cor);
            log.info("  ✓ Cor criada: {} ({})", nome, codigoHex);
        } else {
            log.info("  - Cor já existe: {}", nome);
        }
    }

    private void createCategoryIfNotExists(
            CategoriasRepository categoriasRepository,
            Administrador superAdmin,
            String nome,
            String descricao,
            TipoMovimentacao tipo
    ) {
        // Verificar se já existe
        Categoria existente = categoriasRepository.buscarPorNomeEAdministrador(nome, superAdmin.getId());

        if (existente == null) {
            Categoria categoria = new Categoria();
            categoria.setNome(nome);
            categoria.setDescricao(descricao);
            categoria.setTipoMovimentacao(tipo);
            categoria.setAdministradorId(superAdmin.getId());  // ✅ Vincula ao administrador
            categoria.setStatus(Status.ATIVO);
            categoria.setDataCriacao(new Date());
            categoria.setDataAtualizacao(new Date());
            categoriasRepository.save(categoria);
            log.info("  ✓ Categoria criada: {} ({})", nome, tipo);
        } else {
            log.info("  - Categoria já existe: {}", nome);
        }
    }

    private void createCategoriasReceitas(CategoriasRepository categoriasRepository, Administrador superAdmin) {
        createCategoryIfNotExists(
                categoriasRepository,
                superAdmin,
                "Salário",
                "Renda proveniente de trabalho fixo",
                TipoMovimentacao.RECEITA
        );

        createCategoryIfNotExists(
                categoriasRepository,
                superAdmin,
                "Freelancers",
                "Renda proveniente de trabalhos autônomos e projetos",
                TipoMovimentacao.RECEITA
        );

        createCategoryIfNotExists(
                categoriasRepository,
                superAdmin,
                "Investimentos",
                "Renda proveniente de aplicações financeiras, dividendos, juros",
                TipoMovimentacao.RECEITA
        );
    }

    private void createCategoriasDespesas(CategoriasRepository categoriasRepository, Administrador superAdmin) {
        createCategoryIfNotExists(
                categoriasRepository,
                superAdmin,
                "Alimentação",
                "Despesas com supermercado, restaurantes, delivery",
                TipoMovimentacao.DESPESA
        );

        createCategoryIfNotExists(
                categoriasRepository,
                superAdmin,
                "Transporte",
                "Despesas com combustível, transporte público, aplicativos de mobilidade",
                TipoMovimentacao.DESPESA
        );

        createCategoryIfNotExists(
                categoriasRepository,
                superAdmin,
                "Lazer",
                "Despesas com entretenimento, viagens, hobbies",
                TipoMovimentacao.DESPESA
        );
    }


    private void createRoleIfNotExists(
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

    private Administrador getOrCreateSuperAdmin(
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
            return superAdmin;
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

        return savedAdmin;
    }

}

