package io.github.dudupuci.appdespesas.services.webservices.dtos.request;

import io.github.dudupuci.appdespesas.models.entities.Assinatura;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.services.AssinaturaService;
import io.github.dudupuci.appdespesas.services.webservices.enums.BillingType;

import java.math.BigDecimal;
import java.util.Date;

public record CriarCobrancaAsaasRequestDto(
        String customer,
        BillingType billingType,
        BigDecimal value,
        String dueDate,
        String description,
        String externalReference
) {

    public static CriarCobrancaAsaasRequestDto fromObjects(
            UsuarioSistema usuarioSistema,
            Assinatura assinaturaCobranca,
            BillingType formaPagamento
    ) {
        return new CriarCobrancaAsaasRequestDto(
                usuarioSistema.getAsaasCustomerId(),
                formaPagamento,
                assinaturaCobranca.getValor(),
                calculaDataVencimento(formaPagamento),
                "Assinatura: " + assinaturaCobranca.getNomePlano() +
                        " para: " + usuarioSistema.getNomeCompleto(),
                String.valueOf(usuarioSistema.getId())
        );
    }

    private static String calculaDataVencimento(BillingType formaPagamento) {
        Date dataAtual = new Date();
        Date dataVencimento;

        switch (formaPagamento) {
            // Para PIX, o vencimento é em 15 minutos
            case PIX -> dataVencimento = new Date(dataAtual.getTime() + 15 * 60 * 1000);
            // Para BOLETO, o vencimento é em 3 dias
            case BOLETO -> dataVencimento = new Date(dataAtual.getTime() + 3 * 24 * 60 * 60 * 1000);
            default -> throw new IllegalArgumentException("Forma de pagamento não suportada: " + formaPagamento);
        }

        // Formata a data para o formato esperado pelo Asaas (YYYY-MM-DD)
        return String.format("%tF", dataVencimento);
    }
}
