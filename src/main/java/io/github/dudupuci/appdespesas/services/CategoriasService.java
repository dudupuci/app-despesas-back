package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.config.persistence.Transacional;
import io.github.dudupuci.appdespesas.controllers.dtos.request.categoria.CriarCategoriaRequestDto;
import io.github.dudupuci.appdespesas.exceptions.*;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.repositories.CategoriasRepository;
import io.github.dudupuci.appdespesas.repositories.UsuariosRepository;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoriasService {

    private final CategoriasRepository repository;
    private final UsuariosRepository usuariosRepository;

    @Value("${tudin.app.super-adm-id}")
    private String superAdmId;

    public CategoriasService(CategoriasRepository repository, UsuariosRepository usuariosRepository) {
        this.repository = repository;
        this.usuariosRepository = usuariosRepository;
    }

    public List<Categoria> listarCategoriasBySearch(String search) {
        return this.repository.listarCategoriasBySearch(search);
    }

    @Transacional
    public Categoria createCategoria(CriarCategoriaRequestDto dto, UUID usuarioId) {
        Optional<UsuarioSistema> usuario;

        try {
            validarCriacao(dto);
            // Buscar usuário
            usuario = this.usuariosRepository.findById(usuarioId);
            if (usuario.isEmpty()) {
                throw new UsuarioNotFoundException("Usuário não encontrado");
            }

            Categoria categoria = dto.toCategoria();
            categoria.setUsuarioSistema(usuario.get());
            this.repository.save(categoria);
            return categoria;
        } catch (CategoriaJaExisteException err) {
            throw new CategoriaJaExisteException(AppDespesasMessages.getMessage(
                    "categoria.ja.existente",
                    new Object[]{dto.nome()})
            );
        }
    }

    public Categoria buscarPorId(UUID id) throws EntityNotFoundException {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria com ID " + id + " não encontrada"));
    }

    public List<Categoria> listarTodasPorUsuarioId(
            UUID usuarioId,
            TipoMovimentacao tipoMovimentacao
    ) {

        return this.repository.listarTodasPorUsuarioId(usuarioId, getSuperAdmId() ,tipoMovimentacao);
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

    private UUID getSuperAdmId() {
        try {
            return UUID.fromString(superAdmId);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("ID do super administrador configurado é inválido: " + superAdmId);
        }
    }
}
