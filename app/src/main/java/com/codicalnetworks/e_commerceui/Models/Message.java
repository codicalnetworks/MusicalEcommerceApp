package com.codicalnetworks.e_commerceui.Models;

/**
 * Created by ADETOBA on 10/31/2018.
 */

public class Message {
    String fullName;
    String email;
    String message;
    String key;

    public Message(String fullName, String email, String message, String key) {
        this.fullName = fullName;
        this.email = email;
        this.message = message;
        this.key = key;
    }

    public Message() {

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
