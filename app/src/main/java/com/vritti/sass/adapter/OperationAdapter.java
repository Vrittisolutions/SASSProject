package com.vritti.sass.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.model.AuthorizedPerson;
import com.vritti.sass.model.Operation;

import java.util.ArrayList;


/**
 * Created by pradnya on 10/13/16.
 */
public class OperationAdapter extends BaseAdapter {
    ArrayList<Operation> operationArrayList;
    LayoutInflater mInflater;
    Context context;

    public OperationAdapter(Context context1, ArrayList<Operation> operationArrayList) {
        this.operationArrayList = operationArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return operationArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return operationArrayList.get(position);
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

        holder.txt.setText(operationArrayList.get(position).getOperation());








        return convertView;
    }



    static class ViewHolder {
        TextView txt;

    }


}
