package com.github.am_moving_lightspeed.compliment_bot.config;

import com.github.am_moving_lightspeed.compliment_bot.converter.Converter;
import com.github.am_moving_lightspeed.compliment_bot.converter.ConverterDecorator;
import com.github.am_moving_lightspeed.compliment_bot.converter.cache.ComplimentToComplimentCacheConverter;
import com.github.am_moving_lightspeed.compliment_bot.converter.cache.ContentToContentCacheConverter;
import com.github.am_moving_lightspeed.compliment_bot.converter.cache.UserToUserCacheConverter;
import com.github.am_moving_lightspeed.compliment_bot.converter.integration.UpdateToRequestConverter;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.Content;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.User;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.Request;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.cache.ComplimentCache;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.cache.ContentCache;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.cache.UserCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

@Configuration(proxyBeanMethods = false)
public class ConverterConfiguration {

    @Bean
    public Converter<Content, ContentCache> contentConverter(Converter<Compliment, ComplimentCache> complimentConverter,
                                                             Converter<User, UserCache> userConverter) {
        return new ConverterDecorator<>(new ContentToContentCacheConverter(complimentConverter, userConverter));
    }

    @Bean
    public Converter<Compliment, ComplimentCache> complimentConverter() {
        return new ConverterDecorator<>(new ComplimentToComplimentCacheConverter());
    }

    @Bean
    public Converter<User, UserCache> userConverter() {
        return new ConverterDecorator<>(new UserToUserCacheConverter());
    }

    @Bean
    public Converter<Update, Request> updateToRequestConverter() {
        return new ConverterDecorator<>(new UpdateToRequestConverter());
    }
}
