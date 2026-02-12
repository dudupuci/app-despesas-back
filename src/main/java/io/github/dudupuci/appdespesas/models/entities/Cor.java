package io.github.dudupuci.appdespesas.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.dudupuci.appdespesas.models.entities.base.EntidadeUuid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "cores")
@Getter
@Setter
public class Cor extends EntidadeUuid {

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "codigo_hexadecimal", nullable = false, length = 7)
    private String codigoHexadecimal;

    // Relacionamento OPCIONAL com UsuarioSistema (para cores criadas por usuários)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private UsuarioSistema usuarioSistema;


    @Column(name = "is_visivel")
    private Boolean isVisivel = true;

    public Cor() {
        super();
    }

    public Cor(String nome, String codigoHexadecimal) {
        super();
        this.nome = nome;
        this.codigoHexadecimal = codigoHexadecimal;
    }

}
