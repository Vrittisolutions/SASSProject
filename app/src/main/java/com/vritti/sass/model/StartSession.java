package com.vritti.sass.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.LoginActivity;

import java.net.URLEncoder;

public class StartSession {
    private Context parent;
    String CompanyURL, EnvMasterId, LoginId, Password, PlantMasterId;
    SharedPreferences userpreferences;
    private CallbackInterface callback;

    ProgressBar progressBar ;

    public StartSession(Context context, CallbackInterface callback) {
        parent = context;
        this.callback = callback;
        userpreferences = context.getSharedPreferences(LoginActivity.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", null);
        EnvMasterId = userpreferences.getString("EnvMasterId", null);
        LoginId = userpreferences.getString("LoginId", null);
        Password = userpreferences.getString("Password", null);
        PlantMasterId = userpreferences.getString("PlantMasterId", null);
        //   new DownloadGetEnvJSON().execute();
       // new GetSessionFromServer().execute();
       // progressBar = new ProgressBar(parent);
        new DownloadUserMasterIdFromServer().execute();
    }

    class DownloadUserMasterIdFromServer extends AsyncTask<Integer, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebAPIUrl.api_GetUserMasterIdAndroid;

            try {
                res = CommonClass.OpenConnection(url,parent);
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");


            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (integer.contains("UserMasterId")) {//UserMasterId##317250de-e70b-4a1c-9b6d-e41bc3fb824a
                callback.callMethod();
            } else {
                new GetSessionFromServer().execute();
            }

        }

    }

    class GetSessionFromServer extends AsyncTask<Integer, Void, Integer> {
        String res;
        Boolean IsSessionActivate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           // progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebAPIUrl.api_GetSessions + "?AppEnvMasterId=" +
                        URLEncoder.encode(EnvMasterId, "UTF-8") + "&UserLoginId=" +
                        URLEncoder.encode(LoginId, "UTF-8") + "&UserPwd=" +
                        URLEncoder.encode(Password, "UTF-8") + "&PlantId=" +
                        URLEncoder.encode(PlantMasterId, "UTF-8");

               /* String url="http://z207.ekatm.com/api/LoginAPI/GetSessions?AppEnvMasterId=z207&UserLoginId=1001&UserPwd=7&PlantId=1";*/
                res = CommonClass.OpenConnection(url,parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
                res = res.replaceAll(" ", "");
                IsSessionActivate = Boolean.parseBoolean(res);

            } catch (Exception e) {
                e.printStackTrace();
                IsSessionActivate = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            // dismissProgressDialog();
            if (IsSessionActivate) {
                callback.callMethod();
               // Toast.makeText(parent, "Internet Connection available", Toast.LENGTH_SHORT).show();
            } else {
                 callback.callfailMethod("The server is taking too long to respond or something " +
                         "is wrong with your internet connection. Please try again later");
            }

        }
    }




//<<<<<<<<<<<<<<<<,-------------------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>....





    }
