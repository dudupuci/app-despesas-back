package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.domain.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.domain.exceptions.UsuarioNotFoundException;
import io.github.dudupuci.appdespesas.domain.entities.Cor;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.infrastructure.repositories.CorRepository;
import io.github.dudupuci.appdespesas.infrastructure.repositories.UsuariosRepository;
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

    private final CorRepository corRepository;
    private final UsuariosRepository usuariosRepository;
    private final UsuarioService usuarioService;


    public CorService(
            CorRepository corRepository,
            UsuariosRepository usuariosRepository,
            UsuarioService usuarioService) {
        this.corRepository = corRepository;
        this.usuariosRepository = usuariosRepository;
        this.usuarioService = usuarioService;
    }

    /**
     * Criar nova cor para o usuário
     */
    @Transactional
    public Cor criar(Cor cor, UUID usuarioId) {
        UsuarioSistema usuario = usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));

        // Validar se já existe cor com o mesmo nome
        if (corRepository.existsByNomeAndUsuarioSistema(cor.getNome(), usuario)) {
            throw new IllegalArgumentException("Já existe uma cor com o nome '" + cor.getNome() + "'");
        }

        // Validar se já existe cor com o mesmo código hexadecimal
        if (corRepository.existsByCodigoHexadecimalAndUsuarioSistema(cor.getCodigoHexadecimal(), usuario)) {
            throw new IllegalArgumentException("Já existe uma cor com o código '" + cor.getCodigoHexadecimal() + "'");
        }

        // Validar formato hexadecimal
        if (!cor.getCodigoHexadecimal().matches("^#[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("Código hexadecimal inválido. Use o formato #RRGGBB");
        }

        cor.setUsuarioSistema(usuario);
        cor.setDataCriacao(new Date());
        cor.setDataAtualizacao(new Date());

        Cor corCriada = corRepository.save(cor);

        log.info("🎨 Cor criada: {} ({}) para o usuário: {}",
                corCriada.getNome(),
                corCriada.getCodigoHexadecimal(),
                usuario.getContato().getEmail());

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
        UsuarioSistema usuario = usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));

        return corRepository.listarTodasPorUsuarioId(usuario.getId());
    }

    /**
     * Atualizar cor existente
     */
    @Transactional
    public Cor atualizar(UUID id, Cor corAtualizada, UUID usuarioId) {
        Cor cor = buscarPorId(id, usuarioId);
        UsuarioSistema usuario = usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));

        // Validar se o novo nome já existe (exceto para a própria cor)
        if (!cor.getNome().equals(corAtualizada.getNome())) {
            if (corRepository.existsByNomeAndUsuarioSistema(corAtualizada.getNome(), usuario)) {
                throw new IllegalArgumentException("Já existe uma cor com o nome '" + corAtualizada.getNome() + "'");
            }
        }

        // Validar se o novo código já existe (exceto para a própria cor)
        if (!cor.getCodigoHexadecimal().equals(corAtualizada.getCodigoHexadecimal())) {
            if (corRepository.existsByCodigoHexadecimalAndUsuarioSistema(corAtualizada.getCodigoHexadecimal(), usuario)) {
                throw new IllegalArgumentException("Já existe uma cor com o código '" + corAtualizada.getCodigoHexadecimal() + "'");
            }
        }

        // Validar formato hexadecimal
        if (!corAtualizada.getCodigoHexadecimal().matches("^#[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("Código hexadecimal inválido. Use o formato #RRGGBB");
        }

        cor.setNome(corAtualizada.getNome());
        cor.setCodigoHexadecimal(corAtualizada.getCodigoHexadecimal());
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

