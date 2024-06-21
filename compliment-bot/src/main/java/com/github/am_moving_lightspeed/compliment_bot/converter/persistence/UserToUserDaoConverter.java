package com.github.am_moving_lightspeed.compliment_bot.converter.persistence;

import com.github.am_moving_lightspeed.compliment_bot.converter.Converter;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.User;
import com.github.am_moving_lightspeed.compliment_bot.persistence.model.UserDao;

public class UserToUserDaoConverter implements Converter<User, UserDao> {

    @Override
    public UserDao convert(User source) {
        return new UserDao(source.getId());
    }

    @Override
    public User reverseConvert(UserDao source) {
        return new User(source.getId());
    }
}
