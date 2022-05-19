package com.example.adm.Fragments.Users;

public class UserDetailsList {
    private final String username,  email, phone_number;
    public UserDetailsList(String username, String email, String phone_number) {
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_number() {
        return phone_number;
    }
}
