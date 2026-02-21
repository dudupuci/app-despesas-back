package io.github.dudupuci.appdespesas.models.entities;

import io.github.dudupuci.appdespesas.models.entities.base.Usuario;
import io.github.dudupuci.appdespesas.services.annotations.CpfOuCnpj;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import io.github.dudupuci.appdespesas.models.entities.Cobranca;


@Table(name = "usuarios")
@Entity
@Getter
@Setter
public class UsuarioSistema extends Usuario {

    @Column(unique = true, name = "nome_usuario")
    private String nomeUsuario;

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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contato_id", unique = true)
    private Contato contato;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id", unique = true)
    private Endereco endereco;

    @Column(name = "cpf_cnpj", unique = true)
    @CpfOuCnpj
    private String cpfCnpj;

    @Column(name = "asaas_customer_id", unique = true)
    private String asaasCustomerId;

    private Boolean ativo;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Cobranca> cobrancas;

    @Override
    public String getNomeCompleto() {
        return this.getNome() + " " + this.getSobrenome();
    }
}
