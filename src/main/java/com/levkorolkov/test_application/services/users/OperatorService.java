package com.levkorolkov.test_application.services.users;

import com.levkorolkov.test_application.entity.Application;
import com.levkorolkov.test_application.entity.User;
import com.levkorolkov.test_application.entity.enums.Status;
import com.levkorolkov.test_application.exceptions.ApplicationNotFoundException;
import com.levkorolkov.test_application.repository.ApplicationRepository;
import com.levkorolkov.test_application.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
public class OperatorService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public OperatorService(ApplicationRepository applicationRepository, UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    public List<Application> getAllUserApplicationOrderByDateTimeDesc(User user, Pageable pageable) {
        return applicationRepository.findAllByUserAndStatus_SentOrderByDateTimeDesc(user, pageable);
    }

    public List<Application> getAllUserByUsernameApplicationOrderByDateTimeDesc(String username, Pageable pageable) {
        return applicationRepository.findAllByUserNameAndStatus_SentOrderByDateTimeDesc(username, pageable);
    }

    public List<Application> getAllUserByUsernameApplicationOrderByDateTimeAsc(String username, Pageable pageable) {
        return applicationRepository.findAllByUserNameAndStatus_SentOrderByDateTimeAsc(username, pageable);
    }

    public List<Application> getAllUserApplicationOrderByDateTimeAsc(User user, Pageable pageable) {
        return applicationRepository.findAllByUserAndStatus_SentOrderByDateTimeAsc(user, pageable);
    }

    public List<Application> getAllApplicationsTimeDesc(Pageable pageable) {
        return applicationRepository.findAllApplicationsByStatus_SentOrderByDateTimeDesc(pageable);
    }

    public List<Application> getAllApplicationsTimeAsc(Pageable pageable) {
        return applicationRepository.findAllApplicationsByStatus_SentOrderByDateTimeAsc(pageable);
    }

    public Application setStatusAccepted(Long appId, Principal principal) {
        User user = getUserByPrincipal(principal);
        Application application = applicationRepository.findApplicationsByIdAndUserAndStatus_Sent(appId, user)
                .orElseThrow(() -> new ApplicationNotFoundException("Application with id=" + appId +
                        " and user=" + user.getEmail()
                        + " not found"));
        application.setStatus(Status.ACCEPTED);

        return applicationRepository.save(application);
    }

    public Application setStatusRejected(Long appId, Principal principal) {
        User user = getUserByPrincipal(principal);
        Application application = applicationRepository
                .findApplicationsByIdAndUserAndStatus_Sent(appId, user)
                .orElseThrow(() -> new ApplicationNotFoundException("Application with id=" + appId + " and user=" + user.getEmail()
                        + " not found"));
        application.setStatus(Status.REJECTED);

        return applicationRepository.save(application);
    }


    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
