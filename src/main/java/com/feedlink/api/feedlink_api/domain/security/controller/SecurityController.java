package com.feedlink.api.feedlink_api.domain.security.controller;

import com.feedlink.api.feedlink_api.domain.security.dto.ReissueRequest;
import com.feedlink.api.feedlink_api.domain.security.service.SecurityService;
import com.feedlink.api.feedlink_api.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse<String>> reissue(@RequestBody ReissueRequest reissueRequest) {
        String token = securityService.reissue(reissueRequest);
        return new ResponseEntity<>(CommonResponse.ok(token), HttpStatus.CREATED) ;
    }
}
