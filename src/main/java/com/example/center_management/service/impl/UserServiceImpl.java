package com.example.center_management.service.impl;

import com.example.center_management.domain.entity.User;
import com.example.center_management.dto.response.UserSimpleResponse;
import com.example.center_management.exception.ResourceNotFoundException;
import com.example.center_management.repository.UserRepository;
import com.example.center_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserSimpleResponse getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserSimpleResponse res = new UserSimpleResponse();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setFullName(user.getFullName());
        res.setRole(user.getRole());
        return res;
    }
}
