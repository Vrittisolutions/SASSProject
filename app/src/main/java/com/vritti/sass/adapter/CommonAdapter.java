package com.vritti.sass.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.model.WorkHeight;

import java.util.ArrayList;
import java.util.Locale;



/**
 * Created by pradnya on 10/13/16.
 */
public class CommonAdapter extends BaseAdapter {
    ArrayList<WorkHeight> workHeightArrayList;
    LayoutInflater mInflater;
    Context context;

    public CommonAdapter(Context context1, ArrayList<WorkHeight> workHeightArrayList) {
        this.workHeightArrayList = workHeightArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return workHeightArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return workHeightArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.common_item_lay, null);
            holder = new ViewHolder();
            holder.checkbox_user= (AppCompatCheckBox) convertView.findViewById(R.id.checkbox_user);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkbox_user.setText(workHeightArrayList.get(position).getName());
        holder.checkbox_user.setChecked(workHeightArrayList.get(position).isSelected());



        holder.checkbox_user.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                boolean isSelected = ((AppCompatCheckBox)v).isChecked();
                workHeightArrayList.get(position).setSelected(isSelected);

            }
        });




        return convertView;
    }
    public ArrayList<WorkHeight> getArrayList(){
        ArrayList<WorkHeight> list = new ArrayList<>();
        for(int i=0;i<workHeightArrayList.size();i++){
            if(workHeightArrayList.get(i).isSelected())
                list.add(workHeightArrayList.get(i));
        }
        return list;
    }


    static class ViewHolder {
        TextView txt_user_name_data;
        AppCompatCheckBox checkbox_user;

    }


}
