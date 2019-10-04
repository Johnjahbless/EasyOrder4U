package com.app.android.june.easyorder4u.helpers;


import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Orders {
    private String FoodName;
    private long Date;
    private String ShopName;
    private Double Likes;
    private String FoodPhoto;
    private String FoodDesc;
    private String FoodId;
    private Integer TotalPrice;
    private Integer FoodPieces;
    private String FoodTags;
    private String ShopId;
    private String PaymentMethod;
    private Integer Status;
    private String Time;
    private String UserId;
    private String HomeAddress;
    private String OrderId;
    private String UserName;
    private String UserPhone;
    private String UserEmail;
    private String UserPhoto;
    private String Id;

    public Orders(){

    }

    public Orders(String foodName, long date, String shopName, Double likes, String foodPhoto, String foodDesc, String foodId, Integer totalPrice, Integer foodPieces, String foodTags, String shopId, String paymentMethod, Integer status, String time, String userId, String homeAddress, String orderId,
                  String userName, String userPhone, String userEmail, String userPhoto, String id) {
        FoodName = foodName;
        Date = date;
        ShopName = shopName;
        Likes = likes;
        FoodPhoto = foodPhoto;
        FoodDesc = foodDesc;
        FoodId = foodId;
        TotalPrice = totalPrice;
        FoodPieces = foodPieces;
        FoodTags = foodTags;
        ShopId = shopId;
        PaymentMethod = paymentMethod;
        Status = status;
        Time = time;
        UserId = userId;
        HomeAddress = homeAddress;
        OrderId = orderId;
        UserName = userName;
        UserPhone = userPhone;
        UserEmail = userEmail;
        UserPhoto = userPhoto;
        Id = id;
    }

    

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public Double getLikes() {
        return Likes;
    }

    public void setLikes(Double likes) {
        Likes = likes;
    }

    public String getFoodPhoto() {
        return FoodPhoto;
    }

    public void setFoodPhoto(String foodPhoto) {
        FoodPhoto = foodPhoto;
    }

    public String getFoodDesc() {
        return FoodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        FoodDesc = foodDesc;
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }

    public Integer getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        TotalPrice = totalPrice;
    }

    public Integer getFoodPieces() {
        return FoodPieces;
    }

    public void setFoodPieces(Integer foodPieces) {
        FoodPieces = foodPieces;
    }

    public String getFoodTags() {
        return FoodTags;
    }

    public void setFoodTags(String foodTags) {
        FoodTags = foodTags;
    }

    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String shopId) {
        ShopId = shopId;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getHomeAddress() {
        return HomeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        HomeAddress = homeAddress;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName){
        UserName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        UserPhoto = userPhoto;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    
    
 
}
