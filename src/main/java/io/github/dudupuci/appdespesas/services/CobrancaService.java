package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.models.entities.Cobranca;
import io.github.dudupuci.appdespesas.repositories.CobrancaRepository;
import org.springframework.stereotype.Service;

@Service
public class CobrancaService {

    private final CobrancaRepository cobrancaRepository;

    public CobrancaService(CobrancaRepository cobrancaRepository) {
        this.cobrancaRepository = cobrancaRepository;
    }

    public Cobranca createCobranca(Cobranca cobranca) {
        return this.cobrancaRepository.save(cobranca);
    }
}
