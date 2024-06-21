package com.github.am_moving_lightspeed.compliment_bot.persistence.model;

import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
public class StorageDao {

    private Set<ComplimentDao> pendingCompliments;
    private Set<String> usedComplimentsHashes;
    private Set<UserDao> users;

    public Set<ComplimentDao> getPendingCompliments() {
        if (pendingCompliments == null) {
            pendingCompliments = new HashSet<>();
        }
        return pendingCompliments;
    }

    public Set<String> getUsedComplimentsHashes() {
        if (usedComplimentsHashes == null) {
            usedComplimentsHashes = new HashSet<>();
        }
        return usedComplimentsHashes;
    }

    public Set<UserDao> getUsers() {
        if (users == null) {
            users = new HashSet<>();
        }
        return users;
    }
}
