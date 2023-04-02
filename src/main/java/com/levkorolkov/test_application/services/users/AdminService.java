package com.levkorolkov.test_application.services.users;

import com.levkorolkov.test_application.dto.UserDTO;
import com.levkorolkov.test_application.entity.User;
import com.levkorolkov.test_application.entity.enums.Role;
import com.levkorolkov.test_application.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class AdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);
    private final UserService userService;
    private final UserRepository userRepository;

    public AdminService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public User updateUserRole(User user, Role role) {
        Set<Role> roles = user.getRoles();
        roles.add(role);

        user.setRoles(roles);
        return userRepository.save(user);
    }


    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
