package com.example.sheikhrashid.fyp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class Login extends Activity {

    String phone, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CheckBox box = (CheckBox)findViewById(R.id.Login_checkbox);
        final EditText password = (EditText) findViewById(R.id.XML_Login_Password);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if(checked){
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else{
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            }
        });

        OnForgotPassword();
        AsUser_LoginPressed();
        AsProvider_LoginPressed();
    } // end of OnCreate


    public void OnForgotPassword()
    {
        TextView t2 = (TextView) findViewById(R.id.XML_textView_forgot);
        t2.setPaintFlags(t2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this, PasswordVerfication.class);
                startActivity(i);

            }
        });
    }

    public void AsUser_LoginPressed()
    {

        Button btn = (Button) findViewById(R.id.XML_Login_User_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText ed1 = (EditText) findViewById(R.id.XML_Login_Phone);
                phone = ed1.getText().toString();
                final EditText ed2 = (EditText) findViewById(R.id.XML_Login_Password);
                pass = ed2.getText().toString();

                final Intent i2 = new Intent(Login.this,ServicesList.class);


                final ProgressDialog loading = ProgressDialog.show(Login.this,"Logging in...","Please wait...",false,false);
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/loginUser.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String r) {
                                //Disimissing the progress dialog
                                if(r.equals("UserSuccess"))
                                {
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("SP_U_phone", phone);
                                    editor.apply();

                                    startActivity(i2);
                                }
                                 else{
                                    Toast.makeText(Login.this, "Please Enter Valid Phone and Password",Toast.LENGTH_LONG).show();
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

                        //Adding parameters
                        params.put("KEY_PHONE", phone);
                        params.put("KEY_PASSWORD", pass);

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
        });
    }


    public void AsProvider_LoginPressed()
    {

        Button btn = (Button) findViewById(R.id.XML_Login_Provider_btn);
        btn.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0x000000)); //changing color without changing its style
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final EditText ed1 = (EditText) findViewById(R.id.XML_Login_Phone);
                phone = ed1.getText().toString();
                final EditText ed2 = (EditText) findViewById(R.id.XML_Login_Password);
                pass = ed2.getText().toString();


                final Intent i = new Intent(Login.this,ProviderMap.class);


                final ProgressDialog loading = ProgressDialog.show(Login.this,"Logging in...","Please wait...",false,false);
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/loginProvider.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Disimissing the progress dialog
                                if(response.equals("ProviderSuccess"))
                                {
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("SP_P_phone", phone);
                                    editor.apply();

                                    startActivity(i);
                                }
                                else{
                                    Toast.makeText(Login.this, "Please Enter Valid Phone and Password",Toast.LENGTH_LONG).show();
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

                        //Adding parameters
                        params.put("KEY_PHONE", phone);
                        params.put("KEY_PASSWORD", pass);

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
        });

    }
} // end of Activity
