package com.github.am_moving_lightspeed.compliment_bot.infrastructure.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import com.github.am_moving_lightspeed.compliment_bot.config.model.BotProperties;
import com.github.am_moving_lightspeed.compliment_bot.domain.service.bot.BotService;
import lombok.Setter;

@Setter
public class MessageSendingAppender extends AppenderBase<ILoggingEvent> {

    private BotProperties botProperties;
    private BotService botService;
    private Encoder<ILoggingEvent> encoder;

    /**
     * This method's purpose is to attempt to notify the admin about any
     * occurred error. If it fails to do so, no handling required.
     */
    @Override
    protected void append(ILoggingEvent event) {
        try {
            var encoded = encoder.encode(event);
            var message = new String(encoded);
            var chatId = botProperties.getAdmin().getId();
            botService.sendMessage(chatId, message, true);
        } catch (Throwable t) {
            // Intentionally swallowing any exception
        }
    }
}
