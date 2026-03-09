package io.github.dudupuci.appdespesas.domain.entities;

import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeUuid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe que representa um endereço.
 * No ASAAS:
 * - logradouro = address
 * - numero = addressNumber
 * - complemento = complement
 * - bairro = province
 * - cep = postalCode
 * - todos opcionais.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco extends EntidadeUuid {
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cep;
}
