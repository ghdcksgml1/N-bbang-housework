package com.heachi.housework.api.service.housework.info;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;
import com.heachi.admin.common.exception.group.info.GroupInfoException;
import com.heachi.admin.common.exception.group.member.GroupMemberException;
import com.heachi.admin.common.exception.housework.HouseworkException;
import com.heachi.admin.common.utils.DayOfWeekUtils;
import com.heachi.housework.api.controller.housework.info.request.HouseworkInfoDeleteType;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoCreateServiceRequest;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoDeleteRequest;
import com.heachi.housework.api.service.housework.info.response.HouseworkInfoUpdatePageResponse;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
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
import com.heachi.redis.define.housework.todo.repository.TodoListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HouseworkInfoService {
    private final GroupInfoRepository groupInfoRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final HouseworkInfoRepository houseworkInfoRepository;
    private final HouseworkTodoRepository houseworkTodoRepository;
    private final HouseworkMemberRepository houseworkMemberRepository;
    private final HouseworkCategoryRepository houseworkCategoryRepository;

    private final TodoListRepository todoListRepository;

    @Transactional(readOnly = false)
    public void createHouseworkInfo(HouseworkInfoCreateServiceRequest request) {
        try {
            // HOUSEWORK_CATEGORY 조회
            HouseworkCategory category = houseworkCategoryRepository.findById(request.getHouseworkCategoryId()).orElseThrow(() -> {
                log.warn(">>>> HouseworkCategory Not Found : {}", ExceptionMessage.HOUSEWORK_CATEGORY_NOT_FOUND.getText());

                throw new HouseworkException(ExceptionMessage.HOUSEWORK_CATEGORY_NOT_FOUND);
            });

            // GROUP_INFO 조회
            GroupInfo groupInfo = groupInfoRepository.findById(request.getGroupId()).orElseThrow(() -> {
                log.warn(">>>> GroupInfo Not Found : {}", ExceptionMessage.GROUP_INFO_NOT_FOUND.getText());

                throw new GroupInfoException(ExceptionMessage.GROUP_INFO_NOT_FOUND);
            });

            // 담당자 지정 - HOUSEWORK_MEMBER 생성
            List<GroupMember> groupMemberList = groupMemberRepository.findGroupMemberListByGroupMemberIdList(request.getGroupMemberIdList());

            // 한 건이라도 조회 실패시 예외 발생
            if (groupMemberList.size() != request.getGroupMemberIdList().size()) {
                log.warn(">>>> GroupMember Not Found : {}", ExceptionMessage.GROUP_MEMBER_NOT_FOUND.getText());

                throw new GroupMemberException(ExceptionMessage.GROUP_MEMBER_NOT_FOUND);
            }

            // 딱 한번 하는 집안일의 경우 HOUSEWORK_INFO를 생성해줄 필요 없이, 바로 HOUSEWORK_TODO로 생성
            if (request.getType() == HouseworkPeriodType.HOUSEWORK_PERIOD_DAY) {

                // HouseworkTodo 생성
                houseworkTodoRepository.save(HouseworkTodo.builder()
                        .houseworkInfo(null) // 단건은 HouseworkInfo가 존재하지 않는다.
                        .groupInfo(groupInfo)
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

                // 해당 HouseworkTodo에 맞는 groupInfoId와 Date가 캐싱되어있다면, dirtyBit를 true로 바꿔줘야함.
                todoListRepository.findByGroupInfoIdAndDate(request.getGroupId(), request.getDayDate())
                        // Todo가 캐싱되어 있다면, dirtyBit 체킹
                        .ifPresent(todoList -> {
                            todoList.checkDirtyBit();
                            todoListRepository.save(todoList);
                            log.info(">>>> dirtyBit Checking TodoList id: {}", todoList.getId());
                        });
            } else {

                // HOUSEWORK_INFO 저장
                HouseworkInfo houseworkInfo = houseworkInfoRepository.save(HouseworkInfo.builder()
                        .groupInfo(groupInfo)
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

                // findByGroupInfoId를 통해 해당 그룹의 캐싱된 객체 조회
                todoListRepository.findByGroupInfoId(request.getGroupId()).stream()
                        .filter(todoList -> // PERIOD에 맞는 TodoList 선별
                                switch (request.getType()) {
                                    case HOUSEWORK_PERIOD_DAY -> false;
                                    case HOUSEWORK_PERIOD_EVERYDAY -> true;
                                    case HOUSEWORK_PERIOD_WEEK ->
                                            DayOfWeekUtils.equals(request.getWeekDate(), todoList.getDate());
                                    case HOUSEWORK_PERIOD_MONTH -> Arrays.stream(request.getMonthDate().split(","))
                                            .anyMatch(d -> Integer.parseInt(d) == todoList.getDate().getDayOfMonth());
                                })
                        .forEach(todoList -> { // dirtyBit Checking
                            todoList.checkDirtyBit();
                            todoListRepository.save(todoList);
                            log.info(">>>> dirtyBit Checking TodoList id: {}", todoList.getId());
                        });
            }

        } catch (RuntimeException e) {
            log.warn(">>>> Housework Add Fail : {}", e.getMessage());

            throw e;
        }
    }

    @Transactional(readOnly = false)
    public void deleteHouseworkInfo(HouseworkInfoDeleteRequest request) {
        Long groupId = request.getGroupId();
        LocalDate requestDate = request.getDate();
        Long todoId = request.getTodoId();
        HouseworkInfoDeleteType deleteType = request.getDeleteType();


        try {
            // HouseworkTodo 조회 -> HouseworkInfo도 fetch Join 함께 조회
            HouseworkTodo requestTodo = houseworkTodoRepository.findHouseworkTodoByIdJoinFetchHouseworkInfo(todoId).orElseThrow(() -> {
                log.warn(">>>> HouseworkTodo Not Found : {}", ExceptionMessage.HOUSEWORK_TODO_NOT_FOUND.getText());

                throw new HouseworkException(ExceptionMessage.HOUSEWORK_TODO_NOT_FOUND);
            });

            HouseworkInfo houseworkInfo = requestTodo.getHouseworkInfo();

            // 단건 집안일(HOUSEWORK_PERIOD_DAY)이거나 비단건 집안일이지만 해당 건만 삭제하고 싶을 경우
            if (houseworkInfo == null || deleteType == HouseworkInfoDeleteType.ONE) {
                // HouseworkTodoStatus를 DELETE로 변경
                requestTodo.deleteHouseworkTodo();

                // 요청 날짜의 todoList를 조회 후 dirtyBit 체킹
                todoListRepository.findByGroupInfoIdAndDate(groupId, requestDate)
                        .ifPresent(todoList -> {
                            todoList.checkDirtyBit();
                            todoListRepository.save(todoList);
                            log.info(">>>> dirtyBit Checking TodoList id: {}", todoList.getId());
                        });

                // 비단건 집안일(HOUSEWORK_PERIOD_DAY를 제외한 모든 경우)이고 반복되는 모든 건을 삭제하고 싶을 경우
            } else if (deleteType == HouseworkInfoDeleteType.ALL) {
                // HouseworkInfo를 외래키로 가진 HouseworkMember 삭제
                houseworkMemberRepository.deleteByHouseworkInfo(houseworkInfo);

                // HouseworkInfo를 외래키로 가진 HouseworkTodo의 houseworkInfo 필드값 null로 변환해 관계 해제
                houseworkTodoRepository.updateHouseworkTodoByHouseworkInfoId(houseworkInfo.getId());

                // HouseworkInfo 삭제
                houseworkInfoRepository.deleteById(houseworkInfo.getId());
                log.info(">>>> HouseworkInfo Deleted: {}", houseworkInfo.getId());

                // requestDate 이후의 HouseworkTodo를 HOUSEWORK_TODO_DELETE로 상태 변경
                houseworkTodoRepository.findHouseworkTodoByHouseworkInfo(houseworkInfo.getId()).stream()
                        .filter(todo -> todo.getDate().isAfter(requestDate))
                        .forEach(HouseworkTodo::deleteHouseworkTodo);

                // groupId로 TodoList 조회 후 요청한 requestTodo를 가진 TodoList를 필터링한 후 dirtyBit Checking
                todoListRepository.findByGroupInfoId(groupId).stream()
                        .filter(todoList -> // PERIOD에 맞는 TodoList 선별
                                switch (requestTodo.getHouseworkInfo().getType()) {
                                    case HOUSEWORK_PERIOD_DAY -> false;
                                    case HOUSEWORK_PERIOD_EVERYDAY -> true;
                                    case HOUSEWORK_PERIOD_WEEK ->
                                            DayOfWeekUtils.equals(houseworkInfo.getWeekDate(), todoList.getDate());
                                    case HOUSEWORK_PERIOD_MONTH ->
                                            Arrays.stream(houseworkInfo.getMonthDate().split(","))
                                                    .anyMatch(d -> Integer.parseInt(d) == todoList.getDate().getDayOfMonth());
                                })
                        .forEach(todoList -> { // dirtyBit Checking
                            todoList.checkDirtyBit();
                            todoListRepository.save(todoList);
                            log.info(">>>> dirtyBit Checking TodoList id: {}", todoList.getId());
                        });

                /* redis에서 _IdIn 지원하지 않음 -> 이렇게 한번에 조회하려면 RedisTemplate 사용이 필요해 보입니다.
                todoListRepository.findByTodoList_IdIn(todoList.stream()
                                .map(HouseworkTodo::getId)
                                .collect(Collectors.toList()))
                        .forEach(tList -> { // dirtyBit Checking
                            tList.checkDirtyBit();
                            todoListRepository.save(tList);
                            log.info(">>>> dirtyBit Checking TodoList id: {}", tList.getId());
                        });*/

            }

        } catch (HeachiException e) {
            log.warn(">>>> Housework Delete Fail : {}", e.getMessage());

            throw e;
        }
    }

    public HouseworkInfoUpdatePageResponse updateHouseworkPage(Long todoId) {
        // HouseworkTodo 조회 -> HouseworkInfo도 fetch Join 함께 조회
        HouseworkTodo requestTodo = houseworkTodoRepository.findHouseworkTodoByIdJoinFetchHouseworkInfo(todoId).orElseThrow(() -> {
            log.warn(">>>> HouseworkTodo Not Found : {}", ExceptionMessage.HOUSEWORK_TODO_NOT_FOUND.getText());

            return new HouseworkException(ExceptionMessage.HOUSEWORK_TODO_NOT_FOUND);
        });

        HouseworkInfo houseworkInfo = requestTodo.getHouseworkInfo();

        // 담장자 그룹 멤버 Id 리스트 조회
        List<Long> groupMemberIdList = groupMemberRepository.findGroupMemberListByGroupMemberIdList(Arrays.stream(requestTodo.getHouseworkMember().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList())).stream().map(GroupMember::getId).toList();

        // 단건 집안일의 경우
        if (houseworkInfo == null) {
            // 카테고리 조회
            HouseworkCategory category = houseworkCategoryRepository.findHouseworkCategoryByName(requestTodo.getCategory());

            return HouseworkInfoUpdatePageResponse.of(requestTodo, category, groupMemberIdList);

        } else {    // 비단건 집안일의 경우
            return HouseworkInfoUpdatePageResponse.of(houseworkInfo, groupMemberIdList);
        }
    }
}