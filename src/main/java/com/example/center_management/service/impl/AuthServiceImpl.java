        package com.example.center_management.service.impl;

        import com.example.center_management.domain.entity.Student;
        import com.example.center_management.domain.entity.User;
        import com.example.center_management.domain.enums.Role;
        import com.example.center_management.dto.auth.AuthLoginRequest;
        import com.example.center_management.dto.auth.AuthRegisterRequest;
        import com.example.center_management.dto.response.AuthResponse;
        import com.example.center_management.dto.response.UserSimpleResponse;
        import com.example.center_management.repository.UserRepository;
        import com.example.center_management.security.jwt.JwtService;
        import com.example.center_management.service.AuthService;

        import lombok.RequiredArgsConstructor;

        import java.time.LocalDateTime;

        import org.springframework.dao.DataIntegrityViolationException;
        import org.springframework.http.HttpStatus;
        import org.springframework.security.authentication.AuthenticationManager;
        import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
        import org.springframework.security.core.Authentication;
        import org.springframework.security.crypto.password.PasswordEncoder;
        import org.springframework.stereotype.Service;
        import org.springframework.web.server.ResponseStatusException;

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
        public AuthResponse registerStudent(AuthRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email đã tồn tại");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
                throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }
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

        try {
                userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Username, email hoặc số điện thoại đã tồn tại");
        }

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
                Authentication authentication;
        try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );
        } catch (org.springframework.security.core.AuthenticationException ex) {
                throw new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.UNAUTHORIZED,
                        "Tài khoản hoặc mật khẩu sai"
                );
        }

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
                .build();
        }

        // ==== MAP ENTITY -> DTO ĐƠN GIẢN CHO FE ===z==
        private UserSimpleResponse toUserSimpleResponse(User user) {
        return UserSimpleResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
        }

        }
