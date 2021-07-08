package com.vritti.sass.adapter;

import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.model.Category;
import com.vritti.sass.model.ItemDetail;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    List<String>  listDataHeader_1;
    HashMap<String,List<String>> hashMapAllList;
    private int itemLayoutId;
    private int groupLayoutId;
    private Context ctx;

    public ExpandableAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>>  listchiddata) {

        this.ctx = context;
        this.listDataHeader_1 = listDataHeader;
        this.hashMapAllList = listchiddata;
        this.itemLayoutId = R.layout.item_layout;
        this.groupLayoutId = R.layout.group_layout;
    }



    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.hashMapAllList.get(this.listDataHeader_1.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
       // return catList.get(groupPosition).getItemList().get(childPosition).hashCode();
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childtext = (String)getChild(groupPosition,childPosition);

        //View v = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_layout,null);
        }

        //TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        TextView itemDescr = (TextView) convertView.findViewById(R.id.itemDescr);

        /*itemName.setText(det.getName());
        itemDescr.setText(det.getDescr());*/


       // itemName.setText(childtext.getName());
        itemDescr.setText(childtext);


        return convertView ;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
     //   int size = catList.get(groupPosition).getItemList().size();
      //  System.out.println("Child for group ["+groupPosition+"] is ["+size+"]");
        return (this.hashMapAllList.get(this.listDataHeader_1.get(groupPosition)).size());
    }

    @Override
    public Object getGroup(int groupPosition) {

        return this.listDataHeader_1.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader_1.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String)getGroup(groupPosition);

        //View convertView = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_layout, null);
        }

        TextView groupName = (TextView) convertView.findViewById(R.id.groupName);
        //TextView groupDescr = (TextView) convertView.findViewById(R.id.groupDescr);


        /*Category cat = catList.get(groupPosition);

        groupName.setText(cat.getName());
        groupDescr.setText(cat.getDescr());*/
        groupName.setTypeface(null, Typeface.BOLD);
        groupName.setText(headerTitle);

        /*Toast.makeText(
                getApplicationContext(),
                listDataHeader.get(groupPosition)
                        + " : "
                        + listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition), Toast.LENGTH_SHORT)
                .show();*/

        return convertView;

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}