package com.example.pizza_appproject;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private long orderID;
    private String email;
    private String orderDate;
    private double totalPrice;
    private String extras;
    private String notes;
    private List<OrderItem> orderItems;
    private long orderId;
    private String orderTime;
    private String customerName;

    public Order(String email, String orderDate, double totalPrice, String extras, String notes, long orderId, String orderTime, String customerName) {
        this.email = email;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.extras = extras;
        this.notes = notes;
        this.orderId = orderId;
        this.orderTime=orderTime;
        this.customerName = customerName;
        this.orderItems = new ArrayList<>();
    }
    public Order(String email, String orderDate, double totalPrice, String extras, String notes, String orderTime, String customerName) {
        this.email = email;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.extras = extras;
        this.notes = notes;
        this.orderTime = orderTime;
        this.customerName = customerName;
        this.orderItems = new ArrayList<>();
    }

    // Getters and setters...
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
