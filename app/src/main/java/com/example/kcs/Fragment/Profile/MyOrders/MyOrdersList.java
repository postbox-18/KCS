package com.example.kcs.Fragment.Profile.MyOrders;

public class MyOrdersList {
    private final String   func,  header;
    private final int size;
    public MyOrdersList(String func, String header,  int size) {

        this.func = func;
        this.header = header;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public String getFunc() {
        return func;
    }

    public String getHeader() {
        return header;
    }
}
