package com.example.center_management.service.impl;

import com.example.center_management.domain.entity.User;
import com.example.center_management.domain.enums.Role;
import com.example.center_management.dto.auth.AdminCreateRequest;
import com.example.center_management.dto.auth.AuthLoginRequest;
import com.example.center_management.dto.auth.StudentRegisterRequest;
import com.example.center_management.dto.response.UserSimpleResponse;
import com.example.center_management.exception.BadRequestException;
import com.example.center_management.repository.UserRepository;
import com.example.center_management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ============ /auth/register – Học viên đăng ký ============
    @Override
    @Transactional
    public UserSimpleResponse registerStudent(StudentRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())) // mã hoá
                .role(Role.STUDENT)
                .isActive(true)
                .build();

        user = userRepository.save(user);
        return toSimpleResponse(user);
    }

    // ============ /auth/login ============
    @Override
    @Transactional(readOnly = true)
    public UserSimpleResponse login(AuthLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid username or password"));

        // DEBUG xem cho chắc
        boolean match = passwordEncoder.matches(request.getPassword(), user.getPassword());
        System.out.println(">>> LOGIN DEBUG username=" + request.getUsername()
                + " raw=" + request.getPassword()
                + " hash=" + user.getPassword()
                + " match=" + match);

        if (!match) {
            throw new BadRequestException("Invalid username or password");
        }

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new BadRequestException("User is inactive");
        }

        return toSimpleResponse(user);
    }

    // ============ /admin/users/create-admin ============
    @Override
    @Transactional
    public UserSimpleResponse createAdmin(AdminCreateRequest request) {
        // Admin cố định, không cho tạo qua API nữa
        throw new BadRequestException("Admin account is fixed and cannot be created via API");
    }

    // ============ Helper ============
    private UserSimpleResponse toSimpleResponse(User user) {
        return UserSimpleResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
