package io.github.dudupuci.appdespesas.models.entities;

import io.github.dudupuci.appdespesas.models.entities.base.Entidade;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "assinaturas")
@Getter
@Setter
public class Assinatura extends Entidade {
    private String nomePlano;
    private BigDecimal valor;
    private String descricao;
    private List<String> beneficios;
}
