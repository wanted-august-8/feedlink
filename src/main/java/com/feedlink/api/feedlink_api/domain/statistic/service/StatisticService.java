package com.feedlink.api.feedlink_api.domain.statistic.service;

import com.feedlink.api.feedlink_api.domain.statistic.dto.StatisticResultDTO;
import com.feedlink.api.feedlink_api.domain.statistic.request.StatisticReqDTO;
import com.feedlink.api.feedlink_api.domain.statistic.response.StatisticRes;
import com.feedlink.api.feedlink_api.global.common.CommonResponse;
import java.util.List;

public interface StatisticService {

    CommonResponse<StatisticRes> getPostStatistics(StatisticReqDTO statisticReqDTO);
}
