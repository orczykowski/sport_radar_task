package com.orczykowski.sport_radar.task;

import java.util.UUID;

public record MatchId(String id) {
    static MatchId generateNew() {
        return new MatchId(UUID.randomUUID().toString());
    }
}
