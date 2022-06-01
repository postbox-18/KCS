package com.example.kcs.Fragment.Profile.MyOrders.MyOrdersItems;

public class MyOrdersList {
    private final String header;
    private final int size;
    public MyOrdersList( String header,  int size) {
        this.header = header;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public String getHeader() {
        return header;
    }
}
