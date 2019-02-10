package com.example.sheikhrashid.fyp;

import java.util.ArrayList;

/**
 * Created by rashid on 5/14/2017.
 */

public class UserDataClass
{
    String id, name, email, password, phone, pic;
    ArrayList<UserDataClass> arr = new ArrayList<>();

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public ArrayList<UserDataClass> getArr() {
        return arr;
    }

    public void setArr(ArrayList<UserDataClass> arr) {
        this.arr = arr;
    }
}
