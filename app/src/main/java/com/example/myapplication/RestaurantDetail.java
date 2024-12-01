package com.example.myapplication;

import java.util.List;

public class RestaurantDetail {
    private String name;
    private String address;
    private String rating;
    private String imageUrl;
    private String url;
    private List<UserComment> userComments;



    public RestaurantDetail(String name, String address, String rating, String imageUrl, String url, List<UserComment> userComments) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.url = url;
        this.userComments = userComments;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<UserComment> getUserComments() {
        return userComments;
    }

    public void setUserComments(List<UserComment> userComments) {
        this.userComments = userComments;
    }

    public List<UserComment> getComments() {
        return userComments;
    }
}
