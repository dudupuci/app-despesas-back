package io.github.dudupuci.appdespesas.application.ports.repositories;

import io.github.dudupuci.appdespesas.domain.entities.Cor;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) para o repositório de Cores
 */
public interface CorRepositoryPort {

    Cor save(Cor cor);

    Optional<Cor> findById(UUID id);

    List<Cor> findByUsuarioSistema(UsuarioSistema usuarioSistema);

    Optional<Cor> findByNome(String nome);

    List<Cor> listarTodasPorUsuarioId(UUID usuarioId);

    Optional<Cor> findByIdAndUsuarioSistema(UUID id, UsuarioSistema usuarioSistema);

    boolean existsByNomeAndUsuarioSistema(String nome, UsuarioSistema usuarioSistema);

    boolean existsByCodigoHexadecimalAndUsuarioSistema(String codigoHexadecimal, UsuarioSistema usuarioSistema);

    List<Cor> findByUsuarioSistemaAndNomeContainingIgnoreCase(UsuarioSistema usuarioSistema, String nome);

    void delete(Cor cor);

    void deleteById(UUID id);
}

