package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.config.app.jwt.JwtConfig;
import io.github.dudupuci.appdespesas.controllers.dtos.request.auth.AuthRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.request.registro.RegistroRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.response.auth.AuthResponseDto;
import io.github.dudupuci.appdespesas.controllers.dtos.response.auth.RefreshTokenResponseDto;
import io.github.dudupuci.appdespesas.exceptions.*;
import io.github.dudupuci.appdespesas.models.entities.*;
import io.github.dudupuci.appdespesas.models.enums.Status;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.repositories.CategoriasRepository;
import io.github.dudupuci.appdespesas.repositories.CorRepository;
import io.github.dudupuci.appdespesas.repositories.RoleRepository;
import io.github.dudupuci.appdespesas.repositories.UsuariosRepository;
import io.github.dudupuci.appdespesas.services.generators.UsernameGenerator;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import jakarta.transaction.Transactional;
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

    private final UsuariosRepository usuariosRepository;
    private final RoleRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final UsernameGenerator usernameGenerator;
    private final CorRepository corRepository;
    private final CategoriasRepository categoriasRepository;

    public AuthService(
            UsuariosRepository usuariosRepository,
            RoleRepository rolesRepository,
            PasswordEncoder passwordEncoder,
            JwtConfig jwtConfig,
            UsernameGenerator usernameGenerator,
            CorRepository corRepository,
            CategoriasRepository categoriasRepository
    ) {
        this.usuariosRepository = usuariosRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.usernameGenerator = usernameGenerator;
        this.corRepository = corRepository;
        this.categoriasRepository = categoriasRepository;
    }

    @Transactional
    public AuthResponseDto registrar(RegistroRequestDto dto) {
        // Validar se email já existe
        if (usuariosRepository.existsByContatoEmail(dto.email())) {
            throw new EmailJaExisteException(
                    AppDespesasMessages.getMessage(
                            "auth.email.ja.existe",
                            new Object[]{dto.email()}
                    )
            );
        }

        // Validar senha e confirmação de senha
        validarSenha(dto);

        // Buscar role padrão (USER)
        Role roleUser = rolesRepository.buscarPorNome("USER");
        if (roleUser == null) {
            throw new AppDespesasException("Role USER não encontrada no sistema");
        }

        // Criar novo usuário
        UsuarioSistema novoUsuario = new UsuarioSistema();
        novoUsuario.setNome(dto.nome());
        novoUsuario.setContato(new Contato());
        novoUsuario.getContato().setCelular(dto.celular());
        novoUsuario.getContato().setEmail(dto.email());
        novoUsuario.getContato().setTelefoneFixo(dto.telefoneFixo());
        novoUsuario.setSobrenome(dto.sobrenome());
        novoUsuario.setSenha(passwordEncoder.encode(dto.senha()));
        novoUsuario.setRole(roleUser);
        novoUsuario.setAtivo(true);
        novoUsuario.setMovimentacoes(new ArrayList<>());

        // Gerar username automaticamente no formato nome.sobrenome
        String usernameGerado = usernameGenerator.gerarUsernameParaUsuarioSistema(novoUsuario);
        novoUsuario.setNomeUsuario(usernameGerado);

        // Salvar usuário
        UsuarioSistema usuarioSalvo = usuariosRepository.save(novoUsuario);

        // Criar cores e categorias padrão para o novo usuário
        criarCoresECategoriasDefault(usuarioSalvo);

        // Gerar tokens
        String accessToken = jwtConfig.generateAccessToken(usuarioSalvo);
        String refreshToken = jwtConfig.generateRefreshToken(usuarioSalvo);

        return AuthResponseDto.fromEntityRegistro(usuarioSalvo, accessToken, refreshToken);
    }

    @Transactional
    public AuthResponseDto login(AuthRequestDto dto) {
        // Buscar usuário por email
        Optional<UsuarioSistema> usuario = usuariosRepository.buscarPorEmail(dto.email());

        if (usuario.isEmpty()) {
            throw new CredenciaisInvalidasException(
                    AppDespesasMessages.getMessage("auth.credenciais.invalidas")
            );
        }

        // Validar senha
        if (!passwordEncoder.matches(dto.senha(), usuario.get().getSenha())) {
            throw new CredenciaisInvalidasException(
                    AppDespesasMessages.getMessage("auth.credenciais.invalidas")
            );
        }

        // Validar se usuário está ativo
        if (!usuario.get().getAtivo()) {
            throw new UsuarioInativoException(
                    AppDespesasMessages.getMessage("auth.usuario.inativo")
            );
        }

        // Gerar tokens
        String accessToken = jwtConfig.generateAccessToken(usuario.get());
        String refreshToken = jwtConfig.generateRefreshToken(usuario.get());

        return AuthResponseDto.fromEntityLogin(usuario.get(), accessToken, refreshToken);
    }

    private void validarSenha(RegistroRequestDto dto) {
        if (dto.senha() != null && !dto.senha().isBlank() && dto.confirmacaoSenha() != null && !dto.confirmacaoSenha().isBlank()) {
            if (!dto.senha().equals(dto.confirmacaoSenha())) {
                throw new SenhasNaoCoincidemException(
                        AppDespesasMessages.getMessage("auth.senha.confirmacaoSenha.diferentes")
                );
            }
        }
    }

    @Transactional
    public RefreshTokenResponseDto refreshToken(String refreshToken) {
        try {
            // Extrai o email do refresh token
            String email = jwtConfig.extractEmail(refreshToken);

            // Busca o usuário
            Optional<UsuarioSistema> usuario = usuariosRepository.buscarPorEmail(email);

            if (usuario.isEmpty()) {
                throw new CredenciaisInvalidasException(
                        AppDespesasMessages.getMessage("auth.token.invalido")
                );
            }

            // Valida se usuário está ativo
            if (!usuario.get().getAtivo()) {
                throw new UsuarioInativoException(
                        AppDespesasMessages.getMessage("auth.usuario.inativo")
                );
            }

            // Gera novo access token
            String newAccessToken = jwtConfig.generateAccessToken(usuario.get());

            return RefreshTokenResponseDto.of(newAccessToken);

        } catch (Exception e) {
            throw new CredenciaisInvalidasException(
                    AppDespesasMessages.getMessage("auth.token.invalido")
            );
        }
    }

    /**
     * Cria cores e categorias padrão para o novo usuário
     */
    private void criarCoresECategoriasDefault(UsuarioSistema usuario) {
        log.info("🎨 Criando cores e categorias padrão para o usuário: {}", usuario.getContato().getEmail());

        Date agora = new Date();

        // Criar cores padrão
        Cor verdeEscuro = criarCor(usuario, "Verde Escuro", "#006400", agora);
        Cor verdeClaro = criarCor(usuario, "Verde Claro", "#90EE90", agora);
        Cor azulEscuro = criarCor(usuario, "Azul Escuro", "#00008B", agora);
        Cor amareloEscuro = criarCor(usuario, "Amarelo Escuro", "#FFD700", agora);
        Cor laranjaClaro = criarCor(usuario, "Laranja Claro", "#FFA07A", agora);
        Cor vermelho = criarCor(usuario, "Vermelho", "#FF0000", agora);

        // Criar categorias padrão de RECEITAS
        criarCategoria(usuario, "Salário", "Renda proveniente de trabalho fixo",
                TipoMovimentacao.RECEITA, verdeEscuro, agora);
        criarCategoria(usuario, "Freelancers", "Renda proveniente de trabalhos autônomos e projetos",
                TipoMovimentacao.RECEITA, verdeClaro, agora);
        criarCategoria(usuario, "Investimentos", "Renda proveniente de aplicações financeiras, dividendos, juros",
                TipoMovimentacao.RECEITA, azulEscuro, agora);

        // Criar categorias padrão de DESPESAS
        criarCategoria(usuario, "Alimentação", "Despesas com supermercado, restaurantes, delivery",
                TipoMovimentacao.DESPESA, amareloEscuro, agora);
        criarCategoria(usuario, "Transporte", "Despesas com combustível, transporte público, aplicativos de mobilidade",
                TipoMovimentacao.DESPESA, laranjaClaro, agora);
        criarCategoria(usuario, "Lazer", "Despesas com entretenimento, viagens, hobbies",
                TipoMovimentacao.DESPESA, vermelho, agora);

        log.info("✅ Cores e categorias padrão criadas para o usuário: {}", usuario.getContato().getEmail());
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
