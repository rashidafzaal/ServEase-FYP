package com.example.sheikhrashid.fyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ProviderInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.sheikhrashid.fyp.Provider.ProviderMap;

import java.security.Provider;
import java.util.HashMap;

public class ProviderInformation extends AppCompatActivity {

    String name, phone, service, details, address;
    String provider_phone, user_phone;
    ProgressDialog waiting = null;
    String u_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_information);



        Intent i = getIntent();
        name = i.getStringExtra("KEY_Name");
        phone = i.getStringExtra("KEY_Phone");
        service = i.getStringExtra("KEY_Service");
        details = i.getStringExtra("KEY_Details");
        address = i.getStringExtra("KEY_Address");

        TextView ed = (TextView) findViewById(R.id.XML_name);
        ed.setText(name);

        TextView ed1 = (TextView) findViewById(R.id.XML_phoneNo);
        ed1.setText(phone);

        TextView ed2 = (TextView) findViewById(R.id.XML_service);
        ed2.setText(service);

        TextView ed3 = (TextView) findViewById(R.id.XML_details);
        ed3.setText(details);

        TextView ed4 = (TextView) findViewById(R.id.XML_address);
        ed4.setText(address);


        Button b =  (Button)(findViewById(R.id.XML_Call));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t = (TextView)findViewById(R.id.XML_phoneNo);
                String i = t.getText().toString();
                String a="tel:"+i ;
                Uri call = Uri.parse(a);

                Intent in = new Intent("android.intent.action.DIAL",call);
                startActivity(in);

            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ProviderInformation.this);
        user_phone = prefs.getString("SP_U_phone", ""); // user phone number
        provider_phone = phone;     // provider phone number


        CallVolley();
        OnPressRequest();
    }

    public void CallVolley()
    {
        final ProgressDialog loading = ProgressDialog.show(ProviderInformation.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(ProviderInformation.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/getImage.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog


                        byte[] decodedString = Base64.decode(response, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        ImageView nav_user = (ImageView)findViewById(R.id.XML_providers_image);
                        nav_user.setImageBitmap(decodedByte);



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

//
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ProviderInformation.this);
//                phone = prefs.getString("SP_P_phone", "");

                params.put("KEY_PHONE", phone);
                //returning parameters
                return params;
            }
        };
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void OnPressRequest()
    {
        Button bt = (Button) findViewById(R.id.XML_Request);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                VolleyOnRequestBtn();
            }
        });
    }


    public void VolleyOnRequestBtn()
    {

        final ProgressDialog loading = ProgressDialog.show(ProviderInformation.this,"Making Requesting Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(ProviderInformation.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/userSendingRequest.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        if (response.contains("DONE"))
                        {
                            loading.dismiss();

                            waiting = ProgressDialog.show(ProviderInformation.this,"Waiting For Provider...","Please wait...",false,false);
                            new CountDownTimer(30000,1000){

                                @Override
                                public void onTick(long l) {

                                }

                                @Override
                                public void onFinish() {
                                    VolleyForConfirmation();
                                    // Volley CAll

                                }
                            }.start();
                        }

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


                params.put("KEY_U_PHONE", user_phone);
                params.put("KEY_P_PHONE", provider_phone);
                //returning parameters
                return params;
            }
        };
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    public void VolleyForConfirmation()
    {
        final ProgressDialog loading = ProgressDialog.show(ProviderInformation.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(ProviderInformation.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/getProviderID.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        AnotherVolleyWhileConfirmation(response);
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


                params.put("KEY_PHONE", provider_phone);
                //returning parameters
                return params;
            }
        };
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void AnotherVolleyWhileConfirmation(final String pid)
    {
        final ProgressDialog loading = ProgressDialog.show(ProviderInformation.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(ProviderInformation.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/checkRstatus.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog
                        if (response.equals("0"))
                        {
                            waiting.dismiss();
                            Toast.makeText(ProviderInformation.this, "Provider Has Rejected your Request", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                        else if (response.equals("1"))
                        {
                            waiting.dismiss();
                            u_id = DialogBox.id;

                            VolleyChangeRStatusto4();

                            Toast.makeText(ProviderInformation.this, "Provider Not Responding", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                        else if (response.equals("2"))
                        {
                            waiting.dismiss();
                            Toast.makeText(ProviderInformation.this, "Provider Has Accepted your Request", Toast.LENGTH_LONG).show();
                            onBackPressed();
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

                params.put("KEY_PID", pid);
                //returning parameters
                return params;
            }
        };
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void VolleyChangeRStatusto4()
    {
        final ProgressDialog loading = ProgressDialog.show(ProviderInformation.this,"Updating Status...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(ProviderInformation.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/changeRStatusTo4.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        Toast.makeText(ProviderInformation.this, "Status Updated", Toast.LENGTH_SHORT).show();
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

                
                params.put("KEY_ID", u_id);
                //returning parameters
                return params;
            }
        };
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}
