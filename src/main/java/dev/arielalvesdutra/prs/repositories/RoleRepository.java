package dev.arielalvesdutra.prs.repositories;

import dev.arielalvesdutra.prs.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByUsers_Id(Long userId);

    Page<Role> findAllByUsers_Id(Long useId, Pageable pageable);
}
