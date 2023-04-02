package com.levkorolkov.test_application.dto;

import com.levkorolkov.test_application.entity.enums.Status;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ApplicationDTO {
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String text;
    private String username;
    private Status status;
}
