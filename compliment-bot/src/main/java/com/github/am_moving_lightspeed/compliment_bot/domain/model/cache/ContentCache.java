package com.github.am_moving_lightspeed.compliment_bot.domain.model.cache;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentCache {

    private Set<ComplimentCache> pendingCompliments;
    private Set<Integer> usedComplimentsHashes;
    private Set<UserCache> users;

    public Set<ComplimentCache> getPendingCompliments() {
        if (pendingCompliments == null) {
            pendingCompliments = new HashSet<>();
        }
        return pendingCompliments;
    }

    public Set<Integer> getUsedComplimentsHashes() {
        if (usedComplimentsHashes == null) {
            usedComplimentsHashes = new HashSet<>();
        }
        return usedComplimentsHashes;
    }

    public Set<UserCache> getUsers() {
        if (users == null) {
            users = new HashSet<>();
        }
        return users;
    }
}
