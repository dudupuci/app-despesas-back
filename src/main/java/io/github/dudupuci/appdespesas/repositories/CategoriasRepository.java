package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.models.entities.base.BaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CategoriasRepository implements BaseRepository<Categoria> {

    @PersistenceContext
    private EntityManager entityManager;

    public CategoriasRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(Categoria entidade) {
        entityManager.merge(entidade);
    }

    public List<Categoria> listarCategoriasBySearch(String search) {
        if (search == null || search.trim().isEmpty()) {
            String jpqlAll = "SELECT c FROM Categoria c";
            return entityManager.createQuery(jpqlAll, Categoria.class)
                    .getResultList();
        }

        String jpql = "SELECT c FROM Categoria c " +
                "WHERE LOWER(c.nome) LIKE LOWER(:search) " +
                "OR LOWER(c.descricao) LIKE LOWER(:search)";
        return entityManager.createQuery(jpql, Categoria.class)
                .setParameter("search", "%" + search + "%")
                .getResultList();
    }

    public List<Categoria> listarTodasPorUsuarioId(UUID usuarioId, Object tipoMovimentacao) {
        StringBuilder jpql = new StringBuilder("SELECT c FROM Categoria c WHERE c.usuarioSistema.id = :usuarioId");

        // Adiciona filtro por tipo se fornecido
        if (tipoMovimentacao != null) {
            jpql.append(" AND c.tipoMovimentacao = :tipoMovimentacao");
        }

        jpql.append(" ORDER BY c.dataCriacao DESC");

        TypedQuery<Categoria> query = entityManager.createQuery(jpql.toString(), Categoria.class);
        query.setParameter("usuarioId", usuarioId);

        if (tipoMovimentacao != null) {
            query.setParameter("tipoMovimentacao", tipoMovimentacao);
        }

        return query.getResultList();
    }

    @Override
    public Categoria buscarPorId(Object id) throws EntityNotFoundException {
        Categoria categoria = entityManager.find(Categoria.class, id);
        if (categoria == null) {
            throwEntityNotFound("Categoria", id);
        }
        return categoria;
    }

    public Optional<Categoria> buscarPorNome(String nome) {
        String jpql = "SELECT c FROM Categoria c WHERE c.nome = :nome";
        Categoria categoria = entityManager.createQuery(jpql, Categoria.class)
                .setParameter("nome", nome)
                .getResultStream()
                .findFirst()
                .orElse(null);
        return Optional.ofNullable(categoria);
    }

    @Override
    public void deletar(Categoria categoria) {
        entityManager.remove(categoria);
    }
}
