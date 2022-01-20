package com.cookandroid.registeration;

public class User {
    String id;
    static User instance;

    User() {
    }

    User(String id) {
        this.id = id;
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
