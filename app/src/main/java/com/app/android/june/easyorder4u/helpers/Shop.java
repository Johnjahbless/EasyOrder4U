package com.app.android.june.easyorder4u.helpers;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Shop {
    private String ShopName;
    private String ShopAddress;
    private String ShopNumber;
    private String ShopCity;
    private String ShopStreet;
    private String ShopAcctName;
    private String ShopAcctNumber;
    private String ShopCoverage;
    private String ShopPrice;
    private String ShopMinPrice;
    private String ShopOrder;
    private String ShopType;
    private String ShopBank;
    private String ShopState;
    private String ShopID;
    private String ShopPic;
    private String User_id;
    private Double ShopLikes;
    private String Id;


    public Shop() {
    }

    public Shop(String shopName, String shopAddress, String shopNumber, String shopCity, String shopStreet, String shopAcctName, String shopAcctNumber, String shopCoverage, String shopPrice, String shopMinPrice, String shopOrder, String shopType, String shopBank, String shopState, String shopID, String shopPic, String userID, Double shopLikes, String id) {
        ShopName = shopName;
        ShopAddress = shopAddress;
        ShopNumber = shopNumber;
        ShopCity = shopCity;
        ShopStreet = shopStreet;
        ShopAcctName = shopAcctName;
        ShopAcctNumber = shopAcctNumber;
        ShopCoverage = shopCoverage;
        ShopPrice = shopPrice;
        ShopMinPrice = shopMinPrice;
        ShopOrder = shopOrder;
        ShopType = shopType;
        ShopBank = shopBank;
        ShopState = shopState;
        ShopID = shopID;
        ShopPic = shopPic;
        User_id = userID;
        ShopLikes = shopLikes;
        Id = id;
    }



    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getShopAddress() {
        return ShopAddress;
    }

    public void setShopAddress(String shopAddress) {
        ShopAddress = shopAddress;
    }

    public String getShopNumber() {
        return ShopNumber;
    }

    public void setShopNumber(String shopNumber) {
        ShopNumber = shopNumber;
    }

    public String getShopCity() {
        return ShopCity;
    }

    public void setShopCity(String shopCity) {
        ShopCity = shopCity;
    }

    public String getShopStreet() {
        return ShopStreet;
    }

    public void setShopStreet(String shopStreet) {
        ShopStreet = shopStreet;
    }

    public String getShopAcctName() {
        return ShopAcctName;
    }

    public void setShopAcctName(String shopAcctName) {
        ShopAcctName = shopAcctName;
    }

    public String getShopAcctNumber() {
        return ShopAcctNumber;
    }

    public void setShopAcctNumber(String shopAcctNumber) {
        ShopAcctNumber = shopAcctNumber;
    }

    public String getShopCoverage() {
        return ShopCoverage;
    }

    public void setShopCoverage(String shopCoverage) {
        ShopCoverage = shopCoverage;
    }

    public String getShopPrice() {
        return ShopPrice;
    }

    public void setShopPrice(String shopPrice) {
        ShopPrice = shopPrice;
    }

    public String getShopMinPrice() {
        return ShopMinPrice;
    }

    public void setShopMinPrice(String shopMinPrice) {
        ShopMinPrice = shopMinPrice;
    }

    public String getShopOrder() {
        return ShopOrder;
    }

    public void setShopOrder(String shopOrder) {
        ShopOrder = shopOrder;
    }

    public String getShopType() {
        return ShopType;
    }

    public void setShopType(String shopType) {
        ShopType = shopType;
    }

    public String getShopBank() {
        return ShopBank;
    }

    public void setShopBank(String shopBank) {
        ShopBank = shopBank;
    }

    public String getShopState() {
        return ShopState;
    }

    public void setShopState(String shopState) {
        ShopState = shopState;
    }

    public String getShopID() {
        return ShopID;
    }

    public void setShopID(String shopID) {
        ShopID = shopID;
    }

    public String getShopPic() {
        return ShopPic;
    }

    public void setShopPic(String shopPic) {
        ShopPic = shopPic;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String userID) {
        User_id = userID;
    }

    public Double getShopLikes() {
        return ShopLikes;
    }

    public void setShopLikes(Double shopLikes) {
        ShopLikes = shopLikes;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

}