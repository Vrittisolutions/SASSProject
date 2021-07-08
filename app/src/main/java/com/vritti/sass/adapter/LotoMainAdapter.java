package com.vritti.sass.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.model.Permit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LotoMainAdapter extends BaseAdapter {

    ArrayList<Permit> permitArrayList ;
    LayoutInflater mInflater;
    Context context;

    public LotoMainAdapter(Context context1, ArrayList<Permit> permitArrayList) {
        this.permitArrayList = permitArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return permitArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return permitArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.permit_item_list_lay, null);
            holder = new ViewHolder();
            holder.txt_permit= (TextView) convertView.findViewById(R.id.txt_permit);
            holder.txt_nature_operation= (TextView) convertView.findViewById(R.id.txt_nature_operation);
            holder.txt_location_operation= (TextView) convertView.findViewById(R.id.txt_location_operation);
            holder.text_date= (TextView) convertView.findViewById(R.id.text_date);
            holder.rel_main=  convertView.findViewById(R.id.rel_main);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(permitArrayList.get(position).getIsolatedImageName() != ""
                || permitArrayList.get(position).getElectricalImageName() != ""){
            holder.rel_main.setVisibility(View.VISIBLE);
            holder.txt_permit.setText(permitArrayList.get(position).getPermitNo());
            holder.txt_nature_operation.setText(permitArrayList.get(position).getNature_Operation());
            holder.txt_location_operation.setText(permitArrayList.get(position).getLocation_operation());
            String date;
            String dt = permitArrayList.get(position).getAddedDt();

            date = formateDateFromstring("yyyy-MM-dd hh:mm:ss aa", "dd MMM", dt);
            holder.text_date.setText(date);
        }else{
            holder.rel_main.setVisibility(View.GONE);
        }



        return convertView;
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e("Error in parsing Date",e.getMessage());

        }

        return outputDate;

    }

    static class ViewHolder {
        TextView txt_permit,txt_nature_operation,txt_location_operation,text_date;
        RelativeLayout rel_main;

    }


}
