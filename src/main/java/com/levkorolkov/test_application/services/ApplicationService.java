package com.levkorolkov.test_application.services;

import com.levkorolkov.test_application.dto.ApplicationDTO;
import com.levkorolkov.test_application.entity.Application;
import com.levkorolkov.test_application.entity.User;
import com.levkorolkov.test_application.entity.enums.Status;
import com.levkorolkov.test_application.repository.ApplicationRepository;
import com.levkorolkov.test_application.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class ApplicationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationService.class);
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    public Application createApplicationDraft(ApplicationDTO applicationDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Application application = new Application();
        application.setUser(user);
        application.setTitle(applicationDTO.getTitle());
        application.setText(applicationDTO.getText());
        application.setStatus(Status.DRAFT);

        LOGGER.info("Saving application draft from User: " + user.getEmail());
        return applicationRepository.save(application);
    }

    public Application sendApplication(Principal principal, Long id) {
        User user = getUserByPrincipal(principal);
        List<Application> applications = user.getApplications();
        Application application = applications.get(id.intValue() - 1);
        application.setStatus(Status.SENT);

        LOGGER.info("User with username: " + user.getEmail() + " sent and application");
        return applicationRepository.save(application);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }

}
