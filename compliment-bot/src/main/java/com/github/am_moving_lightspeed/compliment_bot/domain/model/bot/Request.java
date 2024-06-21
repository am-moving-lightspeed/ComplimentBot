package com.github.am_moving_lightspeed.compliment_bot.domain.model.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Request {

    private String command;
    private Long userId;
}
