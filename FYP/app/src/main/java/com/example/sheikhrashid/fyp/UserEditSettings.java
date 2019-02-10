package com.example.sheikhrashid.fyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UserEditSettings extends AppCompatActivity {

    Bitmap bitmap;
    String image;
    Uri uri;
    String storedPath, your_phone, _name, _password, old_pass, old_name, _pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_settings);

        VolleyForSaveChanges();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Intent i = getIntent();
        your_phone = i.getStringExtra("YOUR_PHONE");
        EditText ed1 = (EditText) findViewById(R.id.U_editsetting_name);
        old_name = i.getStringExtra("YOUR_NAME");
        ed1.setText(old_name);
        EditText ed2 = (EditText) findViewById(R.id.U_editsetting_password);
        old_pass = i.getStringExtra("YOUR_PASSWORD");
        ed2.setText(old_pass);


        String mypic = i.getStringExtra("YOUR_PIC");
        if (mypic!=null)
        {
            byte[] decodedString = Base64.decode(mypic, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ImageView nav_user = (ImageView)findViewById(R.id.XML_user_edit_img);
            nav_user.setImageBitmap(decodedByte);
        }

        OnPress_Upload();
        OnPress_XML_save();
    }


    public void OnPress_Upload()
    {
        Button btn = (Button)findViewById(R.id.XML_edit_upload_btn);

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

                ImageView imageView = (ImageView) findViewById(R.id.XML_user_edit_img);
                imageView.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    public void OnPress_XML_save()
    {
        Button btn = (Button) findViewById(R.id.XML_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                EditText ed1 = (EditText) findViewById(R.id.U_editsetting_name);
                _name = ed1.getText().toString();
                EditText ed2 = (EditText) findViewById(R.id.U_editsetting_password);
                _password = ed2.getText().toString();


                VolleyForSaveChanges(); //Volley Call


            }
        });
    }




    public void VolleyForSaveChanges()
    {
        final ProgressDialog loading = ProgressDialog.show(UserEditSettings.this,"Saving Changes...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(UserEditSettings.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/UserSaveChanges.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        Intent i = new Intent(UserEditSettings.this, UserProfileSettings.class);
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
