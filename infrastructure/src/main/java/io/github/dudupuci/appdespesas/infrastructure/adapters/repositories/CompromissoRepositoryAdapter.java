package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.CompromissoRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Compromisso;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaCompromisso;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaUsuarioSistema;
import io.github.dudupuci.appdespesas.infrastructure.repositories.CompromissoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompromissoRepositoryAdapter implements CompromissoRepositoryPort {

    private final CompromissoRepository jpaRepository;

    @Override
    public Compromisso save(Compromisso compromisso) {
        JpaCompromisso jpa = JpaCompromisso.fromEntity(compromisso);

        if (compromisso.getId() != null) {
            Optional<JpaCompromisso> existing = jpaRepository.findById(compromisso.getId());
            if (existing.isPresent()) {
                JpaCompromisso existingEntity = existing.get();
                existingEntity.updateFromEntity(compromisso);
                jpa = existingEntity;
            }
        }

        if (compromisso.getUsuarioSistema() != null) {
            jpa.setUsuarioSistema(JpaUsuarioSistema.fromEntity(compromisso.getUsuarioSistema()));
        }

        return jpaRepository.save(jpa).toEntity();
    }

    @Override
    public Optional<Compromisso> findById(UUID id) {
        return jpaRepository.findById(id).map(JpaCompromisso::toEntity);
    }

    @Override
    public List<Compromisso> findByUsuarioSistema(UsuarioSistema usuarioSistema) {
        JpaUsuarioSistema jpaUsuario = JpaUsuarioSistema.fromEntity(usuarioSistema);
        return jpaRepository.findByUsuarioSistema(jpaUsuario).stream()
                .map(JpaCompromisso::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Compromisso> findByUsuarioAndPeriodo(UsuarioSistema usuario, Date dataInicio, Date dataFim) {
        JpaUsuarioSistema jpaUsuario = JpaUsuarioSistema.fromEntity(usuario);
        return jpaRepository.findByUsuarioAndPeriodo(jpaUsuario, dataInicio, dataFim).stream()
                .map(JpaCompromisso::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Compromisso> findByUsuarioAndConcluido(UsuarioSistema usuario, Boolean concluido) {
        JpaUsuarioSistema jpaUsuario = JpaUsuarioSistema.fromEntity(usuario);
        return jpaRepository.findByUsuarioAndConcluido(jpaUsuario, concluido).stream()
                .map(JpaCompromisso::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Compromisso> findByUsuarioAndData(UsuarioSistema usuario, Date data) {
        JpaUsuarioSistema jpaUsuario = JpaUsuarioSistema.fromEntity(usuario);
        return jpaRepository.findByUsuarioAndData(jpaUsuario, data).stream()
                .map(JpaCompromisso::toEntity).collect(Collectors.toList());
    }

    @Override
    public void delete(Compromisso compromisso) {
        if (compromisso.getId() != null) {
            jpaRepository.deleteById(compromisso.getId());
        }
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}

