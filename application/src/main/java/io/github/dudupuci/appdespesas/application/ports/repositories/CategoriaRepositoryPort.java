package io.github.dudupuci.appdespesas.application.ports.repositories;

import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) para o repositório de Categorias
 */
public interface CategoriaRepositoryPort {

    Categoria save(Categoria categoria);

    Optional<Categoria> findById(UUID id);

    List<Categoria> listarCategoriasBySearch(String search);

    List<Categoria> listarTodasPorUsuarioId(UUID usuarioId, TipoMovimentacao tipoMovimentacao);

    Optional<Categoria> buscarPorNome(String nome);

    Categoria buscarPorNomeEUsuario(String nome, UsuarioSistema usuarioSistema);

    void delete(Categoria categoria);

    void deleteById(UUID id);
}

