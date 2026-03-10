package io.github.dudupuci.appdespesas.application.usecases.movimentacao;

import io.github.dudupuci.appdespesas.application.ports.repositories.MovimentacaoRepositoryPort;
import io.github.dudupuci.appdespesas.application.ports.repositories.UsuarioRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CategoriaService;
import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.entities.Movimentacao;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.exceptions.UsuarioNotFoundException;

import java.util.Optional;

public class CriarMovimentacaoUseCaseImpl extends CriarMovimentacaoUseCase {

    private final MovimentacaoRepositoryPort repository;
    private final CategoriaService categoriaService;
    private final UsuarioRepositoryPort usuariosRepository;

    public CriarMovimentacaoUseCaseImpl(MovimentacaoRepositoryPort repository,
                                        CategoriaService categoriaService,
                                        UsuarioRepositoryPort usuariosRepository) {
        this.repository = repository;
        this.categoriaService = categoriaService;
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    public Movimentacao executar(MovimentacaoCommand cmd) {
        Categoria categoria = categoriaService.validarCategoriaPorId(cmd.categoriaId());
        Optional<UsuarioSistema> usuario = usuariosRepository.findById(cmd.usuarioId());
        if (usuario.isEmpty()) throw new UsuarioNotFoundException("Usuário não encontrado");

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setTitulo(cmd.titulo());
        movimentacao.setDescricao(cmd.descricao());
        movimentacao.setValor(cmd.valor());
        movimentacao.setDataDaMovimentacao(cmd.dataDaMovimentacao());
        movimentacao.setTipoMovimentacao(cmd.tipoMovimentacao());
        movimentacao.setCategoria(categoria);
        movimentacao.setUsuarioSistema(usuario.get());
        return repository.save(movimentacao);
    }
}
