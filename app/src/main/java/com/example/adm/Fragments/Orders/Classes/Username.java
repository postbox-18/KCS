package com.example.adm.Fragments.Orders.Classes;

public class Username {
private final String username,phone_number;
    public Username(String username, String phone_number) {
        this.username = username;
        this.phone_number = phone_number;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone_number() {
        return phone_number;
    }
}
