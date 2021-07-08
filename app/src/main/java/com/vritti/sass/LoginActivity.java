package com.vritti.sass;


import android.app.ProgressDialog;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.WebAPIUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;



public class LoginActivity extends AppCompatActivity {
    // DownloadAuthenticate();
    private CommonClass cc;
    Button btnLogin;
    Boolean IsSessionActivate, IsValidUser;
    Spinner edEnv, edPlant;
    SharedPreferences userpreferences;
    String EnvMasterId, PlantMasterId="1", LoginId, Password, UserMasterId, MobileNo, IsCrmUser;
    private ProgressDialog progressDialog;
    EditText edLoginId, edPassword, edmob;
    SQLiteDatabase sql;

    public static final String USERINFO = "UserInfo";
    ProgressBar mprogress;
    public static EditText textotp;
    public static Context context;
    public static Intent igpsalarm;
    String App_version;
    private JobScheduler mJobScheduler;

   public String CompanyURL ="http://z207.ekatm.co.in";


   // public String CompanyURL ="http://192.168.1.221";

    //public String CompanyURL = "http://192.168.1.99:81";

    TelephonyManager manager;
    boolean isTablet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_lay);
        cc = new CommonClass();
        InitView();
        setListner();
       /* App_version=intent.getStringExtra("version");*/
        userpreferences = getSharedPreferences(USERINFO,
                Context.MODE_PRIVATE);
        context = getApplicationContext();

        SharedPreferences.Editor editor = userpreferences.edit();
        editor.putString("CompanyURL",CompanyURL);
        editor.commit();


        EnvMasterId = userpreferences.getString("EnvMasterId", null);
        PlantMasterId = userpreferences.getString("PlantMasterId", PlantMasterId);
        LoginId = userpreferences.getString("LoginId", null);
        Password = userpreferences.getString("Password", null);
        UserMasterId = userpreferences.getString("UserMasterId", null);
        CompanyURL = userpreferences.getString("CompanyURL", null);
        MobileNo = userpreferences.getString("MobileNo", null);
        isTablet = getResources().getBoolean(R.bool.is_tablet);


        if (isTablet==false){
            if (UserMasterId!=null){
                startActivity(new Intent(LoginActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        }else {
            if (UserMasterId!=null){
                startActivity(new Intent(LoginActivity.this,SupervisorMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        }




        if (CommonClass.checkNet(getApplicationContext())) {
            new DownloadGetEnvJSON().execute();
        } else {
            cc.displayToast(getApplicationContext(), "No Internet Connetion");
        }

    }

    private void setListner() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkEditText()) {
                    LoginId = edLoginId.getText().toString();
                    Password = edPassword.getText().toString();
                    MobileNo = edmob.getText().toString();
                    SharedPreferences.Editor editor = userpreferences.edit();
                    editor.putString("LoginId", LoginId);
                    editor.putString("Password", Password);
                    editor.putString("MobileNo", MobileNo);
                    editor.commit();
                    new DownloadIsValidUser().execute();
                }
            }
        });



    }

    private void InitView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);
        edEnv = findViewById(R.id.edEnv);
        edPlant = findViewById(R.id.edPlant);
        ((Spinner) findViewById(R.id.edEnv)).setSelection(0);
        ((Spinner) findViewById(R.id.edPlant)).setSelection(0);
        edLoginId = findViewById(R.id.edLoginId);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        edmob = findViewById(R.id.edmob);

    }





    private Boolean checkEditText() {
        if (!(edLoginId.getText().toString().length() > 0)) {
            cc.displayToast(getApplicationContext(), "Please Enter User ID");
            return false;
        } else if (!(edPassword.getText().toString().length() > 0)) {
            cc.displayToast(getApplicationContext(), "Please Enter Password");
            return false;
        } else if (!(edmob.getText().toString().length() > 0)) {
            cc.displayToast(getApplicationContext(), "Please Enter Register Mobile No.");
            return false;
        }else if (!(edmob.getText().toString().length() > 9)) {
            cc.displayToast(getApplicationContext(), "Please Enter valid Mobile No.");
            return false;
        }

        return true;
    }

    class DownloadIsValidUser extends AsyncTask<Integer, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();*/
            //  showProgressDialog();

        //    mprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Integer... params) {

            String response;
            try {
                String url = CompanyURL + WebAPIUrl.api_GetIsValidUser + "?AppEnvMasterId=" + URLEncoder.encode(EnvMasterId, "UTF-8") +
                        "&PlantId=" + URLEncoder.encode(PlantMasterId, "UTF-8") + "&UserLoginId="
                        + URLEncoder.encode(LoginId, "UTF-8") + "&UserPwd=" + URLEncoder.encode(Password, "UTF-8");
                res = CommonClass.OpenConnection(url,LoginActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                response.substring(1, response.length() - 1);
                //IsValidUser = Boolean.parseBoolean(res);


            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //  progressDialog.dismiss();
            // dismissProgressDialog();
         //   mprogress.setVisibility(View.INVISIBLE);
            if (integer.contains("true")) {

               if(CommonClass.checkNet(LoginActivity.this)) {
                   new GetSessionFromServer().execute() ;
               }else{
                   Toast.makeText(LoginActivity.this, "Please Check Internet", Toast.LENGTH_SHORT).show();
               }
            } else if (integer.contains("Invalid Password And Plant")) {
                cc.displayToast(getApplicationContext(), "Invalid Password And Plant");
            } else if (integer.contains("Invalid Password")) {
                cc.displayToast(getApplicationContext(), "Invalid Password");
            } else if (integer.contains("You are not valid user for selected plant")) {
                cc.displayToast(getApplicationContext(), "You are not valid user for selected plant");
            } else {
                cc.displayToast(getApplicationContext(), "Not Valid User");
            }
        }

    }

    class GetSessionFromServer extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Integer doInBackground(Integer... params) {



            try {

                String url = CompanyURL + WebAPIUrl.api_GetSessions + "?AppEnvMasterId=" +
                        URLEncoder.encode(EnvMasterId, "UTF-8") + "&UserLoginId=" + URLEncoder.encode(LoginId, "UTF-8") + "&UserPwd=" + URLEncoder.encode(Password, "UTF-8") + "&PlantId=" + URLEncoder.encode(PlantMasterId, "UTF-8");
                res = CommonClass.OpenConnection(url,getApplicationContext());
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll(" ", "");
                IsSessionActivate = Boolean.parseBoolean(res);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (IsSessionActivate) {
                new DownloadUserMasterIdFromServer().execute();

            } else {
                cc.displayToast(getApplicationContext(), "The server is taking too long to respond OR something " +
                        "is wrong with your internet connection. Please try again later");
            }

        }

    }

    private JSONObject getJobject() {
        JSONObject data = null;
        try {
            data = new JSONObject();
            userpreferences = getSharedPreferences(WebAPIUrl.MyPREFERENCES,
                    Context.MODE_PRIVATE);
            String Token = userpreferences.getString(WebAPIUrl.MyPREFERENCES_FIREBASE_TOKEN_KEY, null);

            data.put("App_Name", WebAPIUrl.AppName);
            data.put("UserMasterId", UserMasterId);
            data.put("DeviceId", Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return data;
    }

    class UploadFCMDetail extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebAPIUrl.FCMurl;
            try {
                res = CommonClass.OpenPostConnection(url, params[0],getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
          //  new DownloadIfCRMUserJson().execute();

            if (integer.equalsIgnoreCase("Fail")) {

            } else if (integer.equalsIgnoreCase("Error")) {
                cc.displayToast(getApplicationContext(), "FCM Registration Failed");
                if (isTablet==false){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                   // startActivity(new Intent(LoginActivity.this,SupervisorMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                }else {

                    startActivity(new Intent(LoginActivity.this,SupervisorMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                }
            } else if (integer.equalsIgnoreCase("Success")) {
                if (isTablet==false){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                }else {

                    startActivity(new Intent(LoginActivity.this,SupervisorMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                }
                // cc.displayToast(getApplicationContext(), "FCM Registration Failed");
            }
        }
    }



    class DownloadUserMasterIdFromServer extends AsyncTask<String , Void, String> {
        String res;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // mprogress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebAPIUrl.api_GetUserMasterId;

            try {
                res = CommonClass.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
              /*  JSONArray jResults = new JSONArray(res);
                for (int index = 0; index < jResults.length(); index++) {

                    JSONObject jorder = jResults.getJSONObject(index);*/
                UserMasterId = res;
                /*}*/

            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            if (res!=null) {
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("UserMasterId", UserMasterId);
                editor.commit();
                Toast.makeText(LoginActivity.this,"Login successful",Toast.LENGTH_SHORT).show();




                //startActivity(new Intent(LoginActivity.this, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            JSONObject Jobj = getJobject();
            String jobject = Jobj.toString().replaceAll("\\\\", "");
            new UploadFCMDetail().execute(jobject);
            }
        }

    }



    class DownloadGetEnvJSON extends AsyncTask<Integer, Void, Integer> {
        String res;
        List<String> EnvName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();*/
            //showProgressDialog();
           // mprogress.setVisibility(View.VISIBLE);

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebAPIUrl.api_getEnv;
            try {
                res = CommonClass.OpenConnection(url,LoginActivity.this);
                if (res!=null) {
                    res = res.replaceAll("\\\\", "");
                    res = res.substring(1, res.length() - 1);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            // progressDialog.dismiss();
            //  dismissProgressDialog();
           // mprogress.setVisibility(View.INVISIBLE);

            if (res.contains("AppEnvMasterId")) {
                try {
                    JSONArray jResults = new JSONArray(res);
                    EnvName = new ArrayList<String>();


                    for (int index = 0; index < jResults.length(); index++) {
                        JSONObject jorder = jResults.getJSONObject(index);
                        EnvName.add(jorder.getString("AppEnvMasterId"));
                        EnvMasterId=jorder.getString("AppEnvMasterId");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_item, EnvName);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                if (EnvName.size()>1){
                    edEnv.setVisibility(View.VISIBLE);
                    edEnv.setAdapter(dataAdapter);

                    SharedPreferences.Editor editor = userpreferences.edit();
                    editor.putString("PlantMasterId", PlantMasterId);
                    editor.putString("EnvMasterId",EnvMasterId);

                    editor.commit();
                }else {
                    edEnv.setVisibility(View.GONE);
                    SharedPreferences.Editor editor = userpreferences.edit();
                    editor.putString("PlantMasterId", PlantMasterId);
                    editor.putString("EnvMasterId",EnvMasterId);

                    editor.commit();

                }

            } else {
                cc.displayToast(getApplicationContext(), "Error in Parsing EnvMaster ID");

                if(CommonClass.checkNet(LoginActivity.this)){
                    new StartSession(LoginActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadGetEnvJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            CommonClass.displayToast(LoginActivity.this,msg);

                        }
                    });
                }else{

                }

            }
        }
    }




}

