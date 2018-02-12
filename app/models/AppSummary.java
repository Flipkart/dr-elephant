package models;

import java.util.LinkedHashMap;
import java.util.Map;

public class AppSummary {
    private String jobDefId;
    private String jobName;
    private String jobType;
    private int stage;
    private int totalRuns;
    private int totalRunsWithIssues;
    private int totalIssues;
    private int criticalRuns;
    private int severeRuns;
    private int moderateRuns;
    private int lowRuns;
    private int normalRuns;

    private Map<String, AppHeuristicSummary> appHeuristicSummaryMap;


    public AppSummary(){
        this.stage = 1;
        this.totalRuns = 0;
        this.totalRunsWithIssues = 0;
        this.criticalRuns = 0;
        this.severeRuns = 0;
        this.moderateRuns = 0;
        this.lowRuns = 0;
        this.normalRuns = 0;
        this.totalIssues = 0;

        this.appHeuristicSummaryMap = new LinkedHashMap<String, AppHeuristicSummary>();
    }

    public String getJobDefId() {
        return jobDefId;
    }

    public void setJobDefId(String jobDefId) {
        this.jobDefId = jobDefId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public int getTotalRunsWithIssues() {
        return totalRunsWithIssues;
    }

    public void setTotalRunsWithIssues(int totalRunsWithIssues) {
        this.totalRunsWithIssues = totalRunsWithIssues;
    }

    public int getCriticalRuns() {
        return criticalRuns;
    }

    public void setCriticalRuns(int criticalRuns) {
        this.criticalRuns = criticalRuns;
    }

    public int getSevereRuns() {
        return severeRuns;
    }

    public void setSevereRuns(int severeRuns) {
        this.severeRuns = severeRuns;
    }

    public int getModerateRuns() {
        return moderateRuns;
    }

    public void setModerateRuns(int moderateRuns) {
        this.moderateRuns = moderateRuns;
    }

    public int getLowRuns() {
        return lowRuns;
    }

    public void setLowRuns(int lowRuns) {
        this.lowRuns = lowRuns;
    }

    public int getNormalRuns() {
        return normalRuns;
    }

    public void setNormalRuns(int normalRuns) {
        this.normalRuns = normalRuns;
    }


    public int getTotalIssues() {
        return totalIssues;
    }

    public void setTotalIssues(int totalIssues) {
        this.totalIssues = totalIssues;
    }

    public Map<String, AppHeuristicSummary> getAppHeuristicSummaryMap() {
        return appHeuristicSummaryMap;
    }

    public void setAppHeuristicSummaryMap(Map<String, AppHeuristicSummary> appHeuristicSummaryMap) {
        this.appHeuristicSummaryMap = appHeuristicSummaryMap;
    }
}