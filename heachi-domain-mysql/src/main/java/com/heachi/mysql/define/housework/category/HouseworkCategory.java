package com.heachi.mysql.define.housework.category;

import com.heachi.mysql.define.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "HOUSEWORK_CATEGORY")
public class HouseworkCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOUSEWORK_CATEGORY_ID")
    private Long id;        // 집안일 카테고리 아이디

    @Column(name = "NAME", nullable = false)
    private String name;    // 카테고리 이름

    @Builder
    private HouseworkCategory(String name) {
        this.name = name;
    }

}
