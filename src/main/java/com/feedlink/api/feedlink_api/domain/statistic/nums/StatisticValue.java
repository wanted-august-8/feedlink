package com.feedlink.api.feedlink_api.domain.statistic.nums;

import lombok.Getter;

@Getter
public enum StatisticValue {
    COUNT("count"),
    LIKE("like_count"),
    VIEW("view_count"),
    SHARE("share_count");

    private String value;

    StatisticValue(String value) {
        this.value = value;
    }

    /**
     * 통계 요청 value 유효성 검사 메서드
     *
     * @Param value
     * @return 유효성 검증 결과 Boolean
     * */
    public static boolean isValidValue(String value) {
        for (StatisticValue statisticValue : values()) {
            if (statisticValue.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
