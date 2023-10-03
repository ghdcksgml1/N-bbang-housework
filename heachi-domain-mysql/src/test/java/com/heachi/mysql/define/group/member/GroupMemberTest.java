package com.heachi.mysql.define.group.member;

import com.heachi.mysql.TestConfig;
import com.heachi.mysql.define.group.member.constant.GroupMemberRole;
import com.heachi.mysql.define.group.member.constant.GroupMemberStatus;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GroupMemberTest extends TestConfig {

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @AfterEach
    void tearDown() {
        groupMemberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("groupMember를 생성하여 저장할때, @ColumnDefault를 설정한 컬럼은 Default값이 들어가야한다.")
    void groupMemberColumnDefaultTest() {
        // given
        GroupMember groupMember = GroupMember.builder()
                .build();
        GroupMember savedMember = groupMemberRepository.save(groupMember);

        // when
        GroupMember findMember = groupMemberRepository.findById(savedMember.getId()).get();

        // then
        System.out.println("findMember = " + findMember);
        assertThat(findMember.getRole()).isEqualTo(GroupMemberRole.GROUP_MEMBER);
        assertThat(findMember.getStatus()).isEqualTo(GroupMemberStatus.WAITING);
    }
}