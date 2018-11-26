package com.codicalnetworks.e_commerceui.Adapters;

/**
 * Created by ADETOBA on 11/2/2018.
 */

public class Users {
    String name;
    String email;
    String telephone;
    String address;

    public Users(String name, String email, String telephone, String address) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.address = address;
    }

    public Users () {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
