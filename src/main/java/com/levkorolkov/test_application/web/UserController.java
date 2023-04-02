package com.levkorolkov.test_application.web;

import com.levkorolkov.test_application.dto.UserDTO;
import com.levkorolkov.test_application.entity.User;
import com.levkorolkov.test_application.entity.enums.Role;
import com.levkorolkov.test_application.facade.UserFacade;
import com.levkorolkov.test_application.services.users.AdminService;
import com.levkorolkov.test_application.services.users.UserService;
import com.levkorolkov.test_application.validations.ResponseErrorValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;
    private final UserFacade userFacade;
    private final AdminService adminService;
    private final ResponseErrorValidation responseErrorValidation;

    public UserController(UserService userService, UserFacade userFacade, AdminService adminService, ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.userFacade = userFacade;
        this.adminService = adminService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") String userId) {
        User user = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userList = userService.getAllUsers()
                .stream()
                .map(userFacade::userToUserDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUserRole(@PathVariable("userId") String userId,
                                                 @PathVariable("role") String role,
                                                 BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        User user = userService.getUserById(Long.parseLong(userId));
        adminService.updateUserRole(user, Role.valueOf(role));
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
