package com.vritti.sass.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.model.PermitMenuList;
import com.vritti.sass.model.WorkHeight;

import java.util.ArrayList;


/**
 * Created by pradnya on 10/13/16.
 */
public class PermitMenuListAdapter extends BaseAdapter {
    ArrayList<PermitMenuList> permitMenuListArrayList;
    LayoutInflater mInflater;
    Context context;

    public PermitMenuListAdapter(Context context1, ArrayList<PermitMenuList> permitMenuListArrayList) {
        this.permitMenuListArrayList = permitMenuListArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return permitMenuListArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return permitMenuListArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.permit_menu_item, null);
            holder = new ViewHolder();
            holder.txt_permit_desc= (TextView) convertView.findViewById(R.id.txt_permit_desc);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_permit_desc.setText(permitMenuListArrayList.get(position).getFormDesc());









        return convertView;
    }



    static class ViewHolder {
        TextView txt_permit_desc;

    }


}
