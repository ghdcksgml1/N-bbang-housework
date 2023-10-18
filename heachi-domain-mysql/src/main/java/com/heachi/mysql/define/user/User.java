package com.heachi.mysql.define.user;

import com.heachi.mysql.define.BaseEntity;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.constant.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@DynamicInsert // Entity save 시점에 null 값은 배제하고 Insert Query를 날려줌
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "USERS")
@Table(indexes = { @Index(name = "USERS_EMAIL_INDEX", columnList = "EMAIL") })
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;                                            // 아이디

    @OneToMany(mappedBy = "user")
    private List<GroupInfo> groupInfoList = new ArrayList<>();  // 가입된 모든 그룹 리스트

    @Column(name = "PLATFORM_ID")
    private String platformId;                                  // 플랫폼 아이디

    @Enumerated(EnumType.STRING)
    @Column(name = "PLATFORM_TYPE")
    @ColumnDefault(value = "'KAKAO'")
    private UserPlatformType platformType;                      // 플랫폼 타입

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    @ColumnDefault(value = "'UNAUTH'")
    private UserRole role;                                      // 역할

    @Column(name = "NAME")
    private String name;                                        // 이름

    @Column(name = "EMAIL", unique = true)
    private String email;                                       // 이메일

    @Column(name = "PHONE_NUMBER", unique = true)
    private String phoneNumber;                                 // 전화번호

    @Column(name = "PROFILE_IMAGE_URL")
    private String profileImageUrl;                             // 프로필 사진

    @Column(name = "PUSH_ALARM_YN")
    private boolean pushAlarmYn = false;                        // 알림 수신 동의

    public void updateProfile(String name, String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        this.name = name;
    }

    public void updateRegister(UserRole role, String phoneNumber) {
        this.role = role;
        this.phoneNumber = phoneNumber;
    }

    @Builder
    private User(List<GroupInfo> groupInfoList, String platformId, UserPlatformType platformType, UserRole role,
                 String name, String email, String phoneNumber, String profileImageUrl, boolean pushAlarmYn) {
        this.groupInfoList = groupInfoList;
        this.platformId = platformId;
        this.platformType = platformType;
        this.role = role;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.pushAlarmYn = pushAlarmYn;
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
