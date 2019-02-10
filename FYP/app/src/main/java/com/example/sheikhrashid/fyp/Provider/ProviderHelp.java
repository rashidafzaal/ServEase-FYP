package com.example.sheikhrashid.fyp.Provider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sheikhrashid.fyp.R;
import com.example.sheikhrashid.fyp.UserHelp;
import com.example.sheikhrashid.fyp.Utils.GMailSender;
import com.example.sheikhrashid.fyp.Utils.JSSEProvider;

import java.security.Provider;

public class ProviderHelp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_help);

        //android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        OnPress_XML_FAQ_submit();
    }


    public void OnPress_XML_FAQ_submit()
    {

        Button btn = (Button) findViewById(R.id.XML_FAQp_submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email =(EditText)findViewById(R.id.faqp_email);
                String e = email.getText().toString();
                if (!e.contains("@"))
                {
                    Toast.makeText(ProviderHelp.this, "Your email id is incorrect.", Toast.LENGTH_SHORT).show();
                    return;
                }
                EditText detail =(EditText)findViewById(R.id.faqp_question);
                String q = email.getText().toString();
                Toast.makeText(ProviderHelp.this, "Thank you for your time. Your Question has been Submitted", Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {

                    public void run() {

                        try {
                            EditText email =(EditText)findViewById(R.id.faqp_email);
                            String e = email.getText().toString();
                            EditText detail =(EditText)findViewById(R.id.faqp_question);
                            String q = detail.getText().toString();

                            GMailSender sender = new GMailSender(

                                    "serveasegroups@gmail.com",

                                    "ghufranrashid");



                            //sender.addAttachment(Environment.getExternalStorageDirectory().getPath()+"/image.jpg");


                            sender.sendMail(e, q ,

                                    "serveasegroups@gmail.com",

                                    "serveasegroups@gmail.com");

                            //Toast.makeText(PasswordVerfication.this, digitsVerify, Toast.LENGTH_SHORT).show();
                            Toast.makeText(ProviderHelp.this, "Thank you for your time. Your Question has been Submitted", Toast.LENGTH_SHORT).show();



                        } catch (Exception e) {

                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();



                        }

                    }

                }).start();


            }
        });
    }
}
