package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.ports.repositories.RoleRepositoryPort;
import io.github.dudupuci.appdespesas.infrastructure.controllers.admin.dtos.request.role.CriarRoleRequestDto;
import io.github.dudupuci.appdespesas.domain.entities.Role;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepositoryPort rolesRepository;

    public RoleService(RoleRepositoryPort rolesRepository) {
        this.rolesRepository = rolesRepository;
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
