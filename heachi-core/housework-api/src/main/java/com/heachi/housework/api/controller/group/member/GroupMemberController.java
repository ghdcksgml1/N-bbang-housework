package com.heachi.housework.api.controller.group.member;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.housework.api.controller.group.member.response.GroupMemberResponse;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.housework.api.service.group.member.GroupMemberService;
import com.heachi.redis.define.housework.todo.TodoList;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/group/member")
public class GroupMemberController {
    private final AuthExternalService authExternalService;
    private final GroupMemberService groupMemberService;

    @ApiResponse(responseCode = "200", description = "Group Member 리스트 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GroupMemberResponse.class))))
    @GetMapping("/{groupId}")
    public JsonResult<?> groupMemberList(@RequestHeader(name = "Authorization") String authorization,
                                         @PathVariable(name = "groupId") Long groupId) {
        // 유저 인증 & 해당 그룹원인지 확인
        authExternalService.userAuthenticateAndGroupMatch(authorization, groupId);

        List<GroupMemberResponse> groupMemberList = groupMemberService.selectGroupMember(groupId);

        return JsonResult.successOf(groupMemberList);
    }
}
