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