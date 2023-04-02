package com.levkorolkov.test_application.web;

import com.levkorolkov.test_application.dto.ApplicationDTO;
import com.levkorolkov.test_application.entity.Application;
import com.levkorolkov.test_application.facade.ApplicationFacade;
import com.levkorolkov.test_application.services.ApplicationService;
import com.levkorolkov.test_application.services.users.OperatorService;
import com.levkorolkov.test_application.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/application")
public class ApplicationController {
    private final OperatorService operatorService;
    private final ApplicationFacade applicationFacade;
    private final ApplicationService applicationService;
    private final ResponseErrorValidation responseErrorValidation;

    public ApplicationController(OperatorService operatorService, ApplicationFacade applicationFacade, ApplicationService applicationService, ResponseErrorValidation responseErrorValidation) {
        this.operatorService = operatorService;
        this.applicationFacade = applicationFacade;
        this.applicationService = applicationService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createApplicationDraft(@Valid @RequestBody ApplicationDTO applicationDTO,
                                                         BindingResult bindingResult,
                                                         Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Application application = applicationService.createApplicationDraft(applicationDTO, principal);
        ApplicationDTO applicationDTO1 = applicationFacade.applicationToApplicationDTO(application);

        return new ResponseEntity<>(applicationDTO1, HttpStatus.OK);
    }

    @GetMapping("/all/desc/{username}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsUserDesc(@PathVariable("username") String username,
                                                                        @PageableDefault(size = 5) Pageable pageable) {
        List<ApplicationDTO> applicationDTOList = operatorService.getAllUserByUsernameApplicationOrderByDateTimeDesc(username, pageable)
                .stream()
                .map(applicationFacade::applicationToApplicationDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(applicationDTOList, HttpStatus.OK);
    }

    @GetMapping("/all/desc")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsDesc(@PageableDefault(size = 5) Pageable pageable) {
        List<ApplicationDTO> applicationDTOList = operatorService.getAllApplicationsTimeDesc(pageable)
                .stream()
                .map(applicationFacade::applicationToApplicationDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(applicationDTOList, HttpStatus.OK);
    }

    @GetMapping("/all/asc")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsAsc(@PageableDefault(size = 5) Pageable pageable) {
        List<ApplicationDTO> applicationDTOList = operatorService.getAllApplicationsTimeAsc(pageable)
                .stream()
                .map(applicationFacade::applicationToApplicationDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(applicationDTOList, HttpStatus.OK);
    }

    @PostMapping("/accept/{appId}")
    public ResponseEntity<ApplicationDTO> acceptApplication(@PathVariable("appId") String appId,
                                                            Principal principal) {
        Application application = operatorService.setStatusAccepted(Long.parseLong(appId), principal);
        ApplicationDTO applicationDTO = applicationFacade.applicationToApplicationDTO(application);
        return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
    }

    @PostMapping("/reject/{appId}")
    public ResponseEntity<ApplicationDTO> rejectApplication(@PathVariable("appId") String appId,
                                                            Principal principal) {
        Application application = operatorService.setStatusRejected(Long.parseLong(appId), principal);
        ApplicationDTO applicationDTO = applicationFacade.applicationToApplicationDTO(application);
        return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
    }

    @GetMapping("/all/asc/{username}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsUserAsc(@PathVariable("username") String username,
                                                                       @PageableDefault(size = 5) Pageable pageable) {
        List<ApplicationDTO> applicationDTOList = operatorService.getAllUserByUsernameApplicationOrderByDateTimeAsc(username, pageable)
                .stream()
                .map(applicationFacade::applicationToApplicationDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(applicationDTOList, HttpStatus.OK);
    }

    @GetMapping("/all/desc")
    public ResponseEntity<List<ApplicationDTO>> getAllApplicationDesc(@PageableDefault(size = 5) Pageable pageable) {

        List<ApplicationDTO> applicationDTOList = operatorService
                .getAllApplicationsTimeDesc(pageable)
                .stream().
                map(applicationFacade::applicationToApplicationDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(applicationDTOList, HttpStatus.OK);
    }

    @GetMapping("/all/asc")
    public ResponseEntity<List<ApplicationDTO>> getAllApplicationAsc(@PageableDefault(size = 5) Pageable pageable) {
        List<ApplicationDTO> applicationDTOList = operatorService
                .getAllApplicationsTimeAsc(pageable)
                .stream().
                map(applicationFacade::applicationToApplicationDTO).
                collect(Collectors.toList());

        return new ResponseEntity<>(applicationDTOList, HttpStatus.OK);
    }
}
