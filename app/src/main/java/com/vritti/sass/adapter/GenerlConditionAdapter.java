package com.vritti.sass.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.model.IndicateRisk;

import java.util.ArrayList;


/**
 * Created by pradnya on 10/13/16.
 */
public class GenerlConditionAdapter extends BaseAdapter {
    ArrayList<IndicateRisk> indicateRiskArrayList;
    LayoutInflater mInflater;
    Context context;
    boolean isAns;
    String mode = "",permitStatus="";

    public GenerlConditionAdapter(Context context1, ArrayList<IndicateRisk> indicateRiskArrayList, String Mode,String PermitStatus) {
        this.indicateRiskArrayList = indicateRiskArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        this.mode = Mode;
        this.permitStatus = PermitStatus;

    }

    public GenerlConditionAdapter() {

    }

    @Override
    public int getCount() {
        return indicateRiskArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return indicateRiskArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.general_item_lay, null);
            holder = new ViewHolder();
            holder.checkbox_user = (AppCompatCheckBox) convertView.findViewById(R.id.checkbox_user);
            holder.txt_others =  convertView.findViewById(R.id.txt_others);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkbox_user.setText(indicateRiskArrayList.get(position).getSelectionText());
        holder.checkbox_user.setChecked(indicateRiskArrayList.get(position).isSelected());
        if(indicateRiskArrayList.get(position).getSelectionText().equalsIgnoreCase("Other(s) –Specify")
                || indicateRiskArrayList.get(position).getSelectionText().equalsIgnoreCase("Other(s)")) {

            if (indicateRiskArrayList.get(position).getRemarks() != null) {
                if(!indicateRiskArrayList.get(position).getRemarks().equals("")) {
                    indicateRiskArrayList.get(position).setSelected(true);
                    holder.txt_others.setVisibility(View.VISIBLE);
                    holder.txt_others.setText("Remark :" + indicateRiskArrayList.get(position).getRemarks());
                }else{
                    holder.txt_others.setVisibility(View.GONE);
                }
            }
        }


        if(permitStatus != null && (permitStatus.equalsIgnoreCase("A")
                || permitStatus.equalsIgnoreCase("C")
                || permitStatus.equalsIgnoreCase("R"))) {

            holder.checkbox_user.setClickable(false);

        }else{

            holder.checkbox_user.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    boolean isSelected = ((AppCompatCheckBox) v).isChecked();
                    final String text = indicateRiskArrayList.get(position).getSelectionText();
                    /*08F365BF-4B90-49EF-AFEF-15F5AEEA1389   ,   1E12389B-9589-49FB-AA10-67ED23B29F88*/

                    // holder.checkbox_user.setChecked(isSelected);
                    // indicateRiskArrayList.get(position).setSelected(isSelected);

                    if (text.equalsIgnoreCase("Other(s)")) {
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
                        if (indicateRiskArrayList != null) {
                            if (indicateRiskArrayList.get(position).getRemarks() != null) {
                                indicateRiskArrayList.get(position).setSelected(true);
                                String remarks = indicateRiskArrayList.get(position).getRemarks();
                                edit_remark.setText(remarks);
                            }
                        }
                        b.show();
                        txt_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String remarks = edit_remark.getText().toString();
                                if (remarks.equals("")) {
                                    indicateRiskArrayList.get(position).setSelected(false);
                                    indicateRiskArrayList.get(position).setRemarks(remarks);
                                    holder.checkbox_user.setChecked(false);
                                    holder.txt_others.setVisibility(View.GONE);

                                } else {
                                    indicateRiskArrayList.get(position).setSelected(true);
                                    indicateRiskArrayList.get(position).setRemarks(remarks);
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
                           /* holder.checkbox_user.setChecked(false);
                            indicateRiskArrayList.get(position).setRemarks("");*/


                                if (indicateRiskArrayList.get(position).getRemarks() == null ||
                                        indicateRiskArrayList.get(position).getRemarks().equalsIgnoreCase("")) {
                                    if (isAns) {
                                        // holder.checkbox_user.setChecked(false);
                                        if (!indicateRiskArrayList.get(position).getRemarks().equals("")) {
                                            indicateRiskArrayList.get(position).setSelected(true);
                                            indicateRiskArrayList.get(position).setRemarks(indicateRiskArrayList.get(position).getRemarks());
                                            holder.txt_others.setVisibility(View.VISIBLE);
                                            holder.txt_others.setText(indicateRiskArrayList.get(position).getRemarks());
                                            holder.checkbox_user.setChecked(true);

                                        } else {
                                            indicateRiskArrayList.get(position).setSelected(false);
                                            indicateRiskArrayList.get(position).setRemarks("");
                                            holder.checkbox_user.setChecked(false);
                                        }

                                    } else {

                                        indicateRiskArrayList.get(position).setSelected(false);
                                        indicateRiskArrayList.get(position).setRemarks("");
                                        holder.txt_others.setVisibility(View.GONE);
                                        holder.checkbox_user.setChecked(false);
                                    }

                                } else {
                                    // holder.checkbox_user.setChecked(true);
                                    if (indicateRiskArrayList.get(position).isSelected()) {
                                        if (indicateRiskArrayList.get(position).getRemarks().equals("")) {
                                            indicateRiskArrayList.get(position).setSelected(false);
                                            holder.checkbox_user.setChecked(false);
                                            indicateRiskArrayList.get(position).setRemarks("");
                                            holder.txt_others.setVisibility(View.GONE);
                                        } else {
                                            indicateRiskArrayList.get(position).setSelected(true);
                                            holder.checkbox_user.setChecked(true);
                                            holder.txt_others.setVisibility(View.VISIBLE);
                                            holder.txt_others.setText("Remark : " + indicateRiskArrayList.get(position).getRemarks());
                                            indicateRiskArrayList.get(position).setRemarks(indicateRiskArrayList.get(position).getRemarks());
                                        }
                                    } else {
                                        indicateRiskArrayList.get(position).setSelected(false);
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


                    } else {
                        holder.checkbox_user.setChecked(isSelected);
                        indicateRiskArrayList.get(position).setSelected(isSelected);

                    }
                }
            });


        }





        return convertView;
    }

    public ArrayList<IndicateRisk> getArrayList() {
        ArrayList<IndicateRisk> list = new ArrayList<>();
        for (int i = 0; i < indicateRiskArrayList.size(); i++) {
            if (indicateRiskArrayList.get(i).isSelected())
                list.add(indicateRiskArrayList.get(i));
        }
        return list;
    }


    static class ViewHolder {
        TextView txt_user_name_data;
        AppCompatCheckBox checkbox_user;
        TextView txt_others;

    }

}
