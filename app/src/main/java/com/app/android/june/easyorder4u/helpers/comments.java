package com.app.android.june.easyorder4u.helpers;

public class comments {

    private String Name;
    private String Date;
    private String Email;
    private String Gender;
    private String Comments;
    private String Photo;
    private String UserId;
    private String Id;

    public comments() {

    }
    public comments(String name, String date, String email, String gender, String comments, String photo, String userId, String id) {
        Name = name;
        Date = date;
        Email = email;
        Gender = gender;
        Comments = comments;
        Photo = photo;
        UserId = userId;
        Id = id;
    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }





}
