package com.vritti.sass;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import static com.vritti.sass.WorkAuthorizationActivity.formateDateFromstring;


/**
 * Created by 300151 on 10/13/16.
 */
public class DowngradeConditionAdapter extends BaseAdapter {
    ArrayList<DowngradeCondition> downgradeConditionArrayList ;
    LayoutInflater mInflater;
    Context context;
    AlertDialog alertDialog;

    public DowngradeConditionAdapter(Context context1, ArrayList<DowngradeCondition> downgradeConditionArrayList) {
        this.downgradeConditionArrayList = downgradeConditionArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;


    }

    @Override
    public int getCount() {
        return downgradeConditionArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return downgradeConditionArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_text, null);

            holder.txt_product = (TextView) convertView.findViewById(R.id.txt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String dt1 = downgradeConditionArrayList.get(position).getDateTime1();
        String[] namesList1 = dt1.split("T");
        String d1 = namesList1 [0];
        String t1 = namesList1 [1];
        String date1=d1+" "+t1;


        String Start = formateDateFromstring("yyyy-MM-dd hh:mm:ss.SSS", "dd MMM yyyy hh:mm", date1);
        holder.txt_product.setText(downgradeConditionArrayList.get(position).getDowngradConditions()+"\n"+Start);

        return convertView;
    }

    static class ViewHolder {
        TextView txt_product;
    }
}
