package io.github.dudupuci.appdespesas.domain.entities;

import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeUuid;
import jakarta.persistence.*;
import lombok.Getter;
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

@Entity
@Table(name = "endereco")
@Getter
@Setter
public class Endereco extends EntidadeUuid {
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;

    @Column(length = 8)
    private String cep;
}
