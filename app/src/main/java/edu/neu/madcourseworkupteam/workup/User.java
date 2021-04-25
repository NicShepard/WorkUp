package edu.neu.madcourseworkupteam.workup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class User {

    String firstName;
    String lastName;
    String username;
    Long totalSteps;
    String email;
    HashMap<String, String> favorites;
    List<String> friends;
    Map<String, Challenge> activeChallenges;
    Map<String, Challenge> pastChallenges;
    Long stepGoal;
    HashMap<String, Object> streak;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getStepGoal() { return this.stepGoal; }

    public void setStepGoal(Long stepGoal) { this.stepGoal = stepGoal; }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(Long totalSteps) {
        this.totalSteps = totalSteps;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Challenge> getActiveChallenges() {
        return activeChallenges;
    }

    public void setActiveChallenges(Map<String, Challenge> activeChallenges) {
        this.activeChallenges = activeChallenges;
    }

    public Map<String, Challenge> getPastChallenges() {
        return pastChallenges;
    }

    public void setPastChallenges(Map<String, Challenge> pastChallenges) {
        this.pastChallenges = pastChallenges;
    }

    public HashMap<String, String> getFavorites() {
        return favorites;
    }

    public void setFavorites(HashMap<String, String> favorites) {
        this.favorites = favorites;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public void setStreak(HashMap<String, Object> streak) {
        this.streak = streak;
    }

    public HashMap<String, Object> getStreak() {
        return streak;
    }
}