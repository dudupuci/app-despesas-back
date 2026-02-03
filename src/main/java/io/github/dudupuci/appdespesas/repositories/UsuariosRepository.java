package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.models.entities.base.BaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class UsuariosRepository implements BaseRepository<UsuarioSistema> {

    @PersistenceContext
    private EntityManager entityManager;

    public UsuariosRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void salvar(UsuarioSistema entidade) {
        if (entidade.getId() == null) {
            entityManager.persist(entidade);
        } else {
            entityManager.merge(entidade);
        }
    }

    @Override
    public UsuarioSistema buscarPorId(Object id) {
        return entityManager.find(UsuarioSistema.class, id);
    }

    @Override
    public void deletar(UsuarioSistema entidade) {
        entityManager.remove(entityManager.contains(entidade) ? entidade : entityManager.merge(entidade));
    }


    public UsuarioSistema buscarPorEmail(String email) {
        try {
            String jpql = "SELECT u FROM UsuarioSistema u WHERE u.email = :email";
            return entityManager.createQuery(jpql, UsuarioSistema.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public UsuarioSistema buscarPorNomeUsuario(String nomeUsuario) {
        try {
            String jpql = "SELECT u FROM UsuarioSistema u WHERE u.nomeUsuario = :nomeUsuario";
            return entityManager.createQuery(jpql, UsuarioSistema.class)
                    .setParameter("nomeUsuario", nomeUsuario)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean existePorEmail(String email) {
        String jpql = "SELECT COUNT(u) FROM UsuarioSistema u WHERE u.email = :email";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }

    public boolean existePorNomeUsuario(String nomeUsuario) {
        String jpql = "SELECT COUNT(u) FROM UsuarioSistema u WHERE u.nomeUsuario = :nomeUsuario";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("nomeUsuario", nomeUsuario)
                .getSingleResult();
        return count > 0;
    }

}
