package io.github.dudupuci.appdespesas.application.commands.auth;

/**
 * Command para registrar um novo usuário no sistema.
 * Não depende de nenhuma camada de infraestrutura.
 */
public record RegistroCommand(
        String nome,
        String sobrenome,
        String email,
        String celular,
        String telefoneFixo,
        String senha,
        String confirmacaoSenha
) {}

