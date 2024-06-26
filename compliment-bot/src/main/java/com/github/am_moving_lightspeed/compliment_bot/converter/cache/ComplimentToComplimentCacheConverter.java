package com.github.am_moving_lightspeed.compliment_bot.converter.cache;

import com.github.am_moving_lightspeed.compliment_bot.converter.Converter;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.cache.ComplimentCache;

public class ComplimentToComplimentCacheConverter implements Converter<Compliment, ComplimentCache> {

    @Override
    public ComplimentCache convert(Compliment source) {
        var complimentDao = new ComplimentCache();
        complimentDao.setContent(source.getContent());
        complimentDao.setHash(source.hashCode());
        return complimentDao;
    }

    @Override
    public Compliment convertBack(ComplimentCache source) {
        return new Compliment(source.getContent());
    }
}
