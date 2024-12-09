package com.orczykowski.sport_radar.task;

import java.util.UUID;

public record Team(TeamId id, TeamName name) {
    record TeamId(UUID id) {
        static TeamId generate() {
            return new TeamId(UUID.randomUUID());
        }
    }

    record TeamName(String value) {

        TeamName {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException("Team value cannot be null or blank");
            }
        }
    }
}
