package com.vritti.sass.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.vritti.sass.R;
import com.vritti.sass.WorkAuthorizationActivity;
import com.vritti.sass.model.GoldenRules;
import com.vritti.sass.model.IndicateRisk;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class GoldenRuleAdapter extends RecyclerView.Adapter<GoldenRuleAdapter.ActivityHolder> {
    Context context;
    ArrayList<GoldenRules> goldenRulesArrayList;
    String mode="";
    String permitStatus="",permitName="";


    public GoldenRuleAdapter(Context context, ArrayList<GoldenRules> goldenRulesArrayList, String permitName,String mode, String permitStatus) {
        this.context = context;
        this.goldenRulesArrayList = goldenRulesArrayList;
        this.mode = mode;
        this.permitStatus= permitStatus;
        this.permitName = permitName;

    }

    public GoldenRuleAdapter(Context context, ArrayList<GoldenRules> goldenRulesArrayList, String mode, String permitStatus) {
        this.context = context;
        this.goldenRulesArrayList = goldenRulesArrayList;
        this.mode = mode;
        this.permitStatus = permitStatus;

    }

    public GoldenRuleAdapter() {


    }

    @NonNull
    @Override
    public GoldenRuleAdapter.ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goldenrules_list, parent, false);


        return new ActivityHolder(itemView);

    }


    @Override
    public void onBindViewHolder(@NonNull GoldenRuleAdapter.ActivityHolder holder, int position) {

        // holder.txt_name.setText(safetyMeasureArrayList.get(position).getProjectName());
        holder.checkbox_user.setText(goldenRulesArrayList.get(position).getGoldenRulesDesc());
        holder.checkbox_user.setChecked(goldenRulesArrayList.get(position).isSelected());
        if(mode.equals("E")) {
            if (!permitStatus.equals("P")) {
                if (permitName.equals("WA")) {
                    holder.checkbox_user.setClickable(true);
                } else {
                    holder.checkbox_user.setClickable(false);
                }
            }
        }else{
            holder.checkbox_user.setClickable(true);
        }

    }


    @Override
    public int getItemCount() {
        return goldenRulesArrayList.size();
    }

    public void updateList(ArrayList<GoldenRules> goldenRulesArrayList,String mode,String permitStatus) {
        this.goldenRulesArrayList = goldenRulesArrayList;
        this.mode = mode;
        this.permitStatus = permitStatus;
    }


    public class ActivityHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checkbox_user1)
        AppCompatCheckBox checkbox_user;

        public ActivityHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnCheckedChanged(R.id.checkbox_user1)
        void check(AppCompatCheckBox checkBox, boolean checked){

            boolean isSelected = checkBox.isChecked();
            final String text = goldenRulesArrayList.get(getAdapterPosition()).getGoldenRulesDesc();
            goldenRulesArrayList.get(getAdapterPosition()).setSelected(isSelected);

        }
    }

    public ArrayList<GoldenRules> getArrayList() {
        ArrayList<GoldenRules> list = new ArrayList<>();
        for (int i = 0; i < goldenRulesArrayList.size(); i++) {
            if (goldenRulesArrayList.get(i).isSelected())
                list.add(goldenRulesArrayList.get(i));
        }
        return list;
    }

}
