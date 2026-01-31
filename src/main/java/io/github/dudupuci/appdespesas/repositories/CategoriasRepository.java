package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.entities.base.BaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Categoria buscarPorId(Long id) throws EntityNotFoundException {
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
