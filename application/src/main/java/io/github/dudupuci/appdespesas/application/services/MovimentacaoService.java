package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.infrastructure.config.persistence.Transacional;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.movimentacao.CriarMovimentacaoRequestDto;
import io.github.dudupuci.appdespesas.domain.exceptions.CategoriaInativaException;
import io.github.dudupuci.appdespesas.domain.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.domain.exceptions.UsuarioNotFoundException;
import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.entities.Movimentacao;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.infrastructure.repositories.MovimentacoesRepository;
import io.github.dudupuci.appdespesas.infrastructure.repositories.UsuariosRepository;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovimentacaoService {

    private final MovimentacoesRepository repository;
    private final CategoriaService categoriaService;
    private final UsuariosRepository usuariosRepository;

    public MovimentacaoService(
            MovimentacoesRepository repository,
            CategoriaService categoriaService,
            UsuariosRepository usuariosRepository
    ) {
        this.repository = repository;
        this.categoriaService = categoriaService;
        this.usuariosRepository = usuariosRepository;
    }

    public Movimentacao buscarPorId(Long id) throws EntityNotFoundException {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimentação com ID " + id + " não encontrada"));
    }

    public List<Movimentacao> listarTodasPorUsuarioId(
            UUID usuarioId,
            TipoMovimentacao tipoMovimentacao,
            Date dataInicio,
            Date dataFim
    ) {

        // Chama o método correto do repository baseado nos filtros
        if (tipoMovimentacao != null && dataInicio != null && dataFim != null) {
            // Filtro por tipo E período
            return this.repository.listarPorUsuarioIdTipoEPeriodo(usuarioId, tipoMovimentacao, dataInicio, dataFim);
        } else if (tipoMovimentacao != null) {
            // Filtro apenas por tipo
            return this.repository.listarPorUsuarioIdETipo(usuarioId, tipoMovimentacao);
        } else if (dataInicio != null && dataFim != null) {
            // Filtro apenas por período
            return this.repository.listarPorUsuarioIdEPeriodo(usuarioId, dataInicio, dataFim);
        } else {
            // Sem filtros
            return this.repository.listarTodasPorUsuarioId(usuarioId);
        }
    }

    @Transacional
    public Movimentacao criarMovimentacao(CriarMovimentacaoRequestDto dto, UUID usuarioId) {
        Movimentacao movimentacao;
        Categoria tempCategoria;
        Optional<UsuarioSistema> usuario;

        try {
            // Buscar e validar categoria
            tempCategoria = this.categoriaService.validarCategoriaPorId(dto.categoriaId());

            // Buscar usuário
            usuario = this.usuariosRepository.findById(usuarioId);
            if (usuario.isEmpty()) {
                throw new UsuarioNotFoundException("Usuário não encontrado");
            }

            // Criar movimentação
            movimentacao = dto.toMovimentacao();
            movimentacao.setCategoria(tempCategoria);
            movimentacao.setUsuarioSistema(usuario.get());
            repository.save(movimentacao);

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        } catch (CategoriaInativaException e) {
            throw new CategoriaInativaException(e.getMessage());
        }

        return movimentacao;
    }

    @Transacional
    public void deletar(Long id) {
        try {
            Movimentacao movimentacao = buscarPorId(id);

            if (AppDespesasUtils.isEntidadeNotNull(movimentacao)) {
                this.repository.delete(movimentacao);
            }
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

}
