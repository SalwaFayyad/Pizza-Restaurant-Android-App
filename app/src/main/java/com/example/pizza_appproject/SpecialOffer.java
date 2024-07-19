package com.example.pizza_appproject;
import java.util.List;
public class SpecialOffer {
    private long offerId;
    private String name;
    private String startDate;
    private String endDate;
    private double totalPrice;
    private String extras;
    private List<SpecialOfferItem> specialOfferItems;

    public SpecialOffer(long offerId, String name, String startDate, String endDate, double totalPrice, String extras) {
        this.offerId = offerId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.extras = extras;
    }
    public SpecialOffer(String name, String startDate, String endDate, double totalPrice, String extras) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.extras = extras;
    }

    public long getOfferId() {
        return offerId;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<SpecialOfferItem> getSpecialOfferItems() {
        return specialOfferItems;
    }

    public void setSpecialOfferItems(List<SpecialOfferItem> specialOfferItems) {
        this.specialOfferItems = specialOfferItems;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }
}

