package com.heachi.mysql.define;

import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.constant.GroupMemberRole;
import com.heachi.mysql.define.group.member.constant.GroupMemberStatus;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.category.repository.HouseworkCategoryRepository;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import com.heachi.mysql.define.housework.member.repository.HouseworkMemberRepository;
import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import com.heachi.mysql.define.housework.todo.repository.HouseworkTodoRepository;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class Initializer implements ApplicationRunner {
    @Autowired private UserRepository userRepository;
    @Autowired private GroupInfoRepository groupInfoRepository;
    @Autowired private GroupMemberRepository groupMemberRepository;
    @Autowired private HouseworkCategoryRepository houseworkCategoryRepository;
    @Autowired private HouseworkInfoRepository houseworkInfoRepository;
    @Autowired private HouseworkMemberRepository houseworkMemberRepository;
    @Autowired private HouseworkTodoRepository houseworkTodoRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = userRepository.save(generateCustomUser("김준영", "kjy@naver.com", "010-0000-0000"));
        User user2 = userRepository.save(generateCustomUser("구지원", "itkoo@naver.com", "010-1111-1111"));
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(user));
        GroupMember groupMember = groupMemberRepository.save(generateGroupMember(user, groupInfo));
        GroupMember groupMember2 = groupMemberRepository.save(generateGroupMember(user2, groupInfo));

        HouseworkCategory houseworkCategory = houseworkCategoryRepository.save(generateCustomHouseworkCategory("집안일"));
        HouseworkCategory houseworkCategory2 = houseworkCategoryRepository.save(generateCustomHouseworkCategory("집 밖의 일"));
        HouseworkInfo houseworkInfo = houseworkInfoRepository.save(generateCustomHouseworkInfo(houseworkCategory, groupInfo, "청소기 돌리기", HouseworkPeriodType.HOUSEWORK_PERIOD_EVERYDAY));
        HouseworkInfo houseworkInfo2 = houseworkInfoRepository.save(generateCustomHouseworkInfo(houseworkCategory2, groupInfo, "쓰레기 버리기", HouseworkPeriodType.HOUSEWORK_PERIOD_EVERYDAY));

        houseworkMemberRepository.save(generateHouseworkMember(groupMember, houseworkInfo));
        houseworkMemberRepository.save(generateHouseworkMember(groupMember2, houseworkInfo));
        houseworkMemberRepository.save(generateHouseworkMember(groupMember2, houseworkInfo2));

        HouseworkTodo houseworkTodo = houseworkTodoRepository.save(generateHouseworkTodo(houseworkInfo, groupInfo, LocalDate.now()));
        HouseworkTodo houseworkTodo2 = houseworkTodoRepository.save(generateHouseworkTodo(houseworkInfo2, groupInfo, LocalDate.now()));
    }

    public static User generateCustomUser(String name, String email, String phoneNumber) {
        return User.builder()
                .platformId("1111")
                .platformType(UserPlatformType.KAKAO)
                .role(UserRole.USER)
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .profileImageUrl("https://google.com")
                .pushAlarmYn(true)
                .build();
    }

    public static GroupInfo generateGroupInfo(User user) {

        return GroupInfo.builder()
                .user(user)
                .bgColor("bbbbbb")
                .colorCode("111111")
                .gradient("csscssscss")
                .name("준영하우스")
                .info("hello!")
                .build();
    }

    public static GroupMember generateGroupMember(User user, GroupInfo groupInfo) {

        return GroupMember.builder()
                .groupInfo(groupInfo)
                .user(user)
                .role(GroupMemberRole.GROUP_MEMBER)
                .status(GroupMemberStatus.ACCEPT)
                .build();
    }

    public static HouseworkCategory generateCustomHouseworkCategory(String name) {

        return HouseworkCategory.builder()
                .name(name)
                .build();
    }

    public static HouseworkInfo generateCustomHouseworkInfo(HouseworkCategory category, GroupInfo groupInfo, String title, HouseworkPeriodType type) {

        return HouseworkInfo.builder()
                .houseworkCategory(category)
                .groupInfo(groupInfo)
                .title(title)
                .detail("빨래 돌리기")
                .type(type)
                .endTime(LocalTime.of(18,0))
                .build();
    }

    public static HouseworkMember generateHouseworkMember(GroupMember groupMember, HouseworkInfo houseworkInfo) {

        return HouseworkMember.builder()
                .houseworkInfo(houseworkInfo)
                .groupMember(groupMember)
                .build();
    }

    public static HouseworkTodo generateHouseworkTodo(HouseworkInfo houseworkInfo, GroupInfo groupInfo, LocalDate date) {

        return HouseworkTodo.makeTodoReferInfo(houseworkInfo, groupInfo, date);
    }
}
