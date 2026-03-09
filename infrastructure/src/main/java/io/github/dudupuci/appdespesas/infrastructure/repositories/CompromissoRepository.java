package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaCompromisso;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaUsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CompromissoRepository extends JpaRepository<JpaCompromisso, Long> {

    List<JpaCompromisso> findByUsuarioSistema(JpaUsuarioSistema usuarioSistema);

    @Query("SELECT c FROM JpaCompromisso c WHERE c.usuarioSistema = :usuario " +
            "AND c.dataInicio BETWEEN :dataInicio AND :dataFim " +
            "ORDER BY c.dataInicio ASC")
    List<JpaCompromisso> findByUsuarioAndPeriodo(
            @Param("usuario") JpaUsuarioSistema usuario,
            @Param("dataInicio") Date dataInicio,
            @Param("dataFim") Date dataFim
    );

    @Query("SELECT c FROM JpaCompromisso c WHERE c.usuarioSistema = :usuario " +
            "AND c.concluido = :concluido " +
            "ORDER BY c.dataInicio ASC")
    List<JpaCompromisso> findByUsuarioAndConcluido(
            @Param("usuario") JpaUsuarioSistema usuario,
            @Param("concluido") Boolean concluido
    );

    @Query("SELECT c FROM JpaCompromisso c WHERE c.usuarioSistema = :usuario " +
            "AND CAST(c.dataInicio AS date) = CAST(:data AS date) " +
            "ORDER BY c.dataInicio ASC")
    List<JpaCompromisso> findByUsuarioAndData(
            @Param("usuario") JpaUsuarioSistema usuario,
            @Param("data") Date data
    );
}