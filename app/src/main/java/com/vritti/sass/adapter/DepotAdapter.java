package com.vritti.sass.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.model.Depot;
import com.vritti.sass.model.WorkHeight;

import java.util.ArrayList;


/**
 * Created by pradnya on 10/13/16.
 */
public class DepotAdapter extends BaseAdapter {
    ArrayList<Depot> depotArrayList;
    LayoutInflater mInflater;
    Context context;

    public DepotAdapter(Context context1, ArrayList<Depot> depotArrayList) {
        this.depotArrayList = depotArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return depotArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return depotArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_text, null);
            holder = new ViewHolder();
            holder.txt= (TextView) convertView.findViewById(R.id.txt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt.setText(depotArrayList.get(position).getDepotname());








        return convertView;
    }



    static class ViewHolder {
        TextView txt;

    }


}
