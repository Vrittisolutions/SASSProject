package com.vritti.sass.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.sass.CleansingPermitActivity;
import com.vritti.sass.ConfinedSpaceEntryActivity;
import com.vritti.sass.ExcavationPermitActivity;
import com.vritti.sass.HOTWorkActivity;
import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.LiftingPermitActivity;
import com.vritti.sass.LoginActivity;
import com.vritti.sass.R;
import com.vritti.sass.WorkAtHeightPermitActivity;
import com.vritti.sass.WorkAuthorizationActivity;
import com.vritti.sass.adapter.PermitAdapter;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.Permit;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.WebAPIUrl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class CompletedPermitFragment extends Fragment {

    View rootView;
    ListView list_permit;
    ArrayList<Permit> permitArrayList;
    PermitAdapter permitAdapter;
    String Permitno="";

    SharedPreferences userpreferences;
    private String CompanyURL, UserID;
    TextView txt_no_record;
    ProgressBar progress;
    private boolean isTablet;

    public CompletedPermitFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.permit_list_lay, container,
                false);
        list_permit = (ListView) rootView.findViewById(R.id.list_permit);
//        list_permit.setEnabled(false);
        txt_no_record= (TextView) rootView.findViewById(R.id.txt_no_record);
        progress=rootView.findViewById(R.id.progress);
        isTablet = getResources().getBoolean(R.bool.is_tablet);

        userpreferences = getActivity().getSharedPreferences(LoginActivity.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", "");
        UserID = userpreferences.getString("UserMasterId", "");

        permitArrayList=new ArrayList<>();
      //  getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        permitAdapter = new PermitAdapter(getActivity(), permitArrayList);
        list_permit.setAdapter(permitAdapter);

        if (CommonClass.checkNet(getActivity())) {
            new StartSession(getActivity(), new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadPermitStatusData().execute();
                    new DownloadHWPermitStatus().execute();
                    new DownloadCADPermitStatus().execute();
                    new DownloadCSEPermitStatus().execute();
                    new downloadWAHPermitstatus().execute();
                    new downloadLPPermitstatus().execute();
                    new downloadEPPermitstatus().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    CommonClass.displayToast(getActivity(), msg);
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        list_permit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isTablet==false){

                }else {

                    Permitno = permitArrayList.get(position).getPermitNo();

                    if (Permitno.contains("HW")) {
                        Intent intent = new Intent(getActivity(), HOTWorkActivity.class);
                        intent.putExtra("permitno", new Gson().toJson(permitArrayList.get(position)));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else if (Permitno.contains("WA")) {
                        Intent intent = new Intent(getActivity(), WorkAuthorizationActivity.class);
                        intent.putExtra("permitno", new Gson().toJson(permitArrayList.get(position)));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else if (Permitno.contains("CD")) {
                        Intent intent = new Intent(getActivity(), CleansingPermitActivity.class);
                        intent.putExtra("permitno", new Gson().toJson(permitArrayList.get(position)));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else if (Permitno.contains("CS")) {
                        Intent intent = new Intent(getActivity(), ConfinedSpaceEntryActivity.class);
                        intent.putExtra("permitno", new Gson().toJson(permitArrayList.get(position)));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else if (Permitno.contains("WH")) {
                        Intent intent = new Intent(getActivity(), WorkAtHeightPermitActivity.class);
                        intent.putExtra("permitno", new Gson().toJson(permitArrayList.get(position)));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else if (Permitno.contains("LP")) {
                        Intent intent = new Intent(getActivity(), LiftingPermitActivity.class);
                        intent.putExtra("permitno", new Gson().toJson(permitArrayList.get(position)));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else if (Permitno.contains("EP")) {
                        Intent intent = new Intent(getActivity(), ExcavationPermitActivity.class);
                        intent.putExtra("permitno", new Gson().toJson(permitArrayList.get(position)));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {

                    }
                }

            }
        });



        return rootView;

    }

    class DownloadPermitStatusData extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_WAStatus + "?AuthorizeBy=" + UserID;

            try {
                res = CommonClass.OpenConnection(url, getActivity());
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    //permitArrayList = new ArrayList<>();
                    //permitArrayList.clear();


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
            try{

            if (response.contains("[]")) {
                progress.setVisibility(View.GONE);
                txt_no_record.setVisibility(View.VISIBLE);
            } else {

                JSONArray jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    Permit permit = new Permit();
                    JSONObject jorder = jResults.getJSONObject(i);
                    String WorkAuthorizationId = jorder.getString("WorkAuthorizationMasterId");
                    String WorkAuthorizationstatus=jorder.getString("WorkAuthorizationstatus");
                    permit.setWorkAuthorizationstatus(WorkAuthorizationstatus);
                    if (WorkAuthorizationstatus.equals("C")) {
                        String WorkAuthorizationMasterId = jorder.getString("WorkAuthorizationMasterId");
                        permit.setWorkAuthorizationMasterId(WorkAuthorizationMasterId);
                        String PkformId = jorder.getString("formid");
                        permit.setPermitId(PkformId);
                        String PermitNo = jorder.getString("PermitNo");
                        permit.setPermitNo(PermitNo);
                        String AddedDt = jorder.getString("AddedDt");
                        permit.setAddedDt(AddedDt);
                        String WarehouseDescription = jorder.getString("WarehouseDescription");
                        permit.setLocation_operation(WarehouseDescription);
                        String Operation = jorder.getString("Operation");
                        permit.setNature_Operation(Operation);

                        permitArrayList.add(permit);
                    }
                }

                permitAdapter.updateList(permitArrayList);
                if (permitAdapter.getCount()!=0){

                }else {
                    txt_no_record.setVisibility(View.VISIBLE);

                }



            }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    class DownloadHWPermitStatus extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_HWStatus +"?AuthorizeBy="+UserID;

            try {
                res = CommonClass.OpenConnection(url,getActivity());
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                   // permitArrayList=new ArrayList<>();
                    //permitArrayList.clear();


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
            try{
            if (response.contains("[]")) {
                progress.setVisibility(View.GONE);
                txt_no_record.setVisibility(View.VISIBLE);
            } else {

                JSONArray jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    Permit permit = new Permit();
                    JSONObject jorder = jResults.getJSONObject(i);

                    String HotWorkPermitStatus=jorder.getString("HotworkpermitStatus");
                    String HotWorkPermiId = jorder.getString("HotWorkPermitMasterId");
                    permit.setWorkAuthorizationstatus(HotWorkPermitStatus);
                    if (HotWorkPermitStatus.equals("C")) {

                        String WorkAuthorizationMasterId = jorder.getString("HotWorkPermitMasterId");
                        permit.setWorkAuthorizationMasterId(WorkAuthorizationMasterId);
                        String PermitNo = jorder.getString("PermitNo");
                        permit.setPermitNo(PermitNo);
                        String PkformId = jorder.getString("formid");
                        permit.setPermitId(PkformId);
                        String AddedDt = jorder.getString("AddedDt");
                        permit.setAddedDt(AddedDt);
                        String WarehouseDescription = jorder.getString("WarehouseDescription");
                        permit.setLocation_operation(WarehouseDescription);
                        String Operation = jorder.getString("Operation");
                        permit.setNature_Operation(Operation);


                        permitArrayList.add(permit);
                    }else{

                    }
                }
                permitAdapter.updateList(permitArrayList);
                if (permitAdapter.getCount()!=0){

                }else {
                    txt_no_record.setVisibility(View.VISIBLE);

                }





            }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class DownloadCADPermitStatus extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_CADStatus +"?AuthorizeBy="+UserID;

            try {
                res = CommonClass.OpenConnection(url,getActivity());
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);


                    // permitArrayList.clear();

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
            try{
            if (response.contains("[]")) {
                progress.setVisibility(View.GONE);
                txt_no_record.setVisibility(View.VISIBLE);
            } else {


                JSONArray jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    Permit permit = new Permit();
                    JSONObject jorder = jResults.getJSONObject(i);

                    String CADPermitstatus=jorder.getString("CleaningandDegassingPermitStatus");
                    String CADPermitId = jorder.getString("CleaningandDegassingPermitId");
                    permit.setWorkAuthorizationstatus(CADPermitstatus);
                    if (CADPermitstatus.equals("C")) {

                        String WorkAuthorizationMasterId = jorder.getString("CleaningandDegassingPermitId");
                        permit.setWorkAuthorizationMasterId(WorkAuthorizationMasterId);
                        String PermitNo = jorder.getString("PermitNo");
                        permit.setPermitNo(PermitNo);
                        String PkformId = jorder.getString("formid");
                        permit.setPermitId(PkformId);
                        String AddedDt = jorder.getString("AddedDt");
                        permit.setAddedDt(AddedDt);
                        String WarehouseDescription = jorder.getString("WarehouseDescription");
                        permit.setLocation_operation(WarehouseDescription);
                        String Operation = jorder.getString("Operation");
                        permit.setNature_Operation(Operation);


                        permitArrayList.add(permit);
                    }else{

                    }
                }

                permitAdapter.updateList(permitArrayList);
              //  permitAdapter = new PermitAdapter(getActivity(), permitArrayList);
                if (permitAdapter.getCount()!=0){
                //    list_permit.setAdapter(permitAdapter);
                  //  permitAdapter.notifyDataSetChanged();

                }else {
                    txt_no_record.setVisibility(View.VISIBLE);

                }





            }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class DownloadCSEPermitStatus extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_CSEStatus +"?AuthorizeBy="+UserID;

            try {
                res = CommonClass.OpenConnection(url,getActivity());
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);


                    // permitArrayList.clear();


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
            try{
            if (response.contains("[]")) {
                progress.setVisibility(View.GONE);
                txt_no_record.setVisibility(View.VISIBLE);
            } else {

                JSONArray jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    Permit permit = new Permit();
                    JSONObject jorder = jResults.getJSONObject(i);

                    String CSEPermitstatus=jorder.getString("ConfinedSpaceEntryPermitStatus");
                    String CSEPermitId = jorder.getString("ConfinedSpaceEntryPermitID");
                    permit.setWorkAuthorizationstatus(CSEPermitstatus);
                    if (CSEPermitstatus.equals("C")) {

                        String WorkAuthorizationMasterId = jorder.getString("ConfinedSpaceEntryPermitID");
                        permit.setWorkAuthorizationMasterId(WorkAuthorizationMasterId);
                        String PermitNo = jorder.getString("PermitNo");
                        permit.setPermitNo(PermitNo);
                        String PkformId = jorder.getString("formid");
                        permit.setPermitId(PkformId);
                        String AddedDt = jorder.getString("AddedDt");
                        permit.setAddedDt(AddedDt);
                        String WarehouseDescription = jorder.getString("WarehouseDescription");
                        permit.setLocation_operation(WarehouseDescription);
                        String Operation = jorder.getString("Operation");
                        permit.setNature_Operation(Operation);


                        permitArrayList.add(permit);
                    }else{

                    }
                }
                permitAdapter.updateList(permitArrayList);
                //permitAdapter = new PermitAdapter(getActivity(), permitArrayList);
                if (permitAdapter.getCount()!=0){
                  //  list_permit.setAdapter(permitAdapter);
                    //permitAdapter.notifyDataSetChanged();

                }else {
                    txt_no_record.setVisibility(View.VISIBLE);

                }




            }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class downloadWAHPermitstatus extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_WAHStatus +"?AuthorizeBy="+UserID;

            try {
                res = CommonClass.OpenConnection(url,getActivity());
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);


                    // permitArrayList.clear();


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
            try {


            if (response.contains("[]")) {
                progress.setVisibility(View.GONE);
                txt_no_record.setVisibility(View.VISIBLE);
            } else {

                JSONArray jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    Permit permit = new Permit();
                    JSONObject jorder = jResults.getJSONObject(i);

                    String WAHPermitstatus=jorder.getString("permitStatus");
                    String CADPermitId = jorder.getString("WorkAtHeightPermitId");
                    permit.setWorkAuthorizationstatus(WAHPermitstatus);
                    if (WAHPermitstatus.equals("C")) {

                        String WorkAuthorizationMasterId = jorder.getString("WorkAtHeightPermitId");
                        permit.setWorkAuthorizationMasterId(WorkAuthorizationMasterId);
                        String PermitNo = jorder.getString("PermitNo");
                        permit.setPermitNo(PermitNo);
                        String PkformId = jorder.getString("formid");
                        permit.setPermitId(PkformId);
                        String AddedDt = jorder.getString("AddedDt");
                        permit.setAddedDt(AddedDt);
                        String WarehouseDescription = jorder.getString("WarehouseDescription");
                        permit.setLocation_operation(WarehouseDescription);
                        String Operation = jorder.getString("Operation");
                        permit.setNature_Operation(Operation);


                        permitArrayList.add(permit);
                    }else{

                    }
                }

                permitAdapter.updateList(permitArrayList);
         //       permitAdapter = new PermitAdapter(getActivity(), permitArrayList);
                if (permitAdapter.getCount()!=0){
           //         list_permit.setAdapter(permitAdapter);
             //       permitAdapter.notifyDataSetChanged();

                }else {
                    txt_no_record.setVisibility(View.VISIBLE);

                }




            }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class downloadLPPermitstatus extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_LPStatus +"?AuthorizeBy="+UserID;

            try {
                res = CommonClass.OpenConnection(url,getActivity());
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);


                    // permitArrayList.clear();


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
            try{
            if (response.contains("[]")) {
                progress.setVisibility(View.GONE);
                txt_no_record.setVisibility(View.VISIBLE);
            } else {

                JSONArray jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    Permit permit = new Permit();
                    JSONObject jorder = jResults.getJSONObject(i);

                    String LPPermitstatus=jorder.getString("LiftingPermitStatus");
                    String CADPermitId = jorder.getString("LiftingPermitID");
                    permit.setWorkAuthorizationstatus(LPPermitstatus);
                    if (LPPermitstatus.equals("C")) {

                        String WorkAuthorizationMasterId = jorder.getString("LiftingPermitID");
                        permit.setWorkAuthorizationMasterId(WorkAuthorizationMasterId);
                        String PermitNo = jorder.getString("PermitNo");
                        permit.setPermitNo(PermitNo);
                        String PkformId = jorder.getString("formid");
                        permit.setPermitId(PkformId);
                        String AddedDt = jorder.getString("AddedDt");
                        permit.setAddedDt(AddedDt);
                        String WarehouseDescription = jorder.getString("WarehouseDescription");
                        permit.setLocation_operation(WarehouseDescription);
                        String Operation = jorder.getString("Operation");
                        permit.setNature_Operation(Operation);


                        permitArrayList.add(permit);
                    }else{

                    }
                }

                permitAdapter.updateList(permitArrayList);
               // permitAdapter = new PermitAdapter(getActivity(), permitArrayList);
                if (permitAdapter.getCount()!=0){
                 //   list_permit.setAdapter(permitAdapter);
                   // permitAdapter.notifyDataSetChanged();

                }else {
                    txt_no_record.setVisibility(View.VISIBLE);

                }





            }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class downloadEPPermitstatus extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_EPStatus +"?AuthorizeBy="+UserID;

            try {
                res = CommonClass.OpenConnection(url,getActivity());
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);


                    // permitArrayList.clear();


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
            try {

            if (response.contains("[]")) {
                progress.setVisibility(View.GONE);
                txt_no_record.setVisibility(View.VISIBLE);
            } else {

                JSONArray jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    Permit permit = new Permit();
                    JSONObject jorder = jResults.getJSONObject(i);

                    String EPPermitstatus=jorder.getString("ExcavationPermitStatus");
                    String EPPermitId = jorder.getString("ExcavationPermitId");
                    permit.setWorkAuthorizationstatus(EPPermitstatus);
                    if (EPPermitstatus.equals("C")) {

                        String WorkAuthorizationMasterId = jorder.getString("ExcavationPermitId");
                        permit.setWorkAuthorizationMasterId(WorkAuthorizationMasterId);
                        String PermitNo = jorder.getString("PermitNo");
                        permit.setPermitNo(PermitNo);
                        String PkformId = jorder.getString("formid");
                        permit.setPermitId(PkformId);
                        String AddedDt = jorder.getString("AddedDt");
                        permit.setAddedDt(AddedDt);
                        String WarehouseDescription = jorder.getString("WarehouseDescription");
                        permit.setLocation_operation(WarehouseDescription);
                        String Operation = jorder.getString("Operation");
                        permit.setNature_Operation(Operation);


                        permitArrayList.add(permit);
                    }else{

                    }
                }


                permitAdapter.updateList(permitArrayList);
                //permitAdapter = new PermitAdapter(getActivity(), permitArrayList);
                if (permitAdapter.getCount()!=0){
                    //list_permit.setAdapter(permitAdapter);
                    //permitAdapter.notifyDataSetChanged();

                }else {
                    txt_no_record.setVisibility(View.VISIBLE);

                }





            }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}