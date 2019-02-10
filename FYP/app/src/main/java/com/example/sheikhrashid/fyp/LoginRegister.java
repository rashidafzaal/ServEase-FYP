package com.example.sheikhrashid.fyp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginRegister extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        loginBtnPressed();
        registerPressed();

    }

    public void loginBtnPressed() {
        Button btn = (Button) findViewById(R.id.loginBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginRegister.this, Login.class);
                startActivity(i);
            }
        });

    }

    public void registerPressed() {
        Button btn = (Button) findViewById(R.id.registerBtn);
        btn.getBackground().setColorFilter(new LightingColorFilter(0x000000, 0x000000)); //changing color without changing its style

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginRegister.this, Register.class);
                startActivity(i);
            }
        });

    }

}
