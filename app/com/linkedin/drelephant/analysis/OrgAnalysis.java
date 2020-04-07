package com.linkedin.drelephant.analysis;

/**
 * Created by shubham.j on 04/04/18.
 */
public class OrgAnalysis {

    private final Severity severity;
    private final JobDetails jobDetails;

    public OrgAnalysis(JobDetails jobDetails, Severity severity) {
        this.jobDetails = jobDetails;
        this.severity = severity;
    }

    public Severity getSeverity() {
        return severity;
    }

    public JobDetails getJobDetails() {
        return jobDetails;
    }
}