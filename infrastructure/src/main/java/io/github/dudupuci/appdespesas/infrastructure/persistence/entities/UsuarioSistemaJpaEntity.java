package io.github.dudupuci.appdespesas.infrastructure.persistence.entities;

import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base.JpaPessoa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class UsuarioSistemaJpaEntity extends JpaPessoa {

    @Column(unique = true, name = "nome_usuario")
    private String nomeUsuario;

    @Column(nullable = false)
    private String senha;

    @ManyToOne
    @JoinColumn(name = "assinatura_id")
    private AssinaturaJpaEntity assinatura;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleJpaEntity role;

    @OneToMany(mappedBy = "usuarioSistema", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovimentacaoJpaEntity> movimentacoes = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contato_id", unique = true)
    private ContatoJpaEntity contato;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id", unique = true)
    private EnderecoJpaEntity endereco;

    @Column(name = "cpf_cnpj", unique = true)
    private String cpfCnpj;

    @Column(name = "asaas_customer_id", unique = true)
    private String asaasCustomerId;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<CobrancaJpaEntity> cobrancas = new ArrayList<>();

    @OneToOne(mappedBy = "usuarioSistema", cascade = CascadeType.ALL, orphanRemoval = true)
    private UsuarioSistemaConfigJpaEntity usuarioSistemaConfig;

    private Boolean ativo;

    public UsuarioSistemaJpaEntity() {
        super();
    }

    /**
     * Converte a entidade JPA para entidade de domínio
     */
    public UsuarioSistema toEntity() {
        UsuarioSistema usuario = new UsuarioSistema();
        super.toDomainPessoa(usuario);

        // Campos de UsuarioSistema
        usuario.setNomeUsuario(this.nomeUsuario);
        usuario.setSenha(this.senha);
        usuario.setCpfCnpj(this.cpfCnpj);
        usuario.setAsaasCustomerId(this.asaasCustomerId);
        usuario.setAtivo(this.ativo);

        // Relacionamentos
        if (this.assinatura != null) {
            usuario.setAssinatura(this.assinatura.toEntity());
        }

        if (this.role != null) {
            usuario.setRole(this.role.toEntity());
        }

        if (this.contato != null) {
            usuario.setContato(this.contato.toEntity());
        }

        if (this.endereco != null) {
            usuario.setEndereco(this.endereco.toEntity());
        }

        if (this.usuarioSistemaConfig != null) {
            usuario.setUsuarioSistemaConfig(this.usuarioSistemaConfig.toEntity());
        }

        if (this.movimentacoes != null && !this.movimentacoes.isEmpty()) {
            usuario.setMovimentacoes(
                this.movimentacoes.stream()
                    .map(MovimentacaoJpaEntity::toEntity)
                    .collect(Collectors.toList())
            );
        }

        if (this.cobrancas != null && !this.cobrancas.isEmpty()) {
            usuario.setCobrancas(
                this.cobrancas.stream()
                    .map(CobrancaJpaEntity::toEntity)
                    .collect(Collectors.toList())
            );
        }

        return usuario;
    }

    /**
     * Cria uma entidade JPA a partir de uma entidade de domínio (sem relacionamentos complexos)
     */
    public static UsuarioSistemaJpaEntity fromEntity(UsuarioSistema usuario) {
        if (usuario == null) return null;

        UsuarioSistemaJpaEntity jpaUsuario = new UsuarioSistemaJpaEntity();
        jpaUsuario.fromDomainPessoa(usuario);

        // Campos de UsuarioSistema
        jpaUsuario.setNomeUsuario(usuario.getNomeUsuario());
        jpaUsuario.setSenha(usuario.getSenha());
        jpaUsuario.setCpfCnpj(usuario.getCpfCnpj());
        jpaUsuario.setAsaasCustomerId(usuario.getAsaasCustomerId());
        jpaUsuario.setAtivo(usuario.getAtivo());

        return jpaUsuario;
    }

    /**
     * Atualiza a entidade JPA com dados da entidade de domínio
     */
    public void updateFromEntity(UsuarioSistema usuario) {
        this.nome = usuario.getNome();
        this.sobrenome = usuario.getSobrenome();
        this.nomeUsuario = usuario.getNomeUsuario();
        this.senha = usuario.getSenha();
        this.cpfCnpj = usuario.getCpfCnpj();
        this.asaasCustomerId = usuario.getAsaasCustomerId();
        this.ativo = usuario.getAtivo();
        this.dataAtualizacao = usuario.getDataAtualizacao();
    }
}

