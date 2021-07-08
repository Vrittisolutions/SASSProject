package com.vritti.sass.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.vritti.sass.ImageFullScreenActivity;
import com.vritti.sass.R;
import com.vritti.sass.WorkAuthorizationActivity;
import com.vritti.sass.model.EquipmentUse;
import com.vritti.sass.model.InstallationPreparation;

import java.util.ArrayList;


/**
 * Created by pradnya on 10/13/16.
 */


public class InstallationListAdapter extends BaseAdapter {

    ArrayList<InstallationPreparation> installationPreparationArrayList;
    LayoutInflater mInflater;
    Context context;
    AppCompatRadioButton lastCheckedRB = null;
    String mode = "", permitStatus = "", path = "";
    String editimgDisply = "";
    ;
    // AppCompatRadioButton lastCheckedRB = null;

    public InstallationListAdapter(Context context1, ArrayList<InstallationPreparation> installationPreparationArrayList) {
        this.installationPreparationArrayList = installationPreparationArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    public InstallationListAdapter(Context context1, ArrayList<InstallationPreparation> indicateRiskArrayList, String Mode, String PermitStatus) {
        this.installationPreparationArrayList = indicateRiskArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        this.mode = Mode;
        this.permitStatus = PermitStatus;

    }

    @Override
    public int getCount() {
        return installationPreparationArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return installationPreparationArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.installation_pre_item_lay, null);
            holder = new ViewHolder();
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.radio_done = (AppCompatRadioButton) convertView.findViewById(R.id.radio_done);
            holder.radio_yes = (AppCompatRadioButton) convertView.findViewById(R.id.radio_yes1);
            holder.radio_no = (AppCompatRadioButton) convertView.findViewById(R.id.radio_no1);
            holder.radiogroup_selection = (RadioGroup) convertView.findViewById(R.id.radiogroup_selection);
            holder.txtothers = convertView.findViewById(R.id.txt_others);
            holder.len_camera = convertView.findViewById(R.id.len_camera);
            holder.img_camera = (ImageView) convertView.findViewById(R.id.img_camera);
            holder.img_display = convertView.findViewById(R.id.img_display);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txt_name.setText(installationPreparationArrayList.get(position).getProjectName());
        if (installationPreparationArrayList.get(position).isSelected()) {
            holder.radio_yes.setChecked(true);
            holder.radio_no.setChecked(false);
            holder.radio_done.setVisibility(View.VISIBLE);
            holder.radio_done.setChecked(true);
        } else {
            holder.radio_no.setChecked(true);
            holder.radio_yes.setChecked(false);
            holder.radio_done.setVisibility(View.GONE);
        }
        if (installationPreparationArrayList.get(position).getProjectCode().equals("ORS")) {
            if (installationPreparationArrayList.get(position).getRemarks() != null) {
                if (!installationPreparationArrayList.get(position).getRemarks().equals("")) {
                    holder.radio_yes.setChecked(true);
                    holder.radio_no.setChecked(false);
                    holder.radio_done.setVisibility(View.VISIBLE);
                    holder.radio_done.setChecked(true);
                    holder.txtothers.setVisibility(View.VISIBLE);
                    holder.txtothers.setText(installationPreparationArrayList.get(position).getRemarks());

                }
            }
        }
        //     if (ProjectCode.equals("IBV")) {
        if (editimgDisply.equals("DisplayEditImage")) {
            if (installationPreparationArrayList.get(position).getProjectCode().equals("IBV")) {
                holder.img_display.setVisibility(View.VISIBLE);
                holder.len_camera.setVisibility(View.VISIBLE);
                path = installationPreparationArrayList.get(position).getiImg();
                String  image="http://z207.ekatm.co.in/Attachments/View%20Attachment/"+path;
                holder.img_display.setImageURI(image);
            } else if (installationPreparationArrayList.get(position).getProjectCode().equals("ECW")) {
                holder.img_display.setVisibility(View.VISIBLE);
                holder.len_camera.setVisibility(View.VISIBLE);
                path = installationPreparationArrayList.get(position).geteImg();
                String  image="http://z207.ekatm.co.in/Attachments/View%20Attachment/"+path;
                holder.img_display.setImageURI(image);
            }

        } else {
            if (installationPreparationArrayList.get(position).getProjectCode().equals("IBV")) {
                if (installationPreparationArrayList.get(position).getElectricalImageName() != null) {
                    holder.len_camera.setVisibility(View.VISIBLE);
                    Bitmap thumbNail = BitmapFactory.decodeFile(installationPreparationArrayList.get(position).getElectricalImageName());
                    //  holder.img_camera.setVisibility(View.GONE);
                    Bitmap thumbnail = BitmapFactory.decodeFile(installationPreparationArrayList.get(position).getElectricalImageName());
                    path = installationPreparationArrayList.get(position).getElectricalImageName();
                    holder.img_display.setVisibility(View.VISIBLE);
                    holder.img_display.setImageBitmap(thumbnail);
                }


            }
            else if (installationPreparationArrayList.get(position).getProjectCode().equals("ECW")) {
                if (installationPreparationArrayList.get(position).getIsolatedImageName() != null) {
                    holder.len_camera.setVisibility(View.VISIBLE);
                    Bitmap thumbNail = BitmapFactory.decodeFile(installationPreparationArrayList.get(position).getIsolatedImageName());
                    //  holder.img_camera.setVisibility(View.GONE);
                    Bitmap thumbnail = BitmapFactory.decodeFile(installationPreparationArrayList.get(position).getIsolatedImageName());
                    path = installationPreparationArrayList.get(position).getIsolatedImageName();
                    holder.img_display.setVisibility(View.VISIBLE);
                    holder.img_display.setImageBitmap(thumbnail);
                }
            }
        }

        // holder.radio_yes.isChecked(tr);


        holder.img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ProjectCode = installationPreparationArrayList.get(position).getProjectCode();

                if (ProjectCode.equals("IBV")) {

                    ((WorkAuthorizationActivity) context).captureimg(ProjectCode, position);

                } else if (ProjectCode.equals("ECW")) {
                    ((WorkAuthorizationActivity) context).captureimg(ProjectCode, position);

                } else {

                }
            }
        });

        holder.img_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String projectCode = installationPreparationArrayList.get(position).getProjectCode();
                if (projectCode.equals("IBV")) {
                    context.startActivity(new Intent(context, ImageFullScreenActivity.class).putExtra("share_image_path",
                            installationPreparationArrayList.get(position).getiImg()));

                } else if (projectCode.equals("ECW")) {
                    context.startActivity(new Intent(context, ImageFullScreenActivity.class).putExtra("share_image_path",
                            installationPreparationArrayList.get(position).geteImg()));
                }
            }
        });

        if (permitStatus.equals("R") || permitStatus.equals("C")) {
            holder.radiogroup_selection.setEnabled(false);
            // holder.radio_yes.setEnabled(false);
            // holder.radio_no.setEnabled(false);
            holder.radio_done.setClickable(false);
            holder.radio_yes.setClickable(false);
            holder.radio_no.setClickable(false);

        } else {
            holder.radiogroup_selection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton checked_rb = (AppCompatRadioButton) group.findViewById(checkedId);

                    String ProjectCode = installationPreparationArrayList.get(position).getProjectCode();
                    String ProjectID = installationPreparationArrayList.get(position).getProjectId();

                    if (checked_rb == holder.radio_no) {
                        if (ProjectCode.equals("IBV")) {
                            holder.len_camera.setVisibility(View.GONE);
                            holder.radio_done.setVisibility(View.GONE);

                        } else if (ProjectCode.equals("ECW")) {
                            holder.len_camera.setVisibility(View.GONE);
                            holder.radio_done.setVisibility(View.GONE);

                        } else {
                            holder.radio_done.setVisibility(View.GONE);
                            holder.txtothers.setVisibility(View.GONE);
                            installationPreparationArrayList.get(position).setRemarks("");
                        }
                    } else {
                        if (ProjectCode.equals("IBV")) {
                            boolean isSelected = checked_rb.isChecked();

                            installationPreparationArrayList.get(position).setSelected(isSelected);
                            holder.len_camera.setVisibility(View.VISIBLE);
                            holder.radio_done.setVisibility(View.VISIBLE);
                            holder.radio_done.setChecked(true);
                            holder.radio_yes.setChecked(true);

                        } else if (ProjectCode.equals("ECW")) {
                            boolean isSelected = checked_rb.isChecked();
                            installationPreparationArrayList.get(position).setSelected(isSelected);
                            holder.len_camera.setVisibility(View.VISIBLE);
                            holder.radio_done.setVisibility(View.VISIBLE);
                            holder.radio_done.setChecked(true);
                            holder.radio_yes.setChecked(true);

                        } else if (ProjectCode.equals("ORS")) {
                            boolean isSelected = checked_rb.isChecked();
                            installationPreparationArrayList.get(position).setSelected(isSelected);
                            holder.radio_done.setVisibility(View.VISIBLE);
                            holder.radio_done.setChecked(true);
                            holder.radio_yes.setChecked(true);
                            ((WorkAuthorizationActivity) context).reasonDialog(position, installationPreparationArrayList.get(position).getProjectCode());
                        } else {
                            boolean isSelected = checked_rb.isChecked();
                            installationPreparationArrayList.get(position).setSelected(isSelected);
                            holder.radio_done.setVisibility(View.VISIBLE);
                            holder.radio_done.setChecked(true);
                            holder.radio_yes.setChecked(true);
                        }


                    }

                }
            });
        }


        return convertView;
    }

    public ArrayList<InstallationPreparation> getArrayList() {
        ArrayList<InstallationPreparation> list = new ArrayList<>();
        for (int i = 0; i < installationPreparationArrayList.size(); i++) {
            if (installationPreparationArrayList.get(i).isSelected()) {
                list.add(installationPreparationArrayList.get(i));
            } else {

            }

        }
        return list;


    }

    public void updateList(ArrayList<InstallationPreparation> installationPreparationArrayList) {
        this.installationPreparationArrayList = installationPreparationArrayList;
        notifyDataSetChanged();
    }

    public void updateEditList(ArrayList<InstallationPreparation> installationPreparationArrayList, String displayEditImage) {
        this.installationPreparationArrayList = installationPreparationArrayList;
        this.editimgDisply = displayEditImage;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        TextView txt_name;
        RadioButton radio_done, radio_yes, radio_no;
        LinearLayout len_camera;
        TextView txtothers;
        RadioGroup radiogroup_selection;
        ImageView img_camera;
        SimpleDraweeView img_display;

    }
}



