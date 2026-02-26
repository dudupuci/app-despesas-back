package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.models.entities.Assinatura;
import io.github.dudupuci.appdespesas.models.entities.Cobranca;
import io.github.dudupuci.appdespesas.repositories.CobrancaRepository;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

import static io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao.DESPESA;

@Service
public class CobrancaService {

    private final CobrancaRepository cobrancaRepository;
    private final AssinaturaService assinaturaService;

    public CobrancaService(CobrancaRepository cobrancaRepository, AssinaturaService assinaturaService) {
        this.cobrancaRepository = cobrancaRepository;
        this.assinaturaService = assinaturaService;
    }

    public void createCobranca(Cobranca cobranca) {
        this.cobrancaRepository.save(cobranca);
    }

    public void atualizaCobrancaConfirmada(
            Date dataPagamento,
            Cobranca cobranca
    ) {
        cobranca.confirmarCobranca(dataPagamento);
        Cobranca cobrancaConfirmada = this.cobrancaRepository.save(cobranca);

        direcionaCobrancaConfirmadaParaServico(cobrancaConfirmada);
    }

    public Cobranca buscarPorAsaasCobrancaId(String asaasCobrancaId) {
        return this.cobrancaRepository.findByAsaasCobrancaId(asaasCobrancaId)
                .orElseThrow(() -> new EntityNotFoundException("Cobrança não encontrada para o ID do Asaas: " + asaasCobrancaId));

    }

    private void direcionaCobrancaConfirmadaParaServico(Cobranca cobrancaConfirmada) {
        if (AppDespesasUtils.isEntidadeNotNull(cobrancaConfirmada)) {

            switch (cobrancaConfirmada.getTipoRecursoPago()) {
                case ASSINATURA -> {
                    this.assinaturaService.tratarCobrancaConfirmada(cobrancaConfirmada);
                }
                // Se no futuro houver outros tipos de recurso pago, como "SERVICO", "PRODUTO", etc.,
                // eles podem ser tratados aqui
                default ->
                        throw new IllegalArgumentException("Tipo de recurso pago não suportado: " + cobrancaConfirmada.getTipoRecursoPago());
            }
        }
    }
}
