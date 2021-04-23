package edu.neu.madcourseworkupteam.workup;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;

@IgnoreExtraProperties
class User {

    String firstName;
    String lastName;
    String username;
    Long totalPoints;
    Long totalSteps;
    String email;
    SettingConfiguration settingConfiguration;
    HashMap<String, String> favorites;
    List<String> friends;

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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Long totalPoints) {
        this.totalPoints = totalPoints;
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

    public SettingConfiguration getSettingConfiguration() {
        return settingConfiguration;
    }

    public void setSettingConfiguration(SettingConfiguration settingConfiguration) {
        this.settingConfiguration = settingConfiguration;
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
}