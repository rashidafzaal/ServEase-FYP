package com.example.sheikhrashid.fyp.Utils;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by rashid on 2/8/2017.
 */

public class Constants
{
    public static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1;
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 2;
    public static GoogleMap mMap;

    public static  Location location;
    public static  double latitude;
    public static  double longitude;
    public static  LocationManager locationManager;
    public static  Criteria criteria;
    public static  String bestProvider;

}

