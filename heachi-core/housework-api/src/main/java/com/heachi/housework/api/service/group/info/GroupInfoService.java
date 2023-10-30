package com.heachi.housework.api.service.group.info;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.group.info.GroupInfoException;
import com.heachi.housework.api.service.group.info.response.GroupInfoUserGroupServiceResponse;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
import com.heachi.mysql.define.group.info.repository.response.GroupInfoUserGroupResponse;
import com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus;
import com.heachi.mysql.define.housework.todo.repository.HouseworkTodoRepository;
import com.heachi.mysql.define.housework.todo.repository.response.HouseworkTodoCount;
import com.heachi.admin.common.exception.user.UserException;
import com.heachi.housework.api.service.group.info.request.GroupInfoCreateServiceRequest;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupInfoService {

    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupInfoRepository groupInfoRepository;
    private final HouseworkTodoRepository houseworkTodoRepository;

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
}