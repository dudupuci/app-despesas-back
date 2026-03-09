package io.github.dudupuci.appdespesas.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO para mensagens de email que serão enviadas via fila SQS
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailQueueDto implements Serializable {

    private String destinatario;

    private String assunto;

    private String corpo;

    /**
     * Tipo de email: BOAS_VINDAS, RECUPERACAO_SENHA, NOTIFICACAO_MOVIMENTACAO, etc
     */
    private String tipoEmail;

    /**
     * Dados adicionais que podem ser usados no template do email
     */
    private Map<String, Object> dadosAdicionais;
}

