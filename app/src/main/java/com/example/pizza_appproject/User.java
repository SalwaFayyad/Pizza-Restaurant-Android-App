package com.example.pizza_appproject;

import android.database.Cursor;

import java.util.List;

public class User {
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String gender;
    private String password;
    private String hashPassword;
    private String permission;
    private byte[] profilePicture;
    public static Cursor currentUser;

    public User(String email,String firstName, String lastName, String phoneNumber,  String gender, String password, String hashPassword, String permission,byte[] profilePicture) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.password = password;
        this.hashPassword = hashPassword;
        this.permission = permission;
        this.profilePicture=profilePicture;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String hashPassword(String password) {
        return Hash.hashPassword(password);
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String gender) {
        this.permission = permission;
    }

    public String getHashPassword() {
        return hashPassword;
    }
    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public static void setCurrentUser(User user) {
        currentUser = user.currentUser;
    }
    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }


    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {

        return plainTextPassword.equals(hashedPassword);
    }
}
