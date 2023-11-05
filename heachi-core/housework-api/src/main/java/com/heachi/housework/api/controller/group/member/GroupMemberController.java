package com.heachi.housework.api.controller.group.member;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.group.member.GroupMemberException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.housework.api.controller.group.member.response.GroupMemberListResponse;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.housework.api.service.group.member.GroupMemberService;
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
    private final GroupMemberService groupMemberService;

    @GetMapping("/member/{groupId}")
    public JsonResult<?> groupMemberList(@RequestHeader(name = "Authorization") String authorization,
                                         @PathVariable(name = "groupId") Long groupId) {
        // 유저 인증
        UserInfoResponse userInfoResponse = authExternalService.userAuthenticate(authorization);

        List<GroupMemberListResponse> groupMemberList = groupMemberService.selctGroupMember(userInfoResponse.getEmail(), groupId);

        return JsonResult.successOf(groupMemberList);
    }
}
