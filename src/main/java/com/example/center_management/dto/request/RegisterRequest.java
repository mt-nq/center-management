// RegisterRequest.java
package com.example.center_management.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String email;
    private String password;
    private String fullName;
}
