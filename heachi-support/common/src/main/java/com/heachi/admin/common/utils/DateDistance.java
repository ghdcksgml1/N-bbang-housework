package com.heachi.admin.common.utils;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

public class DateDistance {
    private static final long SECONDS = 1;
    private static final long MINUTES = 60 * SECONDS;
    private static final long HOURS = 60 * MINUTES;
    private static final long DAYS = 24 * HOURS;
    private static final long WEEKS = 7 * DAYS;
    private static final long MONTHS = 4 * WEEKS;
    private static final long YEAR = 365 * DAYS;

    /**
     * 현재 날짜로부터 얼마나 시간이 지났는지 나타냄.
     */
    public static String of(Temporal target) {
        LocalDateTime targetDate;
        if (target instanceof LocalDateTime) {
            targetDate = (LocalDateTime) target;
        } else if (target instanceof Timestamp) {
            targetDate = ((Timestamp) target).toLocalDateTime();
        } else if (target instanceof LocalDate) {
            targetDate = ((LocalDate) target).atStartOfDay();
        } else {
            throw new IllegalArgumentException("지원하지 않는 타입입니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        long dateDistance = Duration.between(targetDate, now).getSeconds();

        if (dateDistance >= YEAR) {
            return (dateDistance / YEAR) + "년 전";
        } else if (dateDistance >= MONTHS) {
            return (dateDistance / MONTHS) + "달 전";
        } else if (dateDistance >= WEEKS) {
            return (dateDistance / WEEKS) + "주 전";
        } else if (dateDistance >= DAYS) {
            return (dateDistance / DAYS) + "일 전";
        } else if (dateDistance >= HOURS) {
            return (dateDistance / HOURS) + "시간 전";
        } else if (dateDistance >= MINUTES) {
            return (dateDistance / MINUTES) + "분 전";
        } else if (dateDistance >= SECONDS) {
            return (dateDistance / SECONDS) + "초 전";
        } else if (dateDistance == 0) {
            return "방금";
        } else {
            throw new IllegalArgumentException("날짜가 잘못되었습니다.");
        }
    }
}
