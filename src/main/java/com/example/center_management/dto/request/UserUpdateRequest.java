// UserUpdateRequest.java
package com.example.center_management.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {

    private String fullName;
    private String email;
    private String hometown;
    private String permanentProvince;
}
