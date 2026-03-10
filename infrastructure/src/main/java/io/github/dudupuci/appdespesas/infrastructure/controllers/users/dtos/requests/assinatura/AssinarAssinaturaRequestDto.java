package io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.assinatura;

import io.github.dudupuci.appdespesas.application.usecases.assinatura.AssinarAssinaturaCommand;
import io.github.dudupuci.appdespesas.domain.annotations.CpfOuCnpj;
import io.github.dudupuci.appdespesas.domain.enums.BillingType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

public record AssinarAssinaturaRequestDto(
        String nomeCompleto,

        @Email
        @NotBlank(message = "O campo email é obrigatório e deve ser um email válido.")
        String email,

        @CpfOuCnpj
        String cpfCnpj,

        boolean assinaturaParaOutraPessoa,

        @NotNull(message = "Selecione uma forma de pagamento.")
        BillingType formaPagamento
) {
    public AssinarAssinaturaCommand toCommand() {
        return new AssinarAssinaturaCommand(nomeCompleto, email, cpfCnpj, assinaturaParaOutraPessoa, formaPagamento);
    }

    public void validarParaAssinaturaPropria() {
        if (StringUtils.isEmpty(nomeCompleto)) {
            throw new IllegalArgumentException("O nome completo é obrigatório.");
        }
        if (StringUtils.isEmpty(cpfCnpj)) {
            throw new IllegalArgumentException("O CPF/CNPJ é obrigatório.");
        }
    }

}
