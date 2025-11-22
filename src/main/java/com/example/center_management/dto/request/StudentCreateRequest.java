package com.example.center_management.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCreateRequest {

    @NotBlank
    @Size(min = 2, max = 100)
    private String fullName;

    @NotNull
    @Past
    private LocalDate dob;

    private String hometown;

    private String province;
}
