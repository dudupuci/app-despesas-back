package io.github.dudupuci.appdespesas.domain.entities;

import io.github.dudupuci.appdespesas.domain.entities.base.Usuario;
import io.github.dudupuci.appdespesas.application.services.annotations.CpfOuCnpj;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Table(name = "usuario")
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

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Cobranca> cobrancas;

    @OneToOne(mappedBy = "usuarioSistema", cascade = CascadeType.ALL, orphanRemoval = true)
    private UsuarioSistemaConfig usuarioSistemaConfig;

    private Boolean ativo;

    @Override
    public String getNomeCompleto() {
        return this.getNome() + " " + this.getSobrenome();
    }

    public void adicionaCobranca(Cobranca cobranca) {
        this.cobrancas.add(cobranca);
    }

}
