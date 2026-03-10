package io.github.dudupuci.appdespesas.infrastructure.persistence.entities;

import io.github.dudupuci.appdespesas.domain.converters.MapStringStringConverter;
import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.domain.enums.Status;
import io.github.dudupuci.appdespesas.domain.enums.TipoPagamento;
import io.github.dudupuci.appdespesas.domain.enums.TipoRecursoPago;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base.JpaEntidadeUuid;
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
public class CobrancaJpaEntity extends JpaEntidadeUuid {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioSistemaJpaEntity usuario;

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

    public CobrancaJpaEntity() {
        super();
    }

    /**
     * Converte a entidade JPA para entidade de domínio
     */
    public Cobranca toEntity() {
        Cobranca cobranca = new Cobranca();
        super.toDomain(cobranca);
        cobranca.setValor(this.valor);
        cobranca.setDataPagamento(this.dataPagamento);
        cobranca.setStatus(this.status);
        cobranca.setTipoPagamento(this.tipoPagamento);
        cobranca.setTipoRecursoPago(this.tipoRecursoPago);
        cobranca.setIdRecursoPago(this.idRecursoPago);
        cobranca.setAsaasCobrancaId(this.asaasCobrancaId);
        cobranca.setObservacao(this.observacao);
        cobranca.setNotificacoesEnviadasAoUsuario(this.notificacoesEnviadasAoUsuario);

        if (this.usuario != null) {
            cobranca.setUsuario(this.usuario.toEntity());
        }

        return cobranca;
    }

    /**
     * Cria uma entidade JPA a partir de uma entidade de domínio (sem relacionamentos)
     */
    public static CobrancaJpaEntity fromEntity(Cobranca cobranca) {
        if (cobranca == null) return null;

        CobrancaJpaEntity cobrancaJpaEntity = new CobrancaJpaEntity();
        cobrancaJpaEntity.fromDomain(cobranca);
        cobrancaJpaEntity.setValor(cobranca.getValor());
        cobrancaJpaEntity.setDataPagamento(cobranca.getDataPagamento());
        cobrancaJpaEntity.setStatus(cobranca.getStatus());
        cobrancaJpaEntity.setTipoPagamento(cobranca.getTipoPagamento());
        cobrancaJpaEntity.setTipoRecursoPago(cobranca.getTipoRecursoPago());
        cobrancaJpaEntity.setIdRecursoPago(cobranca.getIdRecursoPago());
        cobrancaJpaEntity.setAsaasCobrancaId(cobranca.getAsaasCobrancaId());
        cobrancaJpaEntity.setObservacao(cobranca.getObservacao());
        cobrancaJpaEntity.setNotificacoesEnviadasAoUsuario(cobranca.getNotificacoesEnviadasAoUsuario());
        return cobrancaJpaEntity;
    }

    /**
     * Atualiza a entidade JPA com dados da entidade de domínio
     */
    public void updateFromEntity(Cobranca cobranca) {
        this.valor = cobranca.getValor();
        this.dataPagamento = cobranca.getDataPagamento();
        this.status = cobranca.getStatus();
        this.tipoPagamento = cobranca.getTipoPagamento();
        this.tipoRecursoPago = cobranca.getTipoRecursoPago();
        this.idRecursoPago = cobranca.getIdRecursoPago();
        this.asaasCobrancaId = cobranca.getAsaasCobrancaId();
        this.observacao = cobranca.getObservacao();
        this.notificacoesEnviadasAoUsuario = cobranca.getNotificacoesEnviadasAoUsuario();
        this.dataAtualizacao = cobranca.getDataAtualizacao();
    }
}

