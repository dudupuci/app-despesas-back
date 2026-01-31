package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.models.entities.base.BaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MovimentacoesRepository implements BaseRepository<Movimentacao> {

    @PersistenceContext
    private EntityManager entityManager;

    public MovimentacoesRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void salvar(Movimentacao entidade) {
        entityManager.merge(entidade);
    }

    @Override
    public Movimentacao buscarPorId(Long id) throws EntityNotFoundException {
        Movimentacao movimentacao = entityManager.find(Movimentacao.class, id);
        if (movimentacao == null) {
            throwEntityNotFound("Movimentação", id);
        }
        return movimentacao;
    }

    @Override
    public void deletar(Movimentacao movimentacao) {
        entityManager.remove(movimentacao);
    }
}
