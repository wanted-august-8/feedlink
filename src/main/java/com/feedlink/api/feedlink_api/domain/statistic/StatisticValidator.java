package com.feedlink.api.feedlink_api.domain.statistic;

import com.feedlink.api.feedlink_api.domain.statistic.nums.StatisticType;
import com.feedlink.api.feedlink_api.domain.statistic.nums.StatisticValue;
import com.feedlink.api.feedlink_api.global.error.ErrorCode;
import com.feedlink.api.feedlink_api.global.exception.CustomException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatisticValidator {

    /**
     * 통계 요청 type, value 유효성 검증 메서드입니다.
     *
     * @param type 월별, 시각별
     * @param value 통계 대상
     *
     * */
    public static void validateValueType(List<String> type, String value) {

        if (!StatisticValue.isValidValue(value))
            throw new CustomException(ErrorCode.VALIDATE_PARAM);


        if (!StatisticType.isValidValue(type)) {
            throw new CustomException(ErrorCode.VALIDATE_PARAM);
        }

    }

    /**
     * 통계 요청 날짜 관련 유효성 검증 메서드입니다.
     *
     * @param type date, hour
     * @param startDateTime 통계 시작 일자
     * @param endDateTime 통계 종료 일자
     *
     * */
    public static void validateDate(List<String> type, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        log.info("[{}] Validate by Date- type:{}, startDateTime : {}, endDateTime:{}",Thread.currentThread().getStackTrace()[1].getMethodName(),type.stream().toArray(),startDateTime,endDateTime);
        if(startDateTime.isAfter(endDateTime))
            throw new CustomException(ErrorCode.DATE_VALIDATE_PARAM);

        if(startDateTime.isAfter(LocalDateTime.now()))
            throw new CustomException(ErrorCode.DATE_VALIDATE_PARAM);

        if(endDateTime.isAfter(LocalDateTime.now()))
            throw new CustomException(ErrorCode.DATE_VALIDATE_PARAM);

        if (type.size() > 1) {
            if(Duration.between(startDateTime, endDateTime).toDays() > 30)
                throw new CustomException(ErrorCode.DATE_VALIDATE_PARAM);

        } else {
            if(type.get(0).equals(StatisticType.DATE.getValue())
                && Duration.between(startDateTime, endDateTime).toDays() > 30)
                throw new CustomException(ErrorCode.DATE_VALIDATE_PARAM);

            if(type.get(0).equals(StatisticType.HOUR.getValue())
                && Duration.between(startDateTime, endDateTime).toDays() > 7)
                throw new CustomException(ErrorCode.DATE_VALIDATE_PARAM);

        }
    }
}

