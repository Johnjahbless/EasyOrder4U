package com.app.android.june.easyorder4u.helpers;


import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Food {

    private String ShopName;
    private String ShopAddress;
    private String ProductName;
    private String ProductCity;
    private String ProductDesc;
    private String ProductAvail;
    private String ProductBook;
    private String ProductPic;
    private String ProductPrice;
    private String ProductState;
    private String User_id;
    private String ShopId;
    private String ProductTime;
    private String FoodTags;
    private Double ProductLikes;
    private String Created_date;
    private String Id;



    public Food() {

    }
    public Food(String shopName, String shopAddress, String productName, String productCity, String productDesc, String productAvail, String productBook, String productPic, String productPrice, String productState, String user_id, String shopId, String productTime, String foodTags, Double productLikes, String created_date, String id) {
        ShopName = shopName;
        ShopAddress = shopAddress;
        ProductName = productName;
        ProductCity = productCity;
        ProductDesc = productDesc;
        ProductAvail = productAvail;
        ProductBook = productBook;
        ProductPic = productPic;
        ProductPrice = productPrice;
        ProductState = productState;
        User_id = user_id;
        ShopId = shopId;
        ProductTime = productTime;
        FoodTags = foodTags;
        ProductLikes = productLikes;
        Created_date = created_date;
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

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductCity() {
        return ProductCity;
    }

    public void setProductCity(String productCity) {
        ProductCity = productCity;
    }

    public String getProductDesc() {
        return ProductDesc;
    }

    public void setProductDesc(String productDesc) {
        ProductDesc = productDesc;
    }

    public String getProductAvail() {
        return ProductAvail;
    }

    public void setProductAvail(String productAvail) {
        ProductAvail = productAvail;
    }

    public String getProductBook() {
        return ProductBook;
    }

    public void setProductBook(String productBook) {
        ProductBook = productBook;
    }

    public String getProductPic() {
        return ProductPic;
    }

    public void setProductPic(String productPic) {
        ProductPic = productPic;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductState() {
        return ProductState;
    }

    public void setProductState(String productState) {
        ProductState = productState;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String shopId) {
        ShopId = shopId;
    }

    public String getProductTime() {
        return ProductTime;
    }

    public void setProductTime(String productTime) {
        ProductTime = productTime;
    }

    public String getFoodTags() {
        return FoodTags;
    }

    public void setFoodTags(String foodTags) {
        FoodTags = foodTags;
    }

    public Double getProductLikes() {
        return ProductLikes;
    }

    public void setProductLikes(Double productLikes) {
        ProductLikes = productLikes;
    }

    public String getCreated_date() {
        return Created_date;
    }

    public void setCreated_date(String created_date) {
        Created_date = created_date;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }


}
