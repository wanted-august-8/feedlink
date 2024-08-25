package com.feedlink.api.feedlink_api.domain.statistic.request;

import com.feedlink.api.feedlink_api.domain.statistic.nums.StatisticValue;
import com.feedlink.api.feedlink_api.global.common.DateUtils;
import com.feedlink.api.feedlink_api.global.error.ErrorCode;
import com.feedlink.api.feedlink_api.global.exception.CustomException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class StatisticReqDTO {
    @NonNull
    private LocalDateTime startDateTime;
    @NonNull
    private LocalDateTime endDateTime;
    @NonNull
    private String hashtag;
    @NonNull
    private List<String> types;
    @NonNull
    private String value;

    @Builder
    public StatisticReqDTO(Date start, Date end, @NonNull String hashtag, String value, String[] type) throws CustomException {
        try {

            LocalDateTime endDateTime = null;
            LocalDateTime startDateTime = null;

            endDateTime = DateUtils.convertDateToLocalDateTime(end,23,59,59);

            if(start == null ){
                startDateTime = endDateTime.minusDays(7);

            }else{
                startDateTime = DateUtils.convertDateToLocalDateTime(start,0,0,0);
            }

            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
            this.hashtag = hashtag;
            this.value = value == null? StatisticValue.COUNT.getValue() : value;
            this.types = Arrays.stream(type).toList();
        }catch (NullPointerException e){
            throw new CustomException(ErrorCode.REQUIED_PARAM);
        }
    }
}
