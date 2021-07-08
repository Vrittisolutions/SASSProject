package com.vritti.sass.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vritti.sass.HOTWorkActivity;
import com.vritti.sass.ImageFullScreenActivity;
import com.vritti.sass.R;
import com.vritti.sass.model.SafetyMeasure;

import java.util.ArrayList;


/**
 * Created by pradnya on 10/13/16.
 */
public class SafetyMeasureListAdapter extends BaseAdapter {
    ArrayList<SafetyMeasure> safetyMeasureArrayList;
    ArrayList<String> list;
    LayoutInflater mInflater;
    Context context;
    String checkradioId = "";
    RadioButton checked_rb = null;
    int MY_CAMERA_PERMISSION_CODE = 100;
    int MEDIA_TYPE_IMAGE = 1;
    int CAMERA_REQUEST = 101;
    private Uri fileUri;
    String mode, permitStatus = "", path = "";


    public SafetyMeasureListAdapter(Context context1, ArrayList<SafetyMeasure> safetyMeasureArrayList, String Mode, String permitStatus) {
        this.safetyMeasureArrayList = safetyMeasureArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        this.mode = Mode;
        this.permitStatus = permitStatus;

    }

    @Override
    public int getCount() {
        return safetyMeasureArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return safetyMeasureArrayList.get(position);
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
            holder.radio_done = (RadioButton) convertView.findViewById(R.id.radio_done);
            holder.radio_yes = (RadioButton) convertView.findViewById(R.id.radio_yes1);
            holder.radio_no = (RadioButton) convertView.findViewById(R.id.radio_no1);
            holder.len_camera = convertView.findViewById(R.id.len_camera);
            holder.img_display = convertView.findViewById(R.id.img_display);
            holder.radio_selection = (RadioGroup) convertView.findViewById(R.id.radiogroup_selection);
            holder.txt_others = convertView.findViewById(R.id.txt_others);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_name.setText(safetyMeasureArrayList.get(position).getProjectName());
        holder.radio_yes.setSelected(safetyMeasureArrayList.get(position).isSelected());
        if (safetyMeasureArrayList.get(position).isSelected()) {
            holder.radio_yes.setChecked(true);
            holder.radio_no.setChecked(false);
            holder.radio_done.setChecked(true);
        } else {
            holder.radio_no.setChecked(true);
            holder.radio_done.setChecked(false);
            holder.radio_yes.setChecked(false);
        }

        if (safetyMeasureArrayList.get(position).getProjectCode().equals("ACMB")) {
            holder.len_camera.setVisibility(View.VISIBLE);
            if (safetyMeasureArrayList.get(position).getImgAbsolutePath() != null) {
                Bitmap thumbNail = BitmapFactory.decodeFile(safetyMeasureArrayList.get(position).getImgAbsolutePath());
                //  holder.img_camera.setVisibility(View.GONE);
                Bitmap thumbnail = BitmapFactory.decodeFile(safetyMeasureArrayList.get(position).getImgAbsolutePath());
                path = safetyMeasureArrayList.get(position).getImgAbsolutePath();
                holder.img_display.setVisibility(View.VISIBLE);
                holder.img_display.setImageBitmap(thumbnail);
            }
        }


        if (safetyMeasureArrayList.get(position).getProjectCode().equals("ACMB") ||
                safetyMeasureArrayList.get(position).getProjectCode().equals("ORS") ||
                safetyMeasureArrayList.get(position).getProjectCode().equals("EIP")) {
            if (safetyMeasureArrayList.get(position).getRemarks() != null) {
                if (safetyMeasureArrayList.get(position).getRemarks().equals("")) {
                    holder.txt_others.setVisibility(View.GONE);
                } else {
                    holder.txt_others.setVisibility(View.VISIBLE);
                    holder.txt_others.setText("Remark :" + safetyMeasureArrayList.get(position).getRemarks());
                }
            }
        }
        //holder.radio_no.setSelected(safetyMeasureArrayList.get(position).isSelected());

        holder.img_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String projectCode = safetyMeasureArrayList.get(position).getProjectCode();
                if (projectCode.equals("ACMB")) {
                    context.startActivity(new Intent(context, ImageFullScreenActivity.class).putExtra("share_image_path",
                            safetyMeasureArrayList.get(position).getImgPath()));

                }
            }
        });


        if (mode.equalsIgnoreCase("E") && !permitStatus.equalsIgnoreCase("P")) {
            holder.radio_yes.setClickable(false);
            holder.radio_no.setClickable(false);
        } else {
            if (permitStatus.equals("R") || permitStatus.equals("C")) {
                holder.radio_yes.setClickable(false);
                holder.radio_no.setClickable(false);
            } else {
                holder.radio_selection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        checked_rb = (AppCompatRadioButton) group.findViewById(checkedId);

                        String ProjectCode = safetyMeasureArrayList.get(position).getProjectCode();


                        checkradioId = safetyMeasureArrayList.get(position).getProjectId();

                        if (checked_rb == holder.radio_no) {
                            holder.radio_done.setVisibility(View.GONE);
                            holder.radio_no.setChecked(true);
                            //
                            // safetyMeasureArrayList.get(position).setSelected(isSelected);
                            boolean isSelected = holder.radio_no.isChecked();
                        } else {
                            holder.radio_done.setVisibility(View.VISIBLE);
                            holder.radio_done.setChecked(true);
                            holder.radio_yes.setChecked(true);
                            boolean isSelected = holder.radio_yes.isChecked();
                            safetyMeasureArrayList.get(position).setSelected(isSelected);

                            if (safetyMeasureArrayList.get(position).getProjectCode().equals("ACMB") ||
                                    safetyMeasureArrayList.get(position).getProjectCode().equals("ORS") ||
                                    safetyMeasureArrayList.get(position).getProjectCode().equals("EIP")) {

                                ((HOTWorkActivity) context).reasonDialog(position, safetyMeasureArrayList.get(position).getProjectCode());

                            }
                        }

                    }
                });
            }
        }

        holder.len_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (safetyMeasureArrayList.get(position).getProjectCode().equals("ACMB")) {
                    ((HOTWorkActivity) context).captureimg(position, safetyMeasureArrayList.get(position).getProjectCode());
                }
                //  ((HOTWorkActivity) context).reasonDialog(position, safetyMeasureArrayList.get(position).getProjectCode());
            }
        });



      /*  holder.len_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_PERMISSION_CODE);
                    }
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = Uri.fromFile(((HOTWorkActivity)context).getOutputMediaFile(MEDIA_TYPE_IMAGE));
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });*/


        return convertView;
    }

    public ArrayList<SafetyMeasure> getArrayList() {
        ArrayList<SafetyMeasure> list = new ArrayList<SafetyMeasure>();
        for (int i = 0; i < safetyMeasureArrayList.size(); i++) {
            if (safetyMeasureArrayList.get(i).isSelected()) {
                list.add(safetyMeasureArrayList.get(i));
            }
        }


        return list;
    }

    public void updateList(ArrayList<SafetyMeasure> safetyMeasureArrayList) {
        this.safetyMeasureArrayList = safetyMeasureArrayList;
        notifyDataSetChanged();


    }

/*

    public String getcheckedradio(){

        String OperationCode = checkradioId;

        return OperationCode;
    }
*/


    static class ViewHolder {
        TextView txt_name;
        RadioButton radio_done, radio_yes, radio_no;
        LinearLayout len_camera;
        SimpleDraweeView img_display;
        RadioGroup radio_selection;
        TextView txt_others;

    }


}
