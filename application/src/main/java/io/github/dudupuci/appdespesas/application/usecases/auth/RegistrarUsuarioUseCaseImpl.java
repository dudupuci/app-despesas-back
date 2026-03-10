package io.github.dudupuci.appdespesas.application.usecases.auth;

import io.github.dudupuci.appdespesas.application.ports.repositories.*;
import io.github.dudupuci.appdespesas.application.ports.services.JwtPort;
import io.github.dudupuci.appdespesas.application.ports.services.PasswordEncoderPort;
import io.github.dudupuci.appdespesas.application.responses.auth.AuthResult;
import io.github.dudupuci.appdespesas.application.services.generators.UsernameGenerator;
import io.github.dudupuci.appdespesas.domain.entities.*;
import io.github.dudupuci.appdespesas.domain.enums.Status;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.domain.exceptions.EmailJaExisteException;
import io.github.dudupuci.appdespesas.domain.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.domain.exceptions.SenhasNaoCoincidemException;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;

public class RegistrarUsuarioUseCaseImpl extends RegistrarUsuarioUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegistrarUsuarioUseCaseImpl.class);

    private final UsuarioRepositoryPort usuariosRepository;
    private final RoleRepositoryPort rolesRepository;
    private final PasswordEncoderPort passwordEncoderPort;
    private final JwtPort jwtPort;
    private final UsernameGenerator usernameGenerator;
    private final CorRepositoryPort corRepository;
    private final CategoriaRepositoryPort categoriasRepository;
    private final AssinaturaRepositoryPort assinaturaRepository;

    public RegistrarUsuarioUseCaseImpl(
            UsuarioRepositoryPort usuariosRepository,
            RoleRepositoryPort rolesRepository,
            PasswordEncoderPort passwordEncoderPort,
            JwtPort jwtPort,
            UsernameGenerator usernameGenerator,
            CorRepositoryPort corRepository,
            CategoriaRepositoryPort categoriasRepository,
            AssinaturaRepositoryPort assinaturaRepository
    ) {
        this.usuariosRepository = usuariosRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoderPort = passwordEncoderPort;
        this.jwtPort = jwtPort;
        this.usernameGenerator = usernameGenerator;
        this.corRepository = corRepository;
        this.categoriasRepository = categoriasRepository;
        this.assinaturaRepository = assinaturaRepository;
    }

    @Override
    public AuthResult executar(RegistroCommand cmd) {
        if (usuariosRepository.existsByContatoEmail(cmd.email())) {
            throw new EmailJaExisteException(AppDespesasMessages.getMessage("auth.email.ja.existe", new Object[]{cmd.email()}));
        }
        if (cmd.senha() != null && cmd.confirmacaoSenha() != null && !cmd.senha().equals(cmd.confirmacaoSenha())) {
            throw new SenhasNaoCoincidemException(AppDespesasMessages.getMessage("auth.senha.confirmacaoSenha.diferentes"));
        }
        Role roleUser = rolesRepository.buscarPorNome("USER");
        if (roleUser == null) throw new EntityNotFoundException("Role USER não encontrada no sistema");

        UsuarioSistema novoUsuario = new UsuarioSistema();
        novoUsuario.setNome(cmd.nome());
        novoUsuario.setSobrenome(cmd.sobrenome());
        novoUsuario.setContato(new Contato());
        novoUsuario.getContato().setCelular(cmd.celular());
        novoUsuario.getContato().setEmail(cmd.email());
        novoUsuario.getContato().setTelefoneFixo(cmd.telefoneFixo());
        novoUsuario.setSenha(this.passwordEncoderPort.encode(cmd.senha()));
        novoUsuario.setRole(roleUser);
        novoUsuario.setAtivo(true);
        novoUsuario.setMovimentacoes(new ArrayList<>());
        novoUsuario.setNomeUsuario(usernameGenerator.gerarUsernameParaUsuarioSistema(novoUsuario));
        novoUsuario.setUsuarioSistemaConfig(new UsuarioSistemaConfig(novoUsuario));
        novoUsuario.setAssinatura(assinaturaRepository.buscarAssinaturaGratuita());

        UsuarioSistema salvo = usuariosRepository.save(novoUsuario);
        criarCoresECategoriasDefault(salvo);

        return new AuthResult(jwtPort.generateAccessToken(salvo), jwtPort.generateRefreshToken(salvo), salvo);
    }

    private void criarCoresECategoriasDefault(UsuarioSistema usuario) {
        log.info("🎨 Criando cores e categorias padrão para: {}", usuario.getContato().getEmail());
        Date agora = new Date();
        Cor verdeEscuro   = criarCor(usuario, "Verde Escuro",   "#006400", agora);
        Cor verdeClaro    = criarCor(usuario, "Verde Claro",    "#90EE90", agora);
        Cor azulEscuro    = criarCor(usuario, "Azul Escuro",    "#00008B", agora);
        Cor amareloEscuro = criarCor(usuario, "Amarelo Escuro", "#FFD700", agora);
        Cor laranjaClaro  = criarCor(usuario, "Laranja Claro",  "#FFA07A", agora);
        Cor vermelho      = criarCor(usuario, "Vermelho",       "#FF0000", agora);
        criarCategoria(usuario, "Salário",       "Renda proveniente de trabalho fixo",                             TipoMovimentacao.RECEITA, verdeEscuro,   agora);
        criarCategoria(usuario, "Freelancers",   "Renda proveniente de trabalhos autônomos e projetos",            TipoMovimentacao.RECEITA, verdeClaro,    agora);
        criarCategoria(usuario, "Investimentos", "Renda proveniente de aplicações financeiras, dividendos, juros", TipoMovimentacao.RECEITA, azulEscuro,    agora);
        criarCategoria(usuario, "Alimentação",   "Despesas com supermercado, restaurantes, delivery",              TipoMovimentacao.DESPESA, amareloEscuro, agora);
        criarCategoria(usuario, "Transporte",    "Despesas com combustível, transporte público, mobilidade",       TipoMovimentacao.DESPESA, laranjaClaro,  agora);
        criarCategoria(usuario, "Lazer",         "Despesas com entretenimento, viagens, hobbies",                  TipoMovimentacao.DESPESA, vermelho,      agora);
        log.info("✅ Cores e categorias padrão criadas para: {}", usuario.getContato().getEmail());
    }

    private Cor criarCor(UsuarioSistema usuario, String nome, String hex, Date data) {
        Cor cor = new Cor();
        cor.setNome(nome); cor.setCodigoHexadecimal(hex); cor.setUsuarioSistema(usuario);
        cor.setIsVisivel(true); cor.setDataCriacao(data); cor.setDataAtualizacao(data);
        return corRepository.save(cor);
    }

    private void criarCategoria(UsuarioSistema usuario, String nome, String descricao, TipoMovimentacao tipo, Cor cor, Date data) {
        Categoria categoria = new Categoria();
        categoria.setNome(nome); categoria.setDescricao(descricao); categoria.setTipoMovimentacao(tipo);
        categoria.setUsuarioSistema(usuario); categoria.setCor(cor); categoria.setStatus(Status.ATIVO);
        categoria.setDataCriacao(data); categoria.setDataAtualizacao(data);
        categoriasRepository.save(categoria);
    }
}

