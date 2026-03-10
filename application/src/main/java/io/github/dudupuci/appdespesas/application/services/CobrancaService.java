package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.ports.repositories.CobrancaRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.domain.exceptions.EntityNotFoundException;

import java.util.Date;

public class CobrancaService {

    private final CobrancaRepositoryPort cobrancaRepository;

    public CobrancaService(CobrancaRepositoryPort cobrancaRepository) {
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
                .orElseThrow(() -> new EntityNotFoundException("Cobrança não encontrada para o ID: " + asaasCobrancaId));
        cobranca.confirmarCobranca(dataPagamento);
        return this.cobrancaRepository.save(cobranca);
    }
}
