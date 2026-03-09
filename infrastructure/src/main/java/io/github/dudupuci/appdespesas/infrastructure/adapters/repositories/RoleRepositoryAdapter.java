package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.RoleRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Role;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaRole;
import io.github.dudupuci.appdespesas.infrastructure.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adapter para RoleRepository
 */
@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final RoleRepository jpaRepository;

    @Override
    public Role save(Role role) {
        JpaRole jpaRole = JpaRole.fromEntity(role);

        if (role.getId() != null) {
            Optional<JpaRole> existing = jpaRepository.findById(role.getId());
            if (existing.isPresent()) {
                JpaRole existingEntity = existing.get();
                existingEntity.updateFromEntity(role);
                jpaRole = existingEntity;
            }
        }

        JpaRole saved = jpaRepository.save(jpaRole);
        return saved.toEntity();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return jpaRepository.findById(id)
                .map(JpaRole::toEntity);
    }

    @Override
    public Role buscarPorNome(String nome) {
        JpaRole jpaRole = jpaRepository.buscarPorNome(nome);
        return jpaRole != null ? jpaRole.toEntity() : null;
    }

    @Override
    public boolean existsByNome(String nome) {
        return jpaRepository.buscarPorNome(nome) != null;
    }
}

