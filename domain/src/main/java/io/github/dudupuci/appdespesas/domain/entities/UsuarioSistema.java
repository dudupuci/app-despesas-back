package io.github.dudupuci.appdespesas.domain.entities;

import io.github.dudupuci.appdespesas.domain.annotations.CpfOuCnpj;
import io.github.dudupuci.appdespesas.domain.entities.base.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSistema extends Usuario {

    private String nomeUsuario;
    private String senha;
    private Assinatura assinatura;
    private Role role;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private Contato contato;
    private Endereco endereco;

    @CpfOuCnpj
    private String cpfCnpj;

    private String asaasCustomerId;
    private List<Cobranca> cobrancas = new ArrayList<>();
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
