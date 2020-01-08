package dev.arielalvesdutra.prs.services;

import dev.arielalvesdutra.prs.entities.Permission;
import dev.arielalvesdutra.prs.repositories.PermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission create(Permission permission) {
        return permissionRepository.save(permission);
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Page<Permission> findAll(Pageable pageable) {
        return permissionRepository.findAll(pageable);
    }

    public List<Permission> findAllByIds(List<Long> permissionsIds) {
        return permissionRepository.findAllById(permissionsIds);
    }

    public List<Permission> findAllByRoleId(Long roleId) {
        return permissionRepository.findAllByRoles_Id(roleId);
    }

    public Page<Permission> findAllByRoleId(Long roleId, Pageable pageable) {
        return permissionRepository.findAllByRoles_Id(roleId, pageable);
    }

    public Permission findById(Long id) {
        return permissionRepository.findById(id).get();
    }

    @Transactional
    public void deleteById(Long id) {
        Permission permission = findById(id);

        permissionRepository.deleteById(permission.getId());
    }

    @Transactional
    public Permission update(Long id, Permission parameterPermission) {
        Permission existingPermission = findById(id);

        existingPermission.setName(parameterPermission.getName());
        existingPermission.setDescription(parameterPermission.getDescription());

        return existingPermission;
    }
}
