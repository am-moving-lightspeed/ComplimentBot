package com.github.am_moving_lightspeed.compliment_bot.converter.persistence;

import com.github.am_moving_lightspeed.compliment_bot.converter.Converter;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import com.github.am_moving_lightspeed.compliment_bot.persistence.model.ComplimentDao;

public class ComplimentToComplimentDaoConverter implements Converter<Compliment, ComplimentDao> {

    @Override
    public ComplimentDao convert(Compliment source) {
        var complimentDao = new ComplimentDao();
        complimentDao.setContent(source.getContent());
        complimentDao.setHash(source.hashCode());
        return complimentDao;
    }

    @Override
    public Compliment reverseConvert(ComplimentDao source) {
        return new Compliment(source.getContent());
    }
}
