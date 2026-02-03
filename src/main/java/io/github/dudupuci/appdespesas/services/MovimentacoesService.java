package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.config.persistence.Transacional;
import io.github.dudupuci.appdespesas.controllers.dtos.request.movimentacao.CriarMovimentacaoRequestDto;
import io.github.dudupuci.appdespesas.exceptions.*;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.models.enums.TipoPeriodo;
import io.github.dudupuci.appdespesas.repositories.MovimentacoesRepository;
import io.github.dudupuci.appdespesas.repositories.UsuariosRepository;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MovimentacoesService {

    private final MovimentacoesRepository repository;
    private final CategoriasService categoriasService;
    private final UsuariosRepository usuariosRepository;

    public MovimentacoesService(
            MovimentacoesRepository repository,
            CategoriasService categoriasService,
            UsuariosRepository usuariosRepository
    ) {
        this.repository = repository;
        this.categoriasService = categoriasService;
        this.usuariosRepository = usuariosRepository;
    }

    public Movimentacao buscarPorId(Long id) throws EntityNotFoundException {
        return this.repository.buscarPorId(id);
    }

    public List<Movimentacao> listarTodasPorUsuarioId(
            UUID usuarioId,
            TipoMovimentacao tipoMovimentacao,
            TipoPeriodo tipoPeriodo,
            Date dataReferencia
    ) {
        Date dataInicio = null;
        Date dataFim = null;

        // Calcula o período baseado no tipo e data de referência
        if (tipoPeriodo != null && dataReferencia != null) {
            Date[] periodo = AppDespesasUtils.calcularPeriodo(tipoPeriodo, dataReferencia);
            dataInicio = periodo[0];
            dataFim = periodo[1];

            // ⚠️ LOG TEMPORÁRIO PARA DEBUG
            System.out.println("========== DEBUG PERÍODO ==========");
            System.out.println("Data Referência: " + dataReferencia);
            System.out.println("Tipo Período: " + tipoPeriodo);
            System.out.println("Data Início: " + dataInicio);
            System.out.println("Data Fim: " + dataFim);
            System.out.println("===================================");
        }

        return this.repository.listarTodasPorUsuarioId(usuarioId, tipoMovimentacao, dataInicio, dataFim);
    }

    @Transacional
    public Movimentacao criarMovimentacao(CriarMovimentacaoRequestDto dto, UUID usuarioId) {
        Movimentacao movimentacao;
        Categoria tempCategoria;
        UsuarioSistema usuario;

        try {
            // Buscar e validar categoria
            tempCategoria = this.categoriasService.validarCategoriaPorId(dto.categoriaId());

            // Buscar usuário
            usuario = this.usuariosRepository.buscarPorId(usuarioId);
            if (usuario == null) {
                throw new UsuarioNotFoundException("Usuário não encontrado");
            }

            // Criar movimentação
            movimentacao = dto.toMovimentacao();
            movimentacao.setCategoria(tempCategoria);
            movimentacao.setUsuarioSistema(usuario);
            repository.salvar(movimentacao);

        } catch (EntityNotFoundException e) {
            throw new CategoriaNotFoundException(e.getMessage());
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
                this.repository.deletar(movimentacao);
            }
        } catch (EntityNotFoundException e) {
            throw new MovimentacaoNotFoundException(e.getMessage());
        }
    }
}
