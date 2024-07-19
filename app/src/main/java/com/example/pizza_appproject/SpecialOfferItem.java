package com.example.pizza_appproject;

public class SpecialOfferItem {
    private long offerId;
    private String pizzaName;
    private String size;
    private int quantity;

    public SpecialOfferItem(long offerId, String pizzaName, String size, int quantity) {
        this.offerId = offerId;
        this.pizzaName = pizzaName;
        this.size = size;
        this.quantity = quantity;
    }
    public SpecialOfferItem(String pizzaName, String size, int quantity) {
        this.pizzaName = pizzaName;
        this.size = size;
        this.quantity = quantity;
    }
    public long getOfferId() {
        return offerId;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
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
}
