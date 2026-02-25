package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.models.entities.Assinatura;
import io.github.dudupuci.appdespesas.repositories.AssinaturaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssinaturaService {

    private final AssinaturaRepository assinaturaRepository;

    public AssinaturaService(
            AssinaturaRepository assinaturaRepository
    ) {
        this.assinaturaRepository = assinaturaRepository;
    }

    public List<Assinatura> listarAssinaturas() {
        return this.assinaturaRepository.findAll();
    }

    public Assinatura buscarAssinaturaPorId(Long id) {
        return this.assinaturaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assinatura não encontrada"));
    }


}
