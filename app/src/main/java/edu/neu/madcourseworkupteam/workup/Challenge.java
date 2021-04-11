package edu.neu.madcourseworkupteam.workup;

import java.util.List;
import java.util.Map;

public class Challenge {

    String title;
    Boolean active;
    String startDate;
    String endDate;
    Map<String, Long> userPoints;
    List<String> userPlacement;

    public Challenge() {
    }

    public Challenge(String title, Boolean active, String startDate, String endDate, Map<String, Long> userPoints, List<String> userPlacement) {
        this.title = title;
        this.active = active;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userPoints = userPoints;
        this.userPlacement = userPlacement;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Map<String, Long> getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(Map<String, Long> userPoints) {
        this.userPoints = userPoints;
    }

    public List<String> getUserPlacement() {
        return userPlacement;
    }

    public void setUserPlacement(List<String> userPlacement) {
        this.userPlacement = userPlacement;
    }
}
