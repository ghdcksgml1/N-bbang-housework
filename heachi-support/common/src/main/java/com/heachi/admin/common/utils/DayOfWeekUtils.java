package com.heachi.admin.common.utils;

import java.time.LocalDate;
import java.util.Arrays;

public class DayOfWeekUtils {

    /**
     * 해당 날짜가 day가 나타내는 요일인지
     *
     * @param day 요일을 나타내는 숫자 (월: 1 ~ 일: 7)
     * @param target day가 나타내는 요일과 같은지 판단할 날짜
     * @return
     */
    public static boolean equals(int day, LocalDate target) {

        return (day == target.getDayOfWeek().getValue());
    }

    /**
     * 여러개의 요일 중 하나라도 target 날짜에 들어가면 True 반환
     *
     * @param day 요일을 나타내는 한개 이상의 숫자 (1,3,5 => 월,수,금)
     * @param target day가 나타내는 요일과 같은지 판단할 날짜
     * @return
     */
    public static boolean equals(String day, LocalDate target) {

        return Arrays.stream(day.split(","))
                .mapToInt(Integer::parseInt)
                .filter(i -> equals(i, target))
                .findFirst().isPresent();
    }
}
