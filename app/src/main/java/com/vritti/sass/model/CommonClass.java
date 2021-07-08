package com.vritti.sass.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sharvari on 29-Nov-18.
 */

public class CommonClass {
    public static DefaultHttpClient httpClient = new DefaultHttpClient();
    public static CommonClass mInstance = null;
    private static Context mContext;
    private static final String My_Preferences = "myPreferences";
    private static final String Job_Dispacher_start = "Job_Dispacher_start";

    public static CommonClass getInstance( Context context){
        if (mInstance ==null){
            mInstance = new CommonClass();
        }
        mContext = context;
        return mInstance;
    }


    public static boolean checkNet(Context mContext) {
        Context context = mContext;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static void displayToast(Context mContext, String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    public static String OpenConnection(String url, Context mContext) {

        String res = "";
        InputStream inputStream = null;
        SharedPreferences sp = mContext.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        try {
            HttpGet httppost = new HttpGet(url.toString());
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            HttpResponse response = null;

            response = httpClient.execute(httppost);
           /* inputStream = response.getEntity().getContent();
String result;
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";*/

            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity);
            long consumedNow = entity.getContentLength();
            long consumed = 0;
            long check = sp.getLong("consumed", 0);
            if (check == 0) {
                consumed = consumedNow;
            } else {
                consumed = check;
                consumed += consumedNow;
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong("consumed", consumed);
            editor.commit();
            Log.e("consumed data :", "" + consumedNow);

        } catch (IOException e) {
            e.printStackTrace();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy hh:mm:ss aa");
            String Ldate = sdf.format(new Date());
            StackTraceElement l = new Exception().getStackTrace()[0];
            System.out.println(l.getClassName() + "/" + l.getMethodName()
                    + ":" + l.getLineNumber());

            String data = l.getClassName() + "/" + l.getMethodName()
                    + ":" + l.getLineNumber() + "	" + e.getMessage()
                    + " " + Ldate;
            WriteErrLogFile(data);
        }

        return res;
    }

    public static void WriteErrLogFile(String Error) {
        SimpleDateFormat dff = new SimpleDateFormat("dd_MMM_yyyy");
        String Logfile = dff.format(new Date());
        String a = Environment.getExternalStorageDirectory().toString();
        File Logsdir = new File(Environment.getExternalStorageDirectory()
                + "/SASSLogs");

        if (!Logsdir.exists()) {
            Logsdir.mkdirs();
        }
        File file = new File(Environment.getExternalStorageDirectory()
                + "/SASSLogs", Logfile + ".txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream fOut = new FileOutputStream(file, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append("\n" + "*" + Error + "\n");
                myOutWriter.close();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(file, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append("\n" + "*" + Error + "\n");
                myOutWriter.close();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static Object OpenPostConnection(String url, String FinalObj, Context mContext) {
        String res = null;
        Object response = null;
        InputStream inputStream = null;
        HttpPost httppost = new HttpPost(url);
        StringEntity se = null;
        SharedPreferences sp = mContext.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        try {
            se = new StringEntity(FinalObj);
            httppost.setEntity(se);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            ResponseHandler responseHandler = new BasicResponseHandler();

            response = httpClient.execute(httppost, responseHandler);


            long consumedNow = httppost.getEntity().getContentLength();
            long consumed = 0;
            long check = sp.getLong("consumed", 0);
            if (check == 0) {
                consumed = consumedNow;
            } else {
                consumed = check;
                consumed += consumedNow;
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong("consumed", consumed);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy hh:mm:ss aa");
            String Ldate = sdf.format(new Date());
            StackTraceElement l = new Exception().getStackTrace()[0];
            System.out.println(l.getClassName() + "/" + l.getMethodName()
                    + ":" + l.getLineNumber());

            String data = l.getClassName() + "/" + l.getMethodName()
                    + ":" + l.getLineNumber() + "	" + e.getMessage()
                    + " " + Ldate;
            WriteErrLogFile(data);
        }

        return response;
    }



    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setServiceStarted(boolean value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(My_Preferences ,Context.MODE_PRIVATE ).edit();
        editor.putBoolean(Job_Dispacher_start , value);
        editor.apply();
    }

    public boolean isServiceIsStart(){
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(My_Preferences , Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean(Job_Dispacher_start , false);
    }
}


