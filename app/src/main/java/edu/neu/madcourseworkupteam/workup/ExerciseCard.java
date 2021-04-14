package edu.neu.madcourseworkupteam.workup;


import android.text.BoringLayout;

public class ExerciseCard implements ItemClickListener {

    private int imageSource;
    private String videoName;
    private String videoDesc;
    private Boolean isChecked;


    public ExerciseCard(int videoID) {
        this.imageSource = videoID;
    }

    public ExerciseCard(int videoID, String ID) {
        this.imageSource = videoID;
    }

    public ExerciseCard(int videoID, String name, String desc, Boolean checked) {
        this.imageSource = videoID;
        this.videoName = name;
        this.videoDesc = desc;
        this.isChecked = checked;
    }

    public int getImageSource() { return imageSource; }

    public String getName() { return this.videoName; }

    public String getDesc() { return this.videoDesc; }

    public Boolean getStatus() { return this.isChecked; }

    // Clicking on somewhere within item
    @Override
    public void onItemClick(int position) {
        // should actually take the user to the web page they entered
        return;
    }

    @Override
    public void onCheckBoxClick(int position) {
        isChecked = !isChecked;
    }
}