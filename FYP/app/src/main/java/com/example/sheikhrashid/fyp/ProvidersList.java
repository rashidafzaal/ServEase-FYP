package com.example.sheikhrashid.fyp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.sheikhrashid.fyp.R.id.view;

public class ProvidersList extends AppCompatActivity{

    String id, name, email, password, phone, service, address, details, longitude, latitude;

    String myvar;
    ListView listview;
    String[] values;
    String [] ids;
    StableArrayAdapter adapter;
    ArrayList<ProviderDataClass> res;
    String _id, _lati, _longi, _phone, _name, _address, _service, _details;
    String sorting_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_providers_list);


        Intent i = getIntent();
        myvar = i.getStringExtra("KEY_myvar");

        values = new String[100];
        ids = new String[100];
        listview = (ListView) findViewById(R.id.listview);
        final ProgressDialog loading = ProgressDialog.show(this,"Getting Services...","Please wait...",false,false);

        //====================================== Volley Request ====================================
        RequestQueue queue = Volley.newRequestQueue(ProvidersList.this);
        String url = getString(R.string.ip)+"/FYP_PROJECT/ShowMarkers.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        ProviderJSONParser obj1 = new ProviderJSONParser();
                        res = obj1.showJSON(response);

                        int count;
                        for (count = 0 ;  count < res.size(); count++)
                        {
                            id = res.get(count).getId();
                            name = res.get(count).getName();
                            email = res.get(count).getEmail();
                            password = res.get(count).getPassword();
                            phone = res.get(count).getPhone();
                            address = res.get(count).getAddress();
                            service = res.get(count).getService();
                            details = res.get(count).getDetails();
                            longitude = res.get(count).getJSON_longitude();
                            latitude = res.get(count).getJSON_latitude();

                            values[count] = name;
                            ids[count] = id;
                        }


                        // =================== Adding to List View =================================================

                        final ArrayList<String> list = new ArrayList<String>();
                        for (int i = 0; i < count; ++i) {
                            list.add(values[i]);
                        }
                        adapter = new StableArrayAdapter(ProvidersList.this, android.R.layout.simple_list_item_1, list);
                        listview.setAdapter(adapter);

                        listItemClickFunction(); // Calling Function on Item Click.

                        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                           int pos, long id) {
                                // TODO Auto-generated method stub

                                final String item = (String) arg0.getItemAtPosition(pos);
                                onlong(item);
                                Log.v("long clicked","pos: " + pos);

                                return true;
                            }
                        });

                        loading.dismiss();

                    }
                },                 new Response.ErrorListener() {
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


                params.put("KEY_CategoryName", myvar);
                //returning parameters
                return params;
            }
        };
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    } // end of oncreate


    public void listItemClickFunction ()
    {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);


                for (int j = 0; j < res.size(); j++)
                {
                    if(item.equals(res.get(j).getName()))
                    {
                        _id = res.get(j).getId();
                        _lati = res.get(j).getJSON_latitude();
                        _longi = res.get(j).getJSON_longitude();
                        _name = res.get(j).getName();
                        _address = res.get(j).getAddress();
                        _phone = res.get(j).getPhone();
                        _details = res.get(j).getDetails();
                        _service = res.get(j).getService();

                        break;
                    }
                }

                view.animate().setDuration(100).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {

                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);

                                Intent i = new Intent(ProvidersList.this, Map.class);
                                i.putExtra("KEY_Lati", _lati);
                                i.putExtra("KEY_Longi", _longi);
                                i.putExtra("KEY_Name", _name);
                                i.putExtra("KEY_Address", _address);
                                i.putExtra("KEY_Phone", _phone);
                                i.putExtra("KEY_Details", _details);
                                i.putExtra("KEY_Service", _service);

                                startActivity(i);
                            }
                        });

            }
        });
    }
    public void onlong(String item)
    {
        //final String item = (String) parent.getItemAtPosition(position);


        for (int j = 0; j < res.size(); j++)
        {
            if(item.equals(res.get(j).getName()))
            {
                _id = res.get(j).getId();
                _lati = res.get(j).getJSON_latitude();
                _longi = res.get(j).getJSON_longitude();
                _name = res.get(j).getName();
                _address = res.get(j).getAddress();
                _phone = res.get(j).getPhone();
                _details = res.get(j).getDetails();
                _service = res.get(j).getService();

                break;
            }
        }


                        Intent i = new Intent(ProvidersList.this, ProviderInformation.class);
                        i.putExtra("KEY_Lati", _lati);
                        i.putExtra("KEY_Longi", _longi);
                        i.putExtra("KEY_Name", _name);
                        i.putExtra("KEY_Address", _address);
                        i.putExtra("KEY_Phone", _phone);
                        i.putExtra("KEY_Details", _details);
                        i.putExtra("KEY_Service", _service);

                        startActivity(i);




    }



    //====================================================================================================================
    // ============================== Pressing the Search Button of Dialog Box ===========================================
    //====================================================================================================================

    public void OnPress_XML_sortBtnImageView(View a)
    {


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_search, (ViewGroup) findViewById(R.id.XML_searchDialog));

        AlertDialog.Builder builder = new AlertDialog.Builder(ProvidersList.this);
        builder.setView(layout);


        builder.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Dialog d = new Dialog(ProvidersList.this);
                d.setContentView(R.layout.dialog_search);

                final Switch r = (Switch) d.findViewById(R.id.XML_s3);
                r.setChecked(true);

                if(r.isChecked())
                {
                    Toast.makeText(ProvidersList.this, "Hello", Toast.LENGTH_SHORT).show();
                    CallVolleyforSorting();
                }
                else
                {

                }


            }
        });
        builder.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.map, menu);
        MenuItem item = menu.findItem(R.id.menu_sortBtn);
        item.setActionView(R.layout.sorting_button_layout);

        return true;

    }

    public void CallVolleyforSorting()
    {

        Toast.makeText(ProvidersList.this, "Volley Started", Toast.LENGTH_SHORT).show();
        final ProgressDialog loading = ProgressDialog.show(ProvidersList.this,"Retriving Data...","Please wait...",false,false);
        RequestQueue queue = Volley.newRequestQueue(ProvidersList.this);
        String UPLOAD_URL = getString(R.string.ip)+"/FYP_PROJECT/getAlphaSorting.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Disimissing the progress dialog

                        Toast.makeText(ProvidersList.this, "Reponse inside Volley", Toast.LENGTH_SHORT).show();

                        ProviderJSONParser obj3 = new ProviderJSONParser();
                        res = obj3.showJSON(response);

                        int count;
                        for (count = 0 ;  count < res.size(); count++)
                        {
                            sorting_name = res.get(count).getName();

                            values[count] = sorting_name;
                            ids[count] = id;
                        }


                        // =================== Adding to List View =================================================

                        final ArrayList<String> list = new ArrayList<String>();
                        for (int i = 0; i < count; ++i) {
                            list.add(values[i]);
                        }
                        adapter = new StableArrayAdapter(ProvidersList.this, android.R.layout.simple_list_item_1, list);
                        listview.setAdapter(adapter);

                        listItemClickFunction(); // Calling Function on Item Click.
                        loading.dismiss();

                        //Intent i = new Intent(ProvidersList.this,ProvidersList.class);
                        //startActivity(i);

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

                params.put("KEY_CategoryName", myvar);
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

//====================================== Private Class ========================================
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }



    }



}
