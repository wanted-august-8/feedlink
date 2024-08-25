package com.feedlink.api.feedlink_api.domain.statistic.repository;


import com.feedlink.api.feedlink_api.domain.statistic.dto.StatisticCondition;
import com.feedlink.api.feedlink_api.domain.statistic.request.StatisticReqDTO;
import com.feedlink.api.feedlink_api.domain.statistic.dto.StatisticResultDTO;
import java.util.List;

public interface StatisticRepositoryCustom {
    List<StatisticResultDTO> getPostStatistics(StatisticCondition statisticReqDTO);

}
