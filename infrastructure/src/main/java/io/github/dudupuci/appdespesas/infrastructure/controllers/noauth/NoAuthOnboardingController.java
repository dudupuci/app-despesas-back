package io.github.dudupuci.appdespesas.infrastructure.controllers.noauth;

import io.github.dudupuci.appdespesas.application.responses.auth.AuthResult;
import io.github.dudupuci.appdespesas.application.responses.auth.RefreshTokenResult;
import io.github.dudupuci.appdespesas.application.usecases.auth.LoginUseCase;
import io.github.dudupuci.appdespesas.application.usecases.auth.RefreshTokenUseCase;
import io.github.dudupuci.appdespesas.application.usecases.auth.RegistrarUsuarioUseCase;
import io.github.dudupuci.appdespesas.infrastructure.controllers.noauth.dtos.request.login.LoginRequestDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.noauth.dtos.request.registro.RegistroRequestDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.noauth.dtos.response.login.LoginResponseDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.noauth.dtos.response.login.RefreshTokenResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/auth")
public class NoAuthOnboardingController {

    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public NoAuthOnboardingController(
            RegistrarUsuarioUseCase registrarUsuarioUseCase,
            LoginUseCase loginUseCase,
            RefreshTokenUseCase refreshTokenUseCase
    ) {
        this.registrarUsuarioUseCase = registrarUsuarioUseCase;
        this.loginUseCase = loginUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @PostMapping("/registro")
    public ResponseEntity<LoginResponseDto> registrar(@Valid @RequestBody RegistroRequestDto dto) {
        AuthResult result = registrarUsuarioUseCase.executar(dto.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(LoginResponseDto.fromResultRegistro(result));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        AuthResult result = loginUseCase.executar(dto.toCommand());
        return ResponseEntity.ok(LoginResponseDto.fromResultLogin(result));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDto> refresh(@RequestHeader("Authorization") String authHeader) {
        String refreshToken = authHeader.substring(7);
        RefreshTokenResult result = refreshTokenUseCase.executar(refreshToken);
        return ResponseEntity.ok(RefreshTokenResponseDto.fromResult(result));
    }
}
