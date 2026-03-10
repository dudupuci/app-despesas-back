package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.RoleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, Long> {

    /**
     * Busca uma role por nome
     */
    @Query("SELECT r FROM RoleJpaEntity r WHERE r.nome = :nome")
    RoleJpaEntity buscarPorNome(@Param("nome") String nome);
}

