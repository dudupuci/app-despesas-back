package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.models.entities.Compromisso;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CompromissoRepository extends JpaRepository<Compromisso, Long> {

    List<Compromisso> findByUsuarioSistema(UsuarioSistema usuarioSistema);

    @Query("SELECT c FROM Compromisso c WHERE c.usuarioSistema = :usuario " +
            "AND c.dataInicio BETWEEN :dataInicio AND :dataFim " +
            "ORDER BY c.dataInicio ASC")
    List<Compromisso> findByUsuarioAndPeriodo(
            @Param("usuario") UsuarioSistema usuario,
            @Param("dataInicio") Date dataInicio,
            @Param("dataFim") Date dataFim
    );

    @Query("SELECT c FROM Compromisso c WHERE c.usuarioSistema = :usuario " +
            "AND c.concluido = :concluido " +
            "ORDER BY c.dataInicio ASC")
    List<Compromisso> findByUsuarioAndConcluido(
            @Param("usuario") UsuarioSistema usuario,
            @Param("concluido") Boolean concluido
    );

    @Query("SELECT c FROM Compromisso c WHERE c.usuarioSistema = :usuario " +
            "AND CAST(c.dataInicio AS date) = CAST(:data AS date) " +
            "ORDER BY c.dataInicio ASC")
    List<Compromisso> findByUsuarioAndData(
            @Param("usuario") UsuarioSistema usuario,
            @Param("data") Date data
    );
}