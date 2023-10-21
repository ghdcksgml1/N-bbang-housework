package com.heachi.housework;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

/* static 모음 *
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

* Mocking *
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
*/

@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class TestConfig {

    public static User generateUser() {

        return User.builder()
                .platformId("123456")
                .platformType(UserPlatformType.KAKAO)
                .role(UserRole.USER)
                .name("kms")
                .email("kms@kakao.com")
                .phoneNumber("010-0000-0000")
                .profileImageUrl("https://google.com")
                .pushAlarmYn(true)
                .build();
    }

    public static User generateUser(String email, String phoneNumber) {

        return User.builder()
                .platformId("123456")
                .platformType(UserPlatformType.KAKAO)
                .role(UserRole.USER)
                .name("kms")
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
                .name("group")
                .info("hello world!")
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

    public static HouseworkCategory generateHouseworkCategory() {

        return HouseworkCategory.builder()
                .name("집안일")
                .build();
    }

    public static HouseworkInfo generateHouseworkInfo(GroupInfo groupInfo, HouseworkCategory category) {

        return HouseworkInfo.builder()
                .houseworkCategory(category)
                .groupInfo(groupInfo)
                .title("빨래")
                .detail("빨래 돌리기")
                .type(HouseworkPeriodType.HOUSEWORK_PERIOD_EVERYDAY)
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
