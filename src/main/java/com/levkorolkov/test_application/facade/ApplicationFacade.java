package com.levkorolkov.test_application.facade;

import com.levkorolkov.test_application.dto.ApplicationDTO;
import com.levkorolkov.test_application.entity.Application;
import org.springframework.stereotype.Component;

@Component
public class ApplicationFacade {
    public ApplicationDTO applicationToApplicationDTO(Application application) {
        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setTitle(application.getTitle());
        applicationDTO.setText(application.getText());
        applicationDTO.setUsername(application.getUser().getUsername());
        applicationDTO.setStatus(application.getStatus());

        return applicationDTO;
    }
}
