package com.vritti.sass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.adapter.LOTOPermitAdapter;
import com.vritti.sass.adapter.LotoMainAdapter;
import com.vritti.sass.adapter.SpotCheckPermitAdapter;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.Permit;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.WebAPIUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SpotCheckImageActivity extends AppCompatActivity {

    ListView list_permit;
    Toolbar toolbar;
    String PermitNo="",PermitOpCode="";
    TextView txt_no_record;
    SharedPreferences userpreferences;
    private String CompanyURL,UserID;
    ArrayList<Permit> permitArrayList;
    SpotCheckPermitAdapter spotCheckPermitAdapter;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loto_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        setSupportActionBar(toolbar);

        initView();

        userpreferences = getSharedPreferences(LoginActivity.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", "");
        UserID=userpreferences.getString("UserMasterId","");
        permitArrayList = new ArrayList<>();

        PermitNo=getIntent().getStringExtra("permitno");
        PermitOpCode=getIntent().getStringExtra("code");




        if (CommonClass.checkNet(SpotCheckImageActivity.this)) {
            progress.setVisibility(View.VISIBLE);
            new StartSession(SpotCheckImageActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadPermitData().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    CommonClass.displayToast(getApplicationContext(), msg);

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {


        list_permit = findViewById(R.id.list_permit);
        txt_no_record = findViewById(R.id.txt_blank);
        progress=findViewById(R.id.progress);

    }


    public class DownloadPermitData extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            // String url = CompanyURL +"/api/SASSAPI/lotoPermit?Date=12/10/2019+6:12:57+PM";
            String url = CompanyURL + WebAPIUrl.api_SpotImageDetails+"?PermitNo="+PermitNo+"&PermitOpCode="+PermitOpCode;

            try {
                res = CommonClass.OpenConnection(url, SpotCheckImageActivity.this);
                if (res != null) {
                    response = res.toString();
                    /*response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);*/

                    permitArrayList.clear();


                    //Collections.sort(contractorArrayList);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            progress.setVisibility(View.GONE);


                try {
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        Permit permit = new Permit();
                        JSONObject jorder = jResults.getJSONObject(i);

                        String spotImage =jorder.getString("SpotImage");

                        permit.setSpotImage(spotImage);

                        if (!spotImage.equals("")){
                            permitArrayList.add(permit);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                spotCheckPermitAdapter = new SpotCheckPermitAdapter(SpotCheckImageActivity.this,permitArrayList);

                if (spotCheckPermitAdapter.getCount()!=0){
                    list_permit.setAdapter(spotCheckPermitAdapter);
                }else {
                    txt_no_record.setVisibility(View.VISIBLE);

                }







        }
    }
}
