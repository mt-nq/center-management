package com.example.center_management.service.impl;

import com.example.center_management.domain.entity.User;
import com.example.center_management.dto.auth.AdminCreateRequest;
import com.example.center_management.dto.auth.AuthLoginRequest;
import com.example.center_management.dto.auth.StudentRegisterRequest;
import com.example.center_management.dto.response.UserSimpleResponse;
import com.example.center_management.exception.BadRequestException;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.UserRepository;
import com.example.center_management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserSimpleResponse registerStudent(StudentRegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role("STUDENT")
                .status("ACTIVE")
                .build();

        userRepository.save(user);

        return UserSimpleResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    @Override
    public UserSimpleResponse login(AuthLoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid username or password");
        }

        if ("INACTIVE".equals(user.getStatus())) {
            throw new BadRequestException("Account has been disabled.");
        }

        return UserSimpleResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    @Override
    public UserSimpleResponse createAdmin(AdminCreateRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role("ADMIN")
                .status("ACTIVE")
                .build();

        userRepository.save(user);

        return UserSimpleResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
