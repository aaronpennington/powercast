package com.penningtonb.powercast;

class User {
    private UserBody user;
    private String credential;
    private String operationType;
    private String error;

    public UserBody getUser() {
        return user;
    }
    public String getError() {return error;}
}

class UserBody {
    private String uid;
    private String email;

    public String getUid() {
        return uid;
    }
    public String getEmail() {
        return email;
    }
}
