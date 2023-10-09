package com.heachi.mysql.define.group.background;

import com.heachi.mysql.define.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "GROUP_BG")
public class GroupBg extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_BG_ID")
    private Long id;            // GROUP의 배경색 아이디

    @Column(name = "BG_COLOR", nullable = false)
    private String bgColor;     // 그룹의 배경색

    @Column(name = "COLOR_CODE", nullable = false)
    private String colorCode;   // 그룹의 색상 코드

    @Column(name = "GRADIENT", nullable = false)
    private String gradient;    // 그룹의 CSS 속성

    @Builder
    private GroupBg(String bgColor, String colorCode, String gradient) {
        this.bgColor = bgColor;
        this.colorCode = colorCode;
        this.gradient = gradient;
    }
}