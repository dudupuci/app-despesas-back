package io.github.dudupuci.appdespesas.exceptions;

public class CpfCnpjObrigatorioException extends RuntimeException {
    public CpfCnpjObrigatorioException() {
        super("CPF ou CNPJ é obrigatório para o tipo de pessoa selecionado.");
    }
}
