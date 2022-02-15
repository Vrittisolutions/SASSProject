package com.vritti.sass;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.MotionEvent;
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
import android.widget.ListView;
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
import com.vritti.sass.adapter.EquipmentAdapter;
import com.vritti.sass.adapter.GoldenRuleAdapter;
import com.vritti.sass.adapter.IndicateRiskAdapter;
import com.vritti.sass.adapter.LocationOperationAdapter;
import com.vritti.sass.adapter.OperationAdapter;
import com.vritti.sass.adapter.PermitContractorListAdapter;
import com.vritti.sass.adapter.PrevenionAdapter;
import com.vritti.sass.adapter.SafetyAdapter;
import com.vritti.sass.model.AuthorizedPerson;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.ContractorList;
import com.vritti.sass.model.DateFormatChange;
import com.vritti.sass.model.Depot;
import com.vritti.sass.model.EquipmentUse;
import com.vritti.sass.model.GoldenRules;
import com.vritti.sass.model.IndicateRisk;
import com.vritti.sass.model.Location;
import com.vritti.sass.model.Operation;
import com.vritti.sass.model.Permit;
import com.vritti.sass.model.PermitNoWA;
import com.vritti.sass.model.Prevention;
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
 * Created by sharvari on 23-Nov-18.
 */

public class ExcavationPermitActivity extends AppCompatActivity {

    ListView list_forceble_risk, list_precaution;
    RadioGroup radiogroup_method, radiogroup_clrancecerti;

    Button btn_todate, btn_clrancedate, btn_fromdate, btn_date1, btn_date2, btn_date3, btn_date4, btn_date5, btn_submit, btn_totime,
            btn_fromtime, btn_cancel_date;
    LinearLayout ln_station, ln_WAHNo, ln_contractor, ln_DescOperation, ln_location, len_cancel_permit;
    Spinner spinner_authorize, spinner_authorize1, spinner_authorize2, spinner_permit_closed, spinner_spotcheck,
            spinner_station, spinner_contractor, spinner_operation, spinner_location, spinner_prevention_plan;
    EditText edit_remarks, edit_permitno, edit_clranceno;
    RadioButton rd_clrno, rd_clryes;
    RadioButton rd_oprno, rd_opryes;
    int Year, month, day;
    String date, WAH_No = "";
    String data = "", to_time = "";
    DatePickerDialog datePickerDialog;
    ImageView img_camera;
    int MY_CAMERA_PERMISSION_CODE = 100;
    int MEDIA_TYPE_IMAGE = 1;
    int CAMERA_REQUEST = 101;
    private Uri fileUri;
    File mediaFile;
    private static String IMAGE_DIRECTORY_NAME = "SASS";
    String Password = "";

    TextView txt_authorize, txt_authorize1, txt_permit_closed, txt_spot_check, txt_cancel, txt_cancel_permit;
    String tempVal, ReasonVal;
    int cnt = -1;
    String WAStartTime = "", WAEndTime = "", WAEndTime1 = "";
    int WAEndTimeHr, WAEndTimeMin, WAStartTimeHr, WAStartTimemin;
    int modeefirst = -1;


    SharedPreferences userpreferences;
    private ArrayList<AuthorizedPerson> authorizedPersonArrayList;
    private ArrayList<AuthorizedPerson> txt_authorizeArrayList;
    String categoryDesc = "";
    private ArrayList<ContractorList> contractorListActivityArrayList;
    private ArrayList<Operation> operationArrayList;
    private ArrayList<Location> LocationArraylist;
    AuthorizedPersonAdapter authorizedPersonAdapter;
    PermitContractorListAdapter permitContractorListAdapter;
    OperationAdapter operationAdapter;
    private ArrayList<Depot> depotArrayList;
    LocationOperationAdapter locationOperationAdapter;
    DepotAdapter depotAdapter;

    //String CompanyURL;
    ProgressBar mprogress;
    SharedPreferences sharedPrefs;
    Gson gson;
    String json;
    Type type;
    String authorize = "", authorize1 = "", authorize2 = "", authorize3 = "", authorize4 = "", PermitClosed = "",
            SpotCheck = "", StationId = "", PermitNo = "", method_of_operation = "N", clearancecertival = "N", OperationName = "", userLoginId = "",
            contractorName = "", StationName = "";

    String AuthorizeId = "", Authorize1Id = "", Permitclosed = "", spotcheck = "", cancelId = "";
    int check = 0;
    private String serverResponseMessage, path, Imagefilename;

    PrevenionAdapter prevenionAdapter;
    EquipmentAdapterNew equipmentAdapter;
    IndicateRiskAdapterNew indicateRiskAdapter;

    ArrayList<Prevention> preventionArrayList;
    ArrayList<EquipmentUse> equipmentUseArrayList;
    ArrayList<IndicateRisk> indicateRiskArrayList;
    String PKFormId = "", formcode = "";
    LinearLayout len_foressable;

    GridView grid_safety;
    SafetyTools safetyTools;
    ArrayList<SafetyTools> safetyToolsArrayList;
    SafetyAdapter safetyAdapter;
    JSONObject ActivityJson;
    String StartDate = "", fromtime = "", totime = "", permitDate = "", spotdate = "", Remarks = "", response = "", preventionplan = "", clearancedate = "";
    String PermitclosedDate = "", contractorId = "", operationId = "", locationId = "", safetytools = "", foressablerisk = "", locationName = "",
            precautionuser = "", clearanceno = "", SpotCheckDate = "", AuthorizeDate1 = "", AuthorizeDate2 = "", AuthorizeDate3 = "", cancelDate = "";
    String[] user;
    String[] precautionusr;
    String[] user5;
    String[] foressableuser;
    String foressablersk_Others = "", precaution_Others = "", safety_Others = "";
    boolean isAns;

    AlertDialog b;
    private ProgressDialog progressDialog;
    RelativeLayout cusDialog1, cusDialog2;
    EditText edit_password_pass, edit_password_reason, edit_reason;
    LinearLayout ln_spinner_authorize, ln_spinner_reason;
    Button btn_cancel_pass, btn_submit_pass, btn_cancel_reason, btn_submit_reason;
    Permit permit;
    String Mode = "";
    String CompanyURL;

    private ArrayList<PermitNoWA> WAArayList = new ArrayList<>();
    LinearLayout len_p_closed, len_p_spot;
    TextView tx_p_closed;
    private String PermitStatus;
    RecyclerView list_goldenRules;
    ArrayList<GoldenRules> goldenRulesArrayList;
    GoldenRuleAdapter goldenRuleAdapter;
    GoldenRules goldenRules;
    String goldenRulesList = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.excavation_permit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        toolbar.setTitle(getResources().getString(R.string.application_name));
        setSupportActionBar(toolbar);


        initview();
        userpreferences = getSharedPreferences(LoginActivity.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", "");


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        contractorListActivityArrayList = new ArrayList<>();
        authorizedPersonArrayList = new ArrayList<>();
        operationArrayList = new ArrayList<>();
        LocationArraylist = new ArrayList<>();
        depotArrayList = new ArrayList<>();
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
                    rd_clryes.setClickable(false);
                    rd_clrno.setClickable(false);
                    btn_clrancedate.setEnabled(false);
                    btn_clrancedate.setTextColor(Color.parseColor("#000000"));
                    edit_clranceno.setKeyListener(null);
                    rd_opryes.setClickable(false);
                    rd_oprno.setClickable(false);
                    txt_authorize.setKeyListener(null);
                    txt_authorize1.setKeyListener(null);
                    btn_date1.setEnabled(false);
                    btn_date2.setEnabled(false);
                    btn_date3.setEnabled(false);
                    btn_date1.setTextColor(Color.parseColor("#000000"));
                    btn_date2.setTextColor(Color.parseColor("#000000"));
                    btn_date3.setTextColor(Color.parseColor("#000000"));
                    edit_remarks.setKeyListener(null);

                    if (PermitStatus.equalsIgnoreCase("R") || PermitStatus.equalsIgnoreCase("C")) {
                        txt_spot_check.setEnabled(false);
                        btn_date5.setEnabled(false);
                        btn_date5.setTextColor(Color.parseColor("#000000"));
                        txt_permit_closed.setEnabled(false);
                        btn_date4.setEnabled(false);
                        btn_date4.setTextColor(Color.parseColor("#000000"));
                        tx_p_closed.setKeyListener(null);
                        txt_cancel_permit.setKeyListener(null);
                        btn_cancel_date.setKeyListener(null);
                        btn_cancel_date.setTextColor(Color.parseColor("#000000"));
                        btn_submit.setClickable(false);
                        txt_cancel.setEnabled(false);
                        btn_cancel_date.setEnabled(false);
                        //btn_cancel_date.setTextColor(Color.parseColor("#000000"));
                    }

                }else{btn_fromdate.setEnabled(true);}

               // if (PermitStatus.equals("A") || PermitStatus.equals("p")) {
                    len_p_closed.setVisibility(View.VISIBLE);
                    tx_p_closed.setVisibility(View.VISIBLE);
                    len_cancel_permit.setVisibility(View.VISIBLE);
                    txt_cancel_permit.setVisibility(View.VISIBLE);
             /*   } else {
                    len_p_closed.setVisibility(View.GONE);
                    tx_p_closed.setVisibility(View.GONE);
                    len_cancel_permit.setVisibility(View.GONE);
                    txt_cancel_permit.setVisibility(View.GONE);
                }*/

                Mode = "E";
                PermitNo = permit.getPermitNo();
                //btn_fromdate.setEnabled(false);
                btn_fromdate.setTextColor(Color.parseColor("#101010"));

                if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                    new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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

                if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                    showProgress();
                    new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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
        }
        // Golden Rules
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("GoldenRules", "");
        type = new TypeToken<List<GoldenRules>>() {
        }.getType();
        goldenRulesArrayList = gson.fromJson(json, type);

        if (goldenRulesArrayList == null) {
            if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                showProgress();
                new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadGoldenRules().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(ExcavationPermitActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (goldenRulesArrayList.size() > 0) {

                goldenRuleAdapter = new GoldenRuleAdapter(ExcavationPermitActivity.this, goldenRulesArrayList, Mode, PermitStatus);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExcavationPermitActivity.this);

                // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                list_goldenRules.setLayoutManager(linearLayoutManager);
                list_goldenRules.setAdapter(goldenRuleAdapter);


            }

        }


        //Indicate Risk

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
        gson = new Gson();
        String indicatejson = sharedPrefs.getString("foressable", "");
        String equipmentjson = sharedPrefs.getString("precaution", "");
        Type indicatetype = new TypeToken<List<IndicateRisk>>() {
        }.getType();
        Type equipmenttype = new TypeToken<List<EquipmentUse>>() {
        }.getType();

        indicateRiskArrayList = gson.fromJson(indicatejson, indicatetype);
        equipmentUseArrayList = gson.fromJson(equipmentjson, equipmenttype);

        if (indicateRiskArrayList == null) {
            if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                showProgress();
                new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadExcavationData().execute();
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
                indicateRiskAdapter = new IndicateRiskAdapterNew(ExcavationPermitActivity.this, indicateRiskArrayList, Mode, PermitStatus);
                list_forceble_risk.setAdapter(indicateRiskAdapter);
                len_foressable.setVisibility(View.VISIBLE);

                //Utility.setListViewHeightBasedOnItems(list_indicaterisk);


            }

        }

        //Equipment
        if (equipmentUseArrayList == null) {
            if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                showProgress();

                new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadExcavationData().execute();
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
                equipmentAdapter = new EquipmentAdapterNew(ExcavationPermitActivity.this, equipmentUseArrayList, Mode, PermitStatus);
                list_precaution.setAdapter(equipmentAdapter);
                len_foressable.setVisibility(View.VISIBLE);
                // Utility.setListViewHeightBasedOnItems(list_equipment);

            }

        }


        // Safety Tools
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("safety", "");
        type = new TypeToken<List<SafetyTools>>() {
        }.getType();
        safetyToolsArrayList = gson.fromJson(json, type);

        if (safetyToolsArrayList == null) {
            if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                showProgress();
                new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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
                safetyAdapter = new SafetyAdapter(ExcavationPermitActivity.this, safetyToolsArrayList, "EP", Mode, PermitStatus);
                grid_safety.setAdapter(safetyAdapter);


            }

        }


        list_precaution.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


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
        btn_date1.setText(date);
        btn_date2.setText(date);
        btn_date3.setText(date);
        btn_date4.setText(date);
        btn_date5.setText(date);
        btn_clrancedate.setText(date);
        btn_cancel_date.setText(date);


        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int hour1 = hour + 7;
        int minute = mcurrentTime.get(Calendar.MINUTE);
        String time1 = UpdateTime.updateTime(hour, minute);
        if (hour1 >= 24) {
            to_time = "11:59 PM";
        } else {
            to_time = UpdateTime.updateTime(hour1, minute);
        }
        System.out.println("time: " + time1);


        btn_fromtime.setText(time1);
        btn_totime.setText(to_time);


        // Depot Station
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Depot", "");
        type = new TypeToken<List<Depot>>() {
        }.getType();
        depotArrayList = gson.fromJson(json, type);

        if (depotArrayList == null) {
            if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                showProgress();
                new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadDepotData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(ExcavationPermitActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (depotArrayList.size() > 0) {
                depotAdapter = new DepotAdapter(ExcavationPermitActivity.this, depotArrayList);
                spinner_station.setAdapter(depotAdapter);


            }

        }


        // ContractorList

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Contractor", "");
        type = new TypeToken<List<ContractorList>>() {
        }.getType();
        contractorListActivityArrayList = gson.fromJson(json, type);

        if (contractorListActivityArrayList == null) {
            if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                showProgress();
                new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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
                permitContractorListAdapter = new PermitContractorListAdapter(ExcavationPermitActivity.this, contractorListActivityArrayList);
                spinner_authorize2.setAdapter(permitContractorListAdapter);
                spinner_contractor.setAdapter(permitContractorListAdapter);
            }

        }

        // Operation

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Operation", "");
        type = new TypeToken<List<Operation>>() {
        }.getType();
        operationArrayList = gson.fromJson(json, type);

        if (operationArrayList == null) {
            if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                showProgress();
                new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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
                operationAdapter = new OperationAdapter(ExcavationPermitActivity.this, operationArrayList);
                spinner_operation.setAdapter(operationAdapter);
            }

        }


        spinner_station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                StationId = depotArrayList.get(position).getDepotid();


                if (StationId.contains("Select")) {

                } else {
                    if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                        showProgress();
                        //Location Get
                        new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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
                /*
                String authorize1pos = String.valueOf(spinner_authorize.getSelectedItemPosition());
                String authorize2pos = String.valueOf(spinner_authorize1.getSelectedItemPosition());
                String authorize3pos = String.valueOf(spinner_authorize2.getSelectedItemPosition());
                String permitcheckpos = String.valueOf(spinner_permit_closed.getSelectedItemPosition());
                String spotcheckpos = String.valueOf(spinner_spotcheck.getSelectedItemPosition());*/


                if (StationId.equalsIgnoreCase("select") || StationId.equalsIgnoreCase("")) {
                    Toast.makeText(ExcavationPermitActivity.this, "Please Fill Station Details", Toast.LENGTH_SHORT).show();
                    ln_station.setBackgroundResource(R.drawable.edit_text_red);
                } else if (WAH_No.equalsIgnoreCase("Select") || WAH_No.equalsIgnoreCase("")) {
                    Toast.makeText(ExcavationPermitActivity.this, "Please Fill WorkAuthorization No. Details", Toast.LENGTH_SHORT).show();
                    ln_WAHNo.setBackgroundResource(R.drawable.edit_text_red);
                } else if (contractorId.equalsIgnoreCase("select") || contractorId.equalsIgnoreCase("")) {
                    Toast.makeText(ExcavationPermitActivity.this, "Please Fill Contractor Details", Toast.LENGTH_SHORT).show();
                    ln_contractor.setBackgroundResource(R.drawable.edit_text_red);
                } else if (operationId.equalsIgnoreCase("select") || operationId.equalsIgnoreCase("")) {
                    Toast.makeText(ExcavationPermitActivity.this, "Please Fill Operation Details", Toast.LENGTH_SHORT).show();
                    ln_DescOperation.setBackgroundResource(R.drawable.edit_text_red);
                } else if (locationId.equalsIgnoreCase("select") || locationId.equalsIgnoreCase("")) {
                    Toast.makeText(ExcavationPermitActivity.this, "Please Fill Location Details", Toast.LENGTH_SHORT).show();
                    ln_location.setBackgroundResource(R.drawable.edit_text_red);
                } else if (rd_clryes.isChecked()) {
                    if (edit_clranceno.getText().toString().equals("")) {
                        Toast.makeText(ExcavationPermitActivity.this, "Please Fill Clearance No.", Toast.LENGTH_SHORT).show();
                        edit_clranceno.setBackgroundResource(R.drawable.edit_text_red);
                    }
                } else {
                    if (Mode.equalsIgnoreCase("A")) {
                        saveactivityjson();
                        //startActivity(new Intent(ExcavationPermitActivity.this, ExcavationPermitActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else if (Mode.equalsIgnoreCase("E")) {
                        editjson();
                        //startActivity(new Intent(ExcavationPermitActivity.this, ExcavationPermitActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else {

                    }
                }
/*
                if(StationId.equalsIgnoreCase("Select") || WAH_No.equalsIgnoreCase("")
                        || contractorId.equalsIgnoreCase("") || operationId.equalsIgnoreCase("") ||
                        locationId.equalsIgnoreCase("")){
                    Toast.makeText(ExcavationPermitActivity.this,"Please fill defined operation details",Toast.LENGTH_SHORT).show();
                }else {


               }*/
            }
        });

        btn_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                datePickerDialog = new DatePickerDialog(ExcavationPermitActivity.this,
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
                                            Toast.makeText(ExcavationPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
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

        btn_clrancedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                datePickerDialog = new DatePickerDialog(ExcavationPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_clrancedate.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_clrancedate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_clrancedate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ExcavationPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_clrancedate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_clrancedate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_clrancedate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_clrancedate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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
                // datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);


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
                //   btn_totime.setText(UpdateTime.updateTime(hour1, minute));

                mTimePicker = new TimePickerDialog(ExcavationPermitActivity.this,
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
                                            Toast.makeText(ExcavationPermitActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        } else {
                                            btn_totime.setText(WAEndTime1);
                                        }

                                    } else {
                                        if (selectedHour >= 10 && selectedHour < 13) {
                                            if ((selectedHour + 9) >= WAEndTimeHr) {
                                                time = UpdateTime.updateTime(WAEndTimeHr, WAEndTimeMin);
                                                btn_totime.setText(time);

                                            } else {
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
                                            Toast.makeText(ExcavationPermitActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ExcavationPermitActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(ExcavationPermitActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    } else if (WAStartTimeHr == selectedHour && selectedMinute >= WAStartTimemin) {
                                        if (selectedHour <= 13) {
                                            time1 = UpdateTime.updateTime((selectedHour + 9), selectedMinute);
                                            btn_totime.setText(time1);
                                        } else {
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

                                            Toast.makeText(ExcavationPermitActivity.this, msg, Toast.LENGTH_SHORT).show();
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


/**88888888888888888888888888888888888888888888888888*/
     /*   btn_fromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int hour1 = hour + 7;
                int minute = mcurrentTime.get(Calendar.MINUTE);

                *//* int hour1 = hour+4;*//*

                TimePickerDialog mTimePicker;

                btn_fromtime.setText(UpdateTime.updateTime(hour, minute));
                btn_totime.setText(UpdateTime.updateTime(hour1, minute));
                *//*  edt_andto_time.setText(hour +4 + ":" + minute + " ");*//*

                mTimePicker = new TimePickerDialog(ExcavationPermitActivity.this,
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
                                    Toast.makeText(ExcavationPermitActivity.this, "You cannot select time before 9:00 AM", Toast.LENGTH_SHORT).show();
                                }


                              *//*  if (selectedHour >= 9) {
                                    to_time = UpdateTime.updateTime((selectedHour + 7), selectedMinute);
                                    btn_fromtime.setText(time);
                                    btn_totime.setText(to_time);
                                } else if (selectedHour < 9) {
                                    btn_fromtime.setText(UpdateTime.updateTime(mcurrentTime.get(Calendar.HOUR_OF_DAY), mcurrentTime.get(Calendar.MINUTE)));
                                    btn_totime.setText(UpdateTime.updateTime(mcurrentTime.get(Calendar.HOUR_OF_DAY) + 7, mcurrentTime.get(Calendar.MINUTE)));
                                    Toast.makeText(ExcavationPermitActivity.this, "You cannot select time before 9:00 AM", Toast.LENGTH_SHORT).show();
                                }*//*


                                // to_time = updateTime((selectedHour+4),selectedMinute);

                            }
                        }, hour, minute, false);// Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });*/

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
                datePickerDialog = new DatePickerDialog(ExcavationPermitActivity.this,
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
                                            btn_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ExcavationPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(ExcavationPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(ExcavationPermitActivity.this, "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);


                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

                //datePickerDialog.getDatePicker().setMaxDate(enddate);

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

                mTimePicker = new TimePickerDialog(ExcavationPermitActivity.this,
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


        btn_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                datePickerDialog = new DatePickerDialog(ExcavationPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_date1.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ExcavationPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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

        btn_date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                datePickerDialog = new DatePickerDialog(ExcavationPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_date2.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ExcavationPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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

        btn_date3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


                // Launch Date Picker Dialog
                datePickerDialog = new DatePickerDialog(ExcavationPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_date3.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ExcavationPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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
        btn_date4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                datePickerDialog = new DatePickerDialog(ExcavationPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_date4.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ExcavationPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);
            }
        });

        btn_date5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                datePickerDialog = new DatePickerDialog(ExcavationPermitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_date5.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                String trnselectDate = year + "-" + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + "-" + (month + 1)
                                                    + "-" + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ExcavationPermitActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + "-" + (monthOfYear + 1)
                                                    + "-" + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + "-" + (monthOfYear + 1)
                                                + "-" + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + "-" + (month + 1)
                                                + "-" + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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


        txt_authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Mode.equalsIgnoreCase("E") && !PermitStatus.equalsIgnoreCase("P")) {

                } else {
                    //  passworddialog();
                    //Tier 1
                    CategoryWiseAuthorizeName("level 1", "fromPWD");
                    cusDialog1.setVisibility(View.VISIBLE);
                    tempVal = "0";
                }


            }
        });

        txt_authorize1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authorize1dialog();
                if (Mode.equalsIgnoreCase("E") && !PermitStatus.equalsIgnoreCase("P")) {

                } else {
                    //Tier 1 and Tier 2
                    CategoryWiseAuthorizeName("level 2", "fromPWD");
                    cusDialog1.setVisibility(View.VISIBLE);
                    tempVal = "1";
                }
            }
        });

        txt_permit_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reasondialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 1", "fromReason");
                cusDialog2.setVisibility(View.VISIBLE);
                ReasonVal = "1";
            }
        });

        txt_spot_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Spotcheckdialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 1", "fromReason");
                cusDialog2.setVisibility(View.VISIBLE);
                ReasonVal = "2";
            }
        });


        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Spotcheckdialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 1", "fromReason");
                cusDialog2.setVisibility(View.VISIBLE);
                ReasonVal = "3";
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
                        edit_password_pass.setText("");
                        txt_authorize.setText("Select");
                    } else if (tempVal.equals("1")) {
                        spinner_authorize.setSelection(0);
                        edit_password_pass.setText("");
                        txt_authorize1.setText("Select");
                    }

                } else {
                    cusDialog1.setVisibility(View.VISIBLE);

                }

                // b.dismiss();

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

                    Toast toast = Toast.makeText(ExcavationPermitActivity.this, "Please enter authorized person and password", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(ExcavationPermitActivity.this, "Please select authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && (!authorize.equalsIgnoreCase("--Select--")
                        || !authorize.equalsIgnoreCase(""))) {
                    Toast toast = Toast.makeText(ExcavationPermitActivity.this, "Please enter valid password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_pass.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else {
                    if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                        new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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
                authorizedPersonAdapter = new AuthorizedPersonAdapter(ExcavationPermitActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);

            }

        }

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
                            AuthorizeId = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("1")) {
                            txt_authorize1.setText(name);
                            Authorize1Id = txt_authorizeArrayList.get(position).getAuthorizeid();
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
                        String name = txt_authorizeArrayList.get(position).getAuthorizename();
                        if (ReasonVal.equals("1")) {
                            txt_permit_closed.setText(name);
                            Permitclosed = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (ReasonVal.equals("2")) {
                            txt_spot_check.setText(name);
                            spotcheck = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (ReasonVal.equals("3")) {
                            txt_cancel.setText(name);
                            cancelId = txt_authorizeArrayList.get(position).getAuthorizeid();
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
                        txt_permit_closed.setText("Select");
                        edit_password_reason.setText("");
                        Permitclosed = "";
                    } else if (ReasonVal.equals("2")) {
                        spinner_permit_closed.setSelection(0);
                        txt_spot_check.setText("Select");
                        edit_password_reason.setText("");
                        spotcheck = "";
                    } else if (ReasonVal.equals("3")) {
                        txt_cancel.setText("Select");
                        spinner_permit_closed.setSelection(0);
                        edit_password_reason.setText("");
                        edit_reason.setText("");
                        cancelId = "";
                    }

                    if ((Permitclosed.equalsIgnoreCase("Select") || Permitclosed.equals(""))
                            && (spotcheck.equalsIgnoreCase("Select") || spotcheck.equals(""))) {
                        //btn_submit.setVisibility(View.GONE);
                    } else {
                        btn_submit.setVisibility(View.VISIBLE);
                    }


                } else {
                    cusDialog2.setVisibility(View.VISIBLE);

                }

                //  b.dismiss();
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

                    Toast toast = Toast.makeText(ExcavationPermitActivity.this, "Please enter authorized person,password and reason", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(ExcavationPermitActivity.this, "Please select authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(ExcavationPermitActivity.this, "Please valid password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (!Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(ExcavationPermitActivity.this, "Please enter reason", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    //password and reason blank
                    Toast toast = Toast.makeText(ExcavationPermitActivity.this, "Please valid password and reason", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    //password and permit
                    Toast toast = Toast.makeText(ExcavationPermitActivity.this, "Please valid password and authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (!Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    //reason and permit
                    Toast toast = Toast.makeText(ExcavationPermitActivity.this, "Please enter reason and authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else {
                    if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                        new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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
        });
*/
       /* spinner_contractor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(permit != null){
                    contractorId = contractorListActivityArrayList.get(position).getCustVendorMasterId();
                    WAH_No = contractorListActivityArrayList.get(position).getCustVendorCode();
                    }else {
                    contractorId = contractorListActivityArrayList.get(position).getCustVendorMasterId();
                    WAH_No = contractorListActivityArrayList.get(position).getCustVendorCode();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        spinner_prevention_plan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (WAArayList.size() > 0) {

                    WAH_No = WAArayList.get(position).getPermitNo();

                    if (WAH_No != "") {
                        if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                            showProgress();
                            //Location Get
                            new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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
                            Toast.makeText(ExcavationPermitActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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
                        if (contractorListActivityArrayList.get(i).getCustVendorMasterId().equals(contractorId)) {
                            contractorpos = i;
                            break;
                        }
                    }

                    if (contractorpos != -1) {
                        spinner_authorize2.setSelection(contractorpos);
                    } else {
                        spinner_authorize2.setSelection(0);
                    }

                    if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                        showProgress();
                        //Location Get
                        new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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
                        if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                            new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadWANo().execute(contractorId);
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    Toast.makeText(ExcavationPermitActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(ExcavationPermitActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_operation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                operationId = operationArrayList.get(position).getOperationMasterId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationId = LocationArraylist.get(position).getLocationMasterId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*spinner_authorize1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    }else {
                        authorize1 = authorizedPersonArrayList.get(position).getAuthorizeid();
                        if (authorize1.equals("--Select--")) {

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


        /*spinner_permit_closed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                            passworddialog();
                        }


                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ExcavationPermitActivity.this, Manifest.permission.CAMERA)
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

        radiogroup_method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Toast.makeText(getBaseContext(), value, Toast.LENGTH_SHORT).show();
                int selectedId = radiogroup_method.getCheckedRadioButtonId();
                RadioButton radiobtn = (RadioButton) findViewById(selectedId);

                String radvalue = radiobtn.getText().toString();
                if (radvalue.equalsIgnoreCase("Yes")) {
                    method_of_operation = "Y";
                } else {
                    method_of_operation = "N";
                }

            }
        });

        radiogroup_clrancecerti.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Toast.makeText(getBaseContext(), value, Toast.LENGTH_SHORT).show();
                int selectedId = radiogroup_clrancecerti.getCheckedRadioButtonId();
                RadioButton radiobtn = (RadioButton) findViewById(selectedId);

                String radvalue = radiobtn.getText().toString();
                if (radvalue.equalsIgnoreCase("Yes")) {
                    clearancecertival = "Y";
                } else {
                    clearancecertival = "N";
                }

            }
        });

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
                    authorizedPersonAdapter = new AuthorizedPersonAdapter(ExcavationPermitActivity.this, txt_authorizeArrayList);
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
            authorizedPersonAdapter = new AuthorizedPersonAdapter(ExcavationPermitActivity.this, txt_authorizeArrayList);
            spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            spinner_authorize.setAdapter(authorizedPersonAdapter);
        }
    }

    public void initview() {
        list_forceble_risk = findViewById(R.id.list_forceble_risk);
        list_precaution = findViewById(R.id.list_precaution);

        btn_fromdate = findViewById(R.id.btn_fromdate);
        btn_date1 = findViewById(R.id.btn_date1);
        btn_date2 = findViewById(R.id.btn_date2);
        btn_date3 = findViewById(R.id.btn_date3);
        btn_date4 = findViewById(R.id.edt_permit_date);
        btn_date5 = findViewById(R.id.edt_spot_date);
        btn_cancel_date = findViewById(R.id.edt_cancel_date);
        btn_submit = findViewById(R.id.btn_submit);
        list_goldenRules = findViewById(R.id.list_goldenRules);

        btn_fromtime = findViewById(R.id.btn_fromtime);
        btn_totime = findViewById(R.id.btn_totime);
        btn_totime.setEnabled(false);
        btn_totime.setTextColor(Color.parseColor("#000000"));


        ln_WAHNo = findViewById(R.id.ln_WAHNo);
        ln_station = findViewById(R.id.ln_station);
        ln_contractor = findViewById(R.id.ln_contractor);
        ln_DescOperation = findViewById(R.id.ln_DescOperation);
        ln_location = findViewById(R.id.ln_location);
        len_cancel_permit = findViewById(R.id.len_cancel_permit);


        txt_authorize = (TextView) findViewById(R.id.spinner_authorize);
        txt_authorize1 = (TextView) findViewById(R.id.spinner_authorize1);
        txt_permit_closed = (TextView) findViewById(R.id.txt_permit_closed);
        txt_spot_check = (TextView) findViewById(R.id.txt_spotcheck);
        txt_cancel = (TextView) findViewById(R.id.txt_cancel);
        txt_cancel_permit = (TextView) findViewById(R.id.txt_cancel_permit);

        //spinner_authorize = findViewById(R.id.spinner_authorize);
        //spinner_authorize1 = findViewById(R.id.spinner_authorize1);
        spinner_authorize2 = findViewById(R.id.spinner_authorize2);
        spinner_authorize2.setEnabled(false);
        //spinner_permit_closed = findViewById(R.id.spinner_permit_closed);
        //spinner_spotcheck = findViewById(R.id.spinner_spotcheck);
        edit_permitno = findViewById(R.id.edit_permitno);
        spinner_station = findViewById(R.id.spinner_station);
        spinner_contractor = findViewById(R.id.spinner_contractor);
        grid_safety = findViewById(R.id.grid_safety);
        img_camera = findViewById(R.id.img_camera);
        btn_clrancedate = (Button) findViewById(R.id.btn_clrancedate);
        edit_clranceno = (EditText) findViewById(R.id.edit_clranceno);

        spinner_prevention_plan = findViewById(R.id.spinner_prevention_plan);


        spinner_operation = findViewById(R.id.spinner_operation);
        spinner_location = findViewById(R.id.spinner_location);
        edit_remarks = findViewById(R.id.edit_remarks);
        mprogress = findViewById(R.id.toolbar_progress_App_bar);
        len_foressable = findViewById(R.id.len_foressable);
        radiogroup_method = (RadioGroup) findViewById(R.id.radiogroup_method);
        radiogroup_clrancecerti = (RadioGroup) findViewById(R.id.radiogroup_clrancecerti);
        rd_clrno = (RadioButton) findViewById(R.id.rd_clrno);
        rd_clryes = (RadioButton) findViewById(R.id.rd_clryes);
        rd_oprno = (RadioButton) findViewById(R.id.rd_oprno);
        rd_opryes = (RadioButton) findViewById(R.id.rd_opryes);

        cusDialog1 = findViewById(R.id.cusDialog1);
        edit_password_pass = findViewById(R.id.edt_password_pass);
        ln_spinner_authorize = findViewById(R.id.ln_spinner_authorize);
        ln_spinner_reason = findViewById(R.id.ln_spinner_reason);
        spinner_authorize = findViewById(R.id.spinner_authorize_pas);
        btn_cancel_pass = findViewById(R.id.txt_cancel_pass);
        btn_submit_pass = findViewById(R.id.txt_submit_pass);

        cusDialog2 = findViewById(R.id.cusDialog2);
        edit_password_reason = findViewById(R.id.edt_password_reason);
        edit_reason = findViewById(R.id.edt_reason);
        btn_cancel_reason = findViewById(R.id.txt_cancel_reason);
        btn_submit_reason = findViewById(R.id.txt_submit_reason);
        spinner_permit_closed = findViewById(R.id.spinner_permit_closed1);
        len_p_closed = findViewById(R.id.len_p_closed);
        tx_p_closed = findViewById(R.id.tx_p_closed);
        spinner_location.setEnabled(false);
        spinner_operation.setEnabled(false);

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

    public void reasonDialog_SafetyTools(final int position, String SafetyToolMasterId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ExcavationPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater().from(ExcavationPermitActivity.this);
        final View dialogView = inflater.inflate(R.layout.remarks, null);
        builder.setView(dialogView);
        final EditText edit_remark = dialogView.findViewById(R.id.edit_remarks);
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);


        builder.setCancelable(false);
        final AlertDialog b = builder.create();
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
            progressDialog = new ProgressDialog(ExcavationPermitActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setTitle("Data fetching...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_getHWDetails + "?form=" + PermitNo;

            try {
                res = CommonClass.OpenConnection(url, ExcavationPermitActivity.this);
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
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExcavationPermitActivity.this);

                            // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            list_goldenRules.setLayoutManager(linearLayoutManager);
                            goldenRuleAdapter = new GoldenRuleAdapter();
                            goldenRuleAdapter.updateList(goldenRulesArrayList, Mode, PermitStatus);
                            list_goldenRules.setAdapter(goldenRuleAdapter);


                        } else {
                            if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                                new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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

                        //WAH_No = jorder.getString("PreventionPlanRef");
                     /*   int WAHNo = -1;

                        WAH_No = jorder.getString("PreventionPlanRef");
                        for (int z = 0; z < WAArayList.size(); z++) {
                            if (WAH_No.equals(WAArayList.get(z).getPermitNo())) {
                                WAHNo = z;
                            }
                        }

                        if (WAHNo != -1) {
                            spinner_prevention_plan.setSelection(WAHNo);
                        } else {
                            spinner_prevention_plan.setSelection(0);
                        }*/
                        int WAHNo = -1;

                        WAH_No = jorder.getString("PreventionPlanRef");
                        if (WAArayList.size() != 0) {
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
                        } else {
                            if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                                new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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


                        StationId = jorder.getString("FkWareHouseMasterId");
                        StationName = jorder.getString("WarehouseDescription");
                        int statiopos = -1;
                        for (int j = 0; j < depotArrayList.size(); j++) {
                            if (depotArrayList.get(j).getDepotid().equals(StationId)) {

                                statiopos = j;
                            }

                        }


                        if (statiopos != -1)
                            spinner_station.setSelection(statiopos);
                        else
                            spinner_station.setSelection(0);

                        locationId = jorder.getString("FkLocationMasterId");

                        contractorId = jorder.getString("fkContractorId");
                        contractorName = jorder.getString("CustVendorName");
                        int contractorpos = -1;
                        for (int j = 0; j < contractorListActivityArrayList.size(); j++) {
                            if (contractorId.equals(contractorListActivityArrayList.get(j).getCustVendorMasterId())) {
                                contractorpos = j;
                            }
                        }
                        if (contractorpos != -1) {
                            spinner_contractor.setSelection(contractorpos);
                            spinner_authorize2.setSelection(contractorpos);
                        } else {
                            spinner_contractor.setSelection(0);
                            spinner_authorize2.setSelection(0);
                        }


                        operationId = jorder.getString("fkOperationMasterId");
                        OperationName = jorder.getString("Operation");
                        int operationpos = -1;
                        for (int j = 0; j < operationArrayList.size(); j++) {
                            operationpos = j;
                        }
                        if (operationpos != -1)
                            spinner_operation.setSelection(operationpos);
                        else
                            spinner_operation.setSelection(0);



/*
                        locationId = jorder.getString("FkLocationMasterId");
                        locationName = jorder.getString("LocationDesc");
                        int locationpos=-1;
                        for(int j=0 ; j<LocationArraylist.size() ; j++){
                            if(LocationArraylist.get(j).getLocationMasterId().equals(locationId))
                                locationpos = j;
                        }
                        if(locationpos != -1)
                            spinner_operation.setSelection(locationpos);
                        else
                            spinner_operation.setSelection(0);*/


                        StartDate = jorder.getString("PermitDate");
                        btn_fromdate.setText(StartDate);

                       /* fromtime = jorder.getString("PermitFromTime");
                        btn_fromtime.setText(fromtime);

                        totime = jorder.getString("PermitToTime");
                        btn_totime.setText(totime);*/

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

                        clearancecertival = jorder.getString("permitclearancecode");
                        if (clearancecertival.contains("Y")) {
                            rd_clryes.setChecked(true);
                            rd_clrno.setChecked(false);
                        } else if (clearancecertival.contains("N")) {
                            rd_clryes.setChecked(false);
                            rd_clrno.setChecked(true);
                        } else {
                            rd_clryes.setChecked(false);
                            rd_clrno.setChecked(false);
                        }

                        clearancedate = jorder.getString("permitclearancedate");
                        btn_clrancedate.setText(clearancedate);

                        clearanceno = jorder.getString("permitclearanceno");
                        if (!clearanceno.equalsIgnoreCase(""))
                            edit_clranceno.setText(clearanceno);
                        else
                            edit_clranceno.setText("");


                        safetytools = jorder.getString("SafetyToolMasterId");/* "chetana,sayali,suyog,vritti,pradnya"*/
                        ;
                        String[] safetytoolslist = new String[safetytools.length()];
                        safetytoolslist = safetytools.split(",");
                        for (int j = 0; j < safetyToolsArrayList.size(); j++) {
                            for (int k = 0; k < safetytoolslist.length; k++) {
                                if (safetytoolslist[k].equals(safetyToolsArrayList.get(j).getSafetyToolMasterId())) {
                                    int pos = j;
                                    safetyToolsArrayList.get(pos).setSelected(true);
                                    safetyAdapter = new SafetyAdapter(ExcavationPermitActivity.this, safetyToolsArrayList, "EP", Mode, PermitStatus);
                                    grid_safety.setAdapter(safetyAdapter);
                                }

                            }
                        }


                        method_of_operation = jorder.getString("MethodOperationStatus");
                        if (method_of_operation.contains("Y")) {
                            rd_oprno.setChecked(false);
                            rd_opryes.setChecked(true);
                        } else if (method_of_operation.contains("N")) {
                            rd_opryes.setChecked(false);
                            rd_oprno.setChecked(true);
                        } else {
                            rd_oprno.setChecked(false);
                            rd_opryes.setChecked(true);
                        }

                        AuthorizeId = jorder.getString("AuthorizeBy1");
                        int pos1 = -1;
                        if (AuthorizeId.equalsIgnoreCase("")) {

                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (AuthorizeId.equalsIgnoreCase(authorizedPersonArrayList.get(j).getAuthorizeid()))
                                    pos1 = j;
                            }
                        }

                        if (pos1 != -1)
                            txt_authorize.setText(authorizedPersonArrayList.get(pos1).getAuthorizename());
                        else
                            txt_authorize.setText("Select");

                        Authorize1Id = jorder.getString("AuthorizeBy2");
                        int pos2 = -1;
                        if (Authorize1Id.equalsIgnoreCase("")) {

                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (Authorize1Id.equalsIgnoreCase(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                    pos2 = j;
                                }
                            }
                        }
                        if (pos2 != -1)
                            txt_authorize1.setText(authorizedPersonArrayList.get(pos2).getAuthorizename());
                        else
                            txt_authorize1.setText("Select");


                        Permitclosed = jorder.getString("PermitCloseBy");
                        int pos4 = -1;
                        if (Permitclosed.equalsIgnoreCase("")) {

                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (Permitclosed.equalsIgnoreCase(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                    pos4 = j;
                                }
                            }
                        }
                        if (pos4 != -1)
                            txt_permit_closed.setText(authorizedPersonArrayList.get(pos4).getAuthorizename());
                        else
                            txt_permit_closed.setText("Select");

                        PermitclosedDate = jorder.getString("PermitCloseDate");
                        btn_date4.setText(PermitclosedDate);

                        SpotCheckDate = jorder.getString("SpotCheckDate");
                        btn_date5.setText(SpotCheckDate);


                        spotcheck = jorder.getString("SpotCheckBy");
                        int pos5 = -1;
                        if (spotcheck.equalsIgnoreCase("")) {

                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (spotcheck.equalsIgnoreCase(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                    pos5 = j;
                                }
                            }
                        }
                        if (pos5 != -1)
                            txt_spot_check.setText(authorizedPersonArrayList.get(pos5).getAuthorizename());
                        else
                            txt_spot_check.setText("Select");

                      /*  if(Permitclosed.equals("") || spotcheck.equals("")){
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
                        if (cancelDate.equals("")) {
                            btn_cancel_date.setText(date);
                        } else {
                            btn_cancel_date.setText(cancelDate);
                        }

                        Remarks = jorder.getString("PermitCloseRemark");
                        if (!Remarks.equalsIgnoreCase(""))
                            edit_remarks.setText(Remarks);
                        else
                            edit_remarks.setText("");


                        /*indicateRiskArrayList,equipmentUseArrayList*/

                        foressablersk_Others = jorder.getString("EP_ForceOthers");
                        precaution_Others = jorder.getString("EP_PreOthers");
                        safety_Others = jorder.getString("EP_ProOthers");


                        if (indicateRiskArrayList != null) {
                            for (int j = 0; j < indicateRiskArrayList.size(); j++) {
                                if (indicateRiskArrayList.get(j).getSelectionText().contains("Other")) {
                                    indicateRiskArrayList.get(j).setRemarks(foressablersk_Others);
                                    indicateRiskArrayList.get(j).isSelected();
                                }
                            }
                        }

                        if (equipmentUseArrayList != null) {
                            for (int j = 0; j < equipmentUseArrayList.size(); j++) {
                                if (equipmentUseArrayList.get(j).getSelectionText().equalsIgnoreCase("Other")) {
                                    equipmentUseArrayList.get(j).setRemarks(foressablersk_Others);
                                    equipmentUseArrayList.get(j).isSelected();
                                }
                            }
                        }

                        if (safetyToolsArrayList != null) {
                            for (int j = 0; j < safetyToolsArrayList.size(); j++) {
                                if (safetyToolsArrayList.get(j).getSafetyToolDesc().equals("Other(s)")) {
                                    safetyToolsArrayList.get(j).setRemarks(safety_Others);
                                    safetyToolsArrayList.get(j).isSelected();
                                }
                            }
                        }

                        foressablerisk = jorder.getString("foressableriskMasterId");
                        if (foressablerisk.equalsIgnoreCase("")) {

                        } else {
                            //len_foressable.setVisibility(View.VISIBLE);
                            String[] foressablerisk1 = new String[foressablerisk.length()];
                            foressablerisk1 = foressablerisk.split(",");
                            for (int j = 0; j < indicateRiskArrayList.size(); j++) {
                                for (int k = 0; k < foressablerisk1.length; k++) {
                                    if (foressablerisk1[k].equalsIgnoreCase(indicateRiskArrayList.get(j).getPKQuesID())) {
                                        int pos = j;
                                        indicateRiskArrayList.get(pos).setSelected(true);
                                        indicateRiskArrayList.get(pos).setRemarks(foressablersk_Others);
                                        indicateRiskAdapter = new IndicateRiskAdapterNew(ExcavationPermitActivity.this, indicateRiskArrayList, Mode, PermitStatus);
                                        list_forceble_risk.setAdapter(indicateRiskAdapter);


                                    }
                                }
                            }
                        }
                        precautionuser = jorder.getString("PrecautionriskMasterId");
                        String[] precautionuser1 = new String[precautionuser.length()];
                        precautionuser1 = precautionuser.split(",");
                        for (int j = 0; j < equipmentUseArrayList.size(); j++) {
                            for (int k = 0; k < precautionuser1.length; k++) {
                                if (precautionuser1[k].equalsIgnoreCase(equipmentUseArrayList.get(j).getPKQuesID())) {
                                    int pos = j;
                                    equipmentUseArrayList.get(pos).setSelected(true);
                                    equipmentUseArrayList.get(pos).setRemarks(precaution_Others);
                                    equipmentAdapter = new EquipmentAdapterNew(ExcavationPermitActivity.this, equipmentUseArrayList, Mode, PermitStatus);
                                    list_precaution.setAdapter(equipmentAdapter);
                                }
                            }
                        }


                        /*"EP_ForceOthers": "jitya",
                                "EP_PreOthers": "Nithya matimanda",
                                "EP_ProOthers": "done with testing",*/

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
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

                if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
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

        /*mprogress.setVisibility(View.VISIBLE);
        LayoutInflater inflater = getLayoutInflater();
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
        toast.show();
*/

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
                res = CommonClass.OpenConnection(url, ExcavationPermitActivity.this);
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
                // Toast.makeText(ExcavationPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(authorizedPersonArrayList);
                editor.putString("Authorized", json);
                editor.commit();
                authorizedPersonAdapter = new AuthorizedPersonAdapter(ExcavationPermitActivity.this, authorizedPersonArrayList);
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
                res = CommonClass.OpenConnection(url, ExcavationPermitActivity.this);
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
                // Toast.makeText(ExcavationPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(contractorListActivityArrayList);
                editor.putString("Contractor", json);
                editor.commit();
                permitContractorListAdapter = new PermitContractorListAdapter(ExcavationPermitActivity.this, contractorListActivityArrayList);
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


                if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                    showProgress();
                    new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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

                if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                    showProgress();
                    new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                    showProgress();
                    new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                    showProgress();
                    new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                    showProgress();
                    new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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

                if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                    showProgress();
                    new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadExcavationData().execute();
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
            url = CompanyURL + WebAPIUrl.api_GetEPNo;

            res = CommonClass.OpenConnection(url, ExcavationPermitActivity.this);

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
                res = CommonClass.OpenConnection(url, ExcavationPermitActivity.this);
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
                // Toast.makeText(ExcavationPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(depotArrayList);
                editor.putString("Depot", json);
                editor.commit();
                depotAdapter = new DepotAdapter(ExcavationPermitActivity.this, depotArrayList);
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
                res = CommonClass.OpenConnection(url, ExcavationPermitActivity.this);
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
                //  Toast.makeText(ExcavationPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(operationArrayList);
                editor.putString("Operation", json);
                editor.commit();
                operationAdapter = new OperationAdapter(ExcavationPermitActivity.this, operationArrayList);
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
                res = CommonClass.OpenConnection(url, ExcavationPermitActivity.this);
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
                locationOperationAdapter = new LocationOperationAdapter(ExcavationPermitActivity.this, LocationArraylist);
                spinner_location.setAdapter(locationOperationAdapter);
                //Toast.makeText(ExcavationPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                locationOperationAdapter = new LocationOperationAdapter(ExcavationPermitActivity.this, LocationArraylist);
                spinner_location.setAdapter(locationOperationAdapter);
//                if(cnt == -1){
                // if (Mode.equalsIgnoreCasef("E")) {
                if (locationId != "") {
                    int locationpos = -1;
                    for (int j = 0; j < LocationArraylist.size(); j++) {
                        if (LocationArraylist.get(j).getLocationMasterId().equals(locationId)) {
                            locationpos = j;
                            break;
                        }
                    }
                    if (locationpos != -1)
                        spinner_location.setSelection(locationpos);
                    else
                        spinner_location.setSelection(0);
                }

                // }


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
                    ExcavationPermitActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dismissProgress();
                            Toast.makeText(ExcavationPermitActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();

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
                        ExcavationPermitActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dismissProgress();
                                Toast.makeText(ExcavationPermitActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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

    class DownloadExcavationData extends AsyncTask<String, Void, String> {
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
                res = CommonClass.OpenConnection(url, ExcavationPermitActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //
                    //
                    // response = response.substring(1, response.length() - 1);

                    indicateRiskArrayList = new ArrayList<>();
                    equipmentUseArrayList = new ArrayList<>();

                    indicateRiskArrayList.clear();
                    equipmentUseArrayList.clear();


                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        Prevention prevention = new Prevention();
                        EquipmentUse equipmentUse = new EquipmentUse();
                        IndicateRisk indicateRisk = new IndicateRisk();
                        String QuesCode = jorder.getString("QuesCode");

                        if (QuesCode.equalsIgnoreCase("FR")) {
                            indicateRisk.setPKQuesID(jorder.getString("PKQuesID"));
                            indicateRisk.setQuesText(jorder.getString("QuesText"));
                            indicateRisk.setSelectionText(jorder.getString("SelectionText"));
                            indicateRisk.setSelectionValue(jorder.getString("SelectionValue"));
                            indicateRisk.setQuesCode(jorder.getString("QuesCode"));

                            indicateRiskArrayList.add(indicateRisk);

                        }
                        if (QuesCode.equalsIgnoreCase("PTT")) {
                            equipmentUse.setPKQuesID(jorder.getString("PKQuesID"));
                            equipmentUse.setQuesText(jorder.getString("QuesText"));
                            equipmentUse.setSelectionText(jorder.getString("SelectionText"));
                            equipmentUse.setSelectionValue(jorder.getString("SelectionValue"));
                            equipmentUse.setQuesCode(jorder.getString("QuesCode"));
                            equipmentUseArrayList.add(equipmentUse);


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

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {
                dismissProgress();
                // Toast.makeText(ExcavationPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                gson = new Gson();

                String json = gson.toJson(indicateRiskArrayList);
                String json1 = gson.toJson(equipmentUseArrayList);
                editor.putString("foressable", json);
                editor.putString("precaution", json1);
                editor.commit();
                indicateRiskAdapter = new IndicateRiskAdapterNew(ExcavationPermitActivity.this, indicateRiskArrayList, Mode, PermitStatus);
                list_forceble_risk.setAdapter(indicateRiskAdapter);
                // Utility.setListViewHeightBasedOnItems(list_forceble_risk);

                equipmentAdapter = new EquipmentAdapterNew(ExcavationPermitActivity.this, equipmentUseArrayList, Mode, PermitStatus);
                list_precaution.setAdapter(equipmentAdapter);
                //Utility.setListViewHeightBasedOnItems(list_precaution);


                len_foressable.setVisibility(View.VISIBLE);
            }


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
                res = CommonClass.OpenConnection(url, ExcavationPermitActivity.this);
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
                //  Toast.makeText(ExcavationPermitActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(safetyToolsArrayList);
                editor.putString("safety", json);
                editor.commit();
                safetyAdapter = new SafetyAdapter(ExcavationPermitActivity.this, safetyToolsArrayList, "EP", Mode, PermitStatus);
                grid_safety.setAdapter(safetyAdapter);


            }


        }
    }

    public void saveactivityjson() {


        ActivityJson = new JSONObject();


        StartDate = btn_fromdate.getText().toString();
        fromtime = btn_fromtime.getText().toString();
        totime = btn_totime.getText().toString();
        PermitclosedDate = btn_date4.getText().toString();
        SpotCheckDate = btn_date5.getText().toString();
        Remarks = edit_remarks.getText().toString();
        clearancedate = btn_clrancedate.getText().toString();
        clearanceno = edit_clranceno.getText().toString();
        AuthorizeDate1 = btn_date1.getText().toString();
        AuthorizeDate2 = btn_date2.getText().toString();
        AuthorizeDate3 = btn_date3.getText().toString();


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
//foressable risk checkbox
        indicateRiskArrayList = indicateRiskAdapter.getArrayList();

        if (indicateRiskArrayList.size() > 0) {
            if (indicateRiskArrayList.size() > 0) {
                foressableuser = new String[indicateRiskArrayList.size()];
                for (int i = 0; i < indicateRiskArrayList.size(); i++) {
                    String foressablersk = indicateRiskArrayList.get(i).getPKQuesID();
                    foressableuser[i] = foressablersk.toString();
                    foressablerisk = TextUtils.join(",", foressableuser);
                    if (indicateRiskArrayList.get(i).getSelectionText().equalsIgnoreCase("Other(s) – Specify") ||
                            indicateRiskArrayList.get(i).getSelectionText().contains("Other")) {
                        if (indicateRiskArrayList.get(i).getRemarks() != null) {
                            foressablersk_Others = indicateRiskArrayList.get(i).getRemarks();
                        }
                    }


                }

            }
        }

        //precaution
        equipmentUseArrayList = equipmentAdapter.getArrayList();

        if (equipmentUseArrayList.size() > 0) {
            if (equipmentUseArrayList.size() > 0) {
                precautionusr = new String[equipmentUseArrayList.size()];
                for (int i = 0; i < equipmentUseArrayList.size(); i++) {
                    String precautnuser = equipmentUseArrayList.get(i).getPKQuesID();
                    precautionusr[i] = precautnuser.toString();
                    precautionuser = TextUtils.join(",", precautionusr);
                    if (equipmentUseArrayList.get(i).getSelectionText().equalsIgnoreCase("Other(s)") ||
                            equipmentUseArrayList.get(i).getSelectionText().equalsIgnoreCase("Other(s) –Specify") ||
                            equipmentUseArrayList.get(i).getSelectionText().equalsIgnoreCase("Other")) {
                        if (equipmentUseArrayList.get(i).getRemarks() != null) {
                            precaution_Others = equipmentUseArrayList.get(i).getRemarks();
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
            ActivityJson.put("PreventionPlanRef", WAH_No);
            //  ActivityJson.put("PreventionPlanRef",preventionplan);
            ActivityJson.put("PermitDate", StartDate);
            ActivityJson.put("PermitFromTime", fromtime);
            ActivityJson.put("PermitToTime", totime);
            ActivityJson.put("fkContractorId", contractorId);
            ActivityJson.put("fkOperationMasterId", operationId);
            ActivityJson.put("FkLocationMasterId", locationId);
            ActivityJson.put("SafetyToolMasterId", safetytools);
            ActivityJson.put("permitclearancecode", clearancecertival);
            ActivityJson.put("permitclearancedate", clearancedate);
            if (rd_clryes.isChecked()) {
                ActivityJson.put("permitclearanceno", clearanceno);
            } else {
                ActivityJson.put("permitclearanceno", "");
            }
            ActivityJson.put("foressableriskMasterId", foressablerisk);
            ActivityJson.put("PrecautionriskMasterId", precautionuser);
            ActivityJson.put("MethodOperationCode", method_of_operation);
            ActivityJson.put("AuthorizeBy1", AuthorizeId);
            ActivityJson.put("AuthorizeBy2", Authorize1Id);
            ActivityJson.put("RespContractorId", contractorId);
            ActivityJson.put("PermitCloseBy", Permitclosed);
            ActivityJson.put("PermitCloseDate", PermitclosedDate);
            ActivityJson.put("SpotCheckBy", spotcheck);
            ActivityJson.put("SpotCheckDate", SpotCheckDate);
            ActivityJson.put("SpotImage", "");
            ActivityJson.put("PermitCancelBy", cancelId);
            ActivityJson.put("PermitCancelDate", cancelDate);
            /*ActivityJson.put("",AuthorizeDate1);
            ActivityJson.put("",AuthorizeDate2);
            ActivityJson.put("",AuthorizeDate3);*/
            ActivityJson.put("PermitCloseRemark", Remarks);
            ActivityJson.put("EP_ForceOthers", foressablersk_Others);
            ActivityJson.put("EP_PreOthers", precaution_Others);
            ActivityJson.put("EP_ProOthers", safety_Others);
            ActivityJson.put("GoldenRulesId", goldenRulesList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String FinalJsonObject = ActivityJson.toString();
        //String URL = CompanyURL+ WebAPIUrl.api_PostExcavationPermit;
        //  String op = "Success";

        if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
            showProgress();
            new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
                @Override

                public void callMethod() {
                    new DownloadPostData().execute(FinalJsonObject);
                }

                @Override
                public void callfailMethod(String msg) {
                    CommonClass.displayToast(ExcavationPermitActivity.this, msg);
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
        PermitclosedDate = btn_date4.getText().toString();
        SpotCheckDate = btn_date5.getText().toString();
        cancelDate = btn_cancel_date.getText().toString();
        Remarks = edit_remarks.getText().toString();
        clearancedate = btn_clrancedate.getText().toString();
        clearanceno = edit_clranceno.getText().toString();
        AuthorizeDate1 = btn_date1.getText().toString();
        AuthorizeDate2 = btn_date2.getText().toString();
        AuthorizeDate3 = btn_date3.getText().toString();


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
//foressable risk checkbox
        indicateRiskArrayList = indicateRiskAdapter.getArrayList();

        if (indicateRiskArrayList.size() > 0) {
            if (indicateRiskArrayList.size() > 0) {
                foressableuser = new String[indicateRiskArrayList.size()];
                for (int i = 0; i < indicateRiskArrayList.size(); i++) {
                    String foressablersk = indicateRiskArrayList.get(i).getPKQuesID();
                    foressableuser[i] = foressablersk.toString();
                    foressablerisk = TextUtils.join(",", foressableuser);
                    if (indicateRiskArrayList.get(i).getSelectionText().equalsIgnoreCase("Other(s) – Specify") ||
                            indicateRiskArrayList.get(i).getSelectionText().contains("Other")) {
                        if (indicateRiskArrayList.get(i).getRemarks() != null) {
                            foressablersk_Others = indicateRiskArrayList.get(i).getRemarks();
                        }
                    }


                }

            }
        }

        //precaution
        equipmentUseArrayList = equipmentAdapter.getArrayList();

        if (equipmentUseArrayList.size() > 0) {
            if (equipmentUseArrayList.size() > 0) {
                precautionusr = new String[equipmentUseArrayList.size()];
                for (int i = 0; i < equipmentUseArrayList.size(); i++) {
                    String precautnuser = equipmentUseArrayList.get(i).getPKQuesID();
                    precautionusr[i] = precautnuser.toString();
                    precautionuser = TextUtils.join(",", precautionusr);
                    if (equipmentUseArrayList.get(i).getSelectionText().equalsIgnoreCase("Other(s)") ||
                            equipmentUseArrayList.get(i).getSelectionText().equalsIgnoreCase("Other(s) –Specify")) {
                        if (equipmentUseArrayList.get(i).getRemarks() != null) {
                            precaution_Others = equipmentUseArrayList.get(i).getRemarks();
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
            ActivityJson.put("PreventionPlanRef", WAH_No);
            //  ActivityJson.put("PreventionPlanRef",preventionplan);
            ActivityJson.put("PermitDate", StartDate);
            ActivityJson.put("PermitFromTime", fromtime);
            ActivityJson.put("PermitToTime", totime);
            ActivityJson.put("fkContractorId", contractorId);
            ActivityJson.put("fkOperationMasterId", operationId);
            ActivityJson.put("FkLocationMasterId", locationId);
            ActivityJson.put("SafetyToolMasterId", safetytools);
            ActivityJson.put("permitclearancecode", clearancecertival);
            ActivityJson.put("permitclearancedate", clearancedate);
            if (rd_clryes.isChecked()) {
                ActivityJson.put("permitclearanceno", clearanceno);
            } else {
                ActivityJson.put("permitclearanceno", "");
            }
           // ActivityJson.put("permitclearanceno", clearanceno);
            ActivityJson.put("foressableriskMasterId", foressablerisk);
            ActivityJson.put("PrecautionriskMasterId", precautionuser);
            ActivityJson.put("MethodOperationCode", method_of_operation);
            ActivityJson.put("AuthorizeBy1", AuthorizeId);
            ActivityJson.put("AuthorizeBy2", Authorize1Id);
            ActivityJson.put("RespContractorId", contractorId);
            ActivityJson.put("PermitCloseBy", Permitclosed);
            ActivityJson.put("PermitCloseDate", PermitclosedDate);
            ActivityJson.put("SpotCheckBy", spotcheck);
            ActivityJson.put("SpotCheckDate", SpotCheckDate);
            ActivityJson.put("PermitCancelBy", cancelId);
            ActivityJson.put("PermitCancelDate", cancelDate);
            /*ActivityJson.put("",AuthorizeDate1);
            ActivityJson.put("",AuthorizeDate2);
            ActivityJson.put("",AuthorizeDate3);*/
            ActivityJson.put("EP_ForceOthers", foressablersk_Others);
            ActivityJson.put("EP_PreOthers", precaution_Others);
            ActivityJson.put("EP_ProOthers", safety_Others);
            ActivityJson.put("PermitCloseRemark", Remarks);
            ActivityJson.put("GoldenRulesId", goldenRulesList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String FinalEditJsonObject = ActivityJson.toString();
        //String URL = CompanyURL+ WebAPIUrl.api_PostExcavationPermit;
        //  String op = "Success";

        if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
            showProgress();
            new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
                @Override

                public void callMethod() {
                    new DownloadEditPostData().execute(FinalEditJsonObject);
                }

                @Override
                public void callfailMethod(String msg) {
                    CommonClass.displayToast(ExcavationPermitActivity.this, msg);
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
                objFinalObj = ActivityJson.toString().replaceAll("\\\\", "");
                String url = CompanyURL + WebAPIUrl.api_PostExcavationPermit;
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
                Toast.makeText(ExcavationPermitActivity.this, "Data save successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ExcavationPermitActivity.this, SupervisorMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                //Intent intent = new Intent(AssignActivity.this, ActivityMain.class);
                //startActivity(intent);
            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {
                Toast.makeText(ExcavationPermitActivity.this, "Data not save sucessfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ExcavationPermitActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
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
                objFinalObj = ActivityJson.toString().replaceAll("\\\\", "");
                String url = CompanyURL + WebAPIUrl.api_PosteditExcavation;
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
                Toast.makeText(ExcavationPermitActivity.this, "Data Updated Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ExcavationPermitActivity.this, PermitStatusActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                //Intent intent = new Intent(AssignActivity.this, ActivityMain.class);
                //startActivity(intent);
            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {
                Toast.makeText(ExcavationPermitActivity.this, "Unable to Update Data ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ExcavationPermitActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
            }
        }
    }


    class DownloadIsValidUser extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ExcavationPermitActivity.this);
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
                    //    URLEncoder.encode("z207", "UTF-8") + "&PlantId=" +
                        URLEncoder.encode("ail", "UTF-8") + "&PlantId=" +
                        URLEncoder.encode("1", "UTF-8") + "&UserLoginId=" +
                        URLEncoder.encode(id, "UTF-8") + "&UserPwd=" +
                        URLEncoder.encode(Password, "UTF-8");
                res = CommonClass.OpenConnection(url, ExcavationPermitActivity.this);
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
                cusDialog1.setVisibility(View.GONE);
                spinner_authorize.setSelection(0);
                edit_password_pass.setText("");
                cusDialog2.setVisibility(View.GONE);
                spinner_permit_closed.setSelection(0);
                edit_password_reason.setText("");
                edit_reason.setText("");

                /*if(ReasonVal.equals("1") || ReasonVal.equals("2") && Mode.equals("E")){
                    btn_submit.setVisibility(View.VISIBLE);
                }*/
                hideKeyboard(ExcavationPermitActivity.this);
                // b.dismiss();
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


    /*************************************NOT USED**********************************************/

    private void passworddialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ExcavationPermitActivity.this);
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
                spinner_authorize.setSelection(0);
                txt_authorize.setText("");
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
                    if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                        new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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
                authorizedPersonAdapter = new AuthorizedPersonAdapter(ExcavationPermitActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);

            }

        }
/*
        spinner_authorize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });*/


    }

    private void authorize1dialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ExcavationPermitActivity.this);
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
                    if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                        new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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
                authorizedPersonAdapter = new AuthorizedPersonAdapter(ExcavationPermitActivity.this, authorizedPersonArrayList);
                spinner_authorize1.setAdapter(authorizedPersonAdapter);


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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ExcavationPermitActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.reason_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);
        final EditText edit_password = dialogView.findViewById(R.id.edt_password);
        final EditText edit_reason = dialogView.findViewById(R.id.edt_reason);
        spinner_permit_closed = dialogView.findViewById(R.id.spinner_permit_closed);
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

                Password = edit_password.getText().toString();
                String Reason = edit_reason.getText().toString();
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);

                if (Password.equalsIgnoreCase("") && Reason.equalsIgnoreCase("")) {

                    text.setText("Please enter Password and Reason");
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
                    if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                        new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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
                authorizedPersonAdapter = new AuthorizedPersonAdapter(ExcavationPermitActivity.this, authorizedPersonArrayList);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);

            }

        }

        spinner_permit_closed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                    if (authorizedPersonArrayList == null) {

                    } else {
                        PermitClosed = authorizedPersonArrayList.get(position).getAuthorizeid();
                        String name = authorizedPersonArrayList.get(position).getAuthorizename();
                        txt_permit_closed.setText(name);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void Spotcheckdialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ExcavationPermitActivity.this);
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
                txt_spot_check.setText("");
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

                    if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                        new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                showProgress();
                new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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
                authorizedPersonAdapter = new AuthorizedPersonAdapter(ExcavationPermitActivity.this, authorizedPersonArrayList);
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
                        txt_spot_check.setText(name);


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public class DownloadWANo extends AsyncTask<String, Void, String> {
        String res = "", response = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String Id = params[0];

            String url = CompanyURL + WebAPIUrl.api_GetWANo + "?contractid=" + Id + "&permitOperationcode=EP";


            res = CommonClass.OpenConnection(url, ExcavationPermitActivity.this);
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
                WAArayList.add(0, permitNoWA);
                if (WAArayList.size() != 0) {
                    ContractorPermitAdapter contractorPermitAdapter = new ContractorPermitAdapter(ExcavationPermitActivity.this, WAArayList);
                    spinner_prevention_plan.setAdapter(contractorPermitAdapter);
                    //spinner_permit_closed.setAdapter(authorizedPersonAdapter);

                   /* ArrayAdapter<PermitNoWA> permitNoArrayAdapter = new ArrayAdapter<>(ExcavationPermitActivity.this,
                            android.R.layout.simple_spinner_item, WAArayList);
                    spinner_prevention_plan.setAdapter(permitNoArrayAdapter);*/

                    if (WAH_No != "") {
                        int wahNo = -1;
                        for (int j = 0; j < WAArayList.size(); j++) {
                            if (WAArayList.get(j).getPermitNo().equals(WAH_No)) {
                                wahNo = j;
                                break;
                            }
                        }
                        if (wahNo != -1)
                            spinner_prevention_plan.setSelection(wahNo);
                        else
                            spinner_prevention_plan.setSelection(0);
                    }

                    if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                        showProgress();
                        //Location Get
                        new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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
                        Toast.makeText(ExcavationPermitActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }


                } else {

                    // spinner_prevention_plan.setSelection(0);
                }


            } else {
                WAArayList.clear();
                if (Mode.equals("E")) {
                    if (WAH_No != "") {
                        PermitNoWA permitNo = new PermitNoWA();
                        permitNo.setPermitNo(WAH_No);
                        WAArayList.add(0, permitNo);
                        ArrayAdapter<PermitNoWA> permitNoArrayAdapter = new ArrayAdapter<>(ExcavationPermitActivity.this,
                                android.R.layout.simple_spinner_item, WAArayList);
                        spinner_prevention_plan.setAdapter(permitNoArrayAdapter);
                    }
                } else {
                    Toast.makeText(ExcavationPermitActivity.this, "No WorkAuthorization Present Against Selected Contractor", Toast.LENGTH_SHORT).show();
                    PermitNoWA permitNo = new PermitNoWA();
                    permitNo.setPermitNo("Select");
                    WAArayList.add(0, permitNo);
                    ContractorPermitAdapter contractorPermitAdapter = new ContractorPermitAdapter(ExcavationPermitActivity.this, WAArayList);
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


            res = CommonClass.OpenConnection(url, ExcavationPermitActivity.this);
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

                        WAStartTime = jorder.getString("FromTime1");
                        WAEndTime1 = jorder.getString("ToTime2");
                        WAEndTime = jorder.getString("ToTime2");
                        if (modeefirst == -1) {
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
                                            Toast.makeText(ExcavationPermitActivity.this, "You cannot choose time greater than work authorization end time", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                            btn_totime.setText(WAEndTime);
                            modeefirst = -1;
                        }
                        operationId = jorder.getString("fkOperationMasterId");

                        int operationpos = -1;
                        if (operationArrayList != null) {
                            for (int j = 0; j < operationArrayList.size(); j++) {
                                if (operationArrayList.get(j).getOperationMasterId().equals(operationId)) {
                                    operationpos = j;
                                    break;
                                }
                            }

                            if (operationpos != -1)
                                spinner_operation.setSelection(operationpos);
                            else
                                spinner_operation.setSelection(0);
                        }


                       /* StationId = jorder.getString("FkWareHouseMasterId");
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
                            locationId = jorder.getString("FkLocationMasterId");
                            //Mode = "E";

                            int locationpos = -1;
                            if (LocationArraylist != null) {
                                for (int j = 0; j < LocationArraylist.size(); j++) {
                                    if (LocationArraylist.get(j).getLocationMasterId().equals(locationId)) {
                                        locationpos = j;
                                        break;
                                    }
                                }
                                if (locationpos != -1)
                                    spinner_location.setSelection(locationpos);
                                else
                                    spinner_location.setSelection(0);
                            } else {
                                if (CommonClass.checkNet(ExcavationPermitActivity.this)) {
                                    new StartSession(ExcavationPermitActivity.this, new CallbackInterface() {
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

                        // WANoDetails waNoDetail = new WANoDetails();

                        /*waNoDetail.setFkOperationMasterId(jorder.getString("FkOperationMasterId"));
                        waNoDetail.setFkLocationMasterId(jorder.getString("FkLocationMasterId"));
                        waNoDetail.setFkWareHouseMasterId(jorder.getString("FkWareHouseMasterId"));
                        //jorder.getString("permitno");
                        waNoDetails.add(waNoDetail);*/
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
                Toast.makeText(ExcavationPermitActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
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
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_GetGoldenRules;

            try {
                res = CommonClass.OpenConnection(url, ExcavationPermitActivity.this);
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
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ExcavationPermitActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(goldenRulesArrayList);
                editor.putString("GoldenRules", json);
                editor.commit();
                goldenRuleAdapter = new GoldenRuleAdapter(ExcavationPermitActivity.this, goldenRulesArrayList, Mode, PermitStatus);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExcavationPermitActivity.this);
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
                res = CommonClass.OpenConnection(url, ExcavationPermitActivity.this);
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

                authorizedPersonAdapter = new AuthorizedPersonAdapter(ExcavationPermitActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);


            }


        }
    }

}


