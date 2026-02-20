package io.github.dudupuci.appdespesas.controllers.noauth;

import io.github.dudupuci.appdespesas.controllers.noauth.dtos.request.login.LoginRequestDto;
import io.github.dudupuci.appdespesas.controllers.noauth.dtos.response.login.LoginResponseDto;
import io.github.dudupuci.appdespesas.controllers.noauth.dtos.response.login.RefreshTokenResponseDto;
import io.github.dudupuci.appdespesas.controllers.noauth.dtos.request.registro.RegistroRequestDto;
import io.github.dudupuci.appdespesas.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<LoginResponseDto> registrar(@Valid @RequestBody RegistroRequestDto dto) {
        LoginResponseDto response = authService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        LoginResponseDto response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDto> refresh(@RequestHeader("Authorization") String authHeader) {
        // Remove "Bearer " do token
        String refreshToken = authHeader.substring(7);
        RefreshTokenResponseDto response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
}

