package com.example.sheikhrashid.fyp.Provider;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sheikhrashid.fyp.DialogBox;
import com.example.sheikhrashid.fyp.LoginRegister;
import com.example.sheikhrashid.fyp.Map;
import com.example.sheikhrashid.fyp.ProviderInformation;
import com.example.sheikhrashid.fyp.R;
import com.example.sheikhrashid.fyp.Splash;
import com.example.sheikhrashid.fyp.UserJSONParser;
import com.example.sheikhrashid.fyp.UserProfileSettings;
import com.example.sheikhrashid.fyp.Utils.GMailSender;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Provider;
import java.util.HashMap;

public class ProviderMap extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private GoogleMap mMap;


    private Location location;
    private double latitude;
    private double longitude;
    private LocationManager locationManager;
    private Criteria criteria;
    private String bestProvider;

    static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1;
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 2;

    String myPhone;

    //======================================= OnCreate Function ====================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_map);

        VolleyForStatusCheck();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ProviderMap.this);
        myPhone = prefs.getString("SP_P_phone", "");

        VolleyforGetImage(myPhone);

        getLocationMethod();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ServEasee");

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.provider_map1);
        mapFrag.getMapAsync(this);

        //====================== Runtime Permissions in Marshmallow =========================
        Context c = ProviderMap.this;
        if (ContextCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Provider Inside", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(ProviderMap.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(ProviderMap.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }

            //--------------------------------------------------------
            if (ActivityCompat.shouldShowRequestPermissionRationale(ProviderMap.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(ProviderMap.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
            }
            //return;
        } else {
            //Toast.makeText(this, "Provider Outside", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "1. Please grant the permission! .!.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "2. Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "2. Please grant the permission! .!.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.provider_map, menu);
//        MenuItem item = menu.findItem(R.id.myswitch);
//        item.setActionView(R.layout.switch_layout);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.XML_provider_homepage) {
            Intent i = new Intent(ProviderMap.this, ProviderMap.class);
            startActivity(i);
        }else if (id == R.id.XML_provider_history) {
            Intent i = new Intent(ProviderMap.this, ProviderHistory.class);
            startActivity(i);
        } else if (id == R.id.XML_provider_settings) {
            Intent i = new Intent(ProviderMap.this, ProviderProfileSettings.class);
            startActivity(i);
        }  else if (id == R.id.XML_provider_help) {
            Intent i = new Intent(ProviderMap.this, ProviderHelp.class);
            startActivity(i);
        } else if (id == R.id.XML_provider_signout) {

            final AlertDialog d = new AlertDialog.Builder(ProviderMap.this).create();
            d.setTitle("Sign Out");
            d.setMessage("Do You Want to Sign Out?");

            d.setButton(d.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ProviderMap.this);
                    prefs.edit().clear().commit();
                    Intent i2 = new Intent(ProviderMap.this, LoginRegister.class);
                    startActivity(i2);
                }
            });
            d.setButton(d.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    d.dismiss();
                }
            });
            d.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setPadding(1, 800, 1, 1);

            if (location.getLatitude() > 0 && location.getLongitude() > 0) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14.0f));
            }

        } else {
        }

    }

    public static boolean isLocationEnabled(Context context) {
        //...............
        return true;
    }

    public void getLocationMethod() {
        if (isLocationEnabled(ProviderMap.this))
        {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //You can still do this if you like, you might get lucky:
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(ProviderMap.this, "Inside Checking:", Toast.LENGTH_SHORT).show();
                return;
            }
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Toast.makeText(ProviderMap.this, "Aoooog: latitude:" + latitude + "Aoooog: longitude:" + longitude, Toast.LENGTH_SHORT).show();
            }
            else{
                //This is what you need:
                Toast.makeText(ProviderMap.this, "Request kro Location Updates Provider:", Toast.LENGTH_SHORT).show();
                LocationListener obj = new ProviderMap.ProviderLocationListenerClass();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, obj);
            }
        }
        else
        {
            //prompt user to enable location....
            //.................
        }
    }

    public void VolleyforGetImage(final String myPhone)
    {

        final ProgressDialog loading = ProgressDialog.show(ProviderMap.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(ProviderMap.this);
        final String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/getImage.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Decoding the Picture
                        byte[] decodedString = Base64.decode(response, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                        View hView =  navigationView.getHeaderView(0);
                        ImageView nav_user = (ImageView)hView.findViewById(R.id.provider_imageView);
                        nav_user.setImageBitmap(decodedByte);


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

                params.put("KEY_PHONE", myPhone);
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

// ============================================ ANother Volley Call =================================================
    public void VolleyForStatusCheck()
    {
        final ProgressDialog loading = ProgressDialog.show(ProviderMap.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(ProviderMap.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/getProviderID.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        AnotherVolley(response);
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


                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ProviderMap.this);
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

    public void AnotherVolley(final String P_ID)
    {

        final ProgressDialog loading = ProgressDialog.show(ProviderMap.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(ProviderMap.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/checkRstatus.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        if (response.equals("1"))
                        {
                            ShowNotification();
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

                params.put("KEY_PID", P_ID);
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

    public void ShowNotification()
    {
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

//        Intent mainIntent = new Intent(Intent.ACTION_VIEW,
//                Uri.parse("content://media/internal/images/media"));

        Intent mainIntent = new Intent(this, DialogBox.class);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        Notification noti = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("Servease")
                .setContentText("Hey, you have got a new Service Request!")
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.logofyp2)
                .setTicker("ticker message")
                .setWhen(System.currentTimeMillis())
                .setSound(uri)
                .build();

        notificationManager.notify(0, noti);


    }


    // ====================================================== Inner Class ==================
    public class ProviderLocationListenerClass implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location) {

            //open the map:
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Toast.makeText(ProviderMap.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onStatusChanged(String provider, int i, Bundle bundle) {

            Toast.makeText(ProviderMap.this, provider + "'s status changed to "+i +"!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {

            Toast.makeText(ProviderMap.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {

            Toast.makeText(ProviderMap.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
