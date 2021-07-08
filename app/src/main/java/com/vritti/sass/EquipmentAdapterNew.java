package com.vritti.sass;

import com.vritti.sass.adapter.EquipmentAdapter;
import com.vritti.sass.model.EquipmentUse;

import java.util.ArrayList;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.model.EquipmentUse;
import com.vritti.sass.model.IndicateRisk;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pradnya on 10/13/16.
 */
public class EquipmentAdapterNew extends BaseAdapter {
    ArrayList<EquipmentUse> equipmentUseArrayList;
    LayoutInflater mInflater;
    Context context;
    boolean isAns;
    String mode = "", permitStatus = "";

    public EquipmentAdapterNew(Context context1, ArrayList<EquipmentUse> equipmentUseArrayList, String Mode, String Permitstatus) {
        this.equipmentUseArrayList = equipmentUseArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        this.mode = Mode;
        this.permitStatus = Permitstatus;

    }

    @Override
    public int getCount() {
        return equipmentUseArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return equipmentUseArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
     final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.workauthorization_item_lay, null);
            holder = new ViewHolder();
            holder.checkbox_user = (AppCompatCheckBox) convertView.findViewById(R.id.checkbox_user);
            // holder.txt_title =  convertView.findViewById(R.id.txt_title);
            holder.txt_others = convertView.findViewById(R.id.txt_others);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.checkbox_user.setText(equipmentUseArrayList.get(position).getSelectionText());
        holder.checkbox_user.setChecked(equipmentUseArrayList.get(position).isSelected());
        if(!equipmentUseArrayList.get(position).isSelected()){
            holder.checkbox_user.setButtonDrawable(context.getResources().getDrawable(R.drawable.checkbox_style));
        }else{
            holder.checkbox_user.setButtonDrawable(context.getResources().getDrawable(R.drawable.checkbox_style));
        }

        if (equipmentUseArrayList.get(position).getSelectionText().equalsIgnoreCase("Other(s) –Specify")
                || equipmentUseArrayList.get(position).getSelectionText().equalsIgnoreCase("Other(s)")
                || equipmentUseArrayList.get(position).getSelectionText().equalsIgnoreCase("Other")) {
            if (equipmentUseArrayList.get(position).getRemarks() != null) {
                if (!equipmentUseArrayList.get(position).getRemarks().equals("")) {
                    equipmentUseArrayList.get(position).setSelected(true);
                    holder.txt_others.setVisibility(View.VISIBLE);
                    holder.txt_others.setText("Remark :" + equipmentUseArrayList.get(position).getRemarks());
                } else {
                    holder.txt_others.setVisibility(View.GONE);
                }
            }
        }

        if (permitStatus != null && (permitStatus.equalsIgnoreCase("A")
                || permitStatus.equalsIgnoreCase("C")
                || permitStatus.equalsIgnoreCase("R"))) {
            holder.checkbox_user.setClickable(false);
        } else {


            holder.checkbox_user.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    final boolean isSelected = ((AppCompatCheckBox) v).isChecked();
                    final String text = equipmentUseArrayList.get(position).getSelectionText();


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
                        if (equipmentUseArrayList != null) {
                            if (equipmentUseArrayList.get(position).getRemarks() != null) {
                                equipmentUseArrayList.get(position).setSelected(true);
                                String remarks = equipmentUseArrayList.get(position).getRemarks();
                                edit_remark.setText(remarks);
                            }
                        }
                        b.show();
                        txt_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String remarks = edit_remark.getText().toString();
                                if (remarks.equals("")) {
                                    equipmentUseArrayList.get(position).setSelected(false);
                                    equipmentUseArrayList.get(position).setRemarks(remarks);
                                    holder.checkbox_user.setChecked(false);
                                    holder.txt_others.setVisibility(View.GONE);

                                } else {
                                    equipmentUseArrayList.get(position).setSelected(true);
                                    equipmentUseArrayList.get(position).setRemarks(remarks);
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
                               equipmentUseArrayList.get(position).setRemarks(""); */

                                if (equipmentUseArrayList.get(position).getRemarks() == null ||
                                        equipmentUseArrayList.get(position).getRemarks().equalsIgnoreCase("")) {
                                    if (isAns) {
                                        // holder.checkbox_user.setChecked(false);
                                        if (!equipmentUseArrayList.get(position).getRemarks().equals("")) {
                                            equipmentUseArrayList.get(position).setSelected(true);
                                            equipmentUseArrayList.get(position).setRemarks(equipmentUseArrayList.get(position).getRemarks());
                                            holder.txt_others.setVisibility(View.VISIBLE);
                                            holder.txt_others.setText(equipmentUseArrayList.get(position).getRemarks());
                                            holder.checkbox_user.setChecked(true);

                                        } else {
                                            equipmentUseArrayList.get(position).setSelected(false);
                                            equipmentUseArrayList.get(position).setRemarks("");
                                            holder.checkbox_user.setChecked(false);
                                        }

                                    } else {

                                        equipmentUseArrayList.get(position).setSelected(false);
                                        equipmentUseArrayList.get(position).setRemarks("");
                                        holder.txt_others.setVisibility(View.GONE);
                                        holder.checkbox_user.setChecked(false);
                                    }

                                } else {
                                    // holder.checkbox_user.setChecked(true);
                                    if (equipmentUseArrayList.get(position).isSelected()) {
                                        if (equipmentUseArrayList.get(position).getRemarks().equals("")) {
                                            equipmentUseArrayList.get(position).setSelected(false);
                                            holder.checkbox_user.setChecked(false);
                                            equipmentUseArrayList.get(position).setRemarks("");
                                            holder.txt_others.setVisibility(View.GONE);
                                        } else {
                                            equipmentUseArrayList.get(position).setSelected(true);
                                            holder.checkbox_user.setChecked(true);
                                            holder.txt_others.setVisibility(View.VISIBLE);
                                            holder.txt_others.setText("Remark : " + equipmentUseArrayList.get(position).getRemarks());
                                            equipmentUseArrayList.get(position).setRemarks(equipmentUseArrayList.get(position).getRemarks());
                                        }
                                    } else {
                                        equipmentUseArrayList.get(position).setSelected(false);

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
                    else if (text.equalsIgnoreCase("Other(s)") || text.equalsIgnoreCase("Other")) {
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
                        if (equipmentUseArrayList != null) {
                            if (equipmentUseArrayList.get(position).getRemarks() != null) {
                                equipmentUseArrayList.get(position).setSelected(true);
                                String remarks = equipmentUseArrayList.get(position).getRemarks();
                                edit_remark.setText(remarks);
                            }
                        }
                        b.show();
                        txt_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String remarks = edit_remark.getText().toString();
                                if (remarks.equals("")) {
                                    equipmentUseArrayList.get(position).setSelected(false);
                                    equipmentUseArrayList.get(position).setRemarks(remarks);
                                    holder.checkbox_user.setChecked(false);
                                    holder.txt_others.setVisibility(View.GONE);

                                } else {
                                    equipmentUseArrayList.get(position).setSelected(true);
                                    holder.checkbox_user.setChecked(true);
                                    equipmentUseArrayList.get(position).setRemarks(remarks);
                                    holder.txt_others.setVisibility(View.VISIBLE);
                                    holder.txt_others.setText("Remark : " + remarks);

                                }

                                b.dismiss();
                            }
                        });
                        txt_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (equipmentUseArrayList.get(position).getRemarks() == null ||
                                        equipmentUseArrayList.get(position).getRemarks().equalsIgnoreCase("")) {
                                    if (isAns) {
                                        // holder.checkbox_user.setChecked(false);
                                        if (!equipmentUseArrayList.get(position).getRemarks().equals("")) {
                                            equipmentUseArrayList.get(position).setSelected(true);
                                            equipmentUseArrayList.get(position).setRemarks(equipmentUseArrayList.get(position).getRemarks());
                                            holder.txt_others.setVisibility(View.VISIBLE);
                                            holder.txt_others.setText(equipmentUseArrayList.get(position).getRemarks());
                                            holder.checkbox_user.setChecked(true);

                                        } else {
                                            equipmentUseArrayList.get(position).setSelected(false);
                                            equipmentUseArrayList.get(position).setRemarks("");
                                            holder.checkbox_user.setChecked(false);
                                        }

                                    } else {
                                        equipmentUseArrayList.get(position).setSelected(false);
                                        equipmentUseArrayList.get(position).setRemarks("");
                                        holder.txt_others.setVisibility(View.GONE);
                                        holder.checkbox_user.setChecked(false);

                                    }

                                } else {
                                    // holder.checkbox_user.setChecked(true);
                                    if (equipmentUseArrayList.get(position).isSelected()) {
                                        if (equipmentUseArrayList.get(position).getRemarks().equals("")) {
                                            equipmentUseArrayList.get(position).setSelected(false);
                                            holder.checkbox_user.setChecked(false);
                                            equipmentUseArrayList.get(position).setRemarks("");
                                            holder.txt_others.setVisibility(View.GONE);
                                        } else {
                                            equipmentUseArrayList.get(position).setSelected(true);
                                            holder.checkbox_user.setChecked(true);
                                            holder.txt_others.setVisibility(View.VISIBLE);
                                            holder.txt_others.setText("Remark : " + equipmentUseArrayList.get(position).getRemarks());
                                            equipmentUseArrayList.get(position).setRemarks(equipmentUseArrayList.get(position).getRemarks());
                                        }
                                    } else {
                                        equipmentUseArrayList.get(position).setSelected(false);
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
                    else {
                        holder.checkbox_user.setChecked(isSelected);
                        equipmentUseArrayList.get(position).setSelected(isSelected);

                    }
                }

            });


        }


        return convertView;
    }

    public ArrayList<EquipmentUse> getArrayList() {
        ArrayList<EquipmentUse> list = new ArrayList<>();
        for (int i = 0; i < equipmentUseArrayList.size(); i++) {
            if (equipmentUseArrayList.get(i).isSelected())
                list.add(equipmentUseArrayList.get(i));
        }
        return list;
    }


    static class ViewHolder {
        TextView txt_user_name_data;
        AppCompatCheckBox checkbox_user;
        TextView txt_others, txt_title;
    }


}

