package com.feedlink.api.feedlink_api.domain.security.controller;

import com.feedlink.api.feedlink_api.domain.security.dto.ReissueRequest;
import com.feedlink.api.feedlink_api.domain.security.service.SecurityService;
import com.feedlink.api.feedlink_api.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class Controller {

    private final SecurityService securityService;

    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse<String>> reissue(@RequestBody ReissueRequest reissueRequest) {
        String token = securityService.reissue(reissueRequest);
        return new ResponseEntity<>(CommonResponse.ok(token), HttpStatus.CREATED) ;
    }
}
