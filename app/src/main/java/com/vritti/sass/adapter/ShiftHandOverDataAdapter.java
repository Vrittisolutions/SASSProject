package com.vritti.sass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.ShiftHandOverDisplayActivity;
import com.vritti.sass.model.Contractor;
import com.vritti.sass.model.ShiftHandOver;

import java.util.ArrayList;

import static com.vritti.sass.WorkAuthorizationActivity.formateDateFromstring;

public class ShiftHandOverDataAdapter extends BaseAdapter {


    ArrayList<ShiftHandOver>shiftHandOverArrayList ;
    LayoutInflater mInflater;
    Context context;

    public ShiftHandOverDataAdapter(Context context1, ArrayList<ShiftHandOver> shiftHandOverArrayList) {
        this.shiftHandOverArrayList = shiftHandOverArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return shiftHandOverArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return shiftHandOverArrayList.get(position);
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
            holder.card_view= (CardView) convertView.findViewById(R.id.card_view);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_active.setVisibility(View.GONE);

        holder.txt_maintain_name.setText(shiftHandOverArrayList.get(position).getAddedBy());
        holder.txt_maintain_desc.setText(shiftHandOverArrayList.get(position).getRemark());

        String dt = shiftHandOverArrayList.get(position).getAddedDt();
        String[] namesList = dt.split("T");
        String d = namesList [0];
        String t = namesList [1];
        String date=d+" "+t;

        String date_after = formateDateFromstring("yyyy-MM-dd hh:mm:ss", "dd MMM yyyy", date);
        holder.txt_start.setText(date_after);

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ShiftHandOverDisplayActivity.class)
                .putExtra("PlantDowngardeCondition", shiftHandOverArrayList.get(position).getPlantDowngardeCondition())
                        .putExtra("Hotwork", shiftHandOverArrayList.get(position).getHotwork())
                        .putExtra("PermitIssue", shiftHandOverArrayList.get(position).getPermitIssue())
                        .putExtra("LotoPermit", shiftHandOverArrayList.get(position).getLotoPermit())
                        .putExtra("PlantStatus", shiftHandOverArrayList.get(position).getPlantStatus())
                        .putExtra("Remark", shiftHandOverArrayList.get(position).getRemark())
                        .putExtra("Que1", shiftHandOverArrayList.get(position).getQue1())
                        .putExtra("Que2", shiftHandOverArrayList.get(position).getQue2())
                        .putExtra("Que3", shiftHandOverArrayList.get(position).getQue3())
                        .putExtra("Ans1", shiftHandOverArrayList.get(position).getAns1())
                        .putExtra("Ans2", shiftHandOverArrayList.get(position).getAns2())
                        .putExtra("Ans3", shiftHandOverArrayList.get(position).getAns3())
                        .putExtra("AddedBy", shiftHandOverArrayList.get(position).getAddedBy()));
            }
        });

        return convertView;
    }



    static class ViewHolder {
        TextView txt_maintain_name,txt_maintain_desc,txt_active,txt_start;
        CardView card_view;

    }


}
