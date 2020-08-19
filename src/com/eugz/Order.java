package com.eugz;

import java.util.Random;

public class Order {
    private int price;
    private int size;
    private String orderType;
    private int orderID;

    public Order(int price, int size, String orderType) {
        this.price = price;
        this.size = size;
        this.orderType = orderType;
        this.orderID = new Random().nextInt(Integer.MAX_VALUE);
    }

    public int getPrice() {
        return price;
    }

    public int getSize() {
        return size;
    }

    public void decreaseSize() {
        this.size--;
    }

    public String getOrderType() {
        return orderType;
    }

    public int getOrderID() {
        return orderID;
    }

    public String toString() {
        return "Price: " + price + "; Size: " + size + "; Type: " + orderType;
    }
}
