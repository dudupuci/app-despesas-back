package io.github.dudupuci.appdespesas.domain.entities.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Classe base para entidades que precisam de UUID MANUAL (não gerado automaticamente)
 * Usada para entidades do sistema que precisam ter IDs fixos e conhecidos.
 *
 * Exemplo: Administrador do sistema, dados padrão, etc.
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class EntidadeUuidManual implements Serializable {

    protected UUID id;
    protected Date dataCriacao = new Date();
    protected Date dataAtualizacao = new Date();
}

