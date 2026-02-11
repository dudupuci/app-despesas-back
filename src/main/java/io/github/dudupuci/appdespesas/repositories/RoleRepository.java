package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca uma role por nome
     */
    @Query("SELECT r FROM Role r WHERE r.nome = :nome")
    Role buscarPorNome(@Param("nome") String nome);
}

