package io.github.dudupuci.appdespesas.application.services.webservices.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.dudupuci.appdespesas.application.responses.cep.ViaCepResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepResponseDto(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        String estado
) {
    public static ViaCepResponseDto fromResponse(ViaCepResponse response) {
        return new ViaCepResponseDto(
                response.cep(),
                response.logradouro(),
                response.complemento(),
                response.bairro(),
                response.localidade(),
                response.uf(),
                response.estado()
        );
    }
}
