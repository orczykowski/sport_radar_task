package com.orczykowski.sport_radar.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class MatchTest {

    private static final Team TEAM_A = new Team(Team.TeamId.generate(), new Team.TeamName("Team A"));
    private static final Team TEAM_B = new Team(Team.TeamId.generate(), new Team.TeamName("Team B"));

    @Test
    void shouldInitEmptyMatch() {
        //when
        final var match = Match.begin(TEAM_A, TEAM_B);

        //then
        final var snapshot = match.snapshot();
        assertEquals(TEAM_A.name().value(), snapshot.homeTeamName());
        assertEquals(TEAM_B.name().value(), snapshot.awayTeamName());
        assertEquals(0, snapshot.homeTeamGoals());
        assertEquals(0, snapshot.awayTeamGoals());
        assertNotNull(snapshot.startTime());
        assertNull(snapshot.endTime());
    }

    @Test
    void shouldScoreGoalHome() {
        //given
        final var match = Match.begin(TEAM_A, TEAM_B);

        //when
        final var updatedMatch = match.scoreGoalHome();

        //then
        final var snapshot = updatedMatch.snapshot();
        assertEquals(1, snapshot.homeTeamGoals());
        assertEquals(0, snapshot.awayTeamGoals());
    }

    @Test
    void shouldScoreGoalAway() {
        //given
        final var match = Match.begin(TEAM_A, TEAM_B);

        //when
        final var updatedMatch = match.scoreGoalAway();

        //then
        final var snapshot = updatedMatch.snapshot();
        assertEquals(0, snapshot.homeTeamGoals());
        assertEquals(1, snapshot.awayTeamGoals());
    }

    @Test
    void shouldEndMatch() {
        //given
        final var match = Match.begin(TEAM_A, TEAM_B);

        //when
        final var updatedMatch = match.end();

        //then
        final var snapshot = updatedMatch.snapshot();
        assertNotNull(snapshot.endTime());
    }

    @Test
    void shouldNotAllowToChangeHomeTeamResultWhenMatchIsFinished() {
        //given
        final var match = Match.begin(TEAM_A, TEAM_B).end();

        //when
        final var exception = Assertions.assertThrows(IllegalStateException.class, match::scoreGoalHome);

        //then
        assertEquals("Match is already finished", exception.getMessage());
    }

    @Test
    void shouldNotAllowToChangeAwayTeamResultWhenMatchIsFinished() {
        //given
        final var match = Match.begin(TEAM_A, TEAM_B).end();

        //when
        final var exception = Assertions.assertThrows(IllegalStateException.class, match::scoreGoalAway);

        //then
        assertEquals("Match is already finished", exception.getMessage());
    }

    @Test
    void shouldNotAllowToFinishMatchWhenMatchIsFinished() {
        //given
        final var match = Match.begin(TEAM_A, TEAM_B).end();

        //when
        final var exception = Assertions.assertThrows(IllegalStateException.class, match::end);

        //then
        assertEquals("Match is already finished", exception.getMessage());
    }

    @Test
    void shouldPrintFinalResult() {
        //given
        final var match = Match.begin(TEAM_A, TEAM_B).scoreGoalHome().scoreGoalAway().end();

        //when
        final var result = match.printResult();

        //then
        assertEquals("Team A 1 - 1 Team B", result);
    }

    @Test
    void shouldNotAllowToCreateMatchWithoutHomeTeams() {
        //when
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> Match.begin(null, TEAM_A));

        //then
        assertEquals("Teams cannot be null", exception.getMessage());
    }

    @Test
    void shouldNotAllowToCreateMatchWithoutAwayTeams() {
        //when
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> Match.begin(TEAM_A, null));

        //then
        assertEquals("Teams cannot be null", exception.getMessage());
    }

    @Test
    void shouldNotAllowToCreateMatchOneTeam() {
        //when
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> Match.begin(TEAM_A, TEAM_A));

        //then
        assertEquals("Teams cannot be the same", exception.getMessage());
    }

    @Test
    void shouldCancelGoalHome() {
        //given
        final var match = Match.begin(TEAM_A, TEAM_B).scoreGoalHome();

        //when
        final var updatedMatch = match.cancelGoalHome();

        //then
        final var snapshot = updatedMatch.snapshot();
        assertEquals(0, snapshot.homeTeamGoals());
        assertEquals(0, snapshot.awayTeamGoals());
    }

    @Test
    void shouldCancelGoalAway() {
        //given
        final var match = Match.begin(TEAM_A, TEAM_B).scoreGoalAway();

        //when
        final var updatedMatch = match.cancelGoalAway();

        //then
        final var snapshot = updatedMatch.snapshot();
        assertEquals(0, snapshot.homeTeamGoals());
        assertEquals(0, snapshot.awayTeamGoals());
    }

    @Test
    void shouldNotAllowToCancelGoalHomeWhenMatchIsFinished() {
        //given
        final var match = Match.begin(TEAM_A, TEAM_B).scoreGoalHome().end();

        //when
        final var exception = Assertions.assertThrows(IllegalStateException.class, match::cancelGoalHome);

        //then
        assertEquals("Match is already finished", exception.getMessage());
    }

    @Test
    void shouldNotAllowToCancelGoalAwayWhenMatchIsFinished() {
        //given
        final var match = Match.begin(TEAM_A, TEAM_B).scoreGoalAway().end();

        //when
        final var exception = Assertions.assertThrows(IllegalStateException.class, match::cancelGoalAway);

        //then
        assertEquals("Match is already finished", exception.getMessage());
    }
}