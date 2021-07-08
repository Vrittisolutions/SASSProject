package com.vritti.sass.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.sass.CleansingPermitActivity;
import com.vritti.sass.ConfinedSpaceEntryActivity;
import com.vritti.sass.ExcavationPermitActivity;
import com.vritti.sass.HOTWorkActivity;
import com.vritti.sass.LiftingPermitActivity;
import com.vritti.sass.R;
import com.vritti.sass.WorkAtHeightPermitActivity;
import com.vritti.sass.model.SafetyTools;

import java.util.ArrayList;


/**
 * Created by pradnya on 10/13/16.
 */
public class SafetyAdapter extends BaseAdapter {
    ArrayList<SafetyTools> safetyToolsArrayList;
    LayoutInflater mInflater;
    Context context;
    String permitName = "";
    String permitStatus = "",mode = "";

    public SafetyAdapter(Context context1, ArrayList<SafetyTools> safetyToolsArrayList, String permitName, String Mode,String PermitStatus) {
        this.safetyToolsArrayList = safetyToolsArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        this.permitName = permitName;
        this.mode = Mode;
        this.permitStatus = PermitStatus;

    }

    @Override
    public int getCount() {
        return safetyToolsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return safetyToolsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.safetytools_item_lay, null);
            holder = new ViewHolder();
            holder.checkbox_user = (AppCompatCheckBox) convertView.findViewById(R.id.checkbox_user);
            holder.txt_others =  convertView.findViewById(R.id.txt_others);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.checkbox_user.setButtonDrawable(context.getResources().getDrawable(R.drawable.checkbox_style));


        holder.checkbox_user.setText(safetyToolsArrayList.get(position).getSafetyToolDesc());
        holder.checkbox_user.setChecked(safetyToolsArrayList.get(position).isSelected());

        if(safetyToolsArrayList.get(position).getSafetyToolDesc().equalsIgnoreCase("Other(s) –Specify") ||
                safetyToolsArrayList.get(position).getSafetyToolDesc().equalsIgnoreCase("Other(s)")) {
            if (safetyToolsArrayList.get(position).getRemarks() != null) {
                if(!safetyToolsArrayList.get(position).getRemarks().equals("")) {
                    holder.txt_others.setVisibility(View.VISIBLE);
                    holder.txt_others.setText("Remark :" + safetyToolsArrayList.get(position).getRemarks());
                }else{
                    holder.txt_others.setVisibility(View.VISIBLE);
                    holder.txt_others.setVisibility(View.GONE);
                    safetyToolsArrayList.get(position).setSelected(false);
                    holder.checkbox_user.setChecked(safetyToolsArrayList.get(position).isSelected());
                }
            }
        }


        holder.checkbox_user.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                boolean isSelected = ((AppCompatCheckBox) v).isChecked();

                if(permitStatus != null && (permitStatus.equalsIgnoreCase("A")
                        || permitStatus.equalsIgnoreCase("C")
                        || permitStatus.equalsIgnoreCase("R"))){
                    holder.checkbox_user.setClickable(false);

                    if (safetyToolsArrayList.get(position).isSelected()) {
                        holder.checkbox_user.setChecked(true);
                    } else {
                        holder.checkbox_user.setChecked(false);
                    }

                }
                else{
                    safetyToolsArrayList.get(position).setSelected(isSelected);
                    holder.checkbox_user.setChecked(isSelected);

                    if (safetyToolsArrayList.get(position).getSafetyToolDesc().equals("Other(s)")) {
                        if (permitName.equalsIgnoreCase("HW")) {
                            ((HOTWorkActivity) context).reasonDialog_SafetyTools(position, safetyToolsArrayList.get(position).getSafetyToolMasterId());
                        } else if (permitName.equalsIgnoreCase("CAD")) {
                            ((CleansingPermitActivity) context).reasonDialog_SafetyTools(position, safetyToolsArrayList.get(position).getSafetyToolMasterId());
                        } else if (permitName.equalsIgnoreCase("CSE")) {
                            ((ConfinedSpaceEntryActivity) context).reasonDialog_SafetyTools(position, safetyToolsArrayList.get(position).getSafetyToolMasterId());
                        } else if (permitName.equalsIgnoreCase("WAH")) {
                            ((WorkAtHeightPermitActivity) context).reasonDialog_SafetyTools(position, safetyToolsArrayList.get(position).getSafetyToolMasterId());
                        } else if (permitName.equalsIgnoreCase("LP")) {
                            ((LiftingPermitActivity) context).reasonDialog_SafetyTools(position, safetyToolsArrayList.get(position).getSafetyToolMasterId());
                        } else if (permitName.equalsIgnoreCase("EP")) {
                            ((ExcavationPermitActivity) context).reasonDialog_SafetyTools(position, safetyToolsArrayList.get(position).getSafetyToolMasterId());
                        } else {

                        }
                    }
                }


                /*if (status.equals("A")) {
                    boolean isSelected = ((AppCompatCheckBox) v).isChecked();

                    if(isSelected){
                        safetyToolsArrayList.get(position).setSelected(false);
                        holder.checkbox_user.setChecked(false);
                    }else{
                        safetyToolsArrayList.get(position).setSelected(true);
                        holder.checkbox_user.setChecked(true);
                    }

                }
                else {
                    boolean isSelected = ((AppCompatCheckBox) v).isChecked();
                    safetyToolsArrayList.get(position).setSelected(isSelected);

                    if (safetyToolsArrayList.get(position).getSafetyToolDesc().equals("Other(s)")) {
                        if (permitName.equalsIgnoreCase("HW")) {
                            ((HOTWorkActivity) context).reasonDialog_SafetyTools(position, safetyToolsArrayList.get(position).getSafetyToolMasterId());
                        } else if (permitName.equalsIgnoreCase("CAD")) {
                            ((CleansingPermitActivity) context).reasonDialog_SafetyTools(position, safetyToolsArrayList.get(position).getSafetyToolMasterId());
                        } else if (permitName.equalsIgnoreCase("CSE")) {
                            ((ConfinedSpaceEntryActivity) context).reasonDialog_SafetyTools(position, safetyToolsArrayList.get(position).getSafetyToolMasterId());
                        } else if (permitName.equalsIgnoreCase("WAH")) {
                            ((WorkAtHeightPermitActivity) context).reasonDialog_SafetyTools(position, safetyToolsArrayList.get(position).getSafetyToolMasterId());
                        } else if (permitName.equalsIgnoreCase("LP")) {
                            ((LiftingPermitActivity) context).reasonDialog_SafetyTools(position, safetyToolsArrayList.get(position).getSafetyToolMasterId());
                        } else if (permitName.equalsIgnoreCase("EP")) {
                            ((ExcavationPermitActivity) context).reasonDialog_SafetyTools(position, safetyToolsArrayList.get(position).getSafetyToolMasterId());
                        } else {

                        }
                    }

                }*/

            }
        });


        return convertView;
    }

    public ArrayList<SafetyTools> getArrayList() {
        ArrayList<SafetyTools> list = new ArrayList<>();
        for (int i = 0; i < safetyToolsArrayList.size(); i++) {
            if (safetyToolsArrayList.get(i).isSelected())
                list.add(safetyToolsArrayList.get(i));
        }
        return list;
    }

    public void updateList(ArrayList<SafetyTools> safetyToolsArrayList) {
        this.safetyToolsArrayList = safetyToolsArrayList;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        TextView txt_user_name_data,txt_others;
        AppCompatCheckBox checkbox_user;

    }


}
