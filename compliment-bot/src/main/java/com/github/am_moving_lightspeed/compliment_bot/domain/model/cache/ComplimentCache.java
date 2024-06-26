package com.github.am_moving_lightspeed.compliment_bot.domain.model.cache;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ComplimentCache {

    private Integer hash;
    private String content;
}
