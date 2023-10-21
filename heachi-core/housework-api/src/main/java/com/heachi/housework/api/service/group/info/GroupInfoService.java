package com.heachi.housework.api.service.group.info;

import com.heachi.admin.common.exception.ExceptionMessage;
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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupInfoService {

    private final UserRepository userRepository;
    private final GroupInfoRepository groupInfoRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional(readOnly = false)
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
