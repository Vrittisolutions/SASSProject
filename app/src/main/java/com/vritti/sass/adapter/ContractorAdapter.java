package com.vritti.sass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.sass.ContractorAddActivity;
import com.vritti.sass.ContractorListActivity;
import com.vritti.sass.R;
import com.vritti.sass.model.Contractor;
import com.vritti.sass.model.Permit;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by pradnya on 10/13/16.
 */
public class ContractorAdapter extends RecyclerView.Adapter<ContractorAdapter.ViewHolder> {

    ArrayList<Contractor> contractorslist;
    Context context;


    public ContractorAdapter(ContractorListActivity context, ArrayList<Contractor> contractorArrayList) {
        this.context= context;
        this.contractorslist = contractorArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contractor_item_lay, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Contractor contractor = contractorslist.get(position);

        // Set item views based on your views and data model
        holder.txtcontractorname.setText(contractor.getContractorName());



    }

    @Override
    public int getItemCount() {
        return contractorslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_contractor_name)
        TextView txtcontractorname;
        CardView card_view;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            card_view=itemView.findViewById(R.id.card_view);
            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((ContractorListActivity)context).rowClick(getAdapterPosition());



                }
            });

            // txtcontractorname = (TextView) itemView.findViewById(R.id.txt_contractor_name);
        }

//        @OnClick(R.id.txt_contractor_name)
//        void setonclicklist(){
//            ((ContractorListActivity)context).editName(getAdapterPosition());
//           //String name = String.valueOf(contractorslist.get(getAdapterPosition()));
//            //notifyItemRemoved(getAdapterPosition());
//        }


    }
}




    /*@Override
    public int getCount() {
        return contractorArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return contractorArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.contractor_item_lay, null);
            holder = new ViewHolder();
            holder.txt_contractor_name= (TextView) convertView.findViewById(R.id.txt_contractor_name);
            holder.txt_contractor_mobile= (TextView) convertView.findViewById(R.id.txt_contractor_mobile);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_contractor_name.setText(contractorArrayList.get(position).getContractorName());
      //  holder.txt_contractor_mobile.setText("Mobile No : "+ contractorArrayList.get(position).getContractorMobile());


         return convertView;
    }

    static class ViewHolder {
        TextView txt_contractor_name,txt_contractor_mobile;

    }*/



