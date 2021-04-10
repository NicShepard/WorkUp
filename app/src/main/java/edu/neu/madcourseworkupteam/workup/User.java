package edu.neu.madcourseworkupteam.workup;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public SettingConfiguration settingConfiguration;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.settingConfiguration = new SettingConfiguration();
    }

}