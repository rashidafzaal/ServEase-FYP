package com.example.sheikhrashid.fyp;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Ghufran Shahid on 1/8/2017.
 */

public class ExpandableListAdapters extends BaseExpandableListAdapter {


    private Context _context;
    private List<String> header; // header titles
    // Child data in format of header title, child title
    private HashMap<String, List<String>> child;
    private  Integer[] imgid;

    public ExpandableListAdapters(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData,Integer[] imgid) {
        this._context = context;
        this.header = listDataHeader;
        this.child = listChildData;
        this.imgid=imgid;
    }

    @Override
    public int getGroupCount() {
        // Get header size
        return this.header.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // return children count
        return this.child.get(this.header.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // Get header position
        return this.header.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // This will return the child
        return this.child.get(this.header.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        // Getting header title
        String headerTitle = (String) getGroup(groupPosition);

        // Inflating header layout and setting text
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.services_sublist, parent, false);


        }

        //set content for the parent views

        ImageView imageView = (ImageView) convertView.findViewById(R.id.XML_icons);
        imageView.setImageResource(imgid[groupPosition]);
        TextView header_text = (TextView) convertView.findViewById(R.id.XML_sublist_txt);
        header_text.setText(headerTitle);
        header_text.setTextSize(21);

        // If group is expanded then change the text into bold and change the
        // icon
        if (isExpanded) {
            header_text.setTypeface(null, Typeface.NORMAL);
            header_text.setTextSize(21);
            header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrowup, 0);
        } else {
            // If group is not expanded then change the text back into normal
            // and change the icon
            header_text.setTextSize(21);
            header_text.setTypeface(null, Typeface.NORMAL);
            header_text.setTextColor(Color.BLACK);
            header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrowdown, 0);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // Getting child text
        final String childText = (String) getChild(groupPosition, childPosition);
        // Inflating child layout and setting textview
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.services_sublist_items, parent, false);
        }

        //set content in the child views
        TextView child_text = (TextView) convertView.findViewById(R.id.XML_items);

        child_text.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }


}
