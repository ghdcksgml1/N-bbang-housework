package com.heachi.housework.api.service;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.auth.AuthException;
import com.heachi.admin.common.exception.group.member.GroupMemberException;
import com.heachi.admin.common.exception.housework.HouseworkException;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.housework.api.controller.response.HouseworkAddResponseDTO;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.housework.api.service.request.HouseworkServiceAddRequestDTO;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
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
    private final UserRepository userRepository;
    private final GroupInfoRepository groupInfoRepository;
    private final HouseworkSaveRepository houseworkSaveRepository;
    private final HouseworkInfoRepository houseworkInfoRepository;
    private final HouseworkMemberRepository houseworkMemberRepository;
    private final AuthExternalService authExternalService;

    @Transactional
    public HouseworkAddResponseDTO houseworkAdd(String token, HouseworkServiceAddRequestDTO request) {
        try {
            // Auth 서버에 외부 요청을 보내 유저 정보 조회
//            UserInfoResponse userResponse = authExternalService.userAuthenticate(token);

            // GroupInfo Id 추출
            Long groupInfoId = request.getGroupMembers().get(0).getGroupInfo().getId();
            
            // 요청자가 그룹 구성원인지 조회 - ACCEPT 상태인지까지 확인
            UserInfoResponse userInfoResponse = authExternalService.userAuthenticateAndGroupMatch(token, groupInfoId);

            // 집안일 추가 권한이 있는 구성원인지 확인
            // 그룹장인지 확인. role: GROUP_ADMIN
            if (!(userInfoResponse.getRole().equals(GroupMemberRole.GROUP_ADMIN.name()))) {

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

            // 그룹장 정보 조회
            User findGroupAdmin = userRepository.findByEmail(userInfoResponse.getEmail()).orElseThrow(() -> {
                log.warn(">>>> User Not Found : {}", ExceptionMessage.AUTH_NOT_FOUND);
                throw new AuthException(ExceptionMessage.AUTH_NOT_FOUND);
            });

            // 그룹장 정보로 그룹 조회
            GroupInfo findGroupInfo = groupInfoRepository.findByUser(findGroupAdmin).orElseThrow(() -> {
                log.warn(">>>> Group Not Found : {}", ExceptionMessage.GROUP_NOT_FOUND);
                throw new AuthException(ExceptionMessage.GROUP_NOT_FOUND);
            });

            // HOUSEWORK_SAVE 저장
            HouseworkSave houseworkSave = HouseworkSave.builder()
                    .groupInfo(findGroupInfo)
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
//        catch (AuthException e) {
//            log.warn(">>>> Authentication Fail : {}", ExceptionMessage.AUTH_SERVER_NOT_RESPOND);
//            throw new AuthException(ExceptionMessage.AUTH_SERVER_NOT_RESPOND);
//        } catch (GroupMemberException e) {
//            log.warn(">>>> Not Found Group Member: {}", ExceptionMessage.GROUP_MEMBER_NOT_FOUND);
//            throw new GroupMemberException(ExceptionMessage.GROUP_MEMBER_NOT_FOUND);
//        }
    }
}