package io.github.dudupuci.appdespesas.application.services.webservices.dtos.request;

import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.enums.TipoRecursoPago;
import io.github.dudupuci.appdespesas.application.services.webservices.enums.BillingType;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasUtils;

import java.math.BigDecimal;

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
                AppDespesasUtils.calculaDataVencimentoPorFormaPagamentoAndTipoCobranca(
                        formaPagamento,
                        TipoRecursoPago.ASSINATURA
                ),
                "Assinatura: " + assinaturaCobranca.getNomePlano() +
                        " para: " + usuarioSistema.getNomeCompleto(),
                String.valueOf(usuarioSistema.getId())
        );
    }

}
