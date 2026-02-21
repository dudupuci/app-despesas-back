package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.config.persistence.Transacional;
import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.categoria.CriarCategoriaRequestDto;
import io.github.dudupuci.appdespesas.exceptions.*;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.entities.Cor;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.repositories.CategoriasRepository;
import io.github.dudupuci.appdespesas.repositories.CorRepository;
import io.github.dudupuci.appdespesas.repositories.UsuariosRepository;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoriaService {

    private final CategoriasRepository repository;
    private final UsuariosRepository usuariosRepository;
    private final CorRepository corRepository;

    public CategoriaService(
            CategoriasRepository repository,
            UsuariosRepository usuariosRepository,
            CorRepository corRepository
    ) {
        this.repository = repository;
        this.usuariosRepository = usuariosRepository;
        this.corRepository = corRepository;
    }

    public List<Categoria> listarCategoriasBySearch(String search) {
        return this.repository.listarCategoriasBySearch(search);
    }

    @Transacional
    public Categoria createCategoria(CriarCategoriaRequestDto dto, UUID usuarioId) {
        Optional<UsuarioSistema> usuario;
        Cor cor;

        try {
            validarCriacao(dto);
            // Buscar usuário
            usuario = this.usuariosRepository.findById(usuarioId);
            if (usuario.isEmpty()) {
                throw new UsuarioNotFoundException("Usuário não encontrado");
            }

            Categoria categoria = dto.toCategoria();
            categoria.setUsuarioSistema(usuario.get());

            // Associar cor se fornecida
            // Se o ID da cor for fornecido, buscar a cor e associar
            // Se o ID da cor não for fornecido, associar a cor padrão "Roxo"
            if (dto.corId() != null) {
                 cor = corRepository.findById(dto.corId())
                        .orElseThrow(() -> new RuntimeException("Cor não encontrada com ID: " + dto.corId()));
                categoria.setCor(cor);
            } else {
                cor = corRepository.findByNome("Roxo")
                        .orElseThrow(() -> new EntityNotFoundException("Cor padrão 'Roxo' não encontrada"));
                categoria.setCor(cor);
            }

            this.repository.save(categoria);
            return categoria;
        } catch (CategoriaJaExisteException err) {
            throw new CategoriaJaExisteException(AppDespesasMessages.getMessage(
                    "categoria.ja.existente",
                    new Object[]{dto.nome()})
            );
        }
    }

    @Transacional
    public Categoria updateCategoria(UUID id, CriarCategoriaRequestDto dto, UUID usuarioId) {
        Optional<UsuarioSistema> usuario;
        Cor cor;

        try {
            // Buscar categoria existente
            Categoria categoria = buscarPorId(id);

            // Verificar se o usuário é o dono da categoria
            usuario = this.usuariosRepository.findById(usuarioId);
            if (usuario.isEmpty()) {
                throw new UsuarioNotFoundException("Usuário não encontrado");
            }

            if (!categoria.getUsuarioSistema().getId().equals(usuarioId)) {
                throw new RuntimeException("Você não tem permissão para editar esta categoria");
            }

            // Atualizar campos
            categoria.setNome(dto.nome());
            categoria.setDescricao(dto.descricao());
            categoria.setTipoMovimentacao(dto.tipoMovimentacao());

            // Associar cor se fornecida
            if (dto.corId() != null) {
                cor = corRepository.findById(dto.corId())
                        .orElseThrow(() -> new RuntimeException("Cor não encontrada com ID: " + dto.corId()));
                categoria.setCor(cor);
            } else {
                // Se não forneceu cor, remove a associação
                categoria.setCor(null);
            }

            this.repository.save(categoria);
            return categoria;
        } catch (CategoriaJaExisteException err) {
            throw new CategoriaJaExisteException(AppDespesasMessages.getMessage(
                    "categoria.ja.existente",
                    new Object[]{dto.nome()})
            );
        }
    }

    public Categoria buscarPorId(UUID id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria com ID " + id + " não encontrada"));
    }

    public List<Categoria> listarTodasPorUsuarioId(
            UUID usuarioId,
            TipoMovimentacao tipoMovimentacao
    ) {
        UsuarioSistema usuario = usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));

        return this.repository.listarTodasPorUsuarioId(usuario.getId(), tipoMovimentacao);
    }

    private void validarCriacao(CriarCategoriaRequestDto dto) {
        Optional<Categoria> categoria = this.repository.buscarPorNome(dto.nome());

        if (categoria.isPresent()) {
            throw new CategoriaJaExisteException("Categoria com o nome '" + dto.nome() + "' já existe.");
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
            throw new CategoriaNotFoundException(e.getMessage());
        }
    }
}
