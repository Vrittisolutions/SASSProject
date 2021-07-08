package com.vritti.sass.service;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.LoginActivity;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.WebAPIUrl;



public class MyJobServices extends JobService {
    private MyJobServices context;
    String CompanyURL;

    SharedPreferences userpreferences;

    private static final String TAG1 = "MyJobService";

    @Override
    public boolean onStartJob(JobParameters job) {
        context = MyJobServices.this;
        userpreferences = getSharedPreferences(LoginActivity.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", "");
        //  Toast.makeText(parent, "InJobServices", Toast.LENGTH_SHORT).show();



        if (CommonClass.checkNet(MyJobServices.this)) {
            new StartSession(MyJobServices.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetPermitComplete().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(MyJobServices.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }


    // private static final int TWO_SECONDS = 1000 * 2;



    class GetPermitComplete extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = CompanyURL + WebAPIUrl.api_GetCompletePermit;

            res = CommonClass.OpenConnection(url, MyJobServices.this);
            if (res != null) {
                response = res.toString();

                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");


            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(response.equals("false")){
                //Toast.makeText(SupervisorMainActivity.this, "", Toast.LENGTH_SHORT).show();
            }else{

            }
        }
    }
}
