package io.github.dudupuci.appdespesas.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeUuid;
import io.github.dudupuci.appdespesas.domain.enums.Status;
import io.github.dudupuci.appdespesas.domain.enums.TipoPagamento;
import io.github.dudupuci.appdespesas.domain.enums.TipoRecursoPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cobranca extends EntidadeUuid {

    private UsuarioSistema usuario;
    private BigDecimal valor;
    private Date dataPagamento;
    private Status status;
    private TipoPagamento tipoPagamento;
    private TipoRecursoPago tipoRecursoPago;
    private String idRecursoPago;
    private String asaasCobrancaId;
    private String observacao;
    private Map<String, String> notificacoesEnviadasAoUsuario = new HashMap<>();

    public void confirmarCobranca(Date dataPagamento) {
        this.status = Status.CONFIRMADO;
        this.dataPagamento = dataPagamento;
    }
}

