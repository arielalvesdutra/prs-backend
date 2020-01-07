package dev.arielalvesdutra.prs.repositories;

import dev.arielalvesdutra.prs.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
