package com.github.am_moving_lightspeed.compliment_bot.util;

import com.github.am_moving_lightspeed.compliment_bot.domain.model.Compliment;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@UtilityClass
public class ContentProviderUtils {

    public static Set<Compliment> toContentSet(Elements elements, Function<String, String> contentTextFormatter) {
        var contentSet = new HashSet<Compliment>();
        if (elements != null) {
            elements.stream()
                    .filter(Element::hasText)
                    .map(Element::text)
                    .map(contentTextFormatter)
                    .map(Compliment::new)
                    .forEach(contentSet::add);
        }
        return contentSet;
    }
}
