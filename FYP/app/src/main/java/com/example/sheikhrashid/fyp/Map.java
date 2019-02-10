package com.example.sheikhrashid.fyp;

import android.Manifest;
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
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Map extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private GoogleMap mMap;

    private Location location;
    private double Rlatitude;
    private double Rlongitude;
    private LocationManager locationManager;
    private Criteria criteria;
    private String bestProvider;

    private String latitude;
    private String longitude;
    private double latDouble = 9.0;
    private double longDouble = 8.0;
    String id = null;
    static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1;
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 2;

    Uri uri;
    String storedPath, _phone, pic;

    ArrayList<UserDataClass> res;




    //======================================= OnCreate Function ====================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        onRestart();

        final View viewPos = findViewById(R.id.content_map);
        Snackbar.make(viewPos, "       Click The Markers to Contact Providers", Snackbar.LENGTH_INDEFINITE).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbar.setTitle("ServEase");

        getLocationMethod(); //call function

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
        mapFrag.getMapAsync(this);

        //====================== Runtime Permissions in Marshmallow =========================
        Context c = Map.this;
        if (ContextCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Inside", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(Map.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(Map.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }

            //--------------------------------------------------------
            if (ActivityCompat.shouldShowRequestPermissionRationale(Map.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(Map.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
            }
            //return;
        } else {
            //Toast.makeText(this, "Outside", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    } // end of OnCreate


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please grant the permission 1", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted 2", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please grant the permission 2", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        CallVolley();

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

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();



        if (id == R.id.XML_user_homepage) {
                Intent i = new Intent(Map.this, Map.class);
                startActivity(i);
        }
        if (id == R.id.XML_user_history) {
            Intent i = new Intent(Map.this, UserHistory.class);
            startActivity(i);
        } else if (id == R.id.XML_user_help) {
            Intent i = new Intent(Map.this, UserHelp.class);
            startActivity(i);
        } else if (id == R.id.XML_user_settings) {
            Intent i = new Intent(Map.this, UserProfileSettings.class);
            startActivity(i);
        } else if (id == R.id.XML_signout) {

            final AlertDialog d = new AlertDialog.Builder(Map.this).create();
            d.setTitle("Sign Out");
            d.setMessage("Do You Want to Sign Out?");

            d.setButton(d.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Map.this);
                    prefs.edit().clear().commit();

                    Intent i2 = new Intent(Map.this, LoginRegister.class);
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
        } else if (id == R.id.XML_user_select_service) {
            Intent i = new Intent(Map.this, ServicesList.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //====================================== OnMap Ready Fucntion====================================

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setPadding(1, 800, 1, 1);

            if (location.getLatitude() > 0 && location.getLongitude() > 0) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13.0f));
            }
            Intent i = getIntent();
            final String lati_ = i.getStringExtra("KEY_Lati");
            final String longi_ = i.getStringExtra("KEY_Longi");
            final String phone_ = i.getStringExtra("KEY_Phone");
            final String name_ = i.getStringExtra("KEY_Name");
            final String address_ = i.getStringExtra("KEY_Address");
            final String service_ = i.getStringExtra("KEY_Service");
            final String _details = i.getStringExtra("KEY_Details");


            LatLng sydney = new LatLng(Double.parseDouble(lati_), Double.parseDouble(longi_));
            mMap.addMarker(new MarkerOptions().position(sydney).title("Click to See Details"));

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                        Intent i = new Intent(Map.this, ProviderInformation.class);

                        i.putExtra("KEY_Name", name_);
                        i.putExtra("KEY_Address", address_);
                        i.putExtra("KEY_Phone", phone_);
                        i.putExtra("KEY_Details", _details);
                        i.putExtra("KEY_Service", service_);

                        startActivity(i);

                }
            });



        } else {
        }

    } // end of onMapReady Function



    public static boolean isLocationEnabled(Context context) {
        //...............
        return true;
    }

    public void getLocationMethod() {


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)))
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setMessage("GPS is disabled in your device. Enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();

        }
        if (isLocationEnabled(Map.this))
        {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //You can still do this if you like, you might get lucky:
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(Map.this, "Inside Checking:", Toast.LENGTH_SHORT).show();
                return;
            }
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                Rlatitude = location.getLatitude();
                Rlongitude = location.getLongitude();
                Toast.makeText(Map.this, "Aoooog: latitude:" + Rlatitude + "Aoooog: longitude:" + Rlongitude, Toast.LENGTH_SHORT).show();
            }
            else{
                //This is what you need:
                Toast.makeText(Map.this, "Request kro Location Updates:", Toast.LENGTH_SHORT).show();
                LocationListener obj = new MyLocationListenerClass();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, obj);
                //locationManager.requestSingleUpdate(bestProvider, lll, null);
            }
        }
        else
        {
            //prompt user to enable location....
            //.................
        }
    }


    public void CallVolley()
    {
        final ProgressDialog loading = ProgressDialog.show(Map.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(Map.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/getUserImage.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        byte[] decodedString = Base64.decode(response, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                        View hView =  navigationView.getHeaderView(0);
                        ImageView nav_user = (ImageView)hView.findViewById(R.id.imageView_navHeader);
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


                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Map.this);
                _phone = prefs.getString("SP_U_phone", "");

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
    //====================================== Private Class ====================================

    public class MyLocationListenerClass implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location) {

            //open the map:
            Rlatitude = location.getLatitude();
            Rlongitude = location.getLongitude();
            //Toast.makeText(Map.this, "latitude:" + Rlatitude + " longitude:" + Rlongitude, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onStatusChanged(String provider, int i, Bundle bundle) {

            Toast.makeText(Map.this, provider + "'s status changed to "+i +"!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {

            Toast.makeText(Map.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {

            Toast.makeText(Map.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
