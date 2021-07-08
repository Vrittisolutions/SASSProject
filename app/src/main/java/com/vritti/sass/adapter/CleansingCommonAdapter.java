package com.vritti.sass.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.model.Cleansing;
import com.vritti.sass.model.WorkHeight;

import java.util.ArrayList;


/**
 * Created by pradnya on 10/13/16.
 */
public class CleansingCommonAdapter extends BaseAdapter {
    ArrayList<Cleansing> cleansingArrayList;
    LayoutInflater mInflater;
    Context context;
    ArrayList<Cleansing> arraylist;

    public CleansingCommonAdapter(Context context1, ArrayList<Cleansing> cleansingArrayList) {
        this.cleansingArrayList = cleansingArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        this.arraylist = new ArrayList<Cleansing>();
        this.arraylist.addAll(cleansingArrayList);

    }

    @Override
    public int getCount() {
        return cleansingArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return cleansingArrayList.get(position);
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

        holder.checkbox_user.setText(cleansingArrayList.get(position).getName());
        holder.checkbox_user.setChecked(cleansingArrayList.get(position).isSelected());




        holder.checkbox_user.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                boolean isSelected = ((AppCompatCheckBox)v).isChecked();
                cleansingArrayList.get(position).setSelected(isSelected);





            }
        });




        return convertView;
    }
    public ArrayList<Cleansing> getArrayList(){
        ArrayList<Cleansing> list = new ArrayList<>();
        for(int i=0;i<cleansingArrayList.size();i++){
            if(cleansingArrayList.get(i).isSelected())
                list.add(cleansingArrayList.get(i));
        }
        return list;
    }


    static class ViewHolder {
        TextView txt_user_name_data;
        AppCompatCheckBox checkbox_user;

    }


}
