package com.orczykowski.sport_radar.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TeamTest {

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    void shouldNotAllowToCreateTeamNameWithEmptyValue(String name) {
        //when
        final var exception = assertThrows(IllegalArgumentException.class, () -> new Team.TeamName(name));

        //then
        assertEquals("Team value cannot be null or blank", exception.getMessage());
    }

    @Test
    void shouldCreateTeam() {
        //when
        final var team = new Team(Team.TeamId.generate(), new Team.TeamName("Team A"));

        //then
        assertEquals("Team A", team.name().value());
    }

    @Test
    void shouldGenerateTeamId() {
        //when
        final var teamId = Team.TeamId.generate();

        //then
        assertNotNull(teamId);
        assertNotNull(teamId.id());
    }
}