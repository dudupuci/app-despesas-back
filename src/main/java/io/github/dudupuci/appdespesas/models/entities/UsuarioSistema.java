package io.github.dudupuci.appdespesas.models.entities;

import io.github.dudupuci.appdespesas.models.entities.base.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Table(name = "usuarios")
@Entity
@Getter
@Setter
public class UsuarioSistema extends Usuario {

    /**
     * ID fixo do Super Admin do sistema
     * Este UUID é conhecido e usado para identificar dados padrão do sistema
     */
    public static final java.util.UUID SUPER_ADMIN_ID = java.util.UUID.fromString("839ca206-bc76-427d-b2f2-92a502c6b3b8");

    @Column(unique = true, name = "nome_usuario")
    private String nomeUsuario;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @ManyToOne
    @JoinColumn(name = "assinatura_id")
    private Assinatura assinatura;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "usuarioSistema", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimentacao> movimentacoes;

    private Boolean ativo;

    public String getNomeCompleto() {
        return this.getNome() + " " + this.getSobrenome();
    }
}
