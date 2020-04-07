package com.linkedin.drelephant.analysis;

public class HeuristicDetails {

    private final String heuristicName;
    private final long score;
    private final int criticalSeverity;
    private final int severeSeverity;
    private final int moderateSeverity;

    public HeuristicDetails(String heuristicName, long score, int criticalSeverity, int severeSeverity, int moderateSeverity) {
        this.heuristicName = heuristicName;
        this.score = score;
        this.criticalSeverity = criticalSeverity;
        this.severeSeverity = severeSeverity;
        this.moderateSeverity = moderateSeverity;

    }

    public String getHeuristicName() {
        return heuristicName;
    }

    public long getScore() {
        return score;
    }

    public int getCriticalSeverity() {
        return criticalSeverity;
    }

    public int getSevereSeverity() {
        return severeSeverity;
    }

    public int getModerateSeverity() {
        return moderateSeverity;
    }
}

