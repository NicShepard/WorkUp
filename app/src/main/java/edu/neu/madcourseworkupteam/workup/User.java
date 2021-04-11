package edu.neu.madcourseworkupteam.workup;

import com.google.firebase.database.IgnoreExtraProperties;

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
    List<String> favorites;
    List<String> friends;


    User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    SettingConfiguration getSettingConfiguration() {
        return settingConfiguration;
    }

    void setSettingConfiguration(SettingConfiguration settingConfiguration) {
        this.settingConfiguration = settingConfiguration;
    }

    User(String username, String email) {
        this.username = username;
        this.email = email;
        this.settingConfiguration = new SettingConfiguration();
    }

}