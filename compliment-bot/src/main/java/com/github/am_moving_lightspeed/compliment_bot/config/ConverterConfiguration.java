package com.github.am_moving_lightspeed.compliment_bot.config;

import com.github.am_moving_lightspeed.compliment_bot.converter.Converter;
import com.github.am_moving_lightspeed.compliment_bot.converter.ConverterDecorator;
import com.github.am_moving_lightspeed.compliment_bot.converter.integration.UpdateToRequestConverter;
import com.github.am_moving_lightspeed.compliment_bot.converter.persistence.ComplimentToComplimentDaoConverter;
import com.github.am_moving_lightspeed.compliment_bot.converter.persistence.UserToUserDaoConverter;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.User;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.bot.Request;
import com.github.am_moving_lightspeed.compliment_bot.persistence.model.ComplimentDao;
import com.github.am_moving_lightspeed.compliment_bot.persistence.model.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

@Configuration(proxyBeanMethods = false)
public class ConverterConfiguration {

    @Bean
    public Converter<Compliment, ComplimentDao> complimentConverter() {
        return new ConverterDecorator<>(new ComplimentToComplimentDaoConverter());
    }

    @Bean
    public Converter<User, UserDao> userConverter() {
        return new ConverterDecorator<>(new UserToUserDaoConverter());
    }

    @Bean
    public Converter<Update, Request> updateToRequestConverter() {
        return new ConverterDecorator<>(new UpdateToRequestConverter());
    }
}
