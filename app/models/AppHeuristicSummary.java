package models;

public class AppHeuristicSummary {
    private String heuristicName;
    private int criticalIssues;
    private int severeIssues;
    private int moderateIssues;
    private int lowIssues;
    private int normalIssues;

    public AppHeuristicSummary(){
        this.criticalIssues = 0;
        this.severeIssues = 0;
        this.moderateIssues = 0;
        this.lowIssues = 0;
        this.normalIssues = 0;
    }

    public String getHeuristicName() {
        return heuristicName;
    }

    public void setHeuristicName(String heuristicName) {
        this.heuristicName = heuristicName;
    }

    public int getCriticalIssues() {
        return criticalIssues;
    }

    public void setCriticalIssues(int criticalIssues) {
        this.criticalIssues = criticalIssues;
    }

    public int getSevereIssues() {
        return severeIssues;
    }

    public void setSevereIssues(int severeIssues) {
        this.severeIssues = severeIssues;
    }

    public int getModerateIssues() {
        return moderateIssues;
    }

    public void setModerateIssues(int moderateIssues) {
        this.moderateIssues = moderateIssues;
    }

    public int getLowIssues() {
        return lowIssues;
    }

    public void setLowIssues(int lowIssues) {
        this.lowIssues = lowIssues;
    }

    public int getNormalIssues() {
        return normalIssues;
    }

    public void setNormalIssues(int normalIssues) {
        this.normalIssues = normalIssues;
    }
}