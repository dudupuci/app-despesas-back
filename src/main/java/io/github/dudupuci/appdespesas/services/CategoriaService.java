package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.config.persistence.Transacional;
import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.categoria.CriarCategoriaRequestDto;
import io.github.dudupuci.appdespesas.exceptions.*;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.entities.Cor;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.repositories.CategoriasRepository;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import org.springframework.stereotype.Service;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoriaService {

    private final CategoriasRepository repository;
    private final UsuarioService usuarioService;
    private final CorService corService;

    public CategoriaService(
            CategoriasRepository repository,
            UsuarioService usuarioService,
            CorService corService
    ) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.corService = corService;
    }

    public List<Categoria> listarCategoriasBySearch(String search) {
        return this.repository.listarCategoriasBySearch(search);
    }

    @Transacional
    public Categoria createCategoria(CriarCategoriaRequestDto dto, UUID usuarioId) {
        Cor cor;

        validarCriacao(dto);
        UsuarioSistema usuarioSistema = this.usuarioService.buscarPorId(usuarioId);

        Categoria categoria = dto.toCategoria();
        categoria.setUsuarioSistema(usuarioSistema);

        // Associar cor se fornecida
        // Se o ID da cor for fornecido, buscar a cor e associar
        // Se o ID da cor não for fornecido, associar a cor padrão "Roxo"
        if (dto.corId() != null) {
            cor = corService.buscarPorId(dto.corId(), usuarioId);
            categoria.setCor(cor);
        }

        this.repository.save(categoria);
        return categoria;

    }

    @Transacional
    public Categoria updateCategoria(UUID id, CriarCategoriaRequestDto dto, UUID usuarioId) {
        Cor cor;

        // Buscar categoria existente
        Categoria categoria = buscarPorId(id);

        // Verificar se o usuário é o dono da categoria
        UsuarioSistema usuarioSistema = this.usuarioService.buscarPorId(usuarioId);

        if (!categoria.getUsuarioSistema().getId().equals(usuarioId)) {
            throw new RuntimeException("Você não tem permissão para editar esta categoria");
        }

        // Atualizar campos
        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        categoria.setTipoMovimentacao(dto.tipoMovimentacao());

        // Associar cor se fornecida
        if (dto.corId() != null) {
            cor = corService.buscarPorId(dto.corId(), usuarioId);
            categoria.setCor(cor);
        }

        this.repository.save(categoria);
        return categoria;
    }

    public Categoria buscarPorId(UUID id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria com ID " + id + " não encontrada"));
    }

    public List<Categoria> listarTodasPorUsuarioId(
            UUID usuarioId,
            TipoMovimentacao tipoMovimentacao
    ) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        return this.repository.listarTodasPorUsuarioId(usuario.getId(), tipoMovimentacao);
    }

    private void validarCriacao(CriarCategoriaRequestDto dto) {
        Optional<Categoria> categoria = this.repository.buscarPorNome(dto.nome());

        if (categoria.isPresent()) {
            throw new EntityAlreadyExistsException("Categoria com o nome '" + dto.nome() + "' já existe.");
        }

    }

    public Categoria validarCategoriaPorId(UUID categoriaId) throws EntityNotFoundException, CategoriaInativaException {
        Categoria categoria = buscarPorId(categoriaId);

        if (!AppDespesasUtils.isCategoriaAtiva(categoria)) {
            throw new CategoriaInativaException(AppDespesasMessages.getMessage(
                    "categoria.inativa",
                    new Object[]{categoria.getNome()})
            );
        }

        return categoria;
    }

    @Transacional
    public void deletar(UUID id) {
        try {
            Categoria categoria = buscarPorId(id);

            if (AppDespesasUtils.isEntidadeNotNull(categoria)) {
                this.repository.delete(categoria);
            }
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
