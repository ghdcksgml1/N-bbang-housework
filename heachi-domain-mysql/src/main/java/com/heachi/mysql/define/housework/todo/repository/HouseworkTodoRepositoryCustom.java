package com.heachi.mysql.define.housework.todo.repository;

import com.heachi.mysql.define.housework.todo.repository.response.HouseworkTodoCount;

import java.util.List;

public interface HouseworkTodoRepositoryCustom {

    // groupInfoId 리스트를 받아 해당 그룹들의 오늘 날짜의 HouseworkTodo의 개수를 셀 수 있는 DTO를 반환하는 쿼리
    public List<HouseworkTodoCount> findHouseworkTodoCountByGroupInfoIdList(List<Long> groupInfoIdList);
}
