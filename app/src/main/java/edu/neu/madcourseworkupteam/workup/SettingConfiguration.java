package edu.neu.madcourseworkupteam.workup;

public class SettingConfiguration {

    Boolean notifications;
    String privacy;

    public Boolean getNotifications() {
        return notifications;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public SettingConfiguration() {
        //Default constructor
        notifications = true;
        privacy = "Friends";
    }
}
