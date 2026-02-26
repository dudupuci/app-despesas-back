package io.github.dudupuci.appdespesas.models.entities.base;

import io.github.dudupuci.appdespesas.models.enums.CanalNotificacao;
import io.github.dudupuci.appdespesas.models.enums.TipoNotificacao;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.processing.Exclude;

import java.util.List;
import java.util.UUID;

@ToString
@MappedSuperclass
@Getter
@Setter
public abstract class Notificacao extends EntidadeImutavelUuid {
    private String titulo;
    private String mensagem;
    private UUID usuarioId;
    private TipoNotificacao tipo;
}