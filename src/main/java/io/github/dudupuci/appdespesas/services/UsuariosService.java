package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.repositories.UsuariosRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;

    public UsuariosService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    public UsuarioSistema buscarPorNomeUsuario(String nomeUsuario) {
        return this.usuariosRepository.buscarPorNomeUsuario(nomeUsuario).orElse(null);
    }

    public boolean existePorNomeUsuario(String nomeUsuario) {
        return this.usuariosRepository.existsByNomeUsuario(nomeUsuario);
    }
}
