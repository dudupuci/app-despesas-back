package io.github.dudupuci.appdespesas.infrastructure.persistence.entities;

import io.github.dudupuci.appdespesas.domain.entities.Administrador;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base.JpaEntidadeUuidManual;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "administrador")
@Getter
@Setter
public class AdministradorJpaEntity extends JpaEntidadeUuidManual {

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String sobrenome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(unique = true, length = 100)
    private String username;

    private String password;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleJpaEntity role;

    public AdministradorJpaEntity() {
        super();
    }

    /**
     * Converte a entidade JPA para entidade de domínio
     */
    public Administrador toEntity() {
        Administrador admin = new Administrador();
        super.toDomain(admin);
        admin.setNome(this.nome);
        admin.setSobrenome(this.sobrenome);
        admin.setEmail(this.email);
        admin.setUsername(this.username);
        admin.setPassword(this.password);
        admin.setDescricao(this.descricao);
        admin.setAtivo(this.ativo);

        if (this.role != null) {
            admin.setRole(this.role.toEntity());
        }

        return admin;
    }

    /**
     * Cria uma entidade JPA a partir de uma entidade de domínio (sem relacionamentos)
     */
    public static AdministradorJpaEntity fromEntity(Administrador admin) {
        if (admin == null) return null;

        AdministradorJpaEntity jpaAdmin = new AdministradorJpaEntity();
        jpaAdmin.fromDomain(admin);
        jpaAdmin.setNome(admin.getNome());
        jpaAdmin.setSobrenome(admin.getSobrenome());
        jpaAdmin.setEmail(admin.getEmail());
        jpaAdmin.setUsername(admin.getUsername());
        jpaAdmin.setPassword(admin.getPassword());
        jpaAdmin.setDescricao(admin.getDescricao());
        jpaAdmin.setAtivo(admin.getAtivo());
        return jpaAdmin;
    }

    /**
     * Atualiza a entidade JPA com dados da entidade de domínio
     */
    public void updateFromEntity(Administrador admin) {
        this.nome = admin.getNome();
        this.sobrenome = admin.getSobrenome();
        this.email = admin.getEmail();
        this.username = admin.getUsername();
        this.password = admin.getPassword();
        this.descricao = admin.getDescricao();
        this.ativo = admin.getAtivo();
        this.dataAtualizacao = admin.getDataAtualizacao();
    }

    /**
     * Cria uma instância do Super Admin com o ID fixo
     */
    public static AdministradorJpaEntity criarSuperAdmin(RoleJpaEntity adminRole) {
        AdministradorJpaEntity admin = new AdministradorJpaEntity();
        admin.setId(Administrador.SUPER_ADMIN_ID);
        admin.setNome("Sistema");
        admin.setEmail("sistema@tudinapp.com");
        admin.setDescricao("Administrador do sistema - dados padrão");
        admin.setAtivo(true);
        admin.setRole(adminRole);
        return admin;
    }
}

