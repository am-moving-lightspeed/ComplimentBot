package com.github.am_moving_lightspeed.compliment_bot.domain.model.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandHandlerResult {

    private String message;

    public static CommandHandlerResult empty() {
        return new CommandHandlerResult();
    }

    public boolean isEmpty() {
        return message == null;
    }
}
