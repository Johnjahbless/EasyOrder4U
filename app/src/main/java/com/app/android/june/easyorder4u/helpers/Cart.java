package com.app.android.june.easyorder4u.helpers;

public class Cart {

    private String foodName;
    private String foodPrice;
    private String foodPhoto;
    private String foodLikes;
    private String foodId;
    private String foodTags;
    private String foodDesc;
    private String shopName;
    private String shopCity;
    private String shopState;
    private String shopAddress;
    private String userID;
    private String shopID;
    private String foodPieces;
    private String Id;


    public Cart() {

    }
    public Cart(String foodName, String foodPrice, String foodPhoto, String foodLikes, String foodId, String foodTags, String foodDesc, String shopName, String shopCity, String shopState, String shopAddress, String userID, String shopID, String foodPieces, String id) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodPhoto = foodPhoto;
        this.foodLikes = foodLikes;
        this.foodId = foodId;
        this.foodTags = foodTags;
        this.foodDesc = foodDesc;
        this.shopName = shopName;
        this.shopCity = shopCity;
        this.shopState = shopState;
        this.shopAddress = shopAddress;
        this.userID = userID;
        this.shopID = shopID;
        this.foodPieces = foodPieces;
        Id = id;
    }



    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodPhoto() {
        return foodPhoto;
    }

    public void setFoodPhoto(String foodPhoto) {
        this.foodPhoto = foodPhoto;
    }

    public String getFoodLikes() {
        return foodLikes;
    }

    public void setFoodLikes(String foodLikes) {
        this.foodLikes = foodLikes;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodTags() {
        return foodTags;
    }

    public void setFoodTags(String foodTags) {
        this.foodTags = foodTags;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopCity() {
        return shopCity;
    }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getShopState() {
        return shopState;
    }

    public void setShopState(String shopState) {
        this.shopState = shopState;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getFoodPieces() {
        return foodPieces;
    }

    public void setFoodPieces(String foodPieces) {
        this.foodPieces = foodPieces;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }



}
