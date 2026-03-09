package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.infrastructure.controllers.admin.dtos.request.role.CriarRoleRequestDto;
import io.github.dudupuci.appdespesas.domain.entities.Role;
import io.github.dudupuci.appdespesas.infrastructure.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository rolesRepository;

    public RoleService(RoleRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public List<Role> buscarTodos() {
        return rolesRepository.findAll();
    }

    @Transactional
    public Role criarRoleIfNaoExistir(CriarRoleRequestDto dto) {
        Role role = rolesRepository.buscarPorNome(dto.nome());
        if (role == null) {
            role = new Role(
                    dto.nome(),
                    dto.descricao(),
                    dto.poder()
            );
            rolesRepository.save(role);
        }
        return role;
    }
}
