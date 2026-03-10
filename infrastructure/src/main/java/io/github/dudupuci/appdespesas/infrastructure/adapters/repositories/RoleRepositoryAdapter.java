package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.RoleRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Role;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.RoleJpaEntity;
import io.github.dudupuci.appdespesas.infrastructure.repositories.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter para RoleJpaRepository
 */
@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final RoleJpaRepository jpaRepository;

    @Override
    public Role save(Role role) {
        RoleJpaEntity roleJpaEntity = RoleJpaEntity.fromEntity(role);

        if (role.getId() != null) {
            Optional<RoleJpaEntity> existing = jpaRepository.findById(role.getId());
            if (existing.isPresent()) {
                RoleJpaEntity existingEntity = existing.get();
                existingEntity.updateFromEntity(role);
                roleJpaEntity = existingEntity;
            }
        }

        RoleJpaEntity saved = jpaRepository.save(roleJpaEntity);
        return saved.toEntity();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return jpaRepository.findById(id)
                .map(RoleJpaEntity::toEntity);
    }

    @Override
    public Role buscarPorNome(String nome) {
        RoleJpaEntity roleJpaEntity = jpaRepository.buscarPorNome(nome);
        return roleJpaEntity != null ? roleJpaEntity.toEntity() : null;
    }

    @Override
    public boolean existsByNome(String nome) {
        return jpaRepository.buscarPorNome(nome) != null;
    }

    @Override
    public List<Role> findAll() {
        return jpaRepository.findAll().stream()
                .map(RoleJpaEntity::toEntity)
                .collect(Collectors.toList());
    }
}

