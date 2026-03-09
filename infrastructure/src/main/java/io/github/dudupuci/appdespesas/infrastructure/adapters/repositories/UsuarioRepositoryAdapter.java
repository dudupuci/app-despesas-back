package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.UsuarioRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaUsuarioSistema;
import io.github.dudupuci.appdespesas.infrastructure.repositories.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter que implementa o Port de UsuarioRepository
 * Faz a ponte entre o Application (Domain) e o Infrastructure (JPA)
 */
@Component
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuariosRepository jpaRepository;

    @Override
    public UsuarioSistema save(UsuarioSistema usuario) {
        JpaUsuarioSistema jpaUsuario = JpaUsuarioSistema.fromEntity(usuario);

        // Se já existe, busca e atualiza
        if (usuario.getId() != null) {
            Optional<JpaUsuarioSistema> existing = jpaRepository.findById(usuario.getId());
            if (existing.isPresent()) {
                JpaUsuarioSistema existingEntity = existing.get();
                existingEntity.updateFromEntity(usuario);
                jpaUsuario = existingEntity;
            }
        }

        JpaUsuarioSistema saved = jpaRepository.save(jpaUsuario);
        return saved.toEntity();
    }

    @Override
    public Optional<UsuarioSistema> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(JpaUsuarioSistema::toEntity);
    }

    @Override
    public Optional<UsuarioSistema> buscarPorEmail(String email) {
        return jpaRepository.buscarPorEmail(email)
                .map(JpaUsuarioSistema::toEntity);
    }

    @Override
    public Optional<UsuarioSistema> buscarPorNomeUsuario(String nomeUsuario) {
        return jpaRepository.buscarPorNomeUsuario(nomeUsuario)
                .map(JpaUsuarioSistema::toEntity);
    }

    @Override
    public boolean existsByContatoEmail(String email) {
        return jpaRepository.existsByContatoEmail(email);
    }

    @Override
    public boolean existsByNomeUsuario(String nomeUsuario) {
        return jpaRepository.existsByNomeUsuario(nomeUsuario);
    }

    @Override
    public void delete(UsuarioSistema usuario) {
        if (usuario.getId() != null) {
            jpaRepository.deleteById(usuario.getId());
        }
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}

