package com.github.am_moving_lightspeed.compliment_bot.integration.service;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Integration.RomanticCollection.CONTENT_CONTAINER_CLASS_NAME;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Integration.RomanticCollection.CONTENT_ELEMENT_TAG;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Integration.RomanticCollection.CONTENT_LIST_TAG;
import static java.util.Optional.ofNullable;

import com.github.am_moving_lightspeed.compliment_bot.integration.client.BaseClientWrapper;
import com.github.am_moving_lightspeed.compliment_bot.integration.client.RomanticCollectionClientWrapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RomanticCollectionSupplierService extends BaseContentSupplierService {

    private final RomanticCollectionClientWrapper client;

    @Override
    protected BaseClientWrapper getClient() {
        return client;
    }

    @Override
    protected Elements getContentElements(Document document) {
        var elementsByClass = document.getElementsByClass(CONTENT_CONTAINER_CLASS_NAME);
        var contentList = elementsByClass.select(CONTENT_LIST_TAG)
                                         .first();
        return ofNullable(contentList).map(list -> list.getElementsByTag(CONTENT_ELEMENT_TAG))
                                      .orElse(new Elements());
    }
}
