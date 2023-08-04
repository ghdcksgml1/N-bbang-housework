package com.heachi.admin.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RunningTimeFormatter {

    private static final Map<String, Integer> TIME_UNIT_BINARY_VALUE = new HashMap<>() {
        {
            put("MILLISECONDS", 0B00001);
            put("SECONDS", 0B00010);
            put("MINUTES", 0B00100);
            put("HOURS", 0B01000);
            put("DAYS", 0B10000);
        }
    };
    private static final String[] TIME_FORMAT = {"(ms)", "(s)", "(m)", "(h)", "(d)"};
    private static final Long[] TIME_VALUE    = { 1000L,   60L,   60L,   24L, 100000L};

    /**
     * 시간과 TimeUnit을 파라미터로 받아 Time Unit에 맞는 시간으로 변환해주는 함수
     * (MilliSeconds로 단위가 고정되어 있는게 아니라, TimeUnit 기준으로 단위를 설정함.)
     *
     * ex.1) System.out.println(RunningTimeFormatter.format(4223230L, TimeUnit.MILLISECONDS));
     *      -> 1(h) 10(m) 23(s) 230(ms)
     * ex.2) System.out.println(format(4223L, TimeUnit.SECONDS));
     *      -> 1(h) 10(m) 23(s)
     *
     * @param runningTime : Long 타입의 실행시간 (형식을 변환시킬 대상)
     * @param timeUnit    : 변경할 시간 단위 (MilliSeconds ~ Days)
     * @return
     */
    public static String format(Long runningTime, TimeUnit timeUnit) {
        int bitmask = getBitmask(timeUnit);

        return generateStringFromBitMask(bitmask, runningTime);
    }

    /**
     * TimeUnit Default 버전
     *
     * @param runningTime (, TimeUnit.MILLISECONDS)
     * @return
     */
    public static String format(Long runningTime) {
        int bitmask = TIME_UNIT_BINARY_VALUE.get("MILLISECONDS");

        return generateStringFromBitMask(bitmask, runningTime);
    }

    /**
     * format에서 잘라내고 싶은 부분을 설정할 수 있는 함수
     *
     * ex.1) RunningTimeFormatter.format(4223230L, TimeUnit.MILLISECONDS, 0)
     *       -> 1(h) 10(m) 23(s) 230(ms)
     *       RunningTimeFormatter.format(4223230L, TimeUnit.MILLISECONDS, 1)
     *       -> 1(h) 10(m) 23(s)
     *       RunningTimeFormatter.format(4223230L, TimeUnit.MILLISECONDS, 2)
     *       -> 1(h) 10(m)
     * ex.2) RunningTimeFormatter.format(4223L, TimeUnit.SECONDS, 0);
     *       -> 1(h) 10(m) 23(s)
     *       RunningTimeFormatter.format(4223L, TimeUnit.SECONDS, 1);
     *       -> 1(h) 10(m)
     *       RunningTimeFormatter.format(4223L, TimeUnit.SECONDS, 2);
     *       -> 1(h)
     *
     * @param runningTime : Long 타입의 실행시간 (형식을 변환시킬 대상)
     * @param timeUnit    : 변경할 시간 단위 (MilliSeconds ~ Days)
     * @param startIndex  : 시간 단위로 부터 잘라내고 싶은 단위의 개수
     * @return
     */
    public static String format(Long runningTime, TimeUnit timeUnit, int startIndex) {
        Long cpyRunningTime = runningTime;

        if (!(startIndex >= 0 && startIndex < TIME_VALUE.length)) {
            throw new IllegalArgumentException("startIndex는 0~4 사이의 값 입니다.");
        }

        int bitmask = getBitmask(timeUnit);
        for (int i = 0; bitmask != 0; i++) {
            if (bitmask % 2 == 1) {
                if (i + startIndex >= TIME_VALUE.length) {
                    throw new IllegalArgumentException("잘못된 startIndex 값 입니다.");
                }

                int index = i + startIndex;
                bitmask = (int) Math.pow(2, index);
                for ( ; i < index; i++) {
                    cpyRunningTime /= TIME_VALUE[i];
                }

                break;
            }
            bitmask /= 2;
        }

        return generateStringFromBitMask(bitmask, cpyRunningTime);
    }

    // TimeUnit을 Bitmask로 바꿔주는 메소드
    private static int getBitmask(TimeUnit timeUnit) {
        int bitmask; // TimeUnit에 맞는 2진수를 임시로 저장할 변수

        /**
         * 원하는 시간 단위만 가져오기 편하게 2진수로 구현했음.
         *
         * ex) 현재 TimeUnit.MILLISECONDS라면 0B00001이기 때문에,
         *     값이 충분히 크다면, (d), (h), (m), (s), (ms) 모두 출력한다.
         */
        switch (timeUnit) {
            case MILLISECONDS -> bitmask = TIME_UNIT_BINARY_VALUE.get("MILLISECONDS"); // (d), (h), (m), (s), (ms)
            case SECONDS      -> bitmask = TIME_UNIT_BINARY_VALUE.get("SECONDS");      // (d), (h), (m), (s)
            case MINUTES      -> bitmask = TIME_UNIT_BINARY_VALUE.get("MINUTES");      // (d), (h), (m)
            case HOURS        -> bitmask = TIME_UNIT_BINARY_VALUE.get("HOURS");        // (d), (h)
            case DAYS         -> bitmask = TIME_UNIT_BINARY_VALUE.get("DAYS");         // (d)
            default           -> bitmask = 0B00000; //
        }

        return bitmask;
    }

    private static String generateStringFromBitMask(int bitmask, Long runningTime) {
        StringBuffer sb = new StringBuffer("");
        Long cpyRunningTime = runningTime;

        // 비트 마스킹 되어있는 1 찾기
        for (int i = 0; bitmask != 0; i++) {
            // 1을 찾으면 해당 단위부터 계산 시작
            if (bitmask % 2 == 1) {
                for (; cpyRunningTime != 0;i++) {
                    sb.insert(0, " " + (cpyRunningTime % TIME_VALUE[i]) + TIME_FORMAT[i]);
                    cpyRunningTime /= TIME_VALUE[i];
                }
            }
            bitmask /= 2;
        }

        return sb.toString();
    }
}