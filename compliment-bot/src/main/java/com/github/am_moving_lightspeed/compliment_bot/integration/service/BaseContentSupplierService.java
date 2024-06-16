package com.github.am_moving_lightspeed.compliment_bot.integration.service;

import static com.github.am_moving_lightspeed.compliment_bot.util.JsoupUtils.toContentSet;
import static java.util.function.Function.identity;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.Content;
import com.github.am_moving_lightspeed.compliment_bot.integration.client.BaseClientWrapper;
import java.util.Set;
import java.util.function.Function;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public abstract class BaseContentSupplierService implements ContentSupplierService {

    protected abstract BaseClientWrapper getClient();

    protected abstract Elements getContentElements(Document document);

    @Override
    public Set<Content> getContent() {
        var document = getClient().getContent();
        var contentElements = getContentElements(document);
        return toContentSet(contentElements, getContentFormatter());
    }

    protected Function<String, String> getContentFormatter() {
        return identity();
    }
}
