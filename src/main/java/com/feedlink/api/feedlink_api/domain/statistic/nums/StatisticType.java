package com.feedlink.api.feedlink_api.domain.statistic.nums;

import java.util.List;
import lombok.Getter;

@Getter
public enum StatisticType {
    DATE("date"),
    HOUR("hour");

    private String value;

    StatisticType(String value) {
        this.value = value;
    }

    /**
     * 통계 요청 type 유효성 검사 메서드
     *
     * @param types 문자열 리스트 값
     * @return 유효성 검증 결과 Boolean
     */
    public static boolean isValidValue(List<String> types) {
        for (String type : types) {
            boolean isValid = false;
            for (StatisticType statisticType : values()) {
                if (statisticType.getValue().equals(type)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                return false;
            }
        }
        return true;
    }

    /**
     * value 값을 기준으로 해당하는 StatisticType enum을 찾는다.
     *
     * @param value enum의 value
     * @return 해당하는 StatisticType enum || null
    */
    public static StatisticType fromValue(String value) {
        for (StatisticType statisticType : values()) {
            if (statisticType.getValue().equals(value)) {
                return statisticType;
            }
        }
        return null;
    }

}
