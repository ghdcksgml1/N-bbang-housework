package com.heachi.mysql.define.housework.info;

import com.heachi.mysql.define.BaseEntity;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "HOUSEWORK_INFO")
public class HouseworkInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOUSEWORK_INFO_ID")
    private Long id;                                                    // 집안일 정보 아이디

    @OneToMany(mappedBy = "houseworkInfo")
    private List<HouseworkMember> houseworkMembers = new ArrayList<>(); // 집안일 담당자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOUSEWORK_CATEGORY_ID")
    private HouseworkCategory houseworkCategory;                        // 집안일 카테고리

    @Column(name = "TITLE", nullable = false)
    private String title;                                               // 집안일 제목

    @Column(name = "DETAIL")
    private String detail;                                              // 집안일 내용

    @Builder
    private HouseworkInfo(List<HouseworkMember> houseworkMembers, HouseworkCategory houseworkCategory, String title, String detail) {
        this.houseworkMembers = houseworkMembers;
        this.houseworkCategory = houseworkCategory;
        this.title = title;
        this.detail = detail;
    }
}
