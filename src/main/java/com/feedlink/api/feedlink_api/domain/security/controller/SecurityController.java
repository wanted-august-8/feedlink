package com.feedlink.api.feedlink_api.domain.security.controller;

import com.feedlink.api.feedlink_api.domain.security.dto.ReissueRequest;
import com.feedlink.api.feedlink_api.domain.security.service.SecurityService;
import com.feedlink.api.feedlink_api.global.common.CommonResponse;
import com.feedlink.api.feedlink_api.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/security")
@AllArgsConstructor
public class SecurityController {

    private final SecurityService securityService;
    @Operation(summary = "토큰 재발행", description = "accessToken 만료 시 재발급합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Request Success"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse<String>> reissue(@RequestBody ReissueRequest reissueRequest) {
        String token = securityService.reissue(reissueRequest);
        return new ResponseEntity<>(CommonResponse.ok(token), HttpStatus.CREATED) ;
    }
}


//@Operation(summary = "토큰 재발행", description = "accessToken 만료 시 재발급합니다.")
//@ApiResponses(value = {
//    @ApiResponse(responseCode = "201", description = "Request Success", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
//    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = CustomException.class))),
//    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = CustomException.class))),
//    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomException.class))),
//    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = CustomException.class))),
//})