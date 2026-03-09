package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.MovimentacaoRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Movimentacao;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaMovimentacao;
import io.github.dudupuci.appdespesas.infrastructure.repositories.MovimentacoesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter para MovimentacaoRepository
 */
@Component
@RequiredArgsConstructor
public class MovimentacaoRepositoryAdapter implements MovimentacaoRepositoryPort {

    private final MovimentacoesRepository jpaRepository;

    @Override
    public Movimentacao save(Movimentacao movimentacao) {
        JpaMovimentacao jpaMovimentacao = JpaMovimentacao.fromEntity(movimentacao);

        if (movimentacao.getId() != null) {
            Optional<JpaMovimentacao> existing = jpaRepository.findById(movimentacao.getId());
            if (existing.isPresent()) {
                JpaMovimentacao existingEntity = existing.get();
                existingEntity.updateFromEntity(movimentacao);
                jpaMovimentacao = existingEntity;
            }
        }

        JpaMovimentacao saved = jpaRepository.save(jpaMovimentacao);
        return saved.toEntity();
    }

    @Override
    public Optional<Movimentacao> findById(Long id) {
        return jpaRepository.findById(id)
                .map(JpaMovimentacao::toEntity);
    }

    @Override
    public List<Movimentacao> listarTodasPorUsuarioId(UUID usuarioId) {
        return jpaRepository.listarTodasPorUsuarioId(usuarioId).stream()
                .map(JpaMovimentacao::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movimentacao> listarPorUsuarioIdETipo(UUID usuarioId, TipoMovimentacao tipoMovimentacao) {
        return jpaRepository.listarPorUsuarioIdETipo(usuarioId, tipoMovimentacao).stream()
                .map(JpaMovimentacao::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movimentacao> listarPorUsuarioIdEPeriodo(UUID usuarioId, Date dataInicio, Date dataFim) {
        return jpaRepository.listarPorUsuarioIdEPeriodo(usuarioId, dataInicio, dataFim).stream()
                .map(JpaMovimentacao::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movimentacao> listarPorUsuarioIdTipoEPeriodo(UUID usuarioId, TipoMovimentacao tipoMovimentacao, Date dataInicio, Date dataFim) {
        return jpaRepository.listarPorUsuarioIdTipoEPeriodo(usuarioId, tipoMovimentacao, dataInicio, dataFim).stream()
                .map(JpaMovimentacao::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movimentacao> buscarPorData(UUID usuarioId, Date data) {
        return jpaRepository.buscarPorData(usuarioId, data).stream()
                .map(JpaMovimentacao::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Movimentacao movimentacao) {
        if (movimentacao.getId() != null) {
            jpaRepository.deleteById(movimentacao.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

