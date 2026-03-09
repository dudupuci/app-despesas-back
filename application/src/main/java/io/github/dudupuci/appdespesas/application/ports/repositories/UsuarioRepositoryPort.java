package io.github.dudupuci.appdespesas.application.ports.repositories;

import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) para o repositório de Usuários
 */
public interface UsuarioRepositoryPort {

    UsuarioSistema save(UsuarioSistema usuario);

    Optional<UsuarioSistema> findById(UUID id);

    Optional<UsuarioSistema> buscarPorEmail(String email);

    Optional<UsuarioSistema> buscarPorNomeUsuario(String nomeUsuario);

    boolean existsByContatoEmail(String email);

    boolean existsByNomeUsuario(String nomeUsuario);

    List<UsuarioSistema> findAll();

    void delete(UsuarioSistema usuario);

    void deleteById(UUID id);
}

