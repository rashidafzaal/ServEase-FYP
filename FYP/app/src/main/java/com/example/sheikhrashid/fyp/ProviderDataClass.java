package com.example.sheikhrashid.fyp;

import java.util.ArrayList;

/**
 * Created by rashid on 3/29/2017.
 */

public class ProviderDataClass
{
    String id, name, email, password, phone, service, address, details, JSON_longitude, JSON_latitude, pic;
    ArrayList<ProviderDataClass> arr = new ArrayList<>();

    public ArrayList<ProviderDataClass> getArr() {
        return arr;
    }

    public void setArr(ArrayList<ProviderDataClass> arr) {
        this.arr = arr;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getJSON_latitude() {
        return JSON_latitude;
    }

    public void setJSON_latitude(String JSON_latitude) {
        this.JSON_latitude = JSON_latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getJSON_longitude() {
        return JSON_longitude;
    }

    public void setJSON_longitude(String JSON_longitude) {
        this.JSON_longitude = JSON_longitude;
    }
}
