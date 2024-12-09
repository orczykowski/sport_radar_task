package com.orczykowski.sport_radar.task;

interface FootballScoreBoard {

    MatchId addMatchBetween(Team homeTeam, Team awayTeam);

    void scoreGoalHome(MatchId matchId);

    void scoreGoalAway(MatchId matchId);

    void endMatch(MatchId matchId);

    int numberOfMatches();

    String print();
}
