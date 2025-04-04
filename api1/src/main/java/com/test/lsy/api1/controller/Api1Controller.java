
package com.test.lsy.api1.controller;

import com.test.lsy.api1.service.FallbackService;
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
    private final FallbackService fallbackService;

    @GetMapping("/call-api2")
    @CircuitBreaker(name = "api2Circuit", fallbackMethod = "fallbackApi2")
    public String callApi2() {
        String url = "http://localhost:8082/hello";
        log.info("Calling API2 at {}", url);
        return restTemplate.getForObject(url, String.class);
    }

    public String fallbackApi2(Throwable t) {
        return fallbackService.fallbackApi2(t);
    }
}
