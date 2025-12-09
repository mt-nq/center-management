package com.example.center_management.domain.entity;

import com.example.center_management.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username đăng nhập
    @Column(nullable = false, unique = true)
    private String username;

    // password đã mã hoá (BCrypt)
    @Column(nullable = false)
    private String password;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // ====== CÁC FIELD CŨ ĐANG ĐƯỢC DÙNG TRONG SERVICE ======

    // trạng thái hoạt động (để filter ACTIVE/INACTIVE)
    @Column(name = "is_active")
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (isActive == null) {
            isActive = true;
        }
    }
    @Override
    public String getUsername() {
        return this.username;
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ================= UserDetails implementation =================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE_ADMIN / ROLE_STUDENT tuỳ enum Role
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    // getPassword() & getUsername() đã có nhờ Lombok @Getter

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // nếu muốn dùng isActive để disable account:
        return Boolean.TRUE.equals(isActive);
    }
}
