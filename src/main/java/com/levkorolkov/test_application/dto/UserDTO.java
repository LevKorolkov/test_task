package com.levkorolkov.test_application.dto;

import com.levkorolkov.test_application.entity.enums.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    private String username;
    @NotEmpty
    private Set<Role> roles;

}
