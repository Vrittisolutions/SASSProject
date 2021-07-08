package com.vritti.sass.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.WorkAuthorizationActivity;

import java.util.ArrayList;

public class CompletePermitDetailsAdapter extends RecyclerView.Adapter<CompletePermitDetailsAdapter.CompleteAdapter> {
    Context context;
    ArrayList<String> stringArrayList;

    public CompletePermitDetailsAdapter(Context context, ArrayList<String> stringArrayList) {
        this.context= context;
        this.stringArrayList = stringArrayList;
    }

    @NonNull
    @Override
    public CompletePermitDetailsAdapter.CompleteAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.complete_permit_details_list, viewGroup, false);


        return new CompleteAdapter(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CompleteAdapter holder, int position) {
        try {
            String followupType = stringArrayList.get(position);
            holder.txt_permitName.setText(followupType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public class CompleteAdapter extends RecyclerView.ViewHolder {

        TextView txt_permitName;

        public CompleteAdapter(View itemView) {
            super(itemView);
            txt_permitName = itemView.findViewById(R.id.txt_permitName);
        }
    }


}
