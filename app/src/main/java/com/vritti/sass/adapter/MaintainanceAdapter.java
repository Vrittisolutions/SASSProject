package com.vritti.sass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.model.Contractor;

import java.util.ArrayList;

import static com.vritti.sass.WorkAuthorizationActivity.formateDateFromstring;

public class MaintainanceAdapter extends BaseAdapter {


    ArrayList<Contractor>maintainancearraylist ;
    LayoutInflater mInflater;
    Context context;

    public MaintainanceAdapter(Context context1, ArrayList<Contractor> maintainancearraylist) {
        this.maintainancearraylist = maintainancearraylist;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return maintainancearraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return maintainancearraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.maintain_item_lay, null);
            holder = new ViewHolder();
            holder.txt_maintain_name= (TextView) convertView.findViewById(R.id.txt_maintain_name);
            holder.txt_maintain_desc= (TextView) convertView.findViewById(R.id.txt_maintain_desc);
            holder.txt_start= (TextView) convertView.findViewById(R.id.txt_start);
            holder.txt_active= (TextView) convertView.findViewById(R.id.txt_active);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_maintain_name.setText(maintainancearraylist.get(position).getContractorName());
        holder.txt_maintain_desc.setText(maintainancearraylist.get(position).getInstrumentDescription());

        String dt = maintainancearraylist.get(position).getMaintenanceStartTime();
        String[] namesList = dt.split("T");
        String d = namesList [0];
        String t = namesList [1];
        String date=d+" "+t;

        String dt1 = maintainancearraylist.get(position).getMaintenanceActiveTime();
        String[] namesList1 = dt1.split("T");
        String d1 = namesList1 [0];
        String t1 = namesList1 [1];
        String date1=d1+" "+t1;


        String Start = formateDateFromstring("yyyy-MM-dd hh:mm:ss.SSS", "dd MMM yyyy hh:mm", date);
        String Active = formateDateFromstring("yyyy-MM-dd hh:mm:ss.SSS", "dd MMM yyyy hh:mm", date1);

        holder.txt_start.setText("Start Time : " + Start);

        holder.txt_active.setText("Active Time : " + Active);


        return convertView;
    }



    static class ViewHolder {
        TextView txt_maintain_name,txt_maintain_desc,txt_active,txt_start;

    }


}
