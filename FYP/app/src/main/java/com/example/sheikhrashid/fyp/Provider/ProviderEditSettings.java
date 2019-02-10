package com.example.sheikhrashid.fyp.Provider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sheikhrashid.fyp.Login;
import com.example.sheikhrashid.fyp.R;
import com.example.sheikhrashid.fyp.UserEditSettings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ProviderEditSettings extends AppCompatActivity {


    Bitmap bitmap;
    Uri uri;
    String your_phone, _name, _password, _address, old_pass, old_name, old_address, old_service;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_edit_settings);


        Intent i = getIntent();
        your_phone = i.getStringExtra("YOUR_PHONE");

        EditText ed1 = (EditText) findViewById(R.id.P_editsetting_name);
        old_name = i.getStringExtra("YOUR_NAME");
        ed1.setText(old_name);

        EditText ed2 = (EditText) findViewById(R.id.P_editsetting_password);
        old_pass = i.getStringExtra("YOUR_PASSWORD");
        ed2.setText(old_pass);

        EditText ed3 = (EditText) findViewById(R.id.P_editsetting_address);
        old_address = i.getStringExtra("YOUR_ADDRESS");
        ed3.setText(old_address);

        EditText ed4 = (EditText) findViewById(R.id.P_editsetting_service);
        old_service = i.getStringExtra("YOUR_SERVICE");
        ed4.setText(old_service);



        String mypic = i.getStringExtra("YOUR_PIC");
        if (mypic!=null)
        {
            byte[] decodedString = Base64.decode(mypic, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ImageView nav_user = (ImageView)findViewById(R.id.Provider_XML_user_edit_img);
            nav_user.setImageBitmap(decodedByte);
        }

        OnPress_Provider_XML_save();
        OnPress_Upload();
    } // end of onCReate

    public void OnPress_Upload()
    {

        Button btn = (Button)findViewById(R.id.Provider_XML_edit_upload_btn);

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

        if (resultCode == RESULT_OK && data != null && data.getData() != null)
        {

            uri = data.getData();

            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ImageView imageView = (ImageView) findViewById(R.id.Provider_XML_user_edit_img);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void OnPress_Provider_XML_save()
    {
        Button btn = (Button) findViewById(R.id.Provider_XML_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText ed1 = (EditText) findViewById(R.id.P_editsetting_name);
                _name = ed1.getText().toString();
                EditText ed2 = (EditText) findViewById(R.id.P_editsetting_password);
                _password = ed2.getText().toString();
                EditText ed3 = (EditText) findViewById(R.id.P_editsetting_address);
                _address = ed3.getText().toString();


                VolleyForSaveChanges(); //Volley Call


            }
        });
    }


    public void VolleyForSaveChanges()
    {
        final ProgressDialog loading = ProgressDialog.show(ProviderEditSettings.this,"Saving Changes...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(ProviderEditSettings.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/ProviderSaveChanges.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        Intent i = new Intent(ProviderEditSettings.this, ProviderProfileSettings.class);
                        startActivity(i);

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

                image = getStringImage(bitmap);

                //Creating parameters
                java.util.Map<String, String> params = new HashMap<String, String>();

                params.put("KEY_PHONE", your_phone);
                params.put("KEY_NAME", _name);
                params.put("KEY_PASSWORD", _password);
                params.put("KEY_PIC", image);
                params.put("KEY_ADDRESS", _address);
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

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
