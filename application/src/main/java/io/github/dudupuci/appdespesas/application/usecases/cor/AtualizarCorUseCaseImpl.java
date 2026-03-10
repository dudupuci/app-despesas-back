package io.github.dudupuci.appdespesas.application.usecases.cor;

import io.github.dudupuci.appdespesas.application.ports.repositories.CorRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CorService;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.domain.entities.Cor;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;

import java.util.Date;
import java.util.UUID;

public class AtualizarCorUseCaseImpl extends AtualizarCorUseCase {

    private final CorRepositoryPort corRepository;
    private final CorService corService;
    private final UsuarioService usuarioService;
    private final UUID id;
    private final UUID usuarioId;

    public AtualizarCorUseCaseImpl(CorRepositoryPort corRepository, CorService corService,
                                   UsuarioService usuarioService, UUID id, UUID usuarioId) {
        this.corRepository = corRepository;
        this.corService = corService;
        this.usuarioService = usuarioService;
        this.id = id;
        this.usuarioId = usuarioId;
    }

    @Override
    public Cor executar(CorCommand cmd) {
        Cor cor = corService.buscarPorId(id, usuarioId);
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        if (!cor.getNome().equals(cmd.nome()) && corRepository.existsByNomeAndUsuarioSistema(cmd.nome(), usuario)) {
            throw new IllegalArgumentException("Já existe uma cor com o nome '" + cmd.nome() + "'");
        }
        if (!cor.getCodigoHexadecimal().equals(cmd.codigoHexadecimal())
                && corRepository.existsByCodigoHexadecimalAndUsuarioSistema(cmd.codigoHexadecimal(), usuario)) {
            throw new IllegalArgumentException("Já existe uma cor com o código '" + cmd.codigoHexadecimal() + "'");
        }
        if (!cmd.codigoHexadecimal().matches("^#[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("Código hexadecimal inválido. Use o formato #RRGGBB");
        }
        cor.setNome(cmd.nome());
        cor.setCodigoHexadecimal(cmd.codigoHexadecimal().toUpperCase());
        cor.setDataAtualizacao(new Date());
        return corRepository.save(cor);
    }
}

