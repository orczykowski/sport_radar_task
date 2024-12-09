package com.orczykowski.sport_radar.task;

record MatchStats(Integer homeTeamGoals, Integer awayTeamGoals) {

    static MatchStats empty() {
        return new MatchStats(0, 0);
    }

    MatchStats scoreGoalHome() {
        return new MatchStats(homeTeamGoals + 1, awayTeamGoals);
    }

    MatchStats scoreGoalAway() {
        return new MatchStats(homeTeamGoals, awayTeamGoals + 1);
    }

    MatchStats cancelGoalHome() {
        if (homeTeamGoals == 0) {
            throw new IllegalStateException("Cannot cancel goal for home team, because they have not scored any goals yet");
        }
        return new MatchStats(homeTeamGoals - 1, awayTeamGoals);
    }

    MatchStats cancelGoalAway() {
        if (awayTeamGoals == 0) {
            throw new IllegalStateException("Cannot cancel goal for away team, because they have not scored any goals yet");
        }
        return new MatchStats(homeTeamGoals, awayTeamGoals - 1);
    }
}
