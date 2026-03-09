package io.github.dudupuci.appdespesas.infrastructure.controllers.noauth;

import io.github.dudupuci.appdespesas.application.responses.cep.ViaCepResponse;
import io.github.dudupuci.appdespesas.infrastructure.controllers.noauth.dtos.response.cep.ViaCepResponseDto;
import io.github.dudupuci.appdespesas.domain.annotations.Cep;
import io.github.dudupuci.appdespesas.application.services.webservices.ViaCepService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/consultas")
public class NoAuthConsultaController {

    private final ViaCepService viaCepService;

    public NoAuthConsultaController(ViaCepService viaCepService) {
        this.viaCepService = viaCepService;
    }

    @GetMapping("/buscar/endereco-por-cep")
    public ResponseEntity<?> buscarEnderecoPorCep(@RequestParam(value = "cep") @Cep String cep) {
        try {
            ViaCepResponse response = viaCepService.buscarEndereco(cep);
            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado para o CEP informado.");
            }
            return ResponseEntity.ok(ViaCepResponseDto.fromResponse(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao buscar endereço pelo CEP.");
        }
    }

}
