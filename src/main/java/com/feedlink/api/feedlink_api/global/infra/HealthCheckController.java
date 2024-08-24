package com.feedlink.api.feedlink_api.global.infra;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Value("${server.env}")
    private String env;
    @Value("${server.port}")
    private String port;
    @Value("${server.serverAddress}")
    private String serverAddress;
    @Value("${server.serverName}")
    private String serverName;

    @GetMapping("/env")
    public ResponseEntity<String> getEnv() {
        return ResponseEntity.ok(env);
    }

    @GetMapping("/hc")
    public ResponseEntity<?> heathCheck() {
        Map<String, String> responseData = new HashMap<>();
        responseData.put("env", env);
        responseData.put("port", port);
        responseData.put("serverName", serverName);
        responseData.put("serverAddress", serverAddress);

        return ResponseEntity.ok(responseData);
    }
}
