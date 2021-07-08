package com.vritti.sass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.adapter.MaintainanceAdapter;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.Contractor;
import com.vritti.sass.model.Depot;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.WebAPIUrl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ListView listview_downgrade;
    DowngradeConditionAdapter downgradeConditionAdapter;
    ScrollView scroll_menu;
    LinearLayout len_work_authorization,len_contractor,len_loto_permit,len_shifthandover,len_plant_status,len_spotcheck,
            len_maintainance,len_shifthandover_req,len_alarm;

    ArrayList<DowngradeCondition>downgradeConditionArrayList;
    SharedPreferences userpreferences;
    SharedPreferences sharedPrefs;
    Gson gson;
    String json;
    private Type type;
    private String CompanyURL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        setSupportActionBar(toolbar);

        userpreferences = getSharedPreferences(LoginActivity.USERINFO, Context.MODE_PRIVATE);

        CompanyURL = userpreferences.getString("CompanyURL", "");

        listview_downgrade= (ListView) findViewById(R.id.listview_downgrade);
        len_work_authorization= (LinearLayout) findViewById(R.id.len_work_authorization);
        len_contractor= (LinearLayout) findViewById(R.id.len_contractor);
        len_loto_permit= (LinearLayout) findViewById(R.id.len_loto_permit);
        len_shifthandover = (LinearLayout)findViewById(R.id.len_shifthandover);
        len_plant_status = (LinearLayout)findViewById(R.id.len_plant_status);
        len_maintainance = (LinearLayout)findViewById(R.id.len_maintainance);
        len_spotcheck = (LinearLayout)findViewById(R.id.len_spotcheck);
        len_shifthandover_req = (LinearLayout)findViewById(R.id.len_shifthandover_req);
        len_alarm = (LinearLayout)findViewById(R.id.len_alarm);



        downgradeConditionArrayList=new ArrayList<>();

        if (CommonClass.checkNet(MainActivity.this)) {
            new StartSession(MainActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadContractorData().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    CommonClass.displayToast(getApplicationContext(), msg);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }



        listview_downgrade.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        len_work_authorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PermitStatusActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        len_contractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ContractorListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        len_loto_permit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LotoMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        len_shifthandover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ShiftHandOverActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        len_plant_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PlantStatusActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        len_maintainance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MaintainanceListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        len_spotcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SpotCheckActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        len_shifthandover_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShiftHandoverListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        len_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AlarmListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });



    }
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
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
                startActivity(new Intent(MainActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) );
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    class DownloadContractorData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.GetDowngradData;

            try {
                res = CommonClass.OpenConnection(url,MainActivity.this);
                if (res != null) {
                    response = res.toString();
                    //    response = res.toString().replaceAll("\\\\", "");
                    ///  response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);


                    downgradeConditionArrayList.clear();

                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        DowngradeCondition maintainlist = new DowngradeCondition();
                        JSONObject jorder = jResults.getJSONObject(i);

                        maintainlist.setDowngradConditions(jorder.getString("DowngradConditions"));
                        maintainlist.setDateTime1(jorder.getString("DateTime1"));
                        downgradeConditionArrayList.add(maintainlist);



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


            if (response.contains("[]")) {
                //  Toast.makeText(MaintainanceListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                listview_downgrade.setVisibility(View.VISIBLE);
                downgradeConditionAdapter = new DowngradeConditionAdapter(MainActivity.this, downgradeConditionArrayList);
                listview_downgrade.setAdapter(downgradeConditionAdapter);
                setListViewHeightBasedOnItems(listview_downgrade);


            }


        }
    }

}
