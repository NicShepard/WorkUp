package edu.neu.madcourseworkupteam.workup;


public class ExerciseCard {

    private int imageSource;
    private String imageID;

    public ExerciseCard(int videoID) {
        this.imageSource = videoID;
    }

    public ExerciseCard(int videoID, String ID) {
        this.imageSource = videoID;
        this.imageID = ID;
    }

    public int getImageSource() { return imageSource; }

    public String getImageID() { return imageID; }
}