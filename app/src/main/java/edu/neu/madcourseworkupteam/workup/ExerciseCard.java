package edu.neu.madcourseworkupteam.workup;

public class ExerciseCard implements ItemClickListener {

    private String videoUrl;
    private String videoName;
    private String videoDesc;
    private Boolean isChecked;

    public ExerciseCard(String url, String name, String desc, Boolean checked) {
        this.videoUrl = url;
        this.videoName = name;
        this.videoDesc = desc;
        this.isChecked = checked;
    }

    public ExerciseCard() {
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = "https://www.youtube.com/watch?v=" + videoUrl;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getName() { return this.videoName; }

    public String getDesc() { return this.videoDesc; }

    public Boolean getStatus() { return this.isChecked; }

    public String getVideoUrl() { return this.videoUrl; }

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