package com.heachi.mysql.define.group.info.repository.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GroupInfoGroupMember {
    private String name;            // 멤버 이름
    private String profileImageUrl; // 멤버 프로필 사진

    @Builder
    private GroupInfoGroupMember(String name, String profileImageUrl) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }
}
