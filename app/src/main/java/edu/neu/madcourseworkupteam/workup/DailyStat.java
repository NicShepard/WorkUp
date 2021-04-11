package edu.neu.madcourseworkupteam.workup;

import java.util.Map;

public class DailyStat {
    Map<String, Long> activitiesCompleted;
    Long pointsEarned;
    Long stepsTaken;

    public DailyStat() {
    }

    public DailyStat(Map<String, Long> activitiesCompleted, Long pointsEarned, Long stepsTaken) {
        this.activitiesCompleted = activitiesCompleted;
        this.pointsEarned = pointsEarned;
        this.stepsTaken = stepsTaken;
    }

    public Map<String, Long> getActivitiesCompleted() {
        return activitiesCompleted;
    }

    public void setActivitiesCompleted(Map<String, Long> activitiesCompleted) {
        this.activitiesCompleted = activitiesCompleted;
    }

    public Long getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(Long pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public Long getStepsTaken() {
        return stepsTaken;
    }

    public void setStepsTaken(Long stepsTaken) {
        this.stepsTaken = stepsTaken;
    }
}
