package com.vritti.sass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.adapter.AlarmAdapter;
import com.vritti.sass.adapter.MaintainanceAdapter;
import com.vritti.sass.model.Alarm;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.Contractor;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.WebAPIUrl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AlarmListActivity extends AppCompatActivity {

    ListView list_maintainance;
    ProgressBar mprogress;
    ArrayList<Alarm> alarmArrayList;
    SharedPreferences sharedPrefs;
    Gson gson;
    String json;
    Type type;
    // CommonClass CommonClass;
    AlarmAdapter alarmAdapter;
    String CompanyURL;
    String response = "";
    SharedPreferences userpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintainance_list);
        //setContentView(R.layout.contractor_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        toolbar.setTitle("Alarm List");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        userpreferences = getSharedPreferences(LoginActivity.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", "");


        list_maintainance = findViewById(R.id.list_maintain);
        mprogress = findViewById(R.id.toolbar_progress_App_bar);
        alarmArrayList = new ArrayList<>();

        if (CommonClass.checkNet(AlarmListActivity.this)) {
            showProgress();
            new StartSession(AlarmListActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadContractorData().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    CommonClass.displayToast(getApplicationContext(), msg);
                    dismissProgress();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }




    class DownloadContractorData extends AsyncTask<String, Void, String> {
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

            String url = CompanyURL + WebAPIUrl.GetAlarmTagData;

            try {
                res = CommonClass.OpenConnection(url, AlarmListActivity.this);
                if (res != null) {
                    response = res.toString();
                //    response = res.toString().replaceAll("\\\\", "");
                  ///  response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);


                    alarmArrayList.clear();

                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        Alarm alarm = new Alarm();
                        JSONObject jorder = jResults.getJSONObject(i);

                        alarm.setTagName(jorder.getString("TagName"));
                        alarm.setTagValue(jorder.getString("TagValue"));
                        alarm.setAddeddt(jorder.getString("Addeddt"));
                        alarm.setTagChart(jorder.getString("TagChart"));
                        alarmArrayList.add(alarm);



                    }


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
              //  Toast.makeText(MaintainanceListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                alarmAdapter= new AlarmAdapter(AlarmListActivity.this,alarmArrayList);
                list_maintainance.setAdapter(alarmAdapter);



            }


        }
    }

    private void showProgress() {

        mprogress.setVisibility(View.VISIBLE);

    }

    private void dismissProgress() {

        mprogress.setVisibility(View.GONE);


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        switch (item.getItemId()) {
            case R.id.refresh:

                if (CommonClass.checkNet(AlarmListActivity.this)) {
                    showProgress();
                    new StartSession(AlarmListActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadContractorData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


    /*private void showProgress() {
        mprogress.setVisibility(View.VISIBLE);
    }
    private void dismissProgress(){
        mprogress.setVisibility(View.GONE);
    }*/



