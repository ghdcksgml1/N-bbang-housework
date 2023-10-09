package com.heachi.mysql.define.group.info;

import com.heachi.mysql.define.BaseEntity;
import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import com.heachi.mysql.define.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "GROUP_INFO")
public class GroupInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_INFO_ID")
    private Long id;            // 아이디

    @OneToMany(mappedBy = "groupInfo")   // 집안일 리스트
    private List<HouseworkTodo> houseworkTodoList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;          // 그룹장 아이디

    @Column(name = "BG_COLOR", nullable = false)
    private String bgColor;     // 그룹 배경색

    @Column(name = "COLOR_CODE", nullable = false)
    private String colorCode;   // 그룹 색상코드

    @Column(name = "GRADIENT", nullable = false)
    private String gradient;    // 그룹의 css 속성

    @Column(name = "NAME", nullable = false)
    private String name;        // 그룹 이름

    @Column(name = "INFO", length = 512)
    private String info;        // 그룹 소개

    @Column(name = "JOIN_CODE")
    private String joinCode;    // 그룹 가입코드

    @Builder
    public GroupInfo(User user, String bgColor, String colorCode, String gradient, String name, String info, String joinCode) {
        this.user = user;
        this.bgColor = bgColor;
        this.colorCode = colorCode;
        this.gradient = gradient;
        this.name = name;
        this.info = info;
        this.joinCode = joinCode;
    }
}
