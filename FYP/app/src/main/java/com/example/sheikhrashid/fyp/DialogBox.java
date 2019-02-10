package com.example.sheikhrashid.fyp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.sheikhrashid.fyp.Provider.ProviderMap;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogBox extends AppCompatActivity {

    TextView txt1, txt2, txt3;
    String name, email, password, phone, pic;
    static String id;
    ArrayList<UserDataClass> res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VolleytoPID();

        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final AlertDialog.Builder builder = new AlertDialog.Builder(DialogBox.this);
        final View layout = inflater.inflate(R.layout.activity_dialog_box, (ViewGroup) findViewById(R.id.activity_dialog_box));
        builder.setView(layout);

        final Dialog dialog = new Dialog(DialogBox.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.activity_dialog_box);
        // Set dialog title
        dialog.setTitle("Custom Dialog");

        // set values for custom dialog components - text, image and button
        txt1 = (TextView) dialog.findViewById(R.id.dialog_name);
        txt2 = (TextView) dialog.findViewById(R.id.dialog_phone);
        txt3 = (TextView) dialog.findViewById(R.id.dialog_email);


//        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
//        image.setImageResource(R.drawable.app4);

        dialog.show();

        Button yes = (Button) dialog.findViewById(R.id.yes);
        // if decline button is clicked, close the custom dialog
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                VolleyonAcceptButton(); // It changes to 2
                dialog.dismiss();
            }
        });
        Button no = (Button) dialog.findViewById(R.id.no);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Close dialog
                        VolleyToChangeRStatus(); // It changes to 0
                        dialog.dismiss();
                    }
                });
    }

    public void VolleytoPID()
    {

        final ProgressDialog loading = ProgressDialog.show(DialogBox.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(DialogBox.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/getProviderID.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        AnotherVolleyForUserDataonDialog(response);
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

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DialogBox.this);
                String _phone = prefs.getString("SP_P_phone", "");

                params.put("KEY_PHONE", _phone);
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

    public void AnotherVolleyForUserDataonDialog(final String pid)
    {
        final ProgressDialog loading = ProgressDialog.show(DialogBox.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(DialogBox.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/userDataonDialogBox.php";
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
                            phone = res.get(count).getPhone();
                            //pic = res.get(count).getPic();

                            txt1.setText(name);
                            txt2.setText(phone);
                            txt3.setText(email);

//                            if (pic!=null)
//                            {
//                                byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
//                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                                ImageView nav_user = (ImageView)findViewById(R.id.imageDialog);
//                                nav_user.setImageBitmap(decodedByte);
//                            }

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
    public void VolleyToChangeRStatus()
    {
        final ProgressDialog loading = ProgressDialog.show(DialogBox.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(DialogBox.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/getProviderIDandUpdate.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        loading.dismiss();
                        Intent i = new Intent(DialogBox.this, ProviderMap.class);
                        startActivity(i);

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


                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DialogBox.this);
                String _phone = prefs.getString("SP_P_phone", "");

                params.put("KEY_PHONE", _phone);
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


    public void VolleyonAcceptButton()
    {
        final ProgressDialog loading = ProgressDialog.show(DialogBox.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(DialogBox.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/getProviderIDandUpdateYES.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        loading.dismiss();
                        Intent i = new Intent(DialogBox.this, ProviderMap.class);
                        startActivity(i);

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


                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DialogBox.this);
                String _phone = prefs.getString("SP_P_phone", "");

                params.put("KEY_PHONE", _phone);
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
