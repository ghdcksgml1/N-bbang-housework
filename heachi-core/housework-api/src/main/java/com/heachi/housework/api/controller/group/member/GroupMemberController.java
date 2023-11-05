package com.heachi.housework.api.controller.group.member;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.group.member.GroupMemberException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.housework.api.controller.group.member.response.GroupMemberListResponse;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupMemberController {
    private final AuthExternalService authExternalService;
    private final GroupMemberRepository groupMemberRepository;

    @GetMapping("/{groupId}/members")
    public JsonResult<?> groupMemberList(@RequestHeader(name = "Authorization") String authorization,
                                         @PathVariable(name = "groupId") Long groupId) {
        // 유저 인증
        UserInfoResponse userInfoResponse = authExternalService.userAuthenticate(authorization);

        // 권한 체크 - 해당 그룹의 그룹원인지 확인
        boolean isGroupMember = groupMemberRepository.existsGroupMemberByUserEmailAndGroupInfoId(userInfoResponse.getEmail(), groupId);
        if (!isGroupMember) {
            log.warn(">>>> Is Not Group Member : {}", ExceptionMessage.GROUP_MEMBER_NOT_FOUND.getText());
            throw new GroupMemberException(ExceptionMessage.GROUP_MEMBER_NOT_FOUND);
        }

        // groupId로 상태가 ACCEPT인 모든 그룹 멤버 조회 후 GroupMemberListResponse 리스트로 변환
        List<GroupMemberListResponse> groupMemberList = groupMemberRepository.findGroupMemberByGroupId(groupId)
                .stream()
                .map(gm -> GroupMemberListResponse.builder()
                        .groupMemberId(gm.getId())
                        .username(gm.getUser().getName())
                        .build())
                .toList();

        return JsonResult.successOf(groupMemberList);
    }
}
