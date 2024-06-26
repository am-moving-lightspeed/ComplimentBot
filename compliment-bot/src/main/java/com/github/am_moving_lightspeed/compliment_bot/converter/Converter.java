package com.github.am_moving_lightspeed.compliment_bot.converter;

public interface Converter<S, D> {

    D convert(S source);

    S convertBack(D source);
}
