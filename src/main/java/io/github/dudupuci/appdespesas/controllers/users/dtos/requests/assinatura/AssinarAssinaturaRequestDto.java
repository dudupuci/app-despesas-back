package io.github.dudupuci.appdespesas.controllers.users.dtos.requests.assinatura;


import io.github.dudupuci.appdespesas.services.annotations.CpfOuCnpj;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.StringUtils;

public record AssinarAssinaturaRequestDto(
        String nomeCompleto,

        @Email
        @NotBlank(message = "O campo email é obrigatório e deve ser um email válido.")
        String email,

        @CpfOuCnpj
        String cpfCnpj,

        boolean assinaturaParaOutraPessoa
) {

    public void validarParaAssinaturaPropria() {
        if (StringUtils.isEmpty(nomeCompleto)) {
            throw new IllegalArgumentException("O nome completo é obrigatório.");
        }

        if (StringUtils.isEmpty(cpfCnpj)) {
            throw new IllegalArgumentException("O CPF/CNPJ é obrigatório.");
        }
    }

}
