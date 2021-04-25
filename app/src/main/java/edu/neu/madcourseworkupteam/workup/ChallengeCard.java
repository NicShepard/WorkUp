package edu.neu.madcourseworkupteam.workup;

public class ChallengeCard implements ItemClickListener {

    private String dates;
    private String challengeName;
    private String friends;

    public ChallengeCard(String dates, String name) {
        this.dates = dates;
        this.challengeName = name;
    }

    public String getDate() { return this.dates; }

    public String getChallengeName() { return this.challengeName; }

    public String getFriends() { return this.friends; }

    // Clicking on somewhere within item
    @Override
    public void onItemClick(int position) {
        // should actually take the user to the web page they entered
        return;
    }

    @Override
    public void onCheckBoxClick(int position) {
        return;
    }

}