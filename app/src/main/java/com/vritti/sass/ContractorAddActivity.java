package com.vritti.sass;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.adapter.AuthorizedPersonAdapter;
import com.vritti.sass.adapter.DepotAdapter;
import com.vritti.sass.adapter.LocalAuthorizePersonAdapter;
import com.vritti.sass.adapter.OperationAdapter;
import com.vritti.sass.model.AuthorizePersonLocalBean;
import com.vritti.sass.model.AuthorizedPerson;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.Contractor;
import com.vritti.sass.model.Depot;
import com.vritti.sass.model.Operation;
import com.vritti.sass.model.OperationGrpList;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.WebAPIUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ContractorAddActivity extends AppCompatActivity {

    EditText edt_con_name, edt_prev_no, edt_insu_policy, edt_worker_insured/*,edt_auth_name*/, edt_name, edt_email, edt_mobile;
    TextView txt_preven_date, txt_insurance_date;
    Spinner spinner_insurance;
    LinearLayout len_prevention;
    ImageView imd_pre_date, img_insurance_date;
    Button btn_save, btn_auth_person, btn_add_auth;
    EditText txt_authName;
    TextView txt_authName_display;
    StringBuilder stringBuilder;
    StringBuilder stringBuilder1;

    SharedPreferences userpreferences;
    String CompanyURL;
    ProgressBar mprogress;
    SharedPreferences sharedPrefs;
    Type type;
    private String response;
    private String finaljson, InsuranceFinalJson;
    ProgressDialog progressDialog;
    String CustVendorMasterId, mode = "A";
    private ArrayList<Depot> depotArrayList = new ArrayList<>();
    public ArrayList<Operation> operationArrayList = new ArrayList<>();
    public ArrayList<AuthorizedPerson> authorizedPersonArrayList;
    Gson gson;
    String json;
    DepotAdapter depotAdapter;
    Spinner spinner_station;
    TextView spinner_operation;
    OperationAdapter operationAdapter;
    AuthorizedPersonAdapter authorizedPersonAdapter;
    protected CharSequence[] authName;
    public ArrayList<CharSequence> selectedauth;
    public ArrayList<CharSequence> selectedOp;
    public ArrayList<CharSequence> operationCharNames;
    public ArrayList<String> operationCharID;
    public ArrayList<String> selectedauthId;
    public ArrayList<String> selectedOpId;
    String authorizeNames = "", authorizeId = "", authNames = "", authorizeEmail = "", authorizeMobile = "", authMobile = "", authEmail = "";
    String stationId = "", stationName = "", operationId = "", operationName = "", typeOfinsurance = "";
    boolean[] checkedColours;
    boolean[] checkedColours1;
    ArrayList<AuthorizePersonLocalBean> authorizePersonLocalBeanArrayList;
    RecyclerView list_authorizeperson;
    LinearLayout ln_main_list_auth;
    LocalAuthorizePersonAdapter localAuthorizePersonAdapter;
    private String[] user;
    private String[] user1;
    private String[] user2;
    private String[] user3;
    private String[] user4;
    private String[] user5;
    int editOnce = -1;
    Button btn_add;
    LinearLayout ln_person;
    String accessRight="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contractor_lay);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        toolbar.setTitle("Add Contractor");
        setSupportActionBar(toolbar);

        userpreferences = getSharedPreferences(LoginActivity.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", "");


        edt_con_name = findViewById(R.id.edt_con_name);
        edt_prev_no = findViewById(R.id.edt_prev_no);
        edt_insu_policy = findViewById(R.id.edt_insu_policy);
        edt_worker_insured = findViewById(R.id.edt_worker_insured);
        btn_auth_person = findViewById(R.id.btn_auth_person);
        btn_add_auth = findViewById(R.id.btn_add_auth);
        ln_person = findViewById(R.id.ln_person);
        txt_authName = findViewById(R.id.txt_authName);
        txt_authName_display = findViewById(R.id.txt_authName_display);
        ln_main_list_auth = findViewById(R.id.ln_main_list_auth);
        //   edt_auth_name=findViewById(R.id.edt_auth_name);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        txt_preven_date = findViewById(R.id.txt_preven_date);
        txt_insurance_date = findViewById(R.id.txt_insurance_date);
        spinner_insurance = findViewById(R.id.spinner_insurance);
        len_prevention = findViewById(R.id.len_prevention);
        imd_pre_date = findViewById(R.id.imd_pre_date);
        img_insurance_date = findViewById(R.id.img_insurance_date);
        spinner_station = findViewById(R.id.spinner_station);
        btn_save = findViewById(R.id.save);
        spinner_operation = findViewById(R.id.spinner_operation);
        list_authorizeperson = findViewById(R.id.list_authorizeperson);
        btn_add = findViewById(R.id.btn_add);

        selectedauth = new ArrayList<CharSequence>();
        selectedOp = new ArrayList<CharSequence>();
        selectedOpId = new ArrayList<>();
        operationCharNames = new ArrayList<CharSequence>();
        operationCharID = new ArrayList<>();
        selectedauthId = new ArrayList<>();
        depotArrayList = new ArrayList<>();
        operationArrayList = new ArrayList<>();
        authorizePersonLocalBeanArrayList = new ArrayList<>();
        // list_KendraName = new ArrayList<KendraName>();

        long date1 = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = sdf.format(date1);
        txt_insurance_date.setText(dateString);
        txt_preven_date.setText(dateString);


        if (getIntent().hasExtra("preven")) {

            edt_prev_no.setText(getIntent().getStringExtra("preven"));
            edt_con_name.setText(getIntent().getStringExtra("name"));
            CustVendorMasterId = getIntent().getStringExtra("id");
            mode = getIntent().getStringExtra("Mode");
            accessRight = getIntent().getStringExtra("AccessRight");

            if(accessRight.equals("false")){
                edt_con_name.setKeyListener(null);
                edt_prev_no.setKeyListener(null);
                txt_preven_date.setEnabled(false);
                spinner_insurance.setEnabled(false);
                edt_insu_policy.setKeyListener(null);
                txt_insurance_date.setEnabled(false);
                edt_worker_insured.setKeyListener(null);
                btn_add.setEnabled(false);
                btn_add_auth.setEnabled(false);
                spinner_station.setEnabled(false);
                spinner_operation.setEnabled(false);
                btn_save.setEnabled(false);
            }

            if (CommonClass.checkNet(ContractorAddActivity.this)) {
                new StartSession(ContractorAddActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCustomerData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(getApplicationContext(), msg);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }


     /*   // Depot Station
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ContractorAddActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Depot", "");
        type = new TypeToken<List<Depot>>() {
        }.getType();
        depotArrayList = gson.fromJson(json, type);

        if (depotArrayList == null) {*/
        if (CommonClass.checkNet(ContractorAddActivity.this)) {
            //showProgress();
            new StartSession(ContractorAddActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadDepotData().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    CommonClass.displayToast(ContractorAddActivity.this, msg);
                    //dismissProgress();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

     /*   } else {
            if (depotArrayList.size() > 0) {
                depotAdapter = new DepotAdapter(ContractorAddActivity.this, depotArrayList);
                spinner_station.setAdapter(depotAdapter);


            }

        }*/

        //Authorized Person
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ContractorAddActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(ContractorAddActivity.this)) {
                //showProgress();
                new StartSession(ContractorAddActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadAuthorizedPersonData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(getApplicationContext(), msg);
                        //  dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (authorizedPersonArrayList.size() > 0) {
                //authorizedPersonAdapter = new AuthorizedPersonAdapter(ContractorAddActivity.this, authorizedPersonArrayList);
                // spinner_authorize.setAdapter(authorizedPersonAdapter);
                // spinner_permit_closed.setAdapter(authorizedPersonAdapter);


            }

        }

        // Operation

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ContractorAddActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Operation", "");
        type = new TypeToken<List<Operation>>() {
        }.getType();
        operationArrayList = gson.fromJson(json, type);

        if (operationArrayList == null) {
            if (CommonClass.checkNet(ContractorAddActivity.this)) {
                // showProgress();
                new StartSession(ContractorAddActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadNatureOperationData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(getApplicationContext(), msg);
                        //  dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (operationArrayList.size() > 0) {
                operationAdapter = new OperationAdapter(ContractorAddActivity.this, operationArrayList);
                // spinner_operation.setAdapter(operationAdapter);

                if (mode != null && mode.equals("E")) {
                    if (operationId != "") {

                        int pos = -1;
                        if (operationArrayList.size() != 0) {
                            for (int i = 0; i < operationArrayList.size(); i++) {
                                if (operationArrayList.get(i).getOperationMasterId().equals(operationId)) {
                                    pos = i;
                                    break;
                                }
                            }
                            if (pos != -1) {
                                // spinner_operation.setSelection(pos);
                            }
                        }
                    }
                }
            }

        }
        spinner_station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                stationId = depotArrayList.get(position).getDepotid();
                stationName = depotArrayList.get(position).getDepotname();

                if (stationId.equals("Select")) {


                } else {
                    // ln_station.setBackgroundResource(R.drawable.edit_text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*spinner_operation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                operationId = operationArrayList.get(position).getOperationMasterId();
                operationName = operationArrayList.get(position).getOperation();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        spinner_operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showSelectColoursDialog();

            }
        });

        spinner_insurance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeOfinsurance = spinner_insurance.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        txt_preven_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ContractorAddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                String date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                                txt_preven_date.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        txt_insurance_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ContractorAddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                String date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                                txt_insurance_date.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialog.show();
            }
        });
        imd_pre_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ContractorAddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                String date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                                txt_preven_date.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialog.show();
            }
        });
        img_insurance_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ContractorAddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                String date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                                txt_insurance_date.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getobject();

            }
        });


        btn_auth_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(ContractorAddActivity.this, AuthorizePersonDetailsActivity.class);
                startActivity(intent);*/

                txt_authName.setVisibility(View.VISIBLE);

                //showSelectColoursDialog();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ln_person.setVisibility(View.VISIBLE);
                edt_email.setText("");
                edt_mobile.setText("");
                edt_name.setText("");

            }
        });

        btn_add_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  authName = toCharSequenceArray(authorizedPersonArrayList);

                //   int count = authName.length;
////////////////////// commented on 11 may 2021
               /* if (selectedauth.size() < 5) {

                    selectedauth.add(txt_authName.getText().toString());
                    txt_authName.setText("");
                    authorizeNames = String.valueOf(selectedauth).replaceAll("\\[", "").replaceAll("\\]", "");

                    txt_authName_display.setText(authorizeNames);
                } else {
                    Toast.makeText(ContractorAddActivity.this, "You can select atmost 5 authorize person", Toast.LENGTH_SHORT).show();
                }*/
                // selectedauthId.add(authorizedPersonArrayList.get(position).getAuthorizeid());

                if (authorizePersonLocalBeanArrayList.size() < 5) {
                    ln_main_list_auth.setVisibility(View.VISIBLE);
                    String name = "", email = "", mobile = "";
                    name = edt_name.getText().toString();
                    email = edt_email.getText().toString();
                    mobile = edt_mobile.getText().toString();

                    AuthorizePersonLocalBean authorizePersonLocalBean = new AuthorizePersonLocalBean();
                    authorizePersonLocalBean.setName(name);
                    authorizePersonLocalBean.setEmail(email);
                    authorizePersonLocalBean.setMobileNo(mobile);
                    authorizePersonLocalBeanArrayList.add(authorizePersonLocalBean);

                    String[] user4;
                    // user4[i] = edt_name.getText().toString();
                    // authorizeNames = TextUtils.join(",", Collections.singleton(edt_name.getText().toString()));

                    localAuthorizePersonAdapter = new LocalAuthorizePersonAdapter(ContractorAddActivity.this, authorizePersonLocalBeanArrayList,accessRight);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    list_authorizeperson.setLayoutManager(mLayoutManager);
                    list_authorizeperson.setAdapter(localAuthorizePersonAdapter);
                    edt_name.setText("");
                    edt_mobile.setText("");
                    edt_email.setText("");
                    ln_person.setVisibility(View.GONE);

                } else {
                    Toast.makeText(ContractorAddActivity.this, "You can select atmost 5 authorize person", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getobject() {


        if (authorizePersonLocalBeanArrayList.size() != 0) {
            user1 = new String[authorizePersonLocalBeanArrayList.size()];
            user2 = new String[authorizePersonLocalBeanArrayList.size()];
            user3 = new String[authorizePersonLocalBeanArrayList.size()];
            for (int i = 0; i < authorizePersonLocalBeanArrayList.size(); i++) {
                authorizeNames = authorizePersonLocalBeanArrayList.get(i).getName();
                if(authorizePersonLocalBeanArrayList.get(i).getEmail() != null){
                authorizeEmail = authorizePersonLocalBeanArrayList.get(i).getEmail();}else{authorizeEmail = "";}
                if(authorizePersonLocalBeanArrayList.get(i).getMobileNo() != null){
                authorizeMobile = authorizePersonLocalBeanArrayList.get(i).getMobileNo();}else{ authorizeMobile = "";}

                user1[i] = authorizeNames.toString();
                authNames = TextUtils.join(",", user1);
                user2[i] = authorizeEmail.toString();
                authEmail = TextUtils.join(",", user2);
                user3[i] = authorizeMobile.toString();
                authMobile = TextUtils.join(",", user3);
            }
        }

        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray = new JSONArray();

        try {

            String uniqueID = UUID.randomUUID().toString();

            jsonObject.put("InfluentialLevel", "");
            jsonObject.put("CustVendorMasterId", uniqueID);
            jsonObject.put("VendorMasterId", "");
            jsonObject.put("Longitude", "");
            jsonObject.put("vendorCode", "");
            jsonObject.put("Latitude", "");
            jsonObject.put("LendCountry1", "");
            jsonObject.put("LendSeg1", "");
            jsonObject.put("InsCountry1", "");
            jsonObject.put("InsSeg1", "");
            jsonObject.put("CommRate", "");
            jsonObject.put("EntityRestDate", "");
            jsonObject.put("CRMCode", "");
            jsonObject.put("CustVendor", "V");
            jsonObject.put("CustVendorCode", edt_prev_no.getText().toString());
            jsonObject.put("CustVendorName", edt_con_name.getText().toString());
            jsonObject.put("ShortName", edt_con_name.getText().toString());
            jsonObject.put("CustVendorType", "");
            jsonObject.put("ContactName", edt_name.getText().toString());
            jsonObject.put("ContactTitle", "");
            jsonObject.put("Address", "");
            jsonObject.put("PaymentTerms", "");
            jsonObject.put("DeliveryTerms", "");
            jsonObject.put("City", "");
            jsonObject.put("CurrencyMasterID", "");
            jsonObject.put("Country", "");
            jsonObject.put("Pin", "");
            jsonObject.put("Phone", "");
            jsonObject.put("Fax", "");
            jsonObject.put("Mobile", edt_mobile.getText().toString());
            jsonObject.put("Email", edt_email.getText().toString());
            jsonObject.put("CAT", "");
            jsonObject.put("CreditLimit", 0.0);
            jsonObject.put("CreditDays", 0.0);
            jsonObject.put("CreditTerms", "");
            jsonObject.put("CommPer", 0.0);
            jsonObject.put("CommFrom", "");
            jsonObject.put("CommTo", "");
            jsonObject.put("Currency", "1");
            jsonObject.put("BankName", "");
            jsonObject.put("BankAddress", "");
            jsonObject.put("CSTNo", "");
            jsonObject.put("MSTNo", "");
            jsonObject.put("ECCNo", "");
            jsonObject.put("ExDivi", "");
            jsonObject.put("ExRange", "");
            jsonObject.put("CountryId", "");
            jsonObject.put("Active", "true");
            jsonObject.put("InspectionBody", "");
            jsonObject.put("RemittanceInstruction", "");
            jsonObject.put("State", "");
            jsonObject.put("District", "");
            jsonObject.put("Taluka", "");
            jsonObject.put("Website", "");
            jsonObject.put("AadharNo", "");
            jsonObject.put("EsicNo", "");
            jsonObject.put("PFNo", "");
            jsonObject.put("PANNO", "");
            jsonObject.put("ServiceTaxNo", "");
            jsonObject.put("EnterpriseType", "");
            jsonObject.put("CreationLevel", "");
            jsonObject.put("UserLevel", "");
            jsonObject.put("IsDeleted", "");
            jsonObject.put("AddedBy", "");
            jsonObject.put("AddedDt", "");
            jsonObject.put("ModifiedBy", "");
            jsonObject.put("ModifiedDt", "");
            jsonObject.put("AccountId", "");
            jsonObject.put("PayeeName", "");
            jsonObject.put("OMS", "");
            jsonObject.put("EntityGroupMasterId", "");
            jsonObject.put("Typeofservices", "");
            jsonObject.put("IsForeign", "");
            jsonObject.put("EntityClass", "");
            jsonObject.put("SystemUserId", "");
            jsonObject.put("ExtnlSysRef1", "");
            jsonObject.put("ExtnlSysRef2", "");
            jsonObject.put("ExtnlSysRef3", "");
            jsonObject.put("IFSCode", "");
            jsonObject.put("Branch", "");
            jsonObject.put("AccountType", "");
            jsonObject.put("AccountNo", "");
            jsonObject.put("RegistrationFormNo", "");
            jsonObject.put("IsSalesEngr", "");
            jsonObject.put("TaxClass", "");
            jsonObject.put("ServClId", "");
            jsonObject.put("SlCatId", "");
            jsonObject.put("ENPndPO", "");
            jsonObject.put("ENGRN", "");
            jsonObject.put("ENPymt", "");
            jsonObject.put("ENInv", "Y");
            jsonObject.put("ModifiedBy", "");
            jsonObject.put("ENRect", "Y");
            jsonObject.put("TransitDays", "");
            jsonObject.put("TANNO", "");
            jsonObject.put("PriceListId", "");
            jsonObject.put("IsApproved", "");
            jsonObject.put("IsContractReqd", "");
            jsonObject.put("EvaluationDt", "");
            jsonObject.put("ResellerName", "");
            jsonObject.put("ValidFrom", "");

            jsonObject.put("ValidTo", "");
            jsonObject.put("IsActive", "");
            jsonObject.put("IsWLForCRMRef", "Y");
            jsonObject.put("SalesFamily", "");
            jsonObject.put("CallId", "");
            jsonObject.put("CIN", "");
            jsonObject.put("TenorYear", "");
            jsonObject.put("IndIndemnity", "N");
            jsonObject.put("GroupId", "");
           /* jsonObject.put("StationId", stationId);
            jsonObject.put("OperationId", operationId);
            jsonObject.put("AuthorizeId", authorizeId);*/

            JSONArray vLenderDetails = new JSONArray();
            JSONArray vInsuranceDetails = new JSONArray();
            JSONArray ClientDetails = new JSONArray();
            JSONArray ShipToDetails = new JSONArray();
            JSONArray LenderDetails = new JSONArray();
            JSONArray InsuranceDetails = new JSONArray();
            JSONArray BankPayeeName = new JSONArray();
            JSONArray ExpertiseDetails = new JSONArray();
            try {
                jsonObject.put("vLenderDetails", vLenderDetails);
                jsonObject.put("vInsuranceDetails", vInsuranceDetails);
                jsonObject.put("ClientDetails", ClientDetails);
                jsonObject.put("ShipToDetails", ShipToDetails);
                jsonObject.put("LenderDetails", LenderDetails);
                jsonObject.put("InsuranceDetails", InsuranceDetails);
                jsonObject.put("BankPayeeName", BankPayeeName);
                jsonObject.put("ExpertiseDetails", ExpertiseDetails);
            } catch (JSONException e) {

            }
           /* try {

                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("prev_validity", txt_preven_date.getText().toString());
                jsonObject1.put("type_insurance", "ESIC");
                jsonObject1.put("insurance_no", edt_insu_policy.getText().toString());
                jsonObject1.put("insurance_validity", txt_insurance_date.getText().toString());
                jsonObject1.put("no_worker", edt_worker_insured.getText().toString());
                jsonObject1.put("Auth_person", authorizeId);
                jsonObject1.put("StationId", stationId);
                jsonObject1.put("OperationId", operationId);

                JSONArray insu_array = new JSONArray();
                try {
                    JSONObject a = new JSONObject(jsonObject1.toString());
                    insu_array.put(a);
                    jsonObject.put("SASSInsurance", insu_array);
                } catch (JSONException e) {

                }

            } catch (JSONException e) {

            }*/


            finaljson = jsonObject.toString();


            JSONObject InsuranceJsonObject = new JSONObject();
            String s = txt_authName_display.getText().toString();
            InsuranceJsonObject.put("prev_validity", txt_preven_date.getText().toString());
            if (mode != null && mode.equals("E")) {
                InsuranceJsonObject.put("CustVendorMasterId", CustVendorMasterId);
            } else {
                InsuranceJsonObject.put("CustVendorMasterId", uniqueID);
            }
            InsuranceJsonObject.put("type_insurance", typeOfinsurance);
            InsuranceJsonObject.put("insurance_no", edt_insu_policy.getText().toString());
            InsuranceJsonObject.put("insurance_validity", txt_insurance_date.getText().toString());
            InsuranceJsonObject.put("no_worker", edt_worker_insured.getText().toString());
            InsuranceJsonObject.put("Auth_person", authNames);
            InsuranceJsonObject.put("Auth_mobile", authMobile);
            InsuranceJsonObject.put("Auth_email", authEmail);
            InsuranceJsonObject.put("StationId", stationId);
            InsuranceJsonObject.put("OperationId", operationId);
            InsuranceFinalJson = InsuranceJsonObject.toString();


            //  finaljson = finaljson.replaceAll("\\\\", "");


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mode != null && mode.equals("E")) {
            if (CommonClass.checkNet(ContractorAddActivity.this)) {
                new StartSession(ContractorAddActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new POSTUpdateEntity().execute(finaljson);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(getApplicationContext(), msg);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        } else {
            if (CommonClass.checkNet(ContractorAddActivity.this)) {
                new StartSession(ContractorAddActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadPostData().execute(finaljson);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(getApplicationContext(), msg);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }


    }

    public class DownloadPostData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ContractorAddActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.show();
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

        @Override
        protected String doInBackground(String... voids) {
            String objFinalObj = voids[0];

            Object res;
            http:
//z207.ekatm.com/api/CommonPurchaseAPI/GetSupplier
            try {
                String url = CompanyURL + WebAPIUrl.api_EntityPost;
                res = CommonClass.OpenPostConnection(url, objFinalObj, ContractorAddActivity.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = WebAPIUrl.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String val) {
            super.onPostExecute(val);
            if (response != null) {

                if (!val.equals("")) {


                    if (CommonClass.checkNet(ContractorAddActivity.this)) {
                        new StartSession(ContractorAddActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadPostInsuranceData().execute(InsuranceFinalJson);
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                CommonClass.displayToast(getApplicationContext(), msg);
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                }
            }

            /*if (val.contains("Success")) {//Success
                Toast.makeText(WorkAuthorizationActivity.this, "Data save successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(WorkAuthorizationActivity.this, WorkAuthorizationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {
                progressDialog.dismiss();
                Toast.makeText(WorkAuthorizationActivity.this, "Unable to save data", Toast.LENGTH_LONG).show();
            } else {
                progressDialog.dismiss();
                Toast.makeText(WorkAuthorizationActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
            }*/
        }
    }

    private class POSTUpdateEntity extends AsyncTask<String, Void, String> {

        String response = "";
        Object res;
        String finalJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            finalJson = params[0];
            String url = CompanyURL + WebAPIUrl.api_POSTUpdateEntity;

            try {
                res = CommonClass.OpenPostConnection(url, finalJson, ContractorAddActivity.this);

                if (res != null) {

                    String rplc = "\\\r\\\n";
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                } else {
                    response = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (!response.equals("error")) {
                Toast.makeText(ContractorAddActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                if (CommonClass.checkNet(ContractorAddActivity.this)) {
                    new StartSession(ContractorAddActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadPostInsuranceData().execute(InsuranceFinalJson);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            CommonClass.displayToast(getApplicationContext(), msg);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(ContractorAddActivity.this, "Failed to Update", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class DownloadPostInsuranceData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         /*   progressDialog = new ProgressDialog(ContractorAddActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.show();
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
*/
        }

        @Override
        protected String doInBackground(String... voids) {
            String InsuobjFinalObj = voids[0];

            Object res;


            try {
                String url = CompanyURL + WebAPIUrl.api_PostInsurance;
                res = CommonClass.OpenPostConnection(url, InsuobjFinalObj, getApplicationContext());
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = WebAPIUrl.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String val) {
            super.onPostExecute(val);
            //   progressDialog.dismiss();
            startActivity(new Intent(ContractorAddActivity.this, ContractorListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();

            /*if (val.contains("Success")) {//Success
                Toast.makeText(WorkAuthorizationActivity.this, "Data save successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(WorkAuthorizationActivity.this, WorkAuthorizationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {
                progressDialog.dismiss();
                Toast.makeText(WorkAuthorizationActivity.this, "Unable to save data", Toast.LENGTH_LONG).show();
            } else {
                progressDialog.dismiss();
                Toast.makeText(WorkAuthorizationActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
            }*/
        }
    }


    public class DownloadCustomerData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ContractorAddActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.show();
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

        @Override
        protected String doInBackground(String... voids) {


            Object res;


            try {
                String url = CompanyURL + WebAPIUrl.apiGetInsuranceData + "?CustVendorId=" + CustVendorMasterId;
                res = CommonClass.OpenConnection(url, getApplicationContext());
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                }

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

            JSONArray jResults = null;
            try {
                jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {

                    JSONObject jorder = jResults.getJSONObject(0);

                    edt_worker_insured.setText(jorder.getString("No_Worker"));
                    txt_preven_date.setText(jorder.getString("EffToDt"));
                    txt_insurance_date.setText(jorder.getString("IssueDt"));
                    edt_insu_policy.setText(jorder.getString("PolicyNo"));
                    txt_authName_display.setText(jorder.getString("authorizedpersonid"));
                    typeOfinsurance = jorder.getString("DocumentType");
                    if (typeOfinsurance.equals("Type of Insurance")) {
                        spinner_insurance.setSelection(0);
                    } else if (typeOfinsurance.equals("ESIC")) {
                        spinner_insurance.setSelection(1);
                    } else if (typeOfinsurance.equals("WIC")) {
                        spinner_insurance.setSelection(2);
                    }
                    txt_preven_date.setText(jorder.getString("EffFromDt"));
                    txt_insurance_date.setText(jorder.getString("EffToDt"));


                    authNames = jorder.getString("authorizedpersonid");
                    authEmail = jorder.getString("Auth_email");
                    authMobile = jorder.getString("Auth_mobile");
                    String[] split = authNames.split(",");
                    String[] split1 = authEmail.split(",");
                    String[] split2 = authMobile.split(",");
                    authorizePersonLocalBeanArrayList.clear();
                    if (split.length != 0 && split1.length != 0 && split2.length != 0) {
                        for (int j = 0; j < split.length; j++) {
                            AuthorizePersonLocalBean authorizePersonLocalBean = new AuthorizePersonLocalBean();
                            authorizePersonLocalBean.setName(split[j]);
                            if((split1.length) > j) {
                                authorizePersonLocalBean.setEmail(split1[j]);
                            }
                            if((split2.length) > j) {
                                authorizePersonLocalBean.setMobileNo(split2[j]);
                            }
                            authorizePersonLocalBeanArrayList.add(authorizePersonLocalBean);
                        }
                        if (authorizePersonLocalBeanArrayList.size() != 0) {
                            ln_person.setVisibility(View.GONE);
                            localAuthorizePersonAdapter = new LocalAuthorizePersonAdapter(ContractorAddActivity.this, authorizePersonLocalBeanArrayList,accessRight);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            list_authorizeperson.setLayoutManager(mLayoutManager);
                            list_authorizeperson.setAdapter(localAuthorizePersonAdapter);
                        }
                    }

                    if (split.length != 0) {
                        for (int j = 0; j < split.length; j++) {
                            selectedauth.add(j, split[j]);
                        }
                    }

                    edt_mobile.setText(jorder.getString("Mobile"));
                    edt_email.setText(jorder.getString("Email"));

                    stationId = jorder.getString("stationid");
                    operationId = jorder.getString("operationid");
                    operationId = operationId.replaceAll(" ", "");
                    String[] opereation = operationId.split(",");
                    if (opereation.length != 0) {
                        int p =0;
                        operationCharNames.clear();
                        if (operationArrayList.size() != 0) {
                            for (int j = 0; j < operationArrayList.size(); j++) {
                                for (int k = 0; k < opereation.length; k++) {
                                    if (operationArrayList.get(j).getOperationMasterId().equals(opereation[k])) {
                                        operationCharNames.add(p, operationArrayList.get(j).getOperation());
                                        operationCharID.add(p, operationArrayList.get(j).getOperationMasterId());
                                        p++;
                                    }
                                }
                            }
                        }
                        operationName = String.valueOf(operationCharNames);
                        operationName = operationName.replaceAll("\\[", "");
                        operationName = operationName.replaceAll("\\]", "");


                        spinner_operation.setText((CharSequence) operationName);
                    }
                    // edt_auth_name.setText(jorder.getString("AddedBy"));


                    if (mode != null && mode.equals("E")) {
                        if (operationId != "") {

                            int pos = -1;
                            if (operationArrayList.size() != 0) {
                                for (int j = 0; j < operationArrayList.size(); j++) {
                                    if (operationArrayList.get(j).getOperationMasterId().equals(operationId)) {
                                        pos = j;
                                        break;
                                    }
                                }
                                if (pos != -1) {
                                    // spinner_operation.setSelection(pos);
                                }
                            }
                        }
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class DownloadDepotData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        // ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showProgress();
            //  progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_GetStationList;

            try {
                res = CommonClass.OpenConnection(url, ContractorAddActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    depotArrayList = new ArrayList<>();
                    depotArrayList.clear();
                    Depot depot = new Depot();
                    depot.setDepotid("Select");
                    depot.setDepotname("Select");
                    depotArrayList.add(0, depot);
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        depot = new Depot();
                        JSONObject jorder = jResults.getJSONObject(i);

                        depot.setDepotid(jorder.getString("WareHouseMasterId"));
                        depot.setDepotname(jorder.getString("WarehouseDescription"));
                        depotArrayList.add(depot);
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
            //  progressBar.setVisibility(View.GONE);
            // progressDialog.dismiss();
            // dismissProgress();
            if (response.contains("[]")) {
                // dismissProgress();
                //Toast.makeText(WorkAuthorizationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ContractorAddActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(depotArrayList);
                editor.putString("Depot", json);
                editor.commit();
                depotAdapter = new DepotAdapter(ContractorAddActivity.this, depotArrayList);
                spinner_station.setAdapter(depotAdapter);

                if (mode != null && mode.equals("E")) {
                    if (stationId != "") {

                        int pos = -1;
                        if (depotArrayList.size() != 0) {
                            for (int i = 0; i < depotArrayList.size(); i++) {
                                if (depotArrayList.get(i).getDepotid().equals(stationId)) {
                                    pos = i;
                                    break;
                                }
                            }
                            if (pos != -1) {
                                spinner_station.setSelection(pos);
                            }
                        }
                    }
                }


            }


        }
    }


    class DownloadNatureOperationData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        // ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showProgress();
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_GetOperationList;

            try {
                res = CommonClass.OpenConnection(url, ContractorAddActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    operationArrayList = new ArrayList<>();
                    operationArrayList.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        Operation operation = new Operation();
                        JSONObject jorder = jResults.getJSONObject(i);
                        operation.setOperationMasterId(jorder.getString("OperationMasterId"));
                        operation.setOperation(jorder.getString("Operation"));
                        operationArrayList.add(operation);


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
            //progressBar.setVisibility(View.GONE);
            // progressDialog.dismiss();
            //dismissProgress();
            if (response.contains("[]")) {
                //  dismissProgress();
                //Toast.makeText(WorkAuthorizationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ContractorAddActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(operationArrayList);
                editor.putString("Operation", json);
                editor.commit();
                operationAdapter = new OperationAdapter(ContractorAddActivity.this, operationArrayList);
                // spinner_operation.setAdapter(operationAdapter);


            }


        }
    }

    class DownloadAuthorizedPersonData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        // ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showProgress();
            // progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_GetApproverPerson;

            try {
                res = CommonClass.OpenConnection(url, ContractorAddActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    authorizedPersonArrayList = new ArrayList<>();
                    authorizedPersonArrayList.clear();
                    AuthorizedPerson authorizedPerson = new AuthorizedPerson();
                    authorizedPerson.setAuthorizeid("Select");
                    authorizedPerson.setAuthorizename("Select");
                    authorizedPersonArrayList.add(0, authorizedPerson);

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
            // progressBar.setVisibility(View.GONE);
            // progressDialog.dismiss();
            //dismissProgress();
            if (response.contains("[]")) {
                //  dismissProgress();
                //  Toast.makeText(WorkAuthorizationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ContractorAddActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(authorizedPersonArrayList);
                editor.putString("Authorized", json);
                editor.commit();


               /* authorizedPersonAdapter = new AuthorizedPersonAdapter(ContractorAddActivity.this, authorizedPersonArrayList);
                edt_auth_name.setAdapter(authorizedPersonAdapter);*/


                //spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                //permit_closed.setAdapter(authorizedPersonAdapter);


            }


        }
    }

    protected void showSelectColoursDialog() {

        authName = toCharSequenceArray(operationArrayList);
//checkedColours =new  boolean[authName.length];
        //   checkedColours= new boolean[authName.length];

        // selectedauth = operationCharNames;
        String[] op = operationName.split(", ");
        selectedOp = operationCharNames;
        selectedOpId = operationCharID;


        //  checkedColours[position] = false;
        //   ((AlertDialog) dialog).getListView().setItemChecked(position, false);
        int count = authName.length;

        if (mode.equalsIgnoreCase("E") && editOnce == -1) {
            checkedColours = new boolean[authName.length];
            for (int i = 0; i < authName.length; i++) {
                for (int j = 0; j < op.length; j++) {
                    if (authName[i].equals(op[j])) {
                        checkedColours[i] = true;
                        // ((AlertDialog) dialog).getListView().setItemChecked(true, false);
                    }
                }
            }

            editOnce = 1;
        }
        checkedColours1 = checkedColours;
        checkedColours = new boolean[count];
        if (checkedColours1 != null) {
            checkedColours = checkedColours1;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int position, boolean isChecked) {


              /*  if (selectedauthId.size() >= 5) {
                    Toast.makeText(ContractorAddActivity.this, "You can select atmost 5 authorize person", Toast.LENGTH_SHORT).show();
                    selectedauth.remove(authName[position]);
                    selectedauthId.remove(authorizedPersonArrayList.get(position).getAuthorizeid());
                    *//*((AlertDialog) dialog).getListView().setItemChecked(position,false);*//*
                }else {*/
                if (isChecked) {
                   /* if (selectedauthId.size() >= 5) {
                        isChecked = false;
                        //  dialog.cancel();

                        checkedColours[position] = false;
                        ((AlertDialog) dialog).getListView().setItemChecked(position, false);
                        Toast.makeText(ContractorAddActivity.this, "You can select atmost 5 authorize person", Toast.LENGTH_SHORT).show();
                        // alertDialogList.setItemChecked(which, false);
                    } else {*/

                    selectedOp.add(authName[position]);
                    selectedOpId.add(operationArrayList.get(position).getOperationMasterId());
                    onChangeSelectedColours();
                    //  }

                } else {

                    selectedOp.remove(authName[position]);
                    selectedOpId.remove(operationArrayList.get(position).getOperationMasterId());
                    onChangeSelectedColours();
                }


            }
            //  }

        };


        builder.setTitle("Select nature of operation");
        // builder.setIcon(R.drawable.correct);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               /* ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(String.valueOf(selectedKendra));*/

                operationName = "";
                operationName = String.valueOf(selectedOp).replaceAll("\\[", "").replaceAll("\\]", "");

                txt_authName.setText(operationName);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               /* ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(String.valueOf(selectedKendra));*/

                /*              if (selectedauthId.size() < 5) {*/
                operationName = "";
                selectedOp.clear();
                // check_school.setChecked(false);
                txt_authName.setVisibility(View.GONE);
                // kendraNames = String.valueOf(selectedKendra).replaceAll("\\[","").replaceAll("\\]","");

                txt_authName.setText(operationName);
              /*  }else{
                    Toast.makeText(ContractorAddActivity.this, "You can select atmost 5 authorize person", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        builder.setMultiChoiceItems(authName, checkedColours, coloursDialogListener);

        AlertDialog dialog = builder.create();
        dialog.show();


    }


    public CharSequence[] toCharSequenceArray(List<Operation> authorizedPeople) {

        CharSequence[] bookTitles = new CharSequence[authorizedPeople.size()];
        for (int i = 0; i < bookTitles.length; i++) {
            bookTitles[i] = authorizedPeople.get(i).getOperation();
        }
        return bookTitles;
    }

    public void onChangeSelectedColours() {

        stringBuilder = new StringBuilder();
        stringBuilder1 = new StringBuilder();

        for (CharSequence colour : selectedOp) {
            stringBuilder.append(colour + " , ");
        }
        for (CharSequence colour : selectedOpId) {
            stringBuilder1.append(colour + " , ");
        }
        operationName = String.valueOf(selectedOp).replaceAll("\\[", "").replaceAll("\\]", "");
        operationId = String.valueOf(selectedOpId).replaceAll("\\[", "").replaceAll("\\]", "");

        spinner_operation.setText(operationName);


    }


}
