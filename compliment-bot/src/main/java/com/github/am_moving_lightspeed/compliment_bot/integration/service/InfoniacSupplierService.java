package com.github.am_moving_lightspeed.compliment_bot.integration.service;

import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Integration.Infoniac.CONTENT_ELEMENT_TAG;
import static com.github.am_moving_lightspeed.compliment_bot.util.Constants.Integration.Infoniac.CONTENT_REGEX;

import com.github.am_moving_lightspeed.compliment_bot.integration.client.BaseClientWrapper;
import com.github.am_moving_lightspeed.compliment_bot.integration.client.InfoniacClientWrapper;
import java.util.function.Function;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InfoniacSupplierService extends BaseContentSupplierService {

    private static final Pattern CONTENT_PATTERN = Pattern.compile(CONTENT_REGEX);

    private final InfoniacClientWrapper client;

    @Override
    protected BaseClientWrapper getClient() {
        return client;
    }

    @Override
    protected Elements getContentElements(Document document) {
        var elementsByTag = document.getElementsByTag(CONTENT_ELEMENT_TAG);
        var contentElementsList = elementsByTag.stream()
                                               .filter(Element::hasText)
                                               .filter(element -> CONTENT_PATTERN.matcher(element.text()).matches())
                                               .toList();
        return new Elements(contentElementsList);
    }

    @Override
    protected Function<String, String> getContentFormatter() {
        return source -> {
            var matcher = CONTENT_PATTERN.matcher(source);
            return matcher.find() ? matcher.group(1) : source;
        };
    }
}
