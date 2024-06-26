package com.github.am_moving_lightspeed.compliment_bot.converter.cache;

import static com.github.am_moving_lightspeed.compliment_bot.util.CollectionUtils.toSet;

import com.github.am_moving_lightspeed.compliment_bot.converter.Converter;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.Content;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.User;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.cache.ComplimentCache;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.cache.ContentCache;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.cache.UserCache;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContentToContentCacheConverter implements Converter<Content, ContentCache> {

    private final Converter<Compliment, ComplimentCache> complimentConverter;
    private final Converter<User, UserCache> userConverter;

    @SuppressWarnings("DuplicatedCode")
    @Override
    public ContentCache convert(Content source) {
        var cache = new ContentCache();

        var pendingCompliments = toSet(source.getPendingCompliments(), complimentConverter::convert);
        var usedComplimentsHashes = toSet(source.getUsedComplimentsHashes());
        var users = toSet(source.getUsers(), userConverter::convert);

        cache.setPendingCompliments(pendingCompliments);
        cache.setUsedComplimentsHashes(usedComplimentsHashes);
        cache.setUsers(users);
        return cache;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public Content convertBack(ContentCache source) {
        var content = new Content();

        var pendingCompliments = toSet(source.getPendingCompliments(), complimentConverter::convertBack);
        var usedComplimentsHashes = toSet(source.getUsedComplimentsHashes());
        var users = toSet(source.getUsers(), userConverter::convertBack);

        content.setPendingCompliments(pendingCompliments);
        content.setUsedComplimentsHashes(usedComplimentsHashes);
        content.setUsers(users);
        return content;
    }
}
