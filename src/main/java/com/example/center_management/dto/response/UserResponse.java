package com.example.center_management.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String fullName;
    private String role;
    private String status;
    private String createdAt;
}
