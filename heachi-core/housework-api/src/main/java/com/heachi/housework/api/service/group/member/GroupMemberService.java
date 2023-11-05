package com.heachi.housework.api.service.group.member;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.group.member.GroupMemberException;
import com.heachi.housework.api.controller.group.member.response.GroupMemberListResponse;
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

    public List<GroupMemberListResponse> selctGroupMember(String email, Long groupId) {
        // 권한 체크 - 해당 그룹의 그룹원인지 확인
        boolean isGroupMember = groupMemberRepository.existsGroupMemberByUserEmailAndGroupInfoId(email, groupId);
        if (!isGroupMember) {
            log.warn(">>>> Is Not Group Member : {}", ExceptionMessage.GROUP_MEMBER_NOT_FOUND.getText());
            throw new GroupMemberException(ExceptionMessage.GROUP_MEMBER_NOT_FOUND);
        }

        // groupId로 상태가 ACCEPT인 모든 그룹 멤버 조회 후 GroupMemberListResponse 리스트로 변환해서 반환
        return groupMemberRepository.findGroupMemberByGroupId(groupId)
                .stream()
                .map(gm -> GroupMemberListResponse.builder()
                        .groupMemberId(gm.getId())
                        .username(gm.getUser().getName())
                        .build())
                .toList();
    }
}
