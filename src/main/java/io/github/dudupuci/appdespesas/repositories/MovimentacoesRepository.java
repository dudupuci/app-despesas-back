package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.exceptions.NotFoundException;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.models.entities.base.BaseRepository;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class MovimentacoesRepository implements BaseRepository<Movimentacao> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void salvar(Movimentacao entidade) {
        entityManager.merge(entidade);
    }

    @Override
    public Movimentacao buscarPorId(Long id) throws NotFoundException {
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
