package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.commands.auth.LoginCommand;
import io.github.dudupuci.appdespesas.application.commands.auth.RegistroCommand;
import io.github.dudupuci.appdespesas.application.ports.repositories.*;
import io.github.dudupuci.appdespesas.application.ports.services.JwtPort;
import io.github.dudupuci.appdespesas.application.ports.services.PasswordEncoderPort;
import io.github.dudupuci.appdespesas.application.responses.auth.AuthResult;
import io.github.dudupuci.appdespesas.application.responses.auth.RefreshTokenResult;
import io.github.dudupuci.appdespesas.application.services.generators.UsernameGenerator;
import io.github.dudupuci.appdespesas.domain.entities.*;
import io.github.dudupuci.appdespesas.domain.enums.Status;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.domain.exceptions.*;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepositoryPort usuariosRepository;
    private final RoleRepositoryPort rolesRepository;
    private final PasswordEncoderPort passwordEncoderPort;
    private final JwtPort jwtPort;
    private final UsernameGenerator usernameGenerator;
    private final CorRepositoryPort corRepository;
    private final CategoriaRepositoryPort categoriasRepository;
    private final AssinaturaRepositoryPort assinaturaRepository;

    public AuthService(
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

    public AuthResult registrar(RegistroCommand cmd) {
        // Validar se email já existe
        if (usuariosRepository.existsByContatoEmail(cmd.email())) {
            throw new EmailJaExisteException(
                    AppDespesasMessages.getMessage("auth.email.ja.existe", new Object[]{cmd.email()})
            );
        }

        // Validar senha e confirmação de senha
        validarSenha(cmd.senha(), cmd.confirmacaoSenha());

        // Buscar role padrão (USER)
        Role roleUser = rolesRepository.buscarPorNome("USER");
        if (roleUser == null) {
            throw new EntityNotFoundException("Role USER não encontrada no sistema");
        }

        // Criar novo usuário
        UsuarioSistema novoUsuario = new UsuarioSistema();
        novoUsuario.setNome(cmd.nome());
        novoUsuario.setSobrenome(cmd.sobrenome());
        novoUsuario.setContato(new Contato());
        novoUsuario.getContato().setCelular(cmd.celular());
        novoUsuario.getContato().setEmail(cmd.email());
        novoUsuario.getContato().setTelefoneFixo(cmd.telefoneFixo());
        novoUsuario.setSenha(passwordEncoderPort.encode(cmd.senha()));
        novoUsuario.setRole(roleUser);
        novoUsuario.setAtivo(true);
        novoUsuario.setMovimentacoes(new ArrayList<>());

        // Gerar username automaticamente no formato nome.sobrenome
        String usernameGerado = usernameGenerator.gerarUsernameParaUsuarioSistema(novoUsuario);
        novoUsuario.setNomeUsuario(usernameGerado);

        UsuarioSistemaConfig usuarioSistemaConfig = new UsuarioSistemaConfig(novoUsuario);
        novoUsuario.setUsuarioSistemaConfig(usuarioSistemaConfig);

        // Atribuir assinatura gratuita padrão ao novo usuário
        Assinatura assinaturaGratuita = assinaturaRepository.buscarAssinaturaGratuita();
        novoUsuario.setAssinatura(assinaturaGratuita);

        // Salvar usuário no banco de dados
        UsuarioSistema usuarioSalvo = usuariosRepository.save(novoUsuario);

        // Criar cores e categorias padrão para o novo usuário
        criarCoresECategoriasDefault(usuarioSalvo);

        // Gerar tokens
        String accessToken = jwtPort.generateAccessToken(usuarioSalvo);
        String refreshToken = jwtPort.generateRefreshToken(usuarioSalvo);

        return new AuthResult(accessToken, refreshToken, usuarioSalvo);
    }

    public AuthResult login(LoginCommand cmd) {
        // Buscar usuário por email
        Optional<UsuarioSistema> usuario = usuariosRepository.buscarPorEmail(cmd.email());

        if (usuario.isEmpty()) {
            throw new CredenciaisInvalidasException(AppDespesasMessages.getMessage("auth.credenciais.invalidas"));
        }

        // Validar senha
        if (!passwordEncoderPort.matches(cmd.senha(), usuario.get().getSenha())) {
            throw new CredenciaisInvalidasException(AppDespesasMessages.getMessage("auth.credenciais.invalidas"));
        }

        // Validar se usuário está ativo
        if (!usuario.get().getAtivo()) {
            throw new UsuarioInativoException(AppDespesasMessages.getMessage("auth.usuario.inativo"));
        }

        // Gerar tokens
        String accessToken = jwtPort.generateAccessToken(usuario.get());
        String refreshToken = jwtPort.generateRefreshToken(usuario.get());

        return new AuthResult(accessToken, refreshToken, usuario.get());
    }

    public RefreshTokenResult refreshToken(String refreshToken) {
        try {
            // Extrai o email do refresh token
            String email = jwtPort.extractEmail(refreshToken);

            // Busca o usuário
            Optional<UsuarioSistema> usuario = usuariosRepository.buscarPorEmail(email);

            if (usuario.isEmpty()) {
                throw new CredenciaisInvalidasException(AppDespesasMessages.getMessage("auth.token.invalido"));
            }
            if (!usuario.get().getAtivo()) {
                throw new UsuarioInativoException(AppDespesasMessages.getMessage("auth.usuario.inativo"));
            }

            // Gera novo access token
            String newAccessToken = jwtPort.generateAccessToken(usuario.get());
            return new RefreshTokenResult(newAccessToken);

        } catch (CredenciaisInvalidasException | UsuarioInativoException e) {
            throw e;
        } catch (Exception e) {
            throw new CredenciaisInvalidasException(AppDespesasMessages.getMessage("auth.token.invalido"));
        }
    }

    private void validarSenha(String senha, String confirmacaoSenha) {
        if (senha != null && !senha.isBlank() && confirmacaoSenha != null && !confirmacaoSenha.isBlank()) {
            if (!senha.equals(confirmacaoSenha)) {
                throw new SenhasNaoCoincidemException(
                        AppDespesasMessages.getMessage("auth.senha.confirmacaoSenha.diferentes")
                );
            }
        }
    }

    /**
     * Cria cores e categorias padrão para o novo usuário
     */
    private void criarCoresECategoriasDefault(UsuarioSistema usuario) {
        log.info("🎨 Criando cores e categorias padrão para: {}", usuario.getContato().getEmail());
        Date agora = new Date();

        // Criar cores padrão
        Cor verdeEscuro   = criarCor(usuario, "Verde Escuro",    "#006400", agora);
        Cor verdeClaro    = criarCor(usuario, "Verde Claro",     "#90EE90", agora);
        Cor azulEscuro    = criarCor(usuario, "Azul Escuro",     "#00008B", agora);
        Cor amareloEscuro = criarCor(usuario, "Amarelo Escuro",  "#FFD700", agora);
        Cor laranjaClaro  = criarCor(usuario, "Laranja Claro",   "#FFA07A", agora);
        Cor vermelho      = criarCor(usuario, "Vermelho",        "#FF0000", agora);

        // Criar categorias padrão de RECEITAS
        criarCategoria(usuario, "Salário",       "Renda proveniente de trabalho fixo",                              TipoMovimentacao.RECEITA, verdeEscuro,   agora);
        criarCategoria(usuario, "Freelancers",   "Renda proveniente de trabalhos autônomos e projetos",             TipoMovimentacao.RECEITA, verdeClaro,    agora);
        criarCategoria(usuario, "Investimentos", "Renda proveniente de aplicações financeiras, dividendos, juros",  TipoMovimentacao.RECEITA, azulEscuro,    agora);

        // Criar categorias padrão de DESPESAS
        criarCategoria(usuario, "Alimentação",   "Despesas com supermercado, restaurantes, delivery",               TipoMovimentacao.DESPESA, amareloEscuro, agora);
        criarCategoria(usuario, "Transporte",    "Despesas com combustível, transporte público, mobilidade",        TipoMovimentacao.DESPESA, laranjaClaro,  agora);
        criarCategoria(usuario, "Lazer",         "Despesas com entretenimento, viagens, hobbies",                   TipoMovimentacao.DESPESA, vermelho,      agora);

        log.info("✅ Cores e categorias padrão criadas para: {}", usuario.getContato().getEmail());
    }

    private Cor criarCor(UsuarioSistema usuario, String nome, String codigoHex, Date data) {
        Cor cor = new Cor();
        cor.setNome(nome);
        cor.setCodigoHexadecimal(codigoHex);
        cor.setUsuarioSistema(usuario);
        cor.setIsVisivel(true);
        cor.setDataCriacao(data);
        cor.setDataAtualizacao(data);
        return corRepository.save(cor);
    }

    private void criarCategoria(UsuarioSistema usuario, String nome, String descricao,
                                TipoMovimentacao tipo, Cor cor, Date data) {
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setDescricao(descricao);
        categoria.setTipoMovimentacao(tipo);
        categoria.setUsuarioSistema(usuario);
        categoria.setCor(cor);
        categoria.setStatus(Status.ATIVO);
        categoria.setDataCriacao(data);
        categoria.setDataAtualizacao(data);
        categoriasRepository.save(categoria);
    }
}
