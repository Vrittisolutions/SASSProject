package com.vritti.sass;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.adapter.AdapterQuestion;
import com.vritti.sass.adapter.AuthorizedPersonAdapter;
import com.vritti.sass.adapter.DepotAdapter;
import com.vritti.sass.model.AuthorizedPerson;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.ContractorList;
import com.vritti.sass.model.Location;
import com.vritti.sass.model.Operation;
import com.vritti.sass.model.Question;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.WebAPIUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.vritti.sass.LoginActivity.USERINFO;

public class ShiftHandOverActivity extends AppCompatActivity {


    Spinner spinner_shift;
    CheckBox checkbox_condition,checkbox_hotwork,checkbox_permit_issue,
            checkbox_loto_permits,checkbox_plant_status;
    Button btn_hslevel,btn_seapot_pressure,btn_gantry_massflow,btn_submit;
    EditText edit_notes;
    String response;
    JSONObject ActivityJson;
    String shift="",ConditionCheck="N",HotWorkCheck = "N",PermitIssueCheck="N",LotoCheck="N",PlantCheck="N",Notes;
    private ArrayList<AuthorizedPerson> authorizedPersonArrayList;
    AuthorizedPersonAdapter authorizedPersonAdapter;
    private String Que1="",Que2="",Que3="",Ans1="",Ans2="",Ans3="",UserMasterId="";
    private SharedPreferences userpreferences;
    private String  FromTime="9 AM";
    private String  ToTime="10 PM";
    AdapterQuestion adapterQuestion;
    RecyclerView my_recycler_view;

    ArrayList<Question>questionArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_handover_mockup);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        toolbar.setTitle("Shift Hand Over");

        Intent intent = getIntent();


        spinner_shift = (Spinner)findViewById(R.id.spinner_shift);
        checkbox_condition = (CheckBox)findViewById(R.id.checkbox_condition);
        checkbox_hotwork = (CheckBox)findViewById(R.id.checkbox_hotwork);
        checkbox_loto_permits = (CheckBox)findViewById(R.id.checkbox_loto_permits);
        checkbox_permit_issue = (CheckBox)findViewById(R.id.checkbox_permit_issue);
        checkbox_plant_status=findViewById(R.id.checkbox_plant_status);

     /*   btn_hslevel = (Button)findViewById(R.id.btn_hslevel);
        btn_seapot_pressure = (Button)findViewById(R.id.btn_seapot_pressure);
        btn_gantry_massflow = (Button)findViewById(R.id.btn_gantry_massflow);*/
        btn_submit = (Button)findViewById(R.id.btn_submit);
        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);



        String s="Y O6C FRONT BLACK ";
        Log.d("B", String.valueOf(s.length()));
        String s1="B97-F5168-00 ";
        Log.d("B1", String.valueOf(s1.length()));
        String s2="QTY: 2 NOS ";
        Log.d("B2", String.valueOf(s2.length()));
        String s3="30012021.15:24.";
        Log.d("B2", String.valueOf(s3.length()));

        questionArrayList=new ArrayList<>();




        edit_notes = (EditText)findViewById(R.id.edit_notes);
        authorizedPersonArrayList=new ArrayList<>();

        userpreferences = getSharedPreferences(USERINFO,
                Context.MODE_PRIVATE);
        UserMasterId = userpreferences.getString("UserMasterId", null);



        if (CommonClass.checkNet(ShiftHandOverActivity.this)) {
            new StartSession(ShiftHandOverActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadAuthorizedPersonData().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        if (CommonClass.checkNet(ShiftHandOverActivity.this)) {
            new StartSession(ShiftHandOverActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadQuestionData().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveactivityjson();

            }
        });
        spinner_shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (authorizedPersonArrayList.size()>0){
                    shift = authorizedPersonArrayList.get(position).getAuthorizeid();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        checkbox_condition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ConditionCheck =  "Y";
                }else{
                    ConditionCheck = "N";
                }
            }
        });

        checkbox_hotwork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    HotWorkCheck =  "Y";
                }else{
                    HotWorkCheck = "N";
                }
            }
        });

        checkbox_permit_issue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    PermitIssueCheck =  "Y";
                }else{
                    PermitIssueCheck = "N";
                }
            }
        });

        checkbox_loto_permits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    LotoCheck =  "Y";
                }else{
                    LotoCheck = "N";
                }
            }
        });

        checkbox_plant_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    PlantCheck =  "Y";
                }else{
                    PlantCheck = "N";
                }
            }
        });


    }



    public void saveactivityjson(){
        ActivityJson = new JSONObject();

        Notes = edit_notes.getText().toString();


        questionArrayList= adapterQuestion.getListData();
        Que1 = questionArrayList.get(0).getqId();
        Ans1 = questionArrayList.get(0).getQuestion_ans();
        Que2 = questionArrayList.get(1).getqId();
        Ans2 = questionArrayList.get(1).getQuestion_ans();
        Que3 = questionArrayList.get(2).getqId();
        Ans3 = questionArrayList.get(2).getQuestion_ans();

        try {
            ActivityJson.put("ShifthandoverTo", shift);
            ActivityJson.put("ShifthandoverFrom", UserMasterId);
            ActivityJson.put("PlantDowngardeCondition",ConditionCheck);
            ActivityJson.put("Hotwork",HotWorkCheck);
            ActivityJson.put("PermitIssue",PermitIssueCheck);
            ActivityJson.put("LotoPermit",LotoCheck);
            ActivityJson.put("PlantStatus",PlantCheck);
            ActivityJson.put("Remark",Notes);
            ActivityJson.put("Que1",Que1);
            ActivityJson.put("Ans1",Ans1);
            ActivityJson.put("Que2",Que2);
            ActivityJson.put("Ans2",Ans2);
            ActivityJson.put("Que3",Que3);
            ActivityJson.put("Ans3",Ans3);
            ActivityJson.put("FromTime",FromTime);
            ActivityJson.put("ToTime",ToTime);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String FinalJsonObject = ActivityJson.toString();

        Log.d("Shift",ActivityJson.toString());

        if(CommonClass.checkNet(ShiftHandOverActivity.this)){
            new StartSession(ShiftHandOverActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadPostData().execute(FinalJsonObject);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }


    }


    public class DownloadPostData extends AsyncTask<String, Void, String> {


        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ShiftHandOverActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.show();
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

        @Override
        protected String doInBackground(String... voids) {
            String objFinalObj=voids[0];

            Object res;
            try {
                String url = WebAPIUrl.CompanyURL + WebAPIUrl.api_PostShiftHandOver;
                res = CommonClass.OpenPostConnection(url, objFinalObj, getApplicationContext());
                if (res!=null) {
                    response = res.toString().replaceAll("\\\\", "");
                }
                //   response = response.replaceAll("\"", "");
            } catch (Exception e) {
                e.printStackTrace();
                response = WebAPIUrl.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String val) {
            super.onPostExecute(val);
            progressDialog.dismiss();

            if (val.contains("Shift Hand Over Success")) {//Success
                Toast.makeText(ShiftHandOverActivity.this, "Shift handover successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ShiftHandOverActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));



            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {
                progressDialog.dismiss();
                Toast.makeText(ShiftHandOverActivity.this, val, Toast.LENGTH_LONG).show();
            } else {
                progressDialog.dismiss();
                Toast.makeText(ShiftHandOverActivity.this, val, Toast.LENGTH_LONG).show();
                // startActivity(new Intent(ShiftHandOverActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }
    }

    class DownloadAuthorizedPersonData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ShiftHandOverActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.show();
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @Override
        protected String doInBackground(String... params) {

            String url =  WebAPIUrl.CompanyURL + WebAPIUrl.api_GetApproverPerson;

            try {
                res = CommonClass.OpenConnection(url,ShiftHandOverActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    authorizedPersonArrayList.clear();
                    AuthorizedPerson authorizedPerson = new AuthorizedPerson();
                    authorizedPerson.setAuthorizeid("Select");
                    authorizedPerson.setAuthorizename("Select");
                    authorizedPersonArrayList.add(0,authorizedPerson);

                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        authorizedPerson = new AuthorizedPerson();
                        JSONObject jorder = jResults.getJSONObject(i);
                        authorizedPerson.setAuthorizeid(jorder.getString("UserMasterId"));
                        authorizedPerson.setAuthorizename(jorder.getString("UserName"));
                        authorizedPersonArrayList.add(authorizedPerson);



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

            progressDialog.dismiss();
            if (response.contains("[]")) {
                progressDialog.dismiss();

            } else {

                authorizedPersonAdapter=new AuthorizedPersonAdapter(ShiftHandOverActivity.this,authorizedPersonArrayList);
                spinner_shift.setAdapter(authorizedPersonAdapter);




            }


        }
    }

    class DownloadQuestionData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String url =  WebAPIUrl.CompanyURL + WebAPIUrl.GetRandomQuestionnaireData;

            try {
                res = CommonClass.OpenConnection(url,ShiftHandOverActivity.this);
                if (res != null) {
                    response = res.toString();
                    /*response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);
*/
                    questionArrayList.clear();

                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        Question question= new Question();
                        JSONObject jorder = jResults.getJSONObject(i);
                        question.setShifthandoverQuestionMasterId(jorder.getString("ShifthandoverQuestionMasterId"));
                        question.setTagname(jorder.getString("Tagname"));
                        question.setQuestion(jorder.getString("Question"));
                        questionArrayList.add(question);
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


            adapterQuestion=new AdapterQuestion(ShiftHandOverActivity.this,questionArrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            my_recycler_view.setLayoutManager(mLayoutManager);
            my_recycler_view.setAdapter(adapterQuestion);
        }
    }

}
