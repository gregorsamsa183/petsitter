package com.batech.app.petsitter.model;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

// [START comment_class]
@IgnoreExtraProperties
public class Comment {

    public String uid;
    public String author;
    public String text;
    public String date;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text) {
        this.uid = uid;
        this.author = author;
        this.text = text;
        this.date = String.valueOf(new Date(System.currentTimeMillis()));
    }

}
// [END comment_class]
