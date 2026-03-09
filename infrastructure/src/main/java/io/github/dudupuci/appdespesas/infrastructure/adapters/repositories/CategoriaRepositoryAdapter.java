package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.CategoriaRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaCategoria;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaUsuarioSistema;
import io.github.dudupuci.appdespesas.infrastructure.repositories.CategoriasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter para CategoriaRepository
 */
@Component
@RequiredArgsConstructor
public class CategoriaRepositoryAdapter implements CategoriaRepositoryPort {

    private final CategoriasRepository jpaRepository;

    @Override
    public Categoria save(Categoria categoria) {
        JpaCategoria jpaCategoria = JpaCategoria.fromEntity(categoria);

        if (categoria.getId() != null) {
            Optional<JpaCategoria> existing = jpaRepository.findById(categoria.getId());
            if (existing.isPresent()) {
                JpaCategoria existingEntity = existing.get();
                existingEntity.updateFromEntity(categoria);
                jpaCategoria = existingEntity;
            }
        }

        JpaCategoria saved = jpaRepository.save(jpaCategoria);
        return saved.toEntity();
    }

    @Override
    public Optional<Categoria> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(JpaCategoria::toEntity);
    }

    @Override
    public List<Categoria> listarCategoriasBySearch(String search) {
        return jpaRepository.listarCategoriasBySearch(search).stream()
                .map(JpaCategoria::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Categoria> listarTodasPorUsuarioId(UUID usuarioId, TipoMovimentacao tipoMovimentacao) {
        return jpaRepository.listarTodasPorUsuarioId(usuarioId, tipoMovimentacao).stream()
                .map(JpaCategoria::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Categoria> buscarPorNome(String nome) {
        return jpaRepository.buscarPorNome(nome)
                .map(JpaCategoria::toEntity);
    }

    @Override
    public Categoria buscarPorNomeEUsuario(String nome, UsuarioSistema usuarioSistema) {
        JpaUsuarioSistema jpaUsuario = JpaUsuarioSistema.fromEntity(usuarioSistema);
        JpaCategoria jpaCategoria = jpaRepository.buscarPorNomeEUsuario(nome, jpaUsuario);
        return jpaCategoria != null ? jpaCategoria.toEntity() : null;
    }

    @Override
    public void delete(Categoria categoria) {
        if (categoria.getId() != null) {
            jpaRepository.deleteById(categoria.getId());
        }
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}

