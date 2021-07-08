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
import com.vritti.sass.R;
import com.vritti.sass.model.Permit;

import java.util.ArrayList;


/**
 * Created by pradnya on 10/13/16.
 */
public class SpotCheckPermitAdapter extends BaseAdapter {

    private final Context context;
    ArrayList<Permit>permitArrayList ;
    LayoutInflater mInflater;

    public SpotCheckPermitAdapter(Context context, ArrayList<Permit> permitArrayList) {
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

        holder.txt_attachment1.setVisibility(View.GONE);


        String SpotImage = permitArrayList.get(position).getSpotImage();

        if(SpotImage.equals("")){
            holder.len.setVisibility(View.GONE);
            holder.txt_attachment.setVisibility(View.GONE);
        }else {
            holder.len.setVisibility(View.VISIBLE);
            holder.txt_loto_name.setText(SpotImage);
            holder.txt_attachment.setText("View");
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

                String Attachment=permitArrayList.get(position).getSpotImage();

                    context.startActivity(new Intent(context, ImageFullScreenActivity.class).putExtra("share_image_path",Attachment));



            }
        });







         return convertView;
    }



    static class ViewHolder {
        TextView txt_loto_name,txt_attachment,txt_attachment1;
        LinearLayout len;

    }


}
