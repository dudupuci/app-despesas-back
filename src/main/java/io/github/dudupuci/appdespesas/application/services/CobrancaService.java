package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.domain.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.infrastructure.repositories.CobrancaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CobrancaService {

    private final CobrancaRepository cobrancaRepository;

    public CobrancaService(CobrancaRepository cobrancaRepository) {
        this.cobrancaRepository = cobrancaRepository;
    }

    public void createOrUpdate(Cobranca cobranca) {
        this.cobrancaRepository.save(cobranca);
    }

    public Cobranca atualizaCobrancaConfirmada(
            Date dataPagamento,
            String asaasCobrancaId
    ) {
        Cobranca cobranca = this.cobrancaRepository.findByAsaasCobrancaId(asaasCobrancaId)
                .orElseThrow(() -> new EntityNotFoundException("Cobrança não encontrada para o ID do pagamento do Asaas: " + asaasCobrancaId));
        cobranca.confirmarCobranca(dataPagamento);
        return this.cobrancaRepository.save(cobranca);
    }

}
