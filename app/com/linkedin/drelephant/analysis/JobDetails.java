package com.linkedin.drelephant.analysis;

public class JobDetails {

    private final String jobDefId;
    private final String jobName;
    private final String jobType;
    private final String userName;
    private final String queueName;
    private final String scheduler;
    private final long score;
    private final int JobRuns;
    private final long resourceUsed;
    private final long resourceWasted;
    private final long totalDelay;

    public JobDetails(String jobDefId, String jobName, String jobType, String userName, String queueName,
                      String scheduler, long score, int jobRuns, long resourceUsed,
                      long resourceWasted, long totalDelay){
        this.jobDefId = jobDefId;
        this.jobName = jobName;
        this.jobType = jobType;
        this.userName = userName;
        this.queueName = queueName;
        this.scheduler = scheduler;
        this.score = score;
        this.JobRuns = jobRuns;
        this.resourceUsed = resourceUsed;
        this.resourceWasted = resourceWasted;
        this.totalDelay = totalDelay;
    }

    public String getJobDefId() {
        return jobDefId;
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public String getUserName() {
        return userName;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getScheduler() {
        return scheduler;
    }

    public long getScore() {
        return score;
    }

    public int getJobRuns() {
        return JobRuns;
    }

    public long getResourceUsed() {
        return resourceUsed;
    }

    public long getResourceWasted() {
        return resourceWasted;
    }

    public long getTotalDelay() {
        return totalDelay;
    }
}