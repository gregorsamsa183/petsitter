package com.batech.app.petsitter.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TR21718 on 8.06.2017.
 */

public class Profiles {
    private String  user_id;
    private int     age;
    private String  name;
    private String  surname;
    private String  username;
    private String  gender;
    private String  password;
    private String  country;
    private String  town;
    private String  district;
    private String  picture_url;

    public Profiles() {}

    public Profiles(String name, String surname, String user_id, String username) {
        this.user_id = user_id;
        this.name = name;
        this.surname = surname;
        this.username = username;
    }


    public String getPicture_url() {return picture_url;}

    public void setPicture_url(String picture_url) {this.picture_url = picture_url;}

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_id", user_id);
        result.put("name", name);
        result.put("surname", surname);
        result.put("username", username);

        return result;
    }

}
