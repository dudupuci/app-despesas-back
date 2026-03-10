package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.CompromissoJpaEntity;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.UsuarioSistemaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CompromissoJpaRepository extends JpaRepository<CompromissoJpaEntity, Long> {

    List<CompromissoJpaEntity> findByUsuarioSistema(UsuarioSistemaJpaEntity usuarioSistema);

    @Query("SELECT c FROM CompromissoJpaEntity c WHERE c.usuarioSistema = :usuario " +
            "AND c.dataInicio BETWEEN :dataInicio AND :dataFim " +
            "ORDER BY c.dataInicio ASC")
    List<CompromissoJpaEntity> findByUsuarioAndPeriodo(
            @Param("usuario") UsuarioSistemaJpaEntity usuario,
            @Param("dataInicio") Date dataInicio,
            @Param("dataFim") Date dataFim
    );

    @Query("SELECT c FROM CompromissoJpaEntity c WHERE c.usuarioSistema = :usuario " +
            "AND c.concluido = :concluido " +
            "ORDER BY c.dataInicio ASC")
    List<CompromissoJpaEntity> findByUsuarioAndConcluido(
            @Param("usuario") UsuarioSistemaJpaEntity usuario,
            @Param("concluido") Boolean concluido
    );

    @Query("SELECT c FROM CompromissoJpaEntity c WHERE c.usuarioSistema = :usuario " +
            "AND CAST(c.dataInicio AS date) = CAST(:data AS date) " +
            "ORDER BY c.dataInicio ASC")
    List<CompromissoJpaEntity> findByUsuarioAndData(
            @Param("usuario") UsuarioSistemaJpaEntity usuario,
            @Param("data") Date data
    );
}