package com.heachi.housework.api.service.group.info;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.group.info.GroupInfoException;
import com.heachi.admin.common.exception.group.member.GroupMemberException;
import com.heachi.housework.api.controller.group.info.request.GroupInfoRegisterRequest;
import com.heachi.housework.api.controller.group.info.request.GroupInfoRegisterRequestStatusEnum;
import com.heachi.housework.api.service.group.info.request.GroupInfoUpdateServiceRequest;
import com.heachi.housework.api.service.group.info.response.GroupInfoUpdatePageResponse;
import com.heachi.housework.api.service.group.info.response.GroupInfoUserGroupServiceResponse;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
import com.heachi.mysql.define.group.info.repository.response.GroupInfoUserGroupResponse;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.housework.member.repository.HouseworkMemberRepository;
import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus;
import com.heachi.mysql.define.housework.todo.repository.HouseworkTodoRepository;
import com.heachi.mysql.define.housework.todo.repository.response.HouseworkTodoCount;
import com.heachi.admin.common.exception.user.UserException;
import com.heachi.housework.api.service.group.info.request.GroupInfoCreateServiceRequest;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.constant.GroupMemberRole;
import com.heachi.mysql.define.group.member.constant.GroupMemberStatus;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupInfoService {
    private final HouseworkMemberRepository houseworkMemberRepository;
    private final HouseworkInfoRepository houseworkInfoRepository;

    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupInfoRepository groupInfoRepository;
    private final HouseworkTodoRepository houseworkTodoRepository;

    @Transactional
    public void createGroupInfo(GroupInfoCreateServiceRequest request) {
        // User 가져오기
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
            log.warn(">>>> {} : {}", ExceptionMessage.USER_NOT_FOUND.getText(), request.getEmail());

            throw new UserException(ExceptionMessage.USER_NOT_FOUND);
        });

        // GroupInfo 생성하기
        GroupInfo groupInfo = groupInfoRepository.save(GroupInfo.builder()
                .user(user) // 생성하는 사람을 자동으로 그룹장으로 임명
                .bgColor(request.getBgColor())
                .colorCode(request.getColorCode())
                .gradient(request.getGradient())
                .name(request.getName())
                .info(request.getInfo())
                .build());

        // GroupMember 생성하기
        groupMemberRepository.save(GroupMember.builder()
                .groupInfo(groupInfo)
                .user(user)
                .role(GroupMemberRole.GROUP_ADMIN)  // 그룹장
                .status(GroupMemberStatus.ACCEPT)   // 그룹원
                .build());
    }

    public List<GroupInfoUserGroupServiceResponse> userGroupInfoList(String email) {

        // 해당 유저가 속한 그룹 가져오기
        List<GroupInfoUserGroupResponse> groupInfoList = groupInfoRepository.findGroupInfoUserGroupResponseListByUserEmail(email);
        if (groupInfoList.isEmpty()) {
            log.warn(">>>> 유저가 가입한 그룹이 존재하지 않습니다.");

            throw new GroupInfoException(ExceptionMessage.GROUP_INFO_NOT_FOUND);
        }

        // 유저속한 그룹에서 그룹 아이디만 추출
        List<Long> groupInfoIdList = groupInfoList.stream()
                .map(GroupInfoUserGroupResponse::getId)
                .collect(Collectors.toList());

        // Map<GROUP_INFO_ID, HouseworkTodoCount> 각 그룹의 HouseworkTodo 가져오기
        Map<Long, HouseworkTodoCount> houseworkTodoCountMap = houseworkTodoRepository.findHouseworkTodoCountByGroupInfoIdList(groupInfoIdList).stream()
                .collect(Collectors.toMap(HouseworkTodoCount::getId, hwTodo -> hwTodo));

        return groupInfoList.stream()
                .map(groupInfo -> {
                    if (houseworkTodoCountMap.containsKey(groupInfo.getId())) {
                        List<HouseworkTodoStatus> statusList = houseworkTodoCountMap.get(groupInfo.getId()).getStatus();
                        int remainTodoListCnt = (int) statusList.stream()
                                .filter(status -> status == HouseworkTodoStatus.HOUSEWORK_TODO_INCOMPLETE)
                                .count();

                        return GroupInfoUserGroupServiceResponse.builder()
                                .id(groupInfo.getId())
                                .name(groupInfo.getName())
                                .groupMembers(
                                        groupInfo.getGroupMembers()
                                )
                                .remainTodoListCnt(remainTodoListCnt)
                                .progressPercent(Math.round((float) (100 * (statusList.size() - remainTodoListCnt)) / statusList.size()))
                                .build();
                    } else {
                        return GroupInfoUserGroupServiceResponse.builder()
                                .id(groupInfo.getId())
                                .name(groupInfo.getName())
                                .groupMembers(
                                        groupInfo.getGroupMembers()
                                )
                                .remainTodoListCnt(0)
                                .progressPercent(0)
                                .build();
                    }
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void joinGroupInfo(String email, String joinCode) {
        // email를 통해 User 가져오기
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.warn(">>>> joinGroupInfo에서 유저를 찾지 못했습니다 : [{}]", email);

            throw new UserException(ExceptionMessage.USER_NOT_FOUND);
        });

        // joinCode를 통해 GroupInfo 조회하기
        GroupInfo groupInfo = groupInfoRepository.findByJoinCode(joinCode).orElseThrow(() -> {
            log.warn(">>>> joinGroupInfo에서 해당 joinCode를 가진 그룹이 존재하지 않습니다 : [{}]", joinCode);

            throw new GroupInfoException(ExceptionMessage.GROUP_INFO_NOT_FOUND);
        });

        // GroupInfo를 통해 해당 사용자가 GroupMember로 존재하는지 확인
        Optional<GroupMember> findGroupMember = groupMemberRepository.findByUserAndGroupInfo(user, groupInfo);

        // 만약, 존재한다면, WAITING, ACCEPT인지 확인하고, 맞다면 아무런 동작도 하지않음
        if (findGroupMember.isPresent()) {
            GroupMember groupMember = findGroupMember.get();

            // WITHDRAW 인데 신청하는 경우 -> 재가입으로 간주
            if (groupMember.getStatus() == GroupMemberStatus.WITHDRAW) {
                groupMember.rejoinGroup();
            }
        } else {
            // 아닐경우 GroupMember로 추가해주기
            groupMemberRepository.save(GroupMember.builder()
                    .groupInfo(groupInfo)
                    .user(user)
                    .role(GroupMemberRole.GROUP_MEMBER)
                    .status(GroupMemberStatus.WAITING)
                    .build());
        }

    }

    @Transactional
    public void joinRequestHandler(String adminEmail, GroupInfoRegisterRequest request) {
        // 그룹장의 email과 GroupId로 GROUP_MEMBER 조회
        GroupMember adminGroupMember = groupMemberRepository.findGroupMemberByUserEmailAndGroupInfoId(adminEmail, request.getGroupId()).orElseThrow(() -> {
            log.warn(">>>> 해당 그룹의 구성원이 아닙니다. : [{}]", adminEmail);

            throw new GroupMemberException(ExceptionMessage.GROUP_MEMBER_NOT_FOUND);
        });

        // role이 GROUP_ADMIN(그룹장)인지 확인
        if (adminGroupMember.getRole() != GroupMemberRole.GROUP_ADMIN) {
            log.warn(">>>> 해당 그룹의 그룹장이 아닙니다. : [role: {}]", adminGroupMember.getRole());

            throw new GroupMemberException(ExceptionMessage.GROUP_MEMBER_ROLE_NOT_ADMIN);
        }

        // 그룹 가입 요청자 조회
        GroupMember requestGroupMember = groupMemberRepository.findGroupMemberByGroupMemberIdAndGroupInfoId(request.getGroupMemberId(), request.getGroupId()).orElseThrow(() -> {
            log.warn(">>>> Group Member Not Found : [{}]", request.getGroupMemberId());

            throw new GroupMemberException(ExceptionMessage.GROUP_MEMBER_NOT_FOUND);
        });

        // 그룹 가입 요청자 상태 확인: WAITING 상태가 아닐경우 예외처리
        if (requestGroupMember.getStatus() != GroupMemberStatus.WAITING) {
            log.warn(">>>> Group Member's Status Is Not WAITING : [{}]", requestGroupMember.getStatus());

            throw new GroupMemberException(ExceptionMessage.GROUP_MEMBER_STATUS_NOT_WAITING);
        }

        // status에 따른 그룹 가입 요청 핸들링
        if (request.getStatus().equals(GroupInfoRegisterRequestStatusEnum.ACCEPT)) {
            requestGroupMember.acceptJoin();
        } else {
            requestGroupMember.refuseJoin();
        }
    }

    public GroupInfoUpdatePageResponse updateGroupInfoPage(Long groupId) {
        GroupInfo group = groupInfoRepository.findById(groupId).orElseThrow(() -> {
            log.warn(">>>> 그룹 정보를 찾을 수 없습니다.");

            throw new GroupInfoException(ExceptionMessage.GROUP_INFO_NOT_FOUND);
        });

        return GroupInfoUpdatePageResponse.of(group);

    }

    @Transactional
    public void updateGroupInfo(GroupInfoUpdateServiceRequest request) {
        Long groupId = request.getGroupId();

        GroupInfo group = groupInfoRepository.findById(groupId).orElseThrow(() -> {
            log.warn(">>>> 그룹 정보를 찾을 수 없습니다.");

            throw new GroupInfoException(ExceptionMessage.GROUP_INFO_NOT_FOUND);
        });

        group.updateGroupInfo(request.getBgColor(),
                request.getColorCode(),
                request.getGradient(),
                request.getName(),
                request.getInfo());
    }

    @Transactional
    public void deleteGroupInfo(Long groupId) {
        GroupInfo group = groupInfoRepository.findGroupInfoByGroupIdJoinFetchUser(groupId).orElseThrow(() -> {
            log.warn(">>>> 그룹 정보를 찾을 수 없습니다.");

            throw new GroupInfoException(ExceptionMessage.GROUP_INFO_NOT_FOUND);
        });

        // User의 GroupInfoList에서 삭제
        group.getUser().deleteGroupInfo(group);

        // 연관된 HouseworkTodo들 삭제
        houseworkTodoRepository.deleteByGroupInfo(group);

        // 해당 그룹의 HouseworkInfo 리스트 조회
        List<HouseworkInfo> houseworkInfoList = houseworkInfoRepository.findHouseworkInfoByGroupInfoId(groupId);

        // 각 HouseworkInfo의 HouseworkMember 리스트 삭제
        houseworkMemberRepository.deleteByHouseworkInfoList(houseworkInfoList);

        // HouseworkInfo 리스트 삭제
        houseworkInfoRepository.deleteHouseworkInfoByHouseworkInfoList(houseworkInfoList);

        // 연관된 GroupMember들 삭제
        groupMemberRepository.deleteByGroupInfo(group);

        // GroupInfo 삭제
        groupInfoRepository.deleteById(groupId);

    }
}
