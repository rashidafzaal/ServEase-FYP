package com.example.sheikhrashid.fyp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class ProfilePicture extends Activity
{
    Bitmap bitmap;
    EditText edLocation, edDetails;
    String ed_Location, ed_Details;
    String Phone;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        Intent i = getIntent();
        String Name = i.getStringExtra("name");
        String Email = i.getStringExtra("email");
        String Password = i.getStringExtra("password");
        Phone = i.getStringExtra("phone");
        String Profession = i.getStringExtra("profession");

        TextView tv1 = (TextView) findViewById(R.id.XML_txtView1);
        tv1.setText(Name);
        TextView tv2 = (TextView) findViewById(R.id.XML_txtView2);
        tv2.setText(Email);
        TextView tv3 = (TextView) findViewById(R.id.XML_txtView3);
        tv3.setText(Password);
        TextView tv4 = (TextView) findViewById(R.id.XML_txtView4);
        tv4.setText(Phone);
        TextView tv5 = (TextView) findViewById(R.id.XML_txtView5);
        tv5.setText(Profession);


        pressed_XML_btn_upload();
        pressed_XML_NEXT();
    }

    public void pressed_XML_btn_upload()
    {
        Button btn = (Button)findViewById(R.id.XML_btn_upload);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int a =1234;
                startActivityForResult(i, a);

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ImageView imageView = (ImageView) findViewById(R.id.XML_profile_images);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void pressed_XML_NEXT()
    {
        Button btn = (Button)findViewById(R.id.XML_NEXT);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                edLocation = (EditText) findViewById(R.id.XML_shopLocation);
                ed_Location = edLocation.getText().toString();

                edDetails = (EditText) findViewById(R.id.XML_P_detail);
                ed_Details = edDetails.getText().toString();

                uploadImage(ed_Location, ed_Details); // Upload image and text to server



            }
        }); //end of BUTTON CLICK

    } //end of function


    private void uploadImage(final String Plocation, final String PDetails){
        //Showing the progress dialog
        final ProgressDialog loading2 = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/uploadProvider.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        if (s.contains("Success"))
                        {

                            Intent i = new Intent(ProfilePicture.this, ProviderMap.class);
                            i.putExtra("myPhone", Phone);
                            startActivity(i);
                            Toast.makeText(ProfilePicture.this, s , Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(ProfilePicture.this, s , Toast.LENGTH_LONG).show();
                        }
                        loading2.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog

                        Toast.makeText(ProfilePicture.this, "Error:"+ volleyError, Toast.LENGTH_SHORT).show();
                        loading2.dismiss();

                    }
                }){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                 image = getStringImage(bitmap);

                //Creating parameters
                java.util.Map<String, String> params = new HashMap<String, String>();

                //Adding parameters
                params.put("KEY_IMAGE", image);
                params.put("KEY_LOCATION", Plocation);
                params.put("KEY_DETAILS", PDetails);
                params.put("KEY_PHONE", Phone);

                LatLng l = getLocationFromAddress(ProfilePicture.this, Plocation);
                double longi = l.longitude;
                double lati = l.latitude;

                params.put("KEY_LONGITUDE", String.valueOf(longi));
                params.put("KEY_LATITUDE", String.valueOf(lati));


                //returning parameters
                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



}
