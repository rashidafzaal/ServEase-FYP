package com.example.sheikhrashid.fyp.Provider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sheikhrashid.fyp.ProviderDataClass;
import com.example.sheikhrashid.fyp.ProviderJSONParser;
import com.example.sheikhrashid.fyp.R;
import com.example.sheikhrashid.fyp.UserDataClass;
import com.example.sheikhrashid.fyp.UserEditSettings;
import com.example.sheikhrashid.fyp.UserJSONParser;
import com.example.sheikhrashid.fyp.UserProfileSettings;

import java.util.ArrayList;
import java.util.HashMap;

public class ProviderProfileSettings extends AppCompatActivity {

    String SP_phone, id, name, email, password, phone, pic, address, service;
    ArrayList<ProviderDataClass> res;
    TextView txt1, txt2, txt3, txt4, txt5, txt6;

    Bitmap decodedByte = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_profile_settings);


        txt1 = (TextView) findViewById(R.id.XML_P_name);
        txt2 = (TextView) findViewById(R.id.XML_P_email);
        txt3 = (TextView) findViewById(R.id.XML_P_password);
        txt4 = (TextView) findViewById(R.id.XML_P_phone);
        txt5 = (TextView) findViewById(R.id.XML_P_address);
        txt6 = (TextView) findViewById(R.id.XML_P_service);


        CalltoVolley();
        OnPress_Provider_XML_btn_editSettings();
    } // end of Oncreate

    @Override
    protected void onRestart() {
        super.onRestart();
        CalltoVolley();
    }

    public void OnPress_Provider_XML_btn_editSettings()
    {
        Button btn = (Button) findViewById(R.id.Provider_XML_btn_editSettings);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String your_name = txt1.getText().toString();
                String your_password = txt3.getText().toString();
                String your_phone = txt4.getText().toString();
                String your_adddress = txt5.getText().toString();
                String your_service = txt6.getText().toString();

                Intent i = new Intent(ProviderProfileSettings.this, ProviderEditSettings.class);

                i.putExtra("YOUR_NAME", your_name);
                i.putExtra("YOUR_PASSWORD", your_password);
                i.putExtra("YOUR_PHONE", your_phone);
                i.putExtra("YOUR_PIC", pic);
                i.putExtra("YOUR_ADDRESS", your_adddress);
                i.putExtra("YOUR_SERVICE", your_service);

                startActivity(i);
            }
        });
    }

    public void CalltoVolley()
    {
        final ProgressDialog loading = ProgressDialog.show(ProviderProfileSettings.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(ProviderProfileSettings.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/getProviderProfileData.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        ProviderJSONParser obj1 = new ProviderJSONParser();
                        res = obj1.showJSON(response);

                        int count;
                        for (count = 0 ;  count < res.size(); count++)
                        {
                            id = res.get(count).getId();
                            name = res.get(count).getName();
                            email = res.get(count).getEmail();
                            password = res.get(count).getPassword();
                            phone = res.get(count).getPhone();
                            address = res.get(count).getAddress();
                            service = res.get(count).getService();
                            pic = res.get(count).getPic();

                            txt1.setText(name);
                            txt2.setText(email);
                            txt3.setText(password);
                            txt4.setText(phone);
                            txt5.setText(address);
                            txt6.setText(service);

                            if (pic!=null)
                            {
                                byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
                                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                ImageView nav_user = (ImageView)findViewById(R.id.Provider_XML_user_profile_img);
                                nav_user.setImageBitmap(decodedByte);
                            }


                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ProviderProfileSettings.this);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("SP_Name", name);
                            editor.apply();
                        }

                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                    }
                }){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                //Creating parameters
                java.util.Map<String, String> params = new HashMap<String, String>();


                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ProviderProfileSettings.this);
                SP_phone = prefs.getString("SP_P_phone", "");

                params.put("KEY_SP_P_Phone", SP_phone);
                //returning parameters
                return params;
            }
        };
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    } // end of Function
}
