package io.github.dudupuci.appdespesas.domain.entities;

import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeUuidManual;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Entidade Administrador com UUID fixo e conhecido
 * Usado para criar dados padrão do sistema (cores, categorias, etc)
 * que podem ser compartilhados com todos os usuários.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Administrador extends EntidadeUuidManual {

    private String nome;
    private String sobrenome;
    private String email;
    private String username;
    private String password;
    private String descricao;
    private Boolean ativo = true;
    private Role role;

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

