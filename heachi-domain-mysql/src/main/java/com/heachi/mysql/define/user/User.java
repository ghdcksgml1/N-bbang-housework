package com.heachi.mysql.define.user;

import com.heachi.mysql.define.BaseEntity;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.constant.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "USERS")
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String platformId;

    @Enumerated(EnumType.STRING)
    private UserPlatformType platformType;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String name;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    private String profileImageUrl;

    public User updateProfile(String name, String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        this.name = name;

        return this;
    }

    @Builder
    private User(String platformId, UserPlatformType platformType, UserRole role, String name, String email, String phoneNumber, String profileImageUrl) {
        this.platformId = platformId;
        this.platformType = platformType;
        this.role = role;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
    }

    // Spring Security UserDetails Area

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return platformId;
    }

    @Override
    public String getUsername() {
        return email;
    }

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
        return true;
    }
}
