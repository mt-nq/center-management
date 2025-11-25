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
    private Boolean isActive;
    private String createdAt;   // để map sang String cho đẹp (ISO)
}
