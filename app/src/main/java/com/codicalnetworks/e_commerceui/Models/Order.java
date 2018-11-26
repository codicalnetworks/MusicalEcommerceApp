package com.codicalnetworks.e_commerceui.Models;

/**
 * Created by ADETOBA on 11/5/2018.
 */

public class Order {
    private String name;
    private String address;
    private String city;
    private String zone;
    private String country;
    private String postCode;
    private String id;
    private String cartId;
    private String items;
    private String quantity;
    private String totalPrice;

    public Order(String name, String address, String city, String zone, String postCode, String id, String cartId, String items, String quantity, String totalPrice, String country) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.zone = zone;
        this.postCode = postCode;
        this.id = id;
        this.cartId = cartId;
        this.items = items;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.country = country;
    }

    public Order() {

    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
