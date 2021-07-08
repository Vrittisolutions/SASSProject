package com.vritti.sass.adapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.sass.R;
import com.vritti.sass.model.EquipmentUse;
import com.vritti.sass.model.Prevention;

import java.util.ArrayList;


/**
 * Created by pradnya on 10/13/16.
 */
public class PrevenionAdapter extends BaseAdapter {
    ArrayList<Prevention> preventionArrayList;
    LayoutInflater mInflater;
    Context context;
    boolean isAns;
    String mode="",permitStatus="";

    public PrevenionAdapter(Context context1, ArrayList<Prevention> preventionArrayList,String Mode,String PermitStatus) {
        this.preventionArrayList = preventionArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        this.mode = Mode;
        this.permitStatus = PermitStatus;

    }

    @Override
    public int getCount() {
        return preventionArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return preventionArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.workauthorization_item_lay_demo, null);
            holder = new ViewHolder();
            holder.checkbox_user = (AppCompatCheckBox) convertView.findViewById(R.id.checkbox_user);
            holder.txt_others = convertView.findViewById(R.id.txt_others);
            // holder.txt_title = convertView.findViewById(R.id.txt_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkbox_user.setButtonDrawable(context.getResources().getDrawable(R.drawable.checkbox_style));


        holder.checkbox_user.setText(preventionArrayList.get(position).getSelectionText());
        holder.checkbox_user.setChecked(preventionArrayList.get(position).isSelected());
        if(preventionArrayList.get(position).getSelectionText().equalsIgnoreCase("Other(s) –Specify")
                || preventionArrayList.get(position).getSelectionText().equalsIgnoreCase("Other(s)")) {
            if (preventionArrayList.get(position).getRemarks() != null) {
                if(!preventionArrayList.get(position).getRemarks().equals("")){
                    preventionArrayList.get(position).setSelected(true);
                    holder.txt_others.setVisibility(View.VISIBLE);
                    holder.txt_others.setText("Remark :" + preventionArrayList.get(position).getRemarks());
                }else{
                    preventionArrayList.get(position).setSelected(false);
                    holder.checkbox_user.setChecked(preventionArrayList.get(position).isSelected());
                    holder.txt_others.setVisibility(View.GONE);
                    holder.txt_others.setText("Remark :" + preventionArrayList.get(position).getRemarks());
                }
            }else{
               /* holder.txt_others.setVisibility(View.GONE);
                preventionArrayList.get(position).setSelected(false);
                holder.checkbox_user.setChecked(preventionArrayList.get(position).isSelected());*/
            }
        }
        if(permitStatus != null && (permitStatus.equalsIgnoreCase("A")
                || permitStatus.equalsIgnoreCase("C")
                || permitStatus.equalsIgnoreCase("R"))){

            holder.checkbox_user.setClickable(false);

        }else{

            holder.checkbox_user.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {

                    final String Remarks;
                    boolean isSelected = ((AppCompatCheckBox) v).isChecked();
                    final String text = preventionArrayList.get(position).getSelectionText();



                    if (text.equalsIgnoreCase("Other(s) –Specify")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = mInflater.from(context);
                        final View dialogView = inflater.inflate(R.layout.remarks, null);
                        builder.setView(dialogView);
                        final EditText edit_remark = dialogView.findViewById(R.id.edit_remarks);
                        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
                        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);


                        builder.setCancelable(false);
                        final AlertDialog b = builder.create();
                        b.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        if (preventionArrayList != null) {
                            if (preventionArrayList.get(position).getRemarks() != null) {
                                preventionArrayList.get(position).setSelected(true);
                                String remarks = preventionArrayList.get(position).getRemarks();
                                edit_remark.setText(remarks);
                            }
                        }
                        b.show();
                        txt_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String remarks = edit_remark.getText().toString();
                                if(remarks.equals("")){
                                    preventionArrayList.get(position).setSelected(false);
                                    preventionArrayList.get(position).setRemarks(remarks);
                                    holder.checkbox_user.setChecked(false);
                                    holder.txt_others.setVisibility(View.GONE);
                                }else{
                                    preventionArrayList.get(position).setSelected(true);
                                    preventionArrayList.get(position).setRemarks(remarks);
                                    holder.checkbox_user.setChecked(true);
                                    holder.txt_others.setVisibility(View.VISIBLE);
                                    holder.txt_others.setText("Remark : " + remarks);
                                }

                                b.dismiss();
                            }
                        });
                        txt_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (preventionArrayList.get(position).getRemarks() == null ||
                                        preventionArrayList.get(position).getRemarks().equalsIgnoreCase("")) {
                                    if (isAns) {
                                        // holder.checkbox_user.setChecked(false);
                                        if (!preventionArrayList.get(position).getRemarks().equals("")) {
                                            preventionArrayList.get(position).setSelected(true);
                                            preventionArrayList.get(position).setRemarks(preventionArrayList.get(position).getRemarks());
                                            holder.txt_others.setVisibility(View.VISIBLE);
                                            holder.txt_others.setText(preventionArrayList.get(position).getRemarks());
                                            holder.checkbox_user.setChecked(true);

                                        }else {
                                            preventionArrayList.get(position).setSelected(false);
                                            preventionArrayList.get(position).setRemarks("");
                                            holder.checkbox_user.setChecked(false);
                                        }

                                    }
                                    else {

                                        preventionArrayList.get(position).setSelected(false);
                                        preventionArrayList.get(position).setRemarks("");
                                        holder.txt_others.setVisibility(View.GONE);
                                        holder.checkbox_user.setChecked(false);

                                    }

                                } else {
                                    if (preventionArrayList.get(position).isSelected()) {
                                        if(preventionArrayList.get(position).getRemarks().equals("")){
                                            preventionArrayList.get(position).setSelected(false);
                                            holder.checkbox_user.setChecked(false);
                                            preventionArrayList.get(position).setRemarks("");
                                            holder.txt_others.setVisibility(View.GONE);
                                        }else {
                                            preventionArrayList.get(position).setSelected(true);
                                            holder.checkbox_user.setChecked(true);
                                            holder.txt_others.setVisibility(View.VISIBLE);
                                            holder.txt_others.setText("Remark : " + preventionArrayList.get(position).getRemarks());
                                            preventionArrayList.get(position).setRemarks(preventionArrayList.get(position).getRemarks());
                                        }
                                    } else {
                                        preventionArrayList.get(position).setSelected(false);
                                        holder.checkbox_user.setChecked(false);

                                    }

                                }
                                b.dismiss();
                            }
                        });

                        edit_remark.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                                if (s.toString().equals("")) {
                                    isAns = false;
                                } else {
                                    isAns = true;
                                }

                            }
                        });



                    }
                    else if (text.equalsIgnoreCase("Other(s)")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = mInflater.from(context);
                        final View dialogView = inflater.inflate(R.layout.remarks, null);
                        builder.setView(dialogView);
                        final EditText edit_remark = dialogView.findViewById(R.id.edit_remarks);
                        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
                        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);


                        builder.setCancelable(false);
                        final AlertDialog b = builder.create();
                        b.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        if(preventionArrayList != null){
                            if(preventionArrayList.get(position).getRemarks() != null){
                                String remarks = preventionArrayList.get(position).getRemarks();
                                preventionArrayList.get(position).setSelected(true);
                                edit_remark.setText(remarks);
                            }
                        }

                        b.show();
                        txt_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                String remarks = edit_remark.getText().toString();
                                if(remarks.equals("")){
                                    preventionArrayList.get(position).setSelected(false);
                                    preventionArrayList.get(position).setRemarks(remarks);
                                    holder.checkbox_user.setChecked(false);
                                    holder.txt_others.setVisibility(View.GONE);

                                }else{
                                    preventionArrayList.get(position).setSelected(true);
                                    preventionArrayList.get(position).setRemarks(remarks);
                                    holder.checkbox_user.setChecked(true);
                                    holder.txt_others.setVisibility(View.VISIBLE);
                                    holder.txt_others.setText("Remark : " + remarks);
                                }

                                b.dismiss();
                            }
                        });
                        txt_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (preventionArrayList.get(position).getRemarks() == null ||
                                        preventionArrayList.get(position).getRemarks().equalsIgnoreCase("")) {
                                    if (isAns) {
                                        if (!preventionArrayList.get(position).getRemarks().equals("")) {
                                            preventionArrayList.get(position).setSelected(true);
                                            preventionArrayList.get(position).setRemarks(preventionArrayList.get(position).getRemarks());
                                            holder.txt_others.setVisibility(View.VISIBLE);
                                            holder.txt_others.setText(preventionArrayList.get(position).getRemarks());

                                        }else {
                                            preventionArrayList.get(position).setSelected(false);
                                            preventionArrayList.get(position).setRemarks("");
                                        }

                                    } else {
                                        preventionArrayList.get(position).setSelected(false);
                                        preventionArrayList.get(position).setRemarks("");
                                        holder.txt_others.setVisibility(View.GONE);

                                    }

                                } else {
                                    if (preventionArrayList.get(position).isSelected()) {
                                        if(preventionArrayList.get(position).getRemarks().equals("")){
                                            preventionArrayList.get(position).setSelected(false);
                                            holder.checkbox_user.setChecked(false);
                                            preventionArrayList.get(position).setRemarks("");
                                            holder.txt_others.setVisibility(View.GONE);
                                        }else {
                                            preventionArrayList.get(position).setSelected(true);
                                            holder.checkbox_user.setChecked(true);
                                            holder.txt_others.setVisibility(View.VISIBLE);
                                            holder.txt_others.setText("Remark : " + preventionArrayList.get(position).getRemarks());
                                            preventionArrayList.get(position).setRemarks(preventionArrayList.get(position).getRemarks());
                                        }
                                    } else {
                                        preventionArrayList.get(position).setSelected(false);

                                    }
                                }
                                b.dismiss();
                            }
                        });

                        edit_remark.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {


                                if (s.toString().equals("")) {
                                    isAns = false;
                                } else {
                                    isAns = true;
                                }

                            }
                        });

                    } else {
                        holder.checkbox_user.setChecked(isSelected);
                        preventionArrayList.get(position).setSelected(isSelected);

                    }
                }

            });

        }

        return convertView;
    }

    public ArrayList<Prevention> getArrayList() {
        ArrayList<Prevention> list = new ArrayList<>();
        for (int i = 0; i < preventionArrayList.size(); i++) {
            if (preventionArrayList.get(i).isSelected())
                list.add(preventionArrayList.get(i));
        }
        return list;
    }


    static class ViewHolder {
        TextView txt_user_name_data;
        TextView txt_others,txt_title;
        AppCompatCheckBox checkbox_user;


    }

}
