package dev.arielalvesdutra.prs.services;

import dev.arielalvesdutra.prs.entities.Role;
import dev.arielalvesdutra.prs.entities.User;
import dev.arielalvesdutra.prs.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public List<Role> findAllRoles(Long userId) {
        User user = findById(userId);

        return roleService.findAllByUserId(user.getId());
    }

    public Page<Role> findAllRoles(Long userId, Pageable pageable) {
        User user = findById(userId);

        return roleService.findAllByUserId(user.getId(), pageable);
    }

    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    public void deleteById(Long id) {
        User user = findById(id);

        userRepository.deleteById(user.getId());
    }

    @Transactional
    public User update(Long id, User parameterUser) {
        User existingUser = findById(id);

        existingUser.setName(parameterUser.getName());
        existingUser.setEmail(parameterUser.getEmail());
        if (parameterUser.getPassword() != null)
            existingUser.setPassword(parameterUser.getPassword());

        return existingUser;
    }

    @Transactional
    public Set<Role> updateRoles(Long id, Set<Role> roleSet) {
        User user = findById(id);
        user.setRoles(roleSet);
        return user.getRoles();
    }
}
