package com.orczykowski.sport_radar.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FootballScoreBoardTest {
    private static final Team TEAM_A = new Team(Team.TeamId.generate(), new Team.TeamName("Team A"));
    private static final Team TEAM_B = new Team(Team.TeamId.generate(), new Team.TeamName("Team B"));
    private static final MatchId NON_EXISTING_MATCH_ID = MatchId.generateNew();

    FootballScoreBoard subject;

    @BeforeEach
    void setup() {
        subject = InMemoryLiveWorldCupFootballScoreBoard.create();
    }

    @Test
    void shouldInitEmptyScoreBoard() {
        //when
        final var scoreBoard = InMemoryLiveWorldCupFootballScoreBoard.create();

        //then
        Assertions.assertEquals(0, scoreBoard.numberOfMatches());
    }

    @Test
    void shouldNotAllowToAddToMatchesWithSameTeamsWhenOneIsAlreadyInProgress() {
        //given
        subject.addMatchBetween(TEAM_A, TEAM_B);

        //when
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> subject.addMatchBetween(TEAM_A, TEAM_B));

        //then
        Assertions.assertEquals("Match with given teams is already in progress", exception.getMessage());
    }

    @Test
    void shouldPrintScoreBoard() {
        //given
        final var id = subject.addMatchBetween(TEAM_A, TEAM_B);
        subject.scoreGoalHome(id);

        final var expectedScoreBoard = """
                LiveWorldCupFootballScoreBoard
                --------------------------------
                                
                Team A 1 - 0 Team B
                --------------------------------
                """;
        //when
        final var scoreBoard = subject.print();

        //then
        Assertions.assertEquals(expectedScoreBoard, scoreBoard);
    }

    @Test
    void shouldSortScoreBoardByResultAndDate() {
        //given

        Team teamA = new Team(Team.TeamId.generate(), new Team.TeamName("Team A"));
        Team teamB = new Team(Team.TeamId.generate(), new Team.TeamName("Team B"));
        Team teamC = new Team(Team.TeamId.generate(), new Team.TeamName("Team C"));
        Team teamD = new Team(Team.TeamId.generate(), new Team.TeamName("Team D"));
        Team teamE = new Team(Team.TeamId.generate(), new Team.TeamName("Team E"));
        Team teamF = new Team(Team.TeamId.generate(), new Team.TeamName("Team F"));
        final var id1 = subject.addMatchBetween(teamA, teamB);
        final var id2 = subject.addMatchBetween(teamC, teamD);
        final var id3 = subject.addMatchBetween(teamE, teamF);

        subject.scoreGoalHome(id2);
        subject.scoreGoalHome(id3);
        subject.scoreGoalHome(id3);
        subject.scoreGoalHome(id3);

        //when
        final var scoreBoard = subject.print();

        //then
        final var expectedScoreBoard = """
                LiveWorldCupFootballScoreBoard
                --------------------------------
                                
                Team E 3 - 0 Team F
                Team C 1 - 0 Team D
                Team A 0 - 0 Team B
                --------------------------------
                """;
        Assertions.assertEquals(expectedScoreBoard, scoreBoard);
    }

    @Test
    void shouldNotAllowToScoreHomeGoalForNonExistingMatch() {
        //when
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> subject.scoreGoalHome(NON_EXISTING_MATCH_ID));

        //then
        Assertions.assertEquals("Match not found", exception.getMessage());
    }

    @Test
    void shouldNotAllowToScoreHomeAwayForNonExistingMatch() {
        //when
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> subject.scoreGoalAway(NON_EXISTING_MATCH_ID));

        //then
        Assertions.assertEquals("Match not found", exception.getMessage());
    }

    @Test
    void shouldNotAllowToEndNonExistingMatch() {
        //when
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> subject.endMatch(NON_EXISTING_MATCH_ID));

        //then
        Assertions.assertEquals("Match not found", exception.getMessage());
    }

    @Test
    void shouldNotAllowToScoreGoalHomeForHomeTeamWhenMatchIsFinished() {
        //given
        final var id = subject.addMatchBetween(TEAM_A, TEAM_B);
        subject.endMatch(id);

        //when
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> subject.scoreGoalHome(id));

        //then
        Assertions.assertEquals("Match not found", exception.getMessage());
    }

    @Test
    void shouldNotAllowToScoreGoalHomeForAwayTeamWhenMatchIsFinished() {
        //given
        final var id = subject.addMatchBetween(TEAM_A, TEAM_B);
        subject.endMatch(id);

        //when
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> subject.scoreGoalAway(id));

        //then
        Assertions.assertEquals("Match not found", exception.getMessage());
    }

    @Test
    void shouldRemoveFinishedMatchFromScoreBoard() {
        //given
        final var id = subject.addMatchBetween(TEAM_A, TEAM_B);

        //when
        subject.endMatch(id);

        //then
        Assertions.assertEquals(0, subject.numberOfMatches());
    }

}