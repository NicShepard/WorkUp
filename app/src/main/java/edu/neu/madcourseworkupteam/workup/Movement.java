package edu.neu.madcourseworkupteam.workup;

import java.util.HashMap;
import java.util.List;

public class Movement {

    enum Difficulty {EASY, MEDIUM, HARD};

    public String title;
    public String description;
    public Difficulty difficulty;
    public String videoURL;
    HashMap<String, List<String>> userViews;


    public Movement(String title, String description, Difficulty difficulty, String videoURL, HashMap<String, List<String>> userViews) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.videoURL = videoURL;
        this.userViews = userViews;
    }

    public Movement() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public HashMap<String,List<String>> getUserViews() {
        return userViews;
    }

    public void setUserViews(HashMap<String,List<String>> userViews) {
        this.userViews = userViews;
    }
}
