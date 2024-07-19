package com.example.pizza_appproject;

public class OrderItem {
    private long orderItemID;
    private long orderID;
    private String name;
    private String size;
    private int quantity;
    private double price;

    public OrderItem(String name, String size, int quantity, double price) {
        this.name = name;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
    }
    public OrderItem(){

    }

    // Getters and setters
    public long getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(long orderItemID) {
        this.orderItemID = orderItemID;
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}