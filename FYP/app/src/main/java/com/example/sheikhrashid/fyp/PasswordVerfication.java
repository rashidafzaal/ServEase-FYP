package com.example.sheikhrashid.fyp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sheikhrashid.fyp.Utils.GMailSender;

import java.util.HashMap;

public class PasswordVerfication extends AppCompatActivity {

    String a;
    String strPassword1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_verfication);
        Toast.makeText(this, digitsVerify, Toast.LENGTH_SHORT).show();
        onSend();
        onVerify();

    }
    String digitsVerify=random();
    public String random()
    {
        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int)(Math.random()*9000)+1000;

        //Store integer in a string
        String PINString = String.valueOf(randomPIN);


        return PINString;
    }


    public void email()
    {
        final EditText eml = (EditText)findViewById(R.id.XML_EmailVarify);
        a = String.valueOf(eml.getText());
        new Thread(new Runnable() {

            public void run() {

                try {
                    String s = String.valueOf(eml.getText());
                    GMailSender sender = new GMailSender(

                            "serveasegroups@gmail.com",

                            "ghufranrashid");



                    //sender.addAttachment(Environment.getExternalStorageDirectory().getPath()+"/image.jpg");


                    sender.sendMail("ServEase", digitsVerify+"  This is your 4 digit password authentication code",

                            "serveasegroups@gmail.com",

                            s);
                    //Toast.makeText(PasswordVerfication.this, digitsVerify, Toast.LENGTH_SHORT).show();


                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();



                }

            }

        }).start();
    }
    public void onSend()
    {
        final EditText eml = (EditText)findViewById(R.id.XML_EmailVarify);
        Button vrfy = (Button)findViewById(R.id.XML_send);
        vrfy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // eml.setFocusable(false);
                if(eml.getText().toString().contains("@")) {
                    email();
                    eml.setFocusable(false);
                    //eml.setTextColor(4);
                    Toast.makeText(PasswordVerfication.this, "4 digit code has been sent to your email. Enter that code beneath", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(PasswordVerfication.this, "Email is Incorrect. Please Recheck your Email.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }
    public void onVerify()
    {
        final EditText eml = (EditText)findViewById(R.id.XML_code);
        Button vrfy = (Button)findViewById(R.id.XML_verifyBtn);
        vrfy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PasswordVerfication.this, digitsVerify, Toast.LENGTH_SHORT).show();
                //Toast.makeText(PasswordVerfication.this, eml.getText().toString(), Toast.LENGTH_SHORT).show();
            if(eml.getText().toString() .equals(digitsVerify) ) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View layout = inflater.inflate(R.layout.passwordialog, (ViewGroup) findViewById(R.id.dialog));
                final EditText password1 = (EditText) layout.findViewById(R.id.XML_ED_PSD1);
                final EditText password2 = (EditText) layout.findViewById(R.id.XML_ED_PSD2);

                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordVerfication.this);
                builder.setTitle("Reset Your Password");
                builder.setView(layout);


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(PasswordVerfication.this, "Password Not Set", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        strPassword1 = password1.getText().toString();
                        String strPassword2 = password2.getText().toString();
                        if (strPassword1.equals(strPassword2))
                        {
                            VolleyResetPassword();
                            Toast.makeText(PasswordVerfication.this,
                                    "Your New Password is = " + strPassword2, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(PasswordVerfication.this, "Please write same password in both fields snd please verify again", Toast.LENGTH_LONG).show();

                        }

                    }
                });
                builder.show();

            }
                else{
                //Toast.makeText(PasswordVerfication.this, "Password Not Set", Toast.LENGTH_SHORT).show();
                Toast.makeText(PasswordVerfication.this, "Incorrect Code.", Toast.LENGTH_LONG).show();
                return;
            }
            }
        });

    } // end of OnVerfiy

    //============================================== Volley call ===============================================================
    public void VolleyResetPassword()
    {
        final ProgressDialog loading = ProgressDialog.show(PasswordVerfication.this,"Saving your Password...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(PasswordVerfication.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/passwordVerification.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        if(response.equals("UserSuccess") || response.equals("ProviderSuccess"))
                        {
                            Toast.makeText(PasswordVerfication.this, "Updated Successfully",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(PasswordVerfication.this, "Failed",Toast.LENGTH_LONG).show();
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


                params.put("KEY_EMAIL", a);
                params.put("KEY_PASSWORD", strPassword1);
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

