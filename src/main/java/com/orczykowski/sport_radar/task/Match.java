package com.orczykowski.sport_radar.task;

import java.time.Instant;
import java.util.Objects;

class Match {
    private final MatchId id;
    private final Team homeTeam;
    private final Team awayTeam;
    private final MatchStats playStats;
    private final Instant startTime;
    private final Instant endTime;

    private Match(final MatchId id, final Team homeTeam, final Team awayTeam, final MatchStats playStats, final Instant startTime, final Instant endTime) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.playStats = playStats;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    static Match begin(final Team homeTeam, final Team awayTeam) {
        if (Objects.isNull(homeTeam) || Objects.isNull(awayTeam)) {
            throw new IllegalArgumentException("Teams cannot be null");
        }
        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Teams cannot be the same");
        }
        return new Match(MatchId.generateNew(), homeTeam, awayTeam, MatchStats.empty(), Instant.now(), null);
    }

    Match scoreGoalHome() {
        if (isFinished()) {
            throw new IllegalStateException("Match is already finished");
        }
        final var stats = playStats.scoreGoalHome();
        return new Match(id, homeTeam, awayTeam, stats, startTime, endTime);
    }

    Match scoreGoalAway() {
        if (isFinished()) {
            throw new IllegalStateException("Match is already finished");
        }
        final var stats = playStats.scoreGoalAway();
        return new Match(id, homeTeam, awayTeam, stats, startTime, endTime);
    }

    Match cancelGoalHome() {
        if (isFinished()) {
            throw new IllegalStateException("Match is already finished");
        }
        final var stats = playStats.cancelGoalHome();
        return new Match(id, homeTeam, awayTeam, stats, startTime, endTime);
    }

    Match cancelGoalAway() {
        if (isFinished()) {
            throw new IllegalStateException("Match is already finished");
        }
        final var stats = playStats.cancelGoalAway();
        return new Match(id, homeTeam, awayTeam, stats, startTime, endTime);
    }


    Match end() {
        if (isFinished()) {
            throw new IllegalStateException("Match is already finished");
        }
        return new Match(id, homeTeam, awayTeam, playStats, startTime, Instant.now());
    }

    boolean isFinished() {
        return Objects.nonNull(endTime);
    }

    String printResult() {
        return homeTeam.name().value() + " " + playStats.homeTeamGoals() + " - " + playStats.awayTeamGoals() + " " + awayTeam.name().value();
    }

    MatchSnapshot snapshot() {
        return MatchSnapshot.fromMatch(this);
    }

    MatchId id() {
        return id;
    }

    Team homeTeam() {
        return homeTeam;
    }

    Team awayTeam() {
        return awayTeam;
    }


    record MatchSnapshot(String homeTeamName, String awayTeamName,
                         Integer homeTeamGoals, Integer awayTeamGoals,
                         Instant startTime, Instant endTime) {
        static MatchSnapshot fromMatch(Match match) {
            return new MatchSnapshot(match.homeTeam.name().value(), match.awayTeam.name().value(), match.playStats.homeTeamGoals(), match.playStats.awayTeamGoals(), match.startTime, match.endTime);
        }
    }
}
