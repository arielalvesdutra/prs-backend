package dev.arielalvesdutra.prs.repositories;

import dev.arielalvesdutra.prs.entities.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Page<Permission> findAllByRoles_Id(Long roleId, Pageable pageable);

    List<Permission> findAllByRoles_Id(Long roleId);
}
