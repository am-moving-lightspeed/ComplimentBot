package com.github.am_moving_lightspeed.compliment_bot.converter.cache;

import com.github.am_moving_lightspeed.compliment_bot.converter.Converter;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.User;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.cache.UserCache;

public class UserToUserCacheConverter implements Converter<User, UserCache> {

    @Override
    public UserCache convert(User source) {
        return new UserCache(source.getId());
    }

    @Override
    public User convertBack(UserCache source) {
        return new User(source.getId());
    }
}
