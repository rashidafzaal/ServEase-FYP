package com.example.sheikhrashid.fyp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rashid on 3/29/2017.
 */

public class ProviderJSONParser
{
    String id, name, email, password, phone, service, address, details, JSON_longitude, JSON_latitude, pic;
    ArrayList<ProviderDataClass> arr = new ArrayList<>();
    public ArrayList<ProviderDataClass> showJSON (String response)
    {

        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");


            for (int i = 0; i < result.length(); i++)
            {
                JSONObject Data = result.getJSONObject(i);

                //if (Data.getString("id") != null)
                    id = Data.getString("id");

                //if (Data.getString("name") != null)
                    name = Data.getString("name");

                //if (Data.getString("email") != null)
                    email = Data.getString("email");

                //if (Data.getString("password") != null)
                    password = Data.getString("password");

                //if (Data.getString("phone") != null)
                    phone = Data.getString("phone");

                    pic  = Data.getString("pic");

                //if (Data.getString("service") != null)
                    service = Data.getString("service");

                //if (Data.getString("address") != null)
                    address = Data.getString("address");

                //if (Data.getString("details") != null)
                    details = Data.getString("details");

                //if (Data.getString("long") != null)
                    JSON_longitude = Data.getString("long");

                //if (Data.getString("lat") != null)
                    JSON_latitude = Data.getString("lat");

                ProviderDataClass obj = new ProviderDataClass();
                obj.setId(id);
                obj.setName(name);
                obj.setEmail(email);
                obj.setPassword(password);
                obj.setPhone(phone);
                obj.setPic(pic);
                obj.setService(service);
                obj.setDetails(details);
                obj.setAddress(address);
                obj.setJSON_latitude(JSON_latitude);
                obj.setJSON_longitude(JSON_longitude);


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
