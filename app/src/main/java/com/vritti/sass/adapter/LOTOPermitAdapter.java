package com.vritti.sass.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.sass.ImageFullScreenActivity;
import com.vritti.sass.LOTOPermitActivity;
import com.vritti.sass.R;
import com.vritti.sass.model.Permit;

import java.util.ArrayList;


/**
 * Created by pradnya on 10/13/16.
 */
public class LOTOPermitAdapter extends BaseAdapter {

    private final Context context;
    ArrayList<Permit>permitArrayList ;
    LayoutInflater mInflater;

    public LOTOPermitAdapter(Context context, ArrayList<Permit> permitArrayList) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.permitArrayList = permitArrayList;
    }


    @Override
    public int getCount() {
        return permitArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return permitArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.loto_permit_item_lay, null);
            holder = new ViewHolder();
            holder.txt_loto_name= (TextView) convertView.findViewById(R.id.txt_loto_name);
            holder.txt_attachment=convertView.findViewById(R.id.txt_attachment);
            holder.txt_attachment1=convertView.findViewById(R.id.txt_attachment1);
            holder.len=convertView.findViewById(R.id.len);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        String IsolatedImageName = permitArrayList.get(position).getIsolatedImageName();
        String ElectricalImageName = permitArrayList.get(position).getElectricalImageName();
        String lotoPermitName = permitArrayList.get(position).getElectricalImageName();

        if(IsolatedImageName != null || IsolatedImageName != "" && ElectricalImageName != null || ElectricalImageName != "" ){
            holder.len.setVisibility(View.VISIBLE);
            holder.txt_loto_name.setText(lotoPermitName);
        }else{
            holder.len.setVisibility(View.GONE);
        }


        /*if (IsolatedImageName!=null){
            holder.txt_loto_name.setText("Isolated by valves");
        }else  if (ElectricalImageName!=null) {
            holder.txt_loto_name.setText("Electrical closing for works");

        }else {
            holder.len.setVisibility(View.GONE);
        }*/


        holder.txt_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Attachment=permitArrayList.get(position).getIsolatedImageName();
                String Attachment1=permitArrayList.get(position).getElectricalImageName();

                if (Attachment!=null){
                    context.startActivity(new Intent(context, ImageFullScreenActivity.class).putExtra("share_image_path",Attachment));
                }else {
                    context.startActivity(new Intent(context, ImageFullScreenActivity.class).putExtra("share_image_path",Attachment1));

                }


            }
        });

        holder.txt_attachment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Attachment=permitArrayList.get(position).getIsolatedImageName();
                String Attachment1=permitArrayList.get(position).getElectricalImageName();

                if (Attachment!=null){
                    context.startActivity(new Intent(context, ImageFullScreenActivity.class).putExtra("share_image_path",Attachment));
                }else {
                    context.startActivity(new Intent(context, ImageFullScreenActivity.class).putExtra("share_image_path",Attachment1));

                }


            }
        });





         return convertView;
    }



    static class ViewHolder {
        TextView txt_loto_name,txt_attachment,txt_attachment1;
        LinearLayout len;

    }


}
