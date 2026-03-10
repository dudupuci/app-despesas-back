package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.CategoriaRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.CategoriaJpaEntity;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.UsuarioSistemaJpaEntity;
import io.github.dudupuci.appdespesas.infrastructure.repositories.CategoriaJpaRepository;
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

    private final CategoriaJpaRepository jpaRepository;

    @Override
    public Categoria save(Categoria categoria) {
        CategoriaJpaEntity categoriaJpaEntity = CategoriaJpaEntity.fromEntity(categoria);

        if (categoria.getId() != null) {
            Optional<CategoriaJpaEntity> existing = jpaRepository.findById(categoria.getId());
            if (existing.isPresent()) {
                CategoriaJpaEntity existingEntity = existing.get();
                existingEntity.updateFromEntity(categoria);
                categoriaJpaEntity = existingEntity;
            }
        }

        CategoriaJpaEntity saved = jpaRepository.save(categoriaJpaEntity);
        return saved.toEntity();
    }

    @Override
    public Optional<Categoria> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(CategoriaJpaEntity::toEntity);
    }

    @Override
    public List<Categoria> listarCategoriasBySearch(String search) {
        return jpaRepository.listarCategoriasBySearch(search).stream()
                .map(CategoriaJpaEntity::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Categoria> listarTodasPorUsuarioId(UUID usuarioId, TipoMovimentacao tipoMovimentacao) {
        return jpaRepository.listarTodasPorUsuarioId(usuarioId, tipoMovimentacao).stream()
                .map(CategoriaJpaEntity::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Categoria> buscarPorNome(String nome) {
        return jpaRepository.buscarPorNome(nome)
                .map(CategoriaJpaEntity::toEntity);
    }

    @Override
    public Categoria buscarPorNomeEUsuario(String nome, UsuarioSistema usuarioSistema) {
        UsuarioSistemaJpaEntity jpaUsuario = UsuarioSistemaJpaEntity.fromEntity(usuarioSistema);
        CategoriaJpaEntity categoriaJpaEntity = jpaRepository.buscarPorNomeEUsuario(nome, jpaUsuario);
        return categoriaJpaEntity != null ? categoriaJpaEntity.toEntity() : null;
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

