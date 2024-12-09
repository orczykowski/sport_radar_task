package com.orczykowski.sport_radar.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MatchIdTest {

    @Test
    void shouldGenerateNewMatchId() {
        //when
        final var matchId = MatchId.generateNew();

        //then
        assertNotNull(matchId.id());
    }

}