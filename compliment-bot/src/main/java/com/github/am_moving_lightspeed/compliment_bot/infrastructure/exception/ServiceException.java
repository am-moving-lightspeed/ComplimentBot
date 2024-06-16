package com.github.am_moving_lightspeed.compliment_bot.infrastructure.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final String code;

    public ServiceException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }
}
