package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.models.entities.Role;
import io.github.dudupuci.appdespesas.models.entities.base.BaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RolesRepository implements BaseRepository<Role> {

    @PersistenceContext
    private final EntityManager entityManager;

    public RolesRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(Role entidade) {
        if (entidade.getId() == null) {
            entityManager.persist(entidade);
        } else {
            entityManager.merge(entidade);
        }
    }

    @Override
    public Role buscarPorId(Long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public void deletar(Role entidade) {
        entityManager.remove(entityManager.contains(entidade) ? entidade : entityManager.merge(entidade));
    }

    public Role buscarPorNome(String nome) {
        try {
            String jpql = "SELECT r FROM Role r WHERE UPPER(r.nome) = UPPER(:nome)";
            return entityManager.createQuery(jpql, Role.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Role> buscarTodos() {
        String jpql = "SELECT r FROM Role r";
        return entityManager.createQuery(jpql, Role.class)
                .getResultList();
    }
}
