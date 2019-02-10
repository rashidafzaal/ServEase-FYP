package com.example.sheikhrashid.fyp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserProfileSettings extends AppCompatActivity
{
    String _phone;
    String id, name, email, password, phone, pic;
    ArrayList<UserDataClass> res;
    TextView txt1, txt2, txt3, txt4;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);

        onRestart();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        txt1 = (TextView) findViewById(R.id.u_XML_name);
        txt2 = (TextView) findViewById(R.id.u_XML_email);
        txt3 = (TextView) findViewById(R.id.u_XML_password);
        txt4 = (TextView) findViewById(R.id.u_XML_phone);


        OnPressed_XML_btn_editSettings();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        CalltoVolley();
    }

    public void OnPressed_XML_btn_editSettings()
    {
        Button btn = (Button) findViewById(R.id.XML_btn_editSettings);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String your_name = txt1.getText().toString();
                String your_password = txt3.getText().toString();
                String your_phone = txt4.getText().toString();

                Intent i = new Intent(UserProfileSettings.this, UserEditSettings.class);
                i.putExtra("YOUR_NAME", your_name);
                i.putExtra("YOUR_PASSWORD", your_password);
                i.putExtra("YOUR_PHONE", your_phone);
                i.putExtra("YOUR_PIC", pic);
                startActivityForResult(i,1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu_edit:
                Intent i = new Intent(UserProfileSettings.this, UserEditSettings.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void CalltoVolley()
    {
        final ProgressDialog loading = ProgressDialog.show(UserProfileSettings.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(UserProfileSettings.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/getUserProfileData.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        UserJSONParser obj1 = new UserJSONParser();
                        res = obj1.showJSON(response);

                        int count;
                        for (count = 0 ;  count < res.size(); count++)
                        {
                            id = res.get(count).getId();
                            name = res.get(count).getName();
                            email = res.get(count).getEmail();
                            password = res.get(count).getPassword();
                            phone = res.get(count).getPhone();
                            pic = res.get(count).getPic();

                            txt1.setText(name);
                            txt2.setText(email);
                            txt3.setText(password);
                            txt4.setText(phone);

                            if (pic!=null)
                            {
                                byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                ImageView nav_user = (ImageView)findViewById(R.id.XML_user_profile_img);
                                nav_user.setImageBitmap(decodedByte);
                            }

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


                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UserProfileSettings.this);
                _phone = prefs.getString("SP_U_phone", "");

                params.put("KEY_SP_Phone", _phone);
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

} //end of class

