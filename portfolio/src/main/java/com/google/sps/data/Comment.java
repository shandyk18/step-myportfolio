package com.google.sps.data;

/**
 * Class representing a single comment from a user
 */
public class Comment {

    private String name;
    private String email;
    private String text;
    private String image;

    public Comment(String name, String email, String text, String image) {
        this.name = name;
        this.email = email;
        this.text = text;
        this.image = image;
    }
}