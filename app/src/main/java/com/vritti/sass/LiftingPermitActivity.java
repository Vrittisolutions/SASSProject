package com.vritti.sass;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.vritti.sass.adapter.GoldenRuleAdapter;
import com.vritti.sass.adapter.LocationOperationAdapter;
import com.vritti.sass.adapter.OperationAdapter;
import com.vritti.sass.adapter.PermitContractorListAdapter;
import com.vritti.sass.adapter.SafetyAdapter;
import com.vritti.sass.model.AuthorizedPerson;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.ContractorList;
import com.vritti.sass.model.DateFormatChange;
import com.vritti.sass.model.Depot;
import com.vritti.sass.model.GoldenRules;
import com.vritti.sass.model.Location;
import com.vritti.sass.model.Operation;
import com.vritti.sass.model.Permit;
import com.vritti.sass.model.PermitNoWA;
import com.vritti.sass.model.SafetyTools;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.UpdateTime;
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
 * Created by sharvari on 27-Nov-18.
 */

public class LiftingPermitActivity extends AppCompatActivity {

    Button btn_fromtime, btn_totime, btn_fromdate, btn_todate, btn_liftingdate, btn_liftingdate1, btn_liftingdate2, btn_liftingdate3,
            btn_liftingdate4, btn_submit, btn_last_certificate,btn_cancel_date;
    LinearLayout ln_station, ln_WAHNo, ln_contractor, ln_DescOperation, ln_location;
    DatePickerDialog datePickerDialog;
    RadioGroup radiogroup_estimated_wght, radiogroup_gasdetect, radiogroup_emergency_stop, radiogroup_loadingpt, radiogroup_comm_crew,
            radiogroup_grndcondtn, radiogroup_safety, radiogroup_light_adequate, radiogroup_demarcation;
    Spinner spinner_prevention_plan, spinner_station, spinner_operation, spinner_location, spinner_authorize, spinner_authorize1, spinner_authorize2,
            spinner_permit_closed, spinner_spotcheck, spinner_contractor;
    EditText edit_desc_work, edit_loc_operation, edit_desc_load, edit_dimensions,
            edit_load, edit_distance, edit_height, edit_lift_types, edit_safe_work, edit_permitno, edit_mac_maxlen, edit_rad_dist, edit_safework_load, edit_liftgear_types, edit_certificate_gears, edit_Precaution1, edit_precaution2, edit_Precaution3, edit_precaution4, edit_specify, edit_remarks, edit_supervisor_name,
            edit_supervisor_certi, edit_craneoperator_name, edit_craneoperator_certi, edit_rigger_name, edit_rigger_certi, edit_signalman_name, edit_signalman_certi;
    String StartDate = "", fromtime = "", totime = "", descload = "", overalldimension = "", WghtLoad = "", ShiftedLoad = "", ShiftedHeight = "",
            lifttypes = "", safewrk = "", lstcertificateDate = "", maxlen = "", raddistance = "", safeworkload = "", liftgeartypes = "", gearcertificate = "", GroundPrecaution1 = "",
            Precaution2 = "", Precaution3 = "", Precaution4 = "", specifyprecaution = "", PermitclosedDate = "", spotcheckdate = "", Remarks = "", Supervisor_Name = "", Supervisor_certi = "",
            CraneOperator_Name = "", CraneOperator_Certi = "", RiggerName = "", RiggerCerti = "", Signalman_Name = "", Signalman_certi = "",
            Password = "", WAH_No = "", to_time = "",cancelDate="";
    RadioButton rd_gasdetect_no, rd_gasdetect_yes;
    RadioButton rd_emergency_no, rd_emergency_yes;
    RadioButton rd_loadyes, rd_loadno;
    RadioButton rd_radio, rd_signal;
    RadioButton rd_obstacle_yes, rd_obstacle_no;
    RadioButton rd_grncdtn_no, rd_grncdtn_yes;
    RadioButton rd_safety_no, rd_safety_yes;
    RadioButton rd_dem_yes, rd_dem_no;
    RadioButton rd_light_no, rd_light_yes;
    RadioButton radio_known_weight, radio_estimated_weight;
    RadioGroup radiogroup_obstacle;
    String date;
    String data = "", safetytools = "", response = "", safety_Others = "";
    String[] user;
    String[] user5;
    int Year, month, day;
    ImageView img_camera;
    int MY_CAMERA_PERMISSION_CODE = 100;
    int MEDIA_TYPE_IMAGE = 1;
    int CAMERA_REQUEST = 101;
    private Uri fileUri;
    File mediaFile;
    private static String IMAGE_DIRECTORY_NAME = "SASS";

int modeefirst = -1;
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
    private LiftingPermitActivity parent;

    //public String CompanyURL ="http://192.168.1.221";
    ProgressBar mprogress;
    SharedPreferences sharedPrefs;
    Gson gson;
    String json;
    Type type;
    String authorize = "", authorize1 = "", authorize2 = "", authorize3 = "", authorize4 = "",
            PermitClosed = "", SpotCheck = "", StationId = "", PermitNo = "", StationName = "", contractorName = "", OperationName = "",categoryDesc="",userLoginId="";
    String contractorId = "", OperationId = "", LocationId = "", estimatedweight = "", gasdetect = "N", emergencystop = "N", loadingpt = "N",
            CrewCommunication = "", GroundCondition = "N", SafetyMeasures = "N", Adequatelight = "N", Demarcation = "N",obstacleRd="N",
            obstacleVal="N", weight="Kg";
    String PKFormId = "", formcode = "";
    JSONObject ActivityJson;
    int check = 0;
    private String serverResponseMessage, path, Imagefilename;

    GridView grid_safety;
    SafetyTools safetyTools;
    ArrayList<SafetyTools> safetyToolsArrayList;
    SafetyAdapter safetyAdapter;
    private ProgressDialog progressDialog;
    private AlertDialog b;
    TextView txt_authorize, txt_authorize1, txt_permit_closed, txt_spotcheck;
    RelativeLayout cusDialog1, cusDialog2;
    EditText edit_password_pass, edit_password_reason, edit_reason;
    LinearLayout ln_spinner_authorize, ln_spinner_reason;
    Button btn_cancel_pass, btn_submit_pass, btn_cancel_reason, btn_submit_reason;
    String tempVal, ReasonVal;
    String AuthorizedId = "", Authorized1Id = "", Permitclosed = "", Spotcheck = "",cancelId="";
    Permit permit;
    String Mode = "";
    String CompanyURL;
    boolean isAns;

    private ArrayList<PermitNoWA> WAArayList = new ArrayList<>();

    LinearLayout len_p_closed, len_p_spot,len_cancel_permit;
    TextView tx_p_closed,txt_cancel_permit,txt_cancel;
    private String PermitStatus;
    RecyclerView list_goldenRules;
    ArrayList<GoldenRules> goldenRulesArrayList;
    GoldenRuleAdapter goldenRuleAdapter;
    GoldenRules goldenRules;
    String goldenRulesList = "";
    Spinner spinner_weight;
    String WAStartTime = "", WAEndTime = "", WAEndTime1 = "";
    int WAEndTimeHr, WAEndTimeMin, WAStartTimeHr, WAStartTimemin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lifting_permit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        toolbar.setTitle(getResources().getString(R.string.application_name));
        setSupportActionBar(toolbar);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        // parent = LiftingPermitActivity.this;
        //hideKeyboard(LiftingPermitActivity.this);
        initview();
        setListener();
        dateListner();

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
        goldenRulesArrayList = new ArrayList<>();

        Intent intent = getIntent();

        if (getIntent() != null) {
            permit = new Gson().fromJson(getIntent().getStringExtra("permitno"), Permit.class);
            if (permit != null) {
                PermitStatus = permit.getWorkAuthorizationstatus();

                if (!PermitStatus.equalsIgnoreCase("P")) {
                    spinner_station.setEnabled(false);
                    spinner_prevention_plan.setEnabled(false);
                    btn_fromdate.setEnabled(false);
                    btn_fromtime.setEnabled(false);
                    btn_totime.setEnabled(false);
                    btn_fromdate.setTextColor(Color.parseColor("#000000"));
                    btn_fromtime.setTextColor(Color.parseColor("#000000"));
                    btn_totime.setTextColor(Color.parseColor("#000000"));
                    spinner_contractor.setEnabled(false);
                    spinner_operation.setEnabled(false);
                    spinner_location.setEnabled(false);
                    spinner_weight.setEnabled(false);
                    edit_desc_load.setKeyListener(null);
                    edit_dimensions.setKeyListener(null);
                    edit_load.setKeyListener(null);
                    radio_known_weight.setClickable(false);
                    radio_estimated_weight.setClickable(false);
                    edit_distance.setKeyListener(null);
                    edit_height.setKeyListener(null);
                    edit_lift_types.setKeyListener(null);
                    edit_safe_work.setKeyListener(null);
                    btn_last_certificate.setEnabled(false);
                    btn_last_certificate.setTextColor(Color.parseColor("#000000"));
                    edit_mac_maxlen.setKeyListener(null);
                    edit_rad_dist.setKeyListener(null);
                    edit_safework_load.setKeyListener(null);
                    edit_liftgear_types.setKeyListener(null);
                    edit_certificate_gears.setKeyListener(null);
                    rd_gasdetect_yes.setClickable(false);
                    rd_gasdetect_no.setClickable(false);
                    rd_emergency_yes.setClickable(false);
                    rd_emergency_no.setClickable(false);
                    rd_obstacle_no.setClickable(false);
                    rd_obstacle_yes.setClickable(false);
                    rd_loadyes.setClickable(false);
                    rd_loadno.setClickable(false);
                    rd_radio.setClickable(false);
                    rd_signal.setClickable(false);
                    edit_supervisor_name.setKeyListener(null);
                    edit_supervisor_certi.setKeyListener(null);
                    edit_craneoperator_name.setKeyListener(null);
                    edit_craneoperator_certi.setKeyListener(null);
                    edit_rigger_name.setKeyListener(null);
                    edit_rigger_certi.setKeyListener(null);
                    edit_signalman_name.setKeyListener(null);
                    edit_signalman_certi.setKeyListener(null);
                    rd_grncdtn_yes.setClickable(false);
                    rd_grncdtn_no.setClickable(false);
                    edit_Precaution1.setKeyListener(null);
                    edit_precaution2.setKeyListener(null);
                    rd_safety_yes.setClickable(false);
                    rd_safety_no.setClickable(false);
                    edit_Precaution3.setKeyListener(null);
                    rd_light_yes.setClickable(false);
                    rd_light_no.setClickable(false);
                    rd_dem_yes.setClickable(false);
                    rd_dem_no.setClickable(false);
                    edit_precaution4.setKeyListener(null);
                    edit_specify.setKeyListener(null);
                    txt_authorize.setKeyListener(null);
                    txt_authorize1.setKeyListener(null);
                    btn_liftingdate.setEnabled(false);
                    btn_liftingdate1.setEnabled(false);
                    btn_liftingdate2.setEnabled(false);
                    btn_liftingdate.setTextColor(Color.parseColor("#000000"));
                    btn_liftingdate1.setTextColor(Color.parseColor("#000000"));
                    btn_liftingdate2.setTextColor(Color.parseColor("#000000"));
                    edit_remarks.setKeyListener(null);

                    if(PermitStatus.equalsIgnoreCase("R") || PermitStatus.equalsIgnoreCase("C")){
                        txt_spotcheck.setEnabled(false);
                        btn_liftingdate4.setEnabled(false);
                        btn_liftingdate4.setTextColor(Color.parseColor("#000000"));
                        txt_permit_closed.setEnabled(false);
                        btn_liftingdate3.setEnabled(false);
                        btn_liftingdate3.setTextColor(Color.parseColor("#000000"));
                        tx_p_closed.setKeyListener(null);
                        txt_cancel_permit.setKeyListener(null);
                        btn_cancel_date.setKeyListener(null);
                        btn_cancel_date.setTextColor(Color.parseColor("#000000"));
                        btn_submit.setClickable(false);
                        txt_cancel.setEnabled(false);
                    }

                }else{btn_fromdate.setEnabled(true);}

              //  if (PermitStatus.equals("A") || PermitStatus.equals("P")) {
                    len_p_closed.setVisibility(View.VISIBLE);
                    tx_p_closed.setVisibility(View.VISIBLE);
                    txt_cancel_permit.setVisibility(View.VISIBLE);
                    len_cancel_permit.setVisibility(View.VISIBLE);

              /*  } else {
                    len_p_closed.setVisibility(View.GONE);
                    tx_p_closed.setVisibility(View.GONE);
                    txt_cancel_permit.setVisibility(View.GONE);
                    len_cancel_permit.setVisibility(View.GONE);
                }*/

                Mode = "E";
                PermitNo = permit.getPermitNo();
               // btn_fromdate.setEnabled(false);
                btn_fromdate.setTextColor(Color.parseColor("#101010"));

                if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                    new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new getdetails().execute(PermitNo);
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

                if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                    showProgress();
                    new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetPermitNo().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            CommonClass.displayToast(LiftingPermitActivity.this, msg);
                            dismissProgress();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        }
        else {

        }


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

        btn_fromdate.setText(date);
        // btn_todate.setText(date);
        btn_liftingdate.setText(date);
        btn_liftingdate1.setText(date);
        btn_liftingdate2.setText(date);
        btn_liftingdate3.setText(date);
        btn_liftingdate4.setText(date);
        btn_cancel_date.setText(date);

        long date1 = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = sdf.format(date1);
        btn_last_certificate.setText(dateString);

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int hour1;
        if (hour <= 13) {
            hour1 = hour + 9;
        } else {
            hour1 = hour + 8;
        }
        int minute = mcurrentTime.get(Calendar.MINUTE);
        String time1 = UpdateTime.updateTime(hour, minute);
        to_time = UpdateTime.updateTime(hour1, minute);
        System.out.println("time: " + time1);


        btn_fromtime.setText(time1);
        if (hour1 >= 24) {
            btn_totime.setText("11:59 PM");
        } else {
            btn_totime.setText(to_time);
        }


        // Safety Tools
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("safety", "");
        type = new TypeToken<List<SafetyTools>>() {
        }.getType();
        safetyToolsArrayList = gson.fromJson(json, type);

        if (safetyToolsArrayList == null) {
            if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                showProgress();
                new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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
                safetyAdapter = new SafetyAdapter(LiftingPermitActivity.this, safetyToolsArrayList, "LP", Mode, PermitStatus);
                grid_safety.setAdapter(safetyAdapter);


            }

        }
        // Golden Rules
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("GoldenRules", "");
        type = new TypeToken<List<GoldenRules>>() {
        }.getType();
        goldenRulesArrayList = gson.fromJson(json, type);

        if (goldenRulesArrayList == null) {
            if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                showProgress();
                new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadGoldenRules().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(LiftingPermitActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (goldenRulesArrayList.size() > 0) {

                goldenRuleAdapter = new GoldenRuleAdapter(LiftingPermitActivity.this, goldenRulesArrayList, Mode, PermitStatus);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LiftingPermitActivity.this);

                // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                list_goldenRules.setLayoutManager(linearLayoutManager);
                list_goldenRules.setAdapter(goldenRuleAdapter);


            }

        }


        // Depot Station
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Depot", "");
        type = new TypeToken<List<Depot>>() {
        }.getType();
        depotArrayList = gson.fromJson(json, type);

        if (depotArrayList == null) {
            if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                showProgress();
                new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadDepotData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(LiftingPermitActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (depotArrayList.size() > 0) {
                depotAdapter = new DepotAdapter(LiftingPermitActivity.this, depotArrayList);
                spinner_station.setAdapter(depotAdapter);


            }

        }


        // ContractorList

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Contractor", "");
        type = new TypeToken<List<ContractorList>>() {
        }.getType();
        contractorListActivityArrayList = gson.fromJson(json, type);

        if (contractorListActivityArrayList == null) {
            if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                showProgress();
                new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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
                permitContractorListAdapter = new PermitContractorListAdapter(LiftingPermitActivity.this, contractorListActivityArrayList);
                spinner_authorize2.setAdapter(permitContractorListAdapter);
                spinner_contractor.setAdapter(permitContractorListAdapter);
            }

        }

        // Operation

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Operation", "");
        type = new TypeToken<List<Operation>>() {
        }.getType();
        operationArrayList = gson.fromJson(json, type);

        if (operationArrayList == null) {
            if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                showProgress();
                new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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
                operationAdapter = new OperationAdapter(LiftingPermitActivity.this, operationArrayList);
                spinner_operation.setAdapter(operationAdapter);
            }

        }


        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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
                authorizedPersonAdapter = new AuthorizedPersonAdapter(LiftingPermitActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                // spinner_authorize1.setAdapter(authorizedPersonAdapter);
                // spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            }

        }


    }

    public void reasonDialog_SafetyTools(final int position, String SafetyToolMasterId) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LiftingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater().from(LiftingPermitActivity.this);
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
                        if(safetyToolsArrayList.get(position).getRemarks() != null){
                            if( safetyToolsArrayList.get(position).getRemarks().equalsIgnoreCase("")){
                                safetyToolsArrayList.get(position).setSelected(false);
                                safetyToolsArrayList.get(position).setRemarks("");
                                safetyAdapter.updateList(safetyToolsArrayList);
                            }else {
                                safetyToolsArrayList.get(position).setSelected(true);
                                safetyToolsArrayList.get(position).setRemarks(edit_remark.getText().toString());
                                safetyAdapter.updateList(safetyToolsArrayList);
                            }
                        }else{
                            safetyToolsArrayList.get(position).setSelected(false);
                            safetyToolsArrayList.get(position).setRemarks("");
                            safetyAdapter.updateList(safetyToolsArrayList);
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


    class getdetails extends AsyncTask<String, Void, String> {
        Object res;
        String response = "";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LiftingPermitActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setTitle("Data fetching...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_getHWDetails + "?form=" + PermitNo;


            try {
                res = CommonClass.OpenConnection(url, LiftingPermitActivity.this);
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
                    modeefirst = 1;
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
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LiftingPermitActivity.this);

                            // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            list_goldenRules.setLayoutManager(linearLayoutManager);
                            goldenRuleAdapter = new GoldenRuleAdapter();
                            goldenRuleAdapter.updateList(goldenRulesArrayList, Mode, PermitStatus);
                            list_goldenRules.setAdapter(goldenRuleAdapter);


                        } else {
                            if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                                new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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



                        // WAH_No = jorder.getString("FkWorkAuthorizationMasterId");


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


                        LocationId = jorder.getString("FkLocationMasterId");

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

                        StartDate = jorder.getString("PermitDate");
                        btn_fromdate.setText(StartDate);

                        fromtime = jorder.getString("PermitFromTime");
                        btn_fromtime.setText(fromtime);

                        totime = jorder.getString("PermitToTime");
                        btn_totime.setText(totime);


                        //FromTime1":"9:00 AM","ToTime1":"1:00 PM","FromTime2":"4:00 PM","ToTime2":"8:00 PM",

                    /*    WAStartTime = jorder.getString("FromTime1");
                        String[] start = WAStartTime.split(" ");
                        String[] startSplit = start[0].split(":");
                        WAStartTimeHr = Integer.parseInt(startSplit[0]);
                        WAStartTimemin = Integer.parseInt(startSplit[1]);
*/

                        WAStartTime = jorder.getString("FromTime1");
                        String start1 = DateFormatChange.formateDateFromstring("hh:mm aa", "HH:mm", WAStartTime);
                        String[] splitstrat = start1.split(":");
                        WAStartTimeHr = Integer.parseInt(splitstrat[0]);
                        WAStartTimemin = Integer.parseInt(splitstrat[1].split(" ")[0]);


/*                        WAEndTime = jorder.getString("ToTime2");
                        WAEndTime1 = jorder.getString("ToTime2");
                        String[] end = WAEndTime.split(" ");
                        String[] endSplit = end[0].split(":");
                        WAEndTimeHr = Integer.parseInt(endSplit[0]);
                        WAEndTimeMin = Integer.parseInt(endSplit[1]);*/

                        WAEndTime = jorder.getString("ToTime2");
                        WAEndTime1 = jorder.getString("ToTime2");
                        String waend = DateFormatChange.formateDateFromstring("hh:mm aa", "HH:mm", WAEndTime);
                        String[] splitend = waend.split(":");
                        WAEndTimeHr = Integer.parseInt(splitend[0]);
                        WAEndTimeMin = Integer.parseInt(splitend[1].split(" ")[0]);

                        descload = jorder.getString("Descload");
                        if (!descload.equalsIgnoreCase(""))
                            edit_desc_load.setText(descload);
                        else
                            edit_desc_load.setText("");

                        overalldimension = jorder.getString("Overalldimension");
                        if (!overalldimension.equalsIgnoreCase(""))
                            edit_dimensions.setText(overalldimension);
                        else
                            edit_dimensions.setText("");

                        WghtLoad = jorder.getString("WghtLoad");
                        String[] wghtSplit = WghtLoad.split(" ");
                        if(wghtSplit.length == 2) {
                            if (!wghtSplit[0].equalsIgnoreCase(""))
                                edit_load.setText(wghtSplit[0]);
                            else
                                edit_load.setText("");

                            if (wghtSplit[0].equalsIgnoreCase("kg")) {
                                spinner_weight.setSelection(0);
                            } else if (wghtSplit[1].equalsIgnoreCase("ton")) {
                                spinner_weight.setSelection(1);
                            } else {
                                spinner_weight.setSelection(0);
                            }
                        }else{
                            if (!wghtSplit[0].equalsIgnoreCase(""))
                                edit_load.setText(wghtSplit[0]);
                            else
                                edit_load.setText("");
                        }



                        estimatedweight = jorder.getString("Weight_KE");
                        if (estimatedweight.contains("K")) {
                            radio_estimated_weight.setChecked(false);
                            radio_known_weight.setChecked(true);
                        } else if (estimatedweight.contains("E")) {
                            radio_estimated_weight.setChecked(true);
                            radio_known_weight.setChecked(false);
                        } else {
                            radio_estimated_weight.setChecked(false);
                            radio_known_weight.setChecked(false);
                        }

                        ShiftedLoad = jorder.getString("ShiftedLoad");
                        if (!ShiftedLoad.equalsIgnoreCase(""))
                            edit_distance.setText(ShiftedLoad);
                        else
                            edit_distance.setText("");

                        ShiftedHeight = jorder.getString("ShiftedHeight");
                        if (!ShiftedHeight.equalsIgnoreCase(""))
                            edit_height.setText(ShiftedHeight);
                        else
                            edit_height.setText("");

                        lifttypes = jorder.getString("Lifttypes");
                        if (!lifttypes.equalsIgnoreCase(""))
                            edit_lift_types.setText(lifttypes);
                        else
                            edit_lift_types.setText("");

                        safewrk = jorder.getString("Safewrk");
                        if (!safewrk.equalsIgnoreCase(""))
                            edit_safe_work.setText(safewrk);
                        else
                            edit_safe_work.setText("");

                        lstcertificateDate = jorder.getString("lstcertificateDate");
                        if (!lstcertificateDate.equalsIgnoreCase(""))
                            btn_last_certificate.setText(lstcertificateDate);
                        else
                            btn_last_certificate.setText(date);

                        maxlen = jorder.getString("Maxlen");
                        if (!maxlen.equalsIgnoreCase(""))
                            edit_mac_maxlen.setText(maxlen);
                        else
                            edit_mac_maxlen.setText("");

                        raddistance = jorder.getString("Raddistance");
                        if (!raddistance.equalsIgnoreCase(""))
                            edit_rad_dist.setText(raddistance);
                        else
                            edit_rad_dist.setText("");

                        safeworkload = jorder.getString("Safeworkload");
                        if (!safeworkload.equalsIgnoreCase(""))
                            edit_safework_load.setText(safeworkload);
                        else
                            edit_safework_load.setText("");


                        liftgeartypes = jorder.getString("Liftgeartypes");
                        if (!liftgeartypes.equalsIgnoreCase(""))
                            edit_liftgear_types.setText(liftgeartypes);
                        else
                            edit_liftgear_types.setText("");

                        gearcertificate = jorder.getString("Gearcertificate");
                        if (!gearcertificate.equalsIgnoreCase(""))
                            edit_certificate_gears.setText(gearcertificate);
                        else
                            edit_certificate_gears.setText("");

                        gasdetect = jorder.getString("Gasdetect");
                        if (gasdetect.contains("Y")) {
                            rd_gasdetect_yes.setChecked(true);
                            rd_gasdetect_no.setChecked(false);
                        } else if (gasdetect.contains("N")) {
                            rd_gasdetect_no.setChecked(true);
                            rd_gasdetect_yes.setChecked(false);
                        } else {
                            rd_gasdetect_yes.setChecked(false);
                            rd_gasdetect_no.setChecked(false);
                        }

                        emergencystop = jorder.getString("Emergencystop");
                        if (emergencystop.contains("Y")) {
                            rd_emergency_yes.setChecked(true);
                            rd_emergency_no.setChecked(false);
                        } else if (emergencystop.contains("N")) {
                            rd_emergency_no.setChecked(true);
                            rd_emergency_yes.setChecked(false);
                        } else {
                            rd_emergency_no.setChecked(false);
                            rd_emergency_yes.setChecked(false);
                        }

                        obstacleVal = jorder.getString("Obstacles");
                        if (emergencystop.contains("Y")) {
                            rd_obstacle_yes.setChecked(true);
                            rd_obstacle_no.setChecked(false);
                        } else if (emergencystop.contains("N")) {
                            rd_obstacle_no.setChecked(true);
                            rd_obstacle_yes.setChecked(false);
                        } else {
                            rd_obstacle_no.setChecked(false);
                            rd_obstacle_yes.setChecked(false);
                        }


                        loadingpt = jorder.getString("Loadingpt");
                        if (loadingpt.contains("Y")) {
                            rd_loadyes.setChecked(true);
                            rd_loadno.setChecked(false);
                        } else if (loadingpt.contains("N")) {
                            rd_loadyes.setChecked(false);
                            rd_loadno.setChecked(true);
                        } else {
                            rd_loadyes.setChecked(false);
                            rd_loadno.setChecked(false);
                        }

                        CrewCommunication = jorder.getString("CrewCommunication");
                        if (CrewCommunication.contains("R")) {
                            rd_radio.setChecked(true);
                            rd_signal.setChecked(false);
                        } else if (CrewCommunication.contains("S")) {
                            rd_radio.setChecked(false);
                            rd_signal.setChecked(true);
                        } else {
                            rd_radio.setChecked(false);
                            rd_signal.setChecked(false);
                        }

                        GroundCondition = jorder.getString("GroundCondition");
                        if (GroundCondition.contains("Y")) {
                            rd_grncdtn_yes.setChecked(true);
                            rd_grncdtn_no.setChecked(false);
                        } else if (GroundCondition.contains("N")) {
                            rd_grncdtn_yes.setChecked(false);
                            rd_grncdtn_no.setChecked(true);
                        } else {
                            rd_grncdtn_yes.setChecked(false);
                            rd_grncdtn_no.setChecked(false);
                        }

                        GroundPrecaution1 = jorder.getString("GroundPrecaution");
                        if (!GroundPrecaution1.equalsIgnoreCase(""))
                            edit_Precaution1.setText(GroundPrecaution1);
                        else
                            edit_Precaution1.setText("");

                        Precaution2 = jorder.getString("ObstaclesPrecaution");
                        if (!Precaution2.equalsIgnoreCase(""))
                            edit_precaution2.setText(Precaution2);
                        else
                            edit_precaution2.setText("");

                        SafetyMeasures = jorder.getString("SafetyMeasures");
                        if (SafetyMeasures.contains("Y")) {
                            rd_safety_yes.setChecked(true);
                            rd_safety_no.setChecked(false);
                        } else if (SafetyMeasures.contains("N")) {
                            rd_safety_yes.setChecked(false);
                            rd_safety_no.setChecked(true);
                        } else {
                            rd_safety_yes.setChecked(false);
                            rd_safety_no.setChecked(false);
                        }

                        Precaution3 = jorder.getString("SafetyPrecaution");
                        if (!Precaution3.equalsIgnoreCase(""))
                            edit_Precaution3.setText(Precaution3);
                        else
                            edit_Precaution3.setText("");

                        Adequatelight = jorder.getString("Adequatelight");
                        if (Adequatelight.contains("Y")) {
                            rd_light_yes.setChecked(true);
                            rd_light_no.setChecked(false);
                        } else if (Adequatelight.contains("N")) {
                            rd_light_yes.setChecked(false);
                            rd_light_no.setChecked(true);
                        } else {
                            rd_light_yes.setChecked(false);
                            rd_light_no.setChecked(false);
                        }

                        Demarcation = jorder.getString("Demarcation");
                        if (Demarcation.contains("Y")) {
                            rd_dem_yes.setChecked(true);
                            rd_dem_no.setChecked(false);
                        } else if (Demarcation.contains("N")) {
                            rd_dem_yes.setChecked(false);
                            rd_dem_no.setChecked(true);
                        } else {
                            rd_dem_yes.setChecked(false);
                            rd_dem_no.setChecked(false);
                        }

                        Precaution4 = jorder.getString("DemarcationPrecaution");
                        if (!Precaution4.equalsIgnoreCase(""))
                            edit_precaution4.setText(Precaution4);
                        else
                            edit_precaution4.setText("");


                        specifyprecaution = jorder.getString("specifyprecaution");
                        if (!specifyprecaution.equalsIgnoreCase(""))
                            edit_specify.setText(specifyprecaution);
                        else
                            edit_specify.setText("");


                        safetytools = jorder.getString("safetytools");/* "chetana,sayali,suyog,vritti,pradnya"*/
                        ;
                        String[] safetytoolslist = new String[safetytools.length()];
                        safetytoolslist = safetytools.split(",");
                        for (int j = 0; j < safetyToolsArrayList.size(); j++) {
                            for (int k = 0; k < safetytoolslist.length; k++) {
                                if (safetytoolslist[k].equals(safetyToolsArrayList.get(j).getSafetyToolMasterId())) {
                                    int pos = j;
                                    safetyToolsArrayList.get(pos).setSelected(true);
                                    safetyAdapter = new SafetyAdapter(LiftingPermitActivity.this, safetyToolsArrayList, "LP", Mode, PermitStatus);
                                    grid_safety.setAdapter(safetyAdapter);
                                }

                            }
                        }

                        AuthorizedId = jorder.getString("AuthorizeBy1");
                        int authorizepos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(AuthorizedId)) {
                                authorizepos = j;
                            }
                        }
                        if (authorizepos != -1)
                            txt_authorize.setText(authorizedPersonArrayList.get(authorizepos).getAuthorizename());
                        else
                            txt_authorize.setText("Select");

                        Authorized1Id = jorder.getString("AuthorizeBy2");
                        int authorize1pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(Authorized1Id)) {
                                authorize1pos = j;
                            }
                        }
                        if (authorize1pos != -1)
                            txt_authorize1.setText(authorizedPersonArrayList.get(authorize1pos).getAuthorizename());
                        else
                            txt_authorize1.setText("Select");


                        PermitclosedDate = jorder.getString("PermitCloseDAte");
                        btn_liftingdate3.setText(PermitclosedDate);

                        spotcheckdate = jorder.getString("SpotCheckDate");
                        btn_liftingdate4.setText(spotcheckdate);


                        Permitclosed = jorder.getString("PermitCloseBy");
                        int permitpos = -1;
                        if (Permitclosed.equalsIgnoreCase("")) {

                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (Permitclosed.equalsIgnoreCase(authorizedPersonArrayList.get(j).getAuthorizeid()))
                                    permitpos = j;
                            }
                        }


                        if (permitpos != -1)
                            txt_permit_closed.setText(authorizedPersonArrayList.get(permitpos).getAuthorizename());
                        else
                            txt_permit_closed.setText("Select");


                        Spotcheck = jorder.getString("SpotCheckBy");
                        int spotchckpos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (Spotcheck.equalsIgnoreCase(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                spotchckpos = j;
                            }
                        }
                        if (spotchckpos != -1)
                            txt_spotcheck.setText(authorizedPersonArrayList.get(spotchckpos).getAuthorizename());
                        else
                            txt_spotcheck.setText("Select");

                       /* if(SpotCheck.equals("") && Permitclosed.equals("")){
                            btn_submit.setVisibility(View.GONE);
                        }else{
                            btn_submit.setVisibility(View.VISIBLE);
                        }*/


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
                        if(cancelDate.equals("")) {
                            btn_cancel_date.setText(date);
                        }else{
                            btn_cancel_date.setText(cancelDate);
                        }
                        Remarks = jorder.getString("PermitCloseRemark");
                        edit_remarks.setText(Remarks);

                        Supervisor_Name = jorder.getString("LiftingSupervisorName");
                        if (!Supervisor_Name.equalsIgnoreCase(""))
                            edit_supervisor_name.setText(Supervisor_Name);
                        else
                            edit_supervisor_name.setText("");

                        Supervisor_certi = jorder.getString("LiftingSupervisorQualification");
                        if (!Supervisor_certi.equalsIgnoreCase(""))
                            edit_supervisor_certi.setText(Supervisor_certi);
                        else
                            edit_supervisor_certi.setText("");

                        CraneOperator_Name = jorder.getString("CraneOperatorName");
                        if (!CraneOperator_Name.equalsIgnoreCase(""))
                            edit_craneoperator_name.setText(CraneOperator_Name);
                        else
                            edit_craneoperator_name.setText("");

                        CraneOperator_Certi = jorder.getString("CraneOperatorQualification");
                        if (!CraneOperator_Certi.equalsIgnoreCase(""))
                            edit_craneoperator_certi.setText(CraneOperator_Certi);
                        else
                            edit_craneoperator_certi.setText("");


                        RiggerName = jorder.getString("RiggerName");
                        if (!RiggerName.equalsIgnoreCase(""))
                            edit_rigger_name.setText(RiggerName);
                        else
                            edit_rigger_name.setText("");

                        RiggerCerti = jorder.getString("RiggerQualification");
                        if (!RiggerCerti.equalsIgnoreCase(""))
                            edit_rigger_certi.setText(RiggerCerti);
                        else
                            edit_rigger_certi.setText("");

                        Signalman_Name = jorder.getString("SignalmanName");
                        if (!Signalman_Name.equalsIgnoreCase(""))
                            edit_signalman_name.setText(Signalman_Name);
                        else
                            edit_signalman_name.setText("");

                        Signalman_certi = jorder.getString("SignalmanQualification");
                        if (!Signalman_certi.equalsIgnoreCase(""))
                            edit_signalman_certi.setText(Signalman_certi);
                        else
                            edit_signalman_certi.setText("");


                        int WAHNo = -1;

                        WAH_No = jorder.getString("FkWorkAuthorizationMasterId");
                        for (int z = 0; z < WAArayList.size(); z++) {
                            if (WAH_No.equals(WAArayList.get(z).getPermitNo())) {
                                WAHNo = z;
                                break;
                            }
                        }

                        if (WAHNo != -1) {
                            spinner_prevention_plan.setSelection(WAHNo);
                        } else {
                            spinner_prevention_plan.setSelection(0);
                        }


                        safety_Others = jorder.getString("LP_ProOthers");

                        if (safetyToolsArrayList != null) {
                            for (int j = 0; j < safetyToolsArrayList.size(); j++) {
                                if (safetyToolsArrayList.get(j).getSafetyToolDesc().equals("Other(s)")) {
                                    safetyToolsArrayList.get(j).setRemarks(safety_Others);
                                    safetyToolsArrayList.get(j).isSelected();
                                }
                            }
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initview() {
        btn_fromdate = findViewById(R.id.btn_fromdate);
        //btn_todate = findViewById(R.id.btn_todate);
        btn_liftingdate = findViewById(R.id.btn_liftingdate);
        btn_liftingdate1 = findViewById(R.id.btn_liftingdate1);
        btn_liftingdate2 = findViewById(R.id.btn_liftingdate2);
        btn_liftingdate3 = findViewById(R.id.edt_permit_date);
        btn_liftingdate4 = findViewById(R.id.edt_spot_date);
        btn_cancel_date = findViewById(R.id.edt_cancel_date);
        btn_submit = findViewById(R.id.btn_submit);
        btn_fromtime = findViewById(R.id.btn_fromtime);
        btn_totime = findViewById(R.id.btn_totime);
        btn_totime.setEnabled(false);
        btn_totime.setTextColor(Color.parseColor("#000000"));
        btn_last_certificate = (Button) findViewById(R.id.btn_last_certificate);
        ln_station = findViewById(R.id.ln_station);
        ln_WAHNo = findViewById(R.id.ln_WAHNo);
        ln_contractor = findViewById(R.id.ln_contractor);
        ln_DescOperation = findViewById(R.id.ln_DescOperation);
        ln_location = findViewById(R.id.ln_location);
        spinner_weight = findViewById(R.id.spinner_weight);


        radio_estimated_weight = findViewById(R.id.radio_estimated_weight);
        radiogroup_obstacle = findViewById(R.id.radiogroup_obstacle);
        radio_known_weight = findViewById(R.id.radio_known_weight);
        rd_gasdetect_no = findViewById(R.id.rd_gasdetect_no);
        rd_gasdetect_yes = findViewById(R.id.rd_gasdetect_yes);
        rd_emergency_no = findViewById(R.id.rd_emergency_no);
        rd_emergency_yes = findViewById(R.id.rd_emergency_yes);
        rd_loadyes = findViewById(R.id.rd_loadyes);
        rd_loadno = findViewById(R.id.rd_loadno);
        rd_signal = findViewById(R.id.rd_signal);
        rd_radio = findViewById(R.id.rd_radio);
        rd_obstacle_no = findViewById(R.id.rd_obstacle_no);
        rd_obstacle_yes = findViewById(R.id.rd_obstacle_yes);
        rd_grncdtn_no = findViewById(R.id.rd_grncdtn_no);
        rd_grncdtn_yes = findViewById(R.id.rd_grncdtn_yes);
        rd_safety_no = findViewById(R.id.rd_safety_no);
        rd_safety_yes = findViewById(R.id.rd_safety_yes);
        rd_light_no = findViewById(R.id.rd_light_no);
        rd_light_yes = findViewById(R.id.rd_light_yes);
        rd_dem_yes = findViewById(R.id.rd_dem_yes);
        rd_dem_no = findViewById(R.id.rd_dem_no);
        list_goldenRules = findViewById(R.id.list_goldenRules);

//        edit_desc_work = findViewById(R.id.edit_desc_work);
        // edit_loc_operation = findViewById(R.id.edit_loc_operation);
        edit_desc_load = findViewById(R.id.edit_desc_load);
        edit_dimensions = findViewById(R.id.edit_dimensions);
        edit_load = findViewById(R.id.edit_load);
        spinner_prevention_plan = findViewById(R.id.spinner_prevention_plan);
        edit_distance = findViewById(R.id.edit_distance);
        edit_height = findViewById(R.id.edit_height);
        edit_lift_types = findViewById(R.id.edit_lift_types);
        edit_safe_work = findViewById(R.id.edit_safe_work);
        edit_mac_maxlen = (EditText) findViewById(R.id.edit_mac_maxlen);
        edit_rad_dist = (EditText) findViewById(R.id.edit_rad_dist);
        edit_safework_load = (EditText) findViewById(R.id.edit_safework_load);
        edit_liftgear_types = (EditText) findViewById(R.id.edit_liftgear_types);
        edit_certificate_gears = (EditText) findViewById(R.id.edit_certificate_gears);
        edit_Precaution1 = (EditText) findViewById(R.id.edit_Precaution1);
        edit_precaution2 = (EditText) findViewById(R.id.edit_precaution2);
        edit_Precaution3 = (EditText) findViewById(R.id.edit_precaution3);
        edit_precaution4 = (EditText) findViewById(R.id.edit_precaution4);
        edit_specify = (EditText) findViewById(R.id.edit_specify);
        edit_supervisor_name = (EditText) findViewById(R.id.edit_supervisor_name);
        edit_supervisor_certi = (EditText) findViewById(R.id.edit_supervisor_certi);
        edit_craneoperator_name = (EditText) findViewById(R.id.edit_craneoperator_name);
        edit_craneoperator_certi = (EditText) findViewById(R.id.edit_craneoperator_certi);
        edit_rigger_name = (EditText) findViewById(R.id.edit_rigger_name);
        edit_rigger_certi = (EditText) findViewById(R.id.edit_rigger_certi);
        edit_signalman_name = (EditText) findViewById(R.id.edit_signalman_name);
        edit_signalman_certi = (EditText) findViewById(R.id.edit_signalman_certi);
        edit_remarks = (EditText) findViewById(R.id.edit_remarks);

        cusDialog1 = findViewById(R.id.cusDialog1);
        spinner_authorize = findViewById(R.id.spinner_authorize_pas);
        edit_password_pass = findViewById(R.id.edt_password_pass);
        ln_spinner_authorize = findViewById(R.id.ln_spinner_authorize);
        ln_spinner_reason = findViewById(R.id.ln_spinner_reason);
        btn_cancel_pass = findViewById(R.id.txt_cancel_pass);
        btn_submit_pass = findViewById(R.id.txt_submit_pass);

        cusDialog2 = findViewById(R.id.cusDialog2);
        spinner_permit_closed = findViewById(R.id.spinner_permit_closed1);
        edit_password_reason = findViewById(R.id.edt_password_reason);
        edit_reason = findViewById(R.id.edt_reason);
        btn_cancel_reason = findViewById(R.id.txt_cancel_reason);
        btn_submit_reason = findViewById(R.id.txt_submit_reason);


        txt_authorize = (TextView) findViewById(R.id.spinner_authorize);
        txt_authorize1 = (TextView) findViewById(R.id.spinner_authorize1);
        txt_permit_closed = (TextView) findViewById(R.id.txt_permit_closed);
        txt_spotcheck = (TextView) findViewById(R.id.txt_spotcheck);
        //spinner_authorize = findViewById(R.id.spinner_authorize);
        //spinner_authorize1 = findViewById(R.id.spinner_authorize1);
        spinner_authorize2 = findViewById(R.id.spinner_authorize2);
        spinner_authorize2.setEnabled(false);
        //spinner_permit_closed = findViewById(R.id.spinner_permit_closed);
        //spinner_spotcheck = findViewById(R.id.spinner_spotcheck);
        edit_permitno = findViewById(R.id.edit_permitno);
        spinner_contractor = findViewById(R.id.spinner_contractor);
        spinner_station = findViewById(R.id.spinner_station);
        img_camera = findViewById(R.id.img_camera);
        radiogroup_estimated_wght = (RadioGroup) findViewById(R.id.radiogroup_estwght);
        radiogroup_gasdetect = (RadioGroup) findViewById(R.id.radiogroup_gasdetect);
        radiogroup_emergency_stop = (RadioGroup) findViewById(R.id.radiogroup_emergency_stop);
        radiogroup_loadingpt = (RadioGroup) findViewById(R.id.radiogroup_loadingpt);
        radiogroup_comm_crew = (RadioGroup) findViewById(R.id.radiogroup_comm_crew);
        radiogroup_grndcondtn = (RadioGroup) findViewById(R.id.radiogroup_grndcondtn);
        radiogroup_safety = (RadioGroup) findViewById(R.id.radiogroup_safety);
        radiogroup_light_adequate = (RadioGroup) findViewById(R.id.radiogroup_light_adequate);
        radiogroup_demarcation = (RadioGroup) findViewById(R.id.radiogroup_demarcation);

        mprogress = findViewById(R.id.toolbar_progress_App_bar);

        spinner_operation = findViewById(R.id.spinner_operation);
        spinner_location = findViewById(R.id.spinner_location);

        grid_safety = findViewById(R.id.grid_safety);
        len_p_closed = findViewById(R.id.len_p_closed);
        tx_p_closed = findViewById(R.id.tx_p_closed);
        txt_cancel_permit = findViewById(R.id.txt_cancel_permit);
        txt_cancel = findViewById(R.id.txt_cancel);
        len_cancel_permit = findViewById(R.id.len_cancel_permit);
        list_goldenRules =  findViewById(R.id.list_goldenRules);

        spinner_location.setEnabled(false);
        spinner_operation.setEnabled(false);

        String[] weightLoad = getResources().getStringArray(R.array.weight);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.crm_custom_spinner_txt, weightLoad);

    }

    public void setListener() {

        spinner_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weight= spinner_weight.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*spinner_weight.s(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  weight = spinner_weight.getSelectedItem().toString();



            }
        });*/

        spinner_station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StationId = depotArrayList.get(position).getDepotid();

                if (StationId.equals("--Select--") || StationId.equals("Select")) {

                } else {
                    if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                        showProgress();
                        //Location Get
                        new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*int authorize1pos = spinner_authorize.getSelectedItemPosition();
                int authorize2pos = spinner_authorize1.getSelectedItemPosition();
                int authorize3pos = spinner_authorize2.getSelectedItemPosition();
                int permitcheckpos = spinner_permit_closed.getSelectedItemPosition();
                int spotcheckpos = spinner_spotcheck.getSelectedItemPosition();*/

                if (StationId.equalsIgnoreCase("Select") || StationId.equalsIgnoreCase("")) {
                    Toast.makeText(LiftingPermitActivity.this, "Please Fill Station Details", Toast.LENGTH_SHORT).show();
                    ln_station.setBackgroundResource(R.drawable.edit_text_red);
                } else if (WAH_No.equalsIgnoreCase("Select") || WAH_No.equalsIgnoreCase("")) {
                    Toast.makeText(LiftingPermitActivity.this, "Please Fill WAH No Details", Toast.LENGTH_SHORT).show();
                    ln_WAHNo.setBackgroundResource(R.drawable.edit_text_red);
                } else if (contractorId.equalsIgnoreCase("Select") || contractorId.equalsIgnoreCase("")) {
                    Toast.makeText(LiftingPermitActivity.this, "Please Fill Contractor Details", Toast.LENGTH_SHORT).show();
                    ln_contractor.setBackgroundResource(R.drawable.edit_text_red);
                } else if (OperationId.equalsIgnoreCase("Select") || OperationId.equalsIgnoreCase("")) {
                    Toast.makeText(LiftingPermitActivity.this, "Please Fill Operation  Details", Toast.LENGTH_SHORT).show();
                    ln_DescOperation.setBackgroundResource(R.drawable.edit_text_red);
                } else if (LocationId.equalsIgnoreCase("select") || LocationId.equalsIgnoreCase("")) {
                    Toast.makeText(LiftingPermitActivity.this, "Please Fill Location Details", Toast.LENGTH_SHORT).show();
                    ln_location.setBackgroundResource(R.drawable.edit_text_red);
                } else {
                    if (Mode.equalsIgnoreCase("A")) {
                        saveactivityjson();
                       // startActivity(new Intent(LiftingPermitActivity.this, LiftingPermitActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else if (Mode.equalsIgnoreCase("E")) {
                        editjson();
                        //startActivity(new Intent(LiftingPermitActivity.this, LiftingPermitActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else {

                    }
                }




            /*    if(StationId.equalsIgnoreCase("Select") || WAH_No.equalsIgnoreCase("")
                        || contractorId.equalsIgnoreCase("") || OperationId.equalsIgnoreCase("") ||
                        LocationId.equalsIgnoreCase("")){
                    Toast.makeText(LiftingPermitActivity.this,"Please fill defined operation details",Toast.LENGTH_SHORT).show();
            }else{


                 }*/

            }
        });


        txt_authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // authorizedialog();
                //tier 1
                CategoryWiseAuthorizeName("level 1","fromPWd");
                cusDialog1.setVisibility(View.VISIBLE);
                tempVal = "0";
            }
        });
        txt_authorize1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authorize1dialog();
                //Tier 1 and Tier 2
                CategoryWiseAuthorizeName("level 2","fromPWD");
                cusDialog1.setVisibility(View.VISIBLE);
                tempVal = "1";
            }
        });
        txt_permit_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Reasondialog();
                CategoryWiseAuthorizeName("level 1","fromReason");
                cusDialog2.setVisibility(View.VISIBLE);
                ReasonVal = "1";
            }
        });
        txt_spotcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Spotcheckdialog();
                CategoryWiseAuthorizeName("level 1","fromReason");
                cusDialog2.setVisibility(View.VISIBLE);
                ReasonVal = "2";
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Spotcheckdialog();
                CategoryWiseAuthorizeName("level 1","fromReason");
                cusDialog2.setVisibility(View.VISIBLE);
                ReasonVal = "3";
            }
        });


        spinner_prevention_plan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (WAArayList.size() > 0) {

                    WAH_No = WAArayList.get(position).getPermitNo();

                    if (WAH_No != "") {
                        if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                            showProgress();
                            //Location Get
                            new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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
                            Toast.makeText(LiftingPermitActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
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
                        }
                    }

                    if (contractorpos != -1) {
                        // spinner_contractor.setSelection(contractorpos);
                        spinner_authorize2.setSelection(contractorpos);
                    } else {
                        spinner_authorize2.setSelection(0);
                    }


                    if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                        showProgress();
                        //Location Get
                        new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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
                        if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                            new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadWANo().execute(contractorId);
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    Toast.makeText(LiftingPermitActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(LiftingPermitActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        /*spinner_contractor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(permit != null){
                    contractorId = contractorListActivityArrayList.get(position).getCustVendorMasterId();
                    WAH_No = contractorListActivityArrayList.get(position).getCustVendorCode();
                    edit_Prevention_plan.setText(WAH_No);
                }else {
                    contractorId = contractorListActivityArrayList.get(position).getCustVendorMasterId();
                    WAH_No = contractorListActivityArrayList.get(position).getCustVendorCode();
                    edit_Prevention_plan.setText(WAH_No);
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

        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                LocationId = LocationArraylist.get(position).getLocationMasterId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radiogroup_estimated_wght.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Toast.makeText(getBaseContext(), value, Toast.LENGTH_SHORT).show();
                int selectedId = radiogroup_estimated_wght.getCheckedRadioButtonId();
                RadioButton radiobtn = (RadioButton) findViewById(selectedId);

                String radvalue = radiobtn.getText().toString();
                if (radvalue.equalsIgnoreCase("Known weight")) {
                    estimatedweight = "K";
                } else if (radvalue.equalsIgnoreCase("Estimated weight")) {
                    estimatedweight = "E";
                } else {
                    estimatedweight = "";
                }


            }
        });

        radiogroup_gasdetect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Toast.makeText(getBaseContext(), value, Toast.LENGTH_SHORT).show();
                int selectedId = radiogroup_gasdetect.getCheckedRadioButtonId();
                RadioButton radiobtn = (RadioButton) findViewById(selectedId);

                String radvalue = radiobtn.getText().toString();
                if (radvalue.equalsIgnoreCase("Yes")) {
                    gasdetect = "Y";
                } else {
                    gasdetect = "N";
                }

            }
        });

        radiogroup_emergency_stop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Toast.makeText(getBaseContext(), value, Toast.LENGTH_SHORT).show();
                int selectedId = radiogroup_emergency_stop.getCheckedRadioButtonId();
                RadioButton radiobtn = (RadioButton) findViewById(selectedId);

                String radvalue = radiobtn.getText().toString();
                if (radvalue.equalsIgnoreCase("Yes")) {
                    emergencystop = "Y";
                } else {
                    emergencystop = "N";
                }

            }
        });

        radiogroup_loadingpt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radiogroup_loadingpt.getCheckedRadioButtonId();
                RadioButton radiobtn = (RadioButton) findViewById(selectedId);

                String radvalue = radiobtn.getText().toString();

                if (radvalue.equalsIgnoreCase("Yes")) {
                    loadingpt = "Y";
                } else {
                    loadingpt = "N";
                }
            }
        });

        radiogroup_comm_crew.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radiogroup_comm_crew.getCheckedRadioButtonId();
                RadioButton radiobtn = (RadioButton) findViewById(selectedId);

                String radvalue = radiobtn.getText().toString();

                if (radvalue.equalsIgnoreCase("Standard hand signal")) {
                    CrewCommunication = "S";
                } else if (radvalue.equalsIgnoreCase("Radio")) {
                    CrewCommunication = "R";
                } else {
                    CrewCommunication = "";
                }
            }
        });


        radiogroup_grndcondtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radiogroup_grndcondtn.getCheckedRadioButtonId();
                RadioButton radiobtn = (RadioButton) findViewById(selectedId);

                String radvalue = radiobtn.getText().toString();

                if (radvalue.equalsIgnoreCase("Yes")) {
                    GroundCondition = "Y";
                } else {
                    GroundCondition = "N";
                }
            }
        });

        radiogroup_obstacle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radiogroup_obstacle.getCheckedRadioButtonId();
                RadioButton radiobtn = (RadioButton) findViewById(selectedId);

                String radvalue = radiobtn.getText().toString();

                if (radvalue.equalsIgnoreCase("Yes")) {
                    obstacleRd = "Y";
                } else {
                    obstacleRd = "N";
                }
            }
        });

        radiogroup_safety.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radiogroup_safety.getCheckedRadioButtonId();
                RadioButton radiobtn = (RadioButton) findViewById(selectedId);

                String radvalue = radiobtn.getText().toString();

                if (radvalue.equalsIgnoreCase("Yes")) {
                    SafetyMeasures = "Y";
                } else {
                    SafetyMeasures = "N";
                }

            }
        });
        radiogroup_light_adequate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radiogroup_light_adequate.getCheckedRadioButtonId();
                RadioButton radiobtn = (RadioButton) findViewById(selectedId);

                String radvalue = radiobtn.getText().toString();

                if (radvalue.equalsIgnoreCase("Yes")) {
                    Adequatelight = "Y";
                } else {
                    Adequatelight = "N";
                }
            }
        });
        radiogroup_obstacle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radiogroup_obstacle.getCheckedRadioButtonId();
                RadioButton radiobtn = (RadioButton) findViewById(selectedId);

                String radvalue = radiobtn.getText().toString();

                if (radvalue.equalsIgnoreCase("Yes")) {
                    obstacleVal = "Y";
                } else {
                    obstacleVal = "N";
                }
            }
        });

        radiogroup_demarcation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radiogroup_demarcation.getCheckedRadioButtonId();
                RadioButton radiobtn = (RadioButton) findViewById(selectedId);

                String radvalue = radiobtn.getText().toString();

                if (radvalue.equalsIgnoreCase("Yes")) {
                    Demarcation = "Y";
                } else {
                    Demarcation = "N";
                }
            }
        });


        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(LiftingPermitActivity.this, Manifest.permission.CAMERA)
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


        btn_cancel_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text);
                edit_password_pass.setBackgroundResource(R.drawable.edit_text);

                if (cusDialog1.getVisibility() == View.VISIBLE) {
                    cusDialog1.setVisibility(View.GONE);
                    if (tempVal.equals("0")) {
                        spinner_authorize.setSelection(0);
                        txt_authorize.setText("Select");
                        edit_password_pass.setText("");
                    } else if (tempVal.equals("1")) {
                        spinner_authorize.setSelection(0);
                        txt_authorize1.setText("Select");
                        edit_password_pass.setText("");

                    }
                } else {
                    cusDialog1.setVisibility(View.VISIBLE);
                }

                //b.dismiss();
            }
        });
        btn_submit_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progressDialog.show();
                Password = edit_password_pass.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text);
                edit_password_pass.setBackgroundResource(R.drawable.edit_text);
                if (Password.equalsIgnoreCase("") && (authorize.equalsIgnoreCase("--Select--")
                        || authorize.equalsIgnoreCase(""))) {

                    Toast toast = Toast.makeText(LiftingPermitActivity.this, "Please enter authorized person and password", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(LiftingPermitActivity.this, "Please select authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && (!authorize.equalsIgnoreCase("--Select--")
                        || !authorize.equalsIgnoreCase(""))) {
                    Toast toast = Toast.makeText(LiftingPermitActivity.this, "Please enter valid password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_pass.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else {

                    if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                        new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
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
                        userLoginId = txt_authorizeArrayList.get(position).getUserLoginId();
                        String name = txt_authorizeArrayList.get(position).getAuthorizename();
                        if (tempVal.equals("0")) {
                            txt_authorize.setText(name);
                            AuthorizedId = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("1")) {
                            txt_authorize1.setText(name);
                            Authorized1Id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        }
                    }
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_cancel_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cusDialog2.getVisibility() == View.VISIBLE) {
                    cusDialog2.setVisibility(View.GONE);
                    if (ReasonVal.equals("1")) {
                        spinner_permit_closed.setSelection(0);
                        edit_password_reason.setText("");
                        edit_reason.setText("");
                        txt_permit_closed.setText("Select");
                        Permitclosed = "";

                    } else if (ReasonVal.equals("2")) {
                        spinner_permit_closed.setSelection(0);
                        edit_password_reason.setText("");
                        edit_reason.setText("");
                        txt_spotcheck.setText("Select");
                        Spotcheck = "";
                    } else if(ReasonVal.equals("3")){
                        txt_cancel.setText("Select");
                        spinner_permit_closed.setSelection(0);
                        edit_password_reason.setText("");
                        edit_reason.setText("");
                        cancelId = "";
                    }

                    if ((Permitclosed.equalsIgnoreCase("Select") || Permitclosed.equals(""))
                            && (Spotcheck.equalsIgnoreCase("Select") || Spotcheck.equals(""))) {
                        // btn_submit.setVisibility(View.GONE);
                    } else {
                        btn_submit.setVisibility(View.VISIBLE);
                    }

                    hideKeyboard(LiftingPermitActivity.this);
                } else {


                }

                //b.dismiss();
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

                    Toast toast = Toast.makeText(LiftingPermitActivity.this, "Please enter authorized person,password and reason", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(LiftingPermitActivity.this, "Please select authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(LiftingPermitActivity.this, "Please valid password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (!Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(LiftingPermitActivity.this, "Please enter reason", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    //password and reason blank
                    Toast toast = Toast.makeText(LiftingPermitActivity.this, "Please valid password and reason", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    //password and permit
                    Toast toast = Toast.makeText(LiftingPermitActivity.this, "Please valid password and authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (!Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    //reason and permit
                    Toast toast = Toast.makeText(LiftingPermitActivity.this, "Please enter reason and authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else {
                    if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                        new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }

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
                            txt_permit_closed.setText(Permitname);
                            Permitclosed = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (ReasonVal.equals("2")) {
                            txt_spotcheck.setText(Permitname);
                            Spotcheck = txt_authorizeArrayList.get(position).getAuthorizeid();
                        }else if(ReasonVal.equals("3")){
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
    }

    public void dateListner() {
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
                datePickerDialog = new DatePickerDialog(LiftingPermitActivity.this,
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
                                            Toast.makeText(LiftingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
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

                                        Toast.makeText(LiftingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_cancel_date.setText(day + "-"
                                            + (month + 1) + "-" + Year);
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(LiftingPermitActivity.this, "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);


                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

                //datePickerDialog.getDatePicker().setMaxDate(enddate);

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

             //   btn_fromtime.setText(UpdateTime.updateTime(hour, minute));
              //  btn_totime.setText(UpdateTime.updateTime(hour1, minute));

                mTimePicker = new TimePickerDialog(LiftingPermitActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                String time = UpdateTime.updateTime(selectedHour, selectedMinute);
                                String time1 = "";
                                int sel = selectedHour + 8;
                                if (sel >= 24) {
                                    time1 = "11:59 PM";
                                } else {
                                    time1 = UpdateTime.updateTime((selectedHour + 8), selectedMinute);
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
                                    if ((selectedHour + 9) > WAEndTimeHr) {
                                        if (selectedHour == 13) {
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
                                            Toast.makeText(LiftingPermitActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        } else {
                                            btn_totime.setText(WAEndTime1);
                                        }

                                    } else {
                                        if (selectedHour >= 10 && selectedHour < 13) {
                                            if((selectedHour+9) >= WAEndTimeHr) {
                                                time = UpdateTime.updateTime(WAEndTimeHr, WAEndTimeMin);
                                                btn_totime.setText(time);

                                            }else{
                                                time = UpdateTime.updateTime((selectedHour + 9), selectedMinute);
                                                btn_totime.setText(time);
                                            }

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
                                            Toast.makeText(LiftingPermitActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(LiftingPermitActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(LiftingPermitActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    } else if (WAStartTimeHr == selectedHour && selectedMinute >= WAStartTimemin) {
                                        if(selectedHour <= 13){
                                            time1 = UpdateTime.updateTime((selectedHour + 9), selectedMinute);
                                            btn_totime.setText(time1);
                                        }else{
                                            time1 = UpdateTime.updateTime((selectedHour + 8), selectedMinute);
                                            btn_totime.setText(time1);
                                        }

                                        //btn_totime.setText(time1);
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

                                            Toast.makeText(LiftingPermitActivity.this, msg, Toast.LENGTH_SHORT).show();
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





       /* btn_fromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int hour1 = hour + 7;
                int minute = mcurrentTime.get(Calendar.MINUTE);

                *//* int hour1 = hour+4;*//*

                TimePickerDialog mTimePicker;

                btn_fromtime.setText(UpdateTime.updateTime(hour, minute));
                btn_fromtime.setText(UpdateTime.updateTime(hour1, minute));
                *//*  edt_andto_time.setText(hour +4 + ":" + minute + " ");*//*

                mTimePicker = new TimePickerDialog(LiftingPermitActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                String time = UpdateTime.updateTime(selectedHour, selectedMinute);

                                if (selectedHour >= 9) {
                                    to_time = UpdateTime.updateTime((selectedHour + 8), selectedMinute);
                                    if(to_time.contains("AM")){
                                        btn_totime.setText("11:59 PM");
                                    }else{
                                        btn_totime.setText(to_time);
                                    }
                                    btn_fromtime.setText(time);

                                } else if (selectedHour < 9) {
                                    btn_fromtime.setText(UpdateTime.updateTime(mcurrentTime.get(Calendar.HOUR_OF_DAY), mcurrentTime.get(Calendar.MINUTE)));
                                    btn_totime.setText(UpdateTime.updateTime(mcurrentTime.get(Calendar.HOUR_OF_DAY) + 8, mcurrentTime.get(Calendar.MINUTE)));
                                    Toast.makeText(LiftingPermitActivity.this, "You cannot select time before 9:00 AM", Toast.LENGTH_SHORT).show();
                                }

                               *//* if (selectedHour >= 9) {
                                    to_time = UpdateTime.updateTime((selectedHour + 7), selectedMinute);
                                    btn_fromtime.setText(time);
                                    btn_totime.setText(to_time);
                                } else if (selectedHour < 9) {
                                    btn_fromtime.setText(UpdateTime.updateTime(mcurrentTime.get(Calendar.HOUR_OF_DAY), mcurrentTime.get(Calendar.MINUTE)));
                                    btn_totime.setText(UpdateTime.updateTime(mcurrentTime.get(Calendar.HOUR_OF_DAY) + 7, mcurrentTime.get(Calendar.MINUTE)));
                                    Toast.makeText(LiftingPermitActivity.this, "You cannot select time before 9:00 AM", Toast.LENGTH_SHORT).show();
                                }*//*


                                //  to_time = updateTime((selectedHour + 4), selectedMinute);
                               *//* btn_fromtime.setText(time);
                                btn_totime.setText(to_time);*//*
                            }
                        }, hour, minute, false);// Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });*/


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

                mTimePicker = new TimePickerDialog(LiftingPermitActivity.this,
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

                mTimePicker = new TimePickerDialog(LiftingPermitActivity.this,
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


        btn_fromdate.setOnClickListener(new View.OnClickListener() {
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

                /*SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date2 = new Date(enddate);
                String end_dayof_week = targetFormat.format(date2);*/

              /*  SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date2 = new Date(end1);*/

                /*String chat_time = targetFormat.format(date);*/


                // Launch Date Picker Dialog
                datePickerDialog = new DatePickerDialog(LiftingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_fromdate.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(LiftingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + " - " + (month + 1)
                                            + " -" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                 datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);


            }
        });

        btn_last_certificate.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(LiftingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                                btn_last_certificate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();
            }
        });



       /* btn_last_certificate.setOnClickListener(new View.OnClickListener() {
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
                *//*SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z");*//*
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

                *//*SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date2 = new Date(enddate);
                String end_dayof_week = targetFormat.format(date2);*//*

         *//*  SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date2 = new Date(end1);*//*

         *//*String chat_time = targetFormat.format(date);*//*


                // Launch Date Picker Dialog
                datePickerDialog = new DatePickerDialog(LiftingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_last_certificate.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_last_certificate.setText(dayOfMonth + " - "
                                                    + (monthOfYear + 1) + " - " + year);
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_last_certificate.setText(day + " - "
                                                    + (month + 1) + " - " + Year);
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(LiftingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_last_certificate.setText(dayOfMonth + " - "
                                                    + (monthOfYear + 1) + " - " + year);
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_last_certificate.setText(dayOfMonth + " - "
                                                + (monthOfYear + 1) + " - " + year);
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_last_certificate.setText(day + " - "
                                                + (month + 1) + " - " + Year);
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_last_certificate.setText(day + " - "
                                            + (month + 1) + " - " + Year);
                                    trnselectDate = Year + " - " + (month + 1)
                                            + " -" + day + " 00:00:00.000";
                                    Toast.makeText(getApplicationContext(), "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);

                *//*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*//*
              //nilesh-17 feb
               // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);

                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);


            }
        });*/


        btn_liftingdate.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(LiftingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_liftingdate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_liftingdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_liftingdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(LiftingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_liftingdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_liftingdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_liftingdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(LiftingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_liftingdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(LiftingPermitActivity.this, "Past date is  accepted", Toast.LENGTH_SHORT).show();
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

        btn_liftingdate1.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(LiftingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_liftingdate1.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_liftingdate1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_liftingdate1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(LiftingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_liftingdate1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_liftingdate1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_liftingdate1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(LiftingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_liftingdate1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(LiftingPermitActivity.this, "Past date is  accepted", Toast.LENGTH_SHORT).show();
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
        btn_liftingdate2.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(LiftingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_liftingdate2.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_liftingdate2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_liftingdate2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(LiftingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_liftingdate2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_liftingdate2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_liftingdate2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(LiftingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_liftingdate2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(LiftingPermitActivity.this, "Past date is  accepted", Toast.LENGTH_SHORT).show();
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

        btn_liftingdate3.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(LiftingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_liftingdate3.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_liftingdate3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_liftingdate3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(LiftingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_liftingdate3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_liftingdate3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_liftingdate3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(LiftingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_liftingdate3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(LiftingPermitActivity.this, "Past date is  accepted", Toast.LENGTH_SHORT).show();
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

        btn_liftingdate4.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(LiftingPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_liftingdate4.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_liftingdate4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_liftingdate4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(LiftingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_liftingdate4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_liftingdate4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_liftingdate4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(LiftingPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_liftingdate4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(LiftingPermitActivity.this, "Past date is  accepted", Toast.LENGTH_SHORT).show();
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


    private void authorizedialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LiftingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText edt_password = dialogView.findViewById(R.id.edt_password);
        final Button txt_submit = dialogView.findViewById(R.id.txt_submit);
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
                spinner_authorize.setSelection(0);
                txt_authorize.setText("");
                b.dismiss();
            }
        });
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progressDialog.show();
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

                    if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                        new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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
                authorizedPersonAdapter = new AuthorizedPersonAdapter(LiftingPermitActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                // spinner_authorize1.setAdapter(authorizedPersonAdapter);
                // spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                // spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            }

        }

      /*  spinner_authorize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    } else {
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LiftingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.password_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText edt_password = dialogView.findViewById(R.id.edt_password);
        final Button txt_submit = dialogView.findViewById(R.id.txt_submit);
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
                spinner_authorize1.setSelection(0);
                txt_authorize1.setText("");
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
                    if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                        new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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
                authorizedPersonAdapter = new AuthorizedPersonAdapter(LiftingPermitActivity.this, authorizedPersonArrayList);
                //spinner_authorize.setAdapter(authorizedPersonAdapter);
                spinner_authorize1.setAdapter(authorizedPersonAdapter);
                // spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                // spinner_permit_closed.setAdapter(authorizedPersonAdapter);
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

    private void Reasondialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LiftingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.reason_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        final EditText password = dialogView.findViewById(R.id.edt_password);
        spinner_permit_closed = dialogView.findViewById(R.id.spinner_permit_closed);

        final EditText reason = dialogView.findViewById(R.id.edt_reason);
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

                spinner_permit_closed.setSelection(0);
                txt_permit_closed.setText("");
                b.dismiss();
            }
        });
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = password.getText().toString();
                String Reason = reason.getText().toString();
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);

                if (Password.equalsIgnoreCase("") && Reason.equalsIgnoreCase("")) {

                    text.setText("Please enter password and Reason");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, -140);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();


                } else if (Password.equalsIgnoreCase("") && !Reason.equalsIgnoreCase("")) {

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
                } else {
                    if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                        new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userLoginId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
       /* spinner_permit_closed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    } else {
                        PermitClosed = authorizedPersonArrayList.get(position).getAuthorizeid();
                        String Permitname = authorizedPersonArrayList.get(position).getAuthorizename();
                        txt_permit_closed.setText(Permitname);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                showProgress();
                new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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
                authorizedPersonAdapter = new AuthorizedPersonAdapter(LiftingPermitActivity.this, authorizedPersonArrayList);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            }


        }
    }

    private void Spotcheckdialog() {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(LiftingPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.reason_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText edt_password = dialogView.findViewById(R.id.edt_password);
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        EditText reason = dialogView.findViewById(R.id.edt_reason);
        spinner_spotcheck = dialogView.findViewById(R.id.spinner_permit_closed);

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
                txt_spotcheck.setText("");
                b.dismiss();
            }
        });
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // String Password = edt_password.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                Password = edt_password.getText().toString();

                if (Password.equalsIgnoreCase("")) {
                    text.setText("Please enter password");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, -160);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                } else {

                    if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                        new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                showProgress();
                new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new LiftingPermitActivity.DownloadAuthorizedPersonData().execute();
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
                authorizedPersonAdapter = new AuthorizedPersonAdapter(LiftingPermitActivity.this, authorizedPersonArrayList);
                spinner_spotcheck.setAdapter(authorizedPersonAdapter);

            }

        }
        spinner_spotcheck.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    } else {
                        SpotCheck = authorizedPersonArrayList.get(position).getAuthorizeid();

                        String name = authorizedPersonArrayList.get(position).getAuthorizename();
                        txt_spotcheck.setText(name);


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

                if (CommonClass.checkNet(LiftingPermitActivity.this)) {
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

       // mprogress.setVisibility(View.VISIBLE);

    }

    private void dismissProgress() {

       // mprogress.setVisibility(View.GONE);


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
                res = CommonClass.OpenConnection(url, LiftingPermitActivity.this);
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
                //   Toast.makeText(LiftingPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(authorizedPersonArrayList);
                editor.putString("Authorized", json);
                editor.commit();
                authorizedPersonAdapter = new AuthorizedPersonAdapter(LiftingPermitActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                spinner_authorize1.setAdapter(authorizedPersonAdapter);
                spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);


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
                res = CommonClass.OpenConnection(url, LiftingPermitActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    contractorListActivityArrayList = new ArrayList<>();
                    contractorListActivityArrayList.clear();
                    ContractorList contractorList = new ContractorList();
                    contractorList.setCustVendorMasterId("");
                    contractorList.setCustVendorName("--Select--");
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
                //   Toast.makeText(LiftingPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(contractorListActivityArrayList);
                editor.putString("Contractor", json);
                editor.commit();
                permitContractorListAdapter = new PermitContractorListAdapter(LiftingPermitActivity.this, contractorListActivityArrayList);
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


                if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                    showProgress();
                    new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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

                if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                    showProgress();
                    new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                    showProgress();
                    new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                    showProgress();
                    new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                    showProgress();
                    new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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

                if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                    showProgress();
                    new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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


                return true;

            default:
                return super.onOptionsItemSelected(item);
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
            url = CompanyURL + WebAPIUrl.api_GetLPPermitNo;

            res = CommonClass.OpenConnection(url, LiftingPermitActivity.this);

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
                res = CommonClass.OpenConnection(url, LiftingPermitActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    depotArrayList = new ArrayList<>();
                    depotArrayList.clear();
                    Depot depot = new Depot();
                    depot.setDepotid("--Select--");
                    depot.setDepotname("--Select--");
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
                //  Toast.makeText(LiftingPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(depotArrayList);
                editor.putString("Depot", json);
                editor.commit();
                depotAdapter = new DepotAdapter(LiftingPermitActivity.this, depotArrayList);
                spinner_station.setAdapter(depotAdapter);


            }


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
                res = CommonClass.OpenConnection(url, LiftingPermitActivity.this);
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
                //Toast.makeText(LiftingPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(operationArrayList);
                editor.putString("Operation", json);
                editor.commit();
                operationAdapter = new OperationAdapter(LiftingPermitActivity.this, operationArrayList);
                spinner_operation.setAdapter(operationAdapter);


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
                res = CommonClass.OpenConnection(url, LiftingPermitActivity.this);
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
                    LocationArraylist.add(0,location);


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
                locationOperationAdapter = new LocationOperationAdapter(LiftingPermitActivity.this, LocationArraylist);
                spinner_location.setAdapter(locationOperationAdapter);
                // Toast.makeText(LiftingPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                locationOperationAdapter = new LocationOperationAdapter(LiftingPermitActivity.this, LocationArraylist);
                spinner_location.setAdapter(locationOperationAdapter);


                //  if (Mode.equalsIgnoreCase("E")) {
                if (LocationId != "") {
                    int locationpos = -1;
                    for (int j = 0; j < LocationArraylist.size(); j++) {
                        if (LocationArraylist.get(j).getLocationMasterId().equals(LocationId)) {
                            locationpos = j;
                            break;
                        }
                    }
                    if (locationpos != -1)
                        spinner_location.setSelection(locationpos);
                    else
                        spinner_location.setSelection(0);
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
//

                    //
                    LiftingPermitActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dismissProgress();
                            Toast.makeText(LiftingPermitActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();

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
                        LiftingPermitActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dismissProgress();
                                Toast.makeText(LiftingPermitActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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
dismissProgress();

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
                res = CommonClass.OpenConnection(url, LiftingPermitActivity.this);
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
                // Toast.makeText(LiftingPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(safetyToolsArrayList);
                editor.putString("safety", json);
                editor.commit();
                safetyAdapter = new SafetyAdapter(LiftingPermitActivity.this, safetyToolsArrayList, "LP", Mode, PermitStatus);
                grid_safety.setAdapter(safetyAdapter);


            }


        }
    }

    public void saveactivityjson() {

        ActivityJson = new JSONObject();


        StartDate = btn_fromdate.getText().toString();
        fromtime = btn_fromtime.getText().toString();
        totime = btn_totime.getText().toString();
        descload = edit_desc_load.getText().toString();
        overalldimension = edit_dimensions.getText().toString();
        WghtLoad = edit_load.getText().toString() +" " +weight;
        ShiftedLoad = edit_distance.getText().toString();
        ShiftedHeight = edit_height.getText().toString();
        lifttypes = edit_lift_types.getText().toString();
        safewrk = edit_safe_work.getText().toString();
        lstcertificateDate = btn_last_certificate.getText().toString();
        maxlen = edit_mac_maxlen.getText().toString();
        raddistance = edit_rad_dist.getText().toString();
        safeworkload = edit_safework_load.getText().toString();
        liftgeartypes = edit_liftgear_types.getText().toString();
        gearcertificate = edit_certificate_gears.getText().toString();
        Supervisor_Name = edit_supervisor_name.getText().toString();
        Supervisor_certi = edit_supervisor_certi.getText().toString();
        CraneOperator_Name = edit_craneoperator_name.getText().toString();
        CraneOperator_Certi = edit_craneoperator_certi.getText().toString();
        RiggerName = edit_rigger_name.getText().toString();
        RiggerCerti = edit_rigger_certi.getText().toString();
        Signalman_Name = edit_signalman_name.getText().toString();
        Signalman_certi = edit_signalman_certi.getText().toString();
        GroundPrecaution1 = edit_Precaution1.getText().toString();
        Precaution2 = edit_precaution2.getText().toString();
        Precaution3 = edit_Precaution3.getText().toString();
        Precaution4 = edit_precaution4.getText().toString();
        specifyprecaution = edit_specify.getText().toString();
        PermitclosedDate = btn_liftingdate3.getText().toString();
        spotcheckdate = btn_liftingdate4.getText().toString();
        Remarks = edit_remarks.getText().toString();

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
                user = new String[safetyToolsArrayList.size()];
                for (int i = 0; i < safetyToolsArrayList.size(); i++) {
                    String Safety = safetyToolsArrayList.get(i).getSafetyToolMasterId();
                    user[i] = Safety.toString();
                    safetytools = TextUtils.join(",", user);
                    if (safetyToolsArrayList.get(i).getSafetyToolDesc().equals("Other(s)")) {
                        if (safetyToolsArrayList.get(i).getRemarks() != null) {
                            safety_Others = safetyToolsArrayList.get(i).getRemarks();
                        }

                    }
                }

            }
        }
        try {
            ActivityJson.put("FormId", PKFormId);
            ActivityJson.put("PermitNo", PermitNo);
            ActivityJson.put("FkWareHouseMasterId", StationId);
            ActivityJson.put("FkWorkAuthorizationMasterId", WAH_No);
            ActivityJson.put("PermitDate", StartDate);
            ActivityJson.put("PermitFromTime", fromtime);
            ActivityJson.put("PermitToTime", totime);
            ActivityJson.put("fkContractorId", contractorId);
            ActivityJson.put("fkOperationMasterId", OperationId);
            ActivityJson.put("FkLocationMasterId", LocationId);
            ActivityJson.put("Descload", descload);
            ActivityJson.put("Overalldimension", overalldimension);
            ActivityJson.put("WghtLoad", WghtLoad);
            ActivityJson.put("Weight_KE", estimatedweight);
            ActivityJson.put("ShiftedLoad", ShiftedLoad);
            ActivityJson.put("ShiftedHeight", ShiftedHeight);
            ActivityJson.put("Lifttypes", lifttypes);
            ActivityJson.put("Safewrk", safewrk);
            ActivityJson.put("lstcertificateDate", lstcertificateDate);
            ActivityJson.put("Maxlen", maxlen);
            ActivityJson.put("Raddistance", raddistance);
            ActivityJson.put("Safeworkload", safeworkload);
            ActivityJson.put("Liftgeartypes", liftgeartypes);
            ActivityJson.put("Gearcertificate", gearcertificate);
            ActivityJson.put("Gasdetect", gasdetect);
            ActivityJson.put("Emergencystop", emergencystop);
            ActivityJson.put("Loadingpt", loadingpt);
            ActivityJson.put("CrewCommunication", CrewCommunication);
            ActivityJson.put("GroundCondition", GroundCondition);
            ActivityJson.put("LiftingSupervisorName", Supervisor_Name);
            ActivityJson.put("LiftingSupervisorQualification", Supervisor_certi);
            ActivityJson.put("CraneOperatorName", CraneOperator_Name);
            ActivityJson.put("CraneOperatorQualification", CraneOperator_Certi);
            ActivityJson.put("RiggerName", RiggerName);
            ActivityJson.put("RiggerQualification", RiggerCerti);
            ActivityJson.put("SignalmanName", Signalman_Name);
            ActivityJson.put("SignalmanQualification", Signalman_certi);
            ActivityJson.put("GroundPrecaution", GroundPrecaution1);
            ActivityJson.put("ObstaclesPrecaution", Precaution2);
            ActivityJson.put("SafetyMeasures", SafetyMeasures);
            ActivityJson.put("SafetyPrecaution", Precaution3);
            ActivityJson.put("Adequatelight", Adequatelight);
            ActivityJson.put("Demarcation", Demarcation);
            ActivityJson.put("DemarcationPrecaution", Precaution4);
            ActivityJson.put("specifyprecaution", specifyprecaution);
            ActivityJson.put("safetytools", safetytools);
            ActivityJson.put("AuthorizeBy1", AuthorizedId);
            ActivityJson.put("AuthorizeBy2", Authorized1Id);
            ActivityJson.put("RespContractorId", contractorId);
            ActivityJson.put("PermitClosedBy", Permitclosed);
            ActivityJson.put("PermitclosedDAte", PermitclosedDate);
            ActivityJson.put("SpotCheckBy", Spotcheck);
            ActivityJson.put("SpotCheckDate", spotcheckdate);
            ActivityJson.put("PermitCancelBy", cancelId);
            ActivityJson.put("PermitCancelDate", cancelDate);
            ActivityJson.put("PermitCloseRemarks", Remarks);
            ActivityJson.put("SpotImage", "");
            ActivityJson.put("LP_ProOthers", safety_Others);
            ActivityJson.put("Obstacles", obstacleVal);
            ActivityJson.put("GoldenRulesId", goldenRulesList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String FinalJsonObject = ActivityJson.toString();
        /*String URL = CompanyURL+ WebAPIUrl.api_LiftingPermit;
        String op = "Success";*/

        if (CommonClass.checkNet(LiftingPermitActivity.this)) {
            showProgress();
            new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadPostData().execute(FinalJsonObject);
                }

                @Override
                public void callfailMethod(String msg) {
                    CommonClass.displayToast(LiftingPermitActivity.this, msg);
                    dismissProgress();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }


    }

    public void editjson() {

        ActivityJson = new JSONObject();


        StartDate = btn_fromdate.getText().toString();
        fromtime = btn_fromtime.getText().toString();
        totime = btn_totime.getText().toString();
        cancelDate = btn_cancel_date.getText().toString();
        descload = edit_desc_load.getText().toString();
        overalldimension = edit_dimensions.getText().toString();
        WghtLoad = edit_load.getText().toString() +" " + weight;
        ShiftedLoad = edit_distance.getText().toString();
        ShiftedHeight = edit_height.getText().toString();
        lifttypes = edit_lift_types.getText().toString();
        safewrk = edit_safe_work.getText().toString();
        lstcertificateDate = btn_last_certificate.getText().toString();
        maxlen = edit_mac_maxlen.getText().toString();
        raddistance = edit_rad_dist.getText().toString();
        safeworkload = edit_safework_load.getText().toString();
        liftgeartypes = edit_liftgear_types.getText().toString();
        gearcertificate = edit_certificate_gears.getText().toString();
        Supervisor_Name = edit_supervisor_name.getText().toString();
        Supervisor_certi = edit_supervisor_certi.getText().toString();
        CraneOperator_Name = edit_craneoperator_name.getText().toString();
        CraneOperator_Certi = edit_craneoperator_certi.getText().toString();
        RiggerName = edit_rigger_name.getText().toString();
        RiggerCerti = edit_rigger_certi.getText().toString();
        Signalman_Name = edit_signalman_name.getText().toString();
        Signalman_certi = edit_signalman_certi.getText().toString();
        GroundPrecaution1 = edit_Precaution1.getText().toString();
        Precaution2 = edit_precaution2.getText().toString();
        Precaution3 = edit_Precaution3.getText().toString();
        Precaution4 = edit_precaution4.getText().toString();
        specifyprecaution = edit_specify.getText().toString();
        PermitclosedDate = btn_liftingdate3.getText().toString();
        spotcheckdate = btn_liftingdate4.getText().toString();
        Remarks = edit_remarks.getText().toString();


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
                user = new String[safetyToolsArrayList.size()];
                for (int i = 0; i < safetyToolsArrayList.size(); i++) {
                    String Safety = safetyToolsArrayList.get(i).getSafetyToolMasterId();
                    user[i] = Safety.toString();
                    safetytools = TextUtils.join(",", user);
                    if (safetyToolsArrayList.get(i).getSafetyToolDesc().equals("Other(s)")) {
                        if (safetyToolsArrayList.get(i).getRemarks() != null) {
                            safety_Others = safetyToolsArrayList.get(i).getRemarks();
                        }

                    }
                }

            }
        }
        try {
            ActivityJson.put("FormId", PKFormId);
            ActivityJson.put("PermitNo", PermitNo);
            ActivityJson.put("FkWareHouseMasterId", StationId);
            ActivityJson.put("FkWorkAuthorizationMasterId", WAH_No);
            ActivityJson.put("PermitDate", StartDate);
            ActivityJson.put("PermitFromTime", fromtime);
            ActivityJson.put("PermitToTime", totime);
            ActivityJson.put("fkContractorId", contractorId);
            ActivityJson.put("fkOperationMasterId", OperationId);
            ActivityJson.put("FkLocationMasterId", LocationId);
            ActivityJson.put("Descload", descload);
            ActivityJson.put("Overalldimension", overalldimension);
            ActivityJson.put("WghtLoad", WghtLoad);
            ActivityJson.put("Weight_KE", estimatedweight);
            ActivityJson.put("ShiftedLoad", ShiftedLoad);
            ActivityJson.put("ShiftedHeight", ShiftedHeight);
            ActivityJson.put("Lifttypes", lifttypes);
            ActivityJson.put("Safewrk", safewrk);
            ActivityJson.put("lstcertificateDate", lstcertificateDate);
            ActivityJson.put("Maxlen", maxlen);
            ActivityJson.put("Raddistance", raddistance);
            ActivityJson.put("Safeworkload", safeworkload);
            ActivityJson.put("Liftgeartypes", liftgeartypes);
            ActivityJson.put("Gearcertificate", gearcertificate);
            ActivityJson.put("Gasdetect", gasdetect);
            ActivityJson.put("Emergencystop", emergencystop);
            ActivityJson.put("Loadingpt", loadingpt);
            ActivityJson.put("CrewCommunication", CrewCommunication);
            ActivityJson.put("GroundCondition", GroundCondition);
            ActivityJson.put("LiftingSupervisorName", Supervisor_Name);
            ActivityJson.put("LiftingSupervisorQualification", Supervisor_certi);
            ActivityJson.put("CraneOperatorName", CraneOperator_Name);
            ActivityJson.put("CraneOperatorQualification", CraneOperator_Certi);
            ActivityJson.put("RiggerName", RiggerName);
            ActivityJson.put("RiggerQualification", RiggerCerti);
            ActivityJson.put("SignalmanName", Signalman_Name);
            ActivityJson.put("SignalmanQualification", Signalman_certi);
            ActivityJson.put("GroundPrecaution", GroundPrecaution1);
            ActivityJson.put("ObstaclesPrecaution", Precaution2);
            ActivityJson.put("SafetyMeasures", SafetyMeasures);
            ActivityJson.put("SafetyPrecaution", Precaution3);
            ActivityJson.put("Adequatelight", Adequatelight);
            ActivityJson.put("Demarcation", Demarcation);
            ActivityJson.put("DemarcationPrecaution", Precaution4);
            ActivityJson.put("specifyprecaution", specifyprecaution);
            ActivityJson.put("safetytools", safetytools);
            ActivityJson.put("AuthorizeBy1", AuthorizedId);
            ActivityJson.put("AuthorizeBy2", Authorized1Id);
            ActivityJson.put("RespContractorId", contractorId);
            ActivityJson.put("PermitClosedBy", Permitclosed);
            ActivityJson.put("PermitclosedDAte", PermitclosedDate);
            ActivityJson.put("SpotCheckBy", Spotcheck);
            ActivityJson.put("SpotCheckDate", spotcheckdate);
            ActivityJson.put("PermitCancelBy", cancelId);
            ActivityJson.put("PermitCancelDate", cancelDate);
            ActivityJson.put("PermitCloseRemarks", Remarks);
            ActivityJson.put("LP_ProOthers", safety_Others);
            ActivityJson.put("Obstacles", obstacleVal);
            ActivityJson.put("GoldenRulesId", goldenRulesList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String FinalEditJsonObject = ActivityJson.toString();
        /*String URL = CompanyURL+ WebAPIUrl.api_LiftingPermit;
        String op = "Success";*/

        if (CommonClass.checkNet(LiftingPermitActivity.this)) {
            showProgress();
            new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadEditPostData().execute(FinalEditJsonObject);
                }

                @Override
                public void callfailMethod(String msg) {
                    CommonClass.displayToast(LiftingPermitActivity.this, msg);
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
                String url = CompanyURL + WebAPIUrl.api_LiftingPermit;
                res = CommonClass.OpenPostConnection(url, objFinalObj, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
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
                Toast.makeText(LiftingPermitActivity.this, "Data saved successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LiftingPermitActivity.this, SupervisorMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                //Intent intent = new Intent(AssignActivity.this, ActivityMain.class);
                //startActivity(intent);
            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {
                Toast.makeText(LiftingPermitActivity.this, "Data not save successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LiftingPermitActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
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
                String url = CompanyURL + WebAPIUrl.api_PosteditLifting;
                res = CommonClass.OpenPostConnection(url, objFinalObj, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
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
                Toast.makeText(LiftingPermitActivity.this, "Data Updated Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LiftingPermitActivity.this, PermitStatusActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                //Intent intent = new Intent(AssignActivity.this, ActivityMain.class);
                //startActivity(intent);
            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {
                Toast.makeText(LiftingPermitActivity.this, "Data not updated successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LiftingPermitActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void showMessageDialog(final Context parent, String title,
                                         String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setMessage(msg);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    class DownloadIsValidUser extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LiftingPermitActivity.this);
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
                        //URLEncoder.encode("z207", "UTF-8") + "&PlantId=" +
                        URLEncoder.encode("ail", "UTF-8") + "&PlantId=" +
                        URLEncoder.encode("1","UTF-8") + "&UserLoginId=" +
                        URLEncoder.encode(id, "UTF-8") + "&UserPwd=" +
                        URLEncoder.encode(Password, "UTF-8");
                res = CommonClass.OpenConnection(url, LiftingPermitActivity.this);
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
            //   mprogress.setVisibility(View.INVISIBLE);

            if (integer.contains("true") || integer.equals("")) {
                Toast.makeText(LiftingPermitActivity.this, "Valid Password", Toast.LENGTH_SHORT).show();
                //b.dismiss();

                /*if(ReasonVal.equals("1") || ReasonVal.equals("2") && Mode.equals("E")){
                    btn_submit.setVisibility(View.VISIBLE);
                }*/

                cusDialog1.setVisibility(View.GONE);
                cusDialog2.setVisibility(View.GONE);
                spinner_authorize.setSelection(0);
                spinner_permit_closed.setSelection(0);
                edit_password_pass.setText("");
                edit_password_reason.setText("");
                edit_reason.setText("");
                hideKeyboard(LiftingPermitActivity.this);

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

    /***************************Not Used*********************************/


    /*  spinner_permit_closed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });
*/
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
                            //passworddialog();
                        }


                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        /*spinner_authorize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    }else {
                        authorize = authorizedPersonArrayList.get(position).getAuthorizeid();
                        if (authorize.equals("--Select--")) {
                            // Toast.makeText(WorkAuthorizationActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }else {
                            passworddialog();
                        }
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


/*
        spinner_authorize1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    }else {
                        authorize1 = authorizedPersonArrayList.get(position).getAuthorizeid();
                        if (authorize1.equals("--Select--")) {

                            // Toast.makeText(WorkAuthorizationActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }else {
                           // passworddialog();
                        }
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/


     class DownloadWANo extends AsyncTask<String, Void, String> {
        String res = "", response = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String Id = params[0];

            String url = CompanyURL + WebAPIUrl.api_GetWANo + "?contractid=" + Id + "&permitOperationcode=LP";


            res = CommonClass.OpenConnection(url, LiftingPermitActivity.this);
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PermitNoWA permitNoWA = new PermitNoWA();
                permitNoWA.setPermitNo("Select");
                WAArayList.add(0,permitNoWA);
                if (WAArayList.size() > 0) {
                    ContractorPermitAdapter contractorPermitAdapter = new ContractorPermitAdapter(LiftingPermitActivity.this, WAArayList);
                    spinner_prevention_plan.setAdapter(contractorPermitAdapter);
                    //spinner_permit_closed.setAdapter(authorizedPersonAdapter);
                    if(Mode.equals("E")){
                        int pos = -1;
                        for(int i=0;i<WAArayList.size();i++){
                            if(WAH_No.equals(WAArayList.get(i).getPermitNo())){
                                pos = i;
                                break;
                            }
                        }
                        if(pos != -1){
                            spinner_prevention_plan.setSelection(pos);
                        }else{
                            spinner_prevention_plan.setSelection(0);
                        }
                    }
                    if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                        showProgress();
                        //Location Get
                        new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadGETWA_PermitNoDetail().execute(WAArayList.get(0).getPermitNo());
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                CommonClass.displayToast(getApplicationContext(), msg);
                                dismissProgress();
                            }
                        });
                    } else {
                        Toast.makeText(LiftingPermitActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }


                } else {

                //    spinner_prevention_plan.setSelection(0);
                }


            } else {
                WAArayList.clear();
                if(Mode.equals("E")){
                    if(WAH_No != ""){
                        PermitNoWA permitNo = new PermitNoWA();
                        permitNo.setPermitNo(WAH_No);
                        WAArayList.add(0, permitNo);
                        ArrayAdapter<PermitNoWA> permitNoArrayAdapter = new ArrayAdapter<>(LiftingPermitActivity.this,
                                android.R.layout.simple_spinner_item, WAArayList);
                        spinner_prevention_plan.setAdapter(permitNoArrayAdapter);
                    }
                }else {
                    Toast.makeText(LiftingPermitActivity.this, "No WorkAuthorization Present Against Selected Contractor", Toast.LENGTH_SHORT).show();
                    PermitNoWA permitNo = new PermitNoWA();
                    permitNo.setPermitNo("Select");
                    WAArayList.add(0, permitNo);
                    ContractorPermitAdapter contractorPermitAdapter = new ContractorPermitAdapter(LiftingPermitActivity.this, WAArayList);
                    spinner_prevention_plan.setAdapter(contractorPermitAdapter);
                    spinner_operation.setSelection(0);
                    spinner_location.setSelection(0);
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


            res = CommonClass.OpenConnection(url, LiftingPermitActivity.this);
            response = res;


            return response;


        }


        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

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
                            WAEndTime = UpdateTime.updateTime(WAEndTimeHr, WAEndTimeMin);
                            WAEndTime = UpdateTime.updateTime((WAStartTimeHr + 9), WAStartTimemin);
                            //   UpdateTime.updateTime((WAStartTimeHr+4),WAStartTimemin);
                            if ((WAStartTimeHr + 9) > 13 && (WAStartTimeHr + 9) < 14) {
                                WAEndTime = "01:00 PM";
                            } else {
                                if (WAEndTimeHr > 14 && (WAStartTimeHr + 9) < 13) {
                                    WAEndTime = UpdateTime.updateTime(WAStartTimeHr + 1, WAStartTimemin);
                                }
                                else {
                                    if (WAEndTimeHr >= WAStartTimeHr + 9) {
                                        WAEndTime = UpdateTime.updateTime((WAStartTimeHr + 9), WAStartTimemin);
                                    } else {
                                        if ((WAStartTimeHr + 9) >= 24) {
                                            WAEndTime = "11:59 PM";
                                        } else {
                                            WAEndTime = jorder.getString("ToTime2");
                                            Toast.makeText(LiftingPermitActivity.this, "You cannot choose time greater than work authorization end time", Toast.LENGTH_SHORT).show();
                                        }
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


                        /*StationId = jorder.getString("FkWareHouseMasterId");
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

                        }*/
                        try {
                            LocationId = jorder.getString("FkLocationMasterId");
                            // Mode = "E";

                            int locationpos = -1;
                            if (LocationArraylist != null) {
                                for (int j = 0; j < LocationArraylist.size(); j++) {
                                    if (LocationArraylist.get(j).getLocationMasterId().equals(LocationId)) {
                                        locationpos = j;
                                        break;
                                    }

                                }
                                if (locationpos != -1)
                                    spinner_location.setSelection(locationpos);
                                else
                                    spinner_location.setSelection(0);
                            } else {
                                if (CommonClass.checkNet(LiftingPermitActivity.this)) {
                                    new StartSession(LiftingPermitActivity.this, new CallbackInterface() {
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

                        spinner_contractor.setEnabled(false);
                        spinner_location.setEnabled(false);
                        spinner_operation.setEnabled(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                if (Mode.equals("A")) {
                    spinner_contractor.setEnabled(true);
                    //  spinner_location.setEnabled(true);
                    //  spinner_operation.setEnabled(true);
                    spinner_operation.setSelection(0);
                    spinner_location.setSelection(0);
                }
                Toast.makeText(LiftingPermitActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void CategoryWiseAuthorizeName(String categorydesc, String From) {
        String arrivalFrom = From;
        categoryDesc = categorydesc;

        txt_authorizeArrayList = new ArrayList<>();

        if(!StationId.equals("Select")) {
            if (authorizedPersonArrayList != null) {


                if (categoryDesc.equalsIgnoreCase("level 2")) {
                    for (int i = 0; i < authorizedPersonArrayList.size(); i++) {
                        if (authorizedPersonArrayList.get(i).getCategorydesc() == null ||
                                authorizedPersonArrayList.get(i).getCategorydesc().equals("Tier1")
                                || authorizedPersonArrayList.get(i).getCategorydesc().equals("Tier2") ||
                                authorizedPersonArrayList.get(i).getCategorydesc().equals("Admin")) {

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
                    authorizedPersonAdapter = new AuthorizedPersonAdapter(LiftingPermitActivity.this, txt_authorizeArrayList);
                    if (arrivalFrom.equalsIgnoreCase("fromReason")) {
                        spinner_permit_closed.setAdapter(authorizedPersonAdapter);
                    } else if (arrivalFrom.equalsIgnoreCase("fromPWD")) {
                        spinner_authorize.setAdapter(authorizedPersonAdapter);
                    }
                }
                //authorizedPersonAdapter.updateList(txt_authorizeArrayList);
                // spinner_authorize.setAdapter(authorizedPersonAdapter);
            }
        }else{
            AuthorizedPerson authorizedPerson = new AuthorizedPerson();
            authorizedPerson.setAuthorizename("Select");
            txt_authorizeArrayList.add(authorizedPerson);
            authorizedPersonAdapter = new AuthorizedPersonAdapter(LiftingPermitActivity.this, txt_authorizeArrayList);
            spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            spinner_authorize.setAdapter(authorizedPersonAdapter);
        }
    }

    class DownloadGoldenRules extends AsyncTask<String, Void, String> {
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
                res = CommonClass.OpenConnection(url, LiftingPermitActivity.this);
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
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LiftingPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(goldenRulesArrayList);
                editor.putString("GoldenRules", json);
                editor.commit();
                goldenRuleAdapter = new GoldenRuleAdapter(LiftingPermitActivity.this, goldenRulesArrayList, Mode, PermitStatus);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LiftingPermitActivity.this);
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
                res = CommonClass.OpenConnection(url, LiftingPermitActivity.this);
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

                authorizedPersonAdapter = new AuthorizedPersonAdapter(LiftingPermitActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);


            }


        }
    }


    /*public boolean timedifference(String time, String to_time) {

        boolean flag=false;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        try {
            Date startDate = simpleDateFormat.parse(time);
            Date endDate = simpleDateFormat.parse(to_time);

            long difference = endDate.getTime() - startDate.getTime();
            if(endDate.getTime()<startDate.getTime()){
                flag = false;
            }else
                flag = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }*/

}

