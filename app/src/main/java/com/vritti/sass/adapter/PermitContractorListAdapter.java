package com.vritti.sass.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.model.Contractor;
import com.vritti.sass.model.ContractorList;

import java.util.ArrayList;


/**
 * Created by pradnya on 10/13/16.
 */
public class PermitContractorListAdapter extends BaseAdapter {
    ArrayList<ContractorList>contractorArrayList ;
    LayoutInflater mInflater;
    Context context;

    public PermitContractorListAdapter(Context context1, ArrayList<ContractorList> contractorArrayList) {
        this.contractorArrayList = contractorArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return contractorArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return contractorArrayList.get(position);
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

        holder.txt.setText(contractorArrayList.get(position).getCustVendorName());



         return convertView;
    }



    static class ViewHolder {
        TextView txt;

    }


}
