package com.github.am_moving_lightspeed.compliment_bot.domain.model.cache;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentCache {

    private Set<ComplimentCache> pendingCompliments;
    private Set<Integer> usedComplimentsHashes;
    private Set<UserCache> users;
}
