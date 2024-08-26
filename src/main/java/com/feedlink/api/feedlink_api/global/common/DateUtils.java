package com.feedlink.api.feedlink_api.global.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateUtils {
    public static final String TIME_ZONE = "Asia/Seoul";
    public static final String dateFormat = "yyyy-MM-dd";
    public static final String dateTimeFormat = "yyyy-MM-dd HH:00";


    /**
     * Converts an Instant to LocalDateTime with the specified time zone,
     * setting the time part to 00:00:00.
     *
     * @param date the Instant to convert
     * @param hour 설정할 시각
     * @param minute 설정할 분
     * @param sec 설정할 초
     * @return LocalDateTime with the time set
     */
    public static LocalDateTime convertDateToLocalDateTime(Date date,int hour,int minute, int sec) {

        Instant instant = date==null?Instant.now():date.toInstant();

        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of(TIME_ZONE));
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        return localDateTime.withHour(hour).withMinute(minute).withSecond(sec);

    }

}
