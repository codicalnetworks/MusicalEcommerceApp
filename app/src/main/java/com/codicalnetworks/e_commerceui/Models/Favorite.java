package com.codicalnetworks.e_commerceui.Models;

/**
 * Created by ADETOBA on 10/27/2018.
 */

public class Favorite {
    String name;
    String description;
    String category;
    int price;
    String stock;
    String likes;
    String key;
    String imageLink;

    public Favorite(String name, String description, String category, int price, String stock, String likes, String key, String imageLink) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.likes = likes;
        this.key = key;
        this.imageLink = imageLink;
    }

    public Favorite() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
