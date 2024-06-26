package com.github.am_moving_lightspeed.compliment_bot.domain.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Setter;

@Setter
public class Content {

    private Set<Compliment> pendingCompliments;
    private Set<Integer> usedComplimentsHashes;
    private Set<User> users;

    public Set<Compliment> getPendingCompliments() {
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

    public Set<User> getUsers() {
        if (users == null) {
            users = new HashSet<>();
        }
        return users;
    }
}
