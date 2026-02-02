package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.controllers.dtos.request.role.CriarRoleRequestDto;
import io.github.dudupuci.appdespesas.models.entities.Role;
import io.github.dudupuci.appdespesas.repositories.RolesRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesService {

    private final RolesRepository rolesRepository;

    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public List<Role> buscarTodos() {
        return rolesRepository.buscarTodos();
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
            rolesRepository.salvar(role);
        }
        return role;
    }
}
