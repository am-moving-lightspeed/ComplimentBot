package com.github.am_moving_lightspeed.compliment_bot.domain.model.event;

import org.springframework.context.ApplicationEvent;

public class UserSubscribedEvent extends ApplicationEvent {

    public UserSubscribedEvent(Object source) {
        super(source);
    }
}
