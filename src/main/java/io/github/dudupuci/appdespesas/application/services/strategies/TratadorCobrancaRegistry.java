package io.github.dudupuci.appdespesas.application.services.strategies;

import io.github.dudupuci.appdespesas.domain.enums.TipoRecursoPago;
import io.github.dudupuci.appdespesas.application.services.interfaces.TratadorCobrancaStrategy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TratadorCobrancaRegistry {

    private final Map<TipoRecursoPago, TratadorCobrancaStrategy> strategies = new HashMap<>();

    public TratadorCobrancaRegistry(List<TratadorCobrancaStrategy> strategyList) {
        for (TratadorCobrancaStrategy strategy : strategyList) {
            strategies.put(strategy.getTipoSuportado(), strategy);
        }
    }

    public TratadorCobrancaStrategy getStrategy(TipoRecursoPago tipo) {
        TratadorCobrancaStrategy strategy = strategies.get(tipo);

        if (strategy == null) {
            throw new IllegalArgumentException("Nenhum tratador encontrado para o tipo: " + tipo);
        }

        return strategy;
    }
}