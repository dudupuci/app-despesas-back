package io.github.dudupuci.appdespesas.application.ports.repositories;

import io.github.dudupuci.appdespesas.domain.entities.Compromisso;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) para o repositório de Compromissos
 */
public interface CompromissoRepositoryPort {

    Compromisso save(Compromisso compromisso);

    Optional<Compromisso> findById(UUID id);

    List<Compromisso> findByUsuarioSistema(UsuarioSistema usuarioSistema);

    List<Compromisso> findByUsuarioAndPeriodo(UsuarioSistema usuario, Date dataInicio, Date dataFim);

    List<Compromisso> findByUsuarioAndConcluido(UsuarioSistema usuario, Boolean concluido);

    List<Compromisso> findByUsuarioAndData(UsuarioSistema usuario, Date data);

    void delete(Compromisso compromisso);

    void deleteById(UUID id);
}

