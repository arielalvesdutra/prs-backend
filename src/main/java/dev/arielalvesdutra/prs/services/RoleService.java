package dev.arielalvesdutra.prs.services;

import dev.arielalvesdutra.prs.entities.Permission;
import dev.arielalvesdutra.prs.entities.Role;
import dev.arielalvesdutra.prs.repositories.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    private final PermissionService permissionService;

    public RoleService(RoleRepository roleRepository, PermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.permissionService = permissionService;
    }

    public Role create(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    public List<Role> findAllByIds(List<Long> rolesIdsList) {
        return roleRepository.findAllById(rolesIdsList);
    }

    public List<Role> findAllByUserId(Long userId) {
        return roleRepository.findAllByUsers_Id(userId);
    }

    public Page<Role> findAllByUserId(Long useId, Pageable pageable) {
        return roleRepository.findAllByUsers_Id(useId, pageable);
    }

    public List<Permission> findAllPermissions(Long roleId) {
        Role role = findById(roleId);

        return permissionService.findAllByRoleId(role.getId());
    }

    public Page<Permission> findAllPermissions(Long roleId, Pageable pageable) {
        Role role = findById(roleId);

        return permissionService.findAllByRoleId(role.getId(), pageable);
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).get();
    }

    @Transactional
    public void deleteById(Long id) {
        Role role = findById(id);

        roleRepository.deleteById(role.getId());
    }

    @Transactional
    public Role update(Long id, Role parameterRole) {
        Role existingRole = findById(id);

        existingRole.setName(parameterRole.getName());
        existingRole.setDescription(parameterRole.getDescription());

        return existingRole;
    }

    @Transactional
    public Set<Permission> updatePermissions(Long id, Set<Permission> permissions) {
        Role role = findById(id);

        role.setPermissions(permissions);

        return role.getPermissions();
    }
}
