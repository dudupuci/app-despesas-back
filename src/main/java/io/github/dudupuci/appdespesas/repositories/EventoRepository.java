package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.models.entities.Evento;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByUsuarioSistema(UsuarioSistema usuarioSistema);

    @Query("SELECT e FROM Evento e WHERE e.usuarioSistema = :usuario " +
           "AND e.dataInicio BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY e.dataInicio ASC")
    List<Evento> findByUsuarioAndPeriodo(
        @Param("usuario") UsuarioSistema usuario,
        @Param("dataInicio") Date dataInicio,
        @Param("dataFim") Date dataFim
    );

    @Query("SELECT e FROM Evento e WHERE e.usuarioSistema = :usuario " +
           "AND e.isRecorrente = true " +
           "ORDER BY e.dataInicio ASC")
    List<Evento> findEventosRecorrentesByUsuario(@Param("usuario") UsuarioSistema usuario);

    @Query("SELECT e FROM Evento e WHERE e.usuarioSistema = :usuario " +
           "AND CAST(e.dataInicio AS date) = CAST(:data AS date) " +
           "ORDER BY e.dataInicio ASC")
    List<Evento> findByUsuarioAndData(
        @Param("usuario") UsuarioSistema usuario,
        @Param("data") Date data
    );
}

