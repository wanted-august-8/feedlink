package com.feedlink.api.feedlink_api.domain.statistic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatisticResultDTO {
    @NonNull
    private String statisticKey;
    @JsonInclude(Include.NON_NULL)
    private Long postCnt;
    @JsonInclude(Include.NON_NULL)
    private Long viewCnt;
    @JsonInclude(Include.NON_NULL)
    private Long likeCnt;
    @JsonInclude(Include.NON_NULL)
    private Long shareCnt;

    @Builder
    public StatisticResultDTO(@NonNull String statisticKey, Long postCnt, Long viewCnt, Long likeCnt,
        Long shareCnt) {
        this.statisticKey = statisticKey;
        this.postCnt = postCnt;
        this.viewCnt = viewCnt;
        this.likeCnt = likeCnt;
        this.shareCnt = shareCnt;
    }
}
