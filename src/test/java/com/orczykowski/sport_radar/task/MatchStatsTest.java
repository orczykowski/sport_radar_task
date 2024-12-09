package com.orczykowski.sport_radar.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatchStatsTest {

    @Test
    void shouldInitEmptyMatchStats() {
        //when
        MatchStats matchStats = MatchStats.empty();

        //then
        assertEquals(0, matchStats.homeTeamGoals());
        assertEquals(0, matchStats.awayTeamGoals());
    }

    @Test
    void shouldScoreGoalHome() {
        //given
        MatchStats matchStats = MatchStats.empty();

        //when
        MatchStats updatedMatchStats = matchStats.scoreGoalHome();

        //then
        assertEquals(1, updatedMatchStats.homeTeamGoals());
        assertEquals(0, updatedMatchStats.awayTeamGoals());
    }

    @Test
    void shouldScoreGoalAway() {
        //given
        MatchStats matchStats = MatchStats.empty();

        //when
        MatchStats updatedMatchStats = matchStats.scoreGoalAway();

        //then
        assertEquals(0, updatedMatchStats.homeTeamGoals());
        assertEquals(1, updatedMatchStats.awayTeamGoals());
    }

    @Test
    void shouldCancelScoreGoalHome() {
        //given
        MatchStats matchStats = MatchStats.empty().scoreGoalHome();

        //when
        MatchStats updatedMatchStats = matchStats.cancelGoalHome();

        //then
        assertEquals(0, updatedMatchStats.homeTeamGoals());
        assertEquals(0, updatedMatchStats.awayTeamGoals());
    }

    @Test
    void shouldCancelScoreGoalAway() {
        //given
        MatchStats matchStats = MatchStats.empty().scoreGoalAway();

        //when
        MatchStats updatedMatchStats = matchStats.cancelGoalAway();

        //then
        assertEquals(0, updatedMatchStats.homeTeamGoals());
        assertEquals(0, updatedMatchStats.awayTeamGoals());
    }

    @Test
    void shouldNotAllowToCancelGoalForHomeTeamWhenTheyHaveNotScoredAnyGoals() {
        //given
        MatchStats matchStats = MatchStats.empty();

        //when
        IllegalStateException exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, matchStats::cancelGoalHome);

        //then
        assertEquals("Cannot cancel goal for home team, because they have not scored any goals yet", exception.getMessage());
    }

    @Test
    void shouldNotAllowToCancelGoalForAwayTeamWhenTheyHaveNotScoredAnyGoals() {
        //given
        MatchStats matchStats = MatchStats.empty();

        //when
        IllegalStateException exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, matchStats::cancelGoalAway);

        //then
        assertEquals("Cannot cancel goal for away team, because they have not scored any goals yet", exception.getMessage());
    }

}