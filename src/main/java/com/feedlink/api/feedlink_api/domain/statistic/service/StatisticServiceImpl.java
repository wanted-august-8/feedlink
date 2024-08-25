package com.feedlink.api.feedlink_api.domain.statistic.service;

import com.feedlink.api.feedlink_api.domain.statistic.nums.StatisticType;
import com.feedlink.api.feedlink_api.domain.statistic.StatisticValidator;
import com.feedlink.api.feedlink_api.domain.statistic.dto.StatisticCondition;
import com.feedlink.api.feedlink_api.domain.statistic.dto.StatisticResultDTO;
import com.feedlink.api.feedlink_api.domain.statistic.request.StatisticReqDTO;
import com.feedlink.api.feedlink_api.domain.statistic.repository.StatisticRepository;
import com.feedlink.api.feedlink_api.domain.statistic.response.StatisticRes;
import com.feedlink.api.feedlink_api.global.common.CommonResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatisticServiceImpl implements StatisticService{

    private final StatisticRepository statisticRepository;

    @Override
    public CommonResponse<StatisticRes> getPostStatistics(StatisticReqDTO statisticReqDTO) {

        StatisticValidator.validateValueType(statisticReqDTO.getTypes(),statisticReqDTO.getValue());
        StatisticValidator.validateDate(statisticReqDTO.getTypes(),statisticReqDTO.getStartDateTime(),statisticReqDTO.getEndDateTime());

        List<StatisticResultDTO> dateStatistics = new ArrayList<>();
        List<StatisticResultDTO> hourStatistics = new ArrayList<>();

        for (String type : statisticReqDTO.getTypes()) {

            StatisticCondition condition = StatisticCondition.builder()
                .startDateTime(statisticReqDTO.getStartDateTime())
                .endDateTime(statisticReqDTO.getEndDateTime())
                .hashtag(statisticReqDTO.getHashtag())
                .target(statisticReqDTO.getValue())
                .byHour(type.equals(StatisticType.HOUR.getValue()))
                .build();

            List<StatisticResultDTO> postStatistics = statisticRepository.getPostStatistics(condition);

            if (type.equals(StatisticType.DATE.getValue())) {
                dateStatistics = postStatistics;
            } else {
                hourStatistics = postStatistics;
            }
        }

        StatisticRes res = StatisticRes
            .builder()
            .dateList(dateStatistics)
            .hourList(hourStatistics)
            .value(statisticReqDTO.getValue())
            .startDateTime(statisticReqDTO.getStartDateTime())
            .endDateTime(statisticReqDTO.getEndDateTime())
            .build();

        return CommonResponse.ok("통계 요청이 정상적으로 처리되었습니다.",res);

    }
}
