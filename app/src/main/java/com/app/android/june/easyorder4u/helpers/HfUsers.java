package com.app.android.june.easyorder4u.helpers;


import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class HfUsers {
    private String Name;
    private String Gender;
    private String Email;
    private String Profile_pic;
    private String Occupation;
    private String UserID;
    private String Phone;
    private String State;
    private String Lga;
    private String Country;
    private String Hf_deliver;
    private String HomeAddress;
    private String Status;
    private String Id;



    public HfUsers() {

    }
    public HfUsers(String name, String gender, String email, String profile_pic, String occupation, String userID, String phone, String state, String lga, String country, String hf_deliver, String homeAddress, String status, String id) {
        Name = name;
        Gender = gender;
        Email = email;
        Profile_pic = profile_pic;
        Occupation = occupation;
        UserID = userID;
        Phone = phone;
        State = state;
        Lga = lga;
        Country = country;
        Hf_deliver = hf_deliver;
        HomeAddress = homeAddress;
        Status = status;
        Id = id;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getProfile_pic() {
        return Profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        Profile_pic = profile_pic;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getLga() {
        return Lga;
    }

    public void setLga(String lga) {
        Lga = lga;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getHf_deliver() {
        return Hf_deliver;
    }

    public void setHf_deliver(String hf_deliver) {
        Hf_deliver = hf_deliver;
    }

    public String getHomeAddress() {
        return HomeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        HomeAddress = homeAddress;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }



}


