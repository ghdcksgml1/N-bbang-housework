package com.heachi.housework.api.service.housework.info;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.group.member.GroupMemberException;
import com.heachi.admin.common.exception.housework.HouseworkException;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoCreateServiceRequest;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.category.repository.HouseworkCategoryRepository;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import com.heachi.mysql.define.housework.member.repository.HouseworkMemberRepository;
import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus;
import com.heachi.mysql.define.housework.todo.repository.HouseworkTodoRepository;
import com.heachi.redis.define.housework.todo.TodoList;
import com.heachi.redis.define.housework.todo.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HouseworkInfoService {
    private final HouseworkInfoRepository houseworkInfoRepository;
    private final HouseworkMemberRepository houseworkMemberRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final HouseworkCategoryRepository houseworkCategoryRepository;
    private final HouseworkTodoRepository houseworkTodoRepository;

    private final TodoListRepository todoListRepository;

    @Transactional(readOnly = false)
    public void createHouseworkInfo(HouseworkInfoCreateServiceRequest request) {
        try {
            // HOUSEWORK_CATEGORY 조회
            HouseworkCategory category = houseworkCategoryRepository.findById(request.getHouseworkCategoryId()).orElseThrow(() -> {
                log.warn(">>>> HouseworkCategory Not Found : {}", ExceptionMessage.HOUSEWORK_CATEGORY_NOT_FOUND.getText());

                throw new HouseworkException(ExceptionMessage.HOUSEWORK_CATEGORY_NOT_FOUND);
            });

            // 담당자 지정 - HOUSEWORK_MEMBER 생성
            List<GroupMember> groupMemberList = groupMemberRepository.findGroupMemberListByGroupMemberIdList(request.getGroupMemberIdList());

            // 한 건이라도 조회 실패시 예외 발생
            if (groupMemberList.size() != request.getGroupMemberIdList().size()) {
                log.warn(">>>> GourpMember Not Found : {}", ExceptionMessage.GROUP_MEMBER_NOT_FOUND.getText());

                throw new GroupMemberException(ExceptionMessage.GROUP_MEMBER_NOT_FOUND);
            }

            // 딱 한번 하는 집안일의 경우 HOUSEWORK_INFO를 생성해줄 필요 없이, 바로 HOUSEWORK_TODO로 생성
            if (request.getType() == HouseworkPeriodType.HOUSEWORK_PERIOD_DAY) {

                // HouseworkTodo 생성
                houseworkTodoRepository.save(HouseworkTodo.builder()
                        .houseworkInfo(null) // 단건은 HouseworkInfo가 존재하지 않는다.
                        .groupInfo(groupMemberList.isEmpty() ? null : groupMemberList.get(0).getGroupInfo())
                        .houseworkMember(groupMemberList.stream()
                                .map(gm -> gm.getId().toString())
                                .collect(Collectors.joining(",")))
                        .category(category.getName())
                        .title(request.getTitle())
                        .detail(request.getDetail())
                        .status(HouseworkTodoStatus.HOUSEWORK_TODO_INCOMPLETE)
                        .date(request.getDayDate())
                        .endTime(request.getEndTime())
                        .build());

                // TODO: 해당 HouseworkTodo에 맞는 groupInfoId와 Date가 캐싱되어있다면, dirtyBit를 true로 바꿔줘야함.

                return ;
            }

            // HOUSEWORK_INFO 저장
            HouseworkInfo houseworkInfo = houseworkInfoRepository.save(HouseworkInfo.builder()
                    .houseworkCategory(category)
                    .title(request.getTitle())
                    .detail(request.getDetail())
                    .type(request.getType())
                    .dayDate(request.getDayDate())
                    .weekDate(request.getWeekDate())
                    .monthDate(request.getMonthDate())
                    .endTime(request.getEndTime())
                    .build());
            log.info(">>>> HouseworkInfo Create: {}", houseworkInfo.getId());

            groupMemberList.stream()
                    .map(gm -> HouseworkMember.builder()
                            .groupMember(gm)
                            .houseworkInfo(houseworkInfo)
                            .build())
                    // HOUSEWORK_MEMBER 저장
                    .forEach(houseworkMemberRepository::save);

        } catch (RuntimeException e) {
            log.warn(">>>> Housework Add Fail : {}", e.getMessage());

            throw e;
        }
    }
}