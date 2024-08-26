package com.feedlink.api.feedlink_api.domain.statistic.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.feedlink.api.feedlink_api.domain.statistic.dto.StatisticResultDTO;
import com.feedlink.api.feedlink_api.global.common.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticRes {
    @Schema(description = "통계 결과 시작 일자", example = "2024-08-18")
    private String start;

    @Schema(description = "통계 결과 종료 일자", example = "2024-08-25")
    private String end;

    @JsonInclude(Include.NON_EMPTY)
    @Schema(description = "통계 기간내 일자별 목록")
    private List<StatisticResultDTO> dateList;

    @JsonInclude(Include.NON_EMPTY)
    @Schema(description = "통계 기간내 시각별 목록")
    private List<StatisticResultDTO> hourList;

    @Builder
    public StatisticRes(List<StatisticResultDTO> dateList, List<StatisticResultDTO> hourList, LocalDateTime startDateTime, LocalDateTime endDateTime, String value) {
        this.start = startDateTime.format(DateTimeFormatter.ofPattern(DateUtils.dateFormat));
        this.end = endDateTime.format(DateTimeFormatter.ofPattern(DateUtils.dateFormat));

        this.dateList = dateList.isEmpty() ? new ArrayList<>() : addZeroDataByDate(dateList, startDateTime, endDateTime, value);
        this.hourList = hourList.isEmpty() ? new ArrayList<>() : addZeroDataByHour(hourList, startDateTime, endDateTime, value);
    }

    private List<StatisticResultDTO> addZeroDataByHour(List<StatisticResultDTO> hour, LocalDateTime startDateTime, LocalDateTime endDateTime, String value) {
        Set<String> hourSet = hour.stream().map(StatisticResultDTO::getStatisticKey).collect(Collectors.toSet());

        for (LocalDateTime dateTime = startDateTime; !dateTime.isAfter(endDateTime); dateTime = dateTime.plusHours(1)) {
            String key = dateTime.format(DateTimeFormatter.ofPattern(DateUtils.dateTimeFormat));

            if (!hourSet.contains(key)) {
                hour.add(createStatisticResultDTO(key, value));
            }
        }

        return hour.stream()
            .sorted(Comparator.comparing(StatisticResultDTO::getStatisticKey).reversed())
            .collect(Collectors.toList());
    }

    private List<StatisticResultDTO> addZeroDataByDate(List<StatisticResultDTO> date, LocalDateTime startDateTime, LocalDateTime endDateTime, String value) {
        Set<String> dateSet = date.stream().map(StatisticResultDTO::getStatisticKey).collect(Collectors.toSet());

        for (LocalDate day = endDateTime.toLocalDate(); !day.isBefore(startDateTime.toLocalDate()); day = day.minusDays(1)) {
            String key = day.format(DateTimeFormatter.ofPattern(DateUtils.dateFormat));

            if (!dateSet.contains(key)) {
                date.add(createStatisticResultDTO(key, value));
            }
        }

        return date.stream()
            .sorted(Comparator.comparing(StatisticResultDTO::getStatisticKey).reversed())
            .collect(Collectors.toList());
    }

    private StatisticResultDTO createStatisticResultDTO(String key, String value) {
        StatisticResultDTO.StatisticResultDTOBuilder builder = StatisticResultDTO.builder().statisticKey(key);

        switch (value) {
            case "count":
                builder.postCnt(0L);
                break;
            case "like_count":
                builder.likeCnt(0L);
                break;
            case "view_count":
                builder.viewCnt(0L);
                break;
            case "share_count":
                builder.shareCnt(0L);
                break;
        }

        return builder.build();
    }
}
