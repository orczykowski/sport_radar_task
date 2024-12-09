package com.orczykowski.sport_radar.task;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryLiveWorldCupFootballScoreBoard implements FootballScoreBoard {
    private final Map<MatchId, Match> board;

    public InMemoryLiveWorldCupFootballScoreBoard(final Map<MatchId, Match> board) {
        this.board = board;
    }

    static InMemoryLiveWorldCupFootballScoreBoard create() {
        return new InMemoryLiveWorldCupFootballScoreBoard(new ConcurrentHashMap<>());
    }

    @Override
    public MatchId addMatchBetween(final Team homeTeam, final Team awayTeam) {
        if (existMatch(homeTeam, awayTeam)) {
            throw new IllegalArgumentException("Match with given teams is already in progress");
        }
        final var match = Match.begin(homeTeam, awayTeam);
        board.put(match.id(), match);
        return match.id();
    }

    @Override
    public void scoreGoalHome(final MatchId matchId) {
        final var match = board.get(matchId);
        if (Objects.isNull(match)) {
            throw new IllegalArgumentException("Match not found");
        }
        final var updatedMatch = match.scoreGoalHome();
        board.put(matchId, updatedMatch);
    }

    @Override
    public void scoreGoalAway(final MatchId matchId) {
        final var match = board.get(matchId);
        if (Objects.isNull(match)) {
            throw new IllegalArgumentException("Match not found");
        }
        final var updatedMatch = match.scoreGoalAway();
        board.put(matchId, updatedMatch);
    }

    @Override
    public int numberOfMatches() {
        return board.size();
    }


    @Override
    public void endMatch(final MatchId matchId) {
        final var match = board.get(matchId);
        if (Objects.isNull(match)) {
            throw new IllegalArgumentException("Match not found");
        }
        board.remove(matchId);
    }

    @Override
    public String print() {
        return "LiveWorldCupFootballScoreBoard\n"
                + "--------------------------------\n"
                + board.values().stream()
                .sorted((m1, m2) -> {
                    int scoreComparison = Integer.compare(
                            m2.getHomeTeamGoals() + m2.getAwayTeamGoals(),
                            m1.getHomeTeamGoals() + m1.getAwayTeamGoals()
                    );
                    if (scoreComparison == 0) {
                        return m2.startTime().compareTo(m1.startTime());
                    }
                    return scoreComparison;
                })
                .map(Match::printResult)
                .reduce("", (a, b) -> a + "\n" + b)
                + "\n--------------------------------\n";
    }

    private boolean existMatch(final Team homeTeam, final Team awayTeam) {
        return board.entrySet().stream()
                .anyMatch(match ->
                        match.getValue().homeTeam().equals(homeTeam) &&
                                match.getValue().awayTeam().equals(awayTeam) &&
                                !match.getValue().isFinished());
    }

}
