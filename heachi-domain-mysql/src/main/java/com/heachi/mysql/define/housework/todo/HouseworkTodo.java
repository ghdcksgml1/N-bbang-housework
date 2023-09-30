package com.heachi.mysql.define.housework.todo;

import com.heachi.mysql.define.BaseEntity;
import com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "HOUSEWORK_TODO")
public class HouseworkTodo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOUSEWORK_TODO_ID")
    private Long id;                    // 집안일 할일 아이디

    @Column(name = "HOUSEWORK_MEMBER", nullable = false)
    private String houseworkMember;     // 집안일 담당자 (,)으로 joining

    @Column(name = "CATEGORY", nullable = false)
    private String category;            // 집안일 카테고리

    @Column(name = "TITLE", nullable = false)
    private String title;               // 집안일 제목

    @Column(name = "DETAIL")
    private String detail;              // 집안일 내용

    @Column(name = "INDEX", nullable = false)
    @ColumnDefault(value = "0")
    private Integer index;              // 집안일 인덱스 (사용자가 인덱스를 변경할 수 있음)

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    @ColumnDefault(value = "'HOUSEWORK_TODO_INCOMPLETE'")
    private HouseworkTodoStatus status; // 집안일 진행상황

    @Builder
    private HouseworkTodo(String houseworkMember, String category, String title, String detail, Integer index, HouseworkTodoStatus status) {
        this.houseworkMember = houseworkMember;
        this.category = category;
        this.title = title;
        this.detail = detail;
        this.index = index;
        this.status = status;
    }
}
