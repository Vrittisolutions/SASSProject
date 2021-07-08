package com.vritti.sass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.adapter.ContractorAdapter;
import com.vritti.sass.adapter.LOTOPermitAdapter;
import com.vritti.sass.adapter.PermitAdapter;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.Contractor;
import com.vritti.sass.model.Permit;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.WebAPIUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sharvari on 04-Dec-18.
 */

public class LOTOPermitActivity extends AppCompatActivity {


    ListView list_lotopermit;
    LOTOPermitAdapter lotoPermitAdapter;
    ArrayList<Permit> permitArrayList;
    SharedPreferences userpreferences;
    String CompanyURL;
    ProgressBar mprogress;
    Button btn_attachment1,btn_attachment2;
    TextView txt_loto_name1,txt_loto_name2;
    RelativeLayout rl1,rl2;
    String Attachment1="",Attachment2="";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lotopermit_list_lay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        setSupportActionBar(toolbar);

        userpreferences = getSharedPreferences(LoginActivity.USERINFO, Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", "");

        mprogress=findViewById(R.id.toolbar_progress_App_bar);
        permitArrayList=new ArrayList<Permit>();
        list_lotopermit=(ListView) findViewById(R.id.list_lotopermit);
        btn_attachment1 = findViewById(R.id.txt_attachment1);
        btn_attachment2 = findViewById(R.id.txt_attachment2);
        txt_loto_name1 = findViewById(R.id.txt_loto_name);
        txt_loto_name2 = findViewById(R.id.txt_loto_name1);
        rl1 = findViewById(R.id.rl1);
        rl2 = findViewById(R.id.rl2);

        Intent intent = getIntent();
         Attachment1 = intent.getStringExtra("Attachment");
         Attachment2 =intent.getStringExtra("Attachment1");

         if(!Attachment2.equalsIgnoreCase("") && Attachment1.equalsIgnoreCase("")){
             rl1.setVisibility(View.GONE);
            // txt_loto_name1.setText(Attachment1);
             rl2.setVisibility(View.VISIBLE);
             txt_loto_name2.setText(Attachment2);
         }else if(!Attachment1.equalsIgnoreCase("") && Attachment2.equalsIgnoreCase("")){
             rl1.setVisibility(View.VISIBLE);
             txt_loto_name1.setText(Attachment1);
             rl2.setVisibility(View.GONE);
             txt_loto_name2.setText(Attachment2);
         }else {
             rl1.setVisibility(View.VISIBLE);
             txt_loto_name1.setText(Attachment1);
             rl2.setVisibility(View.VISIBLE);
             txt_loto_name2.setText(Attachment2);
         }


        btn_attachment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LOTOPermitActivity.this, ImageFullScreenActivity.class);
                intent.putExtra("share_image_path",Attachment1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        btn_attachment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LOTOPermitActivity.this, ImageFullScreenActivity.class);
                intent.putExtra("share_image_path",Attachment2);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });





       /* permitArrayList.add(new Permit("Isolated by Valves"));
        permitArrayList.add(new Permit("Electrical closing for works"));
       */



    }
    public class DownloadPermitData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {

           // String url = CompanyURL +"/api/SASSAPI/lotoPermit?Date=12/10/2019+6:12:57+PM";
            String url = CompanyURL + WebAPIUrl.api_LotoPermit_Attachment;

            try {
                res = CommonClass.OpenConnection(url,LOTOPermitActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);


                    permitArrayList.clear();


                    //Collections.sort(contractorArrayList);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {
                dismissProgress();
                Toast.makeText(LOTOPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(response);


                for (int i = 0; i < jResults.length(); i++) {
                    Permit permit = new Permit();
                    JSONObject jorder = jResults.getJSONObject(i);

                    permit.setAddedDt1(jorder.getString("AddedDt"));
                    permit.setPermitNo(jorder.getString("permitno"));
                    permit.setIsolatedImageName(jorder.getString("IsolatedImageName"));
                    permit.setElectricalImageName(jorder.getString("ElectricalImageName"));
                    permit.setAttachment(jorder.getString("Attachment"));
                    permit.setAttachment1(jorder.getString("Attachment1"));
                    permitArrayList.add(permit);
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lotoPermitAdapter = new LOTOPermitAdapter(LOTOPermitActivity.this,permitArrayList);
                list_lotopermit.setAdapter(lotoPermitAdapter);



            }


        }
    }

    private void showProgress() {

        mprogress.setVisibility(View.VISIBLE);

    }

    private void dismissProgress() {

        mprogress.setVisibility(View.GONE);


    }

}
