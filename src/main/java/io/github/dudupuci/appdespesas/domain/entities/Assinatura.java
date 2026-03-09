package io.github.dudupuci.appdespesas.domain.entities;

import io.github.dudupuci.appdespesas.domain.entities.base.Entidade;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "assinatura")
@Getter
@Setter
public class Assinatura extends Entidade {
    private String nomePlano;
    private BigDecimal valor;
    private String descricao;
    private List<String> beneficios;
}
