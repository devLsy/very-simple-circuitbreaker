package com.test.lsy.api1.service;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FallbackService {

    public String fallbackApi2(Throwable t) {
        if (t instanceof CallNotPermittedException) {
            log.warn("⚠️ CircuitBreaker OPEN 상태: 호출 차단됨. fallback 진입.");
        } else {
            log.warn("❌ API 호출 중 예외 발생: {}, fallback 진입.", t.toString());
        }
        return "API2 서버가 응답하지 않아 fallback 처리됨";
    }
}
