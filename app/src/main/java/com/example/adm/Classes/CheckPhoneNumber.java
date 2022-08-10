package com.example.adm.Classes;

public class CheckPhoneNumber {
    private final String phone_number,boolen;
    public CheckPhoneNumber(String phone_number, String boolen) {
        this.phone_number = phone_number;
        this.boolen = boolen;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getBoolen() {
        return boolen;
    }
}
