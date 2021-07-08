package com.vritti.sass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.adapter.LOTOPermitAdapter;
import com.vritti.sass.adapter.LotoMainAdapter;
import com.vritti.sass.adapter.PermitAdapter;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.DateFormatChange;
import com.vritti.sass.model.Permit;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.WebAPIUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class LotoMainActivity extends AppCompatActivity {

    ListView list_permit;
    Toolbar toolbar;
    String PermitNo = "";
    TextView txt_no_record;
    SharedPreferences userpreferences;
    private String CompanyURL, UserID;
    ArrayList<Permit> permitArrayList;
    LotoMainAdapter permitmainAdapter;
    LOTOPermitAdapter lotoPermitAdapter;
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
        UserID = userpreferences.getString("UserMasterId", "");
        permitArrayList = new ArrayList<>();
        setListner();

      /*if(CommonClass.checkNet(LotoMainActivity.this)){
          new StartSession(LotoMainActivity.this, new CallbackInterface() {

              @Override
              public void callMethod() {
                  new DownloadPermitStatusData().execute();
              }

              @Override
              public void callfailMethod(String msg) {

              }
          });
      }*/


        if (CommonClass.checkNet(LotoMainActivity.this)) {
            progress.setVisibility(View.VISIBLE);
            new StartSession(LotoMainActivity.this, new CallbackInterface() {
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
       /* toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        setSupportActionBar(toolbar);*/

        list_permit = findViewById(R.id.list_permit);
        txt_no_record = findViewById(R.id.txt_no_record);
        progress = findViewById(R.id.progress);

    }

    private void setListner() {

        list_permit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PermitNo = permitArrayList.get(position).getPermitNo();
                String Attachment = permitArrayList.get(position).getIsolatedImageName();
                String Attachment1 = permitArrayList.get(position).getElectricalImageName();

                if (Attachment != null || Attachment != "" || Attachment1 != "" || Attachment1 != "null") {
                    startActivity(new Intent(LotoMainActivity.this, LOTOPermitActivity.class).putExtra("Attachment", Attachment).putExtra("Attachment1", Attachment1));
                } else {
                    Toast.makeText(LotoMainActivity.this, "No Attachment present", Toast.LENGTH_SHORT).show();

                }

            }
        });

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
            String url = CompanyURL + WebAPIUrl.api_LotoPermit_Attachment;

            try {
                res = CommonClass.OpenConnection(url, LotoMainActivity.this);
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

            progress.setVisibility(View.GONE);
            if (response.contains("[]")) {

                Toast.makeText(LotoMainActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        Permit permit = new Permit();
                        JSONObject jorder = jResults.getJSONObject(i);

                        String Adddate = jorder.getString("AddedDt");
                        permit.setAddedDt(Adddate);
                        permit.setPermitNo(jorder.getString("permitno"));
                        permit.setLocation_operation(jorder.getString("WarehouseDescription"));
                        permit.setNature_Operation(jorder.getString("Operation"));
                        permit.setIsolatedImageName(jorder.getString("IsolatedImageName"));
                        permit.setElectricalImageName(jorder.getString("ElectricalImageName"));
                        permit.setAttachment(jorder.getString("Attachment"));
                        permit.setAttachment1(jorder.getString("Attachment1"));

                        if (jorder.getString("IsolatedImageName").equalsIgnoreCase("") &&
                                jorder.getString("ElectricalImageName").equalsIgnoreCase("")) {
                              /*  jorder.getString("Attachment1").equalsIgnoreCase("") ||
                                jorder.getString("Attachment1").equalsIgnoreCase(null))*/
                        } else {
                            permitArrayList.add(permit);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                permitmainAdapter = new LotoMainAdapter(LotoMainActivity.this, permitArrayList);
                list_permit.setAdapter(permitmainAdapter);


            }


        }
    }
}
