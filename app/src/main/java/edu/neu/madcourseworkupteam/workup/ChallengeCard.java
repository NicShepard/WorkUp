package edu.neu.madcourseworkupteam.workup;

public class ChallengeCard implements ItemClickListener {

    private String dates;
    private String challengeName;
    private String friends;
    private String challengeID;

    public ChallengeCard(String dates, String name) {
        this.dates = dates;
        this.challengeName = name;
    }

    public ChallengeCard(String dates, String name, String ID) {
        this.dates = dates;
        this.challengeName = name;
        this.challengeID = ID;
    }

    public String getDate() { return this.dates; }

    public String getChallengeName() { return this.challengeName; }

    public String getChallengeID() { return this.challengeID; }

    public void setChallengeID(String ID) { this.challengeID = ID; }

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