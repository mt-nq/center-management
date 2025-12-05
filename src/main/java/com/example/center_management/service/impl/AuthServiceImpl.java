package com.example.center_management.service.impl;

import com.example.center_management.domain.entity.Student;
import com.example.center_management.domain.entity.User;
import com.example.center_management.domain.enums.Role;
import com.example.center_management.dto.auth.AuthLoginRequest;
import com.example.center_management.dto.auth.StudentRegisterRequest;
import com.example.center_management.dto.response.AuthResponse;
import com.example.center_management.dto.response.UserSimpleResponse;
import com.example.center_management.repository.UserRepository;
import com.example.center_management.security.jwt.JwtService;
import com.example.center_management.service.AuthService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.center_management.service.StudentService;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StudentService studentService;


    @Override
    public AuthResponse registerStudent(StudentRegisterRequest request) {
        // 1. Tạo user mới với role STUDENT
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(Role.STUDENT)
                .build();

        userRepository.save(user);

        // 2. Tự động tạo Student gắn với User này
        Student student = studentService.createForUser(user, request);

        // 3. Gen token cho user
        String token = jwtService.generateToken(user.getUsername());

        UserSimpleResponse userResponse = toUserSimpleResponse(user);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }



    @Override
    public AuthResponse login(AuthLoginRequest request) {
        // Xác thực username/password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();

        // Dùng username để gen token
        String token = jwtService.generateToken(user.getUsername());

        UserSimpleResponse userResponse = toUserSimpleResponse(user);

    Long studentId = null;
    if (user.getRole() == Role.STUDENT) {
       studentId = studentService.findByUserId(user.getId())
               .map(Student::getId)
               .orElse(null);
    }

    return AuthResponse.builder()
            .token(token)
            .user(userResponse)
            .studentId(studentId)
            .build();
        }

    // ==== MAP ENTITY -> DTO ĐƠN GIẢN CHO FE =====
    private UserSimpleResponse toUserSimpleResponse(User user) {
        // Điều chỉnh các field cho đúng với UserSimpleResponse của bạn
        return UserSimpleResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                // Nếu UserSimpleResponse không có role thì xoá dòng dưới
                .role(user.getRole().name())
                // ĐÃ BỎ .email() vì DTO của bạn không có field này
                .build();
    }
    
}
