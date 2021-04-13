package edu.neu.madcourseworkupteam.workup;

import java.util.HashMap;
import java.util.List;

public class Movement {

//    enum Difficulty {EASY, MEDIUM, HARD};

    public String title;
    public String description;
    public String difficulty;
    public String videoURL;


    public Movement(String title, String description, String difficulty, String videoURL) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.videoURL = videoURL;
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
