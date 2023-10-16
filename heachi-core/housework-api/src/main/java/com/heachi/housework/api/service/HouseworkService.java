package com.heachi.housework.api.service;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.auth.AuthException;
import com.heachi.admin.common.exception.housework.HouseworkException;
import com.heachi.auth.api.service.token.RefreshTokenService;
import com.heachi.housework.api.controller.response.HouseworkAddResponseDTO;
import com.heachi.housework.api.service.request.HouseworkServiceAddRequestDTO;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.constant.GroupMemberRole;
import com.heachi.mysql.define.group.member.constant.GroupMemberStatus;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import com.heachi.mysql.define.housework.member.repository.HouseworkMemberRepository;
import com.heachi.mysql.define.housework.save.HouseworkSave;
import com.heachi.mysql.define.housework.save.repository.HouseworkSaveRepository;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HouseworkService {
    private final HouseworkSaveRepository houseworkSaveRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final HouseworkInfoRepository houseworkInfoRepository;
    private final RefreshTokenService refreshTokenService;
    private final HouseworkMemberRepository houseworkMemberRepository;

    public HouseworkAddResponseDTO houseworkAdd(String token, HouseworkServiceAddRequestDTO request) {
        try {
            // Auth 서버에 Refresh 토큰을 전송해 레디스에서 사용자 Email 정보 조회
            String requestEmail = refreshTokenService.getEmailWithRTK(token);

//            // 요청자 정보 조회
//            User requestUser = userRepository.findByEmail(requestEmail).orElseThrow(() -> {
//                log.warn(">>>> User Not Exist : {}", ExceptionMessage.AUTH_INVALID_REGISTER.getText());
//                throw new AuthException(ExceptionMessage.AUTH_INVALID_REGISTER);
//            });

            // 요청자가 그룹 구성원인지 조회
            Long groupInfoId = request.getGroupMembers().get(0).getGroupInfo().getId();
            GroupMember requestMember = groupMemberRepository.findByUserEmailAndGroupInfoId(requestEmail, groupInfoId).orElseThrow(() -> {
                log.warn(">>>> Group Member Not Exist : {}", ExceptionMessage.GROUP_MEMBER_NOT_FOUND.getText());
                throw new AuthException(ExceptionMessage.GROUP_MEMBER_NOT_FOUND);
            });

            // 집안일 추가 권한이 있는 구성원인지 확인
            // status : ACCEPT  / role: GROUP_ADMIN
            if (!(requestMember.getRole() == GroupMemberRole.GROUP_ADMIN
                    && requestMember.getStatus() == GroupMemberStatus.ACCEPT)) {

                log.warn(">>>> Housework Add Permission Denied : {}", ExceptionMessage.HOUSEWORK_ADD_PERMISSION_DENIED);
                throw new AuthException(ExceptionMessage.HOUSEWORK_ADD_PERMISSION_DENIED);
            }


            // HOUSEWORK_INFO 생성
            HouseworkInfo houseworkInfo = HouseworkInfo.builder()
                    .houseworkCategory(request.getHouseworkCategory())
                    .title(request.getTitle())
                    .detail(request.getDetail())
                    .type(request.getType())
                    .dayDate(request.getDayDate())
                    .weekDate(request.getWeekDate())
                    .monthDate(request.getMonthDate())
                    .endTime(request.getEndTime())
                    .build();

            // 담당자 지정 - HOUSEWORK_MEMBER 생성
            for (GroupMember gm : request.getGroupMembers()) {
                HouseworkMember hm = HouseworkMember.builder()
                        .groupMember(gm)
                        .houseworkInfo(houseworkInfo)
                        .build();
                // HOUSEWORK_MEMBER 저장
                houseworkMemberRepository.save(hm);
            }

            // HOUSEWORK_INFO 저장
            HouseworkInfo savedHousework = houseworkInfoRepository.save(houseworkInfo);

            // HOUSEWORK_SAVE 저장
            HouseworkSave houseworkSave = HouseworkSave.builder()
                    .groupInfo(requestMember.getGroupInfo())
                    .name(savedHousework.getTitle())
                    .build();
            houseworkSaveRepository.save(houseworkSave);

            // HouseworkAddResponseDTO 반환
            return HouseworkAddResponseDTO.builder()
                    .houseworkMembers(savedHousework.getHouseworkMembers())
                    .houseworkCategory(savedHousework.getHouseworkCategory())
                    .title(savedHousework.getTitle())
                    .detail(savedHousework.getDetail())
                    .type(savedHousework.getType())
                    .dayDate(savedHousework.getDayDate())
                    .weekDate(savedHousework.getWeekDate())
                    .monthDate(savedHousework.getMonthDate())
                    .endTime(savedHousework.getEndTime())
                    .build();

        } catch (RuntimeException e) {
            log.warn(">>>> Housework Add Fail : {}", ExceptionMessage.HOUSEWORK_ADD_FAIL);
            throw new HouseworkException(ExceptionMessage.HOUSEWORK_ADD_FAIL);
        }
    }
}
