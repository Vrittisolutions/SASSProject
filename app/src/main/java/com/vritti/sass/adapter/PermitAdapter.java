package com.vritti.sass.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.model.DateFormatChange;
import com.vritti.sass.model.Permit;
import com.vritti.sass.model.WorkHeight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by pradnya on 10/13/16.
 */
public class PermitAdapter extends BaseAdapter {
    ArrayList<Permit> permitArrayList;
    LayoutInflater mInflater;
    Context context;

    public PermitAdapter(Context context, ArrayList<Permit> permitArrayList) {
        this.permitArrayList = permitArrayList;
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.permit_item_list_lay, null);
            holder = new ViewHolder();
            holder.txt_permit = (TextView) convertView.findViewById(R.id.txt_permit);
            holder.txt_nature_operation = (TextView) convertView.findViewById(R.id.txt_nature_operation);
            holder.txt_location_operation = (TextView) convertView.findViewById(R.id.txt_location_operation);
            holder.text_date = (TextView) convertView.findViewById(R.id.text_date);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_permit.setText(permitArrayList.get(position).getPermitNo());
        holder.txt_nature_operation.setText(permitArrayList.get(position).getNature_Operation());
        holder.txt_location_operation.setText(permitArrayList.get(position).getLocation_operation());


        String date = "";
        String AddedDt = permitArrayList.get(position).getAddedDt();
        String[] addedDt = AddedDt.split(" ");
        if (addedDt[0].contains("-")) {
            date = DateFormatChange.formateDateFromstring("yyyy-MM-dd", "dd MMM", addedDt[0]);
        } else if (addedDt[0].contains("/")) {
            date = DateFormatChange.formateDateFromstring("MM/dd/yyyy", "dd MMM", addedDt[0]);
        }
        //date = formateDateFromstring("MM/dd/yyyy hh:mm:ss aa", "dd MMM", dt);


           /*if(permitArrayList.get(position).getPermitNo().contains("WA/")){
              date = formateDateFromstring("yyyy-MM-dd", "dd MMM", dt);
           }else{*/

        //}


        holder.text_date.setText(date);


        return convertView;
    }

    public void updateList(ArrayList<Permit> permitArrayList) {

        this.permitArrayList = permitArrayList;
        notifyDataSetChanged();


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
            Log.e("Error in parsing Date", e.getMessage());

        }

        return outputDate;

    }

    static class ViewHolder {
        TextView txt_permit, txt_nature_operation, txt_location_operation, text_date;

    }


}
