package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.config.app.jwt.JwtConfig;
import io.github.dudupuci.appdespesas.controllers.dtos.request.auth.AuthRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.request.registro.RegistroRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.response.auth.AuthResponseDto;
import io.github.dudupuci.appdespesas.exceptions.*;
import io.github.dudupuci.appdespesas.models.entities.Role;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.repositories.RolesRepository;
import io.github.dudupuci.appdespesas.repositories.UsuariosRepository;
import io.github.dudupuci.appdespesas.services.generators.UsernameGenerator;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuariosRepository usuariosRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final UsernameGenerator usernameGenerator;

    public AuthService(
            UsuariosRepository usuariosRepository,
            RolesRepository rolesRepository,
            PasswordEncoder passwordEncoder,
            JwtConfig jwtConfig,
            UsernameGenerator usernameGenerator
    ) {
        this.usuariosRepository = usuariosRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.usernameGenerator = usernameGenerator;
    }

    @Transactional
    public AuthResponseDto registrar(RegistroRequestDto dto) {
        // Validar se email já existe
        if (usuariosRepository.existePorEmail(dto.email())) {
            throw new EmailJaExisteException(
                    AppDespesasMessages.getMessage(
                            "auth.email.ja.existe",
                            new Object[]{dto.email()}
                    )
            );
        }

        // Buscar role padrão (USER)
        Role roleUser = rolesRepository.buscarPorNome("USER");
        if (roleUser == null) {
            throw new AppDespesasException("Role USER não encontrada no sistema");
        }

        // Criar novo usuário
        UsuarioSistema novoUsuario = new UsuarioSistema();
        novoUsuario.setNome(dto.nome());
        novoUsuario.setSobrenome(dto.sobrenome());
        novoUsuario.setEmail(dto.email());
        novoUsuario.setSenha(passwordEncoder.encode(dto.senha()));
        novoUsuario.setRole(roleUser);
        novoUsuario.setAtivo(true);

        // Gerar username automaticamente no formato nome.sobrenome
        String usernameGerado = usernameGenerator.gerarUsernameParaUsuarioSistema(novoUsuario);
        novoUsuario.setNomeUsuario(usernameGerado);

        usuariosRepository.salvar(novoUsuario);

        // Gerar tokens
        String accessToken = jwtConfig.generateAccessToken(novoUsuario);
        String refreshToken = jwtConfig.generateRefreshToken(novoUsuario);

        return AuthResponseDto.fromEntityRegistro(novoUsuario, accessToken, refreshToken);
    }

    @Transactional
    public AuthResponseDto login(AuthRequestDto dto) {
        // Buscar usuário por email
        UsuarioSistema usuario = usuariosRepository.buscarPorEmail(dto.email());

        if (usuario == null) {
            throw new CredenciaisInvalidasException(
                    AppDespesasMessages.getMessage("auth.credenciais.invalidas")
            );
        }

        // Validar senha
        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException(
                    AppDespesasMessages.getMessage("auth.credenciais.invalidas")
            );
        }

        // Validar se usuário está ativo
        if (!usuario.getAtivo()) {
            throw new UsuarioInativoException(
                    AppDespesasMessages.getMessage("auth.usuario.inativo")
            );
        }

        // Gerar tokens
        String accessToken = jwtConfig.generateAccessToken(usuario);
        String refreshToken = jwtConfig.generateRefreshToken(usuario);

        return AuthResponseDto.fromEntityLogin(usuario, accessToken, refreshToken);
    }
}

