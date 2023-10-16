package com.heachi.housework.api.service.auth;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.auth.AuthException;
import com.heachi.admin.common.exception.group.member.GroupMemberException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.AuthClients;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthExternalService {

    private final AuthClients authClients;
    private final GroupMemberRepository groupMemberRepository;

    // Auth 서버에 인증 요청을 보낸다.
    public UserInfoResponse userAuthenticate(String authorization) {
        JsonResult<UserInfoResponse> jsonResult = authClients.getUserInfo(authorization).block(); // Mono 객체이므로 Block

        if (jsonResult.getResCode() != 200) {
            log.warn(">>>> 유저 인증에 실패했습니다.");

            throw new AuthException(ExceptionMessage.AUTH_SERVER_NOT_RESPOND);
        }

        return jsonResult.getResObj();
    }

    // Auth 서버에 인증 요청을 보낸 후 가져온 정보로 해당 그룹원인지 판별한다.
    public UserInfoResponse userAuthenticateAndGroupMatch(String authorization, Long groupId) {
        JsonResult<UserInfoResponse> jsonResult = authClients.getUserInfo(authorization).block(); // Mono 객체이므로 Block

        if (jsonResult.getResCode() != 200) {
            log.warn(">>>> 유저 인증에 실패했습니다.");

            throw new AuthException(ExceptionMessage.AUTH_SERVER_NOT_RESPOND);
        }

        if (!groupMemberRepository.existsGroupMemberByUserEmailAndGroupInfoId(
                jsonResult.getResObj().getEmail(), groupId)) {
            log.warn(">>>> 해당 유저[{}]는 해당 그룹[{}]의 소속이 아닙니다.", jsonResult.getResObj().getEmail(), groupId);

            throw new GroupMemberException(ExceptionMessage.GROUP_MEMBER_NOT_FOUND);
        }

        return jsonResult.getResObj();
    }
}
