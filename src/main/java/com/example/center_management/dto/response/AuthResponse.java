// AuthResponse.java
package com.example.center_management.dto.response;

import com.example.center_management.domain.enums.RoleName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String accessToken;
    private Integer userId;
    private String username;
    private RoleName role;
}
