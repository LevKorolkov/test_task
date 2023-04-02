package com.levkorolkov.test_application.facade;

import com.levkorolkov.test_application.dto.UserDTO;
import com.levkorolkov.test_application.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    public UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getName());
        userDTO.setLastname(user.getLastname());
        userDTO.setUsername(user.getUsername());

        return userDTO;
    }
}
