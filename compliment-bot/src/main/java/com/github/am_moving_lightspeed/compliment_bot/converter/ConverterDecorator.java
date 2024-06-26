package com.github.am_moving_lightspeed.compliment_bot.converter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConverterDecorator<S, D> implements Converter<S, D> {

    private final Converter<S, D> delegate;

    @Override
    public D convert(S source) {
        if (source == null) {
            return null;
        }
        return delegate.convert(source);
    }

    @Override
    public S convertBack(D source) {
        if (source == null) {
            return null;
        }
        return delegate.convertBack(source);
    }
}
