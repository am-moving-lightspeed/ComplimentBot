package com.github.am_moving_lightspeed.compliment_bot.config;

import com.github.am_moving_lightspeed.compliment_bot.converter.Converter;
import com.github.am_moving_lightspeed.compliment_bot.converter.ConverterDecorator;
import com.github.am_moving_lightspeed.compliment_bot.converter.persistence.ComplimentToComplimentDaoConverter;
import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import com.github.am_moving_lightspeed.compliment_bot.persistence.model.ComplimentDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfiguration {

    @Bean
    public Converter<Compliment, ComplimentDao> complimentConverter() {
        return new ConverterDecorator<>(new ComplimentToComplimentDaoConverter());
    }
}
