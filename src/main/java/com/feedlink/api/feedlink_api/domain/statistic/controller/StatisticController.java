package com.feedlink.api.feedlink_api.domain.statistic.controller;

import com.feedlink.api.feedlink_api.domain.security.PrincipalDetails;
import com.feedlink.api.feedlink_api.domain.statistic.request.StatisticReqDTO;
import com.feedlink.api.feedlink_api.domain.statistic.response.StatisticRes;
import com.feedlink.api.feedlink_api.domain.statistic.service.StatisticService;
import com.feedlink.api.feedlink_api.global.common.CommonResponse;
import com.feedlink.api.feedlink_api.global.error.ErrorCode;
import com.feedlink.api.feedlink_api.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @GetMapping
    @Operation(summary = "해시태그 통계" ,description = "게시글 해시태그별 통계")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Request Success", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomException.class))),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomException.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = CustomException.class))),
        @ApiResponse(responseCode = "504", description = "Gateway Timeout", content = @Content(schema = @Schema(implementation = CustomException.class))),
    })
    public ResponseEntity<CommonResponse<StatisticRes>> getPostStatistic (
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam(value = "hashtag",required = false)
        @Schema(example = "카페", description="해시태그 없으면 로그인한 사용자 계정")
        String hashtag,
        @RequestParam("type")
        @ArraySchema(
            schema = @Schema(
                description = "통계 종류",
                example = "[\"date\", \"hour\"]"
            )
        )
        String[] type,
        @RequestParam(value = "start",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Schema(example = "2024-08-18",description="통계 시작일자")
        Date start,
        @RequestParam(value = "end",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @Schema(example = "2024-08-25",description="통계 종료일자")
        Date end,
        @RequestParam(value = "value",required = false)
        @Schema(example = "view_count",defaultValue = "count", description="통계 대상")
        String value) {
        try {

            StatisticReqDTO statisticReq = StatisticReqDTO.builder()
                .hashtag(hashtag==null?principalDetails.getMember().getMemberAccount():hashtag)
                .start(start)
                .end(end)
                .value(value)
                .type(type)
                .build();

            CommonResponse<StatisticRes> response = statisticService.getPostStatistics(
                statisticReq);

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));

        }catch (NullPointerException e){
            throw new CustomException(ErrorCode.REQUIRED_PARAM);
        }

    }
}
