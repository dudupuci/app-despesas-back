package io.github.dudupuci.appdespesas.application.ports.repositories;

import io.github.dudupuci.appdespesas.domain.entities.Role;

import java.util.Optional;

/**
 * Port (interface) para o repositório de Roles
 */
public interface RoleRepositoryPort {

    Role save(Role role);

    Optional<Role> findById(Long id);

    Role buscarPorNome(String nome);

    boolean existsByNome(String nome);
}

