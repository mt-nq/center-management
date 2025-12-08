package com.example.center_management.dto.auth;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
public class AuthRegisterRequest {

    @Pattern(
        regexp = "^[a-zA-Z0-9_-]+$",
        message = "Username chỉ được chứa chữ cái không dấu, số, dấu gạch dưới (_) hoặc gạch ngang (-), không được có khoảng trắng"
    )
    @NotBlank
    private String username;

    @Pattern(
        regexp = "^0\\d{9}$",
        message = "Số điện thoại phải bắt đầu bằng 0 và có đúng 10 chữ số"
    )
    @NotBlank
    private String phone;

    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$",
        message = "Email phải có định dạng hợp lệ và kết thúc bằng @gmail.com"
    )
    @NotBlank
    private String email;

    @NotNull
    private LocalDate dob;

    @Pattern(
        regexp = "^.{8,}$",
        message = "Mật khẩu phải có ít nhất 8 ký tự"
    )
    @NotBlank
    private String password;

    private String fullName;

    private String hometown;
    private String province;
}
