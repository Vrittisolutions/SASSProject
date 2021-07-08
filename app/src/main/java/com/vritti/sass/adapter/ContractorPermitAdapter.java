package com.vritti.sass.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.sass.ContractorListActivity;
import com.vritti.sass.R;
import com.vritti.sass.model.Contractor;
import com.vritti.sass.model.Depot;
import com.vritti.sass.model.PermitNoWA;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by pradnya on 10/13/16.
 */
public class ContractorPermitAdapter extends BaseAdapter {

    ArrayList<PermitNoWA> permitNoWAArrayList;
    LayoutInflater mInflater;
    Context context;

    public ContractorPermitAdapter(Context context1, ArrayList<PermitNoWA> permitNoWAArrayList) {
        this.permitNoWAArrayList = permitNoWAArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return permitNoWAArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return permitNoWAArrayList.get(position);
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

        holder.txt.setText(permitNoWAArrayList.get(position).getPermitNo());








        return convertView;
    }



    static class ViewHolder {
        TextView txt;

    }


}
