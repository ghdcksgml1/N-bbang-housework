package com.heachi.mysql.define.housework.todo;

import com.heachi.mysql.define.BaseEntity;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "HOUSEWORK_TODO")
public class HouseworkTodo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOUSEWORK_TODO_ID")
    private Long id;                    // 집안일 할일 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOUSEWORK_INFO_ID")
    private HouseworkInfo houseworkInfo;// 집안일 정보 (HOUSEWORK_PERIOD_DAY)가 아닐때만 참조한다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_INFO_ID")
    private GroupInfo groupInfo;        // 그룹 정보

    @Column(name = "HOUSEWORK_MEMBER", nullable = false)
    private String houseworkMember;     // 집안일 담당자 (,)으로 joining

    @Column(name = "CATEGORY", nullable = false)
    private String category;            // 집안일 카테고리

    @Column(name = "TITLE", nullable = false)
    private String title;               // 집안일 제목

    @Column(name = "DETAIL")
    private String detail;              // 집안일 내용

    @Column(name = "IDX")
    @ColumnDefault(value = "0")
    private Integer idx;              // 집안일 인덱스 (사용자가 인덱스를 변경할 수 있음)

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    @ColumnDefault(value = "'HOUSEWORK_TODO_INCOMPLETE'")
    private HouseworkTodoStatus status; // 집안일 진행상황

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE", nullable = false)
    private LocalDate date;                  // 집안일 지정 날짜

    @Column(name = "VERIFICATION_PHOTO_URL")
    private String verificationPhotoURL;    // 다른 사용자가 확인할 수 있는 인증 사진 URL

    @Column(name = "VERIFIER_ID")
    private String verifierId;              // 해당 집안일을 인증해준 사용자

    @Column(name = "VERIFICATION_TIME")
    private LocalDateTime verificationTime; // 인증된 시각

    @Builder
    private HouseworkTodo(HouseworkInfo houseworkInfo, GroupInfo groupInfo, String houseworkMember, String category,
                         String title, String detail, Integer idx, HouseworkTodoStatus status, LocalDate date,
                         String verificationPhotoURL, String verifierId, LocalDateTime verificationTime) {
        this.houseworkInfo = houseworkInfo;
        this.groupInfo = groupInfo;
        this.houseworkMember = houseworkMember;
        this.category = category;
        this.title = title;
        this.detail = detail;
        this.idx = idx;
        this.status = status;
        this.date = date;
        this.verificationPhotoURL = verificationPhotoURL;
        this.verifierId = verifierId;
        this.verificationTime = verificationTime;
    }
}
