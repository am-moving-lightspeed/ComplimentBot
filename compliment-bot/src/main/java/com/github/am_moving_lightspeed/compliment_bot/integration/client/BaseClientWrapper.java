package com.github.am_moving_lightspeed.compliment_bot.integration.client;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Errors.FAILED_TO_FETCH_CONTENT;

import com.github.am_moving_lightspeed.compliment_bot.infrastructure.exception.ServiceException;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class BaseClientWrapper {

    protected abstract String getUri();

    protected abstract int getReadTimeout();

    public Document getContent() {
        try {
            return Jsoup.connect(getUri())
                        .timeout(getReadTimeout())
                        .get();
        } catch (IOException exception) {
            throw new ServiceException(FAILED_TO_FETCH_CONTENT, exception);
        }
    }
}
