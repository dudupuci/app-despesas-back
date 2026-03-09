package io.github.dudupuci.appdespesas.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.dudupuci.appdespesas.domain.converters.MapStringStringConverter;
import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeUuid;
import io.github.dudupuci.appdespesas.domain.enums.Status;
import io.github.dudupuci.appdespesas.domain.enums.TipoPagamento;
import io.github.dudupuci.appdespesas.domain.enums.TipoRecursoPago;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "cobranca")
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
    @Column(name = "tipo_pagamento", nullable = false, length = 20)
    private TipoPagamento tipoPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_recurso_pago", nullable = false)
    private TipoRecursoPago tipoRecursoPago;

    @Column(name = "id_recurso_pago")
    private String idRecursoPago;

    @Column(name = "asaas_cobranca_id", unique = true)
    private String asaasCobrancaId;

    @Column
    private String observacao;

    @Convert(converter = MapStringStringConverter.class)
    @Column(name = "notificacoes_enviadas", columnDefinition = "TEXT")
    private Map<String, String> notificacoesEnviadasAoUsuario = new HashMap<>();

    public void confirmarCobranca(Date dataPagamento) {
        this.status = Status.CONFIRMADO;
        this.dataPagamento = dataPagamento;
    }
}

