package edu.neu.madcourseworkupteam.workup;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;

@IgnoreExtraProperties
public class Movement {

    public String title;
    public String description;
    public String difficulty;
    public String videoURL;
    public String type;


    public Movement(String title, String description, String difficulty, String videoURL, String type) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.videoURL = videoURL;
        this.type = type;
    }

    public Movement() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
}
