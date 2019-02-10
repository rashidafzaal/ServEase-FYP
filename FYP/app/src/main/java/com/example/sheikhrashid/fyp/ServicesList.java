package com.example.sheikhrashid.fyp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServicesList extends AppCompatActivity {

    ListView listView;
    String varName;


    // Array list for child items
    List<String> child1 = new ArrayList<String>();
    List<String> child2 = new ArrayList<String>();
    List<String> child3 = new ArrayList<String>();
    List<String> child4 = new ArrayList<String>();
    List<String> child5 = new ArrayList<String>();
    List<String> child6 = new ArrayList<String>();


    private static ExpandableListView expandableListView;
    public  ExpandableListAdapter adapter;
    HashMap<String, List<String>> hashMap;
    ArrayList<String> header;
    Integer[] imgid={
            R.drawable.truck,
            R.drawable.plumber,
            R.drawable.electrician,
            R.drawable.hammer,
            R.drawable.tailor,
            R.drawable.book,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list);


        expandableListView = (ExpandableListView) findViewById(R.id.services_list);
        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);

        setItems();

        adapter = new ExpandableListAdapters(ServicesList.this, header, hashMap,imgid);
        // Setting adpater for expandablelistview
        expandableListView.setAdapter(adapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        header.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        header.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Toast.makeText(getApplicationContext(), header.get(groupPosition) + " : "
                        + hashMap.get(header.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();





                if(groupPosition==0)
                {
                    if(childPosition==0)
                    {
                        varName = child1.get(0);
                        callVolley(varName);
                    }
                    if(childPosition==1)
                    {
                        varName = child1.get(1);
                        callVolley(varName);
                    }
                    if(childPosition==2)
                    {
                        varName = child1.get(2);
                        callVolley(varName);
                    }
                }


                if(groupPosition==1)
                {
                    if(childPosition==0)
                    {
                        varName = child2.get(0);
                        callVolley(varName);
                    }
                    if(childPosition==1)
                    {
                        varName = child2.get(1);
                        callVolley(varName);
                    }
                    if(childPosition==2)
                    {
                        varName = child2.get(2);
                        callVolley(varName);
                    }
                    if(childPosition==3)
                    {
                        varName = child2.get(3);
                        callVolley(varName);
                    }
                }
                if(groupPosition==2)
                {
                    if(childPosition==0)
                    {
                        varName = child3.get(0);
                        callVolley(varName);
                    }
                    if(childPosition==1)
                    {
                        varName = child3.get(1);
                        callVolley(varName);
                    }

                }
                if(groupPosition==3)
                {
                    if(childPosition==0)
                    {
                        varName = child4.get(0);
                        callVolley(varName);
                    }
                    if(childPosition==1)
                    {
                        varName = child4.get(1);
                        callVolley(varName);
                    }
                    if(childPosition==2)
                    {
                        varName = child4.get(2);
                        callVolley(varName);
                    }
                }
                if(groupPosition==4)
                {
                    if(childPosition==0)
                    {
                        varName = child5.get(0);
                        callVolley(varName);
                    }
                    if(childPosition==1)
                    {
                        varName = child5.get(1);
                        callVolley(varName);
                    }

                }
                if(groupPosition==5)
                {
                    if(childPosition==0)
                    {
                        varName = child6.get(0);
                        callVolley(varName);
                    }
                    if(childPosition==1)
                    {
                        varName = child6.get(1);
                        callVolley(varName);
                    }
                    if(childPosition==2)
                    {
                        varName = child6.get(2);
                        callVolley(varName);
                    }
                }
                  return false;
            }
        });
    }




    void setItems() {

        // Array list for header
        header = new ArrayList<String>();

        // Hash map for both header and child
        hashMap = new HashMap<String, List<String>>();

        // Adding headers to list

            header.add("Vehicles");
            header.add("Plumbers");
            header.add("Electricians");
            header.add("Labour");
            header.add("Tailors");
            header.add("Home Tutors");

        // Adding child data

            child1.add("Shifting Vehicles");
        child1.add("Towing Vehicles");
        child1.add("Trip Vehicles");

        // Adding child data

            child2.add("Water Plumber");
        child2.add("Sanitary Plumber");
        child2.add("Drainage Plumber");
        child2.add("Gas Plumber");

        // Adding child data

            child3.add("Commercial electrician");
        child3.add("Motor electrician");

        // Adding child data

            child4.add("Labour for Construction");
        child4.add("Labour for Towing");
        child4.add("Individual Labour");

        child5.add("Gents Tailor");
        child5.add("Ladies Tailor");


        child6.add("Science Subjects(Biology)");
        child6.add("Science Subjects(Mathematics)");
        child6.add("Arts Subjects");


        // Adding header and childs to hash map
        hashMap.put(header.get(0), child1);
        hashMap.put(header.get(1), child2);
        hashMap.put(header.get(2), child3);
        hashMap.put(header.get(3), child4);
        hashMap.put(header.get(4), child5);
        hashMap.put(header.get(5), child6);

    }


    public void callVolley(String myvar)
    {
        Intent i = new Intent(ServicesList.this, ProvidersList.class);
        i.putExtra("KEY_myvar", myvar);
        startActivity(i);
    }


}
