package io.github.dudupuci.appdespesas.infrastructure.controllers.noauth;

import io.github.dudupuci.appdespesas.application.responses.auth.AuthResult;
import io.github.dudupuci.appdespesas.application.responses.auth.RefreshTokenResult;
import io.github.dudupuci.appdespesas.application.services.AuthService;
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

    private final AuthService authService;

    public NoAuthOnboardingController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<LoginResponseDto> registrar(@Valid @RequestBody RegistroRequestDto dto) {
        AuthResult result = authService.registrar(dto.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(LoginResponseDto.fromResultRegistro(result));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        AuthResult result = authService.login(dto.toCommand());
        return ResponseEntity.ok(LoginResponseDto.fromResultLogin(result));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDto> refresh(@RequestHeader("Authorization") String authHeader) {
        String refreshToken = authHeader.substring(7);
        RefreshTokenResult result = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(RefreshTokenResponseDto.fromResult(result));
    }
}
