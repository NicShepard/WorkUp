package edu.neu.madcourseworkupteam.workup;


import java.util.Date;

public class ChallengeCard {

    private Date date;
    private String placement;
    private String friends;

    public ChallengeCard(Date date, String place, String friends) {
        this.date = date;
        this.placement = place;
        this.friends = friends;
    }

    public Date getDate() { return this.date; }

    public String getPlacement() { return this.placement; }
}