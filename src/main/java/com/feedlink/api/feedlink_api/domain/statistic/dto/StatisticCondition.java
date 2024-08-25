package com.feedlink.api.feedlink_api.domain.statistic.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class StatisticCondition {
    @NonNull
    private String hashtag;
    @NonNull
    private LocalDateTime startDateTime;
    @NonNull
    private LocalDateTime endDateTime;
    @NonNull
    private String target;
    @NonNull
    private Boolean byHour;
}
