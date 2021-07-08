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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.adapter.ContractorAdapter;
import com.vritti.sass.adapter.PermitContractorListAdapter;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.Contractor;
import com.vritti.sass.model.ContractorList;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.WebAPIUrl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.vritti.sass.model.WebAPIUrl.GetTaskAutority_OnEncrptNm;

/**
 * Created by sharvari on 30-Nov-18.
 */

public class ContractorListActivity extends AppCompatActivity {


    RecyclerView list_contractor;
    ContractorAdapter contractorAdapter;
    ArrayList<Contractor> contractorArrayList;
    ContractorAdapter contarctorAdapter;

    SharedPreferences userpreferences;
    String CompanyURL;
    ProgressBar mprogress;
    SharedPreferences sharedPrefs;
    Gson gson;
    String json;
    Type type;
    private boolean ascending = true;
    Button btn_add;
    String nameEdit = "", custVendorMasterIdEdit = "", codeEdit = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contractor_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        toolbar.setTitle("Contractor List");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        userpreferences = getSharedPreferences(LoginActivity.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", "");


        list_contractor = findViewById(R.id.list_contractor);
        btn_add = findViewById(R.id.btn_add);

        mprogress = findViewById(R.id.toolbar_progress_App_bar);
        contractorArrayList = new ArrayList<>();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ContractorListActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Contractorlist", "");
        type = new TypeToken<List<Contractor>>() {
        }.getType();
        contractorArrayList = gson.fromJson(json, type);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommonClass.checkNet(ContractorListActivity.this)) {
                    new StartSession(ContractorListActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadGetTaskAutority_OnEncrptNm().execute("A");
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                } else {
                    Toast.makeText(ContractorListActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                }

                //startActivity(new Intent(ContractorListActivity.this, ContractorAddActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
       /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        list_contractor.setLayoutManager(mLayoutManager);
        ContarctorAdapter  contractorAdapter = new ContarctorAdapter();
        list_contractor.setAdapter(contractorAdapter);*/
        // recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (contractorArrayList == null) {
            if (CommonClass.checkNet(ContractorListActivity.this)) {
                showProgress();
                new StartSession(ContractorListActivity.this, new CallbackInterface() {
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

        } else {
            if (contractorArrayList.size() > 0) {
               /* contractorAdapter = new ContractorAdapter(ContractorListActivity.this, contractorArrayList);
                //list_contractor.setAdapter(new ArrayAdapter(ContractorListActivity.this,android.R.layout.simple_list_item_1,contractorArrayList));
                list_contractor.setAdapter(contractorAdapter);*/

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                list_contractor.setLayoutManager(mLayoutManager);
                contractorAdapter = new ContractorAdapter(this, contractorArrayList);
                sortData(ascending);
                list_contractor.setAdapter(contractorAdapter);
            }

        }

    }

    public void rowClick(int adapterPosition) {

        nameEdit = contractorArrayList.get(adapterPosition).getContractorName();
        custVendorMasterIdEdit = contractorArrayList.get(adapterPosition).getCustVendorMasterId();
        codeEdit = contractorArrayList.get(adapterPosition).getCustVendorCode();


        if (CommonClass.checkNet(ContractorListActivity.this)) {
            new StartSession(ContractorListActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadGetTaskAutority_OnEncrptNm().execute("E");
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        } else {
            Toast.makeText(ContractorListActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

  /* public void editName(int adapterPosition) {
       contractorArrayList.get(adapterPosition).getContractorName();
       contractorArrayList.remove(adapterPosition);
       contarctorAdapter.notifyItemRemoved(adapterPosition);
    }*/

    public class DownloadContractorData extends AsyncTask<String, Void, String> {
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

            String url = CompanyURL + WebAPIUrl.api_GetContractorList;

            try {
                res = CommonClass.OpenConnection(url, ContractorListActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    contractorArrayList = new ArrayList<>();
                    contractorArrayList.clear();

                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        Contractor contractorList = new Contractor();
                        JSONObject jorder = jResults.getJSONObject(i);

                        contractorList.setContractorName(jorder.getString("CustVendorName"));
                        contractorList.setCustVendorCode(jorder.getString("CustVendorCode"));
                        contractorList.setCustVendorMasterId(jorder.getString("CustVendorMasterId"));
                        contractorArrayList.add(contractorList);
                    }
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
                //   Toast.makeText(ContractorListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ContractorListActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(contractorArrayList);
                editor.putString("Contractorlist", json);
                editor.commit();

                /*RecyclerView.LayoutManager mLayoutManager = new LinearLayoutM anager(getApplicationContext());
                list_contractor.setLayoutManager(mLayoutManager);
                contractorAdapter = new ContractorAdapter(this,contractorArrayList);
                sortData(ascending);
                //.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                list_contractor.setAdapter(contarctorAdapter);*/

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                list_contractor.setLayoutManager(mLayoutManager);
                contractorAdapter = new ContractorAdapter(ContractorListActivity.this, contractorArrayList);
                sortData(ascending);
                list_contractor.setAdapter(contractorAdapter);


            }


        }
    }

    private void sortData(boolean asc) {
        //SORT ARRAY ASCENDING AND DESCENDING
        if (asc) {

            Collections.sort(contractorArrayList, new Comparator<Contractor>() {
                @Override
                public int compare(Contractor o1, Contractor o2) {
                    return o1.getContractorName().compareTo(o2.getContractorName());
                }
            });
        } else {

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

                if (CommonClass.checkNet(ContractorListActivity.this)) {
                    showProgress();
                    new StartSession(ContractorListActivity.this, new CallbackInterface() {
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

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DownloadGetTaskAutority_OnEncrptNm extends AsyncTask<String, Void, String> {
        String res = "", response = "";
        String url;
        String mode = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            mode = params[0];
            url = CompanyURL + GetTaskAutority_OnEncrptNm;
            res = CommonClass.OpenConnection(url, ContractorListActivity.this);
            if (res != null) {
                response = res.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (response.equals("true")) {
                if (mode.equals("A")) {
                    startActivity(new Intent(ContractorListActivity.this, ContractorAddActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else if (mode.equals("E")) {
                    startActivity(new Intent(ContractorListActivity.this, ContractorAddActivity.class)
                            .putExtra("name", nameEdit)
                            .putExtra("id", custVendorMasterIdEdit)
                            .putExtra("preven", codeEdit).
                                    putExtra("Mode", "E").putExtra("AccessRight",response));
                }
            } else {
                if(mode.equals("E")){
                    startActivity(new Intent(ContractorListActivity.this, ContractorAddActivity.class)
                            .putExtra("name", nameEdit)
                            .putExtra("id", custVendorMasterIdEdit)
                            .putExtra("preven", codeEdit).
                                    putExtra("Mode", "E").putExtra("AccessRight",response));
                    Toast.makeText(ContractorListActivity.this, "You do not have access rights", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ContractorListActivity.this, "You do not have access rights", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
