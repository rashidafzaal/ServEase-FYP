package com.example.sheikhrashid.fyp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        onPress_rating_dialog_id();
    }

    public void onPress_rating_dialog_id()
    {


        Button btn = (Button) findViewById(R.id.rate_provider_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final AlertDialog.Builder builder = new AlertDialog.Builder(UserHistory.this);
                final View layout = inflater.inflate(R.layout.rating_dialog, (ViewGroup) findViewById(R.id.rating_dialog_id));

                builder.setTitle("Rate The Provider");
                builder.setView(layout);


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                builder.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(UserHistory.this, "Thanks for Your Feedback", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();


            } // end of first onClick
        });


    }
}
