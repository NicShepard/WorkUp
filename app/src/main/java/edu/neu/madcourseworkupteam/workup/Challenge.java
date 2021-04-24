package edu.neu.madcourseworkupteam.workup;

import java.util.List;
import java.util.Map;

/**
 * This class represents a challenge that multiple users are involved in
 */
public class Challenge {

    String title;
    Boolean active;
    String startDate;
    String endDate;
    Map<String, Long> userPoints;
    List<String> userPlacement;
    String pk;
    Boolean accepted;
    Integer placement;


    public Challenge() {
        // Default empty Constructor
    }

    public Challenge(String title, Boolean active, String startDate, String endDate, Map<String, Long> userPoints, List<String> userPlacement) {
        this.title = title;
        this.active = active;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userPoints = userPoints;
        this.userPlacement = userPlacement;
    }

    /**
     * Gets user's placement in that challenge
     * @return Integer value
     */
    public Integer getPlacement() {
        return placement;
    }

    /**
     * Sets the user's placement for that challenge
     * @param placement
     */
    public void setPlacement(Integer placement) {
        this.placement = placement;
    }

    /**
     * Gets the primary key
     * @return String
     */
    public String getPk() {
        return pk;
    }

    /**
     * Sets the primary Key
     * @param pk
     */
    public void setPk(String pk) {
        this.pk = pk;
    }

    /**
     * Checks if the user accepted the challenge
     * @return
     */
    public Boolean getAccepted() {
        return accepted;
    }

    /**
     * Sets whether the user accepted the challenge or not
     * @param accepted
     */
    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    /**
     * Gets the title of the challenge
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the challenge
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Checks if it is an active challenge currently in progress
     * @return
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets whether the challenge is currently active
     * @param active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     *
     * @return
     */
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
