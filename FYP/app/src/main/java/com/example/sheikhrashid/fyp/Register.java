package com.example.sheikhrashid.fyp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LightingColorFilter;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.Map;

public class Register extends Activity implements AdapterView.OnItemSelectedListener {

    String uName, uEmail, uPassword, uPhone;
    //Spinner profession1 = (Spinner) findViewById(R.id.spinner1);
   //Spinner profession2 = (Spinner) findViewById(R.id.spinner2);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CheckBox box = (CheckBox) findViewById(R.id.Register_checkbox);
        final EditText password = (EditText) findViewById(R.id.XML_Password);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if (checked) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            }
        });
        Spinner profession1 = (Spinner) findViewById(R.id.spinner1);
        Spinner profession2 = (Spinner) findViewById(R.id.spinner2);
        profession1.setOnItemSelectedListener(this);
        Press_RegProviderBtn();
        Press_RegUserBtn();
    } //end of onCreate


    // ========================================== Register As Provider =============================

    public void Press_RegProviderBtn() {
        Button btn = (Button) findViewById(R.id.RegProviderBtn);
        btn.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0x000000)); //changing color without changing its style

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Register.this, ProfilePicture.class);

                EditText ed1 = (EditText) findViewById(R.id.XML_Name);
                final String editText_name = ed1.getText().toString();

                EditText ed2 = (EditText) findViewById(R.id.XML_Email);
                final String editText_email;
                if(ed2.getText().toString().contains("@")) {
                    editText_email = ed2.getText().toString();
                }
                else
                {
                    Toast.makeText(Register.this, "Your Email in incorrect please check your email again.", Toast.LENGTH_SHORT).show();
                    return ;
                }
                EditText ed3 = (EditText) findViewById(R.id.XML_Password);
                final String editText_password = ed3.getText().toString();

                EditText ed4 = (EditText) findViewById(R.id.XML_Phone);
                final String editText_phone;
                if(ed4.getText().toString().length()==11) {
                     editText_phone = ed4.getText().toString();
                }
                else
                {
                    Toast.makeText(Register.this, "Your Phone number is not correct please check it again", Toast.LENGTH_SHORT).show();
                    return;
                }

                Spinner profession1 = (Spinner) findViewById(R.id.spinner1);
                final String profession = profession1.getSelectedItem().toString();
                Spinner profession2 = (Spinner) findViewById(R.id.spinner2);
                final String professions = profession2.getSelectedItem().toString();


                i.putExtra("name", editText_name);
                i.putExtra("email", editText_email);
                i.putExtra("password", editText_password);
                i.putExtra("phone", editText_phone);
                i.putExtra("profession", professions);


                // ===================== Volley Code for Provider ==================================

                String URL = getString(R.string.ip)+"/FYP_PROJECT/provider.php";
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("success")) {

                            // Putting phone number in SP while registering as provider
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Register.this);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("SP_P_phone", editText_phone);
                            editor.apply();

                            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "failed" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected java.util.Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<String, String>();

                        //Toast.makeText(getApplicationContext(),"in getParam",Toast.LENGTH_SHORT).show();

                        params.put("KEY_P_NAME", editText_name);
                        params.put("KEY_P_EMAIL", editText_email);
                        params.put("KEY_P_PASSWORD", editText_password);
                        params.put("KEY_P_PHONE", editText_phone);
                        params.put("KEY_P_SERVICE", professions);

                        return params;
                    }
                };
                queue.add(stringRequest);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        3000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.getCache().clear();



                startActivity(i);

            }
        });
    }

    // ========================================== Register As USer =================================

    public void Press_RegUserBtn() {
        Button btn = (Button) findViewById(R.id.RegUserBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Register.this, ServicesList.class);

                EditText ed1 = (EditText) findViewById(R.id.XML_Name);
                uName = ed1.getText().toString();
                EditText ed2 = (EditText) findViewById(R.id.XML_Email);
                if(ed2.getText().toString().contains("@")) {
                    uEmail = ed2.getText().toString();
                }
                else
                {
                    Toast.makeText(Register.this, "Your Email in incorrect please check your email again.", Toast.LENGTH_SHORT).show();
                    return ;
                }
                EditText ed3 = (EditText) findViewById(R.id.XML_Password);
                uPassword = ed3.getText().toString();

                EditText ed4 = (EditText) findViewById(R.id.XML_Phone);
                if(ed4.getText().toString().length()== 11)
                {
                    uPhone = ed4.getText().toString();
                }
                else
                {
                    Toast.makeText(Register.this, "Your Phone number is not correct please check it again", Toast.LENGTH_SHORT).show();
                    return;
                }


                // ===================== Volley Code for User ======================================

                String URL = getString(R.string.ip)+"/FYP_PROJECT/user.php";
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("success")) {
                            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "failed" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected java.util.Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<String, String>();

                        params.put("KEY_U_NAME", uName);
                        params.put("KEY_U_EMAIL", uEmail);
                        params.put("KEY_U_PASSWORD", uPassword);
                        params.put("KEY_U_PHONE", uPhone);

                        return params;
                    }
                };
                queue.add(stringRequest);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        3000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.getCache().clear();
                startActivity(i);
            } // end of on click
        }); //end of listener
    } // end of function


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner profession1 = (Spinner) findViewById(R.id.spinner1);
        Spinner profession2 = (Spinner) findViewById(R.id.spinner2);

        String sp1= String.valueOf(profession1.getSelectedItem());
        Toast.makeText(this, sp1, Toast.LENGTH_SHORT).show();
        if(sp1.contentEquals("Vehicles")) {
            List<String> list = new ArrayList<String>();
            list.add("Shifting Vehicles");
            list.add("Towing Vehicles");
            list.add("Trip Vehicles");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            profession2.setAdapter(dataAdapter);
        }
        if(sp1.contentEquals("Plumbers")) {
            List<String> list = new ArrayList<String>();
            list.add("Water Plumber");
            list.add("Sanitary Plumber");
            list.add("Drainage Plumber");
            list.add("Gas Plumber");

            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            profession2.setAdapter(dataAdapter2);
        }
        if(sp1.contentEquals("Electricians")) {
            List<String> list = new ArrayList<String>();
            list.add("Commercial Electrician");
            list.add("Motor Electrician");


            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            profession2.setAdapter(dataAdapter2);
        }
        if(sp1.contentEquals("Labour")) {
            List<String> list = new ArrayList<String>();
            list.add("Labour for Construstion");
            list.add("Labour for Towing");
            list.add("Individual Labour");


            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            profession2.setAdapter(dataAdapter2);
        }

        if(sp1.contentEquals("Tailor")) {
            List<String> list = new ArrayList<String>();
            list.add("Gents Tailor");
            list.add("Ladies Tailor");



            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            profession2.setAdapter(dataAdapter2);
        }
        if(sp1.contentEquals("Home Tutors")) {
            List<String> list = new ArrayList<String>();
            list.add("Science Subjects(Biology)");
            list.add("Science Subjects(Mathematics)");
            list.add("Arts Subjects");


            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            profession2.setAdapter(dataAdapter2);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
