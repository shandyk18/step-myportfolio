package com.google.sps.data;

/**
 * Class representing a single comment from a user
 */
public class Comment {

    private String name;
    private String text;
    private String image;

    public Comment(String name, String text, String image) {
        this.name = name;
        this.text = text;
        this.image = image;
    }
}