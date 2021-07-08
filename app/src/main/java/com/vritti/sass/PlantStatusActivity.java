package com.vritti.sass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.adapter.ExpandableAdapter;
import com.vritti.sass.model.Category;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.ItemDetail;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.WebAPIUrl;

import org.json.JSONArray;
import org.json.JSONObject;


public class PlantStatusActivity extends AppCompatActivity {

    private List<Category> catList;
    String CompanyURL;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    SharedPreferences userpreferences;
    ExpandableAdapter expandableadapter;
    ExpandableListView exList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_status_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        toolbar.setTitle(" Plant Status");
        setSupportActionBar(toolbar);

        userpreferences = getSharedPreferences(LoginActivity.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", "");

//        initData();

        exList = (ExpandableListView) findViewById(R.id.expandableListView1);


        if (CommonClass.checkNet(PlantStatusActivity.this)) {
            // showProgress();
            new StartSession(PlantStatusActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadJsondata().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    CommonClass.displayToast(PlantStatusActivity.this, msg);
                    //dismissProgress();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }



    }


    public class DownloadJsondata extends AsyncTask<Void, Void, String> {

        Object res;
        String response="";
        String sop ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            List<Category> childdata = new ArrayList<Category>();
            String tagname = "";

            String url = CompanyURL + WebAPIUrl.api_TagList;
            try {
                res = CommonClass.OpenConnection(url, PlantStatusActivity.this);
                response = res.toString().replaceAll("\\\\", "");

                if (res != null) {
                    sop = "valid";

                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jsonObject = jResults.getJSONObject(i);
                        Category category = new Category();

                        // Adding Header data
                        tagname = jsonObject.getString("TagName");
                        /*category.setName(jsonObject.getString("TagName"));*/
                        listDataHeader.add(tagname);

                        // Adding child data for lease offer
                        List<String> childList = new ArrayList<>();

                        // category.setDescr(jsonObject.getString("TagDescription"));
                        String tempStr = jsonObject.getString("TagDescription");
                        String tempStr1 = jsonObject.getString("EngLowValue");
                        String tempStr2 = jsonObject.getString("EngHighValue");
                        String TagValue = jsonObject.getString("TagValue");
                        if (tempStr.equalsIgnoreCase("")){

                        }else {
                            childList.add("TagDescription : " + tempStr);
                        }
                        if (tempStr1.equalsIgnoreCase("")){

                        }else {
                            childList.add("EngLowValue : " + tempStr1);
                        }
                        if (tempStr2.equalsIgnoreCase("")){
                        }else {
                            childList.add("EngHighValue : " + tempStr2);
                        }
                        if (TagValue.equalsIgnoreCase("")){
                        }else {
                            childList.add("Process value(PV) : " + TagValue);
                        }
                        listDataChild.put(listDataHeader.get(i),childList);




                    }


                    // Header into Child data



                } else {
                    sop = "invalid";
                    Toast.makeText(getApplicationContext(),
                            "Please Check internet Connection", Toast.LENGTH_SHORT)
                            .show();

                }
            }catch(Exception e){
                e.printStackTrace();
                sop = "invalid";
            }

            return sop;
        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);

            if (sop == "valid") {
                expandableadapter = new ExpandableAdapter(PlantStatusActivity.this,listDataHeader, listDataChild);
                exList.setAdapter(expandableadapter);

            }else{
            }
        }
    }

/*
    private void initData() {
        catList = new ArrayList<Category>();

        //new DownloadTaglist().execute();

        Category cat1 = createCategory("Area");
        cat1.setItemList(createItems("201_ROV_03L_OP_1", "201_ROV_03L OPEN FB"));

        Category cat2 = createCategory("Area");
        cat2.setItemList(createItems("201_ROV_03L_CL_1", "201_ROV_03L CLOSE FB"));

        Category cat3 = createCategory("Area");
        cat3.setItemList(createItems("201_ROV_04V_OP_1", "201_ROV_04V OPEN FB"));

        Category cat4 = createCategory("Area");
        cat4.setItemList(createItems("201_ROV_04V_CL_1", "201_ROV_04V CLOSE FB"));

        Category cat5 = createCategory("Area");
        cat5.setItemList(createItems("201_ROV_05V_OP_1", "201_ROV_05V OPEN FB"));

        Category cat6 = createCategory("Area");
        cat6.setItemList(createItems("201_ROV_05V_CL_1", "201_ROV_05V CLOSE FB"));

        Category cat7 = createCategory("Area");
        cat7.setItemList(createItems("201_ROV_05V_CL_1", "201_ROV_05V CLOSE FB"));

        Category cat8 = createCategory("Area");
        cat8.setItemList(createItems("201_ROV_05V_CL_1", "201_ROV_05V CLOSE FB"));

        Category cat9 = createCategory("Area");
        cat9.setItemList(createItems("201_ROV_05V_CL_1", "201_ROV_05V CLOSE FB"));

        Category cat_1 = createCategory("Area");
        cat_1.setItemList(createItems("201_ROV_05V_CL_1", "201_ROV_05V CLOSE FB"));

        Category cat_2 = createCategory("Area");
        cat_2.setItemList(createItems("201_ROV_05V_CL_1", "201_ROV_05V CLOSE FB"));

        Category cat_3 = createCategory("Area");
        cat_3.setItemList(createItems("201_ROV_05V_CL_1", "201_ROV_05V CLOSE FB"));

        Category cat_4 = createCategory("Area");
        cat_4.setItemList(createItems("201_ROV_05V_CL_1", "201_ROV_05V CLOSE FB"));

        Category cat_5 = createCategory("Area");
        cat_5.setItemList(createItems("201_ROV_05V_CL_1", "201_ROV_05V CLOSE FB"));

        Category cat_6 = createCategory("Area");
        cat_6.setItemList(createItems("201_ROV_05V_CL_1", "201_ROV_05V CLOSE FB"));

        catList.add(cat1);
        catList.add(cat2);
        catList.add(cat3);
        catList.add(cat4);
        catList.add(cat5);

        catList.add(cat6);
        catList.add(cat6);
        catList.add(cat7);
        catList.add(cat8);
        catList.add(cat9);
        catList.add(cat_1);
        catList.add(cat_2);
        catList.add(cat_3);
        catList.add(cat_4);
        catList.add(cat_5);
        catList.add(cat_6);


    }
*/

    private Category createCategory(String name) {
        return new Category(name);
    }


    private List<ItemDetail> createItems(String name, String descr) {

        List<ItemDetail> result = new ArrayList<ItemDetail>();


        ItemDetail item = new ItemDetail(name,descr);
        result.add(item);

        return result;
    }


}




