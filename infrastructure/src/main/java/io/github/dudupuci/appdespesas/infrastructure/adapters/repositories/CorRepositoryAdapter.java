package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.CorRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Cor;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaCor;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaUsuarioSistema;
import io.github.dudupuci.appdespesas.infrastructure.repositories.CorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CorRepositoryAdapter implements CorRepositoryPort {

    private final CorRepository jpaRepository;

    @Override
    public Cor save(Cor cor) {
        JpaCor jpaCor = JpaCor.fromEntity(cor);

        if (cor.getId() != null) {
            Optional<JpaCor> existing = jpaRepository.findById(cor.getId());
            if (existing.isPresent()) {
                JpaCor existingEntity = existing.get();
                existingEntity.updateFromEntity(cor);
                jpaCor = existingEntity;
            }
        }

        // Setar usuario JPA se existir
        if (cor.getUsuarioSistema() != null && cor.getUsuarioSistema().getId() != null) {
            JpaUsuarioSistema jpaUsuario = JpaUsuarioSistema.fromEntity(cor.getUsuarioSistema());
            jpaCor.setUsuarioSistema(jpaUsuario);
        }

        JpaCor saved = jpaRepository.save(jpaCor);
        return saved.toEntity();
    }

    @Override
    public Optional<Cor> findById(UUID id) {
        return jpaRepository.findById(id).map(JpaCor::toEntity);
    }

    @Override
    public List<Cor> findByUsuarioSistema(UsuarioSistema usuarioSistema) {
        JpaUsuarioSistema jpaUsuario = JpaUsuarioSistema.fromEntity(usuarioSistema);
        return jpaRepository.findByUsuarioSistema(jpaUsuario).stream()
                .map(JpaCor::toEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<Cor> findByNome(String nome) {
        return jpaRepository.findByNome(nome).map(JpaCor::toEntity);
    }

    @Override
    public List<Cor> listarTodasPorUsuarioId(UUID usuarioId) {
        return jpaRepository.listarTodasPorUsuarioId(usuarioId).stream()
                .map(JpaCor::toEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<Cor> findByIdAndUsuarioSistema(UUID id, UsuarioSistema usuarioSistema) {
        JpaUsuarioSistema jpaUsuario = JpaUsuarioSistema.fromEntity(usuarioSistema);
        return jpaRepository.findByIdAndUsuarioSistema(id, jpaUsuario).map(JpaCor::toEntity);
    }

    @Override
    public boolean existsByNomeAndUsuarioSistema(String nome, UsuarioSistema usuarioSistema) {
        JpaUsuarioSistema jpaUsuario = JpaUsuarioSistema.fromEntity(usuarioSistema);
        return jpaRepository.existsByNomeAndUsuarioSistema(nome, jpaUsuario);
    }

    @Override
    public boolean existsByCodigoHexadecimalAndUsuarioSistema(String codigoHexadecimal, UsuarioSistema usuarioSistema) {
        JpaUsuarioSistema jpaUsuario = JpaUsuarioSistema.fromEntity(usuarioSistema);
        return jpaRepository.existsByCodigoHexadecimalAndUsuarioSistema(codigoHexadecimal, jpaUsuario);
    }

    @Override
    public List<Cor> findByUsuarioSistemaAndNomeContainingIgnoreCase(UsuarioSistema usuarioSistema, String nome) {
        JpaUsuarioSistema jpaUsuario = JpaUsuarioSistema.fromEntity(usuarioSistema);
        return jpaRepository.findByUsuarioSistemaAndNomeContainingIgnoreCase(jpaUsuario, nome).stream()
                .map(JpaCor::toEntity).collect(Collectors.toList());
    }

    @Override
    public void delete(Cor cor) {
        if (cor.getId() != null) {
            jpaRepository.deleteById(cor.getId());
        }
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}

