package io.github.dudupuci.appdespesas.infrastructure.persistence.entities;

import io.github.dudupuci.appdespesas.domain.entities.Role;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base.JpaEntidade;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
public class RoleJpaEntity extends JpaEntidade {
    private String nome;
    private String descricao;
    private Integer poder;

    public RoleJpaEntity() {
        super();
    }

    /**
     * Converte a entidade JPA para entidade de domínio
     */
    public Role toEntity() {
        Role role = new Role();
        super.toDomain(role);
        role.setNome(this.nome);
        role.setDescricao(this.descricao);
        role.setPoder(this.poder);
        return role;
    }

    /**
     * Cria uma entidade JPA a partir de uma entidade de domínio
     */
    public static RoleJpaEntity fromEntity(Role role) {
        if (role == null) return null;

        RoleJpaEntity roleJpaEntity = new RoleJpaEntity();
        roleJpaEntity.fromDomain(role);
        roleJpaEntity.setNome(role.getNome());
        roleJpaEntity.setDescricao(role.getDescricao());
        roleJpaEntity.setPoder(role.getPoder());
        return roleJpaEntity;
    }

    /**
     * Atualiza a entidade JPA com dados da entidade de domínio
     */
    public void updateFromEntity(Role role) {
        this.nome = role.getNome();
        this.descricao = role.getDescricao();
        this.poder = role.getPoder();
        this.dataAtualizacao = role.getDataAtualizacao();
    }
}

