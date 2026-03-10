package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.CompromissoRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Compromisso;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.CompromissoJpaEntity;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.UsuarioSistemaJpaEntity;
import io.github.dudupuci.appdespesas.infrastructure.repositories.CompromissoJpaRepository;
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

    private final CompromissoJpaRepository jpaRepository;

    @Override
    public Compromisso save(Compromisso compromisso) {
        CompromissoJpaEntity jpa = CompromissoJpaEntity.fromEntity(compromisso);

        if (compromisso.getId() != null) {
            Optional<CompromissoJpaEntity> existing = jpaRepository.findById(compromisso.getId());
            if (existing.isPresent()) {
                CompromissoJpaEntity existingEntity = existing.get();
                existingEntity.updateFromEntity(compromisso);
                jpa = existingEntity;
            }
        }

        if (compromisso.getUsuarioSistema() != null) {
            jpa.setUsuarioSistema(UsuarioSistemaJpaEntity.fromEntity(compromisso.getUsuarioSistema()));
        }

        return jpaRepository.save(jpa).toEntity();
    }

    @Override
    public Optional<Compromisso> findById(UUID id) {
        return jpaRepository.findById(id).map(CompromissoJpaEntity::toEntity);
    }

    @Override
    public List<Compromisso> findByUsuarioSistema(UsuarioSistema usuarioSistema) {
        UsuarioSistemaJpaEntity jpaUsuario = UsuarioSistemaJpaEntity.fromEntity(usuarioSistema);
        return jpaRepository.findByUsuarioSistema(jpaUsuario).stream()
                .map(CompromissoJpaEntity::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Compromisso> findByUsuarioAndPeriodo(UsuarioSistema usuario, Date dataInicio, Date dataFim) {
        UsuarioSistemaJpaEntity jpaUsuario = UsuarioSistemaJpaEntity.fromEntity(usuario);
        return jpaRepository.findByUsuarioAndPeriodo(jpaUsuario, dataInicio, dataFim).stream()
                .map(CompromissoJpaEntity::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Compromisso> findByUsuarioAndConcluido(UsuarioSistema usuario, Boolean concluido) {
        UsuarioSistemaJpaEntity jpaUsuario = UsuarioSistemaJpaEntity.fromEntity(usuario);
        return jpaRepository.findByUsuarioAndConcluido(jpaUsuario, concluido).stream()
                .map(CompromissoJpaEntity::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Compromisso> findByUsuarioAndData(UsuarioSistema usuario, Date data) {
        UsuarioSistemaJpaEntity jpaUsuario = UsuarioSistemaJpaEntity.fromEntity(usuario);
        return jpaRepository.findByUsuarioAndData(jpaUsuario, data).stream()
                .map(CompromissoJpaEntity::toEntity).collect(Collectors.toList());
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

