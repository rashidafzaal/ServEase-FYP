package com.example.sheikhrashid.fyp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rashid on 5/14/2017.
 */

public class UserJSONParser
{
    String id, name, email, password, phone, pic;
    ArrayList<UserDataClass> arr = new ArrayList<>();

    public ArrayList<UserDataClass> showJSON (String response)
    {

        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");


            for (int i = 0; i < result.length(); i++)
            {
                JSONObject Data = result.getJSONObject(i);

                id = Data.getString("id");
                name = Data.getString("name");
                email = Data.getString("email");
                password = Data.getString("password");
                phone = Data.getString("phone");
                pic = Data.getString("pic");

                UserDataClass obj = new UserDataClass();
                obj.setId(id);
                obj.setName(name);
                obj.setEmail(email);
                obj.setPassword(password);
                obj.setPhone(phone);
                obj.setPic(pic);

                arr.add(obj);

            }
            return arr;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
