package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.CorRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Cor;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.CorJpaEntity;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.UsuarioSistemaJpaEntity;
import io.github.dudupuci.appdespesas.infrastructure.repositories.CorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CorRepositoryAdapter implements CorRepositoryPort {

    private final CorJpaRepository jpaRepository;

    @Transactional
    @Override
    public Cor save(Cor cor) {
        CorJpaEntity corJpaEntity = CorJpaEntity.fromEntity(cor);

        if (cor.getId() != null) {
            Optional<CorJpaEntity> existing = jpaRepository.findById(cor.getId());
            if (existing.isPresent()) {
                CorJpaEntity existingEntity = existing.get();
                existingEntity.updateFromEntity(cor);
                corJpaEntity = existingEntity;
            }
        }

        // Setar usuario JPA se existir
        if (cor.getUsuarioSistema() != null && cor.getUsuarioSistema().getId() != null) {
            UsuarioSistemaJpaEntity jpaUsuario = UsuarioSistemaJpaEntity.fromEntity(cor.getUsuarioSistema());
            corJpaEntity.setUsuarioSistema(jpaUsuario);
        }

        CorJpaEntity saved = jpaRepository.save(corJpaEntity);
        return saved.toEntity();
    }

    @Override
    public Optional<Cor> findById(UUID id) {
        return jpaRepository.findById(id).map(CorJpaEntity::toEntity);
    }

    @Override
    public List<Cor> findByUsuarioSistema(UsuarioSistema usuarioSistema) {
        UsuarioSistemaJpaEntity jpaUsuario = UsuarioSistemaJpaEntity.fromEntity(usuarioSistema);
        return jpaRepository.findByUsuarioSistema(jpaUsuario).stream()
                .map(CorJpaEntity::toEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<Cor> findByNome(String nome) {
        return jpaRepository.findByNome(nome).map(CorJpaEntity::toEntity);
    }

    @Override
    public List<Cor> listarTodasPorUsuarioId(UUID usuarioId) {
        return jpaRepository.listarTodasPorUsuarioId(usuarioId).stream()
                .map(CorJpaEntity::toEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<Cor> findByIdAndUsuarioSistema(UUID id, UsuarioSistema usuarioSistema) {
        UsuarioSistemaJpaEntity jpaUsuario = UsuarioSistemaJpaEntity.fromEntity(usuarioSistema);
        return jpaRepository.findByIdAndUsuarioSistema(id, jpaUsuario).map(CorJpaEntity::toEntity);
    }

    @Override
    public boolean existsByNomeAndUsuarioSistema(String nome, UsuarioSistema usuarioSistema) {
        UsuarioSistemaJpaEntity jpaUsuario = UsuarioSistemaJpaEntity.fromEntity(usuarioSistema);
        return jpaRepository.existsByNomeAndUsuarioSistema(nome, jpaUsuario);
    }

    @Override
    public boolean existsByCodigoHexadecimalAndUsuarioSistema(String codigoHexadecimal, UsuarioSistema usuarioSistema) {
        UsuarioSistemaJpaEntity jpaUsuario = UsuarioSistemaJpaEntity.fromEntity(usuarioSistema);
        return jpaRepository.existsByCodigoHexadecimalAndUsuarioSistema(codigoHexadecimal, jpaUsuario);
    }

    @Override
    public List<Cor> findByUsuarioSistemaAndNomeContainingIgnoreCase(UsuarioSistema usuarioSistema, String nome) {
        UsuarioSistemaJpaEntity jpaUsuario = UsuarioSistemaJpaEntity.fromEntity(usuarioSistema);
        return jpaRepository.findByUsuarioSistemaAndNomeContainingIgnoreCase(jpaUsuario, nome).stream()
                .map(CorJpaEntity::toEntity).collect(Collectors.toList());
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

