package com.batech.app.petsitter.model;

import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String name;
    public String surname;
    public String fullname;
    public Uri uri;
    public String userid;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String name, String surname, String fullname) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.fullname= fullname;
    }
    public User(String username, String email, String name, String surname, String fullname, Uri uri) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.fullname= fullname;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getFullname() { return fullname; }

    public Uri getUri() { return uri; }

    public void setUri(Uri uri) { this.uri = uri; }

    public String getUserid() { return userid; }

    public void setUserid(String userid) { this.userid = userid; }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        if(fullname.trim().length()==0){ fullname = name+" "+surname; }

        result.put("user_id", userid);
        result.put("name", name);
        result.put("surname", surname);
        result.put("username", username);
        result.put("fullname", fullname);
        result.put("email", email);
        result.put("photoUri", uri.toString());
        return result;
    }

}
// [END blog_user_class]
