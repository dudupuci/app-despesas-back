package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.models.entities.base.BaseRepository;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

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
    public Movimentacao buscarPorId(Object id) throws EntityNotFoundException {
        Movimentacao movimentacao = entityManager.find(Movimentacao.class, id);
        if (movimentacao == null) {
            throwEntityNotFound("Movimentação", id);
        }
        return movimentacao;
    }

    public List<Movimentacao> listarTodasPorUsuarioId(UUID usuarioId, TipoMovimentacao tipoMovimentacao) {
        StringBuilder jpql = new StringBuilder("SELECT m FROM Movimentacao m WHERE m.usuarioSistema.id = :usuarioId");

        // Adiciona filtro por tipo se fornecido
        if (tipoMovimentacao != null) {
            jpql.append(" AND m.tipoMovimentacao = :tipoMovimentacao");
        }

        jpql.append(" ORDER BY m.dataDaMovimentacao DESC");

        TypedQuery<Movimentacao> query = entityManager.createQuery(jpql.toString(), Movimentacao.class);
        query.setParameter("usuarioId", usuarioId);

        if (tipoMovimentacao != null) {
            query.setParameter("tipoMovimentacao", tipoMovimentacao);
        }

        return query.getResultList();
    }

    @Override
    public void deletar(Movimentacao movimentacao) {
        entityManager.remove(movimentacao);
    }
}
