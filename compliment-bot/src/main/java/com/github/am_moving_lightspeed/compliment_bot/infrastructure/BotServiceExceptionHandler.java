package com.github.am_moving_lightspeed.compliment_bot.infrastructure;

import com.github.am_moving_lightspeed.compliment_bot.infrastructure.exception.BotApiException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class BotServiceExceptionHandler {

    @AfterThrowing(pointcut = "execution(public * *..BotService.*(..))",
                   throwing = "throwable")
    public void afterThrowing(Throwable throwable) {
        if (throwable instanceof BotApiException exception) {
            log.error(exception.getCode(), exception);
        }
    }
}
