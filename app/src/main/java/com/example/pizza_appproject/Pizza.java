package com.example.pizza_appproject;

import android.os.Parcel;
import android.os.Parcelable;

public class Pizza implements Parcelable{
    private String name;
    private String category;
    private String size;
    private double smallPrice;
    private double mediumPrice;
    private double largePrice;
    private boolean favorite;
    private String discription;

    public Pizza(String name, String category, String size, double smallPrice, double mediumPrice , double largePrice, String discription) {
        this.name = name;
        this.category = category;
        this.size=size;
        this.smallPrice=smallPrice;
        this.mediumPrice=mediumPrice;
        this.largePrice=largePrice;
        this.discription = discription;
    }
    public Pizza(){
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getSize() {
        return size;
    }
    public void setSmallPrice(double smallPrice) {
        this.smallPrice = smallPrice;
    }
    public double getSmallPrice() {
        return smallPrice;
    }
    public void setMediumPrice(double mediumPrice) {
        this.mediumPrice = mediumPrice;
    }
    public double getMediumPrice() {
        return mediumPrice;
    }
    public void setLargePrice(double largePrice) {
        this.largePrice = largePrice;
    }
    public double getLargePrice() {
        return largePrice;
    }

    public boolean isFavorite() {
        return favorite;
    }
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
    protected Pizza(Parcel in) {
        name = in.readString();
        discription = in.readString();
        smallPrice = in.readDouble();
        mediumPrice = in.readDouble();
        largePrice = in.readDouble();
        size = in.readString();
        favorite = in.readByte() != 0;
    }

    public static final Creator<Pizza> CREATOR = new Creator<Pizza>() {
        @Override
        public Pizza createFromParcel(Parcel in) {
            return new Pizza(in);
        }

        @Override
        public Pizza[] newArray(int size) {
            return new Pizza[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(discription);
        dest.writeDouble(smallPrice);
        dest.writeDouble(mediumPrice);
        dest.writeDouble(largePrice);
        dest.writeString(size);
        dest.writeByte((byte) (favorite ? 1 : 0));
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", size=" + size + '\'' +
                ", small price=" + smallPrice +
                ", medium price=" + mediumPrice +
                ", large price=" + largePrice +
                '}';
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}


