package com.github.am_moving_lightspeed.compliment_bot.util;

import static java.util.function.Function.identity;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CollectionUtils {

    public static <T> Set<T> toSet(Collection<T> collection) {
        return toSet(collection, identity());
    }

    public static <T, R> Set<R> toSet(Collection<T> collection, Function<T, R> mappingFunction) {
        if (collection != null) {
            return collection.stream()
                             .map(mappingFunction)
                             .collect(Collectors.toSet());
        }
        return null;
    }
}
