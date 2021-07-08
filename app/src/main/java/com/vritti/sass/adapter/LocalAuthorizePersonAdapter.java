package com.vritti.sass.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vritti.sass.R;
import com.vritti.sass.model.AuthorizePersonLocalBean;

import java.util.ArrayList;

public class LocalAuthorizePersonAdapter extends RecyclerView.Adapter<LocalAuthorizePersonAdapter.AuthorizeHolder> {

    Context context;
    ArrayList<AuthorizePersonLocalBean> authorizePersonLocalBeanArrayList;
    String accessRight;

    public LocalAuthorizePersonAdapter(Context context, ArrayList<AuthorizePersonLocalBean> authorizePersonLocalBeanArrayList, String accessRight) {
        this.authorizePersonLocalBeanArrayList = authorizePersonLocalBeanArrayList;
        this.context = context;
        this.accessRight = accessRight;

    }

    @NonNull
    @Override
    public AuthorizeHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.authorize_person_row, parent, false);


        return new AuthorizeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorizeHolder holder, int i) {

        try {

            holder.mobileAuth.setText(authorizePersonLocalBeanArrayList.get(i).getMobileNo());
            holder.nameAuth.setText(authorizePersonLocalBeanArrayList.get(i).getName());
            holder.emailAuth.setText(authorizePersonLocalBeanArrayList.get(i).getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (accessRight.equals("false")) {
            holder.imgdelete.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return authorizePersonLocalBeanArrayList.size();
    }

    public class AuthorizeHolder extends RecyclerView.ViewHolder {

        TextView mobileAuth, emailAuth, nameAuth;
        ImageView imgdelete;

        public AuthorizeHolder(@NonNull View itemView) {
            super(itemView);
            mobileAuth = itemView.findViewById(R.id.mobileAuth);
            emailAuth = itemView.findViewById(R.id.emailAuth);
            nameAuth = itemView.findViewById(R.id.nameAuth);
            imgdelete = itemView.findViewById(R.id.imgdelete);
            imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    authorizePersonLocalBeanArrayList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }
}
