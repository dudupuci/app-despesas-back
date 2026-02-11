package io.github.dudupuci.appdespesas.models.entities;

import io.github.dudupuci.appdespesas.models.entities.base.EntidadeUuidManual;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Entidade Administrador com UUID fixo e conhecido
 * Usado para criar dados padrão do sistema (cores, categorias, etc)
 * que podem ser compartilhados com todos os usuários.
 */
@Entity
@Table(name = "administradores")
@Getter
@Setter
public class Administrador extends EntidadeUuidManual {

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
    private Role role;

    public Administrador() {
        super();
    }

    public Administrador(UUID id, String nome, String email) {
        super();
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    /**
     * ID fixo do Super Admin do sistema
     * Este UUID é conhecido e usado em queries para identificar dados do sistema
     */
    public static final UUID SUPER_ADMIN_ID = UUID.fromString("839ca206-bc76-427d-b2f2-92a502c6b3b8");

    /**
     * Cria uma instância do Super Admin com o ID fixo
     */
    public static Administrador criarSuperAdmin(Role adminRole) {
        Administrador admin = new Administrador();
        admin.setId(SUPER_ADMIN_ID);
        admin.setNome("Sistema");
        admin.setEmail("sistema@tudinapp.com");
        admin.setDescricao("Administrador do sistema - dados padrão");
        admin.setAtivo(true);
        admin.setRole(adminRole);
        return admin;
    }
}

