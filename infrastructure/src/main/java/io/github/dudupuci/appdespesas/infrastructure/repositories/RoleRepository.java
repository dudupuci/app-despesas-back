package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<JpaRole, Long> {

    /**
     * Busca uma role por nome
     */
    @Query("SELECT r FROM JpaRole r WHERE r.nome = :nome")
    JpaRole buscarPorNome(@Param("nome") String nome);
}

