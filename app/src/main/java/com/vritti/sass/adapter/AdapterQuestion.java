package com.vritti.sass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.internal.zzdnq;
import com.vritti.sass.R;
import com.vritti.sass.model.Question;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin-1 on 9/22/2016.
 */

public class AdapterQuestion extends RecyclerView.Adapter<AdapterQuestion.ViewHolder> {
    public static ArrayList<Question> mList;
    Context context;


    public AdapterQuestion(Context context,ArrayList<Question> list) {
        this.mList = list;
        this.context=context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quesition_lay, parent, false);
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_putaway_new, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        holder.txt_question.setText(mList.get(i).getQuestion());

        holder.edt_ans.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    if(s.toString() == "" || s.toString() == null){

                    }  else {
                        String ans = holder.edt_ans.getText().toString().trim();
                        mList.get(i).setqId(mList.get(i).getShifthandoverQuestionMasterId());
                        mList.get(i).setQuestion_ans(ans);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<Question> dummyList) {
        mList = dummyList;
        notifyDataSetChanged();
    }

    public ArrayList<Question> getListData() {
        ArrayList<Question> finallist = new ArrayList<>();
        finallist.clear();
        finallist.addAll(mList);
        return finallist;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_question;
        private LinearLayout mLinear;
        EditText edt_ans;

        public ViewHolder(final View view) {
            super(view);

            //  tv_Header = (TextView) view.findViewById(R.id.textView_grn_header);
            txt_question = (TextView) view.findViewById(R.id.txt_question);
            edt_ans = (EditText) view.findViewById(R.id.edt_ans);
            context = itemView.getContext();


        }





    }




}