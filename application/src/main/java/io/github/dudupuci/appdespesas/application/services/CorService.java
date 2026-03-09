package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.commands.cor.CorCommand;
import io.github.dudupuci.appdespesas.application.ports.repositories.CorRepositoryPort;
import io.github.dudupuci.appdespesas.domain.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.domain.entities.Cor;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CorService {

    private static final Logger log = LoggerFactory.getLogger(CorService.class);

    private final CorRepositoryPort corRepository;
    private final UsuarioService usuarioService;

    public CorService(CorRepositoryPort corRepository, UsuarioService usuarioService) {
        this.corRepository = corRepository;
        this.usuarioService = usuarioService;
    }

    /**
     * Criar nova cor para o usuário
     */
    @Transactional
    public Cor criar(CorCommand cmd, UUID usuarioId) {
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

        Cor corCriada = corRepository.save(cor);
        log.info("🎨 Cor criada: {} ({}) para o usuário: {}", corCriada.getNome(), corCriada.getCodigoHexadecimal(), usuario.getContato().getEmail());
        return corCriada;
    }

    /**
     * Buscar cor por ID (apenas do usuário logado)
     */
    public Cor buscarPorId(UUID id, UUID usuarioId) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        return corRepository.findByIdAndUsuarioSistema(id, usuario)
                .orElseThrow(() -> new EntityNotFoundException("Cor não encontrada"));
    }

    /**
     * Listar todas as cores do usuário
     */
    public List<Cor> listarTodas(UUID usuarioId) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        return corRepository.listarTodasPorUsuarioId(usuario.getId());
    }

    /**
     * Atualizar cor existente
     */
    @Transactional
    public Cor atualizar(UUID id, CorCommand cmd, UUID usuarioId) {
        Cor cor = buscarPorId(id, usuarioId);
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);

        if (!cor.getNome().equals(cmd.nome())) {
            if (corRepository.existsByNomeAndUsuarioSistema(cmd.nome(), usuario)) {
                throw new IllegalArgumentException("Já existe uma cor com o nome '" + cmd.nome() + "'");
            }
        }
        if (!cor.getCodigoHexadecimal().equals(cmd.codigoHexadecimal())) {
            if (corRepository.existsByCodigoHexadecimalAndUsuarioSistema(cmd.codigoHexadecimal(), usuario)) {
                throw new IllegalArgumentException("Já existe uma cor com o código '" + cmd.codigoHexadecimal() + "'");
            }
        }
        if (!cmd.codigoHexadecimal().matches("^#[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("Código hexadecimal inválido. Use o formato #RRGGBB");
        }

        cor.setNome(cmd.nome());
        cor.setCodigoHexadecimal(cmd.codigoHexadecimal().toUpperCase());
        cor.setDataAtualizacao(new Date());
        log.info("✏️ Cor atualizada: {} ({})", cor.getNome(), cor.getCodigoHexadecimal());
        return corRepository.save(cor);
    }

    /**
     * Deletar cor
     */
    @Transactional
    public void deletar(UUID id, UUID usuarioId) {
        Cor cor = buscarPorId(id, usuarioId);
        log.info("🗑️ Cor deletada: {} ({})", cor.getNome(), cor.getCodigoHexadecimal());
        corRepository.delete(cor);
    }
}
