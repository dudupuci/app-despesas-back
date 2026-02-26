package io.github.dudupuci.appdespesas.services.webservices.dtos.request;

import io.github.dudupuci.appdespesas.models.entities.Assinatura;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.models.enums.TipoRecursoPago;
import io.github.dudupuci.appdespesas.services.AssinaturaService;
import io.github.dudupuci.appdespesas.services.webservices.enums.BillingType;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;

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
