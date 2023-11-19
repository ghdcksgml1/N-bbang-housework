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
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoUpdateServiceRequest;
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
import java.util.Objects;
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
                dirtyBitCheckWithDate(request.getGroupId(), request.getDayDate());
            } else {
                // HouseworkInfo 생성 & 담당자 지정 해주기 - HouseworkMember 생성
                houseworkInfoCreateAndHouseworkMemberCreate(request, groupInfo, category, groupMemberList);

                dirtyBitCheckWithGroupIdAndRequest(request.getGroupId(), request.getType(), request.getWeekDate(), request.getMonthDate());
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
                dirtyBitCheckWithDate(groupId, requestDate);

                // 비단건 집안일(HOUSEWORK_PERIOD_DAY를 제외한 모든 경우)이고 반복되는 모든 건을 삭제하고 싶을 경우
            } else if (deleteType == HouseworkInfoDeleteType.ALL) {
                // houseworkInfo의 외래키, 연관 관계 전부 해제 후 삭제한다. 그리고 현재 시점 이후의 HouseworkTodo를 DELETE로 변경한다.
                houseworkInfoDeleteClean(houseworkInfo);

                dirtyBitCheckWithGroupIdAndRequest(groupId, requestTodo.getHouseworkInfo().getType(), houseworkInfo.getWeekDate(), houseworkInfo.getMonthDate());
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
            throw new HouseworkException(ExceptionMessage.HOUSEWORK_TODO_NOT_FOUND);
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

    public void updateHousework(HouseworkInfoUpdateServiceRequest request) {
        Long groupId = request.getGroupId();
        Long todoId = request.getTodoId();
        LocalDate requestDate = request.getRequestDate();   // 요청 날짜
        LocalDate updateDate = request.getDayDate();        // 수정할 날짜

        // HouseworkTodo 조회 -> HouseworkInfo도 fetch Join 함께 조회
        HouseworkTodo requestTodo = houseworkTodoRepository.findHouseworkTodoByIdJoinFetchHouseworkInfo(todoId).orElseThrow(() -> {
            log.warn(">>>> HouseworkTodo Not Found : {}", ExceptionMessage.HOUSEWORK_TODO_NOT_FOUND.getText());

            throw new HouseworkException(ExceptionMessage.HOUSEWORK_TODO_NOT_FOUND);
        });

        // houseworkInfo
        HouseworkInfo requestInfo = requestTodo.getHouseworkInfo();

        // GroupInfo
        GroupInfo groupInfo = groupInfoRepository.findById(request.getGroupId()).orElseThrow(() -> {
            log.warn(">>>> GroupInfo Not Found : {}", ExceptionMessage.GROUP_INFO_NOT_FOUND.getText());

            throw new GroupInfoException(ExceptionMessage.GROUP_INFO_NOT_FOUND);
        });

        // houseworkCategory
        HouseworkCategory category = houseworkCategoryRepository.findById(request.getHouseworkCategoryId()).orElseThrow(() -> {
            log.warn(">>>> HouseworkTodo Not Found : {}", ExceptionMessage.HOUSEWORK_TODO_NOT_FOUND.getText());
            throw new HouseworkException(ExceptionMessage.HOUSEWORK_TODO_NOT_FOUND);
        });

        // 담당자로 지정할 GroupMember 리스트 조회
        List<GroupMember> groupMemberList = groupMemberRepository.findGroupMemberListByGroupMemberIdList(request.getGroupMemberIdList());

        // GroupMember 한건이라도 조회 실패시 예외 발생
        if (groupMemberList.size() != request.getGroupMemberIdList().size()) {
            log.warn(">>>> GroupMember Not Found : {}", ExceptionMessage.GROUP_MEMBER_NOT_FOUND.getText());

            throw new GroupMemberException(ExceptionMessage.GROUP_MEMBER_NOT_FOUND);
        }

        // 담당자로 지정할 GroupMember의 Id들을 ","으로 연결한 문자열
        String groupMemberIdListString = request.getGroupMemberIdList().stream()
                .map(Objects::toString)
                .collect(Collectors.joining(","));

        // 단건 집안일의 경우
        if (requestInfo == null) {
            // -> 단건 집안일
            if (request.getType() == HouseworkPeriodType.HOUSEWORK_PERIOD_DAY) {
                requestTodo.updateHouseworkTodo(
                        request.getTitle(),
                        request.getDetail(),
                        category.getName(),         // 수정하고 싶은 카테고리
                        groupMemberIdListString,      // 수정하고 싶은 담당자 리스트
                        updateDate,
                        request.getEndTime()
                );

                // 요청 날짜의 todoList dirtyBit 체킹
                dirtyBitCheckWithDate(groupId, requestDate);

                // 수정 날짜의 todoList dirtyBit 체킹
                dirtyBitCheckWithDate(groupId, updateDate);
            }

            // -> 비단건 집안일
            else {
                // HouseworkTodoStatus를 DELETE로 변경
                requestTodo.deleteHouseworkTodo();

                // 요청 날짜의 todoList를 조회 후 dirtyBit 체킹
                dirtyBitCheckWithDate(groupId, requestDate);

                // HouseworkInfo 생성 & 담당자 지정 해주기 - HouseworkMember 생성
                houseworkInfoCreateAndHouseworkMemberCreate(HouseworkInfoCreateServiceRequest.of(request), groupInfo, category, groupMemberList);

                // findByGroupInfoId를 통해 해당 그룹의 캐싱된 객체 조회
                dirtyBitCheckWithGroupIdAndRequest(groupId, request.getType(), request.getWeekDate(), request.getMonthDate());
            }
        }

        // 비단건 집안일의 경우
        else {
            // -> 단건 집안일
            if (request.getType() == HouseworkPeriodType.HOUSEWORK_PERIOD_DAY) {
                // houseworkInfo의 외래키, 연관 관계 전부 해제 후 삭제한다. 그리고 현재 시점 이후의 HouseworkTodo를 DELETE로 변경한다.
                houseworkInfoDeleteClean(requestInfo);

                dirtyBitCheckWithGroupIdAndRequest(groupId, requestTodo.getHouseworkInfo().getType(), requestInfo.getWeekDate(), requestInfo.getMonthDate());


                // HouseworkTodo 생성
                houseworkTodoRepository.save(HouseworkTodo.builder()
                        .houseworkInfo(null)
                        .groupInfo(groupInfo)
                        .houseworkMember(groupMemberIdListString)
                        .category(category.getName())
                        .title(request.getTitle())
                        .detail(request.getDetail())
                        .status(HouseworkTodoStatus.HOUSEWORK_TODO_INCOMPLETE)
                        .date(updateDate)
                        .endTime(request.getEndTime())
                        .build());

                // 해당 HouseworkTodo에 맞는 groupInfoId와 Date가 캐싱되어있다면, dirtyBit를 true로 바꿔줘야함.
                dirtyBitCheckWithDate(request.getGroupId(), updateDate);

            }

            // -> 비단건 집안일
            else {
                // hosueworkInfo의 담당자 리스트를 뽑은 후 groupMemberIdList와 동일하지 않다면 false 리턴
                boolean isUpdateHouseworkMember = houseworkMemberRepository.deleteHouseworkMemberIfGroupMemberIdIn(requestInfo, request.getGroupMemberIdList());

                // 담당자가 바뀔 것이므로 이전 담당자 리스트 삭제
                if (!isUpdateHouseworkMember) {
                    houseworkMemberRepository.deleteByHouseworkInfo(requestInfo);
                }

                // HouseworkInfo를 외래키로 가진 HouseworkTodo의 houseworkInfo 필드값 null로 변환해 관계 해제
                houseworkTodoRepository.updateHouseworkTodoByHouseworkInfoId(requestInfo.getId());

                // 호출 시점 이후의 HouseworkTodo를 HOUSEWORK_TODO_DELETE로 상태 변경
                houseworkTodoRepository.findHouseworkTodoByHouseworkInfo(requestInfo.getId()).stream()
                        .filter(todo -> todo.getDate().isAfter(LocalDate.now())) // 호출 시점 날짜
                        .forEach(HouseworkTodo::deleteHouseworkTodo);

                // findByGroupInfoId를 통해 해당 그룹의 캐싱된 객체 조회 -> 수정 전 날짜 기준
                dirtyBitCheckWithGroupIdAndRequest(groupId, requestInfo.getType(), requestInfo.getWeekDate(), requestInfo.getMonthDate());

                // HouseworkInfo 수정 메서드 사용해 수정
                requestInfo.updateHouseworkInfo(request.getTitle(), request.getDetail(),
                        category, request.getType(), updateDate, request.getEndTime(), requestInfo.getWeekDate(), requestInfo.getMonthDate());

                // HouseworkMember 생성 - 담당자 지정
                if (!isUpdateHouseworkMember) {
                    // HOUSEWORK_MEMBER 저장 - 담당자 지정
                    groupMemberList.stream()
                            .map(gm -> HouseworkMember.builder()
                                    .groupMember(gm)
                                    .houseworkInfo(requestInfo)
                                    .build())
                            .forEach(houseworkMemberRepository::save);
                }

                // findByGroupInfoId를 통해 해당 그룹의 캐싱된 객체 조회 -> 수정 후 날짜 기준
                dirtyBitCheckWithGroupIdAndRequest(request.getGroupId(), request.getType(), request.getWeekDate(), request.getMonthDate());
            }


        }


    }

    // houseworkInfo의 외래키, 연관 관계 전부 해제 후 삭제한다. 그리고 현재 시점 이후의 HouseworkTodo를 DELETE로 변경한다.
    private void houseworkInfoDeleteClean(HouseworkInfo requestInfo) {
        // HouseworkInfo를 외래키로 가진 HouseworkMember 삭제
        houseworkMemberRepository.deleteByHouseworkInfo(requestInfo);

        // HouseworkInfo를 외래키로 가진 HouseworkTodo의 houseworkInfo 필드값 null로 변환해 관계 해제
        houseworkTodoRepository.updateHouseworkTodoByHouseworkInfoId(requestInfo.getId());

        // HouseworkInfo 삭제
        houseworkInfoRepository.deleteById(requestInfo.getId());
        log.info(">>>> HouseworkInfo Deleted: {}", requestInfo.getId());

        // 호출 시점 이후의 HouseworkTodo를 HOUSEWORK_TODO_DELETE로 상태 변경
        houseworkTodoRepository.findHouseworkTodoByHouseworkInfo(requestInfo.getId()).stream()
                .filter(todo -> todo.getDate().isAfter(LocalDate.now()))
                .forEach(HouseworkTodo::deleteHouseworkTodo);
    }

    // 해당 HouseworkTodo에 맞는 groupInfoId와 Date가 캐싱되어있다면, dirtyBit를 true로 바꿔줘야함.
    private void dirtyBitCheckWithDate(Long groupId, LocalDate requestDate) {
        todoListRepository.findByGroupInfoIdAndDate(groupId, requestDate)
                .ifPresent(todoList -> {
                    todoList.checkDirtyBit();
                    todoListRepository.save(todoList);
                    log.info(">>>> dirtyBit Checking TodoList id: {}", todoList.getId());
                });
    }

    // HouseworkInfo 생성 & 담당자 지정 해주기 - HouseworkMember 생성
    private void houseworkInfoCreateAndHouseworkMemberCreate(HouseworkInfoCreateServiceRequest request, GroupInfo groupInfo, HouseworkCategory category, List<GroupMember> groupMemberList) {
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

        // HOUSEWORK_MEMBER 저장 - 담당자 지정
        groupMemberList.stream()
                .map(gm -> HouseworkMember.builder()
                        .groupMember(gm)
                        .houseworkInfo(houseworkInfo)
                        .build())
                .forEach(houseworkMemberRepository::save);
    }


    // groupId로 todoList를 조회해서 PeriodType으로 필터링 후 일치하는 todoList dirtyBit 체킹 (groupId, periodType, 수정할 weekDate, 수정할 monthDate)
    private void dirtyBitCheckWithGroupIdAndRequest(Long groupId, HouseworkPeriodType houseworkPeriodType, String weekDate, String monthDate) {
        todoListRepository.findByGroupInfoId(groupId).stream()
                .filter(todoList -> // PERIOD에 맞는 TodoList 선별
                        switch (houseworkPeriodType) {
                            case HOUSEWORK_PERIOD_DAY -> false;
                            case HOUSEWORK_PERIOD_EVERYDAY -> true;
                            case HOUSEWORK_PERIOD_WEEK -> DayOfWeekUtils.equals(weekDate, todoList.getDate());
                            case HOUSEWORK_PERIOD_MONTH -> Arrays.stream(monthDate.split(","))
                                    .anyMatch(d -> Integer.parseInt(d) == todoList.getDate().getDayOfMonth());
                        })
                .forEach(todoList -> { // dirtyBit Checking
                    todoList.checkDirtyBit();
                    todoListRepository.save(todoList);
                    log.info(">>>> dirtyBit Checking TodoList id: {}", todoList.getId());
                });
    }
}