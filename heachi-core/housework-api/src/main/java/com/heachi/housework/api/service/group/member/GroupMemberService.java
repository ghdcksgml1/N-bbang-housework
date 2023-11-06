package com.heachi.housework.api.service.group.member;

import com.heachi.housework.api.controller.group.member.response.GroupMemberResponse;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;

    public List<GroupMemberResponse> selctGroupMember(Long groupId) {

        // groupId로 상태가 ACCEPT인 모든 그룹 멤버 조회 후 GroupMemberListResponse 리스트로 변환해서 반환
        return groupMemberRepository.findGroupMemberByGroupId(groupId)
                .stream()
                .map(gm -> GroupMemberResponse.builder()
                        .groupMemberId(gm.getId())
                        .username(gm.getUser().getName())
                        .build())
                .toList();
    }
}
