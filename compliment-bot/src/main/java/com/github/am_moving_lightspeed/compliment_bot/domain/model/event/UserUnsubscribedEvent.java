package com.github.am_moving_lightspeed.compliment_bot.domain.model.event;

import org.springframework.context.ApplicationEvent;

public class UserUnsubscribedEvent extends ApplicationEvent {

    public UserUnsubscribedEvent(Object source) {
        super(source);
    }
}
