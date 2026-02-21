package io.github.dudupuci.appdespesas.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.dudupuci.appdespesas.models.entities.base.EntidadeUuid;
import io.github.dudupuci.appdespesas.models.enums.Status;
import io.github.dudupuci.appdespesas.models.enums.TipoPagamento;
import io.github.dudupuci.appdespesas.models.enums.TipoRecursoPago;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "cobrancas")
@Getter
@Setter
public class Cobranca extends EntidadeUuid {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private UsuarioSistema usuario;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "data_pagamento")
    private Date dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoPagamento metodo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_recurso_pago", nullable = false)
    private TipoRecursoPago tipoRecursoPago;

    @Column(name = "id_recurso_pago")
    private String idRecursoPago;

    @Column(name = "id_externo")
    private String idExterno;

    @Column(length = 255)
    private String observacao;
}

