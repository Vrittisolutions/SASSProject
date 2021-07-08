package com.vritti.sass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.imagepipeline.producers.JobScheduler;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.adapter.DepotAdapter;
import com.vritti.sass.adapter.PermitMenuListAdapter;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.Depot;
import com.vritti.sass.model.PermitMenuList;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.WebAPIUrl;
import com.vritti.sass.service.MyJobServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SupervisorMainActivity extends AppCompatActivity {


    LinearLayout len_workauth, len_loto_permit, len_work_height, len_cleansing, len_confined, len_hot_work, len_excavation_permit, len_lifting_permit;
    SharedPreferences userpreferences;
    PermitMenuList permitMenuList;
    PermitMenuListAdapter permitMenuListAdapter;
    ListView listview_menulist;
    String CompanyURL;
    ProgressBar mprogress;
    SharedPreferences sharedPrefs;
    Gson gson;
    String json;
    Type type;
    ArrayList<PermitMenuList> permitMenuListArrayList;
    private View footerView;
    TextView txt_permit_contractor;
    JobScheduler jobScheduler;
    private static final int MYJOBID = 1;
    private static final String TAG1 = "MyJobService";
    public static FirebaseJobDispatcher dispatcher;
    public static Job myJob = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervisor_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        toolbar.setTitle(getResources().getString(R.string.application_name));
        setSupportActionBar(toolbar);
        userpreferences = getSharedPreferences(LoginActivity.USERINFO,
                Context.MODE_PRIVATE);
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);

        listview_menulist = findViewById(R.id.listview_menulist);
        CompanyURL = userpreferences.getString("CompanyURL", "");
        permitMenuListArrayList = new ArrayList<>();


        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view_lay, null, false);

        txt_permit_contractor = footerView.findViewById(R.id.txt_permit_contractor);


        txt_permit_contractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SupervisorMainActivity.this, ContractorListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });


        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SupervisorMainActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("permit", "");
        type = new TypeToken<List<PermitMenuList>>() {
        }.getType();
        permitMenuListArrayList = gson.fromJson(json, type);

        //new DownloadPermitData().execute();

        if (permitMenuListArrayList == null) {
            if (CommonClass.checkNet(SupervisorMainActivity.this)) {
                showProgress();
                new StartSession(SupervisorMainActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadPermitData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(SupervisorMainActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (permitMenuListArrayList.size() > 0) {

                permitMenuListAdapter = new PermitMenuListAdapter(SupervisorMainActivity.this, permitMenuListArrayList);
                listview_menulist.setAdapter(permitMenuListAdapter);
                listview_menulist.addFooterView(footerView);


            }

        }


        listview_menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String Permitname = permitMenuListArrayList.get(position).getFormDesc();
                String PKFormId = permitMenuListArrayList.get(position).getPKFormId();
                String formcode = permitMenuListArrayList.get(position).getFormcode();

                if (Permitname.equalsIgnoreCase("Work Authorization")) {
                    startActivity(new Intent(SupervisorMainActivity.this, WorkAuthorizationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("PKFormId", PKFormId).putExtra("formcode", formcode));
                } else if (Permitname.equalsIgnoreCase("Hot Work Permit/Fire")) {
                    startActivity(new Intent(SupervisorMainActivity.this, HOTWorkActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("PKFormId", PKFormId).putExtra("formcode", formcode));
                } else if (Permitname.equalsIgnoreCase("Cleaning and Degassing")) {
                    startActivity(new Intent(SupervisorMainActivity.this, CleansingPermitActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("PKFormId", PKFormId).putExtra("formcode", formcode));
                } else if (Permitname.equalsIgnoreCase("Confined Space Entry")) {
                    startActivity(new Intent(SupervisorMainActivity.this, ConfinedSpaceEntryActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("PKFormId", PKFormId).putExtra("formcode", formcode));
                } else if (Permitname.equalsIgnoreCase("Work At Height")) {
                    startActivity(new Intent(SupervisorMainActivity.this, WorkAtHeightPermitActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("PKFormId", PKFormId).putExtra("formcode", formcode));
                } else if (Permitname.equalsIgnoreCase("Lifting Permit")) {
                    startActivity(new Intent(SupervisorMainActivity.this, LiftingPermitActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("PKFormId", PKFormId).putExtra("formcode", formcode));
                } else if (Permitname.equalsIgnoreCase("Excavation Permit")) {
                    startActivity(new Intent(SupervisorMainActivity.this, ExcavationPermitActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("PKFormId", PKFormId).putExtra("formcode", formcode));
                } else if (Permitname.equalsIgnoreCase("Permit Status")) {
                    startActivity(new Intent(SupervisorMainActivity.this, PermitStatusActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("PKFormId", PKFormId).putExtra("formcode", formcode));

                }
            }
        });

        setJobShedulder();
    }

    class DownloadPermitData extends AsyncTask<String, Void, String> {
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

            String url = CompanyURL + WebAPIUrl.api_GetPermitData;

            try {
                res = CommonClass.OpenConnection(url, SupervisorMainActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    permitMenuListArrayList = new ArrayList<>();
                    permitMenuListArrayList.clear();

                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        permitMenuList = new PermitMenuList();
                        JSONObject jorder = jResults.getJSONObject(i);

                        permitMenuList.setFormDesc(jorder.getString("FormDesc"));
                        permitMenuList.setPKFormId(jorder.getString("PKFormId"));
                        permitMenuList.setFormcode(jorder.getString("formcode"));
                        permitMenuListArrayList.add(permitMenuList);


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
                Toast.makeText(SupervisorMainActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SupervisorMainActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(permitMenuListArrayList);
                editor.putString("permit", json);
                editor.commit();
                permitMenuListAdapter = new PermitMenuListAdapter(SupervisorMainActivity.this, permitMenuListArrayList);
                listview_menulist.setAdapter(permitMenuListAdapter);
                listview_menulist.addFooterView(footerView);


            }


        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        switch (item.getItemId()) {
            case R.id.logout:

                SharedPreferences.Editor editor = userpreferences.edit();
                editor.remove("EnvMasterId");
                editor.remove("PlantMasterId");
                editor.remove("LoginId");
                editor.remove("Password");
                editor.remove("UserMasterId");
                editor.remove("MobileNo");
                editor.commit();
                startActivity(new Intent(SupervisorMainActivity
                        .this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showProgress() {

        mprogress.setVisibility(View.VISIBLE);

    }

    private void dismissProgress() {

        mprogress.setVisibility(View.GONE);


    }

    private void callJobDispacher() {
        myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(MyJobServices.class)
                // uniquely identifies the job
                .setTag("test")
                // one-off job
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(180, 240))//15min
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(

                        // only run on an unmetered network
                        Constraint.ON_ANY_NETWORK,
                        // only run when the device is charging
                        Constraint.DEVICE_IDLE
                )
                .build();

        dispatcher.mustSchedule(myJob);
        CommonClass.getInstance(this).setServiceStarted(true);
    }

    private void setJobShedulder() {

        if (myJob == null) {
            dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(SupervisorMainActivity.this));
            callJobDispacher();
        } else {


            if (!CommonClass.getInstance(this).isServiceIsStart()) {
                dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(SupervisorMainActivity.this));
                callJobDispacher();
            } else {
                dispatcher.cancelAll();
                dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(SupervisorMainActivity.this));
                myJob = null;
                callJobDispacher();
            }
        }

    }

}
