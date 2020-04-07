package com.linkedin.drelephant.analysis;

/**
 * Created by shubham.j on 04/04/18.
 */
public class JobAnalysis {

    private final Severity severity;
    private final HeuristicDetails heuristicDetails;

    public JobAnalysis(HeuristicDetails heuristicDetails, Severity severity) {
        this.heuristicDetails = heuristicDetails;
        this.severity = severity;
    }

    public Severity getSeverity() {
        return severity;
    }

    public HeuristicDetails getHeuristicDetails() {
        return heuristicDetails;
    }
}
