package com.heachi.mysql.define.housework.info.constant;

public enum HouseworkPeriodType {
    HOUSEWORK_PERIOD_DAY,       // 딱 한번 (Date) ex. 2022.09.12
    HOUSEWORK_PERIOD_EVERYDAY,  // 매일
    HOUSEWORK_PERIOD_WEEK,      // 주마다 (String) ex. 월,화
    HOUSEWORK_PERIOD_MONTH      // 달마다 (String) ex. 1,23,29
}
