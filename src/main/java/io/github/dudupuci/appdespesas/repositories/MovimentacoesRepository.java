package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.models.entities.base.BaseRepository;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    public List<Movimentacao> listarTodasPorUsuarioId(
            UUID usuarioId,
            TipoMovimentacao tipoMovimentacao,
            Date dataInicio,
            Date dataFim
    ) {
        StringBuilder jpql = new StringBuilder("SELECT m FROM Movimentacao m WHERE m.usuarioSistema.id = :usuarioId");

        // Adiciona filtro por tipo se fornecido
        if (tipoMovimentacao != null) {
            jpql.append(" AND m.tipoMovimentacao = :tipoMovimentacao");
        }

        // Adiciona filtro por período se fornecido
        if (dataInicio != null && dataFim != null) {
            jpql.append(" AND m.dataDaMovimentacao BETWEEN :dataInicio AND :dataFim");
        }

        jpql.append(" ORDER BY m.dataDaMovimentacao DESC");

        // ⚠️ LOG TEMPORÁRIO PARA DEBUG
        System.out.println("========== DEBUG QUERY ==========");
        System.out.println("JPQL: " + jpql.toString());
        System.out.println("usuarioId: " + usuarioId);
        System.out.println("tipoMovimentacao: " + tipoMovimentacao);
        System.out.println("dataInicio: " + dataInicio);
        System.out.println("dataFim: " + dataFim);
        System.out.println("=================================");

        TypedQuery<Movimentacao> query = entityManager.createQuery(jpql.toString(), Movimentacao.class);
        query.setParameter("usuarioId", usuarioId);

        if (tipoMovimentacao != null) {
            query.setParameter("tipoMovimentacao", tipoMovimentacao);
        }

        if (dataInicio != null && dataFim != null) {
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
        }

        List<Movimentacao> resultado = query.getResultList();

        // ⚠️ LOG TEMPORÁRIO PARA DEBUG
        System.out.println("========== DEBUG RESULTADO ==========");
        System.out.println("Total de movimentações encontradas: " + resultado.size());
        System.out.println("=====================================");

        return resultado;
    }

    @Override
    public void deletar(Movimentacao movimentacao) {
        entityManager.remove(movimentacao);
    }
}
