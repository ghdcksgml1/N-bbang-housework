package com.heachi.mysql.define.housework.member.repository;

import com.heachi.mysql.TestConfig;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.category.repository.HouseworkCategoryRepository;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import com.heachi.mysql.define.housework.todo.repository.HouseworkTodoRepository;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HouseworkMemberRepositoryTest extends TestConfig {

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupInfoRepository groupInfoRepository;

    @Autowired
    private HouseworkCategoryRepository houseworkCategoryRepository;

    @Autowired
    private HouseworkInfoRepository houseworkInfoRepository;

    @Autowired
    private HouseworkTodoRepository houseworkTodoRepository;

    @Autowired
    private HouseworkMemberRepository houseworkMemberRepository;

    @AfterEach
    void tearDown() {
        houseworkTodoRepository.deleteAllInBatch();
        houseworkMemberRepository.deleteAllInBatch();
        houseworkInfoRepository.deleteAllInBatch();
        houseworkCategoryRepository.deleteAllInBatch();
        groupMemberRepository.deleteAllInBatch();
        groupInfoRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("HouseworkInfo가 일치하는 HouseworkMember Entity를 삭제한다.")
    void deleteByHouseworkInfo() {
        // given
        User user = userRepository.save(generateUser());
        User user2 = userRepository.save(generateCustomUser("ghdcksgml1@naver.com", "010-1111-1111"));
        User user3 = userRepository.save(generateCustomUser("ghdcksgml2@naver.com", "010-2222-2222"));
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(user));
        GroupInfo groupInfo2 = groupInfoRepository.save(generateGroupInfo(user2));
        GroupInfo groupInfo3 = groupInfoRepository.save(generateGroupInfo(user3));
        GroupMember groupMember = groupMemberRepository.save(generateGroupMember(user, groupInfo));
        GroupMember groupMember2 = groupMemberRepository.save(generateGroupMember(user2, groupInfo));
        GroupMember groupMember3 = groupMemberRepository.save(generateGroupMember(user3, groupInfo));

        HouseworkCategory houseworkCategory = houseworkCategoryRepository.save(generateHouseworkCategory());
        HouseworkInfo houseworkInfo = houseworkInfoRepository.save(generateHouseworkInfo(houseworkCategory));
        HouseworkInfo houseworkInfo2 = houseworkInfoRepository.save(generateHouseworkInfo(houseworkCategory));
        HouseworkInfo houseworkInfo3 = houseworkInfoRepository.save(generateHouseworkInfo(houseworkCategory));

        HouseworkMember houseworkMember = houseworkMemberRepository.save(generateHouseworkMember(groupMember, houseworkInfo));
        HouseworkMember houseworkMember2 = houseworkMemberRepository.save(generateHouseworkMember(groupMember2, houseworkInfo2));
        HouseworkMember houseworkMember3 = houseworkMemberRepository.save(generateHouseworkMember(groupMember3, houseworkInfo));
        HouseworkInfo findHouseworkInfo = houseworkInfoRepository.findHouseworkInfoByIdJoinFetchHouseworkMembers(houseworkInfo.getId()).get();

        HouseworkTodo houseworkTodo = houseworkTodoRepository.save(generateHouseworkTodo(findHouseworkInfo, groupInfo, LocalDate.now()));

        // when
        houseworkMemberRepository.findAll().forEach(System.out::println);
        houseworkMemberRepository.deleteByHouseworkInfo(houseworkInfo);
        houseworkMemberRepository.findAll().forEach(System.out::println);

        // then

    }
}