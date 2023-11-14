package com.heachi.mysql.define.housework.todo.repository;

import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import com.heachi.mysql.define.housework.todo.repository.response.HouseworkTodoCount;

import java.util.List;
import java.util.Optional;

public interface HouseworkTodoRepositoryCustom {

    // groupInfoId 리스트를 받아 해당 그룹들의 오늘 날짜의 HouseworkTodo의 개수를 셀 수 있는 DTO를 반환하는 쿼리
    public List<HouseworkTodoCount> findHouseworkTodoCountByGroupInfoIdList(List<Long> groupInfoIdList);

    // todoId와 groupMemberId를 통해 해당 그룹원이 해당 집안일의 담당자인지 확인한다.
    public Optional<HouseworkTodo> findHouseworkTodoByIdAndGroupMemberId(Long todoId, Long groupMemberId);

    // houseworkInfoId와 일치하는 HouseworkTodo들의 houseworkInfoId를 null로 업데이트 한다. (HouseworkInfo 의존성을 끊기위해 사용)
    public long updateHouseworkTodoByHouseworkInfoId(Long houseworkInfoId);
}
