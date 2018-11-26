package com.codicalnetworks.e_commerceui.Models;

/**
 * Created by ADETOBA on 10/27/2018.
 */

public class Cart {
    String name;
    int price;
    int totalPrice;
    String category;
    String imageLink;
    String key;
    int quantity;

    public Cart(String name, int price, String category, String imageLink, String key, int quantity, int totalPrice) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageLink = imageLink;
        this.key = key;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Cart() {


    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
