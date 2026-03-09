package io.github.dudupuci.appdespesas.application.commands.usuario;

/**
 * Command para atualizar dados de contato e endereço de um usuário.
 */
public record AtualizarUsuarioCommand(
        String cpfCnpj,
        ContatoCommand contato,
        EnderecoCommand endereco
) {
    public record ContatoCommand(
            String telefoneFixo,
            String celular
    ) {}

    public record EnderecoCommand(
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String cep
    ) {}
}

