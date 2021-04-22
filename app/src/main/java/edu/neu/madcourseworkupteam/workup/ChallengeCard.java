package edu.neu.madcourseworkupteam.workup;

public class ChallengeCard {

    private String dates;
    private String challengeName;
    private String friends;

    public ChallengeCard(String dates, String name, String friends) {
        this.dates = dates;
        this.challengeName = name;
        this.friends = friends;
    }

    public String getDate() { return this.dates; }

    public String getChallengeName() { return this.challengeName; }

    public String getFriends() { return this.friends; }
}