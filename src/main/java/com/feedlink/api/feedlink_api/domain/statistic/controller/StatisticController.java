package com.feedlink.api.feedlink_api.domain.statistic.controller;

import com.feedlink.api.feedlink_api.domain.statistic.dto.StatisticResultDTO;
import com.feedlink.api.feedlink_api.domain.statistic.request.StatisticReqDTO;
import com.feedlink.api.feedlink_api.domain.statistic.response.StatisticRes;
import com.feedlink.api.feedlink_api.domain.statistic.service.StatisticService;
import com.feedlink.api.feedlink_api.global.common.CommonResponse;
import com.feedlink.api.feedlink_api.global.error.ErrorCode;
import com.feedlink.api.feedlink_api.global.exception.CustomException;
import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/statistics")
@Slf4j
public class StatisticController {

    private final StatisticService statisticService;

    /**
     * 통계 요청을 처리하는 엔드포인트입니다. 요청 조건을 검증하고 일치하는 게시글 통계 프로세스를 실행합니다.
     *
     * @return 통계 결과를 담은 CommonResponse 객체와 HTTP 상태 코드
     * @QueryString hashtag
     * @QueryString type
     * @QueryString start
     * @QueryString end
     * @QueryString value
     */
    @GetMapping()
    public ResponseEntity<CommonResponse<StatisticRes>> getPostStatistic (
        @RequestParam(value = "hashtag",required = false) String hashtag, @RequestParam("type") String[] type, @RequestParam(value = "start",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)Date start,
        @RequestParam(value = "end",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end, @RequestParam(value = "value",required = false) String value) {
        try {

            StatisticReqDTO statisticReq = StatisticReqDTO.builder()
                .hashtag(hashtag)
                .start(start)
                .end(end)
                .value(value)
                .type(type)
                .build();

            CommonResponse<StatisticRes> response = statisticService.getPostStatistics(
                statisticReq);

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));

        }catch (NullPointerException e){
            throw new CustomException(ErrorCode.REQUIED_PARAM);
        }

    }
}
