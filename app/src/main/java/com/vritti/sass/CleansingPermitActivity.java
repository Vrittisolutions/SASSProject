package com.vritti.sass;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.KeyListener;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.adapter.AuthorizedPersonAdapter;
import com.vritti.sass.adapter.ContractorPermitAdapter;
import com.vritti.sass.adapter.DepotAdapter;
import com.vritti.sass.adapter.GenerlConditionAdapter;
import com.vritti.sass.adapter.GoldenRuleAdapter;
import com.vritti.sass.adapter.LocationOperationAdapter;
import com.vritti.sass.adapter.OperationAdapter;
import com.vritti.sass.adapter.PermitContractorListAdapter;
import com.vritti.sass.adapter.SafetyAdapter;
import com.vritti.sass.model.AuthorizedPerson;
import com.vritti.sass.model.Cleansing;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.ContractorList;
import com.vritti.sass.model.DateFormatChange;
import com.vritti.sass.model.Depot;
import com.vritti.sass.model.GoldenRules;
import com.vritti.sass.model.IndicateRisk;
import com.vritti.sass.model.Location;
import com.vritti.sass.model.Operation;
import com.vritti.sass.model.Permit;
import com.vritti.sass.model.PermitNoWA;
import com.vritti.sass.model.SafetyTools;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.UpdateTime;
import com.vritti.sass.model.Utility;
import com.vritti.sass.model.WebAPIUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sharvari on 23-Nov-18.
 */

public class CleansingPermitActivity extends AppCompatActivity {

    ListView list_cleansing;
    Button btn_fromdate, btn_todate, btn_cleansing_date, btn_cleansing_date1, btn_cleansing_date2, btn_cleansing_date3, btn_cleansing_date4, btn_cleansing_date5,
            btn_cleansing_date6, btn_cleansing_date7, btn_cleansing_date8, btn_cleansing_date9, btn_submit, btn_fromtime, btn_totime, btn_cancel_date;
    Spinner spinner_station, spinner_confined_space, spinner_contractor, spinner_operation, spinner_authorize, spinner_authorize1,
            spinner_authorize2, spinner_authorize3, spinner_authorize4, spinner_permit_closed, spinner_spotcheck, spinner_authorize5, spinner_authorize6,
            spinner_authorize7, spinner_authorize8, spinner_authorize9, spinner_authorize10, spinner_prevention_plan;
    EditText edit_permitno;
    JSONObject ActivityJson;
    String Password = "", to_time = "";
    String StartDate = "", fromtime = "", totime = "", Result1 = "", Result2 = "", Result3 = "", Phase2_Top1 = "", PermitLClosedDate = "", SpotcheckDate = "", cancelDate = "",
            Remarks = "", Centre1 = "", Percentage1 = "", Percentage3 = "", Bottom_per1 = "", CleansingDate1 = "", Phase2_Top2 = "",
            Centre2 = "", Percentage2 = "", Percentage4 = "", Bottom_per2 = "", CleansingDate2 = "", Toxic_Top1 = "", Gasname1 = "",
            Toxic_centre = "", Gasname2 = "", Toxic_per1 = "", Gasname3 = "", Toxic_per2 = "", Gasname4 = "", Toxic_bottom = "",
            Gasname5 = "", CleansingDate3 = "", AuthorizeDate3 = "", AuthorizeDate4 = "", AuthorizeDate5 = "", AuthorizeDate6 = "", AuthorizeDate7 = "";

    String response = "", response1 = "";
    DatePickerDialog datePickerDialog;
    int Year, month, day;
    String date;
    CheckBox checkbox_phase2, checkbox_phase1;


    ArrayList<IndicateRisk> cleansingArrayList;
    GenerlConditionAdapter generlConditionAdapter;
    ImageView img_camera;
    int MY_CAMERA_PERMISSION_CODE = 100;
    int MEDIA_TYPE_IMAGE = 1;
    int CAMERA_REQUEST = 101;
    private Uri fileUri;
    File mediaFile;
    private static String IMAGE_DIRECTORY_NAME = "SASS";


    SharedPreferences userpreferences;
    private ArrayList<AuthorizedPerson> authorizedPersonArrayList;
    private ArrayList<AuthorizedPerson> txt_authorizeArrayList;
    private ArrayList<ContractorList> contractorListActivityArrayList;
    private ArrayList<Operation> operationArrayList;
    private ArrayList<Location> LocationArraylist;
    AuthorizedPersonAdapter authorizedPersonAdapter;
    PermitContractorListAdapter permitContractorListAdapter;
    OperationAdapter operationAdapter;
    private ArrayList<Depot> depotArrayList;
    LocationOperationAdapter locationOperationAdapter;
    DepotAdapter depotAdapter;

    String CompanyURL;
    ProgressBar mprogress;
    SharedPreferences sharedPrefs;
    Gson gson;
    String json;
    Type type;
    String PKFormId = "", formcode = "", authorize = "", authorize1 = "", authorize2 = "", authorize3 = "", authorize4 = "",
            authorize5 = "", authorize6 = "", authorize7 = "", authorize8 = "",
            authorize9 = "", authorize10 = "", PermitClosed = "", SpotCheck = "", userLoginId = "",
            StationId = "", PermitNo = "", StationName = "", OperationName = "", LocationName = "", contractorName = "", categoryDesc = "";
    int check = 0;
    private String serverResponseMessage, path, Imagefilename;

    GridView grid_safety;
    SafetyTools safetyTools;
    ArrayList<SafetyTools> safetyToolsArrayList;
    SafetyAdapter safetyAdapter;
    boolean isAns;

    EditText edit_result1, edit_result2, edit_result3, edit_phase2_top1, edit_phase2_centre1, edit_per1, edit_per3, edit_bottom1, edit_phase2_top2, edit_phase2_centre2, edit_per2, edit_per4, edit_bottom2, edit_toxic_top, edit_gasname1, edit_toxic_centre, edit_gasname2, edit_toxic_per1, edit_gasname3,
            edit_toxic_per2, edit_gasname4, edit_toxic_bottom, edit_gasname5, edit_remark;

    String contractorId = "", OperationId = "", locationId = "", WAH_No = "";
    String[] user;
    String[] user1;
    String[] user5;
    String entry_condtn = "", safetytools = "", method_of_operation = "Y", Phase1 = "N", Phase2 = "N";
    CheckBox checkbox_method;
    AuthorizedPersonAdapter authorizedPersonAdapter1;
    TextView txt_authorize, txt_authorize1, txt_authorize3, txt_authorize4, txt_authorize5, txt_authorize6, txt_authorize8, txt_authorize7, txt_authorize9,
            txt_authorize10, txt_permitclosed, txt_spotcheck, txt_cancel, txt_cancel_permit;

    AlertDialog b;
    String safety_Others = "";

    RelativeLayout cusdialog1, cusdialog2;

    EditText edit_password_pass;
    LinearLayout ln_spinner_authorize, ln_spinner_reason, len_cancel_permit;
    Button btn_submit_pass, btn_cancel_pass;
    String tempVal = "", ReasonVal = "";
    EditText edit_reason, edit_password_reason;
    Button btn_submit_reason, btn_cancel_reason;
    String AuthorizedId = "", Authorized1Id = "", Authorized3Id = "", Authorized4Id = "", Authorized5Id = "", Authorized6Id = "",
            Authorized7Id = "", Authorized8Id = "", Authorized9Id = "", Authorized10Id = "";
    String PermitClosedId = "", SpotCheckId = "", cancelId = "";
    Permit permit;
    String Mode = "";

    LinearLayout len_p_closed;
    TextView tx_p_closed;
    private String PermitStatus;
    private ArrayList<PermitNoWA> WAArayList = new ArrayList<>();
    LinearLayout ln_station, ln_contractor, ln_natureOperation, ln_locationOperation, len_prevention, ln_authorize8,
            ln_authorize5, ln_authorize7, ln_authorize6, ln_authorize9;
    RecyclerView list_goldenRules;
    ArrayList<GoldenRules> goldenRulesArrayList;
    GoldenRuleAdapter goldenRuleAdapter;
    GoldenRules goldenRules;
    String goldenRulesList = "";
    String WAStartTime = "", WAEndTime = "", WAEndTime1 = "";
    int WAEndTimeHr, WAEndTimeMin, WAStartTimeHr, WAStartTimemin;
    KeyListener variable1, variable2, variable3, variable4, variable5, variable6, variable7, variable8, variable9, variable10, variable11, variable12, variable13;
    int modeefirst = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cleansing_degassing_lay);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        toolbar.setTitle(getResources().getString(R.string.application_name));
        setSupportActionBar(toolbar);


        initview();
        setListner();
        dateListener();

        Intent intent = getIntent();


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        if (getIntent() != null) {
            permit = new Gson().fromJson(getIntent().getStringExtra("permitno"), Permit.class);
            if (permit != null) {
                PermitStatus = permit.getWorkAuthorizationstatus();

                PermitStatus = permit.getWorkAuthorizationstatus();

                if (!PermitStatus.equalsIgnoreCase("P")) {

                    spinner_station.setEnabled(false);
                    spinner_contractor.setEnabled(false);
                    spinner_prevention_plan.setEnabled(false);
                    btn_fromdate.setEnabled(false);
                    btn_fromtime.setEnabled(false);
                    btn_totime.setEnabled(false);
                    spinner_contractor.setEnabled(false);
                    spinner_operation.setEnabled(false);
                    spinner_confined_space.setEnabled(false);
                    edit_result1.setKeyListener(null);
                    edit_result2.setKeyListener(null);
                    edit_result3.setKeyListener(null);
                    txt_authorize5.setEnabled(false);
                    txt_authorize6.setEnabled(false);
                    txt_authorize7.setEnabled(false);
                    edit_phase2_top1.setKeyListener(null);
                    edit_phase2_centre1.setKeyListener(null);
                    checkbox_phase1.setClickable(false);
                    checkbox_phase2.setClickable(false);

                    edit_per1.setKeyListener(null);
                    edit_per3.setKeyListener(null);
                    edit_bottom1.setKeyListener(null);
                    btn_cleansing_date.setEnabled(false);
                    txt_authorize8.setEnabled(false);

                    edit_phase2_top2.setKeyListener(null);
                    edit_phase2_centre2.setKeyListener(null);
                    edit_per2.setKeyListener(null);
                    edit_per4.setKeyListener(null);
                    edit_bottom2.setKeyListener(null);
                    btn_cleansing_date1.setEnabled(false);
                    txt_authorize9.setEnabled(false);

                    edit_toxic_top.setKeyListener(null);
                    edit_gasname1.setKeyListener(null);
                    edit_toxic_centre.setKeyListener(null);
                    edit_gasname2.setKeyListener(null);
                    edit_toxic_per1.setKeyListener(null);
                    edit_gasname3.setKeyListener(null);
                    edit_toxic_per2.setKeyListener(null);
                    edit_gasname4.setKeyListener(null);
                    edit_toxic_bottom.setKeyListener(null);
                    edit_gasname5.setKeyListener(null);
                    btn_cleansing_date2.setEnabled(false);
                    txt_authorize10.setEnabled(false);

                    txt_authorize.setEnabled(false);
                    btn_cleansing_date3.setEnabled(false);
                    txt_authorize1.setEnabled(false);
                    btn_cleansing_date4.setEnabled(false);
                    spinner_authorize2.setEnabled(false);
                    btn_cleansing_date5.setEnabled(false);
                    txt_authorize3.setEnabled(false);
                    btn_cleansing_date6.setEnabled(false);
                    txt_authorize4.setEnabled(false);
                    btn_cleansing_date7.setEnabled(false);

                    checkbox_method.setClickable(false);
                    edit_remark.setKeyListener(null);
                    btn_fromtime.setTextColor(Color.parseColor("#000000"));
                    btn_cleansing_date3.setTextColor(Color.parseColor("#000000"));
                    btn_cleansing_date.setTextColor(Color.parseColor("#000000"));
                    btn_cleansing_date1.setTextColor(Color.parseColor("#000000"));
                    btn_cleansing_date2.setTextColor(Color.parseColor("#000000"));
                    btn_cleansing_date4.setTextColor(Color.parseColor("#000000"));
                    btn_cleansing_date5.setTextColor(Color.parseColor("#000000"));
                    btn_cleansing_date6.setTextColor(Color.parseColor("#000000"));
                    btn_cleansing_date7.setTextColor(Color.parseColor("#000000"));

                    if (PermitStatus.equalsIgnoreCase("R") || PermitStatus.equalsIgnoreCase("C")) {
                        txt_spotcheck.setEnabled(false);
                        txt_cancel.setEnabled(false);
                        btn_cleansing_date9.setEnabled(false);
                        btn_cleansing_date9.setTextColor(Color.parseColor("#000000"));
                        txt_permitclosed.setEnabled(false);
                        btn_cleansing_date9.setEnabled(false);
                        btn_cleansing_date9.setTextColor(Color.parseColor("#000000"));
                        tx_p_closed.setKeyListener(null);
                        txt_cancel_permit.setKeyListener(null);
                        btn_cancel_date.setKeyListener(null);
                        btn_cancel_date.setTextColor(Color.parseColor("#000000"));
                        btn_cleansing_date8.setEnabled(false);
                        btn_cleansing_date8.setTextColor(Color.parseColor("#000000"));
                        btn_submit.setClickable(false);
                    }


                }
                else{ btn_fromdate.setEnabled(true);}

               // if (PermitStatus.equals("A") || PermitStatus.equals("P")) {
                    len_p_closed.setVisibility(View.VISIBLE);
                    tx_p_closed.setVisibility(View.VISIBLE);
                    len_cancel_permit.setVisibility(View.VISIBLE);
                    txt_cancel_permit.setVisibility(View.VISIBLE);
               /* } else {
                    len_p_closed.setVisibility(View.GONE);
                    tx_p_closed.setVisibility(View.GONE);
                    len_cancel_permit.setVisibility(View.GONE);
                    txt_cancel_permit.setVisibility(View.GONE);
                }*/

            //    btn_fromdate.setEnabled(false);
                btn_fromdate.setTextColor(Color.parseColor("#101010"));
                Mode = "E";
                PermitNo = permit.getPermitNo();
                PKFormId = permit.getPermitId();
                if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                    new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new getDetails().execute(PermitNo);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            } else {
                Mode = "A";
                PKFormId = intent.getStringExtra("PKFormId");
                formcode = intent.getStringExtra("formcode");

                //GetPermit NO

                if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                    showProgress();
                    new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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


        cleansingArrayList = new ArrayList<>();
        goldenRulesArrayList = new ArrayList<>();

        userpreferences = getSharedPreferences(LoginActivity.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", "");

        contractorListActivityArrayList = new ArrayList<>();
        authorizedPersonArrayList = new ArrayList<>();
        operationArrayList = new ArrayList<>();
        LocationArraylist = new ArrayList<>();
        depotArrayList = new ArrayList<>();
        safetyToolsArrayList = new ArrayList<>();
        txt_authorizeArrayList = new ArrayList<>();


        final Calendar c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = (c.get(Calendar.DAY_OF_MONTH));

        date = day + "-"
                + String.format("%02d", (month + 1))
                + "-" + Year;
        date =  DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                + String.format("%02d", (month + 1))
                + "-" + Year);
        // btn_todate.setText(date);
        btn_fromdate.setText(date);
        btn_cleansing_date.setText(date);
        btn_cleansing_date1.setText(date);
        btn_cleansing_date2.setText(date);
        btn_cleansing_date3.setText(date);
        btn_cleansing_date4.setText(date);
        btn_cleansing_date5.setText(date);
        btn_cleansing_date6.setText(date);
        btn_cleansing_date7.setText(date);
        btn_cleansing_date8.setText(date);
        btn_cleansing_date9.setText(date);
        btn_cancel_date.setText(date);

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        int hour1 = hour + 4;
        final String time1 = UpdateTime.updateTime(hour, minute);
        to_time = UpdateTime.updateTime(hour1, minute);
        System.out.println("time: " + time1);

        btn_fromtime.setText(time1);
        btn_totime.setText(to_time);


        // Safety Tools
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("safety", "");
        type = new TypeToken<List<SafetyTools>>() {
        }.getType();
        safetyToolsArrayList = gson.fromJson(json, type);

        if (safetyToolsArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                showProgress();
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadSafetyToolsData().execute();
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
            if (safetyToolsArrayList.size() > 0) {
                safetyAdapter = new SafetyAdapter(CleansingPermitActivity.this, safetyToolsArrayList, "CAD", Mode, PermitStatus);
                grid_safety.setAdapter(safetyAdapter);


            }

        }


        // Depot Station
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Depot", "");
        type = new TypeToken<List<Depot>>() {
        }.getType();
        depotArrayList = gson.fromJson(json, type);

        if (depotArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                showProgress();
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadDepotData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(CleansingPermitActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (depotArrayList.size() > 0) {
                depotAdapter = new DepotAdapter(CleansingPermitActivity.this, depotArrayList);
                spinner_station.setAdapter(depotAdapter);


            }

        }

        //General Condition

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        String General_Condition = sharedPrefs.getString("general_condition", "");

        Type General = new TypeToken<List<IndicateRisk>>() {
        }.getType();
        cleansingArrayList = gson.fromJson(General_Condition, General);

        if (cleansingArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                showProgress();
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadGeneralCOndition().execute();
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
            if (cleansingArrayList.size() > 0) {
                generlConditionAdapter = new GenerlConditionAdapter(CleansingPermitActivity.this, cleansingArrayList, Mode, PermitStatus);
                list_cleansing.setAdapter(generlConditionAdapter);
                Utility.setListViewHeightBasedOnItems(list_cleansing);


            }

        }

        // ContractorList

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Contractor", "");
        type = new TypeToken<List<ContractorList>>() {
        }.getType();
        contractorListActivityArrayList = gson.fromJson(json, type);

        if (contractorListActivityArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                showProgress();
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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
                permitContractorListAdapter = new PermitContractorListAdapter(CleansingPermitActivity.this, contractorListActivityArrayList);
                spinner_authorize2.setAdapter(permitContractorListAdapter);
                spinner_contractor.setAdapter(permitContractorListAdapter);

            }

        }


        // Operation

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Operation", "");
        type = new TypeToken<List<Operation>>() {
        }.getType();
        operationArrayList = gson.fromJson(json, type);

        if (operationArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                showProgress();
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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
                operationAdapter = new OperationAdapter(CleansingPermitActivity.this, operationArrayList);
                spinner_operation.setAdapter(operationAdapter);
            }

        }

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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
            if (authorizedPersonArrayList.size() > 0) {
                authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);
                //authorizedPersonAdapter1 = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);


                ArrayAdapter<AuthorizedPerson> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);

            }

        }

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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
            if (authorizedPersonArrayList.size() > 0) {
                authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);
                //authorizedPersonAdapter1 = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);


                ArrayAdapter<AuthorizedPerson> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);

            }

        }


        // Golden Rules
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("GoldenRules", "");
        type = new TypeToken<List<GoldenRules>>() {
        }.getType();
        goldenRulesArrayList = gson.fromJson(json, type);

        if (goldenRulesArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                showProgress();
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadGoldenRules().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(CleansingPermitActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (goldenRulesArrayList.size() > 0) {

                goldenRuleAdapter = new GoldenRuleAdapter(CleansingPermitActivity.this, goldenRulesArrayList, Mode, PermitStatus);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CleansingPermitActivity.this);

                // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                list_goldenRules.setLayoutManager(linearLayoutManager);
                list_goldenRules.setAdapter(goldenRuleAdapter);


            }

        }


    }


    public void reasonDialog_SafetyTools(final int position, String SafetyToolMasterId) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CleansingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater().from(CleansingPermitActivity.this);
        final View dialogView = inflater.inflate(R.layout.remarks, null);
        builder.setView(dialogView);
        final EditText edit_remark = dialogView.findViewById(R.id.edit_remarks);
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);


        builder.setCancelable(false);
        final android.app.AlertDialog b = builder.create();
        b.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        if (safetyToolsArrayList != null) {
            if (safetyToolsArrayList.get(position).getRemarks() != null) {
                String remarks = safetyToolsArrayList.get(position).getRemarks();
                edit_remark.setText(remarks);
                safetyToolsArrayList.get(position).isSelected();
            }
        }


        b.show();
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remarks = edit_remark.getText().toString();
                if (remarks.equals("")) {
                    safetyToolsArrayList.get(position).setSelected(false);
                    safetyToolsArrayList.get(position).setRemarks(remarks);
                    edit_remark.setText(remarks);

                } else {
                    safetyToolsArrayList.get(position).setSelected(true);
                    safetyToolsArrayList.get(position).setRemarks(remarks);
                    edit_remark.setText(remarks);
                    //safetyAdapter.updateList(safetyToolsArrayList);
                }
                safetyAdapter.updateList(safetyToolsArrayList);
                b.dismiss();
            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  safetyToolsArrayList.get(position).setSelected(false);
                safetyToolsArrayList.get(position).setRemarks("");
                safetyAdapter.updateList(safetyToolsArrayList);*/

                if (safetyToolsArrayList.get(position).getRemarks() == null ||
                        safetyToolsArrayList.get(position).getRemarks().equalsIgnoreCase("")) {
                    if (isAns) {
                        // holder.checkbox_user.setChecked(false);
                        safetyToolsArrayList.get(position).setSelected(false);
                        safetyToolsArrayList.get(position).setRemarks("");
                        safetyAdapter.updateList(safetyToolsArrayList);

                    } else {
                        //  holder.checkbox_user.setChecked(true);
                        if (safetyToolsArrayList.size() != 0) {

                            if (safetyToolsArrayList.get(position).getRemarks() != null) {
                                if (safetyToolsArrayList.get(position).getRemarks().equalsIgnoreCase("")) {
                                    safetyToolsArrayList.get(position).setSelected(false);
                                    safetyToolsArrayList.get(position).setRemarks("");
                                    safetyAdapter.updateList(safetyToolsArrayList);
                                } else {
                                    safetyToolsArrayList.get(position).setSelected(true);
                                    safetyToolsArrayList.get(position).setRemarks(edit_remark.getText().toString());
                                    safetyAdapter.updateList(safetyToolsArrayList);
                                }
                            } else {
                                safetyToolsArrayList.get(position).setSelected(false);
                                safetyToolsArrayList.get(position).setRemarks("");
                                safetyAdapter.updateList(safetyToolsArrayList);
                            }
                        }
                    }

                } else {
                    // holder.checkbox_user.setChecked(true);
                    safetyToolsArrayList.get(position).setSelected(true);
                    safetyAdapter.updateList(safetyToolsArrayList);
                    isAns = false;
                }


                b.dismiss();
            }
        });

        edit_remark.addTextChangedListener(new TextWatcher() {
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

    class getDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response = "";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CleansingPermitActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setTitle("Data fetching...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_getHWDetails + "?form=" + PermitNo;

            try {
                res = CommonClass.OpenConnection(url, CleansingPermitActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            progressDialog.dismiss();
            if (!integer.equalsIgnoreCase("") || !integer.equalsIgnoreCase(null)) {
                try {
                    JSONArray jResults = new JSONArray(integer);
                    for (int i = 0; i < jResults.length(); i++) {
                        Permit permit = new Permit();
                        JSONObject jorder = jResults.getJSONObject(i);

                        //contractorlist,depostation , Operation , PErmitno

                        //  String HotWorkPermitStatus=jorder.getString("HotworkpermitStatus");
                        //  String HotWorkPermiId = jorder.getString("HotWorkPermitMasterId");
                        PermitNo = jorder.getString("PermitNo");
                        edit_permitno.setText(PermitNo);

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
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CleansingPermitActivity.this);

                            // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            list_goldenRules.setLayoutManager(linearLayoutManager);
                            goldenRuleAdapter = new GoldenRuleAdapter();
                            goldenRuleAdapter.updateList(goldenRulesArrayList, Mode, PermitStatus);
                            list_goldenRules.setAdapter(goldenRuleAdapter);


                        } else {
                            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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


                        StationId = jorder.getString("FkWareHouseMasterId");
                        StationName = jorder.getString("WarehouseDescription");
                        int stationpos = -1;
                        for (int j = 0; j < depotArrayList.size(); j++) {
                            if (depotArrayList.get(j).getDepotid().equals(StationId))
                                stationpos = j;
                        }
                        if (stationpos != -1)
                            spinner_station.setSelection(stationpos);
                        else
                            spinner_station.setSelection(0);

                        locationId = jorder.getString("FkLocationMasterId");

                        contractorId = jorder.getString("fkContractorId");
                        contractorName = jorder.getString("CustVendorName");
                        int contractorpos = -1;
                        for (int j = 0; j < contractorListActivityArrayList.size(); j++) {
                            if (contractorListActivityArrayList.get(j).getCustVendorMasterId().equals(contractorId))
                                contractorpos = j;
                        }
                        if (contractorpos != -1) {
                            spinner_contractor.setSelection(contractorpos);
                            spinner_authorize2.setSelection(contractorpos);
                        } else {
                            spinner_contractor.setSelection(0);
                            spinner_authorize2.setSelection(0);
                        }

                        OperationId = jorder.getString("fkOperationMasterId");
                        OperationName = jorder.getString("Operation");
                        int operationpos = -1;
                        for (int j = 0; j < operationArrayList.size(); j++) {
                            if (operationArrayList.get(j).getOperationMasterId().equals(OperationId))
                                operationpos = j;
                        }
                        if (operationpos != -1)
                            spinner_operation.setSelection(operationpos);
                        else
                            spinner_operation.setSelection(0);


                        //   WAH_No = jorder.getString("FkWorkAuthorizationMasterId");
                        int WAHNo = -1;

                        WAH_No = jorder.getString("FkWorkAuthorizationMasterId");
                        if (WAArayList.size() != 0) {
                            for (int z = 0; z < WAArayList.size(); z++) {
                                if (WAH_No.equals(WAArayList.get(z).getPermitNo())) {
                                    WAHNo = z;
                                }
                            }

                            if (WAHNo != -1) {
                                spinner_prevention_plan.setSelection(WAHNo);
                            } else {
                                spinner_prevention_plan.setSelection(0);
                            }
                        } else {
                            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new DownloadWANo().execute(contractorId);
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {

                                    }
                                });
                            }
                        }


                        StartDate = jorder.getString("PermitDate");
                        btn_fromdate.setText(StartDate);

                        fromtime = jorder.getString("PermitFromTime");
                        btn_fromtime.setText(fromtime);

                        totime = jorder.getString("PermitToTime");
                        btn_totime.setText(totime);


                        //general condtn condition
                        entry_condtn = jorder.getString("entry_condtn");
                        String[] gencondtn1 = new String[entry_condtn.length()];
                        gencondtn1 = entry_condtn.split(",");
                        for (int j = 0; j < cleansingArrayList.size(); j++) {
                            for (int k = 0; k < gencondtn1.length; k++) {
                                if (gencondtn1[k].equals(cleansingArrayList.get(j).getPKQuesID())) {
                                    int pos = j;
                                    cleansingArrayList.get(pos).setSelected(true);
                                    generlConditionAdapter = new GenerlConditionAdapter(CleansingPermitActivity.this, cleansingArrayList, Mode, PermitStatus);
                                    list_cleansing.setAdapter(generlConditionAdapter);

                                }


                            }
                        }

                        Result1 = jorder.getString("Meterscreenresult");
                        if (!Result1.equalsIgnoreCase(""))
                            edit_result1.setText(Result1);
                        else
                            edit_result1.setText("");

                        Result2 = jorder.getString("Airscreenresult");
                        if (!Result2.equalsIgnoreCase(""))
                            edit_result2.setText(Result2);
                        else
                            edit_result2.setText("");

                        Result3 = jorder.getString("Toxicgasesresult");
                        if (!Result3.equalsIgnoreCase(""))
                            edit_result3.setText(Result3);
                        else
                            edit_result3.setText("");


                        Authorized5Id = jorder.getString("MeterscreenresultSignature");
                        // Authorize1Name = jorder.getString("");
                        int authorize5pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(Authorized5Id))
                                authorize5pos = j;
                        }

                        if (authorize5pos != -1)
                            txt_authorize5.setText(authorizedPersonArrayList.get(authorize5pos).getAuthorizename());
                        else
                            txt_authorize5.setText("Select");

                        Authorized6Id = jorder.getString("AirscreenresultSignature");
                        int authorize6pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(Authorized6Id))
                                authorize6pos = j;
                        }
                        if (authorize6pos != -1)
                            txt_authorize6.setText(authorizedPersonArrayList.get(authorize6pos).getAuthorizename());
                        else
                            txt_authorize6.setText("Select");

                        Authorized7Id = jorder.getString("ToxicgasesresultSignature");
                        int authorize7pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (Authorized7Id.equals(authorizedPersonArrayList.get(j).getAuthorizeid()))
                                authorize7pos = j;
                        }
                        if (authorize7pos != -1)
                            txt_authorize7.setText(authorizedPersonArrayList.get(authorize7pos).getAuthorizename());
                        else
                            txt_authorize7.setText("Select");

                        //Phase2

                        //Flammable/Explosive Gas Measurement
                        Phase2_Top1 = jorder.getString("FlammableTop");
                        if (!Phase2_Top1.equalsIgnoreCase(""))
                            edit_phase2_top1.setText(Phase2_Top1);
                        else
                            edit_phase2_top1.setText("");

                        Centre1 = jorder.getString("FlammableCentre");
                        if (!Centre1.equalsIgnoreCase(""))
                            edit_phase2_centre1.setText(Centre1);
                        else
                            edit_phase2_centre1.setText("");


                        Percentage1 = jorder.getString("FlammableManholepoint");
                        if (!Percentage1.equalsIgnoreCase(""))
                            edit_per1.setText(Percentage1);
                        else
                            edit_per1.setText("");

                        Percentage3 = jorder.getString("FlammableExtractoroutputpoint");
                        if (!Percentage3.equalsIgnoreCase(""))
                            edit_per3.setText(Percentage3);
                        else edit_per3.setText("");

                        Bottom_per1 = jorder.getString("FlammableBottom");
                        if (!Bottom_per1.equalsIgnoreCase(""))
                            edit_bottom1.setText(Bottom_per1);
                        else
                            edit_bottom1.setText("");

                        Authorized8Id = jorder.getString("FlammableSignature");
                        int authorize8pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (Authorized8Id.equals(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                authorize8pos = j;
                                break;
                            }
                        }
                        if (authorize8pos != -1)
                            txt_authorize8.setText(authorizedPersonArrayList.get(authorize8pos).getAuthorizename());
                        else
                            txt_authorize8.setText("Select");

                        CleansingDate1 = jorder.getString("FlammableDate");
                        btn_cleansing_date.setText(CleansingDate1);

                        //Oxygen Measurement


                        Phase2_Top2 = jorder.getString("OxygenTop");
                        if (!Phase2_Top2.equalsIgnoreCase(""))
                            edit_phase2_top2.setText(Phase2_Top2);
                        else
                            edit_phase2_top2.setText("");

                        Centre2 = jorder.getString("OxygenCentre");
                        if (!Centre2.equalsIgnoreCase(""))
                            edit_phase2_centre2.setText(Centre2);
                        else
                            edit_phase2_centre2.setText("");


                        Percentage2 = jorder.getString("OxygenExtractoroutputpoint");
                        if (!Percentage2.equalsIgnoreCase(""))
                            edit_per2.setText(Percentage2);
                        else
                            edit_per2.setText("");

                        Percentage4 = jorder.getString("OxygenManholepoint");
                        if (!Percentage4.equalsIgnoreCase(""))
                            edit_per4.setText(Percentage4);
                        else edit_per4.setText("");

                        Bottom_per2 = jorder.getString("OxygenBottom");
                        if (!Bottom_per2.equalsIgnoreCase(""))
                            edit_bottom2.setText(Bottom_per2);
                        else
                            edit_bottom2.setText("");

                        Authorized9Id = jorder.getString("OxygenSignature");
                        int authorize9pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (Authorized9Id.equals(authorizedPersonArrayList.get(j).getAuthorizeid()))
                                authorize9pos = j;
                        }
                        if (authorize9pos != -1)
                            txt_authorize9.setText(authorizedPersonArrayList.get(authorize9pos).getAuthorizename());
                        else
                            txt_authorize9.setText("Select");

                        CleansingDate2 = jorder.getString("OxygenDate");
                        btn_cleansing_date1.setText(CleansingDate2);

                        //Toxic Gas Measurement

                        Toxic_Top1 = jorder.getString("ToxicTop");
                        if (!Toxic_Top1.equalsIgnoreCase(""))
                            edit_toxic_top.setText(Toxic_Top1);
                        else
                            edit_toxic_top.setText("");


                        Gasname1 = jorder.getString("ToxicTopGasName");
                        if (!Gasname1.equalsIgnoreCase(""))
                            edit_gasname1.setText(Gasname1);
                        else
                            edit_gasname1.setText("");

                        Toxic_centre = jorder.getString("ToxicCentre");
                        if (!Toxic_centre.equalsIgnoreCase(""))
                            edit_toxic_centre.setText(Toxic_centre);
                        else
                            edit_toxic_centre.setText("");

                        Gasname2 = jorder.getString("ToxicCentreGasName");
                        if (!Gasname2.equalsIgnoreCase(""))
                            edit_gasname2.setText(Gasname2);
                        else
                            edit_gasname2.setText("");

                        Toxic_per1 = jorder.getString("ToxicExtractoroutputpoint");
                        if (!Toxic_per1.equalsIgnoreCase(""))
                            edit_toxic_per1.setText(Toxic_per1);
                        else
                            edit_toxic_per1.setText("");

                        Gasname3 = jorder.getString("ToxicExtractorpointGasName");
                        if (!Gasname3.equalsIgnoreCase(""))
                            edit_gasname3.setText(Gasname3);
                        else
                            edit_gasname3.setText("");

                        Toxic_per2 = jorder.getString("ToxicManholepoint");
                        if (!Toxic_per2.equalsIgnoreCase(""))
                            edit_toxic_per2.setText(Toxic_per2);
                        else
                            edit_toxic_per2.setText("");

                        Gasname4 = jorder.getString("ToxicManholepointGasName");
                        if (!Gasname4.equalsIgnoreCase(""))
                            edit_gasname4.setText(Gasname4);
                        else
                            edit_gasname4.setText("");

                        Toxic_bottom = jorder.getString("ToxicBottom");
                        if (!Toxic_bottom.equalsIgnoreCase(""))
                            edit_toxic_bottom.setText(Toxic_bottom);
                        else
                            edit_toxic_bottom.setText("");

                        Gasname5 = jorder.getString("ToxicBottomGasName");
                        if (!Gasname5.equalsIgnoreCase(""))
                            edit_gasname5.setText(Gasname5);
                        else
                            edit_gasname5.setText("");

                        CleansingDate3 = jorder.getString("ToxicDate");
                        if (!CleansingDate3.equalsIgnoreCase(""))
                            btn_cleansing_date2.setText(CleansingDate3);
                        else
                            btn_cleansing_date2.setText("");

                        Authorized10Id = jorder.getString("ToxicSignature");
                        int authorize10pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (Authorized10Id.equals(authorizedPersonArrayList.get(j).getAuthorizeid()))
                                authorize10pos = j;
                        }
                        if (authorize10pos != -1)
                            txt_authorize10.setText(authorizedPersonArrayList.get(authorize10pos).getAuthorizename());
                        else
                            txt_authorize10.setText("Select");


                        safetytools = jorder.getString("SafetyToolMasterId");
                        String[] safetyTools = new String[safetytools.length()];
                        safetyTools = safetytools.split(",");
                        for (int j = 0; j < safetyToolsArrayList.size(); j++) {
                            for (int k = 0; k < safetyTools.length; k++) {
                                if (safetyTools[k].equals(safetyToolsArrayList.get(j).getSafetyToolMasterId())) {
                                    int pos = j;
                                    safetyToolsArrayList.get(pos).setSelected(true);
                                    safetyAdapter = new SafetyAdapter(CleansingPermitActivity.this, safetyToolsArrayList, "CAD", Mode, PermitStatus);
                                    grid_safety.setAdapter(safetyAdapter);

                                }
                            }
                        }

                        AuthorizedId = jorder.getString("AuthorizeBy1");
                        // Authorize1Name = jorder.getString("");
                        int authorizepos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(AuthorizedId))
                                authorizepos = j;
                        }

                        if (authorizepos != -1)
                            txt_authorize.setText(authorizedPersonArrayList.get(authorizepos).getAuthorizename());
                        else
                            txt_authorize.setText("Select");


                        Authorized1Id = jorder.getString("AuthorizeBy2");
                        // Authorize1Name = jorder.getString("");
                        int authorize1pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(Authorized1Id))
                                authorize1pos = j;
                        }

                        if (authorize1pos != -1)
                            txt_authorize1.setText(authorizedPersonArrayList.get(authorize1pos).getAuthorizename());
                        else
                            txt_authorize1.setText("Select");


                        Authorized3Id = jorder.getString("AuthorizeBy3");
                        // Authorize1Name = jorder.getString("");
                        int authorize3pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(Authorized3Id))
                                authorize3pos = j;
                        }

                        if (authorize3pos != -1)
                            txt_authorize3.setText(authorizedPersonArrayList.get(authorize3pos).getAuthorizename());
                        else
                            txt_authorize3.setText("Select");

                        Authorized4Id = jorder.getString("AuthorizeBy4");
                        // Authorize1Name = jorder.getString("");
                        int authorize4pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(Authorized4Id))
                                authorize4pos = j;
                        }

                        if (authorize4pos != -1)
                            txt_authorize4.setText(authorizedPersonArrayList.get(authorize4pos).getAuthorizename());
                        else
                            txt_authorize4.setText("Select");


                        method_of_operation = jorder.getString("MethodOperationStatus");
                        if (method_of_operation.contains("Y"))
                            checkbox_method.setChecked(true);
                        else if (method_of_operation.contains("N"))
                            checkbox_method.setChecked(false);
                        else
                            checkbox_method.setChecked(false);


                        PermitClosedId = jorder.getString("PermitcloseBy");
                        // Authorize1Name = jorder.getString("");
                        int permitpos = -1;
                        if (PermitClosedId.equalsIgnoreCase("")) {
                            txt_permitclosed.setText("Select");
                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(PermitClosed))
                                    permitpos = j;
                            }
                        }

                        if (permitpos != -1)
                            txt_permitclosed.setText(authorizedPersonArrayList.get(permitpos).getAuthorizename());
                        else
                            txt_permitclosed.setText("Select");


                        SpotCheckId = jorder.getString("SpotCheckBy");
                        // Authorize1Name = jorder.getString("");
                        int spotcheckpos = -1;
                        if (SpotCheckId.equalsIgnoreCase("")) {
                            txt_spotcheck.setText("Select");
                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(SpotCheck))
                                    spotcheckpos = j;
                            }
                        }
                        if (spotcheckpos != -1)
                            txt_spotcheck.setText(authorizedPersonArrayList.get(spotcheckpos).getAuthorizename());
                        else
                            txt_spotcheck.setText("Select");


                        Remarks = jorder.getString("PermitcloseRemark");
                        if (!Remarks.equalsIgnoreCase(""))
                            edit_remark.setText(Remarks);
                        else
                            edit_remark.setText("");

                        PermitLClosedDate = jorder.getString("PermitcloseDate");
                        btn_cleansing_date8.setText(PermitLClosedDate);

                        SpotcheckDate = jorder.getString("SpotCheckDate");
                        btn_cleansing_date9.setText(SpotcheckDate);

                        safety_Others = jorder.getString("CD_ProOthers");

                        if (safetyToolsArrayList != null) {
                            for (int j = 0; j < safetyToolsArrayList.size(); j++) {
                                if (safetyToolsArrayList.get(j).getSafetyToolDesc().equals("Other(s)")) {
                                    safetyToolsArrayList.get(j).setRemarks(safety_Others);
                                    safetyToolsArrayList.get(j).isSelected();
                                }
                            }
                        }

                        cancelId = jorder.getString("PermitCancelBy");
                        //SpotCheckName = jorder.getString("");

                        int cancelpos = -1;
                        if (cancelId.equalsIgnoreCase("") || cancelId.equalsIgnoreCase("null")) {
                            cancelId = "";
                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (cancelId.equals(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                    cancelpos = j;
                                    break;
                                }
                            }
                        }
                        if (cancelpos != -1)
                            txt_cancel.setText(authorizedPersonArrayList.get(cancelpos).getAuthorizename());
                        else
                            txt_cancel.setText("Select");


                        cancelDate = jorder.getString("PermitCancelDate");
                        if (cancelDate.equals("")) {
                            btn_cancel_date.setText(date);
                        } else {
                            btn_cancel_date.setText(cancelDate);
                        }



                      /*  if(PermitClosedId.equals("") || SpotCheckId.equals("")){
                            btn_submit.setVisibility(View.GONE);
                        }else{
                            btn_submit.setVisibility(View.VISIBLE);
                        }*/

                        /*AuthorizeDate3 = jorder.getString("");
                        btn_cleansing_date3.setText(AuthorizeDate3);

                        AuthorizeDate4 = jorder.getString("");
                        btn_cleansing_date4.setText(AuthorizeDate4);

                        AuthorizeDate5 = jorder.getString("");
                        btn_cleansing_date5.setText(AuthorizeDate5);

                        AuthorizeDate6 = jorder.getString("");
                        btn_cleansing_date6.setText(AuthorizeDate6);

                        AuthorizeDate7 = jorder.getString("");
                        btn_cleansing_date7.setText(AuthorizeDate7);*/


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initview() {
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        list_cleansing = findViewById(R.id.list_cleansing);
        btn_fromdate = findViewById(R.id.btn_fromdate);
        checkbox_method = (CheckBox) findViewById(R.id.checkbox_method);
        checkbox_phase2 = findViewById(R.id.checkbox_phase2);
        checkbox_phase1 = findViewById(R.id.checkbox_phase1);
        //  btn_todate = findViewById(R.id.btn_todate);
        btn_cleansing_date = findViewById(R.id.btn_cleansing_date);
        btn_cleansing_date1 = findViewById(R.id.btn_cleansing_date1);
        btn_cleansing_date2 = findViewById(R.id.btn_cleansing_date2);
        btn_cleansing_date3 = findViewById(R.id.btn_cleansing_date3);
        btn_cleansing_date4 = findViewById(R.id.btn_cleansing_date4);
        btn_cleansing_date5 = findViewById(R.id.btn_cleansing_date5);
        btn_cleansing_date6 = findViewById(R.id.btn_cleansing_date6);
        btn_cleansing_date7 = findViewById(R.id.btn_cleansing_date7);
        btn_cleansing_date8 = findViewById(R.id.edt_permit_date);
        btn_cleansing_date9 = findViewById(R.id.edt_spot_date);
        btn_cancel_date = findViewById(R.id.edt_cancel_date);
        btn_totime = findViewById(R.id.btn_totime);
        btn_totime.setEnabled(false);
        btn_totime.setTextColor(Color.parseColor("#000000"));
        btn_fromtime = findViewById(R.id.btn_fromtime);
        btn_submit = findViewById(R.id.btn_submit);
        spinner_authorize = findViewById(R.id.spinner_authorize_pas);
        spinner_permit_closed = findViewById(R.id.spinner_permit_closed1);
        txt_authorize = (TextView) findViewById(R.id.spinner_authorize);
        txt_authorize1 = (TextView) findViewById(R.id.spinner_authorize1);
        txt_authorize3 = (TextView) findViewById(R.id.spinner_authorize3);
        txt_authorize4 = (TextView) findViewById(R.id.spinner_authorize4);
        txt_authorize5 = (TextView) findViewById(R.id.spinner_authorize5);
        txt_authorize6 = (TextView) findViewById(R.id.spinner_authorize6);
        txt_authorize7 = (TextView) findViewById(R.id.spinner_authorize7);
        txt_authorize8 = (TextView) findViewById(R.id.spinner_authorize8);
        txt_authorize9 = (TextView) findViewById(R.id.spinner_authorize9);
        txt_authorize10 = (TextView) findViewById(R.id.spinner_authorize10);
        txt_permitclosed = (TextView) findViewById(R.id.txt_permit_closed);
        txt_spotcheck = (TextView) findViewById(R.id.txt_spotcheck);
        txt_cancel = (TextView) findViewById(R.id.txt_cancel);
        len_cancel_permit = findViewById(R.id.len_cancel_permit);
        txt_cancel_permit = findViewById(R.id.txt_cancel_permit);
        list_goldenRules = findViewById(R.id.list_goldenRules);

        spinner_authorize2 = findViewById(R.id.spinner_authorize2);
        spinner_authorize2.setEnabled(false);
        //spinner_permit_closed = findViewById(R.id.spinner_permit_closed1);
        cusdialog1 = findViewById(R.id.cusDailog1);
        cusdialog2 = findViewById(R.id.cusDailog2);
        edit_password_pass = findViewById(R.id.edt_password_pass);
        ln_spinner_authorize = findViewById(R.id.ln_spinner_authorize);
        ln_spinner_reason = findViewById(R.id.ln_spinner_reason);
        btn_cancel_pass = findViewById(R.id.txt_cancel_pass);
        btn_submit_pass = findViewById(R.id.txt_submit_pass);

        edit_password_reason = findViewById(R.id.edt_password_reason);
        edit_reason = findViewById(R.id.edt_reason);
        btn_cancel_reason = findViewById(R.id.txt_cancel_reason);
        btn_submit_reason = findViewById(R.id.txt_submit_reason);


        spinner_operation = (Spinner) findViewById(R.id.spinner_operation);
        spinner_contractor = (Spinner) findViewById(R.id.spinner_contractor);
        spinner_confined_space = (Spinner) findViewById(R.id.spinner_confined_space);
        spinner_station = (Spinner) findViewById(R.id.spinner_station);
        edit_permitno = (EditText) findViewById(R.id.edit_permitno);

        grid_safety = findViewById(R.id.grid_safety);

        img_camera = findViewById(R.id.img_camera);


        spinner_prevention_plan = findViewById(R.id.spinner_prevention_plan);


        edit_result1 = findViewById(R.id.edit_result1);
        edit_result2 = findViewById(R.id.edit_result2);
        edit_result3 = (EditText) findViewById(R.id.edit_result3);
        edit_phase2_top1 = findViewById(R.id.edit_phase2_top1);
        edit_phase2_centre1 = findViewById(R.id.edit_phase2_centre1);
        edit_per1 = findViewById(R.id.edit_per1);
        edit_per3 = findViewById(R.id.edit_per3);
        edit_bottom1 = findViewById(R.id.edit_bottom1);
        edit_phase2_top2 = findViewById(R.id.edit_phase2_top2);
        edit_phase2_centre2 = findViewById(R.id.edit_phase2_centre2);
        edit_per2 = findViewById(R.id.edit_per2);
        edit_per4 = findViewById(R.id.edit_per4);
        edit_bottom2 = findViewById(R.id.edit_bottom2);
        edit_toxic_top = findViewById(R.id.edit_toxic_top);
        edit_gasname1 = findViewById(R.id.edit_gasname1);
        edit_toxic_centre = findViewById(R.id.edit_toxic_centre);
        edit_gasname2 = findViewById(R.id.edit_gasname2);
        edit_toxic_per1 = findViewById(R.id.edit_toxic_per1);
        edit_gasname3 = findViewById(R.id.edit_gasname3);
        edit_toxic_per2 = findViewById(R.id.edit_toxic_per2);
        edit_gasname4 = findViewById(R.id.edit_gasname4);
        edit_toxic_bottom = findViewById(R.id.edit_toxic_bottom);
        edit_gasname5 = findViewById(R.id.edit_gasname5);
        edit_remark = (EditText) findViewById(R.id.edit_remark);
        len_p_closed = findViewById(R.id.len_p_closed);
        tx_p_closed = findViewById(R.id.tx_p_closed);

        ln_station = findViewById(R.id.ln_station);
        ln_contractor = findViewById(R.id.ln_contractor);
        ln_natureOperation = findViewById(R.id.ln_natureOperation);
        ln_locationOperation = findViewById(R.id.ln_locationOperation);
        len_prevention = findViewById(R.id.len_prevention);
        ln_authorize8 = findViewById(R.id.ln_authorize8);
        ln_authorize5 = findViewById(R.id.ln_authorize5);
        ln_authorize7 = findViewById(R.id.ln_authorize7);
        ln_authorize6 = findViewById(R.id.ln_authorize6);
        ln_authorize9 = findViewById(R.id.ln_authorize9);

        spinner_confined_space.setEnabled(false);
        spinner_operation.setEnabled(false);

        variable1 = edit_result1.getKeyListener();
        edit_result1.setKeyListener(null);
        txt_authorize5.setEnabled(false);
        variable2 = edit_result2.getKeyListener();
        edit_result2.setKeyListener(null);
        txt_authorize6.setEnabled(false);
        variable3 = edit_result3.getKeyListener();
        edit_result3.setKeyListener(null);
        txt_authorize7.setEnabled(false);

        variable4 = edit_phase2_top1.getKeyListener();
        edit_phase2_top1.setKeyListener(null);
        variable5 = edit_phase2_top2.getKeyListener();
        edit_phase2_top2.setKeyListener(null);
        variable6 = edit_phase2_centre1.getKeyListener();
        edit_phase2_centre1.setKeyListener(null);
        variable7 = edit_phase2_centre2.getKeyListener();
        edit_phase2_centre2.setKeyListener(null);
        variable8 = edit_per1.getKeyListener();
        edit_per1.setKeyListener(null);
        variable9 = edit_per2.getKeyListener();
        edit_per2.setKeyListener(null);
        variable10 = edit_per3.getKeyListener();
        edit_per3.setKeyListener(null);
        variable11 = edit_per4.getKeyListener();
        edit_per4.setKeyListener(null);
        variable12 = edit_bottom1.getKeyListener();
        edit_bottom1.setKeyListener(null);
        variable13 = edit_bottom2.getKeyListener();
        edit_bottom2.setKeyListener(null);
        btn_cleansing_date.setEnabled(false);
        btn_cleansing_date.setTextColor(Color.parseColor("#000000"));
        btn_cleansing_date1.setEnabled(false);
        btn_cleansing_date1.setTextColor(Color.parseColor("#000000"));
        txt_authorize8.setEnabled(false);
        txt_authorize9.setEnabled(false);


    }

    public void setListner() {


        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(CleansingPermitActivity.this, Manifest.permission.CAMERA)
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

        spinner_station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StationId = depotArrayList.get(position).getDepotid();

                if (StationId.equals("Select")) {

                } else {
                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        showProgress();
                        //Location Get
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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


        btn_fromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] startTimeHour = new int[1];
                final int[] endTimeHour = new int[1];
                final Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                int hour1 = hour + 4;
                int a = mcurrentTime.get(Calendar.AM_PM);
                String a1;
                if (a == 1) {
                    a1 = "PM";
                } else {
                    a1 = "AM";
                }

                TimePickerDialog mTimePicker;
                UpdateTime.updateTime(hour, minute);

                //  btn_fromtime.setText(UpdateTime.updateTime(hour, minute));
                // btn_totime.setText(UpdateTime.updateTime(hour1, minute));

                mTimePicker = new TimePickerDialog(CleansingPermitActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                String time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                String time1 = "";
                                int sel = selectedHour + 4;
                                if (sel >= 24) {
                                    time1 = "11:59 PM";
                                } else {
                                    time1 = UpdateTime.updateTime((selectedHour + 4), selectedMinute);
                                }
                                Date date = null, date1 = null, dateWAEndTime = null, dateTime = null;
                                String output = "", output1 = "", output2;
                                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                                DateFormat outputformat = new SimpleDateFormat("HH:mm:ss");
                                try {
                                    date = dateFormat.parse(WAStartTime);
                                    dateTime = dateFormat.parse(time);
                                    date1 = dateFormat.parse(time1);
                                    dateWAEndTime = dateFormat.parse(WAEndTime);


                                    output = outputformat.format(dateTime);
                                    output1 = outputformat.format(date);
                                    output2 = outputformat.format(dateWAEndTime);
                                    startTimeHour[0] = Integer.parseInt(output1.split(":")[0]);
                                    endTimeHour[0] = Integer.parseInt(output2.split(":")[0]);


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                System.out.println(date);
                                if (WAStartTimeHr < selectedHour && selectedHour < WAEndTimeHr) {
                                    btn_fromtime.setText(time);
                                    if ((selectedHour + 4) >= WAEndTimeHr) {
                                        if(selectedMinute > WAEndTimeMin){
                                            String time12 =  UpdateTime.updateTime(WAEndTimeHr, WAEndTimeMin);
                                           /* edt_totime.setText(time12);
                                        }else{*/
                                            btn_totime.setText(WAEndTime1);
                                        }
                                       // btn_totime.setText(WAEndTime1);

                                    } else {
                                        if (selectedHour > 10 && selectedHour < 13) {
                                            time = time1 = UpdateTime.updateTime((selectedHour + 5), selectedMinute);
                                            btn_totime.setText(time);
                                        } else if (selectedHour == 13) {
                                            if (WAStartTime.equals("")) {
                                                btn_fromtime.setText(time);
                                            } else {
                                                btn_fromtime.setText(WAStartTime);
                                            }

                                            if (WAEndTime.equals("")) {
                                                btn_totime.setText(time1);
                                            } else {
                                                btn_totime.setText(WAEndTime);
                                            }
                                            String msg = "Please select time between " + WAStartTime + " and " + WAEndTime1;
                                            Toast.makeText(CleansingPermitActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        } else {
                                            btn_totime.setText(time1);
                                        }
                                    }


                                } else if (WAStartTimeHr > selectedHour) {
                                    if (WAStartTime.equals("")) {
                                        btn_fromtime.setText(time);
                                    } else {
                                        btn_fromtime.setText(WAStartTime);
                                    }

                                    if (WAEndTime.equals("")) {
                                        btn_totime.setText(time1);
                                    } else {
                                        btn_totime.setText(WAEndTime);
                                    }
                                    String msg = "Please select time between " + WAStartTime + " and " + WAEndTime1;
                                    Toast.makeText(CleansingPermitActivity.this, msg, Toast.LENGTH_SHORT).show();
                                } else {
                                    if (WAStartTimeHr < selectedHour) {
                                        if (WAStartTime.equals("")) {
                                            btn_fromtime.setText(time);
                                        } else {
                                            btn_fromtime.setText(WAStartTime);
                                        }

                                        if (WAEndTime.equals("")) {
                                            btn_totime.setText(time1);
                                        } else {
                                            btn_totime.setText(WAEndTime);
                                        }
                                        String msg = "Please select time between " + WAStartTime + " and " + WAEndTime1;
                                        Toast.makeText(CleansingPermitActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    } else if (WAStartTimeHr == selectedHour && selectedMinute >= WAStartTimemin) {
                                        btn_totime.setText(time1);
                                        btn_fromtime.setText(time);
                                    } else {
                                        if (WAEndTimeHr >= selectedHour && WAEndTimeMin >= selectedHour) {
                                            btn_totime.setText(time1);
                                            btn_fromtime.setText(time);
                                        } else {
                                            if (WAStartTime.equals("")) {
                                                btn_fromtime.setText(time);
                                            } else {
                                                btn_fromtime.setText(WAStartTime);
                                            }

                                            if (WAEndTime.equals("")) {
                                                btn_totime.setText(time1);
                                            } else {
                                                btn_totime.setText(WAEndTime);
                                            }
                                            String msg = "Please select time between " + WAStartTime + " and " + WAEndTime1;

                                            Toast.makeText(CleansingPermitActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                                /*if (selectedHour >= startTimeHour[0] && endTimeHour[0] >= selectedHour) {
                                    edt_totime.setText(time1);
                                    edt_fromtime.setText(time);
                                } else {
                                    if (WAStartTime.equals("")) {
                                        edt_fromtime.setText(time);
                                    } else {
                                        edt_fromtime.setText(WAStartTime);
                                    }

                                    if (WAEndTime.equals("")) {
                                        edt_totime.setText(time1);
                                    } else {
                                        edt_totime.setText(WAEndTime);
                                    }

                                    Toast.makeText(HOTWorkActivity.this, "Please select time greater than workauthorization time", Toast.LENGTH_SHORT).show();
                                }
*/


                            }
                        }, hour, minute, false);// Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        btn_totime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                //int hour1 = hour + 4;

                TimePickerDialog mTimePicker;

                btn_totime.setText(UpdateTime.updateTime(hour, minute));
                //edt_totime.setText(hour1 + ":" + minute + " ");

                mTimePicker = new TimePickerDialog(CleansingPermitActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                String time2 = "";
                                String time = UpdateTime.updateTime(selectedHour, selectedMinute);

                                boolean val = timedifference(time, to_time);
                                if (val) {
                                    btn_totime.setText(time);
                                } else {
                                    btn_totime.setText(to_time);
                                }


                            }
                        }, hour, minute, false); // Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        


/*

        btn_fromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                int hour1 = hour + 4;

                TimePickerDialog mTimePicker;

                btn_fromtime.setText(UpdateTime.updateTime(hour, minute));
                btn_totime.setText(UpdateTime.updateTime(hour1, minute));

                mTimePicker = new TimePickerDialog(CleansingPermitActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                String time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                String time1="";
                                int sel = selectedHour+4;
                                if(sel >= 24){
                                    time1 = "11:59 PM";
                                }else{
                                    time1 = UpdateTime.updateTime((selectedHour+4), selectedMinute);
                                }

                                if(selectedHour >= 9) {
                                    if(selectedHour <= 18){
                                        if(selectedHour == 18  && selectedMinute == 0){
                                            btn_fromtime.setText(WAEndTime);
                                            btn_totime.setText(WAEndTime);
                                        }else {
                                            btn_fromtime.setText(time);
                                            if(time1.compareTo(WAEndTime)  > 0){
                                                btn_totime.setText(WAEndTime);
                                            }else {
                                                if(selectedHour >= 18 && selectedMinute > 0){
                                                    btn_totime.setText(WAEndTime);
                                                    btn_fromtime.setText(WAStartTime);
                                                }else{
                                                    btn_totime.setText(time1);
                                                }

                                            }
                                        }
                                    }else if(selectedHour >= 20){
                                        btn_fromtime.setText(time);
                                        btn_totime.setText("11:59 PM");
                                    //    btn_totime.setText(UpdateTime.updateTime(mcurrentTime.get(Calendar.HOUR_OF_DAY) + 4, mcurrentTime.get(Calendar.MINUTE)));
                                    }else{
                                        btn_fromtime.setText(WAStartTime);
                                        btn_totime.setText(WAEndTime);
                                    }
                                }
                                else{
                                    if(WAStartTime.equals("")){
                                        btn_fromtime.setText(time);
                                    }else {
                                        btn_fromtime.setText(WAStartTime);
                                    }

                                    if(WAEndTime.equals("")){
                                        btn_totime.setText(time1);
                                    }else{
                                        btn_totime.setText(WAEndTime);
                                    }

                                    Toast.makeText(CleansingPermitActivity.this, "Please select time greater than workauthorization time", Toast.LENGTH_SHORT).show();
                                }



                            }
                        }, hour, minute, false);// Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        btn_totime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                //int hour1 = hour + 4;

                TimePickerDialog mTimePicker;

                btn_totime.setText(hour + ":" + minute + " ");
                //edt_totime.setText(hour1 + ":" + minute + " ");

                mTimePicker = new TimePickerDialog(CleansingPermitActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                String time2 = "";
                                String time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                boolean val = timedifference(time, to_time);
                                if (val) {
                                    btn_totime.setText(time);
                                } else {
                                    btn_totime.setText(to_time);
                                }


                            }
                        }, hour, minute, false);// Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
*/


        btn_cancel_date.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(CleansingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_cancel_date.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_cancel_date.setText(dayOfMonth + " - "
                                                    + (monthOfYear + 1) + "-" + year);
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_cancel_date.setText(day + "-"
                                                    + (month + 1) + "-" + Year);
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(CleansingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_cancel_date.setText(dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year);
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_cancel_date.setText(dayOfMonth + " - "
                                                + (monthOfYear + 1) + "-" + year);
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_cancel_date.setText(day + "-"
                                                + (month + 1) + "-" + Year);
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_cancel_date.setText(day + "-"
                                            + (month + 1) + "-" + Year);
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


        spinner_prevention_plan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (WAArayList.size() > 0) {

                    WAH_No = WAArayList.get(position).getPermitNo();
                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        showProgress();
                        //Location Get
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadGETWA_PermitNoDetail().execute(WAH_No);
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                CommonClass.displayToast(getApplicationContext(), msg);
                                dismissProgress();
                            }
                        });
                    } else {
                        Toast.makeText(CleansingPermitActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_contractor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                int contractorpos = -1;
                if (contractorListActivityArrayList != null) {

                    contractorId = contractorListActivityArrayList.get(position).getCustVendorMasterId();
                    for (int i = 0; i < contractorListActivityArrayList.size(); i++) {
                        if (contractorId.equals(contractorListActivityArrayList.get(i).getCustVendorMasterId())) {
                            contractorpos = i;
                            break;
                        }
                    }

                    if (contractorpos != -1) {
                        spinner_authorize2.setSelection(contractorpos);
                    } else {
                        spinner_authorize2.setSelection(0);
                    }

                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        showProgress();
                        //Location Get
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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


                    if (contractorId != null /*&& contractorId != ""*/) {
                        if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                            new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadWANo().execute(contractorId);
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    Toast.makeText(CleansingPermitActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(CleansingPermitActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    }


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String authorize1pos = txt_authorize.getText().toString();
                String authorize2pos = txt_authorize1.getText().toString();
                String authorize4pos = txt_authorize3.getText().toString();
                String authorize5pos = txt_authorize4.getText().toString();

                int authorize3pos = spinner_authorize2.getSelectedItemPosition();

                if (StationId.equalsIgnoreCase("Select") || StationId.equalsIgnoreCase("")) {
                    Toast.makeText(CleansingPermitActivity.this, "Please Fill Station Details", Toast.LENGTH_SHORT).show();
                    ln_station.setBackgroundResource(R.drawable.edit_text_red);
                } else if (WAH_No.equalsIgnoreCase("") || WAH_No.equalsIgnoreCase("Select")) {
                    Toast.makeText(CleansingPermitActivity.this, "Please Fill Prevention Plan Details", Toast.LENGTH_SHORT).show();
                    len_prevention.setBackgroundResource(R.drawable.edit_text_red);
                } else if (contractorId.equalsIgnoreCase("") || contractorId.equalsIgnoreCase("Select")) {
                    Toast.makeText(CleansingPermitActivity.this, "Please Fill Contractor Details", Toast.LENGTH_SHORT).show();
                    ln_contractor.setBackgroundResource(R.drawable.edit_text_red);
                } else if (OperationId.equalsIgnoreCase("") || OperationId.equalsIgnoreCase("Select")) {
                    Toast.makeText(CleansingPermitActivity.this, "Please Fill Nature of Operation Details", Toast.LENGTH_SHORT).show();
                    ln_natureOperation.setBackgroundResource(R.drawable.edit_text_red);
                } else if (locationId.equalsIgnoreCase("") || locationId.equalsIgnoreCase("Select")) {
                    Toast.makeText(CleansingPermitActivity.this, "Please Fill Location of Operation Details", Toast.LENGTH_SHORT).show();
                    ln_locationOperation.setBackgroundResource(R.drawable.edit_text_red);
                } else if (method_of_operation.equalsIgnoreCase("")) {
                    Toast.makeText(CleansingPermitActivity.this, "Please Check Method of Operation", Toast.LENGTH_SHORT).show();
                } else {
                    if (isValidate()) {
                        if (Mode.equalsIgnoreCase("A")) {
                            saveactivityjson();
                        } else if (Mode.equalsIgnoreCase("E")) {
                            editjson();
                        }
                    }
                }
            }
        });

      /*  spinner_contractor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (contractorListActivityArrayList == null) {
                        Toast.makeText(CleansingPermitActivity.this," No Data Found ",Toast.LENGTH_SHORT).show();
                    }else {
                        if(permit!=null){
                            contractorId = contractorListActivityArrayList.get(position).getCustVendorMasterId();
                            WAH_No = contractorListActivityArrayList.get(position).getCustVendorCode();
                        }else {
                            contractorId = contractorListActivityArrayList.get(position).getCustVendorMasterId();
                            WAH_No = contractorListActivityArrayList.get(position).getCustVendorCode();

                        }
                    }



                }else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        spinner_operation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OperationId = operationArrayList.get(position).getOperationMasterId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_confined_space.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationId = LocationArraylist.get(position).getLocationMasterId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        checkbox_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_method.isChecked()) {
                    //checkoperation = checkbox_method.getText().toString();
                    method_of_operation = "Y";

                } else {
                    method_of_operation = "N";
                }
            }

        });

        checkbox_phase1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_phase1.isChecked()) {
                    //checkoperation = checkbox_method.getText().toString();
                    Phase1 = "Y";
                    edit_result1.setKeyListener(variable1);
                    txt_authorize5.setEnabled(true);
                    edit_result2.setKeyListener(variable2);
                    txt_authorize6.setEnabled(true);
                    edit_result3.setKeyListener(variable3);
                    txt_authorize7.setEnabled(true);


                } else {
                    Phase1 = "N";
                    edit_result1.setKeyListener(null);
                    txt_authorize5.setEnabled(false);
                    edit_result2.setKeyListener(null);
                    txt_authorize6.setEnabled(false);
                    edit_result3.setKeyListener(null);
                    txt_authorize7.setEnabled(false);
                    edit_result1.setText("");
                    edit_result2.setText("");
                    edit_result3.setText("");
                }
            }

        });

        checkbox_phase2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_phase2.isChecked()) {
                    //checkoperation = checkbox_method.getText().toString();
                    Phase2 = "Y";
                    edit_phase2_top1.setKeyListener(variable4);
                    edit_phase2_top2.setKeyListener(variable5);
                    edit_phase2_centre1.setKeyListener(variable6);
                    edit_phase2_centre2.setKeyListener(variable7);
                    edit_per1.setKeyListener(variable8);
                    edit_per2.setKeyListener(variable9);
                    edit_per3.setKeyListener(variable10);
                    edit_per4.setKeyListener(variable11);
                    edit_bottom1.setKeyListener(variable12);
                    edit_bottom2.setKeyListener(variable13);
                    btn_cleansing_date.setEnabled(true);
                    btn_cleansing_date.setTextColor(Color.parseColor("#000000"));
                    btn_cleansing_date1.setEnabled(true);
                    btn_cleansing_date1.setTextColor(Color.parseColor("#000000"));
                    txt_authorize8.setEnabled(true);
                    txt_authorize9.setEnabled(true);

                } else {
                    Phase2 = "N";
                    edit_phase2_top1.setKeyListener(null);
                    edit_phase2_top2.setKeyListener(null);
                    edit_phase2_centre1.setKeyListener(null);
                    edit_phase2_centre2.setKeyListener(null);
                    edit_per1.setKeyListener(null);
                    edit_per2.setKeyListener(null);
                    edit_per3.setKeyListener(null);
                    edit_per4.setKeyListener(null);
                    edit_bottom1.setKeyListener(null);
                    edit_bottom2.setKeyListener(null);


                    edit_phase2_top1.setText("");
                    edit_phase2_top2.setText("");
                    edit_phase2_centre1.setText("");
                    edit_phase2_centre2.setText("");
                    edit_per1.setText("");
                    edit_per2.setText("");
                    edit_per3.setText("");
                    edit_per4.setText("");
                    edit_bottom1.setText("");
                    edit_bottom2.setText("");

                    btn_cleansing_date.setEnabled(false);
                    btn_cleansing_date.setTextColor(Color.parseColor("#000000"));
                    btn_cleansing_date1.setEnabled(false);
                    btn_cleansing_date1.setTextColor(Color.parseColor("#000000"));
                    txt_authorize8.setEnabled(false);
                    txt_authorize9.setEnabled(false);
                }
            }

        });
        txt_authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //passworddialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 1", "fromPwd");
                cusdialog1.setVisibility(View.VISIBLE);
                tempVal = "0";
            }
        });

        txt_authorize1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // authorize1dialog();
                //Tier 1 and Tier 2
                CategoryWiseAuthorizeName("level 2", "fromPwd");
                cusdialog1.setVisibility(View.VISIBLE);
                tempVal = "1";
            }
        });

        txt_authorize3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authorize3dialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 2", "fromPwd");
                cusdialog1.setVisibility(View.VISIBLE);
                tempVal = "3";
            }
        });

        txt_authorize4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // authorize4dialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 2", "fromPwd");
                cusdialog1.setVisibility(View.VISIBLE);
                tempVal = "4";
            }
        });
        txt_authorize5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // authorize5dialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 2", "fromPwd");
                cusdialog1.setVisibility(View.VISIBLE);
                tempVal = "5";
            }
        });

        txt_authorize6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authorize6dialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 2", "fromPwd");
                cusdialog1.setVisibility(View.VISIBLE);
                tempVal = "6";
            }
        });
        txt_authorize7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authorize7dialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 2", "fromPwd");
                cusdialog1.setVisibility(View.VISIBLE);
                tempVal = "7";
            }
        });
        txt_authorize8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authorize8dialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 2", "fromPwd");
                cusdialog1.setVisibility(View.VISIBLE);
                tempVal = "8";
            }
        });
        txt_authorize9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // authorize9dialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 2", "fromPwd");
                cusdialog1.setVisibility(View.VISIBLE);
                tempVal = "9";
            }
        });
        txt_authorize10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // authorize10dialog();
                //tier 1
                CategoryWiseAuthorizeName("level 2", "fromPwd");
                cusdialog1.setVisibility(View.VISIBLE);
                tempVal = "10";
            }
        });


        btn_cancel_pass.setOnClickListener(new View.OnClickListener() {
            //0,1,3,4,5,6,7,8,9,10
            @Override
            public void onClick(View view) {

                ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text);
                edit_password_pass.setBackgroundResource(R.drawable.edit_text);

                if (cusdialog1.getVisibility() == View.VISIBLE) {
                    cusdialog1.setVisibility(View.GONE);
                    if (tempVal.equals("0")) {
                        txt_authorize.setText("Select");
                        spinner_authorize.setSelection(0);
                        edit_password_pass.setText("");
                        txt_authorize.setText("Select");
                    } else if (tempVal.equals("1")) {
                        spinner_authorize.setSelection(0);
                        edit_password_pass.setText("");
                        txt_authorize1.setText("Select");
                    } else if (tempVal.equals("3")) {
                        spinner_authorize.setSelection(0);
                        edit_password_pass.setText("");
                        txt_authorize3.setText("Select");
                    } else if (tempVal.equals("4")) {
                        spinner_authorize.setSelection(0);
                        edit_password_pass.setText("");
                        txt_authorize4.setText("Select");
                    } else if (tempVal.equals("5")) {
                        spinner_authorize.setSelection(0);
                        edit_password_pass.setText("");
                        txt_authorize5.setText("Select");
                    } else if (tempVal.equals("6")) {
                        spinner_authorize.setSelection(0);
                        edit_password_pass.setText("");
                        txt_authorize6.setText("Select");
                    } else if (tempVal.equals("7")) {
                        spinner_authorize.setSelection(0);
                        edit_password_pass.setText("");
                        txt_authorize7.setText("Select");
                    } else if (tempVal.equals("8")) {
                        spinner_authorize.setSelection(0);
                        edit_password_pass.setText("");
                        txt_authorize8.setText("Select");
                    } else if (tempVal.equals("9")) {
                        spinner_authorize.setSelection(0);
                        edit_password_pass.setText("");
                        txt_authorize9.setText("Select");
                    } else if (tempVal.equals("10")) {
                        spinner_authorize.setSelection(0);
                        edit_password_pass.setText("");
                        txt_authorize10.setText("Select");
                    }
                } else {
                    cusdialog1.setVisibility(View.VISIBLE);
                }
                hideKeyboard(CleansingPermitActivity.this);
            }
        });
        btn_submit_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = edit_password_pass.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text);
                edit_password_pass.setBackgroundResource(R.drawable.edit_text);
                if (Password.equalsIgnoreCase("") && (authorize.equalsIgnoreCase("--Select--")
                        || authorize.equalsIgnoreCase(""))) {

                    Toast toast = Toast.makeText(CleansingPermitActivity.this, "Please enter authorized person and password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text_red);
                    edit_password_pass.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();

                    // Toast toast = new Toast(getApplicationContext());
                    // Toast toast = Toast. makeText(WorkAuthorizationActivity.this, "Please enter password", Toast.LENGTH_SHORT);
                    // toast.setGravity(Gravity.CENTER, 0, -160);
                    // toast.setDuration(Toast.LENGTH_SHORT);
                    // toast.setView(layout);
                    // toast.show();
                } else if (!Password.equalsIgnoreCase("") && (authorize.equalsIgnoreCase("--Select--")
                        || authorize.equalsIgnoreCase(""))) {
                    Toast toast = Toast.makeText(CleansingPermitActivity.this, "Please select authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && (!authorize.equalsIgnoreCase("--Select--")
                        || !authorize.equalsIgnoreCase(""))) {
                    Toast toast = Toast.makeText(CleansingPermitActivity.this, "Please enter valid password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_pass.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else {
                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }
        });

        spinner_authorize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (txt_authorizeArrayList == null) {

                    } else {
                        authorize = txt_authorizeArrayList.get(position).getAuthorizeid();
                        String name = txt_authorizeArrayList.get(position).getAuthorizename();
                        userLoginId = txt_authorizeArrayList.get(position).getUserLoginId();
                        if (tempVal.equals("0")) {
                            txt_authorize.setText(name);
                            AuthorizedId = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("1")) {
                            txt_authorize1.setText(name);
                            Authorized1Id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("3")) {
                            txt_authorize3.setText(name);
                            Authorized3Id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("4")) {
                            txt_authorize4.setText(name);
                            Authorized4Id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("5")) {
                            txt_authorize5.setText(name);
                            Authorized5Id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("6")) {
                            txt_authorize6.setText(name);
                            Authorized6Id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("7")) {
                            txt_authorize7.setText(name);
                            Authorized7Id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("8")) {
                            txt_authorize8.setText(name);
                            Authorized8Id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("9")) {
                            txt_authorize9.setText(name);
                            Authorized9Id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("10")) {
                            txt_authorize10.setText(name);
                            Authorized10Id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        }


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                            txt_permitclosed.setText(Permitname);
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


        txt_permitclosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reasondialog();   CategoryWiseAuthorizeName("level 1", "fromReason");
                CategoryWiseAuthorizeName("level 1", "fromReason");
                cusdialog2.setVisibility(View.VISIBLE);
                ReasonVal = "1";
            }
        });

        txt_spotcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Spotcheckdialog();
                CategoryWiseAuthorizeName("level 1", "fromReason");
                cusdialog2.setVisibility(View.VISIBLE);
                ReasonVal = "2";
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryWiseAuthorizeName("level 1", "fromReason");
                cusdialog2.setVisibility(View.VISIBLE);
                ReasonVal = "3";
            }
        });

        btn_cancel_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cusdialog2.getVisibility() == View.VISIBLE) {
                    cusdialog2.setVisibility(View.GONE);
                    if (ReasonVal.equals("1")) {
                        spinner_permit_closed.setSelection(0);
                        edit_password_reason.setText("");
                        edit_reason.setText("");
                        txt_permitclosed.setText("Select");
                        PermitClosedId = "";
                    } else if (ReasonVal.equals("2")) {
                        spinner_permit_closed.setSelection(0);
                        edit_password_reason.setText("");
                        edit_reason.setText("");
                        txt_spotcheck.setText("Select");
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
                } else {
                    cusdialog2.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_submit_reason.setOnClickListener(new View.OnClickListener() {
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

                    Toast toast = Toast.makeText(CleansingPermitActivity.this, "Please enter authorized person,password and reason", Toast.LENGTH_SHORT);
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
                } else if (!Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(CleansingPermitActivity.this, "Please select authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(CleansingPermitActivity.this, "Please valid password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (!Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(CleansingPermitActivity.this, "Please enter reason", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    //password and reason blank
                    Toast toast = Toast.makeText(CleansingPermitActivity.this, "Please valid password and reason", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    //password and permit
                    Toast toast = Toast.makeText(CleansingPermitActivity.this, "Please valid password and authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (!Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    //reason and permit
                    Toast toast = Toast.makeText(CleansingPermitActivity.this, "Please enter reason and authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else {
                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }
        });


    }

    public void dateListener() {

        btn_fromdate.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(CleansingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_fromdate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(CleansingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
// datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);

            }
        });


        btn_cleansing_date.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(CleansingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_cleansing_date.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_cleansing_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_cleansing_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(CleansingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_cleansing_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_cleansing_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_cleansing_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_cleansing_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
// datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);

            }
        });

        btn_cleansing_date1.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(CleansingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_cleansing_date1.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_cleansing_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_cleansing_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(CleansingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_cleansing_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_cleansing_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_cleansing_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_cleansing_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
// datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);

            }
        });

        btn_cleansing_date2.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(CleansingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_cleansing_date2.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_cleansing_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_cleansing_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(CleansingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_cleansing_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_cleansing_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_cleansing_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_cleansing_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
// datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);

            }
        });
        btn_cleansing_date3.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(CleansingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_cleansing_date3.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_cleansing_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_cleansing_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(CleansingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_cleansing_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_cleansing_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_cleansing_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_cleansing_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
// datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);

            }
        });
        btn_cleansing_date4.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(CleansingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_cleansing_date4.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_cleansing_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_cleansing_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(CleansingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_cleansing_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_cleansing_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_cleansing_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_cleansing_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
// datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);

            }
        });

        btn_cleansing_date5.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(CleansingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_cleansing_date5.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_cleansing_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_cleansing_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(CleansingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_cleansing_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_cleansing_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_cleansing_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_cleansing_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
// datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);

            }
        });


        btn_cleansing_date6.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(CleansingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_cleansing_date6.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_cleansing_date6.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_cleansing_date6.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(CleansingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_cleansing_date6.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_cleansing_date6.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_cleansing_date6.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_cleansing_date6.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
// datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);

            }
        });


        btn_cleansing_date7.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(CleansingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_cleansing_date7.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_cleansing_date7.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_cleansing_date7.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(CleansingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_cleansing_date7.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_cleansing_date7.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_cleansing_date7.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_cleansing_date7.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
// datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);

            }
        });


        btn_cleansing_date8.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(CleansingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_cleansing_date8.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_cleansing_date8.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_cleansing_date8.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(CleansingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_cleansing_date8.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_cleansing_date8.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_cleansing_date8.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_cleansing_date8.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
// datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);

            }
        });


        btn_cleansing_date9.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(CleansingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_cleansing_date9.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_cleansing_date9.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_cleansing_date9.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(CleansingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_cleansing_date9.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_cleansing_date9.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_cleansing_date9.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_cleansing_date9.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
// datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);

            }
        });

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
                photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));
                File f = new File(fileUri.getPath().toString());
                path = f.toString();
                Imagefilename = f.getName();

                if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                    new PostUploadImageMethod().execute();

                } else {

                    Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void showProgress() {

        mprogress.setVisibility(View.VISIBLE);
       /* LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.progress,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        // TextView text = (TextView) layout.findViewById(R.id.text);
        ProgressBar progress = (ProgressBar)layout.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        //text.setText("Loading..... please wait");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();*/


    }

    private void dismissProgress() {

        mprogress.setVisibility(View.GONE);


    }

    class DownloadAuthorizedPersonData extends AsyncTask<String, Void, String> {
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

            String url = CompanyURL + WebAPIUrl.api_GetApproverPerson;

            try {
                res = CommonClass.OpenConnection(url, CleansingPermitActivity.this);
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

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {
                dismissProgress();
                //Toast.makeText(CleansingPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(authorizedPersonArrayList);
                editor.putString("Authorized", json);
                editor.commit();
            /*spinner_authorize.setAdapter(authorizedPersonAdapter);
            spinner_authorize1.setAdapter(authorizedPersonAdapter);
            spinner_authorize7.setAdapter(authorizedPersonAdapter);
            spinner_authorize5.setAdapter(authorizedPersonAdapter);
            spinner_authorize6.setAdapter(authorizedPersonAdapter);
            spinner_authorize3.setAdapter(authorizedPersonAdapter);
            spinner_authorize8.setAdapter(authorizedPersonAdapter);
            spinner_authorize9.setAdapter(authorizedPersonAdapter);
            spinner_authorize10.setAdapter(authorizedPersonAdapter);
            spinner_spotcheck.setAdapter(authorizedPersonAdapter);
            spinner_permit_closed.setAdapter(authorizedPersonAdapter);*/


            }


        }
    }

    class DownloadContractorData extends AsyncTask<String, Void, String> {
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
                res = CommonClass.OpenConnection(url, CleansingPermitActivity.this);
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

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {
                dismissProgress();
                //   Toast.makeText(CleansingPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(contractorListActivityArrayList);
                editor.putString("Contractor", json);
                editor.commit();
                permitContractorListAdapter = new PermitContractorListAdapter(CleansingPermitActivity.this, contractorListActivityArrayList);
                spinner_authorize2.setAdapter(permitContractorListAdapter);
                spinner_contractor.setAdapter(permitContractorListAdapter);


            }


        }
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


                if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                    showProgress();
                    new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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

                if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                    showProgress();
                    new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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

                // Contractor List


                if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                    showProgress();
                    new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                    showProgress();
                    new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                    showProgress();
                    new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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

                if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                    showProgress();
                    new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadSafetyToolsData().execute();
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


                if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                    showProgress();
                    new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadGeneralCOndition().execute();
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

    class DownloadNatureOperationData extends AsyncTask<String, Void, String> {
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

            String url = CompanyURL + WebAPIUrl.api_GetOperationList;

            try {
                res = CommonClass.OpenConnection(url, CleansingPermitActivity.this);
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

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {
                dismissProgress();
                // Toast.makeText(CleansingPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(operationArrayList);
                editor.putString("Operation", json);
                editor.commit();
                operationAdapter = new OperationAdapter(CleansingPermitActivity.this, operationArrayList);
                spinner_operation.setAdapter(operationAdapter);


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
            url = CompanyURL + WebAPIUrl.api_GetCADPermitNo;

            res = CommonClass.OpenConnection(url, CleansingPermitActivity.this);

            if (res != null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return res;
        }
    }

    class DownloadDepotData extends AsyncTask<String, Void, String> {
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

            String url = CompanyURL + WebAPIUrl.api_GetStationList;

            try {
                res = CommonClass.OpenConnection(url, CleansingPermitActivity.this);
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

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {
                dismissProgress();
                //Toast.makeText(CleansingPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(depotArrayList);
                editor.putString("Depot", json);
                editor.commit();
                depotAdapter = new DepotAdapter(CleansingPermitActivity.this, depotArrayList);
                spinner_station.setAdapter(depotAdapter);


            }


        }
    }

    class DownloadLocationOperationData extends AsyncTask<String, Void, String> {
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

            String url = CompanyURL + WebAPIUrl.api_GetLocationOperation + "?WarehouseId=" + StationId;

            try {
                res = CommonClass.OpenConnection(url, CleansingPermitActivity.this);
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
                    Location location = new Location();
                    location.setLocationMasterId("");
                    location.setLocationDesc("Select");
                    LocationArraylist.add(0, location);

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
                locationOperationAdapter = new LocationOperationAdapter(CleansingPermitActivity.this, LocationArraylist);
                spinner_confined_space.setAdapter(locationOperationAdapter);
                // Toast.makeText(CleansingPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                locationOperationAdapter = new LocationOperationAdapter(CleansingPermitActivity.this, LocationArraylist);
                spinner_confined_space.setAdapter(locationOperationAdapter);

                if (locationId != "") {
                    int locationpos = -1;
                    if (LocationArraylist != null) {
                        for (int j = 0; j < LocationArraylist.size(); j++) {
                            if (LocationArraylist.get(j).getLocationMasterId().equals(locationId)) {
                                locationpos = j;
                                break;
                            }
                        }
                        if (locationpos != -1)
                            spinner_confined_space.setSelection(locationpos);
                        else
                            spinner_confined_space.setSelection(0);
                    }
                }

            }


        }
    }

    public class PostUploadImageMethod extends AsyncTask<String, Void, String> {

        private Exception exception;
        String params;
        //   ProgressDialog SPdialog;


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
                //File sourceFile = new File(path);
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
//

                    //
                    CleansingPermitActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dismissProgress();
                            Toast.makeText(CleansingPermitActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();

                            // jsonArray.put(Imagefilename);
                           /* try {
                                jsonimage.put("File",Imagefilename);
                                jsonArray.put(jsonimage);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
*/

                        }
                    });


                } else {

                    if (serverResponseMessage.contains("Error")) {
                        CleansingPermitActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dismissProgress();
                                Toast.makeText(CleansingPermitActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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

    class DownloadSafetyToolsData extends AsyncTask<String, Void, String> {
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

            String url = CompanyURL + WebAPIUrl.api_GetSafetyTools;

            try {
                res = CommonClass.OpenConnection(url, CleansingPermitActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    safetyToolsArrayList = new ArrayList<>();
                    safetyToolsArrayList.clear();

                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        safetyTools = new SafetyTools();
                        JSONObject jorder = jResults.getJSONObject(i);

                        safetyTools.setSafetyToolDesc(jorder.getString("SafetyToolDesc"));
                        safetyTools.setSafetyToolMasterId(jorder.getString("SafetyToolMasterId"));
                        safetyToolsArrayList.add(safetyTools);


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
                // Toast.makeText(CleansingPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(safetyToolsArrayList);
                editor.putString("safety", json);
                editor.commit();
                safetyAdapter = new SafetyAdapter(CleansingPermitActivity.this, safetyToolsArrayList, "CAD", Mode, PermitStatus);
                grid_safety.setAdapter(safetyAdapter);


            }


        }
    }

    class DownloadGeneralCOndition extends AsyncTask<String, Void, String> {
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
            String url = CompanyURL + WebAPIUrl.api_GetDataSheet + "?form=" + formcode;

            try {
                res = CommonClass.OpenConnection(url, CleansingPermitActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //
                    //
                    // response = response.substring(1, response.length() - 1);

                    cleansingArrayList = new ArrayList<>();


                    cleansingArrayList.clear();


                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        IndicateRisk indicateRisk = new IndicateRisk();


                        indicateRisk.setPKQuesID(jorder.getString("PKQuesID"));
                        indicateRisk.setQuesText(jorder.getString("QuesText"));
                        indicateRisk.setSelectionText(jorder.getString("SelectionText"));
                        indicateRisk.setSelectionValue(jorder.getString("SelectionValue"));
                        indicateRisk.setQuesCode(jorder.getString("QuesCode"));

                        cleansingArrayList.add(indicateRisk);


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
                // Toast.makeText(CleansingPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                gson = new Gson();

                String json = gson.toJson(cleansingArrayList);

                editor.putString("general_condition", json);

                editor.commit();
                generlConditionAdapter = new GenerlConditionAdapter(CleansingPermitActivity.this, cleansingArrayList, Mode, PermitStatus);
                list_cleansing.setAdapter(generlConditionAdapter);
                Utility.setListViewHeightBasedOnItems(list_cleansing);


            }
        }
    }

    private void saveactivityjson() {


        ActivityJson = new JSONObject();
        //WorkAuthorizationNo = edit_prevention_plan.getTex
        StartDate = btn_fromdate.getText().toString();
        fromtime = btn_fromtime.getText().toString();
        totime = btn_totime.getText().toString();
        Result1 = edit_result1.getText().toString();
        Result2 = edit_result2.getText().toString();
        Result3 = edit_result3.getText().toString();
        Phase2_Top1 = edit_phase2_top1.getText().toString();
        PermitLClosedDate = btn_cleansing_date8.getText().toString();
        SpotcheckDate = btn_cleansing_date9.getText().toString();
        Centre1 = edit_phase2_centre1.getText().toString();
        Percentage1 = edit_per1.getText().toString();
        Percentage3 = edit_per3.getText().toString();
        Bottom_per1 = edit_bottom1.getText().toString();
        CleansingDate1 = btn_cleansing_date.getText().toString();
        Phase2_Top2 = edit_phase2_top2.getText().toString();
        Centre2 = edit_phase2_centre2.getText().toString();
        Percentage2 = edit_per2.getText().toString();
        Percentage4 = edit_per4.getText().toString();
        Bottom_per2 = edit_bottom2.getText().toString();
        CleansingDate2 = btn_cleansing_date1.getText().toString();
        Toxic_Top1 = edit_toxic_top.getText().toString();
        Gasname1 = edit_gasname1.getText().toString();
        Toxic_centre = edit_toxic_centre.getText().toString();
        Gasname2 = edit_gasname2.getText().toString();
        Toxic_per1 = edit_toxic_per1.getText().toString();
        Gasname3 = edit_gasname3.getText().toString();
        Toxic_per2 = edit_toxic_per2.getText().toString();
        Gasname4 = edit_gasname4.getText().toString();
        Toxic_bottom = edit_toxic_bottom.getText().toString();
        Gasname5 = edit_gasname5.getText().toString();
        CleansingDate3 = btn_cleansing_date2.getText().toString();
        Remarks = edit_remark.getText().toString();
        AuthorizeDate3 = btn_cleansing_date3.getText().toString();
        AuthorizeDate4 = btn_cleansing_date4.getText().toString();
        AuthorizeDate5 = btn_cleansing_date5.getText().toString();
        AuthorizeDate6 = btn_cleansing_date6.getText().toString();
        AuthorizeDate7 = btn_cleansing_date7.getText().toString();


        cleansingArrayList = generlConditionAdapter.getArrayList();

        if (cleansingArrayList.size() > 0) {
            if (cleansingArrayList.size() > 0) {
                user = new String[cleansingArrayList.size()];
                for (int i = 0; i < cleansingArrayList.size(); i++) {
                    String Safety = cleansingArrayList.get(i).getPKQuesID();
                    user[i] = Safety.toString();
                    entry_condtn = TextUtils.join(",", user);

                }

            }
        }

        safetyToolsArrayList = safetyAdapter.getArrayList();

        if (safetyToolsArrayList.size() > 0) {
            if (safetyToolsArrayList.size() > 0) {
                user1 = new String[safetyToolsArrayList.size()];
                for (int i = 0; i < safetyToolsArrayList.size(); i++) {
                    String Safety = safetyToolsArrayList.get(i).getSafetyToolMasterId();
                    user1[i] = Safety.toString();
                    safetytools = TextUtils.join(",", user1);
                    if (safetyToolsArrayList.get(i).getSafetyToolDesc().equals("Other(s)")) {
                        if (safetyToolsArrayList.get(i).getRemarks() != null) {
                            safety_Others = safetyToolsArrayList.get(i).getRemarks();
                        } else {
                            safety_Others = "";
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

        try {
            ActivityJson.put("FormId", PKFormId);
            ActivityJson.put("PermitNo", PermitNo);
            ActivityJson.put("FkWareHouseMasterId", StationId);
            ActivityJson.put("FKWorkAuthorizationNo", WAH_No);
            ActivityJson.put("PermitDate", StartDate);
            ActivityJson.put("PermitFromTime", fromtime);
            ActivityJson.put("PermitToTime", totime);
            ActivityJson.put("fkContractorId", contractorId);
            ActivityJson.put("fkOperationMasterId", OperationId);
            ActivityJson.put("FkLocationMasterId", locationId);
            ActivityJson.put("entry_condtn", entry_condtn);
            ActivityJson.put("Meterscreenresult", Result1);
            ActivityJson.put("MeterscreenresultSignature", Authorized5Id);
            ActivityJson.put("Airscreenresult", Result2);
            ActivityJson.put("AirscreenresultSignature", Authorized6Id);
            ActivityJson.put("Toxicgasesresult", Result3);
            ActivityJson.put("ToxicgasesresultSignature", Authorized7Id);
            ActivityJson.put("FlammableTop", Phase2_Top1);
            ActivityJson.put("FlammableCentre", Centre1);
            ActivityJson.put("FlammableManholepoint", Percentage1);
            ActivityJson.put("FlammableExtractoroutputpoint", Percentage3);
            ActivityJson.put("FlammableBottom", Bottom_per1);
            ActivityJson.put("FlammableDate", CleansingDate1);
            ActivityJson.put("FlammableSignature", Authorized8Id);
            ActivityJson.put("OxygenTop", Phase2_Top2);
            ActivityJson.put("OxygenCentre", Centre2);
            ActivityJson.put("OxygenExtractoroutputpoint", Percentage2);
            ActivityJson.put("OxygenManholepoint", Percentage4);
            ActivityJson.put("OxygenBottom", Bottom_per2);
            ActivityJson.put("OxygenDate", CleansingDate2);
            ActivityJson.put("OxygenSignature", Authorized9Id);
            ActivityJson.put("ToxicTop", Toxic_Top1);
            ActivityJson.put("ToxicTopGasName", Gasname1);
            ActivityJson.put("ToxicCentre", Toxic_centre);
            ActivityJson.put("ToxicCentreGasName", Gasname2);
            ActivityJson.put("ToxicExtractoroutputpoint", Toxic_per1);
            ActivityJson.put("ToxicExtractorpointGasName", Gasname3);
            ActivityJson.put("ToxicManholepoint", Toxic_per2);
            ActivityJson.put("ToxicManholepointGasName", Gasname4);
            ActivityJson.put("ToxicBottom", Toxic_bottom);
            ActivityJson.put("ToxicBottomGasName", Gasname5);
            ActivityJson.put("ToxicDate", CleansingDate3);
            ActivityJson.put("ToxicSignature", Authorized10Id);
            ActivityJson.put("SafetyToolMasterId", safetytools);
            ActivityJson.put("AuthorizeBy1", AuthorizedId);
            ActivityJson.put("AuthorizeBy2", Authorized1Id);
            ActivityJson.put("RespContractorId", contractorId);
            ActivityJson.put("AuthorizeBy3", Authorized3Id);
            ActivityJson.put("AuthorizeBy4", Authorized4Id);
            ActivityJson.put("MethodOperationStatus", method_of_operation);
            ActivityJson.put("phase1", Phase1);
            ActivityJson.put("phase2", Phase2);
            ActivityJson.put("PermitClosedBy", PermitClosedId);
            ActivityJson.put("PermitclosedDate", PermitLClosedDate);
            ActivityJson.put("SpotCheckBy", SpotCheckId);
            ActivityJson.put("SpotCheckDate", SpotcheckDate);
            ActivityJson.put("PermitCancelBy", cancelId);
            ActivityJson.put("PermitCancelDate", cancelDate);
            ActivityJson.put("SpotImage", "");

            /*ActivityJson.put("",AuthorizeDate3);
            ActivityJson.put("",AuthorizeDate4);
            ActivityJson.put("",AuthorizeDate5);
            ActivityJson.put("",AuthorizeDate6);
            ActivityJson.put("",AuthorizeDate7);*/
            ActivityJson.put("Remarks", Remarks);
            ActivityJson.put("CD_ProOthers", safety_Others);
            ActivityJson.put("GoldenRulesId", goldenRulesList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String FinalJsonObject = ActivityJson.toString();
        // String URL = CompanyURL+ WebAPIUrl.api_PostWorkAuthorization;
        //String op = "Success";

        if (CommonClass.checkNet(CleansingPermitActivity.this)) {
            showProgress();
            new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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

    private void editjson() {


        ActivityJson = new JSONObject();
        //WorkAuthorizationNo = edit_prevention_plan.getTex
        StartDate = btn_fromdate.getText().toString();
        fromtime = btn_fromtime.getText().toString();
        totime = btn_totime.getText().toString();
        Result1 = edit_result1.getText().toString();
        Result2 = edit_result2.getText().toString();
        Result3 = edit_result3.getText().toString();
        Phase2_Top1 = edit_phase2_top1.getText().toString();
        PermitLClosedDate = btn_cleansing_date8.getText().toString();
        SpotcheckDate = btn_cleansing_date9.getText().toString();
        cancelDate = btn_cancel_date.getText().toString();
        Centre1 = edit_phase2_centre1.getText().toString();
        Percentage1 = edit_per1.getText().toString();
        Percentage3 = edit_per3.getText().toString();
        Bottom_per1 = edit_bottom1.getText().toString();
        CleansingDate1 = btn_cleansing_date.getText().toString();
        Phase2_Top2 = edit_phase2_top2.getText().toString();
        Centre2 = edit_phase2_centre2.getText().toString();
        Percentage2 = edit_per2.getText().toString();
        Percentage4 = edit_per4.getText().toString();
        Bottom_per2 = edit_bottom2.getText().toString();
        CleansingDate2 = btn_cleansing_date1.getText().toString();
        Toxic_Top1 = edit_toxic_top.getText().toString();
        Gasname1 = edit_gasname1.getText().toString();
        Toxic_centre = edit_toxic_centre.getText().toString();
        Gasname2 = edit_gasname2.getText().toString();
        Toxic_per1 = edit_toxic_per1.getText().toString();
        Gasname3 = edit_gasname3.getText().toString();
        Toxic_per2 = edit_toxic_per2.getText().toString();
        Gasname4 = edit_gasname4.getText().toString();
        Toxic_bottom = edit_toxic_bottom.getText().toString();
        Gasname5 = edit_gasname5.getText().toString();
        CleansingDate3 = btn_cleansing_date2.getText().toString();
        Remarks = edit_remark.getText().toString();
        AuthorizeDate3 = btn_cleansing_date3.getText().toString();
        AuthorizeDate4 = btn_cleansing_date4.getText().toString();
        AuthorizeDate5 = btn_cleansing_date5.getText().toString();
        AuthorizeDate6 = btn_cleansing_date6.getText().toString();
        AuthorizeDate7 = btn_cleansing_date7.getText().toString();


        cleansingArrayList = generlConditionAdapter.getArrayList();

        if (cleansingArrayList.size() > 0) {
            if (cleansingArrayList.size() > 0) {
                user = new String[cleansingArrayList.size()];
                for (int i = 0; i < cleansingArrayList.size(); i++) {
                    String Safety = cleansingArrayList.get(i).getPKQuesID();
                    user[i] = Safety.toString();
                    entry_condtn = TextUtils.join(",", user);

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

        safetyToolsArrayList = safetyAdapter.getArrayList();

        if (safetyToolsArrayList.size() > 0) {
            if (safetyToolsArrayList.size() > 0) {
                user1 = new String[safetyToolsArrayList.size()];
                for (int i = 0; i < safetyToolsArrayList.size(); i++) {
                    String Safety = safetyToolsArrayList.get(i).getSafetyToolMasterId();
                    user1[i] = Safety.toString();
                    safetytools = TextUtils.join(",", user1);
                    if (safetyToolsArrayList.get(i).getSafetyToolDesc().equals("Other(s)")) {
                        if (safetyToolsArrayList.get(i).getRemarks() != null) {
                            safety_Others = safetyToolsArrayList.get(i).getRemarks();
                        } else {
                            safety_Others = "";
                        }
                    }
                }

            }
        }

        try {
            ActivityJson.put("FormId", PKFormId);
            ActivityJson.put("PermitNo", PermitNo);
            ActivityJson.put("FkWareHouseMasterId", StationId);
            ActivityJson.put("FKWorkAuthorizationNo", WAH_No);
            ActivityJson.put("PermitDate", StartDate);
            ActivityJson.put("PermitFromTime", fromtime);
            ActivityJson.put("PermitToTime", totime);
            ActivityJson.put("fkContractorId", contractorId);
            ActivityJson.put("fkOperationMasterId", OperationId);
            ActivityJson.put("FkLocationMasterId", locationId);
            ActivityJson.put("entry_condtn", entry_condtn);
            ActivityJson.put("Meterscreenresult", Result1);
            ActivityJson.put("MeterscreenresultSignature", Authorized5Id);
            ActivityJson.put("Airscreenresult", Result2);
            ActivityJson.put("AirscreenresultSignature", Authorized6Id);
            ActivityJson.put("Toxicgasesresult", Result3);
            ActivityJson.put("ToxicgasesresultSignature", Authorized7Id);
            ActivityJson.put("FlammableTop", Phase2_Top1);
            ActivityJson.put("FlammableCentre", Centre1);
            ActivityJson.put("FlammableManholepoint", Percentage1);
            ActivityJson.put("FlammableExtractoroutputpoint", Percentage3);
            ActivityJson.put("FlammableBottom", Bottom_per1);
            ActivityJson.put("FlammableDate", CleansingDate1);
            ActivityJson.put("FlammableSignature", Authorized8Id);
            ActivityJson.put("OxygenTop", Phase2_Top2);
            ActivityJson.put("OxygenCentre", Centre2);
            ActivityJson.put("OxygenExtractoroutputpoint", Percentage2);
            ActivityJson.put("OxygenManholepoint", Percentage4);
            ActivityJson.put("OxygenBottom", Bottom_per2);
            ActivityJson.put("OxygenDate", CleansingDate2);
            ActivityJson.put("OxygenSignature", Authorized9Id);
            ActivityJson.put("ToxicTop", Toxic_Top1);
            ActivityJson.put("ToxicTopGasName", Gasname1);
            ActivityJson.put("ToxicCentre", Toxic_centre);
            ActivityJson.put("ToxicCentreGasName", Gasname2);
            ActivityJson.put("ToxicExtractoroutputpoint", Toxic_per1);
            ActivityJson.put("ToxicExtractorpointGasName", Gasname3);
            ActivityJson.put("ToxicManholepoint", Toxic_per2);
            ActivityJson.put("ToxicManholepointGasName", Gasname4);
            ActivityJson.put("ToxicBottom", Toxic_bottom);
            ActivityJson.put("ToxicBottomGasName", Gasname5);
            ActivityJson.put("ToxicDate", CleansingDate3);
            ActivityJson.put("ToxicSignature", Authorized10Id);
            ActivityJson.put("SafetyToolMasterId", safetytools);
            ActivityJson.put("AuthorizeBy1", AuthorizedId);
            ActivityJson.put("AuthorizeBy2", Authorized1Id);
            ActivityJson.put("RespContractorId", contractorId);
            ActivityJson.put("AuthorizeBy3", Authorized3Id);
            ActivityJson.put("AuthorizeBy4", Authorized4Id);
            ActivityJson.put("MethodOperationStatus", method_of_operation);
            ActivityJson.put("phase1", Phase1);
            ActivityJson.put("phase2", Phase2);
            ActivityJson.put("PermitClosedBy", PermitClosedId);
            ActivityJson.put("PermitclosedDate", PermitLClosedDate);
            ActivityJson.put("SpotCheckBy", SpotCheckId);
            ActivityJson.put("SpotCheckDate", SpotcheckDate);
            ActivityJson.put("PermitCancelBy", cancelId);
            ActivityJson.put("PermitCancelDate", cancelDate);
            /*ActivityJson.put("",AuthorizeDate3);
            ActivityJson.put("",AuthorizeDate4);
            ActivityJson.put("",AuthorizeDate5);
            ActivityJson.put("",AuthorizeDate6);
            ActivityJson.put("",AuthorizeDate7);*/
            ActivityJson.put("Remarks", Remarks);
            ActivityJson.put("CD_ProOthers", safety_Others);
            ActivityJson.put("GoldenRulesId", goldenRulesList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String FinalEditJsonObject = ActivityJson.toString();
        // String URL = CompanyURL+ WebAPIUrl.api_PostWorkAuthorization;
        //String op = "Success";

        if (CommonClass.checkNet(CleansingPermitActivity.this)) {
            showProgress();
            new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadEditPostData().execute(FinalEditJsonObject);
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
                String url = CompanyURL + WebAPIUrl.api_CleansingPermit;
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
                Toast.makeText(CleansingPermitActivity.this, "Data save successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CleansingPermitActivity.this, SupervisorMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {
                Toast.makeText(CleansingPermitActivity.this, "Data not save successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CleansingPermitActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
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
                String url = CompanyURL + WebAPIUrl.api_PosteditCleansing;
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
                Toast.makeText(CleansingPermitActivity.this, "Data updated successfully", Toast.LENGTH_LONG).show();
                // startActivity(new Intent(CleansingPermitActivity.this,CleansingPermitActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                startActivity(new Intent(CleansingPermitActivity.this, PermitStatusActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {
                Toast.makeText(CleansingPermitActivity.this, "Data not updated successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CleansingPermitActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
            }
        }
    }

    class DownloadIsValidUser extends AsyncTask<String, Void, String> {
        String res;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CleansingPermitActivity.this);
            progressDialog.setCancelable(true);

            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String response;
            try {
                String url = CompanyURL + WebAPIUrl.api_GetIsValidUser + "?AppEnvMasterId=" +
                        URLEncoder.encode("z207", "UTF-8") + "&PlantId=" + URLEncoder.encode("1", "UTF-8") +
                        "&UserLoginId=" + URLEncoder.encode(id, "UTF-8") + "&UserPwd=" + URLEncoder.encode(Password, "UTF-8");
                res = CommonClass.OpenConnection(url, CleansingPermitActivity.this);
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

            progressDialog.dismiss();
            // dismissProgressDialog();
            //  mprogress.setVisibility(View.INVISIBLE);
            if (integer.contains("true") || integer.equals("")) {
                Toast.makeText(CleansingPermitActivity.this, "Valid Password", Toast.LENGTH_SHORT).show();
                /*if (ReasonVal.equals("1") || ReasonVal.equals("2") && Mode.equals("E")) {
                    btn_submit.setVisibility(View.VISIBLE);
                }*/

                cusdialog1.setVisibility(View.GONE);
                cusdialog2.setVisibility(View.GONE);
                hideKeyboard(CleansingPermitActivity.this);
                spinner_authorize.setSelection(0);
                edit_password_pass.setText("");


                edit_password_reason.setText("");
                spinner_permit_closed.setSelection(0);
                edit_reason.setText("");


            } else if (integer.contains("Invalid Password And Plant")) {
                CommonClass.displayToast(getApplicationContext(), "Invalid Password And Plant");
            } else if (integer.contains("Invalid Password")) {
                CommonClass.displayToast(getApplicationContext(), "Invalid Password");
            } else if (integer.contains("You are not valid user for selected plant")) {
                CommonClass.displayToast(getApplicationContext(), "You are not valid user for selected plant");
            } else {
                CommonClass.displayToast(getApplicationContext(), "Not Valid User");
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


    /*********************************Not Used************************************/

    private void passworddialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CleansingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText edt_password = dialogView.findViewById(R.id.edt_password);
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        spinner_authorize = dialogView.findViewById(R.id.spinner_authorize);
        // TextView txt_resend_otp=dialogView.findViewById(R.id.txt_resend_otp);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        b.show();
        // if button is clicked, close the custom dialog

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_authorize.setText("Select");

                b.dismiss();
            }
        });
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = edt_password.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                if (Password.equalsIgnoreCase("")) {

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Please enter password");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, -160);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                } else {
                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }
        });

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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
            if (authorizedPersonArrayList.size() > 0) {
                authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);
                //authorizedPersonAdapter1 = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);


                ArrayAdapter<AuthorizedPerson> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                //spinner_authorize1.setAdapter(authorizedPersonAdapter);
                // spinner_authorize3.setAdapter(authorizedPersonAdapter);
                //spinner_authorize4.setAdapter(authorizedPersonAdapter);
                //spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                //spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            }

        }


        /*spinner_authorize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    }else {
                        authorize = authorizedPersonArrayList.get(position).getAuthorizeid();
                        String name = authorizedPersonArrayList.get(position).getAuthorizename();
                        txt_authorize.setText(name);


                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/


    }

    private void authorize1dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CleansingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText edt_password = dialogView.findViewById(R.id.edt_password);
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        spinner_authorize1 = dialogView.findViewById(R.id.spinner_authorize);
        // TextView txt_resend_otp=dialogView.findViewById(R.id.txt_resend_otp);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        b.show();
        // if button is clicked, close the custom dialog

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_authorize1.setText("Select");

                b.dismiss();
            }
        });
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = edt_password.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                if (Password.equalsIgnoreCase("")) {

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Please enter password");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, -160);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                } else {
                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }
        });

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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
            if (authorizedPersonArrayList.size() > 0) {
                authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);
                //authorizedPersonAdapter1 = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);


                ArrayAdapter<AuthorizedPerson> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, authorizedPersonArrayList);
                // spinner_authorize.setAdapter(authorizedPersonAdapter);
                spinner_authorize1.setAdapter(authorizedPersonAdapter);
                // spinner_authorize3.setAdapter(authorizedPersonAdapter);
                //spinner_authorize4.setAdapter(authorizedPersonAdapter);
                //spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                //spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            }

        }


        spinner_authorize1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    } else {
                        authorize1 = authorizedPersonArrayList.get(position).getAuthorizeid();
                        String name = authorizedPersonArrayList.get(position).getAuthorizename();
                        txt_authorize1.setText(name);


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void authorize3dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CleansingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText edt_password = dialogView.findViewById(R.id.edt_password);
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        spinner_authorize3 = dialogView.findViewById(R.id.spinner_authorize);
        // TextView txt_resend_otp=dialogView.findViewById(R.id.txt_resend_otp);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        b.show();
        // if button is clicked, close the custom dialog

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_authorize3.setText("Select");

                b.dismiss();
            }
        });
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = edt_password.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                if (Password.equalsIgnoreCase("")) {

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Please enter password");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, -160);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                } else {
                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }
        });

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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
            if (authorizedPersonArrayList.size() > 0) {
                authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);
                //authorizedPersonAdapter1 = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);


                ArrayAdapter<AuthorizedPerson> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, authorizedPersonArrayList);
                spinner_authorize3.setAdapter(authorizedPersonAdapter);

            }

        }


        spinner_authorize3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    } else {
                        authorize3 = authorizedPersonArrayList.get(position).getAuthorizeid();
                        String name = authorizedPersonArrayList.get(position).getAuthorizename();
                        txt_authorize3.setText(name);


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void authorize4dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CleansingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText edt_password = dialogView.findViewById(R.id.edt_password);
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        spinner_authorize4 = dialogView.findViewById(R.id.spinner_authorize);
        // TextView txt_resend_otp=dialogView.findViewById(R.id.txt_resend_otp);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        b.show();
        // if button is clicked, close the custom dialog

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_authorize4.setText("Select");

                b.dismiss();
            }
        });
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = edt_password.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                if (Password.equalsIgnoreCase("")) {

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Please enter password");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, -160);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                } else {
                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }
        });

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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
            if (authorizedPersonArrayList.size() > 0) {
                authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);
                //authorizedPersonAdapter1 = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);


                ArrayAdapter<AuthorizedPerson> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, authorizedPersonArrayList);
                // spinner_authorize.setAdapter(authorizedPersonAdapter);
                //spinner_authorize1.setAdapter(authorizedPersonAdapter);
                //spinner_authorize3.setAdapter(authorizedPersonAdapter);
                spinner_authorize4.setAdapter(authorizedPersonAdapter);
                //spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                //spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            }

        }


        spinner_authorize4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    } else {
                        authorize4 = authorizedPersonArrayList.get(position).getAuthorizeid();
                        String name = authorizedPersonArrayList.get(position).getAuthorizename();
                        txt_authorize4.setText(name);


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void authorize5dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CleansingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText edt_password = dialogView.findViewById(R.id.edt_password);
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        spinner_authorize5 = dialogView.findViewById(R.id.spinner_authorize);
        // TextView txt_resend_otp=dialogView.findViewById(R.id.txt_resend_otp);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        b.show();
        // if button is clicked, close the custom dialog

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_authorize5.setText("Select");

                b.dismiss();
            }
        });
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = edt_password.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                if (Password.equalsIgnoreCase("")) {

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Please enter password");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, -160);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                } else {
                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }
        });

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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
            if (authorizedPersonArrayList.size() > 0) {
                authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);
                //authorizedPersonAdapter1 = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);


                ArrayAdapter<AuthorizedPerson> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, authorizedPersonArrayList);
                // spinner_authorize.setAdapter(authorizedPersonAdapter);
                //spinner_authorize1.setAdapter(authorizedPersonAdapter);
                //spinner_authorize3.setAdapter(authorizedPersonAdapter);
                //spinner_authorize4.setAdapter(authorizedPersonAdapter);
                spinner_authorize5.setAdapter(authorizedPersonAdapter);
                //spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                //spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            }

        }


        spinner_authorize5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    } else {
                        authorize5 = authorizedPersonArrayList.get(position).getAuthorizeid();
                        String name = authorizedPersonArrayList.get(position).getAuthorizename();
                        txt_authorize5.setText(name);


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void authorize6dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CleansingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText edt_password = dialogView.findViewById(R.id.edt_password);
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        spinner_authorize6 = dialogView.findViewById(R.id.spinner_authorize);
        // TextView txt_resend_otp=dialogView.findViewById(R.id.txt_resend_otp);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        b.show();
        // if button is clicked, close the custom dialog

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_authorize6.setText("Select");

                b.dismiss();
            }
        });
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = edt_password.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                if (Password.equalsIgnoreCase("")) {

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Please enter password");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, -160);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                } else {
                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }
        });

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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
            if (authorizedPersonArrayList.size() > 0) {
                authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);
                //authorizedPersonAdapter1 = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);


                ArrayAdapter<AuthorizedPerson> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, authorizedPersonArrayList);
                // spinner_authorize.setAdapter(authorizedPersonAdapter);
                //spinner_authorize1.setAdapter(authorizedPersonAdapter);
                //spinner_authorize3.setAdapter(authorizedPersonAdapter);
                //spinner_authorize4.setAdapter(authorizedPersonAdapter);
                spinner_authorize6.setAdapter(authorizedPersonAdapter);
                //spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                //spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            }

        }


        spinner_authorize6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    } else {
                        authorize6 = authorizedPersonArrayList.get(position).getAuthorizeid();
                        String name = authorizedPersonArrayList.get(position).getAuthorizename();
                        txt_authorize6.setText(name);


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void authorize7dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CleansingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText edt_password = dialogView.findViewById(R.id.edt_password);
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        spinner_authorize7 = dialogView.findViewById(R.id.spinner_authorize);
        // TextView txt_resend_otp=dialogView.findViewById(R.id.txt_resend_otp);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        b.show();
        // if button is clicked, close the custom dialog

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_authorize7.setText("Select");
                b.dismiss();
            }
        });
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = edt_password.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                if (Password.equalsIgnoreCase("")) {

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Please enter password");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, -160);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                } else {
                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }
        });

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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
            if (authorizedPersonArrayList.size() > 0) {
                authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);
                //authorizedPersonAdapter1 = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);


                ArrayAdapter<AuthorizedPerson> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, authorizedPersonArrayList);
                // spinner_authorize.setAdapter(authorizedPersonAdapter);
                //spinner_authorize1.setAdapter(authorizedPersonAdapter);
                //spinner_authorize3.setAdapter(authorizedPersonAdapter);
                //spinner_authorize4.setAdapter(authorizedPersonAdapter);
                spinner_authorize7.setAdapter(authorizedPersonAdapter);
                //spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                //spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            }

        }


        spinner_authorize7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    } else {
                        authorize7 = authorizedPersonArrayList.get(position).getAuthorizeid();
                        String name = authorizedPersonArrayList.get(position).getAuthorizename();
                        txt_authorize7.setText(name);


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void authorize8dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CleansingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText edt_password = dialogView.findViewById(R.id.edt_password);
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        spinner_authorize8 = dialogView.findViewById(R.id.spinner_authorize);
        // TextView txt_resend_otp=dialogView.findViewById(R.id.txt_resend_otp);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        b.show();
        // if button is clicked, close the custom dialog

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_authorize8.setText("Select");
                b.dismiss();
            }
        });
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = edt_password.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                if (Password.equalsIgnoreCase("")) {

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Please enter password");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, -160);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                } else {
                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }
        });

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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
            if (authorizedPersonArrayList.size() > 0) {
                authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);
                //authorizedPersonAdapter1 = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);


                ArrayAdapter<AuthorizedPerson> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, authorizedPersonArrayList);
                // spinner_authorize.setAdapter(authorizedPersonAdapter);
                //spinner_authorize1.setAdapter(authorizedPersonAdapter);
                //spinner_authorize3.setAdapter(authorizedPersonAdapter);
                //spinner_authorize4.setAdapter(authorizedPersonAdapter);
                spinner_authorize8.setAdapter(authorizedPersonAdapter);
                //spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                //spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            }

        }


        spinner_authorize8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    } else {
                        authorize8 = authorizedPersonArrayList.get(position).getAuthorizeid();
                        String name = authorizedPersonArrayList.get(position).getAuthorizename();
                        txt_authorize8.setText(name);


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void authorize9dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CleansingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText edt_password = dialogView.findViewById(R.id.edt_password);
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        spinner_authorize9 = dialogView.findViewById(R.id.spinner_authorize);
        // TextView txt_resend_otp=dialogView.findViewById(R.id.txt_resend_otp);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        b.show();
        // if button is clicked, close the custom dialog

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_authorize9.setText("Select");
                b.dismiss();
            }
        });
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = edt_password.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                if (Password.equalsIgnoreCase("")) {

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Please enter password");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, -160);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                } else {
                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }
        });

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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
            if (authorizedPersonArrayList.size() > 0) {
                authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);
                //authorizedPersonAdapter1 = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);


                ArrayAdapter<AuthorizedPerson> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, authorizedPersonArrayList);
                // spinner_authorize.setAdapter(authorizedPersonAdapter);
                //spinner_authorize1.setAdapter(authorizedPersonAdapter);
                //spinner_authorize3.setAdapter(authorizedPersonAdapter);
                //spinner_authorize4.setAdapter(authorizedPersonAdapter);
                spinner_authorize9.setAdapter(authorizedPersonAdapter);
                //spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                //spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            }

        }


        spinner_authorize9.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    } else {
                        authorize9 = authorizedPersonArrayList.get(position).getAuthorizeid();
                        String name = authorizedPersonArrayList.get(position).getAuthorizename();
                        txt_authorize9.setText(name);


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void authorize10dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CleansingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText edt_password = dialogView.findViewById(R.id.edt_password);
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        spinner_authorize10 = dialogView.findViewById(R.id.spinner_authorize);
        // TextView txt_resend_otp=dialogView.findViewById(R.id.txt_resend_otp);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        b.show();
        // if button is clicked, close the custom dialog

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_authorize10.setText("Select");
                b.dismiss();
            }
        });
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = edt_password.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                if (Password.equalsIgnoreCase("")) {

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Please enter password");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, -160);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                } else {
                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }
        });

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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
            if (authorizedPersonArrayList.size() > 0) {
                authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);
                //authorizedPersonAdapter1 = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);


                ArrayAdapter<AuthorizedPerson> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, authorizedPersonArrayList);
                // spinner_authorize.setAdapter(authorizedPersonAdapter);
                //spinner_authorize1.setAdapter(authorizedPersonAdapter);
                //spinner_authorize3.setAdapter(authorizedPersonAdapter);
                //spinner_authorize4.setAdapter(authorizedPersonAdapter);
                spinner_authorize10.setAdapter(authorizedPersonAdapter);
                //spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                //spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            }

        }


        spinner_authorize10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    } else {
                        authorize10 = authorizedPersonArrayList.get(position).getAuthorizeid();
                        String name = authorizedPersonArrayList.get(position).getAuthorizename();
                        txt_authorize10.setText(name);


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


     /*  spinner_authorize3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    }else {
                        authorize3 = authorizedPersonArrayList.get(position).getAuthorizeid();
                        if (authorize3.equals("--Select--")) {

                            // Toast.makeText(WorkAuthorizationActivity.this, "Error", Toast.LENGTH_LONG).show();
                        } else {
                            passworddialog();
                        }
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        /*spinner_authorize4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    }else {
                        authorize4 = authorizedPersonArrayList.get(position).getAuthorizeid();
                        if (authorize4.equals("--Select--")) {

                            // Toast.makeText(WorkAuthorizationActivity.this, "Error", Toast.LENGTH_LONG).show();
                        } else {
                            passworddialog();
                        }
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        /*spinner_authorize5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(++check > 1){
                    if(authorizedPersonArrayList == null){

                    }else{
                        authorize5 = authorizedPersonArrayList.get(position).getAuthorizeid();

                        if (authorize5.equals("--Select--")) {

                            // Toast.makeText(WorkAuthorizationActivity.this, "Error", Toast.LENGTH_LONG).show();
                        } else {
                            passworddialog();
                        }
                    }

                }else{


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
      /*  spinner_authorize6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(++check > 1){
                    if(authorizedPersonArrayList == null){

                    }else{
                        authorize6 = authorizedPersonArrayList.get(position).getAuthorizeid();

                        if (authorize6.equals("--Select--")) {

                            // Toast.makeText(WorkAuthorizationActivity.this, "Error", Toast.LENGTH_LONG).show();
                        } else {
                            passworddialog();
                        }
                    }

                }else{


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
      */  /*spinner_authorize7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(++check > 1){
                    if(authorizedPersonArrayList == null){

                    }else{
                        authorize7 = authorizedPersonArrayList.get(position).getAuthorizeid();

                        if (authorize7.equals("--Select--")) {

                            // Toast.makeText(WorkAuthorizationActivity.this, "Error", Toast.LENGTH_LONG).show();
                        } else {
                            passworddialog();
                        }
                    }

                }else{


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/



/*
        spinner_authorize8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(++check > 1){
                    if(authorizedPersonArrayList == null){

                    }else{
                        authorize8 = authorizedPersonArrayList.get(position).getAuthorizeid();

                        if (authorize8.equals("--Select--")) {

                            // Toast.makeText(WorkAuthorizationActivity.this, "Error", Toast.LENGTH_LONG).show();
                        } else {
                            passworddialog();
                        }
                    }

                }else{


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

       /* spinner_authorize9.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(++check > 1){
                    if(authorizedPersonArrayList == null){

                    }else{
                        authorize9 = authorizedPersonArrayList.get(position).getAuthorizeid();

                        if (authorize9.equals("--Select--")) {

                            // Toast.makeText(WorkAuthorizationActivity.this, "Error", Toast.LENGTH_LONG).show();
                        } else {
                            passworddialog();
                        }
                    }

                }else{


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        /*spinner_authorize10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(++check > 1){
                    if(authorizedPersonArrayList == null){

                    }else{
                        authorize10 = authorizedPersonArrayList.get(position).getAuthorizeid();

                        if (authorize10.equals("--Select--")) {

                            // Toast.makeText(WorkAuthorizationActivity.this, "Error", Toast.LENGTH_LONG).show();
                        } else {
                            passworddialog();
                        }
                    }

                }else{


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

*/


       /* spinner_permit_closed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    } else {
                        PermitClosed = authorizedPersonArrayList.get(position).getAuthorizeid();
                        if (PermitClosed.equals("--Select--")) {
                            // Toast.makeText(WorkAuthorizationActivity.this, "Error", Toast.LENGTH_LONG).show();
                        } else {
                            Reasondialog();
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        /*spinner_spotcheck.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(++check > 1) {
                     if (authorizedPersonArrayList == null) {

                    }else {
                        SpotCheck = authorizedPersonArrayList.get(position).getAuthorizeid();
                        if (SpotCheck.equals("--Select--")) {
                            // Toast.makeText(WorkAuthorizationActivity.this, "Error", Toast.LENGTH_LONG).show();
                        } else {
                            passworddialog();
                        }


                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/


    public class DownloadWANo extends AsyncTask<String, Void, String> {
        String res = "", response = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String Id = params[0];

            String url = CompanyURL + WebAPIUrl.api_GetWANo + "?contractid=" + Id + "&permitOperationcode=CAD";


            res = CommonClass.OpenConnection(url, CleansingPermitActivity.this);
            response = res;


            return response;
        }


        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);

            if (!resp.equals("[]")) {


                try {
                    JSONArray jsonArray = new JSONArray(resp);

                    WAArayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jorder = jsonArray.getJSONObject(i);
                        PermitNoWA permitNo = new PermitNoWA();


                        permitNo.setPermitNo(jorder.getString("permitno"));
                        //jorder.getString("permitno");
                        WAArayList.add(permitNo);
                    }

                    PermitNoWA permitNoWA = new PermitNoWA();
                    permitNoWA.setPermitNo("Select");
                    WAArayList.add(0, permitNoWA);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (WAArayList.size() > 0) {
                    ContractorPermitAdapter contractorPermitAdapter = new ContractorPermitAdapter(CleansingPermitActivity.this, WAArayList);
                    spinner_prevention_plan.setAdapter(contractorPermitAdapter);
                    spinner_contractor.setEnabled(false);
                    if (Mode.equals("E")) {
                        int pos = -1;
                        for (int i = 0; i < WAArayList.size(); i++) {
                            if (WAH_No.equals(WAArayList.get(i).getPermitNo())) {
                                pos = i;
                                break;
                            }
                        }
                        if (pos != -1) {
                            spinner_prevention_plan.setSelection(pos);
                        } else {
                            spinner_prevention_plan.setSelection(0);
                        }
                    }

                    if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                        showProgress();
                        //Location Get
                        new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                if (WAH_No.equals("")) {
                                    new DownloadGETWA_PermitNoDetail().execute(WAArayList.get(0).getPermitNo());
                                } else {
                                    new DownloadGETWA_PermitNoDetail().execute(WAH_No);
                                }
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                CommonClass.displayToast(getApplicationContext(), msg);
                                dismissProgress();
                            }
                        });
                    } else {
                        Toast.makeText(CleansingPermitActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                    //spinner_permit_closed.setAdapter(authorizedPersonAdapter);
                } else {
                    spinner_contractor.setEnabled(false);
                    spinner_prevention_plan.setSelection(0);
                }


            } else {
                WAArayList.clear();

                if (Mode.equals("E")) {
                    if (WAH_No != "") {
                        PermitNoWA permitNo = new PermitNoWA();
                        permitNo.setPermitNo(WAH_No);
                        WAArayList.add(0, permitNo);
                        ArrayAdapter<PermitNoWA> permitNoArrayAdapter = new ArrayAdapter<>(CleansingPermitActivity.this,
                                android.R.layout.simple_spinner_item, WAArayList);
                        spinner_prevention_plan.setAdapter(permitNoArrayAdapter);
                    }
                } else {
                    Toast.makeText(CleansingPermitActivity.this, "No WorkAuthorization Present Against Selected Contractor", Toast.LENGTH_SHORT).show();
                    PermitNoWA permitNo = new PermitNoWA();
                    permitNo.setPermitNo("Select");
                    WAArayList.add(0, permitNo);
                    ContractorPermitAdapter contractorPermitAdapter = new ContractorPermitAdapter(CleansingPermitActivity.this, WAArayList);
                    spinner_prevention_plan.setAdapter(contractorPermitAdapter);
                    spinner_operation.setSelection(0);
                    spinner_confined_space.setSelection(0);
                }
            }


        }


    }

    public class DownloadGETWA_PermitNoDetail extends AsyncTask<String, Void, String> {

        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String Id = params[0];

            String url = CompanyURL + WebAPIUrl.api_GETWA_PermitNoDetail + "?permitno=" + Id;


            res = CommonClass.OpenConnection(url, CleansingPermitActivity.this);
            response = res;


            return response;


        }


        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            dismissProgress();
            if (!response.equals("[]")) {

                // waNoDetails.clear();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jorder = jsonArray.getJSONObject(i);
                        if (modeefirst == -1) {
                            WAStartTime = jorder.getString("FromTime1");
                            WAEndTime1 = jorder.getString("ToTime2");
                            WAEndTime = jorder.getString("ToTime2");
                            btn_fromtime.setText(WAStartTime);
                            //   edt_totime.setText(WAEndTime);
                            String WstartTime = DateFormatChange.formateDateFromstring("hh:mm aa", "HH:mm", WAStartTime);
                            String[] e = WstartTime.split(":");
                            WAStartTimeHr = Integer.parseInt(e[0]);
                            WAStartTimemin = Integer.parseInt(e[1].split(" ")[0]);

                            String wendTime = DateFormatChange.formateDateFromstring("hh:mm aa", "HH:mm", WAEndTime);
                            String[] split = wendTime.split(":");
                            WAEndTimeHr = Integer.parseInt(split[0]);
                            WAEndTimeMin = Integer.parseInt(split[1].split(" ")[0]);
                            //   UpdateTime.updateTime((WAStartTimeHr+4),WAStartTimemin);
                            if ((WAStartTimeHr + 4) > 13 && (WAStartTimeHr + 4) < 14) {
                                WAEndTime = "01:00 PM";
                            } else {
                                if (WAEndTimeHr > 14 && (WAStartTimeHr + 4) < 13) {
                                    WAEndTime = UpdateTime.updateTime(WAStartTimeHr + 1, WAStartTimemin);
                                } else {
                                    if (WAEndTimeHr >= WAStartTimeHr + 4) {
                                        WAEndTime = UpdateTime.updateTime((WAStartTimeHr + 4), WAStartTimemin);
                                    } else {
                                        WAEndTime = jorder.getString("ToTime2");
                                        Toast.makeText(CleansingPermitActivity.this, "You cannot choose time greater than work authorization end time", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            btn_totime.setText(WAEndTime);
                            modeefirst = 1;
                        }

                        OperationId = jorder.getString("fkOperationMasterId");

                        int operationpos = -1;
                        if (operationArrayList != null) {
                            for (int j = 0; j < operationArrayList.size(); j++) {
                                if (operationArrayList.get(j).getOperationMasterId().equals(OperationId)) {
                                    operationpos = j;
                                    break;
                                }
                            }

                            if (operationpos != -1)
                                spinner_operation.setSelection(operationpos);
                            else
                                spinner_operation.setSelection(0);
                        }


                        StationId = jorder.getString("FkWareHouseMasterId");
                        //StationName = jorder.getString("WarehouseDescription");
                        int depotpos = -1;
                        if (depotArrayList != null) {
                            for (int j = 0; j < depotArrayList.size(); j++) {
                                if (depotArrayList.get(j).getDepotid().equals(StationId)) {
                                    depotpos = j;
                                    break;
                                }

                            }
                            if (depotpos != -1)
                                spinner_station.setSelection(depotpos);
                            else
                                spinner_station.setSelection(0);

                        }
                        try {
                            int locationpos = -1;
                            locationId = jorder.getString("FkLocationMasterId");

                            if (LocationArraylist != null) {
                                for (int j = 0; j < LocationArraylist.size(); j++) {
                                    if (LocationArraylist.get(j).getLocationMasterId().equals(locationId)) {
                                        locationpos = j;
                                        break;
                                    }

                                }
                                if (locationpos != -1)
                                    spinner_confined_space.setSelection(locationpos);
                                else
                                    spinner_confined_space.setSelection(0);
                            } else {
                                if (CommonClass.checkNet(CleansingPermitActivity.this)) {
                                    new StartSession(CleansingPermitActivity.this, new CallbackInterface() {
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

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner_contractor.setEnabled(false);
                spinner_confined_space.setEnabled(false);
                spinner_operation.setEnabled(false);

            } else {
                if (contractorId != "") {
                    spinner_contractor.setEnabled(true);
                }
                //  spinner_location.setEnabled(true);
                //  spinner_operation.setEnabled(true);
                if (OperationId == "") {
                    spinner_operation.setSelection(0);
                }

                if (locationId == "") {
                    spinner_confined_space.setSelection(0);
                }


                Toast.makeText(CleansingPermitActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
            }


        }
    }


    public boolean timedifference(String time, String to_time) {

        boolean flag = false;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        try {
            Date startDate = simpleDateFormat.parse(time);
            Date endDate = simpleDateFormat.parse(to_time);

            long difference = endDate.getTime() - startDate.getTime();
            if (endDate.getTime() < startDate.getTime()) {
                flag = false;
            } else
                flag = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private boolean isValidate() {
        if (checkbox_phase1.isChecked()) {
            if (edit_result1.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 1", Toast.LENGTH_SHORT).show();
                edit_result1.setBackgroundResource(R.drawable.editext_red_line);
                return false;
            } else if (edit_result2.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 1", Toast.LENGTH_SHORT).show();
                edit_result2.setBackgroundResource(R.drawable.editext_red_line);
                return false;
            } else if (edit_result3.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 1", Toast.LENGTH_SHORT).show();
                edit_result3.setBackgroundResource(R.drawable.editext_red_line);
                return false;
            } else if (Authorized5Id.equalsIgnoreCase("") || Authorized5Id.equalsIgnoreCase("Select")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill Meter Screen  signature details", Toast.LENGTH_SHORT).show();
                ln_authorize5.setBackgroundResource(R.drawable.edit_text_red);
                return false;
            } else if (txt_authorize6.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 1", Toast.LENGTH_SHORT).show();
                ln_authorize6.setBackgroundResource(R.drawable.edit_text_red);
                return false;
            } else if (Authorized7Id.equalsIgnoreCase("") || Authorized7Id.equalsIgnoreCase("Select")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill toxic gas signature details", Toast.LENGTH_SHORT).show();
                ln_authorize7.setBackgroundResource(R.drawable.edit_text_red);
                return false;
            } else {
                return true;

            }
        } else if (checkbox_phase2.isChecked()) {
            if (edit_phase2_top1.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 2", Toast.LENGTH_SHORT).show();
                edit_phase2_top1.setBackgroundResource(R.drawable.editext_red_line);
                return false;
            } else if (edit_phase2_centre1.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 2", Toast.LENGTH_SHORT).show();
                edit_phase2_centre1.setBackgroundResource(R.drawable.editext_red_line);
                return false;
            } else if (edit_per1.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 2", Toast.LENGTH_SHORT).show();
                edit_per1.setBackgroundResource(R.drawable.editext_red_line);
                return false;
            } else if (edit_per3.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 2", Toast.LENGTH_SHORT).show();
                edit_per3.setBackgroundResource(R.drawable.editext_red_line);
                return false;
            } else if (edit_bottom1.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 2", Toast.LENGTH_SHORT).show();
                edit_bottom1.setBackgroundResource(R.drawable.editext_red_line);
                return false;
            } else if (Authorized8Id.equalsIgnoreCase("") || Authorized8Id.equalsIgnoreCase("Select")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill flammable signature details", Toast.LENGTH_SHORT).show();
                ln_authorize8.setBackgroundResource(R.drawable.edit_text_red);
            } else if (edit_phase2_top2.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 2", Toast.LENGTH_SHORT).show();
                edit_phase2_top2.setBackgroundResource(R.drawable.editext_red_line);
                return false;
            } else if (edit_phase2_centre2.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 2", Toast.LENGTH_SHORT).show();
                edit_phase2_top2.setBackgroundResource(R.drawable.editext_red_line);
                return false;
            } else if (edit_per2.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 2", Toast.LENGTH_SHORT).show();
                edit_per2.setBackgroundResource(R.drawable.editext_red_line);

            } else if (edit_per4.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 2", Toast.LENGTH_SHORT).show();
                edit_per4.setBackgroundResource(R.drawable.editext_red_line);
                return false;
            } else if (edit_bottom2.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 2", Toast.LENGTH_SHORT).show();
                edit_bottom2.setBackgroundResource(R.drawable.editext_red_line);
                return false;
            } else if (Authorized9Id.equalsIgnoreCase("") || Authorized9Id.equalsIgnoreCase("Select")) {
                Toast.makeText(CleansingPermitActivity.this, "Please fill complete details of Phase - 2", Toast.LENGTH_SHORT).show();
                ln_authorize9.setBackgroundResource(R.drawable.edit_text_red);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
        return true;
    }

    private void CategoryWiseAuthorizeName(String categorydesc, String From) {
        String arrivalFrom = From;
        categoryDesc = categorydesc;

        txt_authorizeArrayList = new ArrayList<>();
        if (!StationId.equals("Select")) {
            if (authorizedPersonArrayList != null) {


                if (categoryDesc.equalsIgnoreCase("level 2")) {
                    for (int i = 0; i < authorizedPersonArrayList.size(); i++) {
                        if (authorizedPersonArrayList.get(i).getCategorydesc() == null ||
                                authorizedPersonArrayList.get(i).getCategorydesc().equals("Tier1")
                                || authorizedPersonArrayList.get(i).getCategorydesc().equals("Tier2")
                                || authorizedPersonArrayList.get(i).getCategorydesc().equals("Admin")) {

                            AuthorizedPerson authorizedPerson = new AuthorizedPerson();
                            authorizedPerson.setAuthorizeid(authorizedPersonArrayList.get(i).getAuthorizeid());
                            authorizedPerson.setAuthorizename(authorizedPersonArrayList.get(i).getAuthorizename());
                            authorizedPerson.setCategorydesc(authorizedPersonArrayList.get(i).getCategorydesc());
                            authorizedPerson.setUserLoginId(authorizedPersonArrayList.get(i).getUserLoginId());
                            txt_authorizeArrayList.add(authorizedPerson);

                        }
                    }
                } else if (categoryDesc.equalsIgnoreCase("level 1")) {
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
                } else if (categoryDesc.equalsIgnoreCase("level 0")) {

                }
                if (txt_authorizeArrayList.size() != 0) {
                    authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, txt_authorizeArrayList);
                    if (arrivalFrom.equalsIgnoreCase("fromReason")) {
                        spinner_permit_closed.setAdapter(authorizedPersonAdapter);
                    } else if (arrivalFrom.equalsIgnoreCase("fromPWD")) {
                        spinner_authorize.setAdapter(authorizedPersonAdapter);
                    }
                }
                //authorizedPersonAdapter.updateList(txt_authorizeArrayList);
                // spinner_authorize.setAdapter(authorizedPersonAdapter);
            }
        } else {
            AuthorizedPerson authorizedPerson = new AuthorizedPerson();
            authorizedPerson.setAuthorizename("Select");
            txt_authorizeArrayList.add(authorizedPerson);
            authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, txt_authorizeArrayList);
            spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            spinner_authorize.setAdapter(authorizedPersonAdapter);
        }

    }

    private class DownloadGoldenRules extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        // ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_GetGoldenRules;

            try {
                res = CommonClass.OpenConnection(url, CleansingPermitActivity.this);
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
                        goldenRules = new GoldenRules();
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
            dismissProgress();
            if (response.contains("[]")) {
                //Toast.makeText(WorkAuthorizationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CleansingPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(goldenRulesArrayList);
                editor.putString("GoldenRules", json);
                editor.commit();
                goldenRuleAdapter = new GoldenRuleAdapter(CleansingPermitActivity.this, goldenRulesArrayList, Mode, PermitStatus);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CleansingPermitActivity.this);
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
                res = CommonClass.OpenConnection(url, CleansingPermitActivity.this);
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
                authorizedPersonAdapter = new AuthorizedPersonAdapter(CleansingPermitActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);


            }


        }
    }

}

