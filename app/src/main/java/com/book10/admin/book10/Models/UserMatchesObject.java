package com.book10.admin.book10.Models;

/**
 * Created by admin on 1/5/15.
 */
public class UserMatchesObject {
    private String userID;
    private int numberOfMatches;

    public UserMatchesObject(String userID, int numberOfMatches) {
        this.userID = userID;
        this.numberOfMatches = numberOfMatches;
    }

    public String getUserID() {
        return userID;
    }

    public int getNumberOfMatches() {
        return numberOfMatches;
    }
}
