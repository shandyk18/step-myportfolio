package com.google.sps.data;

/**
 * Class representing the login status of a user
 */
public class LoginStatus {

    private Boolean status;
    // Link is for log in or log out depending on status
    private String link;

    public LoginStatus(Boolean status, String link) {
        this.status = status;
        this.link = link;
    }
}