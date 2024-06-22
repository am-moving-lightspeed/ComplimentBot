package com.github.am_moving_lightspeed.compliment_bot.config;

import static ch.qos.logback.classic.Level.ERROR;
import static ch.qos.logback.core.spi.FilterReply.DENY;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Logging.PATTERN;
import static java.nio.charset.StandardCharsets.UTF_8;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.encoder.Encoder;
import com.github.am_moving_lightspeed.compliment_bot.config.model.BotProperties;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.bot.BotService;
import com.github.am_moving_lightspeed.compliment_bot.infrastructure.logging.MessageSendingAppender;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LoggerConfiguration {

    @Bean
    public MessageSendingAppender messageSendingAppender(BotService botService, BotProperties botProperties) {
        var messageSendingAppender = new MessageSendingAppender();
        var loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        var rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        var patternEncoder = createPatternEncoder(loggerContext);
        var errorLevelFilter = createErrorLevelFilter();

        messageSendingAppender.setBotService(botService);
        messageSendingAppender.setBotProperties(botProperties);
        messageSendingAppender.setContext(loggerContext);
        messageSendingAppender.setEncoder(patternEncoder);
        messageSendingAppender.addFilter(errorLevelFilter);
        messageSendingAppender.start();

        rootLogger.addAppender(messageSendingAppender);
        return messageSendingAppender;
    }

    private LevelFilter createErrorLevelFilter() {
        var levelFilter = new LevelFilter();
        levelFilter.setLevel(ERROR);
        levelFilter.setOnMismatch(DENY);
        levelFilter.start();
        return levelFilter;
    }

    private Encoder<ILoggingEvent> createPatternEncoder(Context context) {
        var encoder = new PatternLayoutEncoder();
        encoder.setCharset(UTF_8);
        encoder.setPattern(PATTERN);
        encoder.setContext(context);
        encoder.start();
        return encoder;
    }
}
