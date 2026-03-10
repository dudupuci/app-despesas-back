package io.github.dudupuci.appdespesas.application.usecases.cor;

import io.github.dudupuci.appdespesas.application.ports.repositories.CorRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.domain.entities.Cor;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;

import java.util.Date;
import java.util.UUID;

public class CriarCorUseCaseImpl extends CriarCorUseCase {

    private final CorRepositoryPort corRepository;
    private final UsuarioService usuarioService;
    private final UUID usuarioId;

    public CriarCorUseCaseImpl(CorRepositoryPort corRepository, UsuarioService usuarioService, UUID usuarioId) {
        this.corRepository = corRepository;
        this.usuarioService = usuarioService;
        this.usuarioId = usuarioId;
    }

    @Override
    public Cor executar(CorCommand cmd) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        if (corRepository.existsByNomeAndUsuarioSistema(cmd.nome(), usuario)) {
            throw new IllegalArgumentException("Já existe uma cor com o nome '" + cmd.nome() + "'");
        }
        if (corRepository.existsByCodigoHexadecimalAndUsuarioSistema(cmd.codigoHexadecimal(), usuario)) {
            throw new IllegalArgumentException("Já existe uma cor com o código '" + cmd.codigoHexadecimal() + "'");
        }
        if (!cmd.codigoHexadecimal().matches("^#[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("Código hexadecimal inválido. Use o formato #RRGGBB");
        }
        Cor cor = new Cor();
        cor.setNome(cmd.nome());
        cor.setCodigoHexadecimal(cmd.codigoHexadecimal().toUpperCase());
        cor.setUsuarioSistema(usuario);
        cor.setIsVisivel(true);
        cor.setDataCriacao(new Date());
        cor.setDataAtualizacao(new Date());
        return corRepository.save(cor);
    }
}

