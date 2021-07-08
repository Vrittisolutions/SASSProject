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
import com.vritti.sass.adapter.CleansingCommonAdapter;
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
import com.vritti.sass.model.Cleansing;
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
 * Created by sharvari on 26-Nov-18.
 */

public class ConfinedSpaceEntryActivity extends AppCompatActivity {


    ListView list_condition, list_protection, list_buddy_safety;
    String tempVal = "", ReasonVal = "";
    String Authorizeid = "", Authorize1id = "", Authorize3id = "", Authorize4id = "", Authorize5id = "", Authorize6id = "", Authorize7id = "", Permitclosed = "", spotcheck = "";
    ArrayList<Cleansing> cleansingArrayList;
    ArrayList<Cleansing> cleansingArrayList1;
    ArrayList<Cleansing> cleansingArrayList2;
    CleansingCommonAdapter commonAdapter;
    int Year, month, day;
    String date;
    String time, to_time = "";
    String time1;
    String Password = "";
    DatePickerDialog datePickerDialog;
    Button btn_fromdate, btn_todate, btn_confined_date, btn_confined_date1, btn_confined_date2, btn_confined_date3, btn_confined_date4, btn_confined_date5, btn_confined_date6,
            btn_confined_date7, btn_confined_date8, btn_confined_date9, btn_submit, btn_cancel_date;
    Button btn_fromtime, btn_totime;
    Spinner spinner_authorize, spinner_authorize1, spinner_authorize2, spinner_authorize3, spinner_authorize4, spinner_authorize5, spinner_authorize6,
            spinner_authorize7, spinner_permit_closed, spinner_spotcheck, spinner_station, spinner_contractor, spinner_operation, spinner_confined_space,
            spinner_prevention_plan;
    ImageView img_camera;
    CheckBox check_yes;
    EditText edit_remark, edit_desc_operation, edit_confined_space, edit_top1, edit_top2, edit_centre1, edit_centre2,
            edit_per1, edit_per2, edit_per3, edit_per4, edit_bottom1, edit_bottom2, edit_top3, edit_centre3, edit_per5, edit_per6, edit_gasname1,
            edit_gasname2, edit_gasname3, edit_gasname4, edit_gasname5, edit_permitno, edit_per_bottom;
    EditText edt_details_tank;
    String Flammable_Top = "", Flammable_Centre, Percentage1 = "", Percentage3 = "", Flammable_Bottom = "", Flammable_Date = "", Oxygen_Top = "", Oxygen_centre = "", Percentage2 = "", Percentage4 = "", Oxygen_Bottom = "", Oxygen_Date = "", Toxic_Top = "", Toxic_Centre = "", Toxic_Percentage1 = "", Toxic_Percentage2 = "", Toxic_bottom = "", Toxic_Date = "",
            Toxic_Gasname1 = "", Toxic_Gasname2 = "", Toxic_Gasname3 = "", Toxic_Gasname4 = "", Toxic_Gasname5 = "", PermitClosed_Date = "";

    int MY_CAMERA_PERMISSION_CODE = 100;
    int MEDIA_TYPE_IMAGE = 1;
    int CAMERA_REQUEST = 101;
    private Uri fileUri;
    File mediaFile;
    private static String IMAGE_DIRECTORY_NAME = "SASS";


    SharedPreferences userpreferences;
    private ArrayList<AuthorizedPerson> authorizedPersonArrayList;
    private ArrayList<ContractorList> contractorListActivityArrayList;
    private ArrayList<AuthorizedPerson> txt_authorizeArrayList;
    private ArrayList<ContractorList> contractorListActivityArrayList1;

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
    String authorize = "", authorize1 = "", authorize2 = "", authorize3 = "", authorize4 = "", authorize5 = "", authorize6 = "",
            authorize7 = "", PermitClosed = "", SpotCheck = "", StationId = "", PermitclosedId = "", userLoginId = "",
            PermitNo = "", StationName = "", contractorName = "", OperationName = "";
    String categoryDesc = "";
    int check = 0;
    private String serverResponseMessage, path, Imagefilename;
    GridView grid_safety;
    SafetyTools safetyTools;
    ArrayList<SafetyTools> safetyToolsArrayList;
    SafetyAdapter safetyAdapter;
    boolean isAns;

    ArrayList<Prevention> preventionArrayList;
    ArrayList<EquipmentUse> equipmentUseArrayList;
    ArrayList<IndicateRisk> indicateRiskArrayList;

    PrevenionAdapter prevenionAdapter;

    EquipmentAdapter equipmentAdapter;
    IndicateRiskAdapter indicateRiskAdapter;
    String PKFormId = "", formcode = "";
    String contractorId = "", operationId = "", LocationId = "", Checked = "Y", gen_condtn = "", indiviual_list = "",
            safety_tool_list = "", safetytools = "", TankdetailsId = "", WAH_No = "";
    String[] user, user1, user2, user3, user5;
    String individual_Others = "", buddy_Others = "", safetyTools_Others = "";

    JSONObject Activityjson;
    String StartDate = "", fromtime = "", totime = "", permitDate = "", spotdate = "", Remarks = "", Authorize1date = "", Authorize2date = "",
            Authorize3date = "", Authorize4date = "", Authorize5date = "", cancelDate = "", cancelId = "";
    String response = "";
    TextView txt_authorize, txt_authorize1, txt_authorize3, txt_authorize4, txt_authorize6, txt_authorize7, txt_permit_closed, txt_spot_check, txt_cancel, txt_cancel_permit,
            txt_authorize5;
    AlertDialog b;
    RelativeLayout custDialog1, custDialog2;
    EditText edt_password_reason, edt_reason;
    EditText edt_password_pass;
    LinearLayout ln_spinner_authorize, ln_spinner_reason;
    Button btn_cancel_pass, btn_submit_pass;
    Button btn_cancel_reason, btn_submit_reason;
    Permit permit;
    String Mode = "";
    private ArrayList<PermitNoWA> WAArayList = new ArrayList<>();
    LinearLayout ln_station, ln_WAHNo, ln_contractor, ln_DescOperation, ln_ConfinedSpace, len_cancel_permit;
    LinearLayout len_p_closed, len_p_spot;
    TextView tx_p_closed;
    private String PermitStatus;
    RecyclerView list_goldenRules;
    ArrayList<GoldenRules> goldenRulesArrayList;
    GoldenRuleAdapter goldenRuleAdapter;
    GoldenRules goldenRules;
    String goldenRulesList = "";
    String WAStartTime = "", WAEndTime = "", WAEndTime1 = "";
    int WAEndTimeHr, WAEndTimeMin, WAStartTimeHr, WAStartTimemin;
    int modeefirst = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confined_entry_limit_lay);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        toolbar.setTitle(getResources().getString(R.string.application_name));

        setSupportActionBar(toolbar);


        initview();
        setListener();
        dateListner();
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
                    spinner_contractor.setEnabled(false);
                    spinner_operation.setEnabled(false);
                    spinner_confined_space.setEnabled(false);
                    edt_details_tank.setKeyListener(null);
                    check_yes.setClickable(false);
                    edit_top1.setKeyListener(null);
                    edit_centre1.setKeyListener(null);
                    edit_per1.setKeyListener(null);
                    edit_per3.setKeyListener(null);
                    edit_bottom1.setKeyListener(null);
                    btn_confined_date.setEnabled(false);
                    txt_authorize3.setEnabled(false);
                    edit_top2.setKeyListener(null);
                    edit_centre2.setKeyListener(null);
                    edit_per2.setKeyListener(null);
                    edit_per4.setKeyListener(null);
                    edit_bottom2.setKeyListener(null);
                    btn_confined_date1.setEnabled(false);
                    txt_authorize4.setEnabled(false);
                    edit_top3.setKeyListener(null);
                    edit_gasname1.setKeyListener(null);
                    edit_centre3.setKeyListener(null);
                    edit_gasname2.setKeyListener(null);
                    edit_per5.setKeyListener(null);
                    edit_gasname3.setKeyListener(null);
                    edit_per6.setKeyListener(null);
                    edit_gasname4.setKeyListener(null);
                    edit_per_bottom.setKeyListener(null);
                    edit_gasname5.setKeyListener(null);
                    btn_confined_date2.setEnabled(false);
                    // spinner_authorize5.setEnabled(false);
                    txt_authorize.setKeyListener(null);
                    txt_authorize1.setKeyListener(null);
                    txt_authorize6.setKeyListener(null);
                    txt_authorize7.setKeyListener(null);
                    // txt_authorize5.setKeyListener(null);
                    edit_remark.setKeyListener(null);
                    btn_confined_date3.setEnabled(false);
                    btn_confined_date4.setEnabled(false);
                    btn_confined_date5.setEnabled(false);
                    btn_confined_date6.setEnabled(false);
                    btn_confined_date7.setEnabled(false);
                    btn_fromtime.setTextColor(Color.parseColor("#000000"));
                    btn_confined_date.setTextColor(Color.parseColor("#000000"));
                    btn_confined_date1.setTextColor(Color.parseColor("#000000"));
                    btn_confined_date2.setTextColor(Color.parseColor("#000000"));
                    btn_confined_date3.setTextColor(Color.parseColor("#000000"));
                    btn_confined_date4.setTextColor(Color.parseColor("#000000"));
                    btn_confined_date5.setTextColor(Color.parseColor("#000000"));
                    btn_confined_date6.setTextColor(Color.parseColor("#000000"));
                    btn_confined_date7.setTextColor(Color.parseColor("#000000"));


                    if (PermitStatus.equalsIgnoreCase("R") || PermitStatus.equalsIgnoreCase("C")) {
                        btn_submit.setClickable(false);
                        txt_spot_check.setEnabled(false);
                        btn_confined_date8.setEnabled(false);
                        btn_confined_date8.setTextColor(Color.parseColor("#000000"));
                        txt_permit_closed.setEnabled(false);
                        btn_confined_date9.setEnabled(false);
                        btn_confined_date9.setTextColor(Color.parseColor("#000000"));
                        tx_p_closed.setKeyListener(null);
                        txt_cancel_permit.setKeyListener(null);
                        btn_cancel_date.setKeyListener(null);
                        btn_cancel_date.setTextColor(Color.parseColor("#000000"));
                        txt_cancel.setEnabled(false);
                    }


                }else{btn_fromdate.setEnabled(true);}

                //if (PermitStatus.equals("A") || PermitStatus.equals("P")) {
                    len_p_closed.setVisibility(View.VISIBLE);
                    tx_p_closed.setVisibility(View.VISIBLE);
                    txt_cancel_permit.setVisibility(View.VISIBLE);
                    len_cancel_permit.setVisibility(View.VISIBLE);
               /* } else {
                    len_p_closed.setVisibility(View.GONE);
                    tx_p_closed.setVisibility(View.GONE);
                    len_cancel_permit.setVisibility(View.GONE);
                    txt_cancel_permit.setVisibility(View.GONE);
                }*/

                Mode = "E";
                PermitNo = permit.getPermitNo();
              //  btn_fromdate.setEnabled(false);
                btn_fromdate.setTextColor(Color.parseColor("#101010"));
                if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                    new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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

                if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                    showProgress();
                    new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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
        btn_confined_date.setText(date);
        btn_confined_date1.setText(date);
        btn_confined_date2.setText(date);
        btn_confined_date3.setText(date);
        btn_confined_date4.setText(date);
        btn_confined_date5.setText(date);
        btn_confined_date6.setText(date);
        btn_confined_date7.setText(date);
        btn_confined_date8.setText(date);
        btn_confined_date9.setText(date);
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


        //Indicate Risk

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
        gson = new Gson();
        String indicatejson = sharedPrefs.getString("general", "");
        String equipmentjson = sharedPrefs.getString("protection", "");
        String preventionjson = sharedPrefs.getString("equipped", "");
        Type general = new TypeToken<List<IndicateRisk>>() {
        }.getType();
        Type protection = new TypeToken<List<EquipmentUse>>() {
        }.getType();
        Type equiped = new TypeToken<List<Prevention>>() {
        }.getType();
        indicateRiskArrayList = gson.fromJson(indicatejson, general);
        equipmentUseArrayList = gson.fromJson(equipmentjson, protection);
        preventionArrayList = gson.fromJson(preventionjson, equiped);

        if (indicateRiskArrayList == null) {
            if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                showProgress();
                new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadConfineListData().execute();
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
                indicateRiskAdapter = new IndicateRiskAdapter(ConfinedSpaceEntryActivity.this, indicateRiskArrayList, Mode, PermitStatus);
                list_condition.setAdapter(indicateRiskAdapter);
                Utility.setListViewHeightBasedOnItems(list_condition);


            }

        }

        //Equipment
        if (equipmentUseArrayList == null) {
            if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                showProgress();

                new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadConfineListData().execute();
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
                equipmentAdapter = new EquipmentAdapter(ConfinedSpaceEntryActivity.this, equipmentUseArrayList, Mode, PermitStatus);
                list_protection.setAdapter(equipmentAdapter);
                Utility.setListViewHeightBasedOnItems(list_protection);

            }

        }
        if (preventionArrayList == null) {
            if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                showProgress();
                new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadConfineListData().execute();
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
                prevenionAdapter = new PrevenionAdapter(ConfinedSpaceEntryActivity.this, preventionArrayList, Mode, PermitStatus);
                list_buddy_safety.setAdapter(prevenionAdapter);
                Utility.setListViewHeightBasedOnItems(list_buddy_safety);

            }

        }


        //Authorized Person
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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
                authorizedPersonAdapter = new AuthorizedPersonAdapter(ConfinedSpaceEntryActivity.this, authorizedPersonArrayList);
                //  spinner_authorize5.setAdapter(authorizedPersonAdapter);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);

            }

        }


        // Safety Tools
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("safety", "");
        type = new TypeToken<List<SafetyTools>>() {
        }.getType();
        safetyToolsArrayList = gson.fromJson(json, type);

        if (safetyToolsArrayList == null) {
            if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                showProgress();
                new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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
                safetyAdapter = new SafetyAdapter(ConfinedSpaceEntryActivity.this, safetyToolsArrayList, "CSE", Mode, PermitStatus);
                grid_safety.setAdapter(safetyAdapter);


            }

        }


        // Depot Station
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Depot", "");
        type = new TypeToken<List<Depot>>() {
        }.getType();
        depotArrayList = gson.fromJson(json, type);

        if (depotArrayList == null) {
            if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                showProgress();
                new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadDepotData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(ConfinedSpaceEntryActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (depotArrayList.size() > 0) {
                depotAdapter = new DepotAdapter(ConfinedSpaceEntryActivity.this, depotArrayList);
                spinner_station.setAdapter(depotAdapter);


            }

        }


        // ContractorList

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Contractor", "");
        type = new TypeToken<List<ContractorList>>() {
        }.getType();
        contractorListActivityArrayList = gson.fromJson(json, type);

        if (contractorListActivityArrayList == null) {
            if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                showProgress();
                new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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
                permitContractorListAdapter = new PermitContractorListAdapter(ConfinedSpaceEntryActivity.this, contractorListActivityArrayList);
                spinner_authorize2.setAdapter(permitContractorListAdapter);
                spinner_contractor.setAdapter(permitContractorListAdapter);
            }

        }


        // Golden Rules
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("GoldenRules", "");
        type = new TypeToken<List<GoldenRules>>() {
        }.getType();
        goldenRulesArrayList = gson.fromJson(json, type);

        if (goldenRulesArrayList == null) {
            if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                showProgress();
                new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadGoldenRules().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(ConfinedSpaceEntryActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (goldenRulesArrayList.size() > 0) {

                goldenRuleAdapter = new GoldenRuleAdapter(ConfinedSpaceEntryActivity.this, goldenRulesArrayList, Mode, PermitStatus);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ConfinedSpaceEntryActivity.this);

                // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                list_goldenRules.setLayoutManager(linearLayoutManager);
                list_goldenRules.setAdapter(goldenRuleAdapter);


            }

        }

        //tank details
/*

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Contractor", "");
        type = new TypeToken<List<ContractorList>>() {
        }.getType();
        contractorListActivityArrayList = gson.fromJson(json, type);

        if (contractorListActivityArrayList == null) {
            if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                showProgress();
                new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadTankDetails().execute();
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
                permitContractorListAdapter = new PermitContractorListAdapter(ConfinedSpaceEntryActivity.this, contractorListActivityArrayList);
                //spinner_authorize2.setAdapter(permitContractorListAdapter);
                spinner_details_tank.setAdapter(permitContractorListAdapter);
            }

        }
*/

        // Operation

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Operation", "");
        type = new TypeToken<List<Operation>>() {
        }.getType();
        operationArrayList = gson.fromJson(json, type);

        if (operationArrayList == null) {
            if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                showProgress();
                new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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
                operationAdapter = new OperationAdapter(ConfinedSpaceEntryActivity.this, operationArrayList);
                spinner_operation.setAdapter(operationAdapter);
            }

        }

    }

    public void initview() {
        list_condition = findViewById(R.id.list_condition);
        list_protection = findViewById(R.id.list_protection);
        list_buddy_safety = findViewById(R.id.list_buddy_safety);

        //list_buddy_safety= findViewById(R.id.list_buddy_safety);
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        userpreferences = getSharedPreferences(LoginActivity.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", "");
        grid_safety = findViewById(R.id.grid_safety);


        contractorListActivityArrayList = new ArrayList<>();
        authorizedPersonArrayList = new ArrayList<>();
        operationArrayList = new ArrayList<>();
        LocationArraylist = new ArrayList<>();
        depotArrayList = new ArrayList<>();
        safetyToolsArrayList = new ArrayList<>();
        txt_authorizeArrayList = new ArrayList<>();
        goldenRulesArrayList = new ArrayList<>();

        edit_remark = findViewById(R.id.edit_remark);
        spinner_prevention_plan = findViewById(R.id.spinner_prevention_plan);
        edit_top1 = findViewById(R.id.edit_top1);
        edit_top2 = findViewById(R.id.edit_top2);
        edit_centre1 = findViewById(R.id.edit_centre1);
        edit_centre2 = findViewById(R.id.edit_centre2);
        edit_per1 = findViewById(R.id.edit_per1);
        edit_per2 = findViewById(R.id.edit_per2);
        edit_per3 = findViewById(R.id.edit_per3);
        edit_per4 = findViewById(R.id.edit_per4);
        edit_bottom1 = findViewById(R.id.edit_bottom1);
        edit_bottom2 = findViewById(R.id.edit_bottom2);
        edit_top3 = findViewById(R.id.edit_top3);
        edit_centre3 = findViewById(R.id.edit_centre3);
        edit_per5 = findViewById(R.id.edit_per5);
        edit_per6 = findViewById(R.id.edit_per6);
        edit_gasname1 = findViewById(R.id.edit_gasname1);
        edit_gasname2 = findViewById(R.id.edit_gasname2);
        edit_gasname3 = findViewById(R.id.edit_gasname3);
        edit_gasname4 = findViewById(R.id.edit_gasname4);
        edit_gasname5 = findViewById(R.id.edit_gasname5);
        edit_permitno = findViewById(R.id.edit_permitno);
        edit_per_bottom = (EditText) findViewById(R.id.edit_per_bottom);
        check_yes = (CheckBox) findViewById(R.id.check_yes);

        spinner_authorize = findViewById(R.id.spinner_authorize_pas);
        custDialog1 = findViewById(R.id.cusDialog1);
        edt_password_pass = findViewById(R.id.edt_password_pass);
        ln_spinner_authorize = findViewById(R.id.ln_spinner_authorize);
        ln_spinner_reason = findViewById(R.id.ln_spinner_reason);
        btn_cancel_pass = findViewById(R.id.txt_cancel_pass);
        btn_submit_pass = findViewById(R.id.txt_submit_pass);


        spinner_permit_closed = findViewById(R.id.spinner_permit_closed1);
        custDialog2 = findViewById(R.id.cusDialog2);
        edt_password_reason = findViewById(R.id.edt_password_reason);
        edt_reason = findViewById(R.id.edt_reason);
        btn_cancel_reason = findViewById(R.id.txt_cancel_reason);
        btn_submit_reason = findViewById(R.id.txt_submit_reason);


        btn_fromdate = findViewById(R.id.btn_fromdate);
        //btn_todate = findViewById(R.id.btn_todate);
        btn_confined_date = findViewById(R.id.btn_confined_date);
        btn_confined_date1 = findViewById(R.id.btn_confined_date1);
        btn_confined_date2 = findViewById(R.id.btn_confined_date2);
        btn_confined_date3 = findViewById(R.id.btn_confined_date3);
        btn_confined_date4 = findViewById(R.id.btn_confined_date4);
        btn_confined_date5 = findViewById(R.id.btn_confined_date5);
        btn_confined_date6 = findViewById(R.id.btn_confined_date6);
        btn_confined_date7 = findViewById(R.id.btn_confined_date7);
        btn_confined_date8 = findViewById(R.id.edt_permit_date);
        btn_confined_date9 = findViewById(R.id.edt_spot_date);
        btn_cancel_date = findViewById(R.id.edt_cancel_date);
        btn_submit = findViewById(R.id.btn_submit);

        txt_authorize1 = (TextView) findViewById(R.id.spinner_authorize1);
        txt_authorize3 = (TextView) findViewById(R.id.spinner_authorize3);
        txt_authorize4 = (TextView) findViewById(R.id.spinner_authorize4);
        txt_authorize = (TextView) findViewById(R.id.spinner_authorize);
        txt_authorize6 = (TextView) findViewById(R.id.spinner_authorize6);
        txt_authorize7 = (TextView) findViewById(R.id.spinner_authorize7);
        txt_authorize5 = (TextView) findViewById(R.id.spinner_authorize5);
        txt_permit_closed = (TextView) findViewById(R.id.txt_permit_closed);
        txt_spot_check = (TextView) findViewById(R.id.txt_spotcheck);
        txt_cancel = (TextView) findViewById(R.id.txt_cancel);
        len_cancel_permit = findViewById(R.id.len_cancel_permit);
        txt_cancel_permit = findViewById(R.id.txt_cancel_permit);

        //spinner_authorize = findViewById(R.id.spinner_authorize);

        spinner_authorize2 = findViewById(R.id.spinner_authorize2);
        spinner_authorize2.setEnabled(false);
        //spinner_authorize3 = findViewById(R.id.spinner_authorize3);
        //spinner_authorize4 = findViewById(R.id.spinner_authorize4);
        //spinner_authorize5 = findViewById(R.id.spinner_authorize5);
        //spinner_authorize6 = findViewById(R.id.spinner_authorize6);
        //spinner_authorize7 = findViewById(R.id.spinner_authorize7);

        //spinner_spotcheck = findViewById(R.id.spinner_spotcheck);
        spinner_station = findViewById(R.id.spinner_station);
        spinner_contractor = findViewById(R.id.spinner_contractor);
        spinner_operation = findViewById(R.id.spinner_operation);
        spinner_confined_space = findViewById(R.id.spinner_confined_space);
        edt_details_tank = findViewById(R.id.edt_details_tank);

        btn_fromtime = findViewById(R.id.btn_fromtime);
        btn_totime = findViewById(R.id.btn_totime);
        btn_totime.setEnabled(false);
        btn_totime.setTextColor(Color.parseColor("#000000"));
        img_camera = findViewById(R.id.img_camera);

        ln_station = findViewById(R.id.ln_station);
        ln_WAHNo = findViewById(R.id.ln_WAHNo);
        ln_contractor = findViewById(R.id.ln_contractor);
        ln_DescOperation = findViewById(R.id.ln_DescOperation);
        ln_ConfinedSpace = findViewById(R.id.ln_ConfinedSpace);

        len_p_closed = findViewById(R.id.len_p_closed);
        tx_p_closed = findViewById(R.id.tx_p_closed);
        list_goldenRules = findViewById(R.id.list_goldenRules);


        spinner_operation.setEnabled(false);
        spinner_confined_space.setEnabled(false);


    }

    public void setListener() {


        btn_cancel_pass.setOnClickListener(new View.OnClickListener() {
            //0,1,3,4,5,6,7,8,9,10
            @Override
            public void onClick(View view) {

                ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text);
                edt_password_pass.setBackgroundResource(R.drawable.edit_text);

                if (custDialog1.getVisibility() == View.VISIBLE) {
                    custDialog1.setVisibility(View.GONE);
                    if (tempVal.equals("0")) {
                        txt_authorize.setText("Select");
                        spinner_authorize.setSelection(0);
                        edt_password_pass.setText("");
                        txt_authorize.setText("Select");
                    } else if (tempVal.equals("1")) {
                        spinner_authorize.setSelection(0);
                        edt_password_pass.setText("");
                        txt_authorize1.setText("Select");
                    } else if (tempVal.equals("3")) {
                        spinner_authorize.setSelection(0);
                        edt_password_pass.setText("");
                        txt_authorize3.setText("Select");
                    } else if (tempVal.equals("4")) {
                        spinner_authorize.setSelection(0);
                        edt_password_pass.setText("");
                        txt_authorize4.setText("Select");
                    } else if (tempVal.equals("6")) {
                        spinner_authorize.setSelection(0);
                        edt_password_pass.setText("");
                        txt_authorize6.setText("Select");
                    } else if (tempVal.equals("7")) {
                        spinner_authorize.setSelection(0);
                        edt_password_pass.setText("");
                        txt_authorize7.setText("Select");
                    }
                } else {
                    custDialog1.setVisibility(View.VISIBLE);
                }
                hideKeyboard(ConfinedSpaceEntryActivity.this);
            }
        });

        btn_submit_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = edt_password_pass.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));
                ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text);
                edt_password_pass.setBackgroundResource(R.drawable.edit_text);
                if (Password.equalsIgnoreCase("") && (authorize.equalsIgnoreCase("--Select--")
                        || authorize.equalsIgnoreCase(""))) {

                    Toast toast = Toast.makeText(ConfinedSpaceEntryActivity.this, "Please enter authorized person and password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text_red);
                    edt_password_pass.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();

                    // Toast toast = new Toast(getApplicationContext());
                    // Toast toast = Toast. makeText(WorkAuthorizationActivity.this, "Please enter password", Toast.LENGTH_SHORT);
                    // toast.setGravity(Gravity.CENTER, 0, -160);
                    // toast.setDuration(Toast.LENGTH_SHORT);
                    // toast.setView(layout);
                    // toast.show();
                } else if (!Password.equalsIgnoreCase("") && (authorize.equalsIgnoreCase("--Select--")
                        || authorize.equalsIgnoreCase(""))) {
                    Toast toast = Toast.makeText(ConfinedSpaceEntryActivity.this, "Please select authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && (!authorize.equalsIgnoreCase("--Select--")
                        || !authorize.equalsIgnoreCase(""))) {
                    Toast toast = Toast.makeText(ConfinedSpaceEntryActivity.this, "Please enter valid password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edt_password_pass.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else {
                    if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                        new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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

        btn_cancel_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (custDialog2.getVisibility() == View.VISIBLE) {
                    custDialog2.setVisibility(View.GONE);
                    if (ReasonVal.equals("1")) {
                        spinner_permit_closed.setSelection(0);
                        edt_password_reason.setText("");
                        edt_reason.setText("");
                        txt_permit_closed.setText("Select");
                        PermitclosedId = "";
                    } else if (ReasonVal.equals("2")) {
                        spinner_permit_closed.setSelection(0);
                        edt_password_reason.setText("");
                        edt_reason.setText("");
                        txt_spot_check.setText("Select");
                        spotcheck = "";
                    } else if (ReasonVal.equals("3")) {
                        txt_cancel.setText("Select");
                        spinner_permit_closed.setSelection(0);
                        edt_password_reason.setText("");
                        edt_reason.setText("");
                        cancelId = "";
                    }

                    if ((PermitclosedId.equalsIgnoreCase("Select") || PermitclosedId.equals(""))
                            && (spotcheck.equalsIgnoreCase("Select") || spotcheck.equals(""))) {
                        // btn_submit.setVisibility(View.GONE);
                    } else {
                        btn_submit.setVisibility(View.VISIBLE);
                    }

                } else
                    custDialog2.setVisibility(View.VISIBLE);
            }
        });

        btn_submit_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password = edt_password_reason.getText().toString();
                String Reason = edt_reason.getText().toString();
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                ln_spinner_reason.setBackgroundResource(R.drawable.edit_text);
                edt_password_reason.setBackgroundResource(R.drawable.edit_text);
                edt_reason.setBackgroundResource(R.drawable.edit_text);

                if (Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {

                    Toast toast = Toast.makeText(ConfinedSpaceEntryActivity.this, "Please enter authorized person,password and reason", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    edt_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    edt_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();

                    // Toast toast = new Toast(getApplicationContext());
                    // Toast toast = Toast. makeText(WorkAuthorizationActivity.this, "Please enter password", Toast.LENGTH_SHORT);
                    // toast.setGravity(Gravity.CENTER, 0, -160);
                    // toast.setDuration(Toast.LENGTH_SHORT);
                    // toast.setView(layout);
                    // toast.show();
                } else if (!Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(ConfinedSpaceEntryActivity.this, "Please select authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(ConfinedSpaceEntryActivity.this, "Please valid password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edt_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (!Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(ConfinedSpaceEntryActivity.this, "Please enter reason", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edt_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    //password and reason blank
                    Toast toast = Toast.makeText(ConfinedSpaceEntryActivity.this, "Please valid password and reason", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edt_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    edt_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    //password and permit
                    Toast toast = Toast.makeText(ConfinedSpaceEntryActivity.this, "Please valid password and authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edt_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (!Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    //reason and permit
                    Toast toast = Toast.makeText(ConfinedSpaceEntryActivity.this, "Please enter reason and authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edt_reason.setBackgroundResource(R.drawable.edit_text_red);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else {
                    if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                        new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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


        txt_authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authorizedialog();
                //tier 1
                CategoryWiseAuthorizeName("level 1", "fromPWD");
                custDialog1.setVisibility(View.VISIBLE);
                tempVal = "0";
            }
        });

        txt_authorize1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authorize1dialog();
                //Tier 1 + Tier 2
                CategoryWiseAuthorizeName("level 2", "fromPwd");
                custDialog1.setVisibility(View.VISIBLE);
                tempVal = "1";
            }
        });
        txt_authorize3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passworddialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 2", "fromPWD");
                custDialog1.setVisibility(View.VISIBLE);
                tempVal = "3";
            }
        });
        txt_authorize4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authorize4dialog();
                //tier 1
                CategoryWiseAuthorizeName("level 2", "fromPWD");
                custDialog1.setVisibility(View.VISIBLE);
                tempVal = "4";
            }
        });
        txt_authorize5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authorize4dialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 2", "fromPWD");
                custDialog1.setVisibility(View.VISIBLE);
                tempVal = "5";
            }
        });
        txt_authorize6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authorize6dialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 2", "fromPWD");
                custDialog1.setVisibility(View.VISIBLE);
                tempVal = "6";
            }
        });
        txt_authorize7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //authorize7dialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 2", "fromPWD");
                custDialog1.setVisibility(View.VISIBLE);
                tempVal = "7";
            }
        });
        txt_permit_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reasondialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 1", "fromReason");
                custDialog2.setVisibility(View.VISIBLE);
                ReasonVal = "1";
            }
        });

        txt_spot_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Spotcheckdialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 1", "fromReason");
                custDialog2.setVisibility(View.VISIBLE);
                ReasonVal = "2";

            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Spotcheckdialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 1", "fromReason");
                custDialog2.setVisibility(View.VISIBLE);
                ReasonVal = "3";

            }
        });


        txt_permit_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reasondialog();
                CategoryWiseAuthorizeName("level 1", "fromReason");
                custDialog2.setVisibility(View.VISIBLE);
                ReasonVal = "1";
            }
        });

        txt_spot_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Spotcheckdialog();
                CategoryWiseAuthorizeName("level 1", "fromReason");
                custDialog2.setVisibility(View.VISIBLE);
                ReasonVal = "2";

            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryWiseAuthorizeName("level 1", "fromReason");
                custDialog2.setVisibility(View.VISIBLE);
                ReasonVal = "3";
            }
        });


      /*  spinner_authorize5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                authorize5 = authorizedPersonArrayList.get(position).getAuthorizeid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ConfinedSpaceEntryActivity.this, Manifest.permission.CAMERA)
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

        spinner_prevention_plan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (WAArayList.size() > 0) {

                    WAH_No = WAArayList.get(position).getPermitNo();

                    if (WAH_No != "") {
                        if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                            showProgress();
                            //Location Get
                            new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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
                            Toast.makeText(ConfinedSpaceEntryActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (contractorListActivityArrayList != null) {
                    contractorId = contractorListActivityArrayList.get(position).getCustVendorMasterId();
                    int contractorpos = -1;
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

                    if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                        showProgress();
                        //Location Get
                        new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadLocationOperationData().execute(StationId);
                                /*DownloadLocationOperationData*/
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
                        if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                            new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadWANo().execute(contractorId);
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    Toast.makeText(ConfinedSpaceEntryActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(ConfinedSpaceEntryActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                StationId = depotArrayList.get(position).getDepotid();
                StationName = depotArrayList.get(position).getDepotname();

                if (StationId.equals("Select")) {


                } else {

                    if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                        new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadAuthorizedPersonDataDepo().execute(StationId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

/*
        spinner_details_tank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TankdetailsId = contractorListActivityArrayList.get(position).getCustVendorMasterId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        spinner_operation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                operationId = operationArrayList.get(position).getOperationMasterId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_confined_space.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LocationId = LocationArraylist.get(position).getLocationMasterId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        check_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_yes.isChecked()) {
                    Checked = "Y";
                } else {
                    Checked = "N";
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
                            Authorizeid = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("1")) {
                            txt_authorize1.setText(name);
                            Authorize1id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("3")) {
                            txt_authorize3.setText(name);
                            Authorize3id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("4")) {
                            txt_authorize4.setText(name);
                            Authorize4id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("5")) {
                            txt_authorize5.setText(name);
                            authorize5 = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("6")) {
                            txt_authorize6.setText(name);
                            Authorize6id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("7")) {
                            txt_authorize7.setText(name);
                            Authorize7id = txt_authorizeArrayList.get(position).getAuthorizeid();
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
                            txt_permit_closed.setText(Permitname);
                            PermitclosedId = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (ReasonVal.equals("2")) {
                            txt_spot_check.setText(Permitname);
                            spotcheck = txt_authorizeArrayList.get(position).getAuthorizeid();
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
    }

    public void dateListner() {


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

               // btn_fromtime.setText(UpdateTime.updateTime(hour, minute));
               // btn_totime.setText(UpdateTime.updateTime(hour1, minute));

                mTimePicker = new TimePickerDialog(ConfinedSpaceEntryActivity.this,
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
                                    if ((selectedHour + 4) > WAEndTimeHr) {
                                        btn_totime.setText(WAEndTime1);

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
                                            Toast.makeText(ConfinedSpaceEntryActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ConfinedSpaceEntryActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(ConfinedSpaceEntryActivity.this, msg, Toast.LENGTH_SHORT).show();
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

                                            Toast.makeText(ConfinedSpaceEntryActivity.this, msg, Toast.LENGTH_SHORT).show();
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





      /*  btn_fromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                int hour1 = hour + 4;

                TimePickerDialog mTimePicker;

                btn_fromtime.setText(UpdateTime.updateTime(hour, minute));
                btn_totime.setText(UpdateTime.updateTime(hour1, minute));

                mTimePicker = new TimePickerDialog(ConfinedSpaceEntryActivity.this,
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

                                    Toast.makeText(ConfinedSpaceEntryActivity.this, "Please select time greater than workauthorization time", Toast.LENGTH_SHORT).show();
                                }



                            }
                        }, hour, minute, false);// Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });*/


        /*******************************************************/
      /*  btn_fromtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                int hour1 = hour + 4;

                TimePickerDialog mTimePicker;

                btn_fromtime.setText(UpdateTime.updateTime(hour, minute));
                btn_totime.setText(UpdateTime.updateTime(hour1, minute));

                mTimePicker = new TimePickerDialog(ConfinedSpaceEntryActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                time = UpdateTime.updateTime(selectedHour, selectedMinute);

                                if (selectedHour >= 9) {
                                    to_time = UpdateTime.updateTime((selectedHour + 4), selectedMinute);
                                    if(to_time.contains("AM")){
                                        btn_totime.setText("11:59 PM");
                                    }else{
                                        btn_totime.setText(to_time);
                                    }
                                    btn_fromtime.setText(time);

                                } else if (selectedHour < 9) {
                                    btn_fromtime.setText(UpdateTime.updateTime(mcurrentTime.get(Calendar.HOUR_OF_DAY), mcurrentTime.get(Calendar.MINUTE)));
                                    btn_totime.setText(UpdateTime.updateTime(mcurrentTime.get(Calendar.HOUR_OF_DAY) + 4, mcurrentTime.get(Calendar.MINUTE)));
                                    ;
                                    Toast.makeText(ConfinedSpaceEntryActivity.this, "You cannot select time before 9:00 AM", Toast.LENGTH_SHORT).show();
                                }


                              *//*  if (selectedHour >= 9) {
                                    to_time = UpdateTime.updateTime((selectedHour + 4), selectedMinute);
                                    btn_fromtime.setText(time);
                                    btn_totime.setText(to_time);
                                } else if (selectedHour < 9) {
                                    btn_fromtime.setText(UpdateTime.updateTime(mcurrentTime.get(Calendar.HOUR_OF_DAY), mcurrentTime.get(Calendar.MINUTE)));
                                    btn_totime.setText(UpdateTime.updateTime(mcurrentTime.get(Calendar.HOUR_OF_DAY) + 4, mcurrentTime.get(Calendar.MINUTE)));
                                    ;
                                    Toast.makeText(ConfinedSpaceEntryActivity.this, "You cannot select time before 9:00 AM", Toast.LENGTH_SHORT).show();
                                }*//*


                            }
                        }, hour, minute, false);// Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
*/
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

                mTimePicker = new TimePickerDialog(ConfinedSpaceEntryActivity.this,
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


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* int authorize1pos = spinner_authorize.getSelectedItemPosition();
                 int authorize2pos = spinner_authorize1.getSelectedItemPosition();
                 int authorize3pos = spinner_authorize2.getSelectedItemPosition();
                 int authorize4pos = spinner_authorize6.getSelectedItemPosition();
                 int authorize5pos = spinner_authorize7.getSelectedItemPosition();*/


                if (StationId.equalsIgnoreCase("Select") || StationId.equalsIgnoreCase("")) {
                    Toast.makeText(ConfinedSpaceEntryActivity.this, "Please fill station details", Toast.LENGTH_SHORT).show();
                    ln_station.setBackgroundResource(R.drawable.edit_text_red);
                } else if (WAH_No.equalsIgnoreCase("Select") || WAH_No.equalsIgnoreCase("")) {
                    Toast.makeText(ConfinedSpaceEntryActivity.this, "Please fill WAH No. details", Toast.LENGTH_SHORT).show();
                    ln_WAHNo.setBackgroundResource(R.drawable.edit_text_red);
                } else if (contractorId.equalsIgnoreCase("Select") || contractorId.equalsIgnoreCase("")) {
                    Toast.makeText(ConfinedSpaceEntryActivity.this, "Please fill contractor details", Toast.LENGTH_SHORT).show();
                    ln_contractor.setBackgroundResource(R.drawable.edit_text_red);
                } else if (operationId.equalsIgnoreCase("Select") || operationId.equalsIgnoreCase("")) {
                    Toast.makeText(ConfinedSpaceEntryActivity.this, "Please fill  operation details", Toast.LENGTH_SHORT).show();
                    ln_DescOperation.setBackgroundResource(R.drawable.edit_text_red);
                } else if (LocationId.equalsIgnoreCase("Select") || LocationId.equalsIgnoreCase("")) {
                    Toast.makeText(ConfinedSpaceEntryActivity.this, "Please fill location details", Toast.LENGTH_SHORT).show();
                    ln_ConfinedSpace.setBackgroundResource(R.drawable.edit_text_red);
                } else {
                    if (Mode.equalsIgnoreCase("A")) {
                        saveactivityjson();
                    } else if (Mode.equalsIgnoreCase("E")) {
                        editjson();
                    } else {

                    }
                }


               /* if(StationId.equalsIgnoreCase("Select") || WAH_No.equalsIgnoreCase("")
                        || contractorId.equalsIgnoreCase("") || operationId.equalsIgnoreCase("") ||
                        LocationId.equalsIgnoreCase("")){


                }else{


                }*/
            }
        });


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
                datePickerDialog = new DatePickerDialog(ConfinedSpaceEntryActivity.this,
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
                                            Toast.makeText(ConfinedSpaceEntryActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
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
// datePickerDialog.getDatePicker().setMaxDate(enddate);
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);

            }
        });


        btn_confined_date.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(ConfinedSpaceEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_confined_date.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_confined_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_confined_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ConfinedSpaceEntryActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_confined_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_confined_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_confined_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_confined_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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

        btn_confined_date1.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(ConfinedSpaceEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_confined_date1.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_confined_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_confined_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ConfinedSpaceEntryActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_confined_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_confined_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_confined_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_confined_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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
        btn_confined_date2.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(ConfinedSpaceEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_confined_date2.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_confined_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_confined_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ConfinedSpaceEntryActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_confined_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_confined_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_confined_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_confined_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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
        btn_confined_date3.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(ConfinedSpaceEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_confined_date3.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_confined_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_confined_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ConfinedSpaceEntryActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_confined_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_confined_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_confined_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_confined_date3.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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
        btn_confined_date4.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(ConfinedSpaceEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_confined_date4.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_confined_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_confined_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ConfinedSpaceEntryActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_confined_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_confined_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_confined_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_confined_date4.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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

        btn_confined_date5.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(ConfinedSpaceEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_confined_date5.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_confined_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_confined_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ConfinedSpaceEntryActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_confined_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_confined_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_confined_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_confined_date5.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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
        btn_confined_date6.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(ConfinedSpaceEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_confined_date6.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_confined_date6.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_confined_date6.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ConfinedSpaceEntryActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_confined_date6.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_confined_date6.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_confined_date6.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_confined_date6.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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

        btn_confined_date7.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(ConfinedSpaceEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_confined_date7.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_confined_date7.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_confined_date7.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ConfinedSpaceEntryActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_confined_date7.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_confined_date7.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_confined_date7.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_confined_date7.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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

        btn_confined_date8.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(ConfinedSpaceEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_confined_date8.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_confined_date8.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_confined_date8.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ConfinedSpaceEntryActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_confined_date8.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_confined_date8.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_confined_date8.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_confined_date8.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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

        btn_confined_date9.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(ConfinedSpaceEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                btn_confined_date9.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            btn_confined_date9.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            btn_confined_date9.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(ConfinedSpaceEntryActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            btn_confined_date9.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        btn_confined_date9.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        btn_confined_date9.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_confined_date9.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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
                datePickerDialog = new DatePickerDialog(ConfinedSpaceEntryActivity.this,
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
                                            Toast.makeText(ConfinedSpaceEntryActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
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

                                        Toast.makeText(ConfinedSpaceEntryActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    btn_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year));
                                    trnselectDate = Year + "-" + (month + 1)
                                            + "-" + day + " 00:00:00.000";
                                    Toast.makeText(ConfinedSpaceEntryActivity.this, "Past date is  accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, Year, month, day);


                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

                //datePickerDialog.getDatePicker().setMaxDate(enddate);

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

    public void reasonDialog_SafetyTools(final int position, String SafetyToolMasterId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ConfinedSpaceEntryActivity.this);
        LayoutInflater inflater = getLayoutInflater().from(ConfinedSpaceEntryActivity.this);
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
            progressDialog = new ProgressDialog(ConfinedSpaceEntryActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setTitle("Data fetching...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_getHWDetails + "?form=" + PermitNo;

            try {
                res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);
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
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ConfinedSpaceEntryActivity.this);

                            // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            list_goldenRules.setLayoutManager(linearLayoutManager);
                            goldenRuleAdapter = new GoldenRuleAdapter();
                            goldenRuleAdapter.updateList(goldenRulesArrayList, Mode, PermitStatus);
                            list_goldenRules.setAdapter(goldenRuleAdapter);


                        } else {
                            if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                                new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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
                            if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                                new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new DownloadWANo().execute(contractorId);
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {
                                        Toast.makeText(ConfinedSpaceEntryActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(ConfinedSpaceEntryActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //  edit_Prevention_plan.setText(WAH_No);

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

                        LocationId = jorder.getString("NatConfinedSpace");


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


                        operationId = jorder.getString("fkOperationMasterId");
                        OperationName = jorder.getString("Operation");
                        int operationpos = -1;
                        for (int j = 0; j < operationArrayList.size(); j++) {
                            if (operationArrayList.get(j).getOperationMasterId().equals(operationId))
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

                        TankdetailsId = jorder.getString("TankDetails");
                        edt_details_tank.setText(TankdetailsId);

                        Checked = jorder.getString("MethodOperationStatus");
                        if (Checked.contains("Y"))
                            check_yes.setChecked(true);
                        else if (Checked.equals("N"))
                            check_yes.setChecked(false);
                        else
                            check_yes.setChecked(false);

                        Flammable_Top = jorder.getString("FlammableTop");
                        if (!Flammable_Top.equalsIgnoreCase(""))
                            edit_top1.setText(Flammable_Top);
                        else
                            edit_top1.setText("");

                        Flammable_Centre = jorder.getString("FlammableCentre");
                        if (!Flammable_Centre.equalsIgnoreCase(""))
                            edit_centre1.setText(Flammable_Centre);
                        else
                            edit_centre1.setText("");

                        Percentage1 = jorder.getString("Flammable1stpoint");
                        if (!Percentage1.equalsIgnoreCase(""))
                            edit_per1.setText(Percentage1);
                        else
                            edit_per1.setText("");

                        Percentage3 = jorder.getString("Flammable2ndpoint");
                        if (!Percentage3.equalsIgnoreCase(""))
                            edit_per3.setText(Percentage3);
                        else
                            edit_per3.setText("");

                        Flammable_Bottom = jorder.getString("FlammableBottom");
                        if (!Flammable_Bottom.equalsIgnoreCase(""))
                            edit_bottom1.setText(Flammable_Bottom);
                        else
                            edit_bottom1.setText("");

                        Authorize3id = jorder.getString("FlammablecheckedBy");
                        int authorize3pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (Authorize3id.equalsIgnoreCase(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                authorize3pos = j;
                            }
                        }
                        if (authorize3pos != -1)
                            txt_authorize3.setText(authorizedPersonArrayList.get(authorize3pos).getAuthorizename());
                        else
                            txt_authorize3.setText("");

                        Flammable_Date = jorder.getString("FlammableDate");
                        btn_confined_date.setText(Flammable_Date);


                        Oxygen_Top = jorder.getString("OxygenTop");
                        if (!Oxygen_Top.equalsIgnoreCase(""))
                            edit_top2.setText(Oxygen_Top);
                        else
                            edit_top2.setText("");

                        Oxygen_centre = jorder.getString("OxygenCentre");
                        if (!Oxygen_centre.equalsIgnoreCase(""))
                            edit_centre2.setText(Oxygen_centre);
                        else
                            edit_centre2.setText("");

                        Percentage2 = jorder.getString("Oxygen1stpoint");
                        if (!Percentage2.equalsIgnoreCase(""))
                            edit_per2.setText(Percentage2);
                        else
                            edit_per2.setText("");

                        Percentage4 = jorder.getString("Oxygen2ndpoint");
                        if (!Percentage4.equalsIgnoreCase(""))
                            edit_per4.setText(Percentage4);
                        else
                            edit_per4.setText("");

                        Oxygen_Bottom = jorder.getString("OxygenBottom");
                        if (!Oxygen_Bottom.equalsIgnoreCase(""))
                            edit_bottom2.setText(Oxygen_Bottom);
                        else
                            edit_bottom2.setText("");

                        Authorize4id = jorder.getString("OxygencheckedBy");
                        int authorize4pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (Authorize4id.equalsIgnoreCase(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                authorize4pos = j;
                            }
                        }
                        if (authorize4pos != -1)
                            txt_authorize4.setText(authorizedPersonArrayList.get(authorize4pos).getAuthorizename());
                        else
                            txt_authorize4.setText("Select");

                        Oxygen_Date = jorder.getString("OxygenDate");
                        btn_confined_date1.setText(Oxygen_Date);

                        Toxic_Top = jorder.getString("ToxicTop");
                        edit_top3.setText(Toxic_Top);

                        Toxic_Gasname1 = jorder.getString("ToxicTopGasName");
                        if (!Toxic_Gasname1.equalsIgnoreCase(""))
                            edit_gasname1.setText(Toxic_Gasname1);
                        else
                            edit_gasname1.setText("");

                        Toxic_Centre = jorder.getString("ToxicCentre");
                        if (!Toxic_Centre.equalsIgnoreCase(""))
                            edit_centre3.setText(Toxic_Centre);
                        else
                            edit_centre3.setText("");

                        Toxic_Gasname2 = jorder.getString("ToxicCentreGasName");
                        if (!Toxic_Gasname2.equalsIgnoreCase(""))
                            edit_gasname2.setText(Toxic_Gasname2);
                        else
                            edit_gasname2.setText("");

                        Toxic_Percentage1 = jorder.getString("Toxic1stpoint");
                        if (!Toxic_Percentage1.equalsIgnoreCase(""))
                            edit_per5.setText(Toxic_Percentage1);
                        else
                            edit_per5.setText("");

                        Toxic_Percentage2 = jorder.getString("Toxic2ndpoint");
                        if (!Toxic_Percentage2.equalsIgnoreCase(""))
                            edit_per6.setText(Toxic_Percentage2);
                        else
                            edit_per6.setText("");

                        Toxic_Gasname3 = jorder.getString("Toxic1stpointGasName");
                        if (!Toxic_Gasname3.equalsIgnoreCase(""))
                            edit_gasname3.setText(Toxic_Gasname3);
                        else
                            edit_gasname3.setText("");

                        Toxic_Gasname4 = jorder.getString("Toxic2ndpointGasName");
                        if (!Toxic_Gasname4.equalsIgnoreCase(""))
                            edit_gasname4.setText(Toxic_Gasname4);
                        else
                            edit_gasname4.setText("");

                        Toxic_bottom = jorder.getString("ToxicBottom");
                        if (!Toxic_bottom.equalsIgnoreCase(""))
                            edit_per_bottom.setText(Toxic_bottom);
                        else
                            edit_per_bottom.setText("");

                        Toxic_Gasname5 = jorder.getString("ToxicBottomGasName");
                        if (!Toxic_Gasname5.equalsIgnoreCase(""))
                            edit_gasname5.setText(Toxic_Gasname5);
                        else
                            edit_gasname5.setText("");

                        Toxic_Date = jorder.getString("ToxicDate");
                        btn_confined_date2.setText(Toxic_Date);

                        authorize5 = jorder.getString("ToxiccheckedBy");
                        int authorize5pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (authorize5.equalsIgnoreCase(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                authorize5pos = j;
                            }
                        }
                        if (authorize5pos != -1)
                            txt_authorize5.setText(authorizedPersonArrayList.get(authorize5pos).getAuthorizename());
                        else
                            txt_authorize5.setText(authorizedPersonArrayList.get(0).getAuthorizename());

                        gen_condtn = jorder.getString("GeneralCondition");/* "chetana,sayali,suyog,vritti,pradnya"*/
                        ;
                        String[] gencdtnlist = new String[safetytools.length()];
                        gencdtnlist = gen_condtn.split(",");
                        for (int j = 0; j < indicateRiskArrayList.size(); j++) {
                            for (int k = 0; k < gencdtnlist.length; k++) {
                                if (gencdtnlist[k].equals(indicateRiskArrayList.get(j).getPKQuesID())) {
                                    int pos = j;
                                    indicateRiskArrayList.get(pos).setSelected(true);
                                    indicateRiskAdapter = new IndicateRiskAdapter(ConfinedSpaceEntryActivity.this, indicateRiskArrayList, Mode, PermitStatus);
                                    list_condition.setAdapter(indicateRiskAdapter);
                                }

                            }
                        }


                        indiviual_list = jorder.getString("IndividualProtection");/* "chetana,sayali,suyog,vritti,pradnya"*/
                        ;
                        String[] indiviuallist = new String[safetytools.length()];
                        indiviuallist = indiviual_list.split(",");
                        for (int j = 0; j < equipmentUseArrayList.size(); j++) {
                            for (int k = 0; k < indiviuallist.length; k++) {
                                if (indiviuallist[k].equals(equipmentUseArrayList.get(j).getPKQuesID())) {
                                    int pos = j;
                                    equipmentUseArrayList.get(pos).setSelected(true);
                                    equipmentAdapter = new EquipmentAdapter(ConfinedSpaceEntryActivity.this, equipmentUseArrayList, Mode, PermitStatus);
                                    list_protection.setAdapter(equipmentAdapter);
                                }

                            }
                        }


                        safety_tool_list = jorder.getString("PersonIdentically");/* "chetana,sayali,suyog,vritti,pradnya"*/
                        ;
                        String[] safetylist = new String[safetytools.length()];
                        safetylist = safety_tool_list.split(",");
                        for (int j = 0; j < preventionArrayList.size(); j++) {
                            for (int k = 0; k < safetylist.length; k++) {
                                if (safetylist[k].equals(preventionArrayList.get(j).getPKQuesID())) {
                                    int pos = j;
                                    preventionArrayList.get(pos).setSelected(true);
                                    prevenionAdapter = new PrevenionAdapter(ConfinedSpaceEntryActivity.this, preventionArrayList, Mode, PermitStatus);
                                    list_buddy_safety.setAdapter(prevenionAdapter);
                                }

                            }
                        }


                        safetytools = jorder.getString("SafetyToolMasterId");/* "chetana,sayali,suyog,vritti,pradnya"*/
                        ;
                        String[] safetytoolslist = new String[safetytools.length()];
                        safetytoolslist = safetytools.split(",");
                        for (int j = 0; j < safetyToolsArrayList.size(); j++) {
                            for (int k = 0; k < safetytoolslist.length; k++) {
                                if (safetytoolslist[k].equals(safetyToolsArrayList.get(j).getSafetyToolMasterId())) {
                                    int pos = j;
                                    safetyToolsArrayList.get(pos).setSelected(true);
                                    safetyAdapter = new SafetyAdapter(ConfinedSpaceEntryActivity.this, safetyToolsArrayList, "CSE", Mode, PermitStatus);
                                    grid_safety.setAdapter(safetyAdapter);
                                }

                            }
                        }


                        Authorizeid = jorder.getString("AuthorizeBy1");
                        int authorizepos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (Authorizeid.equalsIgnoreCase(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                authorizepos = j;
                            }
                        }
                        if (authorizepos != -1)
                            txt_authorize.setText(authorizedPersonArrayList.get(authorizepos).getAuthorizename());
                        else
                            txt_authorize.setText("Select");

                        Authorize1id = jorder.getString("AuthorizeBy2");
                        int authorize1pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (Authorize1id.equalsIgnoreCase(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                authorize1pos = j;
                            }
                        }
                        if (authorize1pos != -1)
                            txt_authorize1.setText(authorizedPersonArrayList.get(authorize1pos).getAuthorizename());
                        else
                            txt_authorize1.setText("Select");


                        //spinneer 6

                        Authorize6id = jorder.getString("AuthorizeBy3");
                        int authorize6pos = -1;
                        if (Authorize6id.equalsIgnoreCase("")) {

                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (Authorize6id.equalsIgnoreCase(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                    authorize6pos = j;
                                }
                            }
                        }

                        if (authorize6pos != -1)
                            txt_authorize6.setText(authorizedPersonArrayList.get(authorize6pos).getAuthorizename());
                        else
                            txt_authorize6.setText("Select");

                        Authorize7id = jorder.getString("AuthorizeBy4");
                        int authorize7pos = -1;
                        if (Authorize7id.equalsIgnoreCase("")) {

                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (Authorize7id.equalsIgnoreCase(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                    authorize7pos = j;
                                }
                            }
                        }

                        if (authorize7pos != -1)
                            txt_authorize7.setText(authorizedPersonArrayList.get(authorize7pos).getAuthorizename());
                        else
                            txt_authorize7.setText("Select");


                        //  btn_confined_date3.set


                        PermitclosedId = jorder.getString("PermitcloseBy");
                        int permitpos = -1;

                        if (PermitclosedId.equalsIgnoreCase("")) {

                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (PermitclosedId.equalsIgnoreCase(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                    permitpos = j;

                                }
                            }
                        }


                        if (permitpos != -1)
                            txt_permit_closed.setText(authorizedPersonArrayList.get(permitpos).getAuthorizename());
                        else
                            txt_permit_closed.setText("Select");

                        spotcheck = jorder.getString("SpotCheckBy");
                        int spotcheckpos = -1;

                        if (spotcheck.equalsIgnoreCase("")) {

                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (spotcheck.equalsIgnoreCase(authorizedPersonArrayList.get(spotcheckpos).getAuthorizeid())) {
                                    spotcheckpos = j;
                                }
                            }
                        }

                        if (spotcheckpos != -1)
                            txt_spot_check.setText(authorizedPersonArrayList.get(spotcheckpos).getAuthorizename());
                        else
                            txt_spot_check.setText("Select");


                      /*  if(PermitclosedId.equals("") || spotcheck.equals("")){
                            btn_submit.setVisibility(View.GONE);
                        }else{
                            btn_submit.setVisibility(View.VISIBLE);
                        }*/

                        PermitClosed_Date = jorder.getString("PermitcloseDate");
                        btn_confined_date8.setText(PermitClosed_Date);

                        spotdate = jorder.getString("SpotCheckDate");
                        btn_confined_date9.setText(spotdate);


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

                        /*Authorize1date = jorder.getString("");
                        btn_confined_date3.setText(Authorize1date);

                        Authorize2date = jorder.getString("");
                        btn_confined_date4.setText(Authorize2date);

                        Authorize3date = jorder.getString("");
                        btn_confined_date5.setText(Authorize3date);

                        Authorize4date = jorder.getString("");
                        btn_confined_date6.setText(Authorize4date);

                        Authorize5date = jorder.getString("");
                        btn_confined_date7.setText(Authorize5date);*/

                        Remarks = jorder.getString("PermitcloseRemark");
                        if (!Remarks.equalsIgnoreCase(""))
                            edit_remark.setText(Remarks);
                        else
                            edit_remark.setText("");

                        safetyTools_Others = jorder.getString("CSE_ProOthers");

                        if (safetyToolsArrayList != null) {
                            for (int j = 0; j < safetyToolsArrayList.size(); j++) {
                                if (safetyToolsArrayList.get(j).getSafetyToolDesc().equals("Other(s)")) {
                                    safetyToolsArrayList.get(j).setRemarks(safetyTools_Others);
                                    safetyToolsArrayList.get(j).isSelected();
                                }
                            }
                        }


                        buddy_Others = jorder.getString("CSE_BuddyOthers");
                        if (preventionArrayList != null) {
                            for (int j = 0; j < preventionArrayList.size(); j++) {
                                if (preventionArrayList.get(j).getSelectionText().equalsIgnoreCase("Other(s)")) {
                                    preventionArrayList.get(j).setRemarks(buddy_Others);
                                    preventionArrayList.get(j).isSelected();
                                }
                            }

                        }
                        individual_Others = jorder.getString("CSE_IndiOthers");

                        if (equipmentUseArrayList != null) {
                            for (int j = 0; j < equipmentUseArrayList.size(); j++) {
                                if (equipmentUseArrayList.get(j).getSelectionText().equalsIgnoreCase("Other(s)")) {
                                    equipmentUseArrayList.get(j).setRemarks(individual_Others);
                                    equipmentUseArrayList.get(j).isSelected();
                                }
                            }

                        }

                      /*  "CSE_BuddyOthers": "jitendra",
                                "CSE_ProOthers": "sayali",
                                "CSE_IndiOthers": "Atharva"*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveactivityjson() {

        StartDate = btn_fromdate.getText().toString();
        fromtime = btn_fromtime.getText().toString();
        totime = btn_totime.getText().toString();
        //permitDate = btn_confined_date8.getText().toString();
        Flammable_Top = edit_top1.getText().toString();
        Flammable_Centre = edit_centre1.getText().toString();
        Percentage1 = edit_per1.getText().toString();
        Percentage3 = edit_per3.getText().toString();
        Flammable_Bottom = edit_bottom1.getText().toString();
        Flammable_Date = btn_confined_date.getText().toString();
        Oxygen_Top = edit_top2.getText().toString();
        Oxygen_centre = edit_centre2.getText().toString();
        Percentage2 = edit_per2.getText().toString();
        Percentage4 = edit_per4.getText().toString();
        Oxygen_Bottom = edit_bottom2.getText().toString();
        Oxygen_Date = btn_confined_date1.getText().toString();
        Toxic_Top = edit_top3.getText().toString();
        Toxic_Centre = edit_centre3.getText().toString();
        Toxic_Percentage1 = edit_per5.getText().toString();
        Toxic_Percentage2 = edit_per6.getText().toString();
        Toxic_bottom = edit_per_bottom.getText().toString();
        Toxic_Date = btn_confined_date2.getText().toString();
        Toxic_Gasname1 = edit_gasname1.getText().toString();
        Toxic_Gasname2 = edit_gasname2.getText().toString();
        Toxic_Gasname3 = edit_gasname3.getText().toString();
        Toxic_Gasname4 = edit_gasname4.getText().toString();
        Toxic_Gasname5 = edit_gasname5.getText().toString();
        Authorize1date = btn_confined_date3.getText().toString();
        Authorize2date = btn_confined_date4.getText().toString();
        Authorize3date = btn_confined_date5.getText().toString();
        Authorize4date = btn_confined_date6.getText().toString();
        Authorize5date = btn_confined_date7.getText().toString();
        PermitClosed_Date = btn_confined_date8.getText().toString();
        spotdate = btn_confined_date9.getText().toString();
        Remarks = edit_remark.getText().toString();

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
                    String Safety = indicateRiskArrayList.get(i).getPKQuesID();
                    user[i] = Safety.toString();
                    gen_condtn = TextUtils.join(",", user);

                }

            }
        }


        equipmentUseArrayList = equipmentAdapter.getArrayList();

        if (equipmentUseArrayList.size() > 0) {
            if (equipmentUseArrayList.size() > 0) {
                user1 = new String[equipmentUseArrayList.size()];
                for (int i = 0; i < equipmentUseArrayList.size(); i++) {
                    String Safety = equipmentUseArrayList.get(i).getPKQuesID();
                    user1[i] = Safety.toString();
                    indiviual_list = TextUtils.join(",", user1);
                    if (equipmentUseArrayList.get(i).getSelectionText().equalsIgnoreCase("Other(s) –Specify") ||
                            equipmentUseArrayList.get(i).getSelectionText().equalsIgnoreCase("Other(s)")) {
                        if (equipmentUseArrayList.get(i).getRemarks() != null) {
                            individual_Others = equipmentUseArrayList.get(i).getRemarks();
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
                    String Safety = preventionArrayList.get(i).getPKQuesID();
                    user2[i] = Safety.toString();
                    safety_tool_list = TextUtils.join(",", user2);
                    if (preventionArrayList.get(i).getSelectionText().equalsIgnoreCase("Other(s) –Specify") ||
                            preventionArrayList.get(i).getSelectionText().equalsIgnoreCase("Other(s)")) {
                        if (preventionArrayList.get(i).getRemarks() != null) {
                            buddy_Others = preventionArrayList.get(i).getRemarks();
                        }
                    }
                }

            }
        }


        safetyToolsArrayList = safetyAdapter.getArrayList();

        if (safetyToolsArrayList.size() > 0) {
            if (safetyToolsArrayList.size() > 0) {
                user3 = new String[safetyToolsArrayList.size()];
                for (int i = 0; i < safetyToolsArrayList.size(); i++) {
                    String Safety = safetyToolsArrayList.get(i).getSafetyToolMasterId();
                    user3[i] = Safety.toString();
                    safetytools = TextUtils.join(",", user3);
                    if (safetyToolsArrayList.get(i).getSafetyToolDesc().equals("Other(s)")) {
                        if (safetyToolsArrayList.get(i).getRemarks() != null) {
                            safetyTools_Others = safetyToolsArrayList.get(i).getRemarks();
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
            Activityjson.put("FkWorkAuthorizationMasterId", WAH_No);
            Activityjson.put("PermitDate", StartDate);
            Activityjson.put("PermitFromTime", fromtime);
            Activityjson.put("PermitToTime", totime);
            Activityjson.put("fkContractorId", contractorId);
            Activityjson.put("fkOperationMasterId", operationId);
            Activityjson.put("NatConfinedSpace", LocationId);
            Activityjson.put("TankDetails", edt_details_tank.getText().toString());
            Activityjson.put("MethodOperationStatus", Checked);
            Activityjson.put("FlammableTop", Flammable_Top);
            Activityjson.put("FlammableCentre", Flammable_Centre);
            Activityjson.put("Flammable1stpoint", Percentage1);
            Activityjson.put("Flammable2ndpoint", Percentage3);
            Activityjson.put("FlammableBottom", Flammable_Bottom);
            Activityjson.put("FlammableDate", Flammable_Date);
            Activityjson.put("FlammablecheckedBy", Authorize3id);
            Activityjson.put("OxygenTop", Oxygen_Top);
            Activityjson.put("OxygenCentre", Oxygen_centre);
            Activityjson.put("Oxygen1stpoint", Percentage2);
            Activityjson.put("Oxygen2ndpoint", Percentage4);
            Activityjson.put("OxygenBottom", Oxygen_Bottom);
            Activityjson.put("OxygenDate", Oxygen_Date);
            Activityjson.put("OxygencheckedBy", Authorize4id);
            Activityjson.put("ToxicTop", Toxic_Top);
            Activityjson.put("ToxicCentre", Toxic_Centre);
            Activityjson.put("Toxic1stpoint", Toxic_Percentage1);
            Activityjson.put("Toxic2ndpoint", Toxic_Percentage2);
            Activityjson.put("ToxicBottom", Toxic_bottom);
            Activityjson.put("ToxicDate", Toxic_Date);
            Activityjson.put("ToxicTopGasName", Toxic_Gasname1);
            Activityjson.put("ToxicCentreGasName", Toxic_Gasname2);
            Activityjson.put("Toxic1stpointGasName", Toxic_Gasname3);
            Activityjson.put("Toxic2ndpointGasName", Toxic_Gasname4);
            Activityjson.put("ToxicBottomGasName", Toxic_Gasname5);
            Activityjson.put("ToxiccheckedBy", authorize5);
            Activityjson.put("GeneralCondition", gen_condtn);
            Activityjson.put("IndividualProtection", indiviual_list);
            Activityjson.put("PersonIdentically", safety_tool_list);
            Activityjson.put("SafetyToolMasterId", safetytools);
            Activityjson.put("AuthorizeBy1", Authorizeid);
            Activityjson.put("AuthorizeBy2", Authorize1id);
            Activityjson.put("RespContractorId", contractorId);
            Activityjson.put("AuthorizeBy3", Authorize6id);
            Activityjson.put("AuthorizeBy4", Authorize7id);
            Activityjson.put("PermitClosedBy", PermitclosedId);
            Activityjson.put("PermitclosedDate", PermitClosed_Date);
            Activityjson.put("SpotCheckBy", spotcheck);
            Activityjson.put("SpotCheckDate", spotdate);
            Activityjson.put("PermitCancelBy", cancelId);
            Activityjson.put("PermitCancelDate", cancelDate);
            /*Activityjson.put("",Authorize1date);
            Activityjson.put("",Authorize2date);
            Activityjson.put("",Authorize3date);
            Activityjson.put("",Authorize4date);
            Activityjson.put("",Authorize5date);*/
            Activityjson.put("Remarks", Remarks);
            Activityjson.put("CSE_BuddyOthers", buddy_Others);
            Activityjson.put("CSE_ProOthers", safetyTools_Others);
            Activityjson.put("CSE_IndiOthers", individual_Others);
            Activityjson.put("GoldenRulesId", goldenRulesList);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String FinalJsonObject = Activityjson.toString();
       /* String URL = CompanyURL+ WebAPIUrl.api_PostConfinedSpaceEntry;
        String op = "Success";*/

        if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
            showProgress();
            new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
                @Override

                public void callMethod() {
                    new DownloadPostData().execute(FinalJsonObject);
                }

                @Override
                public void callfailMethod(String msg) {
                    CommonClass.displayToast(ConfinedSpaceEntryActivity.this, msg);
                    dismissProgress();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }


    }

    public void editjson() {

        StartDate = btn_fromdate.getText().toString();
        fromtime = btn_fromtime.getText().toString();
        totime = btn_totime.getText().toString();
        //permitDate = btn_confined_date8.getText().toString();
        spotdate = btn_confined_date9.getText().toString();
        cancelDate = btn_cancel_date.getText().toString();
        Flammable_Top = edit_top1.getText().toString();
        Flammable_Centre = edit_centre1.getText().toString();
        Percentage1 = edit_per1.getText().toString();
        Percentage3 = edit_per3.getText().toString();
        Flammable_Bottom = edit_bottom1.getText().toString();
        Flammable_Date = btn_confined_date.getText().toString();
        Oxygen_Top = edit_top2.getText().toString();
        Oxygen_centre = edit_centre2.getText().toString();
        Percentage2 = edit_per2.getText().toString();
        Percentage4 = edit_per4.getText().toString();
        Oxygen_Bottom = edit_bottom2.getText().toString();
        Oxygen_Date = btn_confined_date1.getText().toString();
        Toxic_Top = edit_top3.getText().toString();
        Toxic_Centre = edit_centre3.getText().toString();
        Toxic_Percentage1 = edit_per5.getText().toString();
        Toxic_Percentage2 = edit_per6.getText().toString();
        Toxic_bottom = edit_per_bottom.getText().toString();
        Toxic_Date = btn_confined_date2.getText().toString();
        Toxic_Gasname1 = edit_gasname1.getText().toString();
        Toxic_Gasname2 = edit_gasname2.getText().toString();
        Toxic_Gasname3 = edit_gasname3.getText().toString();
        Toxic_Gasname4 = edit_gasname4.getText().toString();
        Toxic_Gasname5 = edit_gasname5.getText().toString();
        PermitClosed_Date = btn_confined_date8.getText().toString();
        Remarks = edit_remark.getText().toString();

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
                    String Safety = indicateRiskArrayList.get(i).getPKQuesID();
                    user[i] = Safety.toString();
                    gen_condtn = TextUtils.join(",", user);

                }

            }
        }


        equipmentUseArrayList = equipmentAdapter.getArrayList();

        if (equipmentUseArrayList.size() > 0) {
            if (equipmentUseArrayList.size() > 0) {
                user1 = new String[equipmentUseArrayList.size()];
                for (int i = 0; i < equipmentUseArrayList.size(); i++) {
                    String Safety = equipmentUseArrayList.get(i).getPKQuesID();
                    user1[i] = Safety.toString();
                    indiviual_list = TextUtils.join(",", user1);
                    if (equipmentUseArrayList.get(i).getSelectionText().equalsIgnoreCase("Other(s) –Specify") ||
                            equipmentUseArrayList.get(i).getSelectionText().equalsIgnoreCase("Other(s)")) {
                        if (equipmentUseArrayList.get(i).getRemarks() != null) {
                            individual_Others = equipmentUseArrayList.get(i).getRemarks();
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
                    String Safety = preventionArrayList.get(i).getPKQuesID();
                    user2[i] = Safety.toString();
                    safety_tool_list = TextUtils.join(",", user2);
                    if (preventionArrayList.get(i).getSelectionText().equalsIgnoreCase("Other(s) –Specify") ||
                            preventionArrayList.get(i).getSelectionText().equalsIgnoreCase("Other(s)")) {
                        if (preventionArrayList.get(i).getRemarks() != null) {
                            buddy_Others = preventionArrayList.get(i).getRemarks();
                        }
                    }
                }

            }
        }


        safetyToolsArrayList = safetyAdapter.getArrayList();

        if (safetyToolsArrayList.size() > 0) {
            if (safetyToolsArrayList.size() > 0) {
                user3 = new String[safetyToolsArrayList.size()];
                for (int i = 0; i < safetyToolsArrayList.size(); i++) {
                    String Safety = safetyToolsArrayList.get(i).getSafetyToolMasterId();
                    user3[i] = Safety.toString();
                    safetytools = TextUtils.join(",", user3);
                    if (safetyToolsArrayList.get(i).getSafetyToolDesc().equals("Other(s)")) {
                        if (safetyToolsArrayList.get(i).getRemarks() != null) {
                            safetyTools_Others = safetyToolsArrayList.get(i).getRemarks();
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
            Activityjson.put("FkWorkAuthorizationMasterId", WAH_No);
            Activityjson.put("PermitDate", StartDate);
            Activityjson.put("PermitFromTime", fromtime);
            Activityjson.put("PermitToTime", totime);
            Activityjson.put("fkContractorId", contractorId);
            Activityjson.put("fkOperationMasterId", operationId);
            Activityjson.put("NatConfinedSpace", LocationId);
            Activityjson.put("TankDetails", edt_details_tank.getText().toString());
            Activityjson.put("MethodOperationStatus", Checked);
            Activityjson.put("FlammableTop", Flammable_Top);
            Activityjson.put("FlammableCentre", Flammable_Centre);
            Activityjson.put("Flammable1stpoint", Percentage1);
            Activityjson.put("Flammable2ndpoint", Percentage3);
            Activityjson.put("FlammableBottom", Flammable_Bottom);
            Activityjson.put("FlammableDate", Flammable_Date);
            Activityjson.put("FlammablecheckedBy", Authorize3id);
            Activityjson.put("OxygenTop", Oxygen_Top);
            Activityjson.put("OxygenCentre", Oxygen_centre);
            Activityjson.put("Oxygen1stpoint", Percentage2);
            Activityjson.put("Oxygen2ndpoint", Percentage4);
            Activityjson.put("OxygenBottom", Oxygen_Bottom);
            Activityjson.put("OxygenDate", Oxygen_Date);
            Activityjson.put("OxygencheckedBy", Authorize4id);
            Activityjson.put("ToxicTop", Toxic_Top);
            Activityjson.put("ToxicCentre", Toxic_Centre);
            Activityjson.put("Toxic1stpoint", Toxic_Percentage1);
            Activityjson.put("Toxic2ndpoint", Toxic_Percentage2);
            Activityjson.put("ToxicBottom", Toxic_bottom);
            Activityjson.put("ToxicDate", Toxic_Date);
            Activityjson.put("ToxicTopGasName", Toxic_Gasname1);
            Activityjson.put("ToxicCentreGasName", Toxic_Gasname2);
            Activityjson.put("Toxic1stpointGasName", Toxic_Gasname3);
            Activityjson.put("Toxic2ndpointGasName", Toxic_Gasname4);
            Activityjson.put("ToxicBottomGasName", Toxic_Gasname5);
            Activityjson.put("ToxiccheckedBy", authorize5);
            Activityjson.put("GeneralCondition", gen_condtn);
            Activityjson.put("IndividualProtection", indiviual_list);
            Activityjson.put("PersonIdentically", safety_tool_list);
            Activityjson.put("SafetyToolMasterId", safetytools);
            Activityjson.put("AuthorizeBy1", Authorizeid);
            Activityjson.put("AuthorizeBy2", Authorize1id);
            Activityjson.put("RespContractorId", contractorId);
            Activityjson.put("AuthorizeBy3", Authorize6id);
            Activityjson.put("AuthorizeBy4", Authorize7id);
            Activityjson.put("PermitClosedBy", PermitclosedId);
            Activityjson.put("PermitclosedDate", PermitClosed_Date);
            Activityjson.put("SpotCheckBy", spotcheck);
            Activityjson.put("SpotCheckDate", spotdate);
            Activityjson.put("PermitCancelBy", cancelId);
            Activityjson.put("PermitCancelDate", cancelDate);
            Activityjson.put("Remarks", Remarks);
            Activityjson.put("CSE_BuddyOthers", buddy_Others);
            Activityjson.put("CSE_ProOthers", safetyTools_Others);
            Activityjson.put("CSE_IndiOthers", individual_Others);
            Activityjson.put("GoldenRulesId", goldenRulesList);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String FinalEditJsonObject = Activityjson.toString();
       /* String URL = CompanyURL+ WebAPIUrl.api_PostConfinedSpaceEntry;
        String op = "Success";*/

        if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
            showProgress();
            new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadEditPostData().execute(FinalEditJsonObject);
                }

                @Override
                public void callfailMethod(String msg) {
                    CommonClass.displayToast(ConfinedSpaceEntryActivity.this, msg);
                    dismissProgress();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
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

                if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
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


    }

    private void dismissProgress() {

        mprogress.setVisibility(View.GONE);

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
            String id = params[0];
            String url = CompanyURL + WebAPIUrl.api_GetApproverPerson;

            try {
                res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);
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
                //Toast.makeText(ConfinedSpaceEntryActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(authorizedPersonArrayList);
                editor.putString("Authorized", json);
                editor.commit();
                authorizedPersonAdapter = new AuthorizedPersonAdapter(ConfinedSpaceEntryActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                //spinner_authorize3.setAdapter(authorizedPersonAdapter);
                //spinner_authorize5.setAdapter(authorizedPersonAdapter);
                /*spinner_authorize4.setAdapter(authorizedPersonAdapter);
                spinner_authorize1.setAdapter(authorizedPersonAdapter);
                spinner_authorize6.setAdapter(authorizedPersonAdapter);
                spinner_authorize7.setAdapter(authorizedPersonAdapter);
                spinner_spotcheck.setAdapter(authorizedPersonAdapter);*/
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
                res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);
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
                // Toast.makeText(ConfinedSpaceEntryActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(contractorListActivityArrayList);
                editor.putString("Contractor", json);
                editor.commit();
                permitContractorListAdapter = new PermitContractorListAdapter(ConfinedSpaceEntryActivity.this, contractorListActivityArrayList);
                // spinner_authorize.setAdapter(permitContractorListAdapter);
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


                if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                    showProgress();
                    new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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

                if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                    showProgress();
                    new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                    showProgress();
                    new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                    showProgress();
                    new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                    showProgress();
                    new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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

                if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                    showProgress();
                    new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                    showProgress();
                    new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadConfineListData().execute();
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
            url = CompanyURL + WebAPIUrl.api_GetCSEPermitNo;

            res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);

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
                res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);
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
                // Toast.makeText(ConfinedSpaceEntryActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(depotArrayList);
                editor.putString("Depot", json);
                editor.commit();
                depotAdapter = new DepotAdapter(ConfinedSpaceEntryActivity.this, depotArrayList);
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
                res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);
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
                // Toast.makeText(ConfinedSpaceEntryActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(operationArrayList);
                editor.putString("Operation", json);
                editor.commit();
                operationAdapter = new OperationAdapter(ConfinedSpaceEntryActivity.this, operationArrayList);
                spinner_operation.setAdapter(operationAdapter);
            }


        }
    }

    class DownloadTankDetails extends AsyncTask<String, Void, String> {
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
                res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    contractorListActivityArrayList = new ArrayList<>();
                    contractorListActivityArrayList.clear();
                    ContractorList contractorList = new ContractorList();
                    contractorList.setCustVendorMasterId("--Select--");
                    contractorList.setCustVendorName("--Select--");
                    contractorListActivityArrayList.add(0, contractorList);

                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        contractorList = new ContractorList();
                        JSONObject jorder = jResults.getJSONObject(i);

                        contractorList.setCustVendorMasterId(jorder.getString("CustVendorMasterId"));
                        contractorList.setCustVendorName(jorder.getString("CustVendorName"));
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
                //  Toast.makeText(ConfinedSpaceEntryActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(contractorListActivityArrayList);
                editor.putString("Contractor", json);
                editor.commit();
                permitContractorListAdapter = new PermitContractorListAdapter(ConfinedSpaceEntryActivity.this, contractorListActivityArrayList);
                //spinner_authorize2.setAdapter(permitContractorListAdapter);
                // spinner_details_tank.setAdapter(permitContractorListAdapter);


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
                res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);
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
                locationOperationAdapter = new LocationOperationAdapter(ConfinedSpaceEntryActivity.this, LocationArraylist);
                spinner_confined_space.setAdapter(locationOperationAdapter);
                // Toast.makeText(ConfinedSpaceEntryActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                locationOperationAdapter = new LocationOperationAdapter(ConfinedSpaceEntryActivity.this, LocationArraylist);
                spinner_confined_space.setAdapter(locationOperationAdapter);

                // if (Mode.equalsIgnoreCase("E")) {

                if (LocationId != "") {
                    int locationpos = -1;
                    for (int j = 0; j < LocationArraylist.size(); j++) {
                        if (LocationId.equalsIgnoreCase(LocationArraylist.get(j).getLocationMasterId())) {
                            locationpos = j;
                            break;
                        }
                    }
                    if (locationpos != -1)
                        spinner_confined_space.setSelection(locationpos);
                    else
                        spinner_confined_space.setSelection(0);
                    //  }

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
                    ConfinedSpaceEntryActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dismissProgress();
                            Toast.makeText(ConfinedSpaceEntryActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();

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
                        ConfinedSpaceEntryActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dismissProgress();
                                Toast.makeText(ConfinedSpaceEntryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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
                res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);
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
                //  Toast.makeText(ConfinedSpaceEntryActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(safetyToolsArrayList);
                editor.putString("safety", json);
                editor.commit();
                safetyAdapter = new SafetyAdapter(ConfinedSpaceEntryActivity.this, safetyToolsArrayList, "CSE", Mode, PermitStatus);
                grid_safety.setAdapter(safetyAdapter);


            }


        }
    }

    class DownloadConfineListData extends AsyncTask<String, Void, String> {
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
                res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);
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

                        if (QuesCode.equalsIgnoreCase("GenralCondition")) {
                            indicateRisk.setPKQuesID(jorder.getString("PKQuesID"));
                            indicateRisk.setQuesText(jorder.getString("QuesText"));
                            indicateRisk.setSelectionText(jorder.getString("SelectionText"));
                            indicateRisk.setSelectionValue(jorder.getString("SelectionValue"));
                            indicateRisk.setQuesCode(jorder.getString("QuesCode"));

                            indicateRiskArrayList.add(indicateRisk);

                        }
                        if (QuesCode.equalsIgnoreCase("Individualprotection")) {
                            equipmentUse.setPKQuesID(jorder.getString("PKQuesID"));
                            equipmentUse.setQuesText(jorder.getString("QuesText"));
                            equipmentUse.setSelectionText(jorder.getString("SelectionText"));
                            equipmentUse.setSelectionValue(jorder.getString("SelectionValue"));
                            equipmentUse.setQuesCode(jorder.getString("QuesCode"));
                            equipmentUseArrayList.add(equipmentUse);


                        }
                        if (QuesCode.equalsIgnoreCase("Personidentically")) {
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

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {
                dismissProgress();
                //  Toast.makeText(ConfinedSpaceEntryActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                gson = new Gson();

                String json = gson.toJson(indicateRiskArrayList);
                String json1 = gson.toJson(equipmentUseArrayList);
                String json2 = gson.toJson(preventionArrayList);
                editor.putString("general", json);
                editor.putString("protection", json1);
                editor.putString("equipped", json2);
                editor.commit();
                indicateRiskAdapter = new IndicateRiskAdapter(ConfinedSpaceEntryActivity.this, indicateRiskArrayList, Mode, PermitStatus);
                list_condition.setAdapter(indicateRiskAdapter);
                Utility.setListViewHeightBasedOnItems(list_condition);

                equipmentAdapter = new EquipmentAdapter(ConfinedSpaceEntryActivity.this, equipmentUseArrayList, Mode, PermitStatus);
                list_protection.setAdapter(equipmentAdapter);
                Utility.setListViewHeightBasedOnItems(list_protection);

                prevenionAdapter = new PrevenionAdapter(ConfinedSpaceEntryActivity.this, preventionArrayList, Mode, PermitStatus);
                list_buddy_safety.setAdapter(prevenionAdapter);
                Utility.setListViewHeightBasedOnItems(list_buddy_safety);


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
                String url = CompanyURL + WebAPIUrl.api_PostConfinedSpaceEntry;
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
                Toast.makeText(ConfinedSpaceEntryActivity.this, "Data saved successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ConfinedSpaceEntryActivity.this, SupervisorMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                //Intent intent = new Intent(AssignActivity.this, ActivityMain.class);
                //startActivity(intent);
            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {
                Toast.makeText(ConfinedSpaceEntryActivity.this, "Data not save successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ConfinedSpaceEntryActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
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
                String url = CompanyURL + WebAPIUrl.api_PosteditConfined;
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

            //if (val.contains("Success")) {//Success
            if (val.contains("false")) {//
                Toast.makeText(ConfinedSpaceEntryActivity.this, "Data Updated Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ConfinedSpaceEntryActivity.this, PermitStatusActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                //startActivity(new Intent(ConfinedSpaceEntryActivity.this,ConfinedSpaceEntryActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                //Intent intent = new Intent(AssignActivity.this, ActivityMain.class);
                //startActivity(intent);
            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {
                Toast.makeText(ConfinedSpaceEntryActivity.this, "Data not updated successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ConfinedSpaceEntryActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
            }
        }
    }

    class DownloadIsValidUser extends AsyncTask<String, Void, String> {
        String res;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ConfinedSpaceEntryActivity.this);
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
                        URLEncoder.encode("z207", "UTF-8") + "&PlantId=" +
                        URLEncoder.encode("1", "UTF-8") + "&UserLoginId=" +
                        URLEncoder.encode(id, "UTF-8") + "&UserPwd=" + URLEncoder.encode(Password, "UTF-8");
                res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);
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
                Toast.makeText(ConfinedSpaceEntryActivity.this, "Valid Password", Toast.LENGTH_SHORT).show();
                //b.dismiss();
               /* if(ReasonVal.equals("1") || ReasonVal.equals("2") && Mode.equals("E")){
                    btn_submit.setVisibility(View.VISIBLE);
                }*/

                custDialog1.setVisibility(View.GONE);
                custDialog2.setVisibility(View.GONE);
                spinner_authorize.setSelection(0);
                spinner_permit_closed.setSelection(0);
                edt_password_pass.setText("");
                edt_password_reason.setText("");
                edt_reason.setText("");
                hideKeyboard(ConfinedSpaceEntryActivity.this);
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


    /***************************Not Used***************************/

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

    public class DownloadWANo extends AsyncTask<String, Void, String> {
        String res = "", response = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String Id = params[0];

            String url = CompanyURL + WebAPIUrl.api_GetWANo + "?contractid=" + Id + "&permitOperationcode=CSE";


            res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);
            response = res;


            return response;
        }


        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);

            if (!resp.equals("[]")) {
                WAArayList.clear();

                try {
                    JSONArray jsonArray = new JSONArray(resp);

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


                if (WAArayList.size() != 0) {
                    ContractorPermitAdapter contractorPermitAdapter = new ContractorPermitAdapter(ConfinedSpaceEntryActivity.this, WAArayList);
                    spinner_prevention_plan.setAdapter(contractorPermitAdapter);
                    //spinner_permit_closed.setAdapter(authorizedPersonAdapter);


                    if (WAArayList.size() != 0) {
                        ArrayAdapter<PermitNoWA> permitNoArrayAdapter = new ArrayAdapter<>(ConfinedSpaceEntryActivity.this,
                                android.R.layout.simple_spinner_item, WAArayList);
                        spinner_prevention_plan.setAdapter(permitNoArrayAdapter);
                        spinner_contractor.setEnabled(false);
                        //spinner_permit_closed.setAdapter(authorizedPersonAdapter);
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
                        if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                            showProgress();
                            //Location Get
                            new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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
                            Toast.makeText(ConfinedSpaceEntryActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        spinner_contractor.setEnabled(false);
                        spinner_prevention_plan.setSelection(0);
                        //spinner_prevention_plan.setSelection(0);
                    }


                } else {
                    WAArayList.clear();
                    if (Mode.equals("E")) {
                        if (WAH_No != "") {
                            PermitNoWA permitNo = new PermitNoWA();
                            permitNo.setPermitNo(WAH_No);
                            WAArayList.add(0, permitNo);
                            ArrayAdapter<PermitNoWA> permitNoArrayAdapter = new ArrayAdapter<>(ConfinedSpaceEntryActivity.this,
                                    android.R.layout.simple_spinner_item, WAArayList);
                            spinner_prevention_plan.setAdapter(permitNoArrayAdapter);
                        }
                    } else {
                        Toast.makeText(ConfinedSpaceEntryActivity.this, "No WorkAuthorization Present Against Selected Contractor", Toast.LENGTH_SHORT).show();
                        PermitNoWA permitNo = new PermitNoWA();
                        permitNo.setPermitNo("Select");
                        WAArayList.add(0, permitNo);
                        ArrayAdapter<PermitNoWA> permitNoArrayAdapter = new ArrayAdapter<>(ConfinedSpaceEntryActivity.this,
                                android.R.layout.simple_spinner_item, WAArayList);
                        spinner_prevention_plan.setAdapter(permitNoArrayAdapter);
                        spinner_operation.setSelection(0);
                        spinner_confined_space.setSelection(0);
                    }
                }


            } else {
                WAArayList.clear();
                if (Mode.equals("E")) {
                    if (WAH_No != "") {
                        PermitNoWA permitNo = new PermitNoWA();
                        permitNo.setPermitNo(WAH_No);
                        WAArayList.add(0, permitNo);
                        ArrayAdapter<PermitNoWA> permitNoArrayAdapter = new ArrayAdapter<>(ConfinedSpaceEntryActivity.this,
                                android.R.layout.simple_spinner_item, WAArayList);
                        spinner_prevention_plan.setAdapter(permitNoArrayAdapter);

                    }
                } else {

                    Toast.makeText(ConfinedSpaceEntryActivity.this, "No WorkAuthorization Present Against Selected Contractor", Toast.LENGTH_SHORT).show();
                    PermitNoWA permitNo = new PermitNoWA();
                    permitNo.setPermitNo("Select");
                    WAArayList.add(0, permitNo);
                    ArrayAdapter<PermitNoWA> permitNoArrayAdapter = new ArrayAdapter<>(ConfinedSpaceEntryActivity.this,
                            android.R.layout.simple_spinner_item, WAArayList);
                    spinner_prevention_plan.setAdapter(permitNoArrayAdapter);
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


            res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);
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
                                        Toast.makeText(ConfinedSpaceEntryActivity.this, "You cannot choose time greater than work authorization end time", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            btn_totime.setText(WAEndTime);
                            modeefirst = 1;
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

                        if (!Mode.equals("E")) {
                            try {
                                LocationId = jorder.getString("FkLocationMasterId");
                                //Mode = "E";

                                int locationpos = -1;
                                if (LocationArraylist != null) {
                                    for (int j = 0; j < LocationArraylist.size(); j++) {
                                        if (LocationArraylist.get(j).getLocationMasterId().equals(LocationId)) {
                                            locationpos = j;
                                            break;
                                        }

                                    }
                                    if (locationpos != -1)
                                        spinner_confined_space.setSelection(locationpos);
                                    else
                                        spinner_confined_space.setSelection(0);
                                } else {
                                    if (CommonClass.checkNet(ConfinedSpaceEntryActivity.this)) {
                                        new StartSession(ConfinedSpaceEntryActivity.this, new CallbackInterface() {
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


                    }
                    spinner_contractor.setEnabled(false);
                    spinner_confined_space.setEnabled(false);
                    spinner_operation.setEnabled(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                if (contractorId != "") {
                    spinner_contractor.setEnabled(true);
                }
                //  spinner_location.setEnabled(true);
                //  spinner_operation.setEnabled(true);
                if (operationId == "") {
                    spinner_operation.setSelection(0);
                }

                if (LocationId == "") {
                    spinner_confined_space.setSelection(0);
                }

                Toast.makeText(ConfinedSpaceEntryActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
            }


        }
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
                    authorizedPersonAdapter = new AuthorizedPersonAdapter(ConfinedSpaceEntryActivity.this, txt_authorizeArrayList);
                    if (arrivalFrom.equalsIgnoreCase("fromReason")) {
                        spinner_permit_closed.setAdapter(authorizedPersonAdapter);
                    } else if (arrivalFrom.equalsIgnoreCase("fromPWD")) {
                        spinner_authorize.setAdapter(authorizedPersonAdapter);
                    }
                }
                //authorizedPersonAdapter.updateList(txt_authorizeArrayList);
                // spinner_authorize.setAdapter(authorizedPersonAdapter);
            }

        } else{
            AuthorizedPerson authorizedPerson = new AuthorizedPerson();
            authorizedPerson.setAuthorizename("Select");
            txt_authorizeArrayList.add(authorizedPerson);
            authorizedPersonAdapter = new AuthorizedPersonAdapter(ConfinedSpaceEntryActivity.this, txt_authorizeArrayList);
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
                res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);
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
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ConfinedSpaceEntryActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(goldenRulesArrayList);
                editor.putString("GoldenRules", json);
                editor.commit();
                goldenRuleAdapter = new GoldenRuleAdapter(ConfinedSpaceEntryActivity.this, goldenRulesArrayList, Mode, PermitStatus);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ConfinedSpaceEntryActivity.this);
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
                res = CommonClass.OpenConnection(url, ConfinedSpaceEntryActivity.this);
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

                authorizedPersonAdapter = new AuthorizedPersonAdapter(ConfinedSpaceEntryActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);


            }


        }
    }
}


