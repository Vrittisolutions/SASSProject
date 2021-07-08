package com.vritti.sass.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.vritti.sass.R;
import com.vritti.sass.WorkAuthorizationActivity;
import com.vritti.sass.model.OperationGrpList;

import java.util.ArrayList;

/**
 * Created by pradnya on 10/13/16.
 */

public class WorkOperationAdapter extends BaseAdapter {
    ArrayList<OperationGrpList> OpearationList;
    LayoutInflater mInflater;
    Context context;
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;
    private RadioButton lastCheckedRB = null;
    private RadioButton lastCheckedRB1 = null;
    String Method_of_operation = "", radval = "";
    int check = 0;
    String mode = "", permitStatus = "";


    public WorkOperationAdapter(Context context1, ArrayList<OperationGrpList> OpearationList, String mode, String permitStatus) {
        this.OpearationList = OpearationList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        this.mode = mode;
        this.permitStatus = permitStatus;

    }


    @Override
    public int getCount() {
        return OpearationList.size();
    }

    @Override
    public Object getItem(int position) {
        return OpearationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        //final ViewHolder pos;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.operation_item_lay, null);
            holder = new ViewHolder();
            // holder.radiogrp_operation= (RadioGroup) convertView.findViewById(R.id.radiogrp_operation1);
            holder.check_yes = convertView.findViewById(R.id.check_yes);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }


        /*//final View finalConvertView = convertView;
        holder.radiogrp_operation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton checked_rb = (RadioButton) group.findViewById(checkedId);

               // checkedradiobtn(checked_rb);
                String operationlistname = OpearationList.get(position).getOperationDesc();
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                }
                lastCheckedRB = checked_rb;
              //lastCheckedRB.setChecked(true);
                radval = OpearationList.get(position).getOperationId();
            }
        });*/

        if (permitStatus.equals("R") || permitStatus.equals("C")) {
            holder.check_yes.setClickable(false);
        } else {


            holder.check_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSelected = ((AppCompatCheckBox) v).isChecked();
                    boolean notSeleted = !isSelected;

                    String check_btn = OpearationList.get(position).getOperationDesc();
                    if (check_btn.equalsIgnoreCase("None")) {
                        if (OpearationList.get(position).isChecked()) {
                            holder.check_yes.setChecked(false);
                            OpearationList.get(position).setChecked(isSelected);
                            // Toast.makeText(context, "You Cannot Check None Option ", Toast.LENGTH_SHORT).show();
                        } else {
                            int pos = -1;
                            for (int i = 1; i < OpearationList.size(); i++) {
                                if (OpearationList.get(i).isChecked()) {
                                    holder.check_yes.setChecked(false);
                                    OpearationList.get(position).setChecked(false);
                                    Toast.makeText(context, "You Cannot Check None Option ", Toast.LENGTH_SHORT).show();
                                    pos = i;
                                    break;
                                }
                            }
                            if (pos == -1) {
                                holder.check_yes.setChecked(true);
                                OpearationList.get(position).setChecked(isSelected);
                            }
                        }

                    } else {
                        if (OpearationList.get(0).isChecked()) {
                            OpearationList.get(position).setChecked(false);
                            holder.check_yes.setChecked(false);
                            Toast.makeText(context, "You cannot check as None Option is Checked", Toast.LENGTH_SHORT).show();
                        } else {
                            OpearationList.get(position).setChecked(isSelected);
                            if (OpearationList.get(position).isChecked()) {
                                holder.check_yes.setChecked(true);
                            } else {
                                holder.check_yes.setChecked(false);
                            }

                        }
                    }
                }
            });
        }

        holder.check_yes.setChecked(OpearationList.get(position).isChecked());
        holder.check_yes.setText(OpearationList.get(position).getOperationDesc());


        return convertView;
    }


    public String getcheckedradio() {

        String OperationCode = radval;

        return OperationCode;
    }


    public ArrayList<OperationGrpList> getArrayList() {
        ArrayList<OperationGrpList> list = new ArrayList<>();
        for (int i = 0; i < OpearationList.size(); i++) {
            if (OpearationList.get(i).isChecked())
                list.add(OpearationList.get(i));
        }
        return list;
    }


    static class ViewHolder {
        RadioGroup radiogrp_operation;
        AppCompatCheckBox check_yes;

    }


}
