
package com.test.lsy.api1;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Api1Controller {

    private final RestTemplate restTemplate;

    @GetMapping("/call-api2")
    @CircuitBreaker(name = "api2Circuit", fallbackMethod = "fallbackApi2")
    public String callApi2() {
        String url = "http://localhost:8082/hello";
        log.info("Calling API2 at {}", url);
        return restTemplate.getForObject(url, String.class);
    }

    public String fallbackApi2(Throwable t) {
        if (t instanceof CallNotPermittedException) {
            log.warn("⚠️ CircuitBreaker OPEN 상태: 호출 차단됨. fallback 진입.");
        } else {
            log.warn("❌ API 호출 중 예외 발생: {}, fallback 진입.", t.toString());
        }
        return "API2 서버가 응답하지 않아 fallback 처리됨";
    }
}
