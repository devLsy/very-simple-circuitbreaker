
package com.test.lsy.api2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api2Controller {
    @GetMapping("/hello")
    public String hello() {
        return "API2 정상 응답";
    }
}
