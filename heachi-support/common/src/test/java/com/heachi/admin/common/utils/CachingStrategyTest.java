package com.heachi.admin.common.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CachingStrategyTest {

    @Test
    @DisplayName("만약 메소드에서 올바른 리턴값이 나온다면, 그대로 리턴해준다.")
    void cachingIfEmpty() {
        // given
        UserTest user = new UserTest("hong", "chanhee");

        // when
        String s = CachingStrategy.cachingIfEmpty(user, UserTest::getName, UserTest::getReflectName);

        // then
        assertThat(s).isEqualTo("hong chanhee");
    }

    @Test
    @DisplayName("만약 첫번째 메소드에서 null이라는 리턴값이 나온다면, 두번째 메소드를 실행시킨다.")
    void cachingIfEmptyMiss() {
        // given
        UserTest user = new UserTest("hong", "chanhee");

        // when
        String s = CachingStrategy.cachingIfEmpty(user, UserTest::nullName, UserTest::getReflectName);

        // then
        assertThat(s).isEqualTo("chanhee hong");
    }

    @Test
    @DisplayName("만약 첫번째 메소드에서 리턴값이 나왔지만, 조건에 걸린다면, 두번째 메소드를 실행시킨다.")
    void cachingIfEmptyAddConditional() {
        // given
        UserTest user = new UserTest("hong", "chanhee");

        // when
        String s = CachingStrategy.cachingIfEmpty(user, UserTest::getName, UserTest::getReflectName, (str) -> str.equals(user.getReflectName()));

        // then
        assertThat(s).isEqualTo("chanhee hong");
    }

    private static class UserTest {
        private String firstName;
        private String lastName;

        public UserTest(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String nullName() {
            return null;
        }

        public String getName() {
            return firstName + " " + lastName;
        }

        public String getReflectName() {

            return lastName + " " + firstName;
        }

        public boolean isName(String name) {

            return name.equals(getName());
        }
    }
}