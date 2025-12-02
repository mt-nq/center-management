package com.example.center_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dùng để trả về cho FE:
 *  - token: JWT
 *  - user: thông tin user đơn giản (id, username, role, ...)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UserSimpleResponse user;
}
