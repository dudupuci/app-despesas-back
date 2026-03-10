package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.ports.repositories.RoleRepositoryPort;
import io.github.dudupuci.appdespesas.application.usecases.role.CriarRoleCommand;
import io.github.dudupuci.appdespesas.domain.entities.Role;

import java.util.List;

public class RoleService {

    private final RoleRepositoryPort rolesRepository;

    public RoleService(RoleRepositoryPort rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public Role criarRoleIfNaoExistir(CriarRoleCommand cmd) {
        Role role = rolesRepository.buscarPorNome(cmd.nome());
        if (role == null) {
            role = new Role(cmd.nome(), cmd.descricao(), cmd.poder());
            rolesRepository.save(role);
        }
        return role;
    }

    public List<Role> buscarTodos() {
        return rolesRepository.findAll();
    }
}
