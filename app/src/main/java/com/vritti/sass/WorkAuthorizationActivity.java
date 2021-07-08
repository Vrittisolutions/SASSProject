package com.vritti.sass;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.adapter.AuthorizedPersonAdapter;
import com.vritti.sass.adapter.CompletePermitDetailsAdapter;
import com.vritti.sass.adapter.DepotAdapter;
import com.vritti.sass.adapter.EquipAdapter;
import com.vritti.sass.adapter.GoldenRuleAdapter;
import com.vritti.sass.adapter.InRiskAdapter;
import com.vritti.sass.adapter.InstallationListAdapter;
import com.vritti.sass.adapter.LocationOperationAdapter;
import com.vritti.sass.adapter.OperationAdapter;
import com.vritti.sass.adapter.PermitContractorListAdapter;
import com.vritti.sass.adapter.PrevenionWAdapter;
import com.vritti.sass.adapter.WorkOperationAdapter;
import com.vritti.sass.model.AuthorizedPerson;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.ContractorList;
import com.vritti.sass.model.DateFormatChange;
import com.vritti.sass.model.Depot;
import com.vritti.sass.model.EquipmentUse;
import com.vritti.sass.model.GoldenRules;
import com.vritti.sass.model.IndicateRisk;
import com.vritti.sass.model.InstallationPreparation;
import com.vritti.sass.model.Location;
import com.vritti.sass.model.Operation;
import com.vritti.sass.model.OperationGrpList;
import com.vritti.sass.model.Permit;
import com.vritti.sass.model.Prevention;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.UpdateTime;
import com.vritti.sass.model.Utility;
import com.vritti.sass.model.WebAPIUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


/*
 * Created by sharvari on 14-Nov-18.
 */

public class WorkAuthorizationActivity extends AppCompatActivity {

    private static String IMAGE_DIRECTORY_NAME = "SASS";
    SimpleDateFormat dateFormatdate;
    Permit permit;
    //RadioGroup radio_operation,radiogroup_method;
    ImageView img_camera, img_camera1;
    LinearLayout len_indicate;

    RadioGroup radiogroup_method;
    RadioButton radio_yes, radio_no;

    //RadioButton radio_none,radio_hotwork,radio_excavation,radio_workatheight,radio_liftingpermit,radio_cleaningpermit,radio_confined;

    EditText edit_permitno, edit_Prevention_plan, edit_remarks;
    Spinner spinner_operation, spinner_authorize, spinner_authorize1, spinner_permit_closed, spinner_spotcheck, spinner_station, spinner_contractor,
            spinner_location;
    LinearLayout ln_spinner_authorize, ln_spinner_reason;
    String data = "", Password = "";
    Button btn_submit;
    LinearLayout ln_station, ln_contractor, ln_natureOperation, ln_locationOperation;
    int check = 0;
    Button edt_authorize_date, edt_authorize_date1;
    Button edt_fromdate, edt_todate;
    Button edt_from_time, edt_andto_time, edt_from_to_time, edt_andfrom_time, edt_permit_date, edt_spot_date, edt_cancel_date;
    int Year, month, day;
    DatePickerDialog datePickerDialog;
    private WorkAuthorizationActivity parent;
    public static String time = null, disptime;
    public static String time1 = null;
    String date;
    static String todaysDate;
    int MY_CAMERA_PERMISSION_CODE = 100;
    int MEDIA_TYPE_IMAGE = 1;
    int CAMERA_REQUEST = 101;
    private Uri fileUri;
    File mediaFile;
    private CommonClass cc;
    String tempVal = "", ReasonVal = "";

    SharedPreferences userpreferences;
    private ArrayList<Depot> depotArrayList;
    private ArrayList<AuthorizedPerson> authorizedPersonArrayList;
    String categoryDesc = "";
    private ArrayList<AuthorizedPerson> txt_authorizeArrayList;
    private ArrayList<GoldenRules> goldenRulesArrayList;
    GoldenRuleAdapter goldenRuleAdapter;
    private ArrayList<ContractorList> contractorListActivityArrayList;
    private ArrayList<Operation> operationArrayList;
    private ArrayList<Location> LocationArraylist;
    DepotAdapter depotAdapter;
    AuthorizedPersonAdapter authorizedPersonAdapter;
    PermitContractorListAdapter permitContractorListAdapter;
    OperationAdapter operationAdapter;
    LocationOperationAdapter locationOperationAdapter;
    String CompanyURL;
    SharedPreferences sharedPrefs;
    ProgressBar mprogress;
    Gson gson;
    String json;
    JSONObject Activityjson;
    Type type;
    String authorize = "", ContractorName = "", PermitClosed = "", SpotCheck = "", StationId = "", StationName = "", PermitNo = "", PKFormId = "",
            formcode = "", OperationId = "", LocationId = "", LocationName = "", Authorize1Name = "", AuthorizeDate2 = "", AuthorizeDate1 = "",
            method_of_operation = "N", listindicaterisk, listequipmentrisk, listprevention, contractorId = "", PreventionPlan_Code = "", userLoginId = "";
    String prevention_plan = "", remarks = "", CustPreventionPlan_Code = "", OperationName = "", contractorName = "", listinstallation;
    String StartDate = "", EndDate = "", fromtime = "", totime = "", andfromtime = "", andtotime = "", permitcloseddate = "", spotcheckdate = "", cancelDate = "", goldenRulesList = "";
    private String serverResponseMessage, path, Imagefilename;
    ScrollView scrollView;
    String indicateRisk_Others = "", equipment_Others = "", prevention_Others = "", installation_Others = "";

    String Authorize1Id = "", PermitClosedId = "", SpotCheckId = "", cancelId = "";


    ArrayList<Prevention> preventionArrayList;
    ArrayList<EquipmentUse> equipmentUseArrayList;
    ArrayList<IndicateRisk> indicateRiskArrayList;
    ArrayList<InstallationPreparation> installationPreparationArrayList;
    boolean isAns;
    ArrayList<String> radiocheck;
    ListView list_prevention, list_equipment, list_indicaterisk;
    ListView list_installation;

    PrevenionWAdapter prevenionAdapter;
    EquipAdapter equipmentAdapter;
    InRiskAdapter indicateRiskAdapter;
    InstallationListAdapter installationListAdapter;
    String OperationCode = "", indicateriskCode = "";
    String response;
    String IndicateId, operationCodeId = "";
    private String[] user;
    private String[] user1;
    private String[] user2;
    private String[] user3;
    private String[] user4;
    private String[] user5;
    SimpleDraweeView img_display;

    TextView txt_authorize, txt_permit_closed, txt_spotcheck, txt_cancel;

    private long mLastClickTime = 0;
    GridView grid_operation;
    ArrayList<OperationGrpList> operationlist;
    WorkOperationAdapter adapter;

    RelativeLayout cusdialog1, cusdialog2;
    Button txt_submit_pass, txt_cancel_pass;
    EditText edit_password;

    Button txt_submit_reason, txt_cancel_reason;
    EditText edit_password_reason, edit_reason;
    String Mode = "";
    int img_pos;
    String temp = "", Electricimg = "", Isolatedimg = "", SpotCheckimgpost;
    String isolatedAbsolutePath = "", electricalAbsolutePath = "", spotAbsolutePath = "";
    String Electricimgpost = "", Isolatedimgpost = "", Spotimgpost = "";
    //Button btn_pdf;
    RelativeLayout ln_WAH;
    Bitmap bitmap;
    LinearLayout btLayour;
    Date nextSevenDay = null;
    String sevenDay = "", PermitStatus = "";
    TextView tx_p_closed, txt_cancel_permit;
    LinearLayout len_p_closed, len_cancel_permit;
    RecyclerView list_goldenRules;
    RelativeLayout ln_completePermitDetails;
    RecyclerView list_permitDetails;
    CompletePermitDetailsAdapter completePermitDetailsAdapter;
    Button btn_cancel;
    ProgressBar progressBar;

    int fromTimeSelectedHour;
    int fromToTimeHr = 9, fromToTimeMin = 00;
    int andfromTime;
    int remainingTime = 4, remainingTimeMin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_authorizaation_permit);

        parent = WorkAuthorizationActivity.this;
        CommonClass.hideKeyboard(parent);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }


        // ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        toolbar.setTitle(getResources().getString(R.string.application_name));
        setSupportActionBar(toolbar);



        initview();
        setListner();
        clickdatelistner();

        Intent intent = getIntent();

        dateFormatdate = new SimpleDateFormat("dd/MM/yyyy");


        userpreferences = getSharedPreferences(LoginActivity.USERINFO, Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", "");

        depotArrayList = new ArrayList<>();
        contractorListActivityArrayList = new ArrayList<>();
        authorizedPersonArrayList = new ArrayList<>();
        operationArrayList = new ArrayList<>();
        LocationArraylist = new ArrayList<>();
        preventionArrayList = new ArrayList<>();
        equipmentUseArrayList = new ArrayList<>();
        indicateRiskArrayList = new ArrayList<>();
        installationPreparationArrayList = new ArrayList<>();
        txt_authorizeArrayList = new ArrayList<>();

        operationlist = new ArrayList<OperationGrpList>();
        CommonClass.hideKeyboard(WorkAuthorizationActivity.this);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Intent intent = getIntent();

        final Calendar c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = (c.get(Calendar.DAY_OF_MONTH));
        date = day + "-"
                + String.format("%02d", (month + 1))
                + "-" + Year;
        date = DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy",day + "-"
                        + String.format("%02d", (month + 1))
                        + "-" + Year);
        c.add(Calendar.DAY_OF_YEAR, 6);
        nextSevenDay = c.getTime();
        SimpleDateFormat formattedDate
                = new SimpleDateFormat("dd-MM-yyyy");
        sevenDay = formattedDate.format(c.getTime());

        edt_authorize_date1.setText(date);
        edt_authorize_date.setText(date);
        edt_fromdate.setText(date);
        edt_todate.setText(sevenDay);
        edt_permit_date.setText(date);
        edt_cancel_date.setText(date);
        edt_spot_date.setText(date);


        operationlist.add(new OperationGrpList("None", "None"));
        operationlist.add(new OperationGrpList("HWPF", "Hot work"));
        operationlist.add(new OperationGrpList("EP", "Excavation"));
        operationlist.add(new OperationGrpList("WAH", "Work at height"));
        operationlist.add(new OperationGrpList("LP", "Lifting permit"));
        operationlist.add(new OperationGrpList("CSE", "Confined space entry"));
        operationlist.add(new OperationGrpList("CAD", "Cleaning and degassing"));

        adapter = new WorkOperationAdapter(WorkAuthorizationActivity.this, operationlist, Mode, PermitStatus);
        grid_operation.setAdapter(adapter);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            } else {

            }
        } else {
        }


        //Installation list

        if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
            showProgress();
            new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadInstallationData().execute(PKFormId);
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


// Depot Station
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WorkAuthorizationActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Depot", "");
        type = new TypeToken<List<Depot>>() {
        }.getType();
        depotArrayList = gson.fromJson(json, type);

        if (depotArrayList == null) {
            if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                showProgress();
                new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadDepotData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(WorkAuthorizationActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (depotArrayList.size() > 0) {
                depotAdapter = new DepotAdapter(WorkAuthorizationActivity.this, depotArrayList);
                spinner_station.setAdapter(depotAdapter);


            }

        }


//ContractorList
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WorkAuthorizationActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Contractor", "");
        type = new TypeToken<List<ContractorList>>() {
        }.getType();
        contractorListActivityArrayList = gson.fromJson(json, type);

        if (contractorListActivityArrayList == null) {
            if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                showProgress();
                new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
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
            if (contractorListActivityArrayList.size() > 0) {
                permitContractorListAdapter = new PermitContractorListAdapter(WorkAuthorizationActivity.this, contractorListActivityArrayList);
                spinner_authorize1.setAdapter(permitContractorListAdapter);
                spinner_contractor.setAdapter(permitContractorListAdapter);
            }

        }


        // Operation

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WorkAuthorizationActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Operation", "");
        type = new TypeToken<List<Operation>>() {
        }.getType();
        operationArrayList = gson.fromJson(json, type);

        if (operationArrayList == null) {
            if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                showProgress();
                new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadNatureOperationData().execute();
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
            if (operationArrayList.size() > 0) {
                operationAdapter = new OperationAdapter(WorkAuthorizationActivity.this, operationArrayList);
                spinner_operation.setAdapter(operationAdapter);
            }

        }

//Indicate Risk

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WorkAuthorizationActivity.this);
        gson = new Gson();
        String indicatejson = sharedPrefs.getString("indicate", "");
        String equipmentjson = sharedPrefs.getString("equipment", "");
        String preventionjson = sharedPrefs.getString("prevention", "");
        Type indicatetype = new TypeToken<List<IndicateRisk>>() {
        }.getType();
        Type equipmenttype = new TypeToken<List<EquipmentUse>>() {
        }.getType();
        Type preventiontype = new TypeToken<List<Prevention>>() {
        }.getType();
        indicateRiskArrayList = gson.fromJson(indicatejson, indicatetype);
        equipmentUseArrayList = gson.fromJson(equipmentjson, equipmenttype);
        preventionArrayList = gson.fromJson(preventionjson, preventiontype);

        if (indicateRiskArrayList == null) {
            if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                showProgress();
                new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadIndicateData().execute();
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
            if (indicateRiskArrayList.size() > 0) {
                indicateRiskAdapter = new InRiskAdapter(WorkAuthorizationActivity.this, indicateRiskArrayList, Mode, PermitStatus);
                list_indicaterisk.setAdapter(indicateRiskAdapter);
                len_indicate.setVisibility(View.VISIBLE);
                //Utility.setListViewHeightBasedOnItems(list_indicaterisk);


            }

        }

        //Equipment
        if (equipmentUseArrayList == null) {
            if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                showProgress();

                new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadIndicateData().execute();
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
            if (equipmentUseArrayList.size() > 0) {
                equipmentAdapter = new EquipAdapter(WorkAuthorizationActivity.this, equipmentUseArrayList);
                list_equipment.setAdapter(equipmentAdapter);
                len_indicate.setVisibility(View.VISIBLE);
                // Utility.setListViewHeightBasedOnItems(list_equipment);

            }

        }
        if (preventionArrayList == null) {
            if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                showProgress();
                new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadIndicateData().execute();
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
            if (preventionArrayList.size() > 0) {
                prevenionAdapter = new PrevenionWAdapter(WorkAuthorizationActivity.this, preventionArrayList, Mode);
                list_prevention.setAdapter(prevenionAdapter);
                len_indicate.setVisibility(View.VISIBLE);
                // Utility.setListViewHeightBasedOnItems(list_prevention);


            }

        }


        //Installation Preparation

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WorkAuthorizationActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("installation", "");
        type = new TypeToken<List<InstallationPreparation>>() {
        }.getType();
        installationPreparationArrayList = gson.fromJson(json, type);

        if (installationPreparationArrayList == null) {
            if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                showProgress();
                new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadInstallationData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(WorkAuthorizationActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (installationPreparationArrayList.size() > 0) {

                installationListAdapter = new InstallationListAdapter(WorkAuthorizationActivity.this, installationPreparationArrayList, Mode, PermitStatus);
                list_installation.setAdapter(installationListAdapter);
                Utility.setListViewHeightBasedOnItems(list_installation);
            }

        }


        //Authorized Person
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WorkAuthorizationActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                showProgress();
                new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadAuthorizedPersonData().execute();
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
           /* if (authorizedPersonArrayList.size() > 0) {
                authorizedPersonAdapter = new AuthorizedPersonAdapter(WorkAuthorizationActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);


            }*/

        }


        // Golden Rules
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WorkAuthorizationActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("GoldenRules", "");
        type = new TypeToken<List<GoldenRules>>() {
        }.getType();
        goldenRulesArrayList = gson.fromJson(json, type);

        if (goldenRulesArrayList == null) {
            if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                showProgress();
                new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadGoldenRules().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(WorkAuthorizationActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (goldenRulesArrayList.size() > 0) {

                goldenRuleAdapter = new GoldenRuleAdapter(WorkAuthorizationActivity.this, goldenRulesArrayList, "WA", Mode, PermitStatus);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WorkAuthorizationActivity.this);

                // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                list_goldenRules.setLayoutManager(linearLayoutManager);
                list_goldenRules.setAdapter(goldenRuleAdapter);
            }
        }

        //time by default set
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        time1 = UpdateTime.updateTime(hour, minute);

        System.out.println("time: " + time1);


        if (hour > 13) {
            edt_from_time.setText("9:00 AM");
            // edt_from_time.setEnabled(false);
        } else {
            edt_from_time.setText("9:00 AM");
            // edt_from_time.setText(time1);
        }
        edt_from_to_time.setText("1:00 PM");


        if (hour < 13) {
            edt_andfrom_time.setText("2:00 PM");

        } else if (hour > 18) {
            edt_andfrom_time.setText("2:00 PM");

        } else {
            edt_andfrom_time.setText("2:00 PM");
        }

        //edt_andfrom_time.setText(time1);
        edt_andto_time.setText("6:00 PM");


        if (getIntent() != null) {
            permit = new Gson().fromJson(getIntent().getStringExtra("permitno"), Permit.class);
            permit = new Gson().fromJson(getIntent().getStringExtra("permitno"), Permit.class);
            if (permit != null) {
                Mode = "E";
                PermitStatus = permit.getWorkAuthorizationstatus();
                if (PermitStatus.equalsIgnoreCase("A")) {
                    spinner_contractor.setEnabled(false);
                    spinner_station.setEnabled(false);
                }
                if (!PermitStatus.equalsIgnoreCase("P")) {
                    spinner_station.setEnabled(false);

                    spinner_contractor.setEnabled(false);
                    spinner_operation.setEnabled(false);
                    spinner_location.setEnabled(false);
                    edt_fromdate.setEnabled(false);
                    edt_todate.setEnabled(false);
                    edt_from_time.setEnabled(false);
                    edt_from_to_time.setEnabled(false);
                    edt_andfrom_time.setEnabled(false);
                    edt_andto_time.setEnabled(false);
                    edit_permitno.setKeyListener(null);
                    edit_Prevention_plan.setKeyListener(null);
                    radio_yes.setClickable(false);
                    radio_no.setClickable(false);
                    txt_authorize.setKeyListener(null);
                    edt_authorize_date.setEnabled(false);
                    edt_authorize_date.setTextColor(Color.parseColor("#000000"));
                    edt_authorize_date1.setEnabled(false);
                    edt_authorize_date1.setTextColor(Color.parseColor("#000000"));
                    edit_remarks.setKeyListener(null);
                    if (indicateRiskArrayList != null) {
                        indicateRiskAdapter = new InRiskAdapter(WorkAuthorizationActivity.this, indicateRiskArrayList, Mode, PermitStatus);
                        list_indicaterisk.setAdapter(indicateRiskAdapter);

                    }

                    if (preventionArrayList != null) {
                        prevenionAdapter = new PrevenionWAdapter(WorkAuthorizationActivity.this, preventionArrayList, Mode, PermitStatus);
                        list_prevention.setAdapter(prevenionAdapter);
                    }
                    if (equipmentUseArrayList != null) {

                        equipmentAdapter = new EquipAdapter(WorkAuthorizationActivity.this, equipmentUseArrayList, Mode, PermitStatus);
                        list_equipment.setAdapter(equipmentAdapter);
                    }
                    if (installationPreparationArrayList != null) {
                        installationListAdapter = new InstallationListAdapter(WorkAuthorizationActivity.this, installationPreparationArrayList, Mode, PermitStatus);
                        list_installation.setAdapter(installationListAdapter);
                    }

                    if (PermitStatus.equalsIgnoreCase("R") || PermitStatus.equalsIgnoreCase("C")) {
                        txt_spotcheck.setEnabled(false);
                        txt_cancel.setEnabled(false);
                        edt_spot_date.setEnabled(false);
                        edt_spot_date.setTextColor(Color.parseColor("#000000"));
                        txt_permit_closed.setEnabled(false);
                        edt_permit_date.setEnabled(false);
                        edt_permit_date.setTextColor(Color.parseColor("#000000"));
                        tx_p_closed.setKeyListener(null);
                        txt_cancel_permit.setEnabled(false);
                        edt_cancel_date.setKeyListener(null);
                        edt_cancel_date.setTextColor(Color.parseColor("#000000"));
                        btn_submit.setClickable(false);


                    }


                    adapter = new WorkOperationAdapter(WorkAuthorizationActivity.this, operationlist, Mode, PermitStatus);
                    grid_operation.setAdapter(adapter);

                }
                else {
                    edt_fromdate.setEnabled(true);
                    edt_todate.setEnabled(true);
                }

               // if (PermitStatus.equals("A") || PermitStatus.equals("P")) {
                    len_p_closed.setVisibility(View.VISIBLE);
                    tx_p_closed.setVisibility(View.VISIBLE);
                    len_cancel_permit.setVisibility(View.VISIBLE);
                    txt_cancel_permit.setVisibility(View.VISIBLE);
               // } else {
                   /* len_p_closed.setVisibility(View.GONE);
                    tx_p_closed.setVisibility(View.GONE);
                    len_cancel_permit.setVisibility(View.GONE);
                    txt_cancel_permit.setVisibility(View.GONE);*/
              //  }


                PermitNo = permit.getPermitNo();
                PKFormId = permit.getPermitId();
             //   edt_fromdate.setEnabled(false);
               // edt_todate.setEnabled(false);
                edt_fromdate.setTextColor(Color.parseColor("#101010"));
                edt_todate.setTextColor(Color.parseColor("#101010"));
                if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                    new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new getDetails().execute(PermitNo);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                } else {
                    Toast.makeText(WorkAuthorizationActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            } else {
                Mode = "A";
                PKFormId = intent.getStringExtra("PKFormId");
                formcode = intent.getStringExtra("formcode");


                //GetPermit NO

                if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                    showProgress();
                    new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetPermitNo().execute();
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
            }
        } else {

        }


    }


    public void initview() {
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        spinner_authorize1 = findViewById(R.id.spinner_authorize1);
        spinner_authorize1.setEnabled(false);
        spinner_operation = findViewById(R.id.spinner_operation);
        // btn_pdf = findViewById(R.id.btn_pdf);
        ln_WAH = findViewById(R.id.ln_WAH);
        progressBar = findViewById(R.id.progressBar);

        //mprogress=(ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        //spinner_authorize1= findViewById(R.id.spinner_authorize1);
        spinner_operation = findViewById(R.id.spinner_operation);
        spinner_permit_closed = findViewById(R.id.spinner_permit_closed1);
        //mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        spinner_location = findViewById(R.id.spinner_location);
        spinner_contractor = findViewById(R.id.spinner_contractor);
        spinner_station = findViewById(R.id.spinner_station);
        grid_operation = findViewById(R.id.grid_operation);
        len_indicate = (LinearLayout) findViewById(R.id.len_indicate);
        list_goldenRules = findViewById(R.id.list_goldenRules);
        list_permitDetails = findViewById(R.id.list_permitDetails);
        // ln_completePermitDetails = findViewById(R.id.ln_completePermitDetails);

        scrollView = findViewById(R.id.scrollView);

        edit_permitno = findViewById(R.id.edit_permitno);
        edit_Prevention_plan = findViewById(R.id.edit_Prevention_plan);
        edit_remarks = findViewById(R.id.edit_remarks);
        btn_submit = findViewById(R.id.btn_submit);
        ln_station = findViewById(R.id.ln_station);
        ln_contractor = findViewById(R.id.ln_contractor);
        ln_natureOperation = findViewById(R.id.ln_natureOperation);
        ln_locationOperation = findViewById(R.id.ln_locationOperation);
        edt_fromdate = findViewById(R.id.edt_fromdate);
        edt_todate = findViewById(R.id.edt_todate);
        edt_from_time = findViewById(R.id.edt_from_time);
        edt_andto_time = findViewById(R.id.edt_andto_time);
        edt_from_to_time = findViewById(R.id.edt_from_to_time);
        edt_andfrom_time = findViewById(R.id.edt_andfrom_time);
        edt_authorize_date = findViewById(R.id.edt_authorize_date);
        edt_authorize_date1 = findViewById(R.id.edt_authorize_date1);
        edt_permit_date = findViewById(R.id.edt_permit_date);
        edt_spot_date = findViewById(R.id.edt_spot_date);
        edt_cancel_date = findViewById(R.id.edt_cancel_date);
        radiogroup_method = (RadioGroup) findViewById(R.id.radiogroup_method);
        img_camera = findViewById(R.id.img_camera);
        img_camera.setVisibility(View.VISIBLE);
        radio_yes = findViewById(R.id.radio_yes);
        radio_no = findViewById(R.id.radio_no);
        // img_camera1 = findViewById(R.id.img_camera1);

        list_indicaterisk = findViewById(R.id.list_indicaterisk);
        list_equipment = findViewById(R.id.list_equipment);
        list_prevention = findViewById(R.id.list_prevention);
        list_installation = findViewById(R.id.list_installation);


        txt_authorize = findViewById(R.id.spinner_authorize);
        txt_permit_closed = findViewById(R.id.txt_permit_closed);
        txt_cancel = findViewById(R.id.txt_cancel);
        txt_spotcheck = findViewById(R.id.txt_spotcheck);
        img_display = findViewById(R.id.img_display);

        spinner_authorize = findViewById(R.id.spinner_authorize_pas);
        ln_spinner_authorize = findViewById(R.id.ln_spinner_authorize);
        ln_spinner_reason = findViewById(R.id.ln_spinner_reason);
        edit_password = findViewById(R.id.edt_password_pass);

        cusdialog1 = findViewById(R.id.cusDailog1);
        cusdialog2 = findViewById(R.id.cusDailog2);
        txt_submit_pass = findViewById(R.id.txt_submit_pass);
        txt_cancel_pass = findViewById(R.id.txt_cancel_pass);
        txt_submit_reason = findViewById(R.id.txt_submit_reason);
        txt_cancel_reason = findViewById(R.id.txt_cancel_reason);
        edit_password_reason = findViewById(R.id.edt_password_reason);
        edit_reason = findViewById(R.id.edt_reason);
        btLayour = findViewById(R.id.btLayour);
        tx_p_closed = findViewById(R.id.tx_p_closed);
        txt_cancel_permit = findViewById(R.id.txt_cancel_permit);
        len_p_closed = findViewById(R.id.len_p_closed);
        len_cancel_permit = findViewById(R.id.len_cancel_permit);
    }


    public void setListner() {


        spinner_authorize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (txt_authorizeArrayList == null) {

                    } else {
                        /*authorize = authorizedPersonArrayList.get(position).getAuthorizeid();

                        String name = authorizedPersonArrayList.get(position).getAuthorizename();
                        txt_authorize.setText(name);*/

                        authorize = txt_authorizeArrayList.get(position).getAuthorizeid();
                        userLoginId = txt_authorizeArrayList.get(position).getUserLoginId();

                        String name = txt_authorizeArrayList.get(position).getAuthorizename();
                        txt_authorize.setText(name);

                        /*if(tempVal.equals("0"))

                        Authorize1Id = authorizedPersonArrayList.se*/

                        //txt_authorize.setText(name);


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//Permit and spot check spinner

        spinner_permit_closed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                    if (txt_authorizeArrayList == null) {

                    } else {
                        PermitClosed = txt_authorizeArrayList.get(position).getAuthorizeid();
                        userLoginId = txt_authorizeArrayList.get(position).getUserLoginId();
                        String Permitname = txt_authorizeArrayList.get(position).getAuthorizename();
                        if (ReasonVal.equals("1")) {
                            txt_permit_closed.setText(Permitname);
                            PermitClosedId = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (ReasonVal.equals("2")) {
                            txt_spotcheck.setText(Permitname);
                            SpotCheckId = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (ReasonVal.equals("3")) {
                            txt_cancel.setText(Permitname);
                            cancelId = txt_authorizeArrayList.get(position).getAuthorizeid();

                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        txt_cancel_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* cusdialog1.setVisibility(View.GONE);
                txt_spotcheck.setText("--Select--");*/
                ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text);
                edit_password.setBackgroundResource(R.drawable.edit_text);

                if (cusdialog1.getVisibility() == View.VISIBLE) {
                    cusdialog1.setVisibility(View.GONE);
                    txt_authorize.setText("Select");
                    spinner_authorize.setSelection(0);
                    edit_password.setText("");
                    hideKeyboard(WorkAuthorizationActivity.this);

                } else {
                    cusdialog1.setVisibility(View.VISIBLE);
                }

            }
        });

        txt_cancel_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ln_spinner_reason.setBackgroundResource(R.drawable.edit_text);
                edit_password_reason.setBackgroundResource(R.drawable.edit_text);
                edit_reason.setBackgroundResource(R.drawable.edit_text);
                if (cusdialog2.getVisibility() == View.VISIBLE) {
                    cusdialog2.setVisibility(View.GONE);
                    if (ReasonVal.equals("1")) {
                        txt_permit_closed.setText("Select");
                        spinner_permit_closed.setSelection(0);
                        edit_password_reason.setText("");
                        edit_reason.setText("");
                        PermitClosedId = "";
                    } else if (ReasonVal.equals("2")) {
                        txt_spotcheck.setText("Select");
                        spinner_permit_closed.setSelection(0);
                        edit_password_reason.setText("");
                        edit_reason.setText("");
                        SpotCheckId = "";
                    } else if (ReasonVal.equals("3")) {
                        txt_cancel.setText("Select");
                        spinner_permit_closed.setSelection(0);
                        edit_password_reason.setText("");
                        edit_reason.setText("");
                        cancelId = "";
                    }

                    if ((PermitClosedId.equalsIgnoreCase("Select") || PermitClosedId.equals(""))
                            && (SpotCheckId.equalsIgnoreCase("Select") || SpotCheckId.equals(""))) {
                        // btn_submit.setVisibility(View.GONE);
                    } else {
                        btn_submit.setVisibility(View.VISIBLE);
                    }
                    hideKeyboard(WorkAuthorizationActivity.this);
                } else {
                    cusdialog2.setVisibility(View.VISIBLE);
                }
            }
        });

        txt_submit_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // String Password = edt_password.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                Password = edit_password.getText().toString();

                ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text);
                edit_password.setBackgroundResource(R.drawable.edit_text);
                if (Password.equalsIgnoreCase("") && (authorize.equalsIgnoreCase("--Select--")
                        || authorize.equalsIgnoreCase(""))) {

                    Toast toast = Toast.makeText(WorkAuthorizationActivity.this, "Please enter authorized person and password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text_red);
                    edit_password.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();

                    // Toast toast = new Toast(getApplicationContext());
                    // Toast toast = Toast. makeText(WorkAuthorizationActivity.this, "Please enter password", Toast.LENGTH_SHORT);
                    // toast.setGravity(Gravity.CENTER, 0, -160);
                    // toast.setDuration(Toast.LENGTH_SHORT);
                    // toast.setView(layout);
                    // toast.show();
                } else if (!Password.equalsIgnoreCase("") && (authorize.equalsIgnoreCase("--Select--")
                        || authorize.equalsIgnoreCase(""))) {
                    Toast toast = Toast.makeText(WorkAuthorizationActivity.this, "Please select authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && (!authorize.equalsIgnoreCase("--Select--")
                        || !authorize.equalsIgnoreCase(""))) {
                    Toast toast = Toast.makeText(WorkAuthorizationActivity.this, "Please enter valid password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else {

                    if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                        new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
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

                }
            }
        });

        txt_submit_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = edit_password_reason.getText().toString();
                String Reason = edit_reason.getText().toString();
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                ln_spinner_reason.setBackgroundResource(R.drawable.edit_text);
                edit_password_reason.setBackgroundResource(R.drawable.edit_text);
                edit_reason.setBackgroundResource(R.drawable.edit_text);

                if (Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {

                    Toast toast = Toast.makeText(WorkAuthorizationActivity.this, "Please enter authorized person,password and reason", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();

                    // Toast toast = new Toast(getApplicationContext());
                    // Toast toast = Toast. makeText(WorkAuthorizationActivity.this, "Please enter password", Toast.LENGTH_SHORT);
                    // toast.setGravity(Gravity.CENTER, 0, -160);
                    // toast.setDuration(Toast.LENGTH_SHORT);
                    // toast.setView(layout);
                    // toast.show();
                }
                else if (!Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(WorkAuthorizationActivity.this, "Please select authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(WorkAuthorizationActivity.this, "Please valid password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (!Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(WorkAuthorizationActivity.this, "Please enter reason", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    //password and reason blank
                    Toast toast = Toast.makeText(WorkAuthorizationActivity.this, "Please valid password and reason", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    //password and permit
                    Toast toast = Toast.makeText(WorkAuthorizationActivity.this, "Please valid password and authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (!Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    //reason and permit
                    Toast toast = Toast.makeText(WorkAuthorizationActivity.this, "Please enter reason and authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                }
                /*else if (Password.equalsIgnoreCase("") && !Reason.equalsIgnoreCase("")) {

                    text.setText("Please Enter Password");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, -140);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                } else if (!Password.equalsIgnoreCase("") && Reason.equalsIgnoreCase("")) {
                    text.setText("Please Enter Reason");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, -140);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }*/
                else {

                    if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                        new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
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

                }

            }
        });


        spinner_station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                StationId = depotArrayList.get(position).getDepotid();
                StationName = depotArrayList.get(position).getDepotname();

                if (StationId.equals("Select")) {


                } else {
                    ln_station.setBackgroundResource(R.drawable.edit_text);
                    if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                        showProgress();
                        //Location Get
                        new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadLocationOperationData().execute(StationId);
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


                    if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                        showProgress();
                        new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadAuthorizedPersonDataDepo().execute(StationId);
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

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temp = "3";

                if (ContextCompat.checkSelfPermission(WorkAuthorizationActivity.this, Manifest.permission.CAMERA)

                        != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_PERMISSION_CODE);
                    }
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });


        spinner_contractor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (contractorListActivityArrayList != null) {

                    if (permit != null) {
                        int contractorpos = -1;
                        contractorId = contractorListActivityArrayList.get(position).getCustVendorMasterId();
                        ContractorName = contractorListActivityArrayList.get(position).getCustVendorName();
                        PreventionPlan_Code = contractorListActivityArrayList.get(position).getCustVendorCode();
                        edit_Prevention_plan.setText(PreventionPlan_Code);
                        for (int i = 0; i < contractorListActivityArrayList.size(); i++) {
                            if (contractorId.equals(contractorListActivityArrayList.get(i).getCustVendorMasterId())) {
                                contractorpos = i;
                                break;
                            }
                        }
                        if (contractorpos != -1) {
                            spinner_authorize1.setSelection(contractorpos);
                        } else {
                            spinner_authorize1.setSelection(0);
                        }
                    } else {
                        contractorId = contractorListActivityArrayList.get(position).getCustVendorMasterId();
                        ContractorName = contractorListActivityArrayList.get(position).getCustVendorName();
                        PreventionPlan_Code = contractorListActivityArrayList.get(position).getCustVendorCode();
                        edit_Prevention_plan.setText(PreventionPlan_Code);

                        int contractorpos1 = -1;

                        for (int i = 0; i < contractorListActivityArrayList.size(); i++) {
                            if (contractorId.equals(contractorListActivityArrayList.get(i).getCustVendorMasterId())) {
                                contractorpos1 = i;
                                break;
                            }
                        }
                        if (contractorpos1 != -1) {
                            spinner_authorize1.setSelection(contractorpos1);
                        } else {
                            spinner_authorize1.setSelection(0);
                        }


                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_operation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OperationId = operationArrayList.get(position).getOperationMasterId();
                OperationName = operationArrayList.get(position).getOperation();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LocationId = LocationArraylist.get(position).getLocationMasterId();
                LocationName = LocationArraylist.get(position).getLocationDesc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        radiogroup_method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Toast.makeText(getBaseContext(), value, Toast.LENGTH_SHORT).show();
                int selectedId = radiogroup_method.getCheckedRadioButtonId();
                RadioButton radiobtn = (RadioButton) findViewById(selectedId);

                String radvalue = radiobtn.getText().toString();
                method_of_operation = radvalue;

                if (radvalue.contains("None") || radvalue.equals("Yes")) {
                    method_of_operation = "Y";
                } else {
                    method_of_operation = "N";
                }
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String authorizepos = txt_authorize.getText().toString();
                String permit_pos = txt_authorize.getText().toString();
                String spotcheckpos = txt_authorize.getText().toString();
                //int authorizepos = spinner_authorize.getSelectedItemPosition();
                int authorize1pos = spinner_authorize1.getSelectedItemPosition();

                /*int permit_pos = permit_closed.getSelectedItemPosition();
                int spotcheckpos = spinner_spotcheck.getSelectedItemPosition();*/


                if (StationId.equalsIgnoreCase("Select") || StationId.equalsIgnoreCase("")) {
                    Toast.makeText(WorkAuthorizationActivity.this, "Please Fill Station Details", Toast.LENGTH_SHORT).show();
                    ln_station.setBackgroundResource(R.drawable.edit_text_red);
                } else if (PreventionPlan_Code.equalsIgnoreCase("") || PreventionPlan_Code.equalsIgnoreCase("Select")) {
                    Toast.makeText(WorkAuthorizationActivity.this, "Please Fill Prevention Plan Details", Toast.LENGTH_SHORT).show();
                    edit_Prevention_plan.setBackgroundResource(R.drawable.edit_text_red);
                } else if (contractorId.equalsIgnoreCase("") || contractorId.equalsIgnoreCase("Select")) {
                    Toast.makeText(WorkAuthorizationActivity.this, "Please Fill Contractor Details", Toast.LENGTH_SHORT).show();
                    ln_contractor.setBackgroundResource(R.drawable.edit_text_red);
                } else if (OperationId.equalsIgnoreCase("") || OperationId.equalsIgnoreCase("Select")) {
                    Toast.makeText(WorkAuthorizationActivity.this, "Please Fill Nature of Operation Details", Toast.LENGTH_SHORT).show();
                    ln_natureOperation.setBackgroundResource(R.drawable.edit_text_red);
                } else if (LocationId.equalsIgnoreCase("") || LocationId.equalsIgnoreCase("Select")) {
                    Toast.makeText(WorkAuthorizationActivity.this, "Please Fill Location of Operation Details", Toast.LENGTH_SHORT).show();
                    ln_locationOperation.setBackgroundResource(R.drawable.edit_text_red);
                } else if (method_of_operation.equalsIgnoreCase("")) {
                    Toast.makeText(WorkAuthorizationActivity.this, "Please Check Method of Operation", Toast.LENGTH_SHORT).show();
                } else {
                    if (Mode.equalsIgnoreCase("A")) {
                        saveactivityjson();
                        // startActivity(new Intent(WorkAuthorizationActivity.this, SupervisorMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else if (Mode.equalsIgnoreCase("E")) {
                        editjson();
                        //startActivity(new Intent(WorkAuthorizationActivity.this, PermitStatusActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                }


            }

        });


        /*btn_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkAuthorizationActivity.this,ViewPDFActivity.class);
                startActivity(intent);

            }
        });
*/
        txt_authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* authorizedPersonAdapter = new AuthorizedPersonAdapter(WorkAuthorizationActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);*/
                //Tier 1


                CategoryWiseAuthorizeName("level 1", "fromPwd");


                cusdialog1.setVisibility(View.VISIBLE);
                tempVal = "0";


            }
        });

        txt_spotcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//Tier 1
                CategoryWiseAuthorizeName("level 1", "fromReason");
                cusdialog2.setVisibility(View.VISIBLE);
                ReasonVal = "2";

            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Tier 1
                CategoryWiseAuthorizeName("level 1", "fromReason");
                cusdialog2.setVisibility(View.VISIBLE);
                ReasonVal = "3";

            }
        });


        txt_permit_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Tier 1
                CategoryWiseAuthorizeName("level 1", "fromReason");
                cusdialog2.setVisibility(View.VISIBLE);
                ReasonVal = "1";

            }
        });

    }

    private void CategoryWiseAuthorizeName(String categorydesc, String From) {
        String arrivalFrom = From;
        String categoryDesc = categorydesc;

        txt_authorizeArrayList = new ArrayList<>();
        if (!(StationId.equals("Select"))) {
            if (authorizedPersonArrayList != null) {
//level 0 -Admin
                //level 1 - Tier 1 + Admin
                //leve 2 = Tier1 + Tier2 + Admin
                if (categoryDesc.equalsIgnoreCase("level 1")) {
                    for (int i = 0; i < authorizedPersonArrayList.size(); i++) {
                        if (authorizedPersonArrayList.get(i).getCategorydesc() == null ||
                                authorizedPersonArrayList.get(i).getCategorydesc().equals("Tier1") ||
                                authorizedPersonArrayList.get(i).getCategorydesc().equals("Admin")) {
                            AuthorizedPerson authorizedPerson = new AuthorizedPerson();
                            authorizedPerson.setAuthorizeid(authorizedPersonArrayList.get(i).getAuthorizeid());
                            authorizedPerson.setAuthorizename(authorizedPersonArrayList.get(i).getAuthorizename());
                            authorizedPerson.setCategorydesc(authorizedPersonArrayList.get(i).getCategorydesc());
                            authorizedPerson.setUserLoginId(authorizedPersonArrayList.get(i).getUserLoginId());
                            txt_authorizeArrayList.add(authorizedPerson);

                        }
                    }
                } else if (categoryDesc.equalsIgnoreCase("level 2")) {

                } else if (categoryDesc.equalsIgnoreCase("level 0")) {

                }
                authorizedPersonAdapter = new AuthorizedPersonAdapter(WorkAuthorizationActivity.this, txt_authorizeArrayList);
                if (arrivalFrom.equalsIgnoreCase("fromReason")) {
                    spinner_permit_closed.setAdapter(authorizedPersonAdapter);
                } else if (arrivalFrom.equalsIgnoreCase("fromPWD")) {
                    spinner_authorize.setAdapter(authorizedPersonAdapter);
                }
            }
        } else {
            AuthorizedPerson authorizedPerson = new AuthorizedPerson();
            authorizedPerson.setAuthorizename("Select");
            txt_authorizeArrayList.add(authorizedPerson);
            authorizedPersonAdapter = new AuthorizedPersonAdapter(WorkAuthorizationActivity.this, txt_authorizeArrayList);
            spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            spinner_authorize.setAdapter(authorizedPersonAdapter);
        }

    }

    public void clickdatelistner() {
        edt_authorize_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date date = new Date();
                Date start;
                Date end;
                final Calendar c = Calendar.getInstance();

                Year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                int week = c.get(Calendar.WEEK_OF_MONTH);
                int test = c.get(Calendar.DAY_OF_WEEK);
                //c.add(Calendar.DATE, test);


                c.add(Calendar.DAY_OF_WEEK, 1);
                c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                long enddate = c.getTime().getTime();
                c.set(Calendar.WEEK_OF_MONTH, Calendar.SUNDAY);
                long end2 = c.getTime().getTime();

                Date date1 = new Date(enddate);

                //String strCurrentDate = "Wed, 18 Apr 2012 07:55:29 +0000";
                /*SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z");*/
                SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                Date newDate = null;
                try {
                    newDate = format.parse(String.valueOf(enddate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = formatter.format(new Date(Long.parseLong(String.valueOf(enddate))));
                String end_dayof_week = dateString;


                // Launch Date Picker Dialog
                datePickerDialog = new DatePickerDialog(WorkAuthorizationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                edt_fromdate.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            edt_authorize_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            edt_authorize_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(WorkAuthorizationActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            edt_authorize_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        edt_authorize_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        edt_authorize_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    edt_authorize_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + " - " + (month + 1)
                                            + " -" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);


                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();
                //show dialog


            }
        });


        //edt_authorize_date1.setText(date);


        edt_authorize_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date date = new Date();
                Date start;
                Date end;
                final Calendar c = Calendar.getInstance();

                Year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                int week = c.get(Calendar.WEEK_OF_MONTH);
                int test = c.get(Calendar.DAY_OF_WEEK);
                //c.add(Calendar.DATE, test);


                c.add(Calendar.DAY_OF_WEEK, 1);
                c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                long enddate = c.getTime().getTime();
                c.set(Calendar.WEEK_OF_MONTH, Calendar.SUNDAY);
                long end2 = c.getTime().getTime();

                Date date1 = new Date(enddate);

                //String strCurrentDate = "Wed, 18 Apr 2012 07:55:29 +0000";
                /*SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z");*/
                SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                Date newDate = null;
                try {
                    newDate = format.parse(String.valueOf(enddate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = formatter.format(new Date(Long.parseLong(String.valueOf(enddate))));
                String end_dayof_week = dateString;

                /*SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date2 = new Date(enddate);
                String end_dayof_week = targetFormat.format(date2);*/

              /*  SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date2 = new Date(end1);*/

                /*String chat_time = targetFormat.format(date);*/


                // Launch Date Picker Dialog
                datePickerDialog = new DatePickerDialog(WorkAuthorizationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                edt_authorize_date1.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            edt_authorize_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            edt_authorize_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(WorkAuthorizationActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            edt_authorize_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        edt_authorize_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        edt_authorize_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    edt_authorize_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + " - " + (month + 1)
                                            + " -" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();


            }
        });

        edt_from_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String startTime = "9:00 AM";
                String a1;

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                int a = mcurrentTime.get(Calendar.AM_PM);
                mcurrentTime.set(Calendar.HOUR_OF_DAY, 9);
                mcurrentTime.set(Calendar.MINUTE, 00);
                if (a == 1) {
                    a1 = "PM";
                } else {
                    a1 = "AM";
                }

                /* int hour1 = hour+4;*/

                final TimePickerDialog mTimePicker;


                edt_from_time.setText(hour + ":" + minute + " " + a1);
                /*  edt_andto_time.setText(hour +4 + ":" + minute + " ");*/

                mTimePicker = new TimePickerDialog(WorkAuthorizationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {

                                fromTimeSelectedHour = selectedHour;
                                if (selectedHour < 13) {
                                    if (selectedHour >= 9) {
                                        time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                        time1 = UpdateTime.updateTime((selectedHour + 4), selectedMinute);
                                        int difference1 = 13 - fromTimeSelectedHour;
                                        remainingTime = 8 - difference1;
                                        remainingTimeMin = 60 - selectedMinute;
                                        remainingTimeMin = 60 - remainingTimeMin;
                                        String time2 = UpdateTime.updateTime((18 + (4 - difference1)), selectedMinute);

                                        edt_from_time.setText(time);
                                        fromToTimeHr = 13;
                                        fromToTimeMin = 00;
                                        edt_from_to_time.setText("01:00 PM");
                                        edt_andfrom_time.setText("02:00 PM");
                                        edt_andto_time.setText(time2);
                                        andfromTime = 14;
                                    } else {
                                        Toast.makeText(WorkAuthorizationActivity.this, "Select time greater than 09:00 AM", Toast.LENGTH_SHORT).show();
                                        edt_from_time.setText("09:00 AM");
                                        edt_from_to_time.setText("01:00 PM");
                                        edt_andfrom_time.setText("02:00 PM");
                                        edt_andto_time.setText("06:00 PM");
                                        andfromTime = 14;
                                    }
                                } else if (selectedHour == 13) {
                                    edt_from_time.setText("09:00 AM");
                                    edt_from_to_time.setText("01:00 PM");
                                    edt_andfrom_time.setText("02:00 PM");
                                    edt_andto_time.setText("06:00 PM");
                                    Toast.makeText(WorkAuthorizationActivity.this, "You cannot select time 01:00 PM and 02:00 PM", Toast.LENGTH_SHORT).show();
                                } else if (selectedHour >= 14) {

                                    time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                    time1 = UpdateTime.updateTime((selectedHour + 4), selectedMinute);

                                    String time2 = UpdateTime.updateTime((selectedHour + 8), selectedMinute);

                                    if ((selectedHour + 8) >= 16 && selectedMinute >= 0) {
                                        fromToTimeHr = selectedHour + 4;
                                        fromToTimeMin = selectedMinute;

                                        if (selectedHour >= 20) {
                                            edt_from_time.setText(time);
                                            edt_from_to_time.setText("11:59 PM");
                                            edt_andfrom_time.setText("11:59 PM");
                                            edt_andto_time.setText("11:59 PM");
                                        } else {

                                            edt_from_time.setText(time);
                                            edt_from_to_time.setText(time1);
                                            edt_andfrom_time.setText(time1);
                                            if ((selectedHour + 8) >= 24) {
                                                edt_andto_time.setText("11:59 PM");
                                            } else {
                                                edt_andto_time.setText(time2);
                                            }

                                        }
                                        andfromTime = selectedHour + 4;

                                    } else {
                                        fromToTimeHr = selectedHour + 4;
                                        fromToTimeMin = selectedMinute;
                                        edt_from_time.setText(time);
                                        edt_from_to_time.setText(time1);
                                        edt_andfrom_time.setText(time1);
                                        edt_andto_time.setText(time2);
                                        andfromTime = selectedHour + 4;
                                    }


                                }
                            }
                        }, hour, minute, false);// Yes 24 hour time

                mTimePicker.setTitle("Select Time");

                mTimePicker.show();
                //show dialog


            }
        });

        edt_andfrom_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a1;
                final String startTime = "2:00 PM";

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                int a = mcurrentTime.get(Calendar.AM_PM);
                //    int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                if (a == 1) {
                    a1 = "PM";
                } else {
                    a1 = "AM";
                }
                // String time1 = UpdateTime.updateTime(hour, minute);

                //  edt_andfrom_time.setText(time1);


                mTimePicker = new TimePickerDialog(WorkAuthorizationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {

                                if (fromToTimeHr < selectedHour) {

                                    if (/*selectedHour <= 18 &&*/ selectedHour >= 14) {
                                        if (selectedHour == 18 && selectedMinute == 0) {
                                            time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                            String time2 = UpdateTime.updateTime((selectedHour + remainingTime), (selectedMinute + remainingTimeMin));
                                            edt_andfrom_time.setText(time);
                                            edt_andto_time.setText(time2);
                                            andfromTime = selectedHour;
                                        } else if (selectedHour >= 14 && selectedMinute >= 0) {


                                            time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                            String time2 = UpdateTime.updateTime((selectedHour + remainingTime), (selectedMinute + remainingTimeMin));
                                            if ((selectedHour + remainingTime) >= 24) {
                                                edt_andto_time.setText("11:59 PM");
                                            } else {
                                                edt_andto_time.setText(time2);
                                            }
                                            edt_andfrom_time.setText(time);

                                            andfromTime = selectedHour;

                                            /*if (selectedHour >= 16 && selectedMinute >= 0) {
                                                time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                                edt_andfrom_time.setText(time);
                                                edt_andto_time.setText("11:59 PM");
                                                andfromTime = selectedHour;
                                            } else {*/

                                            //}


                                        } else {
                                            edt_andfrom_time.setText(startTime);
                                            andfromTime = 14;
                                            Toast.makeText(WorkAuthorizationActivity.this, "You can select time from 2:00 PM to 6:00 PM", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        andfromTime = 14;
                                        edt_andfrom_time.setText(startTime);
                                        Toast.makeText(WorkAuthorizationActivity.this, "You can select time from 2:00 PM to 6:00 PM", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(WorkAuthorizationActivity.this, "Select time greater than to time", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, fromToTimeHr, fromToTimeMin, false);// Yes 24 hour time
                // mTimePicker.setMin(hour + 1, minute);
                mTimePicker.setTitle("Select Time");


                mTimePicker.show();

            }
        });





     /*   edt_from_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String startTime = "9:00 AM";
                String a1;

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                int a = mcurrentTime.get(Calendar.AM_PM);
                mcurrentTime.set(Calendar.HOUR_OF_DAY, 9);
                mcurrentTime.set(Calendar.MINUTE, 00);
                if (a == 1) {
                    a1 = "PM";
                } else {
                    a1 = "AM";
                }

                *//* int hour1 = hour+4;*//*

                final TimePickerDialog mTimePicker;


                edt_from_time.setText(hour + ":" + minute + " " + a1);
                *//*  edt_andto_time.setText(hour +4 + ":" + minute + " ");*//*

                mTimePicker = new TimePickerDialog(WorkAuthorizationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                fromTimeSelectedHour = selectedHour;
                                if (selectedHour > 13) {
                                    if (selectedHour < 14) {
                                        Toast.makeText(WorkAuthorizationActivity.this, "You cannot select time 01:00 PM and 02:00 PM", Toast.LENGTH_SHORT).show();
                                    } else {
                                        time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                        time1 = UpdateTime.updateTime((selectedHour + 4), selectedMinute);

                                        String time2 = UpdateTime.updateTime((selectedHour + 8), selectedMinute);

                                        if ((selectedHour + 8) >= 16 && selectedMinute >= 0) {
                                            fromToTimeHr = selectedHour + 4;
                                            fromToTimeMin = selectedMinute;

                                            edt_from_time.setText(time);
                                            edt_from_to_time.setText(time1);
                                            edt_andfrom_time.setText(time1);
                                            edt_andto_time.setText("11:59 PM");
                                            andfromTime = selectedHour + 4;
                                        } else {
                                            fromToTimeHr = selectedHour + 4;
                                            fromToTimeMin = selectedMinute;
                                            edt_from_time.setText(time);
                                            edt_from_to_time.setText(time1);
                                            edt_andfrom_time.setText(time1);
                                            edt_andto_time.setText(time2);
                                            andfromTime = selectedHour + 4;
                                        }
                                    }


                                }
                                else if (selectedHour <= 13) {
                                    if(selectedHour >= 9) {
                                        time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                        time1 = UpdateTime.updateTime((selectedHour + 4), selectedMinute);
                                        edt_from_time.setText(time);
                                        fromToTimeHr = 14;
                                        fromToTimeMin = 00;
                                        edt_from_to_time.setText("01:00 PM");
                                        edt_andfrom_time.setText("02:00 PM");
                                        edt_andto_time.setText("06:00 PM");
                                        andfromTime = 14;
                                    }else{
                                        Toast.makeText(WorkAuthorizationActivity.this, "Select time greater than 09:00 AM", Toast.LENGTH_SHORT).show();
                                        edt_from_time.setText("09:00 AM");
                                        edt_from_to_time.setText("01:00 PM");
                                        edt_andfrom_time.setText("02:00 PM");
                                        edt_andto_time.setText("06:00 PM");
                                        andfromTime = 14;
                                    }

                                }
                            }
                        }, hour, minute, false);// Yes 24 hour time

                mTimePicker.setTitle("Select Time");

                mTimePicker.show();
                //show dialog


            }
        });

*/
        /*edt_from_to_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String endTime = "1:00 PM";
                String a1;

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                int a = mcurrentTime.get(Calendar.AM_PM);
                if (a == 1) {
                    a1 = "PM";
                } else {
                    a1 = "AM";
                }
                TimePickerDialog mTimePicker;

                edt_from_to_time.setText(hour + ":" + minute + " " + a1);

                mTimePicker = new TimePickerDialog(WorkAuthorizationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {

                                String startTime = edt_from_time.getText().toString();

                                if(fromTimeSelectedHour < selectedHour){
                                    if (selectedHour <= 13 && selectedHour >= 9) {

                                        if (selectedHour == 13 && selectedMinute == 0) {
                                            time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                            edt_from_to_time.setText(time);
                                        } else if (selectedHour == 13 && selectedMinute > 0) {
                                            edt_from_to_time.setText(endTime);
                                            Toast.makeText(WorkAuthorizationActivity.this, "You can select time from 9:00 AM to 1:00 PM", Toast.LENGTH_SHORT).show();
                                        } else {
                                            time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                            edt_from_to_time.setText(time);
                                            //Toast.makeText(WorkAuthorizationActivity.this, "You can select time from 9:00 AM to 1:00 PM", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        edt_from_to_time.setText(endTime);
                                        Toast.makeText(WorkAuthorizationActivity.this, "You can select time from 9:00 AM to 1:00 PM", Toast.LENGTH_SHORT).show();
                                    }
                                }else{

                                }




                               *//* SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = null;
                                try {
                                    date = dateFormat.parse(startTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }*//*
         *//*System.out.println(date);
                                if (selectedHour <= 13 && selectedHour >= 9) {

                                    if (selectedHour == 13 && selectedMinute == 0) {
                                        time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                        edt_from_to_time.setText(time);
                                    } else if (selectedHour == 13 && selectedMinute > 0) {
                                        edt_from_to_time.setText(endTime);
                                        Toast.makeText(WorkAuthorizationActivity.this, "You can select time from 9:00 AM to 1:00 PM", Toast.LENGTH_SHORT).show();
                                    } else {
                                        time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                        edt_from_to_time.setText(time);
                                        //Toast.makeText(WorkAuthorizationActivity.this, "You can select time from 9:00 AM to 1:00 PM", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    edt_from_to_time.setText(endTime);
                                    Toast.makeText(WorkAuthorizationActivity.this, "You can select time from 9:00 AM to 1:00 PM", Toast.LENGTH_SHORT).show();
                                }*//*


                            }
                        }, hour, minute, false);// Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });*/

     /*   edt_andfrom_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a1;
                final String startTime = "2:00 PM";

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                int a = mcurrentTime.get(Calendar.AM_PM);
                //    int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                if (a == 1) {
                    a1 = "PM";
                } else {
                    a1 = "AM";
                }
                // String time1 = UpdateTime.updateTime(hour, minute);

                //  edt_andfrom_time.setText(time1);


                mTimePicker = new TimePickerDialog(WorkAuthorizationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                if (fromToTimeHr < selectedHour) {

                                    if (*//*selectedHour <= 18 &&*//* selectedHour >= 14) {
                                        if (selectedHour == 18 && selectedMinute == 0) {
                                            time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                            edt_andfrom_time.setText(time);
                                            andfromTime = selectedHour;
                                        } else if (selectedHour >= 14 && selectedMinute >= 0) {
                                            if (selectedHour >= 16 && selectedMinute >= 0) {
                                                time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                                edt_andfrom_time.setText(time);
                                                edt_andto_time.setText("11:59 PM");
                                                andfromTime = selectedHour;
                                            } else {
                                                time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                                edt_andfrom_time.setText(time);
                                                edt_andto_time.setText("06:00 PM");
                                                andfromTime = selectedHour;
                                            }


                                        } else {
                                            edt_andfrom_time.setText(startTime);
                                            andfromTime = 14;
                                            Toast.makeText(WorkAuthorizationActivity.this, "You can select time from 2:00 PM to 6:00 PM", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        andfromTime = 14;
                                        edt_andfrom_time.setText(startTime);
                                        Toast.makeText(WorkAuthorizationActivity.this, "You can select time from 2:00 PM to 6:00 PM", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(WorkAuthorizationActivity.this, "Select time greater than to time", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, fromToTimeHr, fromToTimeMin, false);// Yes 24 hour time
                // mTimePicker.setMin(hour + 1, minute);
                mTimePicker.setTitle("Select Time");


                mTimePicker.show();

            }
        });
*/

     /*   edt_andto_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                //  edt_andto_time.setText(hour + ":" + minute + " ");

                mTimePicker = new TimePickerDialog(WorkAuthorizationActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {

                                String endTime = "6:00 PM";
                                if(selectedHour >= andfromTime) {
                                    
                                    if (selectedHour >= 14 *//*&& selectedHour <= 18*//*) {
                                        if (selectedHour == 18 && selectedMinute == 0) {
                                            time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                            edt_andto_time.setText(time);
                                        } else if (selectedHour == 18 && selectedMinute > 0) {
                                            edt_andto_time.setText(endTime);
                                            Toast.makeText(WorkAuthorizationActivity.this, "You can select time from 2:00 PM to 6:00 PM", Toast.LENGTH_SHORT).show();
                                        } else {
                                            time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                            edt_andto_time.setText(time);
                                        }
                                    } else {
                                        edt_andto_time.setText(endTime);
                                        Toast.makeText(WorkAuthorizationActivity.this, "You can select time from 2:00 PM to 6:00 PM", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(WorkAuthorizationActivity.this, "Please select time greater than from time", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, hour, minute, false);// Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });*/


        edt_permit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date date = new Date();
                Date start;
                Date end;
                final Calendar c = Calendar.getInstance();

                Year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                int week = c.get(Calendar.WEEK_OF_MONTH);
                int test = c.get(Calendar.DAY_OF_WEEK);
                //c.add(Calendar.DATE, test);


                c.add(Calendar.DAY_OF_WEEK, 1);
                c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                long enddate = c.getTime().getTime();
                c.set(Calendar.WEEK_OF_MONTH, Calendar.SUNDAY);
                long end2 = c.getTime().getTime();

                Date date1 = new Date(enddate);

                //String strCurrentDate = "Wed, 18 Apr 2012 07:55:29 +0000";
                /*SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z");*/
                SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                Date newDate = null;
                try {
                    newDate = format.parse(String.valueOf(enddate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = formatter.format(new Date(Long.parseLong(String.valueOf(enddate))));
                String end_dayof_week = dateString;

                /*SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date2 = new Date(enddate);
                String end_dayof_week = targetFormat.format(date2);*/

              /*  SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date2 = new Date(end1);*/

                /*String chat_time = targetFormat.format(date);*/


                // Launch Date Picker Dialog
                datePickerDialog = new DatePickerDialog(WorkAuthorizationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                edt_permit_date.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            edt_permit_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            edt_permit_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(WorkAuthorizationActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            edt_permit_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        edt_permit_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        edt_permit_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    edt_permit_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + " - " + (month + 1)
                                            + " -" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);


                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

                //datePickerDialog.getDatePicker().setMaxDate(enddate);

            }
        });

        edt_cancel_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date date = new Date();
                Date start;
                Date end;
                final Calendar c = Calendar.getInstance();

                Year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                int week = c.get(Calendar.WEEK_OF_MONTH);
                int test = c.get(Calendar.DAY_OF_WEEK);
                //c.add(Calendar.DATE, test);


                c.add(Calendar.DAY_OF_WEEK, 1);
                c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                long enddate = c.getTime().getTime();
                c.set(Calendar.WEEK_OF_MONTH, Calendar.SUNDAY);
                long end2 = c.getTime().getTime();

                Date date1 = new Date(enddate);

                //String strCurrentDate = "Wed, 18 Apr 2012 07:55:29 +0000";
                /*SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z");*/
                SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                Date newDate = null;
                try {
                    newDate = format.parse(String.valueOf(enddate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = formatter.format(new Date(Long.parseLong(String.valueOf(enddate))));
                String end_dayof_week = dateString;

                /*SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date2 = new Date(enddate);
                String end_dayof_week = targetFormat.format(date2);*/

              /*  SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date2 = new Date(end1);*/

                /*String chat_time = targetFormat.format(date);*/


                // Launch Date Picker Dialog
                datePickerDialog = new DatePickerDialog(WorkAuthorizationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                edt_cancel_date.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            edt_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            edt_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(WorkAuthorizationActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            edt_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        edt_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        edt_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    edt_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);


                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

                //datePickerDialog.getDatePicker().setMaxDate(enddate);

            }
        });


        /* edt_spot_date*/
        edt_spot_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date date = new Date();
                Date start;
                Date end;
                final Calendar c = Calendar.getInstance();

                Year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                int week = c.get(Calendar.WEEK_OF_MONTH);
                int test = c.get(Calendar.DAY_OF_WEEK);
                //c.add(Calendar.DATE, test);


                c.add(Calendar.DAY_OF_WEEK, 1);
                c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                long enddate = c.getTime().getTime();
                c.set(Calendar.WEEK_OF_MONTH, Calendar.SUNDAY);
                long end2 = c.getTime().getTime();

                Date date1 = new Date(enddate);

                //String strCurrentDate = "Wed, 18 Apr 2012 07:55:29 +0000";
                /*SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z");*/
                SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                Date newDate = null;
                try {
                    newDate = format.parse(String.valueOf(enddate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = formatter.format(new Date(Long.parseLong(String.valueOf(enddate))));
                String end_dayof_week = dateString;

                /*SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date2 = new Date(enddate);
                String end_dayof_week = targetFormat.format(date2);*/

              /*  SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date2 = new Date(end1);*/

                /*String chat_time = targetFormat.format(date);*/


                // Launch Date Picker Dialog
                datePickerDialog = new DatePickerDialog(WorkAuthorizationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                edt_spot_date.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            edt_spot_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            edt_spot_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(WorkAuthorizationActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            edt_spot_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        edt_spot_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        edt_spot_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    edt_spot_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + " - " + (month + 1)
                                            + " -" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                //  datePickerDialog.getDatePicker().setMinDate(c.getTime().getTime() - 1000);
                // datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();

                //datePickerDialog.getDatePicker().setMaxDate(enddate);


            }
        });


        edt_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                Date date = new Date();
                Date start;
                Date end;
                final Calendar c = Calendar.getInstance();

                Year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                int week = c.get(Calendar.WEEK_OF_MONTH);
                int test = c.get(Calendar.DAY_OF_WEEK);
                //c.add(Calendar.DATE, test);


                c.add(Calendar.DAY_OF_WEEK, 1);

                c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                long enddate = c.getTime().getTime();
                c.set(Calendar.WEEK_OF_MONTH, Calendar.SUNDAY);
                long end2 = c.getTime().getTime();

                Date date1 = new Date(enddate);

                //String strCurrentDate = "Wed, 18 Apr 2012 07:55:29 +0000";
                /*SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z");*/
                SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                Date newDate = null;
                try {
                    newDate = format.parse(String.valueOf(enddate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = formatter.format(new Date(Long.parseLong(String.valueOf(enddate))));
                String end_dayof_week = dateString;


                // Launch Date Picker Dialog
                datePickerDialog = new DatePickerDialog(WorkAuthorizationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox


                                edt_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year));

                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";


                                // String seldate = trnselectDate;

                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                SelectedDate = DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                Calendar calendar = Calendar.getInstance();
                                sevenDay = timeStampConversion(SelectedDate);


                                edt_todate.setText(sevenDay);
                                //edt_todate.setEnabled(false);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            edt_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                                    + (month + 1) + "-" + Year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            edt_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                                    + (month + 1) + "-" + Year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(WorkAuthorizationActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            edt_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                                    + (month + 1) + "-" + Year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        edt_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                                + (month + 1) + "-" + Year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        edt_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                                + (month + 1) + "-" + Year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    edt_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                            + (month + 1) + "-" + Year));
                                    trnselectDate = Year + " - " + (month + 1)
                                            + " -" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);


                //(6 * 24 * 60 * 60 * 1000)  1582710376970 + 518400000


                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();


                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);


            }
        });

        edt_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog(edt_todate.getText().toString());


            }
        });

        img_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WorkAuthorizationActivity.this, ImageFullScreenActivity.class).putExtra("share_image_path",
                        Spotimgpost));
            }
        });


    }

    private void showDatePickerDialog(final String date) {
        // here date is 5-12-2013
        String[] split = date.split("-");
        int day = Integer.valueOf(split[0]);
        int month = Integer.valueOf(split[1]);
        int year = Integer.valueOf(split[2]);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                edt_todate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                        + (monthOfYear + 1) + "-" + year));
               /* edt_todate.setText(dayOfMonth + "-"
                        + (monthOfYear + 1) + "-" + year);*/
                String trnselectDate = year + " - " + (monthOfYear + 1)
                        + "-" + dayOfMonth + " 00:00:00.000";

                // String seldate = trnselectDate;
                String SelectedDate = (dayOfMonth + "-"
                        + (monthOfYear + 1) + "-" + year);

                String startDateVal = edt_fromdate.getText().toString();


                boolean val = dateDifference(SelectedDate, startDateVal);


                if (val) {
                    edt_todate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                            + (monthOfYear + 1) + "-" + year));

                } else {
                    edt_todate.setText(sevenDay);
                    Toast.makeText(getApplicationContext(), "Selected Date is more than 7 days to from date", Toast.LENGTH_SHORT).show();
                }
            }


        };


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                dateSetListener, year, month, day);

        String SelectedDate = (day + "-"
                + (month) + "-" + year);

        Date localTime = null;
        try {
            localTime = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(sevenDay);
            System.out.println("TimeStamp is " + localTime.getTime());

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMaxDate(localTime.getTime());
        datePickerDialog.show();
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }

    public static boolean compare_datetime(String fromdate, int selectedHour, int selectedMinute) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

        todaysDate = dfDate.format(new Date());
        try {
            if ((dfDate.parse(todaysDate).before(dfDate.parse(fromdate)))) {
                b = true;
            } else if ((dfDate.parse(todaysDate).equals(dfDate.parse(fromdate)))) {
                time = UpdateTime.updateTime(selectedHour, selectedMinute);
                b = true;
            } else {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                time = UpdateTime.updateTime(hour + 2, minute);
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    public File getOutputMediaFile(int type) {

        File mediaStorageDir;
        // External sdcard location
        mediaStorageDir = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME);


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + timeStamp + ".jpg");

            Log.d("test", "mediaFile" + mediaFile);


        } else {
            return null;
        }
        return mediaFile;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            //  Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap photo = null;
            try {


                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                FileOutputStream out = new FileOutputStream(mediaFile);
                photo.compress(Bitmap.CompressFormat.JPEG, 75, out);
              //  String url = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "attachment", null);
                //fileUri = Uri.parse(url);


                //   bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));
                File f = new File(mediaFile.getPath().toString());
                path = f.getName();

                if (temp.equals("1")) {
                    Isolatedimg = f.toString();
                    Isolatedimgpost = f.getName();
                    isolatedAbsolutePath = f.getAbsolutePath();
                    //handleSendImage(data);
                } else if (temp.equals("2")) {
                    Electricimg = f.toString();
                    Electricimgpost = f.getName();
                    electricalAbsolutePath = f.getAbsolutePath();

                    //handleSendImage(data);
                } else if (temp.equals("3")) {
                    SpotCheckimgpost = f.toString();
                    Spotimgpost = f.getName();
                    spotAbsolutePath = f.getAbsolutePath();

                    //handleSendImage(data);
                } else {
                    /*Imagefilename = path.toString();*/
                    Imagefilename = f.toString();
                    //handleSendImage(data);
                }

                if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {

                    new PostUploadImageMethod().execute();

                } else {

                    Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void reasonDialog(final int position, String projectCode) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(WorkAuthorizationActivity.this);
        LayoutInflater inflater = getLayoutInflater().from(WorkAuthorizationActivity.this);
        View dialogView = null;
        final EditText edt_remark;
        if (projectCode.equals("ACMB") || projectCode.equals("EIP")) {
            dialogView = inflater.inflate(R.layout.remarks_value, null);

        } else {
            dialogView = inflater.inflate(R.layout.remarks, null);
            //  edt_remark = dialogView.findViewById(R.id.edit_remark);
        }


        builder.setView(dialogView);

        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        edt_remark = dialogView.findViewById(R.id.edit_remarks);


        if (installationPreparationArrayList != null) {
            if (installationPreparationArrayList.get(position).getRemarks() != null) {
                String remarks = installationPreparationArrayList.get(position).getRemarks();
                edt_remark.setText(remarks);
            }
        }


        builder.setCancelable(false);
        final android.app.AlertDialog b = builder.create();
        b.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (!WorkAuthorizationActivity.this.isFinishing()) {
            b.show();
            //show dialog
        }
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remarks = edt_remark.getText().toString();
                installationPreparationArrayList.get(position).setSelected(true);
                installationPreparationArrayList.get(position).setRemarks(remarks);
                installationListAdapter.updateList(installationPreparationArrayList);
                b.dismiss();
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  installationPreparationArrayList.get(position).setSelected(false);
                installationPreparationArrayList.get(position).setRemarks("");
                installationListAdapter.updateList(installationPreparationArrayList);*/

                if (installationPreparationArrayList.get(position).getRemarks() == null ||
                        installationPreparationArrayList.get(position).getRemarks().equalsIgnoreCase("")) {
                    if (isAns) {
                        //holder.checkbox_user.setChecked(false);
                        installationPreparationArrayList.get(position).setSelected(false);
                        installationPreparationArrayList.get(position).setRemarks("");
                        installationListAdapter.updateList(installationPreparationArrayList);

                    } else {
                        // holder.checkbox_user.setChecked(true);
                        installationPreparationArrayList.get(position).setSelected(true);
                        installationPreparationArrayList.get(position).setRemarks(edt_remark.getText().toString());
                        installationListAdapter.updateList(installationPreparationArrayList);
                    }

                } else {
                    isAns = false;
                    installationPreparationArrayList.get(position).setSelected(true);
                    installationListAdapter.updateList(installationPreparationArrayList);
                    //holder.checkbox_user.setChecked(true);
                }

                b.dismiss();
            }
        });

        edt_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().equals("")) {
                    isAns = false;
                } else {
                    isAns = true;
                }

            }
        });
    }

    class DownloadDepotData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        // ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_GetStationList;

            try {
                res = CommonClass.OpenConnection(url, WorkAuthorizationActivity.this);
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
            progressBar.setVisibility(View.GONE);
            // progressDialog.dismiss();
            // dismissProgress();
            if (response.contains("[]")) {
                // dismissProgress();
                //Toast.makeText(WorkAuthorizationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WorkAuthorizationActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(depotArrayList);
                editor.putString("Depot", json);
                editor.commit();
                depotAdapter = new DepotAdapter(WorkAuthorizationActivity.this, depotArrayList);
                spinner_station.setAdapter(depotAdapter);


            }


        }
    }

    private class DownloadGoldenRules extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        // ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgress();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_GetGoldenRules;

            try {
                res = CommonClass.OpenConnection(url, WorkAuthorizationActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    goldenRulesArrayList = new ArrayList<>();
                    goldenRulesArrayList.clear();

                    // goldenRules.setDepotid("Select");
                    // goldenRules.setDepotname("Select");
                    //goldenRulesArrayList.add(0, goldenRules);
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        GoldenRules goldenRules = new GoldenRules();
                        JSONObject jorder = jResults.getJSONObject(i);

                        goldenRules.setGoldenRulesId(jorder.getString("GoldenRulesId"));
                        goldenRules.setGoldenRulesDesc(jorder.getString("GoldenRulesDesc"));
                        goldenRules.setSeq(jorder.getString("Seq"));
                        goldenRulesArrayList.add(goldenRules);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            // progressDialog.dismiss();
            //   dismissProgress();
            progressBar.setVisibility(View.GONE);
            if (response.contains("[]")) {
                //Toast.makeText(WorkAuthorizationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WorkAuthorizationActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(goldenRulesArrayList);
                editor.putString("GoldenRules", json);
                editor.commit();


                goldenRuleAdapter = new GoldenRuleAdapter(WorkAuthorizationActivity.this, goldenRulesArrayList, "WA", Mode, PermitStatus);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WorkAuthorizationActivity.this);

                // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                list_goldenRules.setLayoutManager(linearLayoutManager);
                list_goldenRules.setAdapter(goldenRuleAdapter);

                if (!goldenRulesList.equals("")) {
                    String[] goldenRules = new String[goldenRulesList.length()];
                    goldenRules = goldenRulesList.split(",");


                    for (int j = 0; j < goldenRulesArrayList.size(); j++) {
                        for (int k = 0; k < goldenRules.length; k++) {
                            if (goldenRulesArrayList.get(j).getGoldenRulesId().equals(goldenRules[k])) {

                                //list_indicaterisk.setItemChecked(k,true);
                                goldenRulesArrayList.get(j).setSelected(true);

                            }
                        }
                    }

                    // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WorkAuthorizationActivity.this);
                    goldenRuleAdapter.updateList(goldenRulesArrayList, Mode, PermitStatus);
                    list_goldenRules.setAdapter(goldenRuleAdapter);

                }


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
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_GetApproverPerson;

            try {
                res = CommonClass.OpenConnection(url, WorkAuthorizationActivity.this);
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
                        authorizedPerson.setCategorydesc(jorder.getString("categorydesc"));
                        authorizedPerson.setUserLoginId(jorder.getString("UserLoginId"));
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
            progressBar.setVisibility(View.GONE);
            // progressDialog.dismiss();
            //dismissProgress();
            if (response.contains("[]")) {
                //  dismissProgress();
                //  Toast.makeText(WorkAuthorizationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WorkAuthorizationActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(authorizedPersonArrayList);
                editor.putString("Authorized", json);
                editor.commit();


                authorizedPersonAdapter = new AuthorizedPersonAdapter(WorkAuthorizationActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                //spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                //permit_closed.setAdapter(authorizedPersonAdapter);


            }


        }
    }

    class DownloadContractorData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        // ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_GetContractorList;

            try {
                res = CommonClass.OpenConnection(url, WorkAuthorizationActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    contractorListActivityArrayList = new ArrayList<>();
                    contractorListActivityArrayList.clear();
                    ContractorList contractorList = new ContractorList();
                    contractorList.setCustVendorMasterId("");
                    contractorList.setCustVendorName("Select");
                    contractorList.setCustVendorCode("");
                    contractorListActivityArrayList.add(0, contractorList);

                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        contractorList = new ContractorList();
                        JSONObject jorder = jResults.getJSONObject(i);

                        contractorList.setCustVendorMasterId(jorder.getString("CustVendorMasterId"));
                        contractorList.setCustVendorName(jorder.getString("CustVendorName"));
                        contractorList.setCustVendorCode(jorder.getString("CustVendorCode"));
                        contractorListActivityArrayList.add(contractorList);
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
            progressBar.setVisibility(View.GONE);
            // progressDialog.dismiss();
            // dismissProgress();
            if (response.contains("[]")) {
                // dismissProgress();
                //  Toast.makeText(WorkAuthorizationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WorkAuthorizationActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(contractorListActivityArrayList);
                editor.putString("Contractor", json);
                editor.commit();
                permitContractorListAdapter = new PermitContractorListAdapter(WorkAuthorizationActivity.this, contractorListActivityArrayList);
                spinner_contractor.setAdapter(permitContractorListAdapter);
                spinner_authorize1.setAdapter(permitContractorListAdapter);


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
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_GetOperationList;

            try {
                res = CommonClass.OpenConnection(url, WorkAuthorizationActivity.this);
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
            progressBar.setVisibility(View.GONE);
            // progressDialog.dismiss();
            //dismissProgress();
            if (response.contains("[]")) {
                //  dismissProgress();
                //Toast.makeText(WorkAuthorizationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WorkAuthorizationActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(operationArrayList);
                editor.putString("Operation", json);
                editor.commit();
                operationAdapter = new OperationAdapter(WorkAuthorizationActivity.this, operationArrayList);
                spinner_operation.setAdapter(operationAdapter);


            }


        }
    }

    private void showProgress() {

        // mprogress.setVisibility(View.VISIBLE);
        /*LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.progress,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        // TextView text = (TextView) layout.findViewById(R.id.text);
        ProgressBar progress = (ProgressBar) layout.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        //text.setText("Loading..... please wait");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();*/


    }

    private void dismissProgress() {

        // mprogress.setVisibility(View.GONE);


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

                // Station List

                if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                    showProgress();
                    new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadDepotData().execute();
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

                // Approver Person List

                /*if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                    showProgress();
                    new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadAuthorizedPersonData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            CommonClass.displayToast(getApplicationContext(), msg);
                            dismissProgress();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }*/

                // Contractor List


                if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                    showProgress();
                    new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
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


                // Nature of Operation List


                if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                    showProgress();
                    new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadNatureOperationData().execute();
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

                // Permit No


                if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                    showProgress();
                    new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetPermitNo().execute();
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
//IndicateRisk

                if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                    showProgress();
                    new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadIndicateData().execute();
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


                if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                    showProgress();
                    new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadInstallationData().execute(PKFormId);
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

    class DownloadLocationOperationData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showProgress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_GetLocationOperation + "?WarehouseId=" + StationId;

            try {
                res = CommonClass.OpenConnection(url, WorkAuthorizationActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //  response = response.substring(1, response.length() - 1);

                    LocationArraylist = new ArrayList<>();
                    LocationArraylist.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        Location location = new Location();
                        JSONObject jorder = jResults.getJSONObject(i);
                        location.setLocationMasterId(jorder.getString("LocationMasterId"));
                        location.setLocationDesc(jorder.getString("LocationDesc"));
                        LocationArraylist.add(location);


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
                // dismissProgress();
                //  Toast.makeText(WorkAuthorizationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {

                locationOperationAdapter = new LocationOperationAdapter(WorkAuthorizationActivity.this, LocationArraylist);
                spinner_location.setAdapter(locationOperationAdapter);

                if (Mode.equalsIgnoreCase("E")) {
                    int locationpos = -1;
                    for (int j = 0; j < LocationArraylist.size(); j++) {
                        if (LocationArraylist.get(j).getLocationMasterId().equals(LocationId))
                            locationpos = j;
                    }
                    if (locationpos != -1)
                        spinner_location.setSelection(locationpos);
                    else
                        spinner_location.setSelection(0);
                }
            }


        }
    }

    class GetPermitNo extends AsyncTask<String, Void, String> {
        String res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            dismissProgress();
            if (integer != null) {

                PermitNo = integer;
                edit_permitno.setText(PermitNo);

            }


        }


        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebAPIUrl.api_GetPermitNo;

            res = CommonClass.OpenConnection(url, WorkAuthorizationActivity.this);

            if (res != null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return res;
        }
    }

    public class PostUploadImageMethod extends AsyncTask<String, Void, String> {

        private Exception exception;
        String params;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();

        }

        protected String doInBackground(String... urls) {

            try {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                // File sourceFile = new File(path);
                // String upLoadServerUri="http://192.168.1.53/api/ChatRoomAPI/UploadFiles";

                String upLoadServerUri = CompanyURL + WebAPIUrl.api_FileUpload;

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(mediaFile);
                URL url = new URL(upLoadServerUri);
                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", mediaFile.getAbsolutePath().toString());

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + mediaFile.getAbsolutePath().toString() + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                serverResponseMessage = conn.getResponseMessage();


                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseMessage.equals("OK")) {
                    if (temp.equals("1")) {
                        installationPreparationArrayList.get(img_pos).setElectricalImageName(isolatedAbsolutePath);
                        installationPreparationArrayList.get(img_pos).setiImg(Isolatedimgpost);
                    } else if (temp.equals("2")) {
                        installationPreparationArrayList.get(img_pos).setIsolatedImageName(electricalAbsolutePath);
                        installationPreparationArrayList.get(img_pos).seteImg(Electricimgpost);
                    } else if (temp.equals("3")) {
                        Bitmap thumbNail = BitmapFactory.decodeFile(SpotCheckimgpost);
                        //  holder.img_camera.setVisibility(View.GONE);
                        Bitmap thumbnail = BitmapFactory.decodeFile(SpotCheckimgpost);
                        // path = safetyMeasureArrayList.get(position).getImgAbsolutePath();
                        img_display.setVisibility(View.VISIBLE);
                        img_display.setImageBitmap(thumbnail);


                        /*SpotCheckimgpost = f.toString();
                        Spotimgpost = f.getName();
                        spotAbsolutePath = f.getAbsolutePath();*/
                    }
                    WorkAuthorizationActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dismissProgress();
                            installationListAdapter.updateList(installationPreparationArrayList);
                            Toast.makeText(WorkAuthorizationActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();


                        }
                    });


                } else {

                    if (serverResponseMessage.contains("Error")) {
                        WorkAuthorizationActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dismissProgress();
                                Toast.makeText(WorkAuthorizationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;

        }

        protected void onPostExecute(String feed) {


        }
    }

    class DownloadIndicateData extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showProgress();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebAPIUrl.api_GetDataSheet + "?form=" + formcode;

            try {
                res = CommonClass.OpenConnection(url, WorkAuthorizationActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //
                    //
                    // response = response.substring(1, response.length() - 1);

                    indicateRiskArrayList = new ArrayList<>();
                    equipmentUseArrayList = new ArrayList<>();
                    preventionArrayList = new ArrayList<>();

                    indicateRiskArrayList.clear();
                    equipmentUseArrayList.clear();
                    preventionArrayList.clear();


                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        Prevention prevention = new Prevention();
                        EquipmentUse equipmentUse = new EquipmentUse();
                        IndicateRisk indicateRisk = new IndicateRisk();
                        String QuesCode = jorder.getString("QuesCode");

                        if (QuesCode.equalsIgnoreCase("IndicatedRisk")) {
                            indicateRisk.setPKQuesID(jorder.getString("PKQuesID"));
                            indicateRisk.setQuesText(jorder.getString("QuesText"));
                            indicateRisk.setSelectionText(jorder.getString("SelectionText"));
                            indicateRisk.setSelectionValue(jorder.getString("SelectionValue"));
                            indicateRisk.setQuesCode(jorder.getString("QuesCode"));

                            indicateRiskArrayList.add(indicateRisk);

                        }
                        if (QuesCode.equalsIgnoreCase("EquipmentUse")) {
                            equipmentUse.setPKQuesID(jorder.getString("PKQuesID"));
                            equipmentUse.setQuesText(jorder.getString("QuesText"));
                            equipmentUse.setSelectionText(jorder.getString("SelectionText"));
                            equipmentUse.setSelectionValue(jorder.getString("SelectionValue"));
                            equipmentUse.setQuesCode(jorder.getString("QuesCode"));
                            equipmentUseArrayList.add(equipmentUse);


                        }
                        if (QuesCode.equalsIgnoreCase("PreventionMeasure")) {
                            prevention.setPKQuesID(jorder.getString("PKQuesID"));
                            prevention.setQuesText(jorder.getString("QuesText"));
                            prevention.setSelectionText(jorder.getString("SelectionText"));
                            prevention.setSelectionValue(jorder.getString("SelectionValue"));
                            prevention.setQuesCode(jorder.getString("QuesCode"));

                            preventionArrayList.add(prevention);


                        }

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
            progressBar.setVisibility(View.GONE);
            // dismissProgress();
            if (response.contains("[]")) {
                //  dismissProgress();
                // Toast.makeText(WorkAuthorizationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WorkAuthorizationActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                gson = new Gson();

                len_indicate.setVisibility(View.VISIBLE);
                String json = gson.toJson(indicateRiskArrayList);
                String json1 = gson.toJson(equipmentUseArrayList);
                String json2 = gson.toJson(preventionArrayList);
                editor.putString("indicate", json);
                editor.putString("equipment", json1);
                editor.putString("prevention", json2);
                editor.commit();
                indicateRiskAdapter = new InRiskAdapter(WorkAuthorizationActivity.this, indicateRiskArrayList, Mode, PermitStatus);
                list_indicaterisk.setAdapter(indicateRiskAdapter);
                Utility.setListViewHeightBasedOnItems(list_indicaterisk);

                equipmentAdapter = new EquipAdapter(WorkAuthorizationActivity.this, equipmentUseArrayList);
                list_equipment.setAdapter(equipmentAdapter);
                Utility.setListViewHeightBasedOnItems(list_equipment);

                prevenionAdapter = new PrevenionWAdapter(WorkAuthorizationActivity.this, preventionArrayList, Mode);
                list_prevention.setAdapter(prevenionAdapter);
                Utility.setListViewHeightBasedOnItems(list_prevention);


            }


        }
    }

    class DownloadInstallationData extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_GetInstallationOperation + "?PKFormId=" + PKFormId;

            try {
                res = CommonClass.OpenConnection(url, WorkAuthorizationActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    // response = response.substring(1, response.length() - 1);

                    installationPreparationArrayList = new ArrayList<>();
                    installationPreparationArrayList.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        InstallationPreparation installationPreparation = new InstallationPreparation();
                        JSONObject jorder = jResults.getJSONObject(i);
                        installationPreparation.setProjectId(jorder.getString("ProjectId"));
                        installationPreparation.setProjectCode(jorder.getString("ProjectCode"));
                        installationPreparation.setProjectName(jorder.getString("ProjectName"));
                        installationPreparationArrayList.add(installationPreparation);


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
            progressBar.setVisibility(View.GONE);
            if (response.contains("[]")) {
                // Toast.makeText(WorkAuthorizationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(WorkAuthorizationActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                gson = new Gson();
                String json = gson.toJson(installationPreparationArrayList);
                editor.putString("installation", json);
                editor.commit();
                installationListAdapter = new InstallationListAdapter(WorkAuthorizationActivity.this, installationPreparationArrayList, Mode, PermitStatus);
                list_installation.setAdapter(installationListAdapter);
                Utility.setListViewHeightBasedOnItems(list_installation);


            }


        }
    }

    public void saveactivityjson() {

        //prevention_plan = edit_Prevention_plan.getText().toString();
        StartDate = edt_fromdate.getText().toString();
        EndDate = edt_todate.getText().toString();
        fromtime = edt_from_time.getText().toString();
        totime = edt_from_to_time.getText().toString();
        andfromtime = edt_andfrom_time.getText().toString();
        andtotime = edt_andto_time.getText().toString();
        permitcloseddate = edt_permit_date.getText().toString();
        spotcheckdate = edt_spot_date.getText().toString();
        //  OperationCode = adapter.getcheckedradio();
        remarks = edit_remarks.getText().toString();
        PermitNo = edit_permitno.getText().toString();
        AuthorizeDate1 = edt_authorize_date.getText().toString();
        AuthorizeDate2 = edt_authorize_date1.getText().toString();

        StartDate = formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd", StartDate);
        EndDate = formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd", EndDate);
        operationlist = adapter.getArrayList();
        if (operationlist.size() > 0) {

            if (operationlist.size() > 0) {
                user4 = new String[operationlist.size()];
                for (int i = 0; i < operationlist.size(); i++) {
                    operationCodeId = operationlist.get(i).getOperationId();
                    user4[i] = operationCodeId.toString();
                    OperationCode = TextUtils.join(",", user4);

                }


            }

        }


        indicateRiskArrayList = indicateRiskAdapter.getArrayList();
        if (indicateRiskArrayList.size() > 0) {

            if (indicateRiskArrayList.size() > 0) {
                user = new String[indicateRiskArrayList.size()];
                for (int i = 0; i < indicateRiskArrayList.size(); i++) {
                    IndicateId = indicateRiskArrayList.get(i).getPKQuesID();
                    user[i] = IndicateId.toString();
                    listindicaterisk = TextUtils.join(",", user);
                    if (indicateRiskArrayList.get(i).getSelectionText().equals("Other(s) –Specify") ||
                            indicateRiskArrayList.get(i).getSelectionText().equals("Other")) {
                        if (indicateRiskArrayList.get(i).getRemarks() == null || indicateRiskArrayList.get(i).getRemarks().equals("")) {
                            indicateRisk_Others = "";
                        } else {
                            indicateRisk_Others = indicateRiskArrayList.get(i).getRemarks();
                        }

                    }
                }
            }
        }


        equipmentUseArrayList = equipmentAdapter.getArrayList();
        if (equipmentUseArrayList.size() > 0) {
            if (equipmentUseArrayList.size() > 0) {
                user1 = new String[equipmentUseArrayList.size()];
                for (int i = 0; i < equipmentUseArrayList.size(); i++) {
                    String Equipment = equipmentUseArrayList.get(i).getPKQuesID();
                    user1[i] = Equipment.toString();
                    listequipmentrisk = TextUtils.join(",", user1);

                    if (equipmentUseArrayList.get(i).getSelectionText().equals("Other(s) –Specify") ||
                            equipmentUseArrayList.get(i).getSelectionText().equals("Other")) {
                        if (equipmentUseArrayList.get(i).getRemarks() == null || equipmentUseArrayList.get(i).getRemarks().equals("")) {
                            equipment_Others = "";
                        } else {
                            equipment_Others = equipmentUseArrayList.get(i).getRemarks();
                        }

                    }

                }

            }
        }
        preventionArrayList = prevenionAdapter.getArrayList();

        if (preventionArrayList.size() > 0) {
            if (preventionArrayList.size() > 0) {
                user2 = new String[preventionArrayList.size()];
                for (int i = 0; i < preventionArrayList.size(); i++) {
                    String Prevention = preventionArrayList.get(i).getPKQuesID();
                    user2[i] = Prevention.toString();
                    listprevention = TextUtils.join(",", user2);
                    if (preventionArrayList.get(i).getSelectionText().equals("Other(s) –Specify") ||
                            preventionArrayList.get(i).getSelectionText().equals("Other")) {
                        if (preventionArrayList.get(i).getRemarks() == null || preventionArrayList.get(i).getRemarks().equals("")) {
                            prevention_Others = "";
                        } else {
                            prevention_Others = preventionArrayList.get(i).getRemarks();
                        }

                    }
                }

            }
        }


        installationPreparationArrayList = installationListAdapter.getArrayList();

        if (installationPreparationArrayList.size() > 0) {
            if (installationPreparationArrayList.size() > 0) {
                user3 = new String[installationPreparationArrayList.size()];
                for (int i = 0; i < installationPreparationArrayList.size(); i++) {
                    String Installation = installationPreparationArrayList.get(i).getProjectId();
                    user3[i] = Installation.toString();
                    listinstallation = TextUtils.join(",", user3);
                    if (installationPreparationArrayList.get(i).getProjectCode().equals("ORS")) {
                        if (installationPreparationArrayList.get(i).getRemarks() == null ||
                                installationPreparationArrayList.get(i).getRemarks().equals("")) {
                            installation_Others = "";
                        } else {
                            installation_Others = installationPreparationArrayList.get(i).getRemarks();
                        }

                    }

                }
            }
        }

        goldenRulesArrayList = goldenRuleAdapter.getArrayList();

        if (goldenRulesArrayList.size() > 0) {
            if (goldenRulesArrayList.size() > 0) {
                user5 = new String[goldenRulesArrayList.size()];
                for (int i = 0; i < goldenRulesArrayList.size(); i++) {
                    String goldenRulesId = goldenRulesArrayList.get(i).getGoldenRulesId();
                    user5[i] = goldenRulesId.toString();
                    goldenRulesList = TextUtils.join(",", user5);

                }
            }
        }


        // String Permitno = edit_permitno.getText().toString();

        Activityjson = new JSONObject();

        try {
            Activityjson.put("FormId", PKFormId);
            Activityjson.put("PermitNo", PermitNo);
            Activityjson.put("FkWareHouseMasterId", StationId);
            Activityjson.put("PreventionPlanRef", PreventionPlan_Code);
            Activityjson.put("PermitFromDate", StartDate);
            Activityjson.put("PermitTodate", EndDate);
            Activityjson.put("FromTime1", fromtime);
            Activityjson.put("ToTime1", totime);
            Activityjson.put("FromTime2", andfromtime);
            Activityjson.put("ToTime2", andtotime);
            Activityjson.put("fkContractorId", contractorId);
            Activityjson.put("fkOperationMasterId", OperationId);
            Activityjson.put("FkLocationMasterId", LocationId);
            Activityjson.put("permitOperationCode", OperationCode);//HW
            Activityjson.put("IndicatedRisk", listindicaterisk);
            Activityjson.put("IsolatedImageName", Isolatedimgpost);
            Activityjson.put("ElectricalImageName", Electricimgpost);
            Activityjson.put("SpotImage", Spotimgpost);
            Activityjson.put("EquipmentUse", listequipmentrisk);
            Activityjson.put("PreventionMeasure", listprevention);
            Activityjson.put("MethodOperationStatus", method_of_operation);//Y/N
            Activityjson.put("Installationlist", listinstallation);
            Activityjson.put("AuthorizeBy", Authorize1Id);
            Activityjson.put("contractorId", contractorId);
            Activityjson.put("PermitCloseDate", permitcloseddate);
            Activityjson.put("PermitCloseBy", PermitClosedId);
            Activityjson.put("SpotCheckBy", SpotCheckId);
            Activityjson.put("SpotCheckDate", spotcheckdate);
            Activityjson.put("PermitCancelBy", cancelId);
            Activityjson.put("PermitCancelDate", cancelDate);
            //cancelById
            Activityjson.put("AuthorizeDate1", AuthorizeDate1);
            Activityjson.put("AuthorizeDate2", AuthorizeDate2);
            Activityjson.put("PermitCloseRemark", remarks);
            Activityjson.put("WA_Others", installation_Others);
            Activityjson.put("WA_IndOthers", indicateRisk_Others);
            Activityjson.put("WA_EquipOthers", equipment_Others);
            Activityjson.put("WA_PrevOthers", prevention_Others);
            Activityjson.put("GoldenRulesId", goldenRulesList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String FinalJsonObject = Activityjson.toString();
        // String URL = CompanyURL+ WebAPIUrl.api_PostWorkAuthorization;
        //String op = "Success";

        if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
            showProgress();
            new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                @Override

                public void callMethod() {
                    new DownloadPostData().execute(FinalJsonObject);

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


    }

    public void editjson() {

        StartDate = edt_fromdate.getText().toString();
        EndDate = edt_todate.getText().toString();
        fromtime = edt_from_time.getText().toString();
        totime = edt_from_to_time.getText().toString();
        andfromtime = edt_andfrom_time.getText().toString();
        andtotime = edt_andto_time.getText().toString();
        permitcloseddate = edt_permit_date.getText().toString();
        spotcheckdate = edt_spot_date.getText().toString();
        cancelDate = edt_cancel_date.getText().toString();
        //  OperationCode = adapter.getcheckedradio();
        remarks = edit_remarks.getText().toString();
        PermitNo = edit_permitno.getText().toString();
        AuthorizeDate1 = edt_authorize_date.getText().toString();
        AuthorizeDate2 = edt_authorize_date1.getText().toString();

        StartDate = formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd", StartDate);
        EndDate = formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd", EndDate);
        operationlist = adapter.getArrayList();
        if (operationlist.size() > 0) {
            if (operationlist.size() > 0) {
                user4 = new String[operationlist.size()];
                for (int i = 0; i < operationlist.size(); i++) {
                    operationCodeId = operationlist.get(i).getOperationId();
                    user4[i] = operationCodeId.toString();
                    OperationCode = TextUtils.join(",", user4);
                }
            }
        }

        goldenRulesArrayList = goldenRuleAdapter.getArrayList();

        if (goldenRulesArrayList.size() > 0) {
            if (goldenRulesArrayList.size() > 0) {
                user5 = new String[goldenRulesArrayList.size()];
                for (int i = 0; i < goldenRulesArrayList.size(); i++) {
                    String goldenRulesId = goldenRulesArrayList.get(i).getGoldenRulesId();
                    user5[i] = goldenRulesId.toString();
                    goldenRulesList = TextUtils.join(",", user5);

                }
            }
        }


        indicateRiskArrayList = indicateRiskAdapter.getArrayList();
        if (indicateRiskArrayList.size() > 0) {

            if (indicateRiskArrayList.size() > 0) {
                user = new String[indicateRiskArrayList.size()];
                for (int i = 0; i < indicateRiskArrayList.size(); i++) {
                    IndicateId = indicateRiskArrayList.get(i).getPKQuesID();
                    user[i] = IndicateId.toString();
                    listindicaterisk = TextUtils.join(",", user);
                    if (indicateRiskArrayList.get(i).getSelectionText().equals("Other(s) –Specify") ||
                            indicateRiskArrayList.get(i).getSelectionText().equals("Other")) {
                        if (indicateRiskArrayList.get(i).getRemarks() == null || indicateRiskArrayList.get(i).getRemarks().equals("")) {
                            indicateRisk_Others = "";
                        } else {
                            indicateRisk_Others = indicateRiskArrayList.get(i).getRemarks();
                        }

                    }

                }


            }

        }


        equipmentUseArrayList = equipmentAdapter.getArrayList();
        if (equipmentUseArrayList.size() > 0) {
            if (equipmentUseArrayList.size() > 0) {
                user1 = new String[equipmentUseArrayList.size()];
                for (int i = 0; i < equipmentUseArrayList.size(); i++) {
                    String Equipment = equipmentUseArrayList.get(i).getPKQuesID();
                    user1[i] = Equipment.toString();
                    listequipmentrisk = TextUtils.join(",", user1);

                    if (equipmentUseArrayList.get(i).getSelectionText().equals("Other(s) –Specify") ||
                            equipmentUseArrayList.get(i).getSelectionText().equals("Other")) {
                        if (equipmentUseArrayList.get(i).getRemarks() == null || equipmentUseArrayList.get(i).getRemarks().equals("")) {
                            equipment_Others = "";
                        } else {
                            equipment_Others = equipmentUseArrayList.get(i).getRemarks();
                        }

                    }

                }

            }
        }
        preventionArrayList = prevenionAdapter.getArrayList();

        if (preventionArrayList.size() > 0) {
            if (preventionArrayList.size() > 0) {
                user2 = new String[preventionArrayList.size()];
                for (int i = 0; i < preventionArrayList.size(); i++) {
                    String Prevention = preventionArrayList.get(i).getPKQuesID();
                    user2[i] = Prevention.toString();
                    listprevention = TextUtils.join(",", user2);
                    if (preventionArrayList.get(i).getSelectionText().equals("Other(s) –Specify") ||
                            preventionArrayList.get(i).getSelectionText().equals("Other")) {
                        if (preventionArrayList.get(i).getRemarks() == null || preventionArrayList.get(i).getRemarks().equals("")) {
                            prevention_Others = "";
                        } else {
                            prevention_Others = preventionArrayList.get(i).getRemarks();
                        }

                    }
                }

            }
        }


        installationPreparationArrayList = installationListAdapter.getArrayList();

        if (installationPreparationArrayList.size() > 0) {
            if (installationPreparationArrayList.size() > 0) {
                user3 = new String[installationPreparationArrayList.size()];
                for (int i = 0; i < installationPreparationArrayList.size(); i++) {
                    String Installation = installationPreparationArrayList.get(i).getProjectId();
                    user3[i] = Installation.toString();
                    listinstallation = TextUtils.join(",", user3);
                    if (installationPreparationArrayList.get(i).getProjectCode().equals("ORS")) {
                        if (installationPreparationArrayList.get(i).getRemarks() == null ||
                                installationPreparationArrayList.get(i).getRemarks().equals("")) {
                            installation_Others = "";
                        } else {
                            installation_Others = installationPreparationArrayList.get(i).getRemarks();
                        }

                    }

                }
            }
        }


        Activityjson = new JSONObject();

        try {
            Activityjson.put("FormId", PKFormId);
            Activityjson.put("PermitNo", PermitNo);
            Activityjson.put("FkWareHouseMasterId", StationId);
            Activityjson.put("PreventionPlanRef", PreventionPlan_Code);
            Activityjson.put("PermitFromDate", StartDate);
            Activityjson.put("PermitTodate", EndDate);
            Activityjson.put("FromTime1", fromtime);
            Activityjson.put("ToTime1", totime);
            Activityjson.put("FromTime2", andfromtime);
            Activityjson.put("ToTime2", andtotime);
            Activityjson.put("fkContractorId", contractorId);
            Activityjson.put("fkOperationMasterId", OperationId);
            Activityjson.put("FkLocationMasterId", LocationId);
            Activityjson.put("permitOperationCode", OperationCode);//HW
            Activityjson.put("IndicatedRisk", listindicaterisk);
            Activityjson.put("Installationlist", listinstallation);
            /*Activityjson.put("IsolatedImageName", mediaFile.getName());
            Activityjson.put("ElectricalImageName", mediaFile.getName());*/
            Activityjson.put("IsolatedImageName", Isolatedimgpost);
            Activityjson.put("ElectricalImageName", Electricimgpost);
            Activityjson.put("EquipmentUse", listequipmentrisk);
            Activityjson.put("PreventionMeasure", listprevention);
            Activityjson.put("MethodOperationStatus", method_of_operation);//Y/N
            Activityjson.put("AuthorizeBy", authorize);
            Activityjson.put("contractorId", contractorId);
            Activityjson.put("PermitCloseDate", permitcloseddate);
            Activityjson.put("PermitCloseBy", PermitClosedId);
            Activityjson.put("SpotCheckBy", SpotCheckId);
            Activityjson.put("SpotCheckDate", spotcheckdate);
            Activityjson.put("PermitCancelDate", cancelDate);
            Activityjson.put("PermitCancelBy", cancelId);
        /*    PermitCancelDate
                    PermitCancelBy*/
            //cancelById
            /*Activityjson.put("",AuthorizeDate1);
            Activityjson.put("",AuthorizeDate2);*/
            Activityjson.put("PermitCloseRemark", remarks);
            Activityjson.put("WA_Others", installation_Others);
            Activityjson.put("WA_IndOthers", indicateRisk_Others);
            Activityjson.put("WA_EquipOthers", equipment_Others);
            Activityjson.put("WA_PrevOthers", prevention_Others);
            Activityjson.put("GoldenRulesId", goldenRulesList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //final String FinalEditJsonObject = new Gson().toJson(Activityjson);
        String FinalEditJsonObject = Activityjson.toString();
        FinalEditJsonObject = FinalEditJsonObject.replaceAll("\\\\", "");
        if (!(PermitStatus.equals("P"))) {

            if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                final String finalFinalEditJsonObject = FinalEditJsonObject;
                new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadChildPermitDetails().execute(PermitNo, finalFinalEditJsonObject);

                        //ChildPermitDetails(string PermitNo)
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }

        } else {
            if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                showProgress();
                final String finalFinalEditJsonObject = FinalEditJsonObject;
                new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadEditPostData().execute(finalFinalEditJsonObject);
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
        }


    }

    public class DownloadPostData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... voids) {
            String objFinalObj = voids[0];

            Object res;
            try {
                String url = CompanyURL + WebAPIUrl.api_PostWorkAuthorization;
                res = CommonClass.OpenPostConnection(url, objFinalObj, getApplicationContext());
                if (res != null) {
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


            if (val.contains("false")) {//Success
                Toast.makeText(WorkAuthorizationActivity.this, "Data save successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(WorkAuthorizationActivity.this, SupervisorMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {
                Toast.makeText(WorkAuthorizationActivity.this, "Data not save successfully", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(WorkAuthorizationActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class DownloadEditPostData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... voids) {
            String objFinalObj = voids[0];

            Object res;
            try {
                String url = CompanyURL + WebAPIUrl.api_PosteditWorkAuthorization;
                res = CommonClass.OpenPostConnection(url, objFinalObj, getApplicationContext());
                if (res != null) {
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


            if (val.contains("false")) {//Success
                Toast.makeText(WorkAuthorizationActivity.this, "Data Updated successfully", Toast.LENGTH_LONG).show();
                //startActivity(new Intent(WorkAuthorizationActivity.this, WorkAuthorizationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                startActivity(new Intent(WorkAuthorizationActivity.this, PermitStatusActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {

                Toast.makeText(WorkAuthorizationActivity.this, "Data not updated successfully", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(WorkAuthorizationActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
            }
        }
    }

    class DownloadIsValidUser extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String loginId = params[0];

            String response;
            try {
                String url = CompanyURL + WebAPIUrl.api_GetIsValidUser + "?AppEnvMasterId=" + URLEncoder.encode("z207", "UTF-8") +
                        "&PlantId=" + URLEncoder.encode("1", "UTF-8") +
                        "&UserLoginId=" + URLEncoder.encode(loginId, "UTF-8") + "&UserPwd=" + URLEncoder.encode(Password, "UTF-8");
                res = CommonClass.OpenConnection(url, WorkAuthorizationActivity.this);
                response = res.toString().replaceAll("\\\\", "");
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


            if (integer.contains("true") || integer.equals("")) {
                Toast.makeText(WorkAuthorizationActivity.this, "Valid Password", Toast.LENGTH_SHORT).show();
                //b.dismiss();

                if (ReasonVal.equals("1") || ReasonVal.equals("2") || ReasonVal.equals("3") && Mode.equals("E")) {
                    btn_submit.setVisibility(View.VISIBLE);
                }
                cusdialog1.setVisibility(View.GONE);
                cusdialog2.setVisibility(View.GONE);
                spinner_authorize.setSelection(0);
                spinner_permit_closed.setSelection(0);
                edit_password.setText("");
                edit_password_reason.setText("");
                edit_reason.setText("");
                hideKeyboard(WorkAuthorizationActivity.this);
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


    class getDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_getHWDetails + "?form=" + PermitNo;

            Log.d("URL", url);

            try {
                res = CommonClass.OpenConnection(url, WorkAuthorizationActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    Log.d("URL", response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            progressBar.setVisibility(View.GONE);
            if (!integer.equalsIgnoreCase("") || !integer.equalsIgnoreCase(null)) {
                try {
                    JSONArray jResults = new JSONArray(integer);
                    for (int i = 0; i < jResults.length(); i++) {
                        Permit permit = new Permit();
                        JSONObject jorder = jResults.getJSONObject(i);

                        //contractorlist,depostation , Operation , PErmitno

                        //  String HotWorkPermitStatus=jorder.getString("HotworkpermitStatus");
                        //  String HotWorkPermiId = jorder.getString("HotWorkPermitMasterId");
                        goldenRulesList = jorder.getString("GoldenRulesId");
                        String[] goldenRules = new String[goldenRulesList.length()];
                        goldenRules = goldenRulesList.split(",");
                        if (goldenRulesArrayList.size() != 0) {

                            for (int j = 0; j < goldenRulesArrayList.size(); j++) {
                                for (int k = 0; k < goldenRules.length; k++) {
                                    if (goldenRulesArrayList.get(j).getGoldenRulesId().equals(goldenRules[k])) {
                                        //list_indicaterisk.setItemChecked(k,true);
                                        goldenRulesArrayList.get(j).setSelected(true);

                                    }

                                }
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WorkAuthorizationActivity.this);

                            // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            list_goldenRules.setLayoutManager(linearLayoutManager);
                            goldenRuleAdapter.updateList(goldenRulesArrayList, Mode, PermitStatus);
                            list_goldenRules.setAdapter(goldenRuleAdapter);


                        } else {
                            if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                                new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new DownloadGoldenRules().execute();
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {

                                    }
                                });
                            }

                        }

                        PermitNo = jorder.getString("PermitNo");
                        edit_permitno.setText(PermitNo);

                        PreventionPlan_Code = jorder.getString("PreventionPlanRef");
                        edit_Prevention_plan.setText(PreventionPlan_Code);

                        StartDate = jorder.getString("PermitFromDate");
                        StartDate = formateDateFromstring("yyyy-MM-dd", "dd-MM-yyyy", StartDate);
                        edt_fromdate.setText(StartDate);

                        EndDate = jorder.getString("PermitTodate");
                        EndDate = formateDateFromstring("yyyy-MM-dd", "dd-MM-yyyy", EndDate);
                        edt_todate.setText(EndDate);

                        fromtime = jorder.getString("FromTime1");
                        edt_from_time.setText(fromtime);

                        totime = jorder.getString("ToTime1");
                        edt_from_to_time.setText(totime);

                        andfromtime = jorder.getString("FromTime2");
                        edt_andfrom_time.setText(andfromtime);

                        andtotime = jorder.getString("ToTime2");
                        edt_andto_time.setText(andtotime);

                        StationId = jorder.getString("FkWareHouseMasterId");
                        StationName = jorder.getString("WarehouseDescription");

                        int depopos = -1;
                        for (int j = 0; j < depotArrayList.size(); j++) {
                            if (depotArrayList.get(j).getDepotid().equals(StationId))
                                depopos = j;
                        }
                        if (depopos != -1)
                            spinner_station.setSelection(depopos);
                        else
                            spinner_station.setSelection(0);

                        LocationId = jorder.getString("FkLocationMasterId");
                        if (LocationArraylist.size() != 0) {

                            int locationpos = -1;
                            for (int j = 0; j < LocationArraylist.size(); j++) {
                                if (LocationArraylist.get(j).getLocationMasterId().equals(LocationId))
                                    locationpos = j;
                            }
                            if (locationpos != -1)
                                spinner_location.setSelection(locationpos);
                            else
                                spinner_location.setSelection(0);

                        } else {
                            if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                                new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new DownloadLocationOperationData().execute(StationId);
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {

                                    }
                                });
                            }
                        }
                        contractorId = jorder.getString("fkContractorId");
                        contractorName = jorder.getString("CustVendorName");

                        int contractorpos = -1;
                        for (int j = 0; j < contractorListActivityArrayList.size(); j++) {
                            if (contractorListActivityArrayList.get(j).getCustVendorMasterId().equals(contractorId))
                                contractorpos = j;
                        }
                        if (contractorpos != -1) {
                            spinner_contractor.setSelection(contractorpos);
                            spinner_authorize1.setSelection(contractorpos);
                        } else {
                            spinner_contractor.setSelection(0);
                            spinner_authorize1.setSelection(0);
                        }


                        OperationId = jorder.getString("fkOperationMasterId");
                        OperationName = jorder.getString("Operation");
                        int operaionpos = -1;
                        for (int j = 0; j < operationArrayList.size(); j++) {
                            if (operationArrayList.get(j).getOperationMasterId().equals(OperationId))
                                operaionpos = j;
                        }

                        if (operaionpos != -1)
                            spinner_operation.setSelection(operaionpos);
                        else
                            spinner_operation.setSelection(0);


                        //get id from service
                        //get position of id from arraylist
                        //set checked to particular position of id in arraylist

                        /*OperationCode*/

                        OperationCode = jorder.getString("permitOperationCode");/* "chetana,sayali,suyog,vritti,pradnya"*/

                        String[] operationcode = new String[OperationCode.length()];
                        operationcode = OperationCode.split(",");
                        for (int j = 0; j < operationlist.size(); j++) {
                            for (int k = 0; k < operationcode.length; k++) {
                                if (operationlist.get(j).getOperationId().equals(operationcode[k])) {
                                    int pos = j;
                                    //list_indicaterisk.setItemChecked(k,true);
                                    operationlist.get(pos).setChecked(true);
                                    adapter = new WorkOperationAdapter(WorkAuthorizationActivity.this, operationlist, Mode, PermitStatus);
                                    grid_operation.setAdapter(adapter);
                                    /*boolean a = list_indicaterisk.isItemChecked(j);*/
                                }

                            }
                        }

                        //method of operation
                        method_of_operation = jorder.getString("MethodOperationStatus");
                        if (method_of_operation.equalsIgnoreCase("Y")) {
                            radio_yes.setChecked(true);

                        } else if (method_of_operation.equalsIgnoreCase("N")) {
                            radio_no.setChecked(true);
                        } else {
                            radio_yes.setChecked(false);
                            radio_no.setChecked(false);
                        }


                        // indicaterisk data
                        //string get value in string
                        //put string into array;
                        //split comma separated

                        listindicaterisk = jorder.getString("IndicatedRisk");/* "chetana,sayali,suyog,vritti,pradnya"*/

                        String[] indicaterisk = new String[listindicaterisk.length()];
                        indicaterisk = listindicaterisk.split(",");
                        for (int j = 0; j < indicateRiskArrayList.size(); j++) {
                            for (int k = 0; k < indicaterisk.length; k++) {
                                if (indicateRiskArrayList.get(j).getPKQuesID().equals(indicaterisk[k])) {
                                    int pos = j;
                                    //list_indicaterisk.setItemChecked(k,true);
                                    indicateRiskArrayList.get(pos).setSelected(true);
                                    indicateRiskAdapter = new InRiskAdapter(WorkAuthorizationActivity.this, indicateRiskArrayList, Mode, PermitStatus);
                                    list_indicaterisk.setAdapter(indicateRiskAdapter);
                                    /*boolean a = list_indicaterisk.isItemChecked(j);*/
                                }

                            }
                        }


                        //equipment risk
                        listequipmentrisk = jorder.getString("EquipmentUse"); /*"chetana,sayali,suyog,vritti,pradnya";*/
                        String[] equipmentrsk = new String[listequipmentrisk.length()];
                        equipmentrsk = listequipmentrisk.split(",");
                        for (int j = 0; j < equipmentUseArrayList.size(); j++) {
                            for (int k = 0; k < equipmentrsk.length; k++) {
                                if (equipmentUseArrayList.get(j).getPKQuesID().equals(equipmentrsk[k])) {
                                    int pos = j;
                                    equipmentUseArrayList.get(pos).setSelected(true);
                                    equipmentAdapter = new EquipAdapter(WorkAuthorizationActivity.this, equipmentUseArrayList);
                                    list_equipment.setAdapter(equipmentAdapter);
                                    //list_equipment.setItemChecked(j,true);
                                }

                            }
                        }

                        //prevention measure
                        listprevention = jorder.getString("PreventionMeasure");
                        String[] preventionmeasure = new String[listprevention.length()];
                        preventionmeasure = listprevention.split(",");
                        for (int j = 0; j < preventionArrayList.size(); j++) {
                            for (int k = 0; k < preventionmeasure.length; k++) {
                                if (preventionArrayList.get(j).getPKQuesID().equals(preventionmeasure[k])) {
                                    int pos = j;
                                    preventionArrayList.get(pos).setSelected(true);
                                    prevenionAdapter = new PrevenionWAdapter(WorkAuthorizationActivity.this, preventionArrayList, Mode);
                                    list_prevention.setAdapter(prevenionAdapter);

                                }
                            }
                        }


                        listinstallation = jorder.getString("Installationlist");
                        String[] installation1 = new String[listinstallation.length()];
                        installation1 = listinstallation.split(",");
                        for (int j = 0; j < installationPreparationArrayList.size(); j++) {
                            for (int k = 0; k < installation1.length; k++) {
                                if (installation1[k].equals(installationPreparationArrayList.get(j).getProjectId())) {
                                    installationPreparationArrayList.get(j).setSelected(true);
                                    installationListAdapter = new InstallationListAdapter(WorkAuthorizationActivity.this, installationPreparationArrayList, Mode, PermitStatus);
                                    list_installation.setAdapter(installationListAdapter);

                                } else {
                                }
                            }
                        }


                        authorize = jorder.getString("AuthorizeBy");
                        // Authorize1Name = jorder.getString("");
                        int authorizepos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(authorize))
                                authorizepos = j;
                        }

                        if (authorizepos != -1)
                            txt_authorize.setText(authorizedPersonArrayList.get(authorizepos).getAuthorizename());
                        else
                            txt_authorize.setText("Select");


                        permitcloseddate = jorder.getString("PermitCloseDate");
                        edt_permit_date.setText(permitcloseddate);

                        spotcheckdate = jorder.getString("SpotCheckDate");
                        edt_spot_date.setText(spotcheckdate);


                        PermitClosedId = jorder.getString("PermitCloseBy");

                        int permitpos = -1;
                        if (PermitClosedId.equalsIgnoreCase("")) {

                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(PermitClosedId))
                                    permitpos = j;
                            }
                        }
                        if (permitpos != -1)
                            txt_permit_closed.setText(authorizedPersonArrayList.get(permitpos).getAuthorizename());
                        else
                            txt_permit_closed.setText("Select");


                        SpotCheckId = jorder.getString("SpotCheckBy");
                        int spotcpos = -1;
                        if (SpotCheckId.equalsIgnoreCase("")) {

                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(SpotCheckId))
                                    spotcpos = j;
                            }
                        }
                        if (spotcpos != -1)
                            txt_spotcheck.setText(authorizedPersonArrayList.get(spotcpos).getAuthorizename());
                        else
                            txt_spotcheck.setText("Select");

                       /* if(PermitClosedId.equals("") &&  SpotCheckId.equals("")){
                            btn_submit.setVisibility(View.GONE);
                        }else{
                            btn_submit.setVisibility(View.VISIBLE);
                        }*/


                        remarks = jorder.getString("PermitCloseRemark");
                        if (!remarks.equalsIgnoreCase(""))
                            edit_remarks.setText(remarks);
                        else
                            edit_remarks.setText("");


                        installation_Others = jorder.getString("WA_Others");
                        if (installation_Others != "") {
                            for (int j = 0; j < installationPreparationArrayList.size(); j++) {
                                if (installationPreparationArrayList.get(j).getProjectCode().equals("ORS")) {
                                    installationPreparationArrayList.get(j).setRemarks(installation_Others);
                                    installationPreparationArrayList.get(j).isSelected();
                                }
                            }
                        }


                        indicateRisk_Others = jorder.getString("WA_IndOthers");

                        if (indicateRisk_Others != "") {
                            for (int j = 0; j < indicateRiskArrayList.size(); j++) {
                                if (indicateRiskArrayList.get(j).getSelectionText().equals("Other(s) –Specify")) {
                                    indicateRiskArrayList.get(j).setRemarks(indicateRisk_Others);
                                    indicateRiskArrayList.get(j).isSelected();
                                }
                            }

                        }

                        equipment_Others = jorder.getString("WA_EquipOthers");//Other(s) –Specify//equipmentUseArrayList

                        if (equipment_Others != "") {
                            for (int j = 0; j < equipmentUseArrayList.size(); j++) {
                                if (equipmentUseArrayList.get(j).getSelectionText().equals("Other(s) –Specify")) {
                                    equipmentUseArrayList.get(j).setRemarks(equipment_Others);
                                    equipmentUseArrayList.get(j).isSelected();
                                }
                            }
                        }

                        prevention_Others = jorder.getString("WA_PrevOthers");//Other(s) –Specify//preventionArrayList
                        for (int j = 0; j < preventionArrayList.size(); j++) {
                            if (prevention_Others != "") {
                                if (preventionArrayList.get(j).getSelectionText().equals("Other(s) –Specify")) {
                                    preventionArrayList.get(j).setRemarks(prevention_Others);
                                    preventionArrayList.get(j).isSelected();
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {

                    new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetWAImages().execute(PermitNo);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }

            }
        }
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

    public void captureimg(String imgcode, int position) {
        img_pos = position;
        if (imgcode.equals("IBV")) {
            temp = "1";

            if (ContextCompat.checkSelfPermission(WorkAuthorizationActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                }
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        } else {
            temp = "2";

            if (ContextCompat.checkSelfPermission(WorkAuthorizationActivity.this, Manifest.permission.CAMERA)

                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                }
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }

    }

    public void SortData() {
        Collections.sort(contractorListActivityArrayList, Collections.reverseOrder());


        Collections.sort(contractorListActivityArrayList, new Comparator<ContractorList>() {
            @Override
            public int compare(ContractorList o1, ContractorList o2) {
                return o1.getCustVendorName().compareTo(o2.getCustVendorName());
            }

        });

    }

    /*public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            document = new PdfDocument();



            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);

            Canvas canvas = page.getCanvas();

            Paint paint = new Paint();
            canvas.drawPaint(paint);

            bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap, 0, 0 , null);
            document.finishPage(page);

            // write the document content
            String targetPdf = "/sdcard/pdffromScroll.pdf";
            File filePath;
            filePath = new File(targetPdf);
            try {
                document.writeTo(new FileOutputStream(filePath));

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
            }

            // close the document
            document.close();
            Toast.makeText(this, "PDF of Scroll is created!!!", Toast.LENGTH_SHORT).show();
        }


        openGeneratedPDF();

    }

    private void openGeneratedPDF(){
        File file = new File("/sdcard/pdffromScroll.pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(WorkAuthorizationActivity.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }*/


    /******************************Not Used**********************************/

    public void handleSendImage(Intent intent) throws IOException {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            File file = new File(getCacheDir(), "image");
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            try {

                OutputStream output = new FileOutputStream(file);
                try {
                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
                    int read;

                    while ((read = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }

                    output.flush();
                } finally {
                    output.close();
                }
            } finally {
                inputStream.close();
                byte[] bytes = getFileFromPath(file);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmapToUriConverter(bitmap);
                //Upload Bytes.
            }
        }
    }

    public static byte[] getFileFromPath(File file) {
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }


    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;


        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 100, 100);
            int w = mBitmap.getWidth();
            int h = mBitmap.getHeight();
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, w, h,
                    true);
            String path1 = Environment.getExternalStorageDirectory()
                    .toString();
            File file = new File(path1 + "/" + "Vwb" + "/" + "Sender");
            if (!file.exists())
                file.mkdirs();
            File file1 = new File(file, "Image-" + new Random().nextInt() + ".jpg");
            if (file1.exists())
                file1.delete();
           /* File file = new File(SharefunctionActivity.this.getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");*/
            FileOutputStream out = new FileOutputStream(file1);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
            out.flush();
            out.close();
            //get absolute path
            path = file1.getAbsolutePath();
            File f = new File(path);
            if (temp.equals("1")) {
                Isolatedimg = f.getName();
            } else if (temp.equals("2")) {
                Electricimg = f.getName();
            } else {
                Imagefilename = f.getName();
            }
            uri = Uri.fromFile(f);
//file:///data/data/vworkbench7.vritti.com.vworkbench7/files/Image1825476171.jpeg


        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return uri;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    private String getyyyymmdd(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        String ghe = "";
        try {
            date = dateFormat1.parse(data);
            ghe = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ghe;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                } else {
                    Log.e("Permission", "Denied");
                }
                break;
            }
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                }
                break;
            case 201:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public String timeStampConversion(String str_date) {

        // String str_date="13-09-2011";
        Calendar cal = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = (Date) formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = date.getTime() / 1000L;

        int val = 60 * 60 * 24 * 6;
        time = time + val;

        //  DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy")
        new SimpleDateFormat("dd-MM-yyyy").format(time * 1000L);
        cal.setTimeInMillis(time);
        String dateString = new SimpleDateFormat("dd-MM-yyyy").format(time * 1000L);

        return dateString;
    }


    public boolean dateDifference(String selectedDay, String startDateVal) {

        boolean flag = false;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
           /* Date startDate = simpleDateFormat.parse(selectedDay);
            Date endDate = simpleDateFormat.parse(startDateVal);*/
            Date startDate = simpleDateFormat.parse(startDateVal);
            Date endDate = simpleDateFormat.parse(selectedDay);

            long diff = endDate.getTime() - startDate.getTime();

            int i = (int) (diff / (1000 * 60 * 60 * 24));

            if (i >= 0 && i < 7) {
                return true;
            } else {

                return false;
            }

           /* long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    private class DownloadChildPermitDetails extends AsyncTask<String, Void, String> {

        String response = "";
        String res = "";
        String permitno = "", obj = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            permitno = strings[0];
            obj = strings[1];
            String url = CompanyURL + WebAPIUrl.api_ChildPermitDetails + "?PermitNo=" + permitno;

            Log.d("URL", url);

            try {
                res = CommonClass.OpenConnection(url, WorkAuthorizationActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    Log.d("URL", response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<String> stringArrayList = new ArrayList<>();
            if (response != null) {
                /*[{"ChildPermit":""},{"ChildPermit":"CS/0013"},{"ChildPermit":"HW/0019"}]*/
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (!jsonObject.getString("ChildPermit").equals("")) {
                            stringArrayList.add(jsonObject.getString("ChildPermit"));
                        }

                    }

                    if (stringArrayList.size() != 0) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(WorkAuthorizationActivity.this);
                        LayoutInflater inflater = getLayoutInflater().from(WorkAuthorizationActivity.this);
                        View dialogView = null;


                        dialogView = inflater.inflate(R.layout.complete_permit_details_main, null);
                        list_permitDetails = dialogView.findViewById(R.id.list_permitDetails1);
                        btn_cancel = dialogView.findViewById(R.id.btn_cancel);

                        builder.setView(dialogView);


                        builder.setCancelable(false);
                        final android.app.AlertDialog b = builder.create();
                        b.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        if (!WorkAuthorizationActivity.this.isFinishing()) {
                            b.show();
                            //show dialog
                        }


                        //  ln_completePermitDetails.setVisibility(View.VISIBLE);
                        completePermitDetailsAdapter = new CompletePermitDetailsAdapter(WorkAuthorizationActivity.this, stringArrayList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        list_permitDetails.setLayoutManager(mLayoutManager);
                        list_permitDetails.setItemAnimator(new DefaultItemAnimator());
                        list_permitDetails.setAdapter(completePermitDetailsAdapter);

                        b.show();
                        setFinishOnTouchOutside(true);
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                b.dismiss();
                            }
                        });

                        list_permitDetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

                    } else {
                        if (CommonClass.checkNet(WorkAuthorizationActivity.this)) {
                            showProgress();
                            final String finalFinalEditJsonObject = obj;
                            new StartSession(WorkAuthorizationActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadEditPostData().execute(finalFinalEditJsonObject);
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }

    private class DownloadAuthorizedPersonDataDepo extends AsyncTask<String, Void, String> {


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
            String id = params[0];
            String url = CompanyURL + WebAPIUrl.api_GetUserListByDepo + "?DepoId=" + id;

            try {
                res = CommonClass.OpenConnection(url, WorkAuthorizationActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    authorizedPersonArrayList = new ArrayList<>();
                    authorizedPersonArrayList.clear();
                    AuthorizedPerson authorizedPerson = new AuthorizedPerson();
                    authorizedPerson.setAuthorizeid("--Select--");
                    authorizedPerson.setAuthorizename("--Select--");
                    authorizedPersonArrayList.add(0, authorizedPerson);

                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        authorizedPerson = new AuthorizedPerson();
                        JSONObject jorder = jResults.getJSONObject(i);
                        authorizedPerson.setAuthorizeid(jorder.getString("UserMasterId"));
                        authorizedPerson.setAuthorizename(jorder.getString("UserName"));
                        authorizedPerson.setCategorydesc(jorder.getString("CategoryDesc"));
                        authorizedPerson.setUserLoginId(jorder.getString("UserLoginId"));
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

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {
                dismissProgress();
                //  Toast.makeText(HOTWorkActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
             /*   SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(authorizedPersonArrayList);
                editor.putString("Authorized", json);
                editor.commit();*/
                authorizedPersonAdapter = new AuthorizedPersonAdapter(WorkAuthorizationActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);


            }


        }
    }

    private class GetWAImages extends AsyncTask<String, Void, String> {

        String res = "", response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String url = CompanyURL + WebAPIUrl.api_GetWAImages + "?PermitNo=" + id;
            try {
                res = CommonClass.OpenConnection(url, WorkAuthorizationActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                }else{
                    response="error";
                }


            } catch (Exception e) {
                e.printStackTrace();
                response="error";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!response.equals("error")){
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Isolatedimgpost = jsonObject.getString("IsolatedImageName");
                        Electricimgpost = jsonObject.getString("ElectricalImageName");
                        Spotimgpost = jsonObject.getString("SpotImage");

                        String  image="http://z207.ekatm.co.in/Attachments/View%20Attachment/"+Spotimgpost;
                        img_camera.setVisibility(View.VISIBLE);
                        img_display.setVisibility(View.VISIBLE);
                        img_display.setImageURI(image);
                        if(installationPreparationArrayList.size() != 0){
                            installationPreparationArrayList.get(0).setiImg(Isolatedimgpost);
                            installationPreparationArrayList.get(1).seteImg(Electricimgpost);

                           // installationPreparationArrayList.get(img_pos).setIsolatedImageName(electricalAbsolutePath);
                            //installationPreparationArrayList.get(img_pos).seteImg(Electricimgpost);
                            installationListAdapter.updateEditList(installationPreparationArrayList,"DisplayEditImage");
                        }





                        /*"IsolatedImagepath": "/Attachments/View Attachment/20210603_153123.jpg",
	"ElectricalImagepath": "/Attachments/View Attachment/20210603_153129.jpg",
	"SpotImagepath": "/Attachments/View Attachment/20210603_153138.jpg"*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }
    }

 /*   @Override
    public void onBackPressed() {

        if (ln_completePermitDetails.getVisibility() == View.VISIBLE) {
            ln_completePermitDetails.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }*/
}



