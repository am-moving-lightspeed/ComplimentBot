package com.github.am_moving_lightspeed.compliment_bot.persistence.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ComplimentDao {

    private Integer hash;
    private String content;
}
