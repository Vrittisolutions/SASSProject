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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.adapter.AuthorizedPersonAdapter;
import com.vritti.sass.adapter.DepotAdapter;
import com.vritti.sass.adapter.GoldenRuleAdapter;
import com.vritti.sass.adapter.LocationOperationAdapter;
import com.vritti.sass.adapter.OperationAdapter;
import com.vritti.sass.adapter.PermitContractorListAdapter;
import com.vritti.sass.adapter.SafetyAdapter;
import com.vritti.sass.adapter.SafetyMeasureListAdapter;
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
import com.vritti.sass.model.SafetyMeasure;
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
 * Created by sharvari on 14-Nov-18.
 */

public class HOTWorkActivity extends AppCompatActivity {

    Context mcontext;
    int img_pos;
    String tempImgVal = "";

    Button edt_fromdate, edt_todate;
    Button edt_authorize_date, edt_authorize_date1, edt_authorize_date2;
    Button edt_permit_date, edt_spot_date, edt_cancel_date;
    Button btn_submit, edt_fromtime, edt_totime;
    String to_time = "";
    Spinner spinner_operation, spinner_location, spinner_authorize, spinner_authorize2,
            spinner_permit_closed, spinner_spotcheck, spinner_permitno, spinner_station, spinner_contractor, spinner_prevention_plan;
    EditText edit_remarks, edit_permitno;
    TextView txt_authorize, txt_authorize1, txt_permit_closed, txt_spot_check, txt_cancel;
    String Authorize1Id = "", Authorize2Id = "", PermitclosedId = "", SpotcheckId = "", Authorize1Name = "", Authorize2Name = "",
            PermitClosedName = "", SpotCheckName = "", cancelPermitId = "";

    String Password = "";
    String permitcloseremark = "", permitcloseddate = "", permittotime = "", permitfromtime = "", StartDate = "", Spotcheckdate = "",
            AuthorizeDate1 = "", AuthorizeDate2 = "", AuthorizeDate3 = "", cancelDate = "";
    CheckBox checkbox_method;
    int Year, month, day;
    DatePickerDialog datePickerDialog;
    String date;
    ImageView img_camera, img_camera1;
    int MY_CAMERA_PERMISSION_CODE = 100;
    int MEDIA_TYPE_IMAGE = 1;
    int CAMERA_REQUEST = 101;
    private Uri fileUri;
    File mediaFile;
    private static String IMAGE_DIRECTORY_NAME = "SASS";
    int check = 0;
    JSONObject Activityjson;
    String safetytools = "", safetyradio = "";
    String tempVal = "", ReasonVal = "";
    String safety_BEL = "", safety_extinguisher = "", safety_Others = "", safetyTools_Others = "", goldenRulesList = "";


    SharedPreferences userpreferences;
    AuthorizedPersonAdapter authorizedPersonAdapter;
    PermitContractorListAdapter permitContractorListAdapter;
    OperationAdapter operationAdapter;

    private ArrayList<AuthorizedPerson> authorizedPersonArrayList;
    ArrayList<AuthorizedPerson> txt_authorizeArrayList;
    private ArrayList<ContractorList> contractorListActivityArrayList;
    private ArrayList<Operation> operationArrayList;
    private ArrayList<Location> LocationArraylist;
    private ArrayList<Depot> depotArrayList;
    private ArrayList<PermitNoWA> WAArayList;
    private ArrayList<WANoDetails> waNoDetails;
    LocationOperationAdapter locationOperationAdapter;
    DepotAdapter depotAdapter;

    //String CompanyURL;
    ProgressBar mprogress;
    SharedPreferences sharedPrefs;
    Gson gson;
    String json;
    Type type;
    String PKFormId = "", formcode = "", authorize = "", authorize1 = "", contractorId = "", WAH_No = "",
            PermitClosed = "", SpotCheck = "", NatureOperation = "", LocationId = "", contractorName = "", OperationName = "",
            StationId = "", PermitNo = "", response = "", StationName = "", LocationName = "", contractorname = "", userloginId = "";
    private String serverResponseMessage, path, Imagefilename, checkoperation = "Y", imgAbsolutePath = "",imgAtmosphericPost="";
    GridView grid_safety;
    String SpotCheckimg = "", SpotCheckimgNamePost = "";
    String WAStartTime = "", WAEndTime = "", WAEndTime1 = "";
    int WAEndTimeHr, WAEndTimeMin, WAStartTimeHr, WAStartTimemin;

    SafetyTools safetyTools;
    ArrayList<SafetyTools> safetyToolsArrayList;
    SafetyAdapter safetyAdapter;
    ArrayList<SafetyMeasure> safetyMeasureArrayList;
    boolean isAns;
    SafetyMeasureListAdapter safetyMeasureListAdapter;
    ListView list_safety_measure;
    public String CompanyURL = "http://192.168.1.221";
    String[] user;
    String[] user5;
    Permit permit;
    SimpleDraweeView spot_img_display;

    RadioGroup radio_selection;
    RadioButton rbtnyes, rbtnno, rbtndone;
    private AlertDialog b;
    private ProgressDialog progressDialog;
    private CommonClass cc;
    RelativeLayout cusDailog, cusDialog1;
    Button txt_cancel_pass;
    Button txt_submit_pass;
    EditText edit_password_pass;
    EditText edit_reason, edit_password_reason;
    Button txt_submit_reason, txt_cancel_reason;
    String Mode = "";
    LinearLayout ln_station, ln_contractor, ln_natureOperation, ln_locationOperation, len_prevention;
    TextView tx_p_closed, txt_cancel_permit;
    LinearLayout len_p_closed, len_cancel_permit;
    String PermitStatus = "";
    LinearLayout ln_spinner_authorize, ln_spinner_reason;
    RecyclerView list_goldenRules;
    ArrayList<GoldenRules> goldenRulesArrayList;
    GoldenRuleAdapter goldenRuleAdapter;
    GoldenRules goldenRules;
    String categoryDesc = "";
    int modeefirst = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_work_permit);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        toolbar.setTitle(getResources().getString(R.string.application_name));

        setSupportActionBar(toolbar);
        mcontext = HOTWorkActivity.this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        initview();
        setListner();
        dateListner();
        Intent intent = getIntent();


        userpreferences = getSharedPreferences(LoginActivity.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", "");

        contractorListActivityArrayList = new ArrayList<>();
        authorizedPersonArrayList = new ArrayList<>();
        txt_authorizeArrayList = new ArrayList<>();
        operationArrayList = new ArrayList<>();
        LocationArraylist = new ArrayList<>();
        depotArrayList = new ArrayList<>();
        safetyToolsArrayList = new ArrayList<>();
        safetyMeasureArrayList = new ArrayList<>();
        WAArayList = new ArrayList<>();
        goldenRulesArrayList = new ArrayList<>();


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

        edt_fromdate.setText(date);
        //edt_todate.setText(date);
        edt_authorize_date.setText(date);
        edt_authorize_date1.setText(date);
        edt_authorize_date2.setText(date);
        edt_permit_date.setText(date);
        edt_spot_date.setText(date);
        edt_cancel_date.setText(date);

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        int hour1 = hour + 4;
        final String time1 = UpdateTime.updateTime(hour, minute);
        to_time = UpdateTime.updateTime(hour1, minute);
        final String time2 = UpdateTime.updateTime(hour1, minute);
        System.out.println("time: " + time1);

        edt_fromtime.setText(time1);
        if (time1.contains("AM")) {
            edt_totime.setText("11:59 PM");
        } else {
            edt_totime.setText(time2);
        }


        // Golden Rules
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("GoldenRules", "");
        type = new TypeToken<List<GoldenRules>>() {
        }.getType();
        goldenRulesArrayList = gson.fromJson(json, type);

        if (goldenRulesArrayList == null) {
            if (CommonClass.checkNet(HOTWorkActivity.this)) {
                showProgress();
                new StartSession(HOTWorkActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadGoldenRules().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(HOTWorkActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (goldenRulesArrayList.size() > 0) {

                goldenRuleAdapter = new GoldenRuleAdapter(HOTWorkActivity.this, goldenRulesArrayList, Mode, PermitStatus);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HOTWorkActivity.this);

                // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                list_goldenRules.setLayoutManager(linearLayoutManager);
                list_goldenRules.setAdapter(goldenRuleAdapter);


            }

        }


        //Installation Preparation

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("measure", "");
        type = new TypeToken<List<SafetyMeasure>>() {
        }.getType();
        safetyMeasureArrayList = gson.fromJson(json, type);

        if (safetyMeasureArrayList == null) {
            if (CommonClass.checkNet(HOTWorkActivity.this)) {
                showProgress();
                new StartSession(HOTWorkActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadSafetyMeasureData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(HOTWorkActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (safetyMeasureArrayList.size() > 0) {
                safetyMeasureListAdapter = new SafetyMeasureListAdapter(HOTWorkActivity.this, safetyMeasureArrayList, Mode, PermitStatus);
                list_safety_measure.setAdapter(safetyMeasureListAdapter);
                Utility.setListViewHeightBasedOnItems(list_safety_measure);
            }

        }


        // Safety Tools
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("safety", "");
        type = new TypeToken<List<SafetyTools>>() {
        }.getType();
        safetyToolsArrayList = gson.fromJson(json, type);

        if (safetyToolsArrayList == null) {
            if (CommonClass.checkNet(HOTWorkActivity.this)) {
                showProgress();
                new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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
                safetyAdapter = new SafetyAdapter(HOTWorkActivity.this, safetyToolsArrayList, "HW", Mode, PermitStatus);
                grid_safety.setAdapter(safetyAdapter);


            }

        }

        //Depot Station
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Depot", "");
        type = new TypeToken<List<Depot>>() {
        }.getType();
        depotArrayList = gson.fromJson(json, type);

        if (depotArrayList == null) {
            if (CommonClass.checkNet(HOTWorkActivity.this)) {
                showProgress();
                new StartSession(HOTWorkActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadDepotData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        CommonClass.displayToast(HOTWorkActivity.this, msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (depotArrayList.size() > 0) {
                depotAdapter = new DepotAdapter(HOTWorkActivity.this, depotArrayList);
                spinner_station.setAdapter(depotAdapter);


            }

        }


        // ContractorList

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Contractor", "");
        type = new TypeToken<List<ContractorList>>() {
        }.getType();
        contractorListActivityArrayList = gson.fromJson(json, type);

        if (contractorListActivityArrayList == null) {
            if (CommonClass.checkNet(HOTWorkActivity.this)) {
                showProgress();
                new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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
                permitContractorListAdapter = new PermitContractorListAdapter(HOTWorkActivity.this, contractorListActivityArrayList);
                spinner_authorize2.setAdapter(permitContractorListAdapter);
                spinner_contractor.setAdapter(permitContractorListAdapter);
            }

        }

        // Operation

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Operation", "");
        type = new TypeToken<List<Operation>>() {
        }.getType();
        operationArrayList = gson.fromJson(json, type);

        if (operationArrayList == null) {
            if (CommonClass.checkNet(HOTWorkActivity.this)) {
                showProgress();
                new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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
                operationAdapter = new OperationAdapter(HOTWorkActivity.this, operationArrayList);
                spinner_operation.setAdapter(operationAdapter);
            }

        }

        // Authorized Person

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("Authorized", "");
        type = new TypeToken<List<AuthorizedPerson>>() {
        }.getType();
        authorizedPersonArrayList = gson.fromJson(json, type);

        if (authorizedPersonArrayList == null) {
            if (CommonClass.checkNet(HOTWorkActivity.this)) {
                showProgress();
                new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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
                authorizedPersonAdapter = new AuthorizedPersonAdapter(HOTWorkActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);
                // spinner_authorize1.setAdapter(authorizedPersonAdapter);

            }

        }

        if (getIntent() != null) {

            permit = new Gson().fromJson(getIntent().getStringExtra("permitno"), Permit.class);
            if (permit != null) {
                Mode = "E";
                PermitStatus = permit.getWorkAuthorizationstatus();
                edt_fromdate.setEnabled(true);

                if (!PermitStatus.equalsIgnoreCase("P")) {
                    spinner_station.setEnabled(false);
                    spinner_prevention_plan.setEnabled(false);
                    edt_fromdate.setEnabled(false);
                    edt_fromtime.setEnabled(false);
                    edt_totime.setEnabled(false);
                    spinner_contractor.setEnabled(false);
                    spinner_operation.setEnabled(false);
                    spinner_location.setEnabled(false);
                    checkbox_method.setClickable(false);
                    txt_authorize.setKeyListener(null);
                    txt_authorize1.setKeyListener(null);
                    spinner_authorize2.setEnabled(false);
                    edit_remarks.setKeyListener(null);
                }else {
                    edt_fromdate.setEnabled(true);
                }
                if (PermitStatus.equalsIgnoreCase("R") || PermitStatus.equalsIgnoreCase("C")) {
                    spinner_station.setEnabled(false);
                    spinner_prevention_plan.setEnabled(false);
                    edt_fromdate.setEnabled(false);
                    edt_fromtime.setEnabled(false);
                    edt_totime.setEnabled(false);
                    spinner_contractor.setEnabled(false);
                    spinner_operation.setEnabled(false);
                    spinner_location.setEnabled(false);

                    safetyAdapter = new SafetyAdapter(HOTWorkActivity.this, safetyToolsArrayList, "HW", Mode, PermitStatus);
                    grid_safety.setAdapter(safetyAdapter);

                    checkbox_method.setClickable(false);
                    txt_authorize.setKeyListener(null);
                    txt_authorize1.setKeyListener(null);
                    spinner_authorize2.setEnabled(false);

                    edt_authorize_date.setEnabled(false);
                    edt_authorize_date1.setEnabled(false);
                    edt_authorize_date2.setEnabled(false);
                    edt_authorize_date.setTextColor(Color.parseColor("#000000"));
                    edt_authorize_date1.setTextColor(Color.parseColor("#000000"));
                    edt_authorize_date2.setTextColor(Color.parseColor("#000000"));

                    txt_spot_check.setEnabled(false);
                    txt_cancel.setEnabled(false);
                    edt_spot_date.setEnabled(false);
                    edt_spot_date.setTextColor(Color.parseColor("#000000"));
                    txt_permit_closed.setEnabled(false);
                    edt_permit_date.setEnabled(false);
                    edt_permit_date.setTextColor(Color.parseColor("#000000"));
                    tx_p_closed.setKeyListener(null);
                    txt_cancel_permit.setKeyListener(null);
                    edt_cancel_date.setKeyListener(null);
                    edt_cancel_date.setTextColor(Color.parseColor("#000000"));

                    edit_remarks.setKeyListener(null);


                }
              //  if (PermitStatus.equals("A") || PermitStatus.equals("P")) {
                    len_p_closed.setVisibility(View.VISIBLE);
                    tx_p_closed.setVisibility(View.VISIBLE);
                    txt_cancel_permit.setVisibility(View.VISIBLE);
                    len_cancel_permit.setVisibility(View.VISIBLE);

               /* } else {
                    len_p_closed.setVisibility(View.GONE);
                    tx_p_closed.setVisibility(View.GONE);
                    txt_cancel_permit.setVisibility(View.GONE);
                    len_cancel_permit.setVisibility(View.GONE);
                }*/


                if (PermitStatus.equals("R") || PermitStatus.equalsIgnoreCase("C")) {
                    btn_submit.setClickable(false);
                    spinner_contractor.setEnabled(false);
                    spinner_station.setEnabled(false);
                    spinner_prevention_plan.setEnabled(false);
                    edt_fromdate.setEnabled(false);
                    edt_fromtime.setEnabled(false);
                    edt_totime.setEnabled(false);
                    spinner_operation.setEnabled(false);
                    spinner_location.setEnabled(false);
                    checkbox_method.setClickable(false);
                    txt_authorize.setKeyListener(null);
                    txt_authorize1.setKeyListener(null);
                    spinner_authorize2.setEnabled(false);
                    edit_remarks.setKeyListener(null);
                    safetyMeasureListAdapter = new SafetyMeasureListAdapter(HOTWorkActivity.this, safetyMeasureArrayList, Mode, PermitStatus);
                    list_safety_measure.setAdapter(safetyMeasureListAdapter);


                }


                PermitNo = permit.getPermitNo();
                PKFormId = permit.getPermitId();
                //formid = permit.getPermitId();
                //edt_fromdate.setEnabled(true);
                edt_fromdate.setTextColor(Color.parseColor("#101010"));
                if (CommonClass.checkNet(HOTWorkActivity.this)) {
                    showProgress();
                    new StartSession(HOTWorkActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new getdetails().execute(PermitNo);
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

                Mode = "A";
                PKFormId = intent.getStringExtra("PKFormId");
                formcode = intent.getStringExtra("formcode");
                //GetPermit NO

                if (CommonClass.checkNet(HOTWorkActivity.this)) {
                    showProgress();
                    new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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

    private void callEditApi(final String Permitno) {
        this.PermitNo = Permitno;

    }

    public void cameraClick(int position, String projectCode) {

        if (ContextCompat.checkSelfPermission(HOTWorkActivity.this, Manifest.permission.CAMERA)
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

    public void reasonDialog(final int position, String projectCode) {
        img_pos = position;

        AlertDialog.Builder builder = new AlertDialog.Builder(HOTWorkActivity.this);
        LayoutInflater inflater = getLayoutInflater().from(HOTWorkActivity.this);
        View dialogView = null;
        if (projectCode.equals("ACMB") || projectCode.equals("EIP")) {
            dialogView = inflater.inflate(R.layout.remarks_value, null);
        } else {
            dialogView = inflater.inflate(R.layout.remarks, null);
        }


        builder.setView(dialogView);
        final EditText edit_remark1 = dialogView.findViewById(R.id.edit_remarks);
        Button txt_submit = dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = dialogView.findViewById(R.id.txt_cancel);


        builder.setCancelable(false);
        final AlertDialog b = builder.create();
        b.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (safetyMeasureArrayList != null) {
            if (safetyMeasureArrayList.get(position).getRemarks() != null) {
                String remarks = safetyMeasureArrayList.get(position).getRemarks();
                safetyMeasureArrayList.get(position).setSelected(true);
                edit_remark1.setText(remarks);
            }
        }
        b.show();
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remarks = edit_remark1.getText().toString();
                safetyMeasureArrayList.get(position).setSelected(true);
                safetyMeasureArrayList.get(position).setRemarks(remarks);
                safetyMeasureListAdapter.updateList(safetyMeasureArrayList);
                b.dismiss();
            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  safetyMeasureArrayList.get(position).setSelected(false);
                //safetyMeasureArrayList.get(position).setRemarks("");
                // safetyMeasureListAdapter.updateList(safetyMeasureArrayList);


                if (safetyMeasureArrayList.get(position).getRemarks() == null ||
                        safetyMeasureArrayList.get(position).getRemarks().equalsIgnoreCase("")) {
                    if (isAns) {
                        //holder.checkbox_user.setChecked(false);
                        safetyMeasureArrayList.get(position).setSelected(false);
                        safetyMeasureArrayList.get(position).setRemarks("");
                        safetyMeasureListAdapter.updateList(safetyMeasureArrayList);
                        /*safetyMeasureArrayList.get(position).setSelected(false);
                        safetyMeasureArrayList.get(position).setRemarks("");
*/
                    } else {
                        // holder.checkbox_user.setChecked(true);
                        safetyMeasureArrayList.get(position).setSelected(true);
                        safetyMeasureArrayList.get(position).setRemarks(edit_remark1.getText().toString());
                        safetyMeasureListAdapter.updateList(safetyMeasureArrayList);

                        /*safetyMeasureArrayList.get(position).setSelected(true);
                        safetyMeasureArrayList.get(position).setRemarks(edit_remark1.getText().toString());*/
                    }

                } else {

                    isAns = false;
                    safetyMeasureArrayList.get(position).setSelected(true);
                    safetyMeasureListAdapter.updateList(safetyMeasureArrayList);
                    //isAns = false;
                    // safetyMeasureArrayList.get(position).setSelected(true);
                    //holder.checkbox_user.setChecked(true);
                }


                b.dismiss();
            }
        });

        edit_remark1.addTextChangedListener(new TextWatcher() {
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

    public void reasonDialog_SafetyTools(final int position, String SafetyToolMasterId) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HOTWorkActivity.this);
        LayoutInflater inflater = getLayoutInflater().from(HOTWorkActivity.this);
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
                safetyToolsArrayList.get(position).setSelected(true);
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
                        safetyToolsArrayList.get(position).setSelected(true);
                        safetyToolsArrayList.get(position).setRemarks(edit_remark.getText().toString());
                        safetyAdapter.updateList(safetyToolsArrayList);
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


    /*public void reasonDialog_SafetyTools(final int position, String SafetyToolMasterId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(HOTWorkActivity.this);
        LayoutInflater inflater = getLayoutInflater().from(HOTWorkActivity.this);
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
                safetyToolsArrayList.get(position).setSelected(true);
            }
        }


        b.show();
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remarks = edit_remark.getText().toString();
                safetyToolsArrayList.get(position).setSelected(true);
                safetyToolsArrayList.get(position).setRemarks(remarks);
                edit_remark.setText(remarks);
                safetyAdapter.updateList(safetyToolsArrayList);
                b.dismiss();
            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              *//*  safetyToolsArrayList.get(position).setSelected(false);
                safetyToolsArrayList.get(position).setRemarks("");
                safetyAdapter.updateList(safetyToolsArrayList);*//*

                if (safetyToolsArrayList.get(position).getRemarks() == null ||
                        safetyToolsArrayList.get(position).getRemarks().equalsIgnoreCase("")) {
                    if (isAns) {
                        // holder.checkbox_user.setChecked(false);
                        safetyToolsArrayList.get(position).setSelected(false);
                        safetyToolsArrayList.get(position).setRemarks("");
                        safetyAdapter.updateList(safetyToolsArrayList);

                    } else {
                        //  holder.checkbox_user.setChecked(true);
                        safetyToolsArrayList.get(position).setSelected(true);
                        safetyToolsArrayList.get(position).setRemarks(edit_remark.getText().toString());
                        safetyAdapter.updateList(safetyToolsArrayList);
                    }

                } else {
                    // holder.checkbox_user.setChecked(true);
                    if (safetyToolsArrayList.get(position).isSelected()) {
                        safetyToolsArrayList.get(position).setSelected(true);
                    } else {
                        safetyToolsArrayList.get(position).setSelected(false);

                    }

                    safetyToolsArrayList.get(position).setRemarks(edit_remark.getText().toString());
                    safetyAdapter.updateList(safetyToolsArrayList);
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

    }*/


    class getdetails extends AsyncTask<String, Void, String> {
        Object res;
        String response = "";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(HOTWorkActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setTitle("Data fetching...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebAPIUrl.api_getHWDetails + "?form=" + PermitNo;

            try {
                res = CommonClass.OpenConnection(url, HOTWorkActivity.this);
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
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HOTWorkActivity.this);

                            // linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            list_goldenRules.setLayoutManager(linearLayoutManager);
                            goldenRuleAdapter = new GoldenRuleAdapter();
                            goldenRuleAdapter.updateList(goldenRulesArrayList, Mode, PermitStatus);
                            list_goldenRules.setAdapter(goldenRuleAdapter);


                        } else {
                            if (CommonClass.checkNet(HOTWorkActivity.this)) {
                                new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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

                        permitfromtime = jorder.getString("PermitFromTime");
                        edt_fromtime.setText(permitfromtime);


                        permittotime = jorder.getString("PermitToTime");
                        edt_totime.setText(permittotime);

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


                        StationId = jorder.getString("FkWareHouseMasterId");
                        StationName = jorder.getString("WarehouseDescription");

                        /* if (depotArrayList.size() != 0) {*/

                        int depotpos = -1;

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

                        LocationId = jorder.getString("FkLocationMasterId");
                        int locationpos = -1;
                        if (LocationArraylist.size() != 0) {
                            for (int j = 0; j < LocationArraylist.size(); j++) {
                                if (LocationArraylist.get(j).getLocationMasterId().equals(LocationId)) {
                                    locationpos = j;
                                    break;
                                }
                            }

                            if (locationpos != -1) {
                                spinner_location.setSelection(locationpos);
                            } else {
                                spinner_location.setSelection(0);
                            }
                        } else {
                            if (StationId != "") {
                                if (CommonClass.checkNet(HOTWorkActivity.this)) {
                                    new StartSession(HOTWorkActivity.this, new CallbackInterface() {
                                        @Override
                                        public void callMethod() {
                                            new DownloadLocationOperationData().execute(StationId);
                                        }

                                        @Override
                                        public void callfailMethod(String msg) {
                                            Toast.makeText(HOTWorkActivity.this, "No internet connnection", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(HOTWorkActivity.this, "No internet connnection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }


                        contractorId = jorder.getString("fkContractorId");
                        contractorName = jorder.getString("CustVendorName");
                        // if (contractorListActivityArrayList.size() != 0) {
                        int selectedPos = -1;
                        for (int j = 0; j < contractorListActivityArrayList.size(); j++) {
                            if (contractorListActivityArrayList.get(j).getCustVendorMasterId().equals(contractorId)) {
                                selectedPos = j;
                                break;
                            }
                        }
                        if (selectedPos != -1) {
                            spinner_contractor.setSelection(selectedPos);
                            spinner_authorize2.setSelection(selectedPos);
                        } else {
                            spinner_contractor.setSelection(0);
                            spinner_authorize2.setSelection(0);
                        }
                   /*     } else {

                            if (CommonClass.checkNet(HOTWorkActivity.this)) {
                                new StartSession(HOTWorkActivity.this, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new DownloadContractorData().execute();
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {

                                    }
                                });
                            }

                        }*/
                        int WAHNo = -1;

                        WAH_No = jorder.getString("FkWorkAuthorizationNo");
                        spinner_prevention_plan.setPrompt(WAH_No);
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
                            if (CommonClass.checkNet(HOTWorkActivity.this)) {
                                new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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


                        NatureOperation = jorder.getString("fkOperationMasterId");
                        OperationName = jorder.getString("Operation");
                        // if (operationArrayList.size() != 0) {

                        int operationpos = -1;
                        for (int j = 0; j < operationArrayList.size(); j++) {
                            if (operationArrayList.get(j).getOperationMasterId().equals(NatureOperation)) {
                                operationpos = j;
                                break;
                            }
                        }
                        if (operationpos != -1)
                            spinner_operation.setSelection(operationpos);
                        else
                            spinner_operation.setSelection(0);
                      /*  } else {
                            if (CommonClass.checkNet(HOTWorkActivity.this)) {
                                showProgress();
                                new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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
                        }*/

                        safetytools = jorder.getString("SafetyToolMasterId");/* "chetana,sayali,suyog,vritti,pradnya"*/
                        //  if (safetyToolsArrayList.size() != 0) {
                        String[] safetytoolslist = new String[safetytools.length()];
                        safetytoolslist = safetytools.split(",");
                        for (int j = 0; j < safetyToolsArrayList.size(); j++) {
                            for (int k = 0; k < safetytoolslist.length; k++) {
                                if (safetytoolslist[k].equals(safetyToolsArrayList.get(j).getSafetyToolMasterId())) {
                                    int pos = j;
                                    safetyToolsArrayList.get(pos).setSelected(true);
                                    safetyAdapter = new SafetyAdapter(HOTWorkActivity.this, safetyToolsArrayList, "HW", Mode, PermitStatus);
                                    grid_safety.setAdapter(safetyAdapter);
                                }

                            }
                        }
                      /*  } else {
                            if (CommonClass.checkNet(HOTWorkActivity.this)) {
                                showProgress();
                                new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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
                        }*/


                        safetyradio = jorder.getString("SafetyMeasuresId");/* "chetana,sayali,suyog,vritti,pradnya"*/

                        //  if (safetyMeasureArrayList.size() != 0) {
                        String[] safetymeasurelist = new String[safetyradio.length()];
                        safetymeasurelist = safetyradio.split(",");
                        for (int j = 0; j < safetyMeasureArrayList.size(); j++) {
                            for (int k = 0; k < safetymeasurelist.length; k++) {
                                if (safetymeasurelist[k].equals(safetyMeasureArrayList.get(j).getProjectId())) {
                                    int pos = j;
                                    safetyMeasureArrayList.get(pos).setSelected(true);
                                    safetyMeasureListAdapter = new SafetyMeasureListAdapter(HOTWorkActivity.this, safetyMeasureArrayList, Mode, PermitStatus);
                                    list_safety_measure.setAdapter(safetyMeasureListAdapter);
                                }

                            }
                        }
                 /*       } else {
                            if (CommonClass.checkNet(HOTWorkActivity.this)) {
                                showProgress();
                                new StartSession(HOTWorkActivity.this, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new DownloadSafetyMeasureData().execute();
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {
                                        CommonClass.displayToast(HOTWorkActivity.this, msg);
                                        dismissProgress();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }*/


                        checkoperation = jorder.getString("MethodOperationStatus");
                        if (checkoperation.equals("N"))
                            checkbox_method.setChecked(false);
                        else if (checkoperation.equals("Y"))
                            checkbox_method.setChecked(true);
                        else
                            checkbox_method.setChecked(false);


                        //Authorize1
                        Authorize1Id = jorder.getString("AuthorizeBy1");
                        //Authorize1Name = jorder.getString("");

                        int authorizepos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(Authorize1Id)) {
                                authorizepos = j;
                            }
                        }
                        if (authorizepos != -1)
                            txt_authorize.setText(authorizedPersonArrayList.get(authorizepos).getAuthorizename());
                        else
                            txt_authorize.setText("Select");

                        //Authorize2
                        Authorize2Id = jorder.getString("AuthorizeBy2");
                        //Authorize2Name = jorder.getString("");
                        int authorize2pos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (authorizedPersonArrayList.get(j).getAuthorizeid().equals(Authorize2Id)) {
                                authorize2pos = j;
                            }
                        }
                        if (authorize2pos != -1)
                            txt_authorize1.setText(authorizedPersonArrayList.get(authorize2pos).getAuthorizename());
                        else
                            txt_authorize1.setText("Select");


                        //contractor
                       /* contractorId = jorder.getString("RespContractorId");
                        //contractorname = jorder.getString("");
                        int contractorpos = -1;
                        for(int j=0;j<contractorListActivityArrayList.size();j++){
                            if(contractorId.equals(contractorListActivityArrayList.get(j).getCustVendorMasterId()))
                                contractorpos = j;
                        }
                        if(contractorpos != -1)
                            spinner_authorize2.setSelection(contractorpos);
                        else
                            spinner_authorize2.setSelection(0);*/

                        //permitclosed
                        PermitclosedId = jorder.getString("PermitCloseBy");
                        //PermitClosedName = jorder.getString("");

                        int permitpos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (PermitclosedId.equals(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                permitpos = j;
                                break;
                            }
                        }
                        if (permitpos != -1)
                            txt_permit_closed.setText(authorizedPersonArrayList.get(permitpos).getAuthorizename());
                        else
                            txt_permit_closed.setText("Select");

                        //spotCheck
                        SpotcheckId = jorder.getString("SpotCheckBy");
                        //SpotCheckName = jorder.getString("");
                        //  if (authorizedPersonArrayList.size() != 0) {
                        int spotcheckpos = -1;
                        for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                            if (SpotcheckId.equals(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                spotcheckpos = j;
                                break;
                            }
                        }
                        if (spotcheckpos != -1)
                            txt_spot_check.setText(authorizedPersonArrayList.get(spotcheckpos).getAuthorizename());
                        else
                            txt_spot_check.setText("Select");
                           /* } else {

                                if (CommonClass.checkNet(HOTWorkActivity.this)) {
                                    showProgress();
                                    new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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


                                    int spotcheckpos = -1;
                                    for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                        if (SpotcheckId.equals(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                            spotcheckpos = j;
                                            break;
                                        }
                                    }
                                    if (spotcheckpos != -1)
                                        txt_spot_check.setText(authorizedPersonArrayList.get(spotcheckpos).getAuthorizename());
                                    else
                                        txt_spot_check.setText("Select");

                                } else {
                                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                                }

                            }
*/

                        cancelPermitId = jorder.getString("PermitCancelBy");
                        //SpotCheckName = jorder.getString("");

                        int cancelpos = -1;
                        if (cancelPermitId.equalsIgnoreCase("") || cancelPermitId.equalsIgnoreCase("null")) {
                            cancelPermitId = "";
                        } else {
                            for (int j = 0; j < authorizedPersonArrayList.size(); j++) {
                                if (cancelPermitId.equals(authorizedPersonArrayList.get(j).getAuthorizeid())) {
                                    cancelpos = j;
                                    break;
                                }
                            }
                        }
                        if (cancelpos != -1)
                            txt_cancel.setText(authorizedPersonArrayList.get(cancelpos).getAuthorizename());
                        else
                            txt_cancel.setText("Select");


                        AuthorizeDate1 = jorder.getString("UserManagerAuthDate");
                        if (AuthorizeDate1.equals("")) {
                            edt_authorize_date.setText(date);
                        } else {
                            edt_authorize_date.setText(AuthorizeDate1);
                        }

                        AuthorizeDate2 = jorder.getString("SurveillanceAuthDate");
                        if (AuthorizeDate2.equals("")) {
                            edt_authorize_date.setText(date);
                        } else {
                            edt_authorize_date1.setText(AuthorizeDate2);
                        }

                        AuthorizeDate3 = jorder.getString("ContractorManagerAuthDate");
                        if (AuthorizeDate3.equals("")) {
                            edt_authorize_date.setText(date);
                        } else {
                            edt_authorize_date2.setText(AuthorizeDate3);
                        }
                        permitcloseddate = jorder.getString("PermitCloseDate");
                        edt_permit_date.setText(permitcloseddate);

                        Spotcheckdate = jorder.getString("SpotCheckDate");
                        edt_spot_date.setText(Spotcheckdate);

                        cancelDate = jorder.getString("PermitCancelDate");
                        edt_cancel_date.setText(cancelDate);


                        permitcloseremark = jorder.getString("PermitCloseRemark");
                        if (!permitcloseremark.equalsIgnoreCase(""))
                            edit_remarks.setText(permitcloseremark);
                        else
                            edit_remarks.setText("");


                        StartDate = jorder.getString("PermitFromDate");
                        edt_fromdate.setText(StartDate);


                        safety_Others = jorder.getString("HW_SafetyOthers");
                        safety_BEL = jorder.getString("HW_SafetyBEL");
                        safety_extinguisher = jorder.getString("HW_SafetyExt");

                        if (safetyMeasureArrayList != null) {

                            for (int j = 0; j < safetyMeasureArrayList.size(); j++) {

                                if (safetyMeasureArrayList.get(j).getProjectCode().equals("ACMB")) {
                                    safetyMeasureArrayList.get(j).setRemarks(safety_BEL);
                                    safetyMeasureArrayList.get(j).isSelected();
                                } else if (safetyMeasureArrayList.get(j).getProjectCode().equals("ORS")) {
                                    safetyMeasureArrayList.get(j).setRemarks(safety_Others);
                                    safetyMeasureArrayList.get(j).isSelected();
                                } else if (safetyMeasureArrayList.get(j).getProjectCode().equals("EIP")) {
                                    safetyMeasureArrayList.get(j).setRemarks(safety_extinguisher);
                                    safetyMeasureArrayList.get(j).isSelected();
                                }

                            }
                        }


                        safetyTools_Others = jorder.getString("HW_ProOthers");
                        if (safetyToolsArrayList != null) {
                            if (safetyTools_Others != "") {
                                for (int j = 0; j < safetyToolsArrayList.size(); j++) {
                                    if (safetyToolsArrayList.get(j).getSafetyToolDesc().equals("Other(s)")) {
                                        if (safetyTools_Others.equals("")) {
                                            safetyToolsArrayList.get(j).setSelected(false);
                                        } else {
                                            safetyToolsArrayList.get(j).setSelected(true);
                                        }

                                        safetyToolsArrayList.get(j).setRemarks(safetyTools_Others);
                                    }
                                }
                            }
                        }




                       /* if(PermitclosedId.equals("") &&  SpotcheckId.equals("")){
                            btn_submit.setVisibility(View.GONE);
                        }else{
                            btn_submit.setVisibility(View.VISIBLE);
                        }*/


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


                if (tempImgVal.equals("1")) {
                    Imagefilename = f.getName();
                    imgAbsolutePath = f.getAbsolutePath();
                    imgAtmosphericPost = f.getName();
                    //img = f.getAbsolutePath();

                    //handleSendImage(data);
                } else if (tempImgVal.equals("2")) {
                    SpotCheckimg = f.toString();
                    SpotCheckimgNamePost = f.getName();
                    // img = f.getAbsolutePath();
                }


                if (CommonClass.checkNet(HOTWorkActivity.this)) {
                    new PostUploadImageMethod().execute();

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void showProgress() {

       /* mprogress.setVisibility(View.VISIBLE);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.progress,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        //TextView text = (TextView) layout.findViewById(R.id.text);
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
                res = CommonClass.OpenConnection(url, HOTWorkActivity.this);
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
                //  Toast.makeText(HOTWorkActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(authorizedPersonArrayList);
                editor.putString("Authorized", json);
                editor.commit();
                authorizedPersonAdapter = new AuthorizedPersonAdapter(HOTWorkActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
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
                res = CommonClass.OpenConnection(url, HOTWorkActivity.this);
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
                //  Toast.makeText(HOTWorkActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(contractorListActivityArrayList);
                editor.putString("Contractor", json);
                editor.commit();
                permitContractorListAdapter = new PermitContractorListAdapter(HOTWorkActivity.this, contractorListActivityArrayList);
                spinner_authorize2.setAdapter(permitContractorListAdapter);
                spinner_contractor.setAdapter(permitContractorListAdapter);


                if (!contractorId.equals("")) {
                    int selectedPos = -1;
                    for (int j = 0; j < contractorListActivityArrayList.size(); j++) {
                        if (contractorListActivityArrayList.get(j).getCustVendorMasterId().equals(contractorId)) {
                            selectedPos = j;
                            break;
                        }
                    }
                    if (selectedPos != -1) {
                        spinner_contractor.setSelection(selectedPos);
                        spinner_authorize2.setSelection(selectedPos);
                    } else {
                        spinner_contractor.setSelection(0);
                        spinner_authorize2.setSelection(0);
                    }
                }


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


                if (CommonClass.checkNet(HOTWorkActivity.this)) {
                    showProgress();
                    new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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

                if (CommonClass.checkNet(HOTWorkActivity.this)) {
                    showProgress();
                    new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(HOTWorkActivity.this)) {
                    showProgress();
                    new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(HOTWorkActivity.this)) {
                    showProgress();
                    new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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


                if (CommonClass.checkNet(HOTWorkActivity.this)) {
                    showProgress();
                    new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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

                if (CommonClass.checkNet(HOTWorkActivity.this)) {
                    showProgress();
                    new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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

                if (CommonClass.checkNet(HOTWorkActivity.this)) {
                    showProgress();
                    new StartSession(HOTWorkActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadSafetyMeasureData().execute();
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
                res = CommonClass.OpenConnection(url, HOTWorkActivity.this);
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
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(goldenRulesArrayList);
                editor.putString("GoldenRules", json);
                editor.commit();
                goldenRuleAdapter = new GoldenRuleAdapter(HOTWorkActivity.this, goldenRulesArrayList, Mode, PermitStatus);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HOTWorkActivity.this);
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
                res = CommonClass.OpenConnection(url, HOTWorkActivity.this);
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
                //  Toast.makeText(HOTWorkActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(operationArrayList);
                editor.putString("Operation", json);
                editor.commit();
                operationAdapter = new OperationAdapter(HOTWorkActivity.this, operationArrayList);
                spinner_operation.setAdapter(operationAdapter);


                if (!OperationName.equals("")) {

                    int operationpos = -1;
                    for (int j = 0; j < operationArrayList.size(); j++) {
                        if (operationArrayList.get(j).getOperationMasterId().equals(NatureOperation)) {
                            operationpos = j;
                            break;
                        }
                    }
                    if (operationpos != -1)
                        spinner_operation.setSelection(operationpos);
                    else
                        spinner_operation.setSelection(0);

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
            if (integer != null || integer != "") {

                PermitNo = integer;
                edit_permitno.setText(PermitNo);

            } else {
                edit_permitno.setText("");
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebAPIUrl.api_GetHWPermitNo;

            res = CommonClass.OpenConnection(url, HOTWorkActivity.this);

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
                res = CommonClass.OpenConnection(url, HOTWorkActivity.this);
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
                //   Toast.makeText(HOTWorkActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(depotArrayList);
                editor.putString("Depot", json);
                editor.commit();


                depotAdapter = new DepotAdapter(HOTWorkActivity.this, depotArrayList);
                spinner_station.setAdapter(depotAdapter);

                if (!StationId.equals("")) {
                    int depotpos = -1;

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
                res = CommonClass.OpenConnection(url, HOTWorkActivity.this);
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
                locationOperationAdapter = new LocationOperationAdapter(HOTWorkActivity.this, LocationArraylist);
                spinner_location.setAdapter(locationOperationAdapter);
                // Toast.makeText(HOTWorkActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                locationOperationAdapter = new LocationOperationAdapter(HOTWorkActivity.this, LocationArraylist);
                spinner_location.setAdapter(locationOperationAdapter);

                /*if (Mode.contains("E")) {*/
                if (LocationId != "") {
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
                    if (tempImgVal == "1") {
                        safetyMeasureArrayList.get(img_pos).setImgAbsolutePath(imgAbsolutePath);
                        safetyMeasureArrayList.get(img_pos).setImgPath(Imagefilename);
                    } else if (tempImgVal == "2") {

                        HOTWorkActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dismissProgress();


                                //   Toast.makeText(HOTWorkActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();


                            }
                        });
                    }
                    HOTWorkActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dismissProgress();
                            safetyMeasureListAdapter.updateList(safetyMeasureArrayList);
                            Toast.makeText(HOTWorkActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();

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
                        HOTWorkActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dismissProgress();
                                Toast.makeText(HOTWorkActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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
                res = CommonClass.OpenConnection(url, HOTWorkActivity.this);
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
                // Toast.makeText(HOTWorkActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(safetyToolsArrayList);
                editor.putString("safety", json);
                editor.commit();
                safetyAdapter = new SafetyAdapter(HOTWorkActivity.this, safetyToolsArrayList, "HW", Mode, PermitStatus);
                grid_safety.setAdapter(safetyAdapter);

                if (!safetytools.equals("")) {
                    String[] safetytoolslist = new String[safetytools.length()];
                    safetytoolslist = safetytools.split(",");
                    for (int j = 0; j < safetyToolsArrayList.size(); j++) {
                        for (int k = 0; k < safetytoolslist.length; k++) {
                            if (safetytoolslist[k].equals(safetyToolsArrayList.get(j).getSafetyToolMasterId())) {
                                int pos = j;
                                safetyToolsArrayList.get(pos).setSelected(true);
                                safetyAdapter = new SafetyAdapter(HOTWorkActivity.this, safetyToolsArrayList, "HW", Mode, PermitStatus);
                                grid_safety.setAdapter(safetyAdapter);
                            }

                        }
                    }
                }


            }


        }
    }

    class DownloadSafetyMeasureData extends AsyncTask<String, Void, String> {
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

            String url = CompanyURL + WebAPIUrl.api_GetInstallationOperation + "?PKFormId=" + PKFormId;

            try {
                res = CommonClass.OpenConnection(url, HOTWorkActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    // response = response.substring(1, response.length() - 1);

                    safetyMeasureArrayList = new ArrayList<>();
                    safetyMeasureArrayList.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        SafetyMeasure safetyMeasure = new SafetyMeasure();
                        JSONObject jorder = jResults.getJSONObject(i);
                        safetyMeasure.setProjectId(jorder.getString("ProjectId"));
                        safetyMeasure.setProjectCode(jorder.getString("ProjectCode"));
                        safetyMeasure.setProjectName(jorder.getString("ProjectName"));
                        safetyMeasureArrayList.add(safetyMeasure);


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
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HOTWorkActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                gson = new Gson();
                String json = gson.toJson(safetyMeasureArrayList);
                editor.putString("measure", json);
                editor.commit();
                safetyMeasureListAdapter = new SafetyMeasureListAdapter(HOTWorkActivity.this, safetyMeasureArrayList, Mode, PermitStatus);
                list_safety_measure.setAdapter(safetyMeasureListAdapter);
                Utility.setListViewHeightBasedOnItems(list_safety_measure);

                if (!safetyradio.equals("")) {

                    String[] safetymeasurelist = new String[safetyradio.length()];
                    safetymeasurelist = safetyradio.split(",");
                    for (int j = 0; j < safetyMeasureArrayList.size(); j++) {
                        for (int k = 0; k < safetymeasurelist.length; k++) {
                            if (safetymeasurelist[k].equals(safetyMeasureArrayList.get(j).getProjectId())) {
                                int pos = j;
                                safetyMeasureArrayList.get(pos).setSelected(true);
                                safetyMeasureListAdapter = new SafetyMeasureListAdapter(HOTWorkActivity.this, safetyMeasureArrayList, Mode, PermitStatus);
                                list_safety_measure.setAdapter(safetyMeasureListAdapter);
                            }

                        }
                    }
                }


            }


        }
    }

    private void saveactivityjson() {

        try {

            StartDate = edt_fromdate.getText().toString();
            permitfromtime = edt_fromtime.getText().toString();
            permittotime = edt_totime.getText().toString();
            permitcloseddate = edt_permit_date.getText().toString();
            permitcloseremark = edit_remarks.getText().toString();
            Spotcheckdate = edt_spot_date.getText().toString();
            cancelDate = edt_cancel_date.getText().toString();
            AuthorizeDate1 = edt_authorize_date.getText().toString();
            AuthorizeDate2 = edt_authorize_date1.getText().toString();
            AuthorizeDate3 = edt_authorize_date2.getText().toString();
            // String PermitNo = edit_permitno.getText().toString();


            safetyToolsArrayList = safetyAdapter.getArrayList();

            if (safetyToolsArrayList.size() > 0) {
                if (safetyToolsArrayList.size() > 0) {
                    user = new String[safetyToolsArrayList.size()];
                    for (int i = 0; i < safetyToolsArrayList.size(); i++) {
                        String Safety = safetyToolsArrayList.get(i).getSafetyToolMasterId();
                        user[i] = Safety.toString();
                        safetytools = TextUtils.join(",", user);
                        if (safetyToolsArrayList.get(i).getSafetyToolDesc().equals("Other(s)")) {
                            if (safetyToolsArrayList.get(i).getRemarks() == null) {
                                safetyTools_Others = "";
                            } else {
                                safetyTools_Others = safetyToolsArrayList.get(i).getRemarks();
                            }
                        }
                    }

                }
            }

            safetyMeasureArrayList = safetyMeasureListAdapter.getArrayList();
            if (safetyMeasureArrayList.size() > 0) {
                if (safetyMeasureArrayList.size() > 0) {
                    user5 = new String[safetyMeasureArrayList.size()];
                    for (int i = 0; i < safetyMeasureArrayList.size(); i++) {
                        String Safety = safetyMeasureArrayList.get(i).getProjectId();
                        user5[i] = Safety.toString();
                        safetyradio = TextUtils.join(",", user5);

                        if (safetyMeasureArrayList.get(i).getProjectCode().equals("ACMB")) {
                            if (safetyMeasureArrayList.get(i).getRemarks() == null ||
                                    safetyMeasureArrayList.get(i).getRemarks().equals("")) {
                                safety_BEL = "";
                            } else {
                                safety_BEL = safetyMeasureArrayList.get(i).getRemarks();
                            }
                        } else if (safetyMeasureArrayList.get(i).getProjectCode().equals("ORS")) {
                            if (safetyMeasureArrayList.get(i).getRemarks() == null ||
                                    safetyMeasureArrayList.get(i).getRemarks().equals("")) {
                                safety_Others = "";
                            } else {
                                safety_Others = safetyMeasureArrayList.get(i).getRemarks();
                            }
                        } else if (safetyMeasureArrayList.get(i).getProjectCode().equals("EIP")) {
                            if (safetyMeasureArrayList.get(i).getRemarks() == null ||
                                    safetyMeasureArrayList.get(i).getRemarks().equals("")) {
                                safety_extinguisher = "";
                            } else {
                                safety_extinguisher = safetyMeasureArrayList.get(i).getRemarks();
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

            Activityjson = new JSONObject();

            // Activityjson.put("",formcode);
            Activityjson.put("FormId", PKFormId);
            Activityjson.put("PermitNo", PermitNo);
            Activityjson.put("FkWorkAuthorizationNo", WAH_No);
            Activityjson.put("FkWareHouseMasterId", StationId);
            Activityjson.put("PermitFromDate", StartDate);
            Activityjson.put("PermitFromTime", permitfromtime);
            Activityjson.put("PermitToTime", permittotime);
            Activityjson.put("fkContractorId", contractorId);
            Activityjson.put("fkOperationMasterId", NatureOperation);
            Activityjson.put("FkLocationMasterId", LocationId);
            Activityjson.put("SafetyMeasuresId", safetyradio);
            Activityjson.put("SafetyToolMasterId", safetytools);
            Activityjson.put("MethodOperationStatus", checkoperation);
            Activityjson.put("AuthorizeBy1", Authorize1Id);
            Activityjson.put("AuthorizeBy2", Authorize2Id);
            Activityjson.put("RespContractorId", contractorId);
            Activityjson.put("PermitCloseBy", PermitclosedId);
            Activityjson.put("PermitCloseDate", permitcloseddate);
            Activityjson.put("SpotCheckBy", SpotcheckId);
            Activityjson.put("SpotCheckDate", Spotcheckdate);
            Activityjson.put("PermitCancelBy", cancelPermitId);
            Activityjson.put("PermitCancelDate", cancelDate);
            Activityjson.put("SpotImage", SpotCheckimgNamePost);

            Activityjson.put("UserManagerAuthDate", AuthorizeDate1);
            Activityjson.put("SurveillanceAuthDate", AuthorizeDate2);
            Activityjson.put("ContractorManagerAuthDate", AuthorizeDate3);
            Activityjson.put("PermitCloseRemark", permitcloseremark);
            Activityjson.put("HW_SafetyOthers", safety_Others);
            Activityjson.put("HW_ProOthers", safetyTools_Others);
            Activityjson.put("HW_SafetyBEL", safety_BEL);
            Activityjson.put("HW_SafetyExt", safety_extinguisher);
            Activityjson.put("GoldenRulesId", goldenRulesList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String FinalJsonObject = Activityjson.toString();

        if (CommonClass.checkNet(HOTWorkActivity.this)) {
            showProgress();
            new StartSession(HOTWorkActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadPostData().execute(FinalJsonObject);
                }

                @Override
                public void callfailMethod(String msg) {
                    CommonClass.displayToast(HOTWorkActivity.this, msg);
                    dismissProgress();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }


    }

    private void editjson() {
        try {

            StartDate = edt_fromdate.getText().toString();
            permitfromtime = edt_fromtime.getText().toString();
            permittotime = edt_totime.getText().toString();
            permitcloseddate = edt_permit_date.getText().toString();
            permitcloseremark = edit_remarks.getText().toString();
            Spotcheckdate = edt_spot_date.getText().toString();
            cancelDate = edt_cancel_date.getText().toString();
            AuthorizeDate1 = edt_authorize_date.getText().toString();
            AuthorizeDate2 = edt_authorize_date1.getText().toString();
            AuthorizeDate3 = edt_authorize_date2.getText().toString();
            // String PermitNo = edit_permitno.getText().toString();
            // String PermitNo = edit_permitno.getText().toString();


            safetyToolsArrayList = safetyAdapter.getArrayList();

            if (safetyToolsArrayList.size() > 0) {
                if (safetyToolsArrayList.size() > 0) {
                    user = new String[safetyToolsArrayList.size()];
                    for (int i = 0; i < safetyToolsArrayList.size(); i++) {
                        String Safety = safetyToolsArrayList.get(i).getSafetyToolMasterId();
                        user[i] = Safety.toString();
                        safetytools = TextUtils.join(",", user);
                        if (safetyToolsArrayList.get(i).getSafetyToolDesc().equals("Other(s)")) {
                            if (safetyToolsArrayList.get(i).getRemarks() == null) {
                                safetyTools_Others = "";
                            } else {
                                safetyTools_Others = safetyToolsArrayList.get(i).getRemarks();
                            }
                        }
                    }

                }
            }

            safetyMeasureArrayList = safetyMeasureListAdapter.getArrayList();
            if (safetyMeasureArrayList.size() > 0) {
                if (safetyMeasureArrayList.size() > 0) {
                    user5 = new String[safetyMeasureArrayList.size()];
                    for (int i = 0; i < safetyMeasureArrayList.size(); i++) {
                        String Safety = safetyMeasureArrayList.get(i).getProjectId();
                        user5[i] = Safety.toString();
                        safetyradio = TextUtils.join(",", user5);

                        if (safetyMeasureArrayList.get(i).getProjectCode().equals("ACMB")) {
                            if (safetyMeasureArrayList.get(i).getRemarks() == null ||
                                    safetyMeasureArrayList.get(i).getRemarks().equals("")) {
                                safety_BEL = "";
                            } else {
                                safety_BEL = safetyMeasureArrayList.get(i).getRemarks();
                            }
                        } else if (safetyMeasureArrayList.get(i).getProjectCode().equals("ORS")) {
                            if (safetyMeasureArrayList.get(i).getRemarks() == null ||
                                    safetyMeasureArrayList.get(i).getRemarks().equals("")) {
                                safety_Others = "";
                            } else {
                                safety_Others = safetyMeasureArrayList.get(i).getRemarks();
                            }
                        } else if (safetyMeasureArrayList.get(i).getProjectCode().equals("EIP")) {
                            if (safetyMeasureArrayList.get(i).getRemarks() == null ||
                                    safetyMeasureArrayList.get(i).getRemarks().equals("")) {
                                safety_extinguisher = "";
                            } else {
                                safety_extinguisher = safetyMeasureArrayList.get(i).getRemarks();
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

            Activityjson = new JSONObject();

            // Activityjson.put("",formcode);
            Activityjson.put("FormId", PKFormId);
            Activityjson.put("PermitNo", PermitNo);
            Activityjson.put("FkWorkAuthorizationNo", WAH_No);
            Activityjson.put("FkWareHouseMasterId", StationId);
            Activityjson.put("PermitFromDate", StartDate);
            Activityjson.put("PermitFromTime", permitfromtime);
            Activityjson.put("PermitToTime", permittotime);
            Activityjson.put("fkContractorId", contractorId);
            Activityjson.put("fkOperationMasterId", NatureOperation);
            Activityjson.put("FkLocationMasterId", LocationId);
            Activityjson.put("SafetyMeasuresId", safetyradio);
            Activityjson.put("SafetyToolMasterId", safetytools);
            Activityjson.put("MethodOperationStatus", checkoperation);
            Activityjson.put("AuthorizeBy1", Authorize1Id);
            Activityjson.put("AuthorizeBy2", Authorize2Id);
            Activityjson.put("RespContractorId", contractorId);
            Activityjson.put("PermitCloseBy", PermitclosedId);
            Activityjson.put("PermitCloseDate", permitcloseddate);
            Activityjson.put("SpotCheckBy", SpotcheckId);
            Activityjson.put("SpotCheckDate", Spotcheckdate);
            Activityjson.put("PermitCancelBy", cancelPermitId);
            Activityjson.put("PermitCancelDate", cancelDate);
            Activityjson.put("UserManagerAuthDate", AuthorizeDate1);
            Activityjson.put("SurveillanceAuthDate", AuthorizeDate2);
            Activityjson.put("ContractorManagerAuthDate", AuthorizeDate3);
            /*Activityjson.put("",AuthorizeDate1);
            Activityjson.put("",AuthorizeDate2);
            Activityjson.put("",AuthorizeDate3);*/
            Activityjson.put("PermitCloseRemark", permitcloseremark);
            Activityjson.put("HW_SafetyOthers", safety_Others);
            Activityjson.put("HW_ProOthers", safetyTools_Others);
            Activityjson.put("HW_SafetyBEL", safety_BEL);
            Activityjson.put("HW_SafetyExt", safety_extinguisher);
            Activityjson.put("GoldenRulesId", goldenRulesList);

            Activityjson.put("SpotImage", SpotCheckimgNamePost);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String FinalEditJsonObject = Activityjson.toString();

        if (CommonClass.checkNet(HOTWorkActivity.this)) {
            showProgress();
            new StartSession(HOTWorkActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadEditPostData().execute(FinalEditJsonObject);
                }

                @Override
                public void callfailMethod(String msg) {
                    CommonClass.displayToast(HOTWorkActivity.this, msg);
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
                String url = CompanyURL + WebAPIUrl.api_PostHOTWork;
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
                Toast.makeText(HOTWorkActivity.this, "Data Saved Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(HOTWorkActivity.this, SupervisorMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                //Intent intent = new Intent(AssignActivity.this, ActivityMain.class);
                //startActivity(intent);
            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {
                Toast.makeText(HOTWorkActivity.this, "Unable to Save Data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(HOTWorkActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
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
                String url = CompanyURL + WebAPIUrl.api_PosteditHotWork;
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
                Toast.makeText(HOTWorkActivity.this, "Data Updated Successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(HOTWorkActivity.this, PermitStatusActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                //startActivity(new Intent(HOTWorkActivity.this, HOTWorkActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                //Intent intent = new Intent(AssignActivity.this, ActivityMain.class);
                //startActivity(intent);
            } else if (val.equalsIgnoreCase(WebAPIUrl.Errormsg)) {
                Toast.makeText(HOTWorkActivity.this, "Unable to Update Data", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(HOTWorkActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
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

    class DownloadIsValidUser extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(HOTWorkActivity.this);
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
                res = CommonClass.OpenConnection(url, HOTWorkActivity.this);
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
                // b.dismiss();
                Toast.makeText(HOTWorkActivity.this, "Valid Password", Toast.LENGTH_SHORT).show();
                /*if (ReasonVal.equals("1") || ReasonVal.equals("2") && Mode.equals("E")) {
                    btn_submit.setVisibility(View.VISIBLE);
                }*/

                cusDailog.setVisibility(View.GONE);
                cusDialog1.setVisibility(View.GONE);
                spinner_authorize.setSelection(0);
                edit_password_pass.setText("");
                spinner_permit_closed.setSelection(0);
                edit_password_reason.setText("");
                edit_reason.setText("");
                hideKeyboard(HOTWorkActivity.this);

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

    public void initview() {
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        spinner_station = (Spinner) findViewById(R.id.spinner_station);
        spinner_contractor = findViewById(R.id.spinner_contractor);
        txt_authorize = (TextView) findViewById(R.id.spinner_authorize);
        txt_authorize1 = (TextView) findViewById(R.id.spinner_authorize1);
        txt_permit_closed = (TextView) findViewById(R.id.txt_permit_closed);
        txt_spot_check = (TextView) findViewById(R.id.txt_spotcheck);
        txt_cancel = (TextView) findViewById(R.id.txt_cancel);

        spinner_prevention_plan = findViewById(R.id.spinner_prevention_plan);
        edit_remarks = findViewById(R.id.edit_remarks);
        edit_permitno = (EditText) findViewById(R.id.edit_permitno);

        edt_fromdate = findViewById(R.id.edt_fromdate);
        // edt_todate = findViewById(R.id.edt_todate);
        edt_authorize_date = findViewById(R.id.edt_authorize_date);
        edt_authorize_date1 = findViewById(R.id.edt_authorize_date1);
        edt_authorize_date2 = findViewById(R.id.edt_authorize_date2);
        edt_permit_date = findViewById(R.id.edt_permit_date);
        edt_spot_date = findViewById(R.id.edt_spot_date);
        edt_cancel_date = findViewById(R.id.edt_cancel_date);
        btn_submit = findViewById(R.id.btn_submit);
        edt_fromtime = findViewById(R.id.edt_fromtime);
        edt_totime = findViewById(R.id.edt_totime);
        edt_totime.setEnabled(false);
        edt_totime.setTextColor(Color.parseColor("#000000"));
        spinner_authorize = (Spinner) findViewById(R.id.spinner_authorize_pas);
        // spinner_authorize1 = findViewById(R.id.spinner_authorize_pas);
        spinner_authorize2 = findViewById(R.id.spinner_authorize2);
        spinner_authorize2.setEnabled(false);
        spinner_permit_closed = findViewById(R.id.spinner_permit_closed1);
        img_camera = findViewById(R.id.img_camera);
        img_camera1 = findViewById(R.id.img_camera1);
        checkbox_method = (CheckBox) findViewById(R.id.checkbox_method);
        radio_selection = (RadioGroup) findViewById(R.id.radiogrp_safetyselect);
        rbtnyes = (RadioButton) findViewById(R.id.radiobtn_yes);
        rbtnno = (RadioButton) findViewById(R.id.radiobtn_no);
        rbtndone = (RadioButton) findViewById(R.id.radiobtn_done);


        spinner_operation = findViewById(R.id.spinner_operation);
        spinner_location = findViewById(R.id.spinner_location);

        grid_safety = findViewById(R.id.grid_safety);
        list_safety_measure = findViewById(R.id.list_safety_measure);
        cusDailog = findViewById(R.id.cusDailog);
        cusDialog1 = findViewById(R.id.cusDailog1);
        // cusSpotDialog = findViewById(R.id.cusDailog1);
        //cusSpotDialog = findViewById(R.id.cusSpotdialog);
        txt_cancel_pass = findViewById(R.id.txt_cancel_pass);
        txt_submit_pass = findViewById(R.id.txt_submit_pass);
        edit_password_pass = findViewById(R.id.edt_password_pass);

        spinner_spotcheck = findViewById(R.id.spinner_permit_closed1);
        txt_submit_reason = findViewById(R.id.txt_submit_reason);
        txt_cancel_reason = findViewById(R.id.txt_cancel_reason);
        edit_reason = findViewById(R.id.edt_reason);
        edit_password_reason = findViewById(R.id.edt_password_reason);

        ln_station = findViewById(R.id.ln_station);
        ln_contractor = findViewById(R.id.ln_contractor);
        ln_natureOperation = findViewById(R.id.ln_natureOperation);
        ln_locationOperation = findViewById(R.id.ln_locationOperation);
        len_prevention = findViewById(R.id.len_prevention);
        len_p_closed = findViewById(R.id.len_p_closed);
        len_cancel_permit = findViewById(R.id.len_cancel_permit);
        tx_p_closed = findViewById(R.id.tx_p_closed);
        txt_cancel_permit = findViewById(R.id.txt_cancel_permit);
        ln_spinner_authorize = findViewById(R.id.ln_spinner_authorize);
        ln_spinner_reason = findViewById(R.id.ln_spinner_reason);
        list_goldenRules = findViewById(R.id.list_goldenRules);
        spot_img_display = findViewById(R.id.spot_img_display);
    }

    public void setListner() {

        spot_img_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HOTWorkActivity.this, ImageFullScreenActivity.class).putExtra("share_image_path", SpotCheckimgNamePost));
            }
        });


        radio_selection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbtn = (RadioButton) group.findViewById(checkedId);
                if (rbtn == rbtnno) {
                    rbtndone.setVisibility(View.GONE);
                } else {
                    rbtndone.setVisibility(View.VISIBLE);
                    rbtndone.setChecked(true);
                }
            }
        });


        txt_cancel_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text);
                edit_password_pass.setBackgroundResource(R.drawable.edit_text);
                if (cusDailog.getVisibility() == View.VISIBLE) {
                    cusDailog.setVisibility(View.GONE);
                    if (tempVal.equals("0")) {
                        txt_authorize.setText("Select");
                        spinner_authorize.setSelection(0);
                        edit_password_pass.setText("");
                    } else if (tempVal.equals("1")) {
                        txt_authorize1.setText("Select");
                        spinner_authorize.setSelection(0);
                        edit_password_pass.setText("");
                    }

                    hideKeyboard(HOTWorkActivity.this);

                } else {

                }

            }
        });

        txt_cancel_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cusDialog1.setVisibility(View.GONE);
                if (ReasonVal.equals("1")) {
                    txt_permit_closed.setText("Select");
                    spinner_permit_closed.setSelection(0);
                    edit_password_reason.setText("");
                    edit_reason.setText("");
                    PermitclosedId = "";


                } else if (ReasonVal.equals("2")) {
                    txt_spot_check.setText("Select");
                    spinner_permit_closed.setSelection(0);
                    edit_password_reason.setText("");
                    edit_reason.setText("");
                    SpotcheckId = "";

                } else if (ReasonVal.equals("3")) {
                    txt_cancel.setText("Select");
                    spinner_permit_closed.setSelection(0);
                    edit_password_reason.setText("");
                    edit_reason.setText("");
                    cancelPermitId = "";

                }
                if ((PermitclosedId.equalsIgnoreCase("Select") || PermitclosedId.equals(""))
                        && (SpotcheckId.equalsIgnoreCase("Select") || SpotcheckId.equals(""))) {
                    // btn_submit.setVisibility(View.GONE);
                } else {
                    btn_submit.setVisibility(View.VISIBLE);
                }
                hideKeyboard(HOTWorkActivity.this);
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

                    Toast toast = Toast.makeText(HOTWorkActivity.this, "Please enter authorized person,password and reason", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(HOTWorkActivity.this, "Please select authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(HOTWorkActivity.this, "Please valid password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (!Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    Toast toast = Toast.makeText(HOTWorkActivity.this, "Please enter reason", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && !(PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    //password and reason blank
                    Toast toast = Toast.makeText(HOTWorkActivity.this, "Please valid password and reason", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && !Reason.equalsIgnoreCase("")) {
                    //password and permit
                    Toast toast = Toast.makeText(HOTWorkActivity.this, "Please valid password and authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_reason.setBackgroundResource(R.drawable.edit_text_red);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (!Password.equalsIgnoreCase("") && (PermitClosed.equalsIgnoreCase("--Select--")
                        || PermitClosed.equalsIgnoreCase("")) && Reason.equalsIgnoreCase("")) {
                    //reason and permit
                    Toast toast = Toast.makeText(HOTWorkActivity.this, "Please enter reason and authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_reason.setBackgroundResource(R.drawable.edit_text_red);
                    ln_spinner_reason.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else {
                    if (CommonClass.checkNet(HOTWorkActivity.this)) {
                        new StartSession(HOTWorkActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userloginId);
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


        txt_submit_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Password = edit_password_pass.getText().toString();
                //String Password = password.getText().toString();
                //String Reason= reason.getText().toString();
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));

                ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text);
                edit_password_pass.setBackgroundResource(R.drawable.edit_text);
                if (Password.equalsIgnoreCase("") && (authorize.equalsIgnoreCase("--Select--")
                        || authorize.equalsIgnoreCase(""))) {

                    Toast toast = Toast.makeText(HOTWorkActivity.this, "Please enter authorized person and password", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(HOTWorkActivity.this, "Please select authorized person", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    ln_spinner_authorize.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else if (Password.equalsIgnoreCase("") && (!authorize.equalsIgnoreCase("--Select--")
                        || !authorize.equalsIgnoreCase(""))) {
                    Toast toast = Toast.makeText(HOTWorkActivity.this, "Please enter valid password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL, 0, 100);
                    edit_password_pass.setBackgroundResource(R.drawable.edit_text_red);
                    toast.show();
                } else {
                    if (CommonClass.checkNet(HOTWorkActivity.this)) {
                        new StartSession(HOTWorkActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadIsValidUser().execute(userloginId);
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


        //station
        spinner_station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StationId = depotArrayList.get(position).getDepotid();

                if (StationId.equals("Select")) {

                } else {

                    if (CommonClass.checkNet(HOTWorkActivity.this)) {
                        showProgress();
                        new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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


        txt_authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // authorizedialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 1", "fromPwd");
                cusDailog.setVisibility(View.VISIBLE);
                tempVal = "0";


            }
        });
        txt_authorize1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // authorize1dialog();

                //Tier 1 + tier 2
                CategoryWiseAuthorizeName("level 2", "fromPwd");
                // CategoryWiseAuthorizeName("level 2 ", "fromPwd");
                cusDailog.setVisibility(View.VISIBLE);
                tempVal = "1";
            }
        });

        txt_permit_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reasondialog();
                //Tier1
                CategoryWiseAuthorizeName("level 1", "fromReason");
                cusDialog1.setVisibility(View.VISIBLE);
                ReasonVal = "1";

            }
        });
        txt_spot_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //spotcheckdialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 1", "fromReason");
                cusDialog1.setVisibility(View.VISIBLE);
                ReasonVal = "2";
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //spotcheckdialog();
                //Tier 1
                CategoryWiseAuthorizeName("level 1", "fromReason");
                cusDialog1.setVisibility(View.VISIBLE);
                ReasonVal = "3";
            }
        });


        //authorized person
        spinner_authorize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (++check > 1) {
                    if (txt_authorizeArrayList == null) {

                    } else {
                        authorize = txt_authorizeArrayList.get(position).getAuthorizeid();
                        userloginId = txt_authorizeArrayList.get(position).getUserLoginId();
                        String name = txt_authorizeArrayList.get(position).getAuthorizename();

                        if (tempVal.equals("0")) {
                            txt_authorize.setText(name);
                            Authorize1Id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (tempVal.equals("1")) {
                            txt_authorize1.setText(name);
                            Authorize2Id = txt_authorizeArrayList.get(position).getAuthorizeid();
                        }

                    }
                }

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
                    checkoperation = "Y";

                } else {
                    checkoperation = "N";
                }
            }

        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* String authorize1pos=txt_authorize.getText().toString();
                String  authorize2pos=txt_authorize1.getText().toString();
                int authorize3pos = spinner_authorize2.getSelectedItemPosition();
                String permitcheckpos=txt_authorize.getText().toString();
                String spotcheckpos=txt_authorize.getText().toString();*/
                if (StationId.equalsIgnoreCase("Select") || StationId.equalsIgnoreCase("")) {
                    Toast.makeText(HOTWorkActivity.this, "Please select station", Toast.LENGTH_SHORT).show();
                    ln_station.setBackgroundResource(R.drawable.edit_text_red);
                } else if (WAH_No.equalsIgnoreCase("") || WAH_No.equalsIgnoreCase("Select")) {
                    Toast.makeText(HOTWorkActivity.this, "Please select work authorization no ", Toast.LENGTH_SHORT).show();
                    len_prevention.setBackgroundResource(R.drawable.edit_text_red);
                } else if (contractorId.equalsIgnoreCase("") || contractorId.equalsIgnoreCase("Select")) {
                    Toast.makeText(HOTWorkActivity.this, "Please select contractor ", Toast.LENGTH_SHORT).show();
                    ln_contractor.setBackgroundResource(R.drawable.edit_text_red);
                } else if (OperationName.equalsIgnoreCase("") || OperationName.equalsIgnoreCase("Select")) {
                    Toast.makeText(HOTWorkActivity.this, "Please select nature of operation", Toast.LENGTH_SHORT).show();
                    ln_natureOperation.setBackgroundResource(R.drawable.edit_text_red);
                } else if (LocationName.equalsIgnoreCase("") || LocationName.equalsIgnoreCase("Select")) {
                    Toast.makeText(HOTWorkActivity.this, "Please select location of operation", Toast.LENGTH_SHORT).show();
                    ln_locationOperation.setBackgroundResource(R.drawable.edit_text_red);
                } else if (checkoperation.equalsIgnoreCase("")) {
                    Toast.makeText(HOTWorkActivity.this, "Please check method of operation", Toast.LENGTH_SHORT).show();
                } else {

                    if (Mode.equalsIgnoreCase("A")) {
                        saveactivityjson();
                        // startActivity(new Intent(HOTWorkActivity.this, HOTWorkActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else if (Mode.equalsIgnoreCase("E")) {
                        editjson();
                        //startActivity(new Intent(HOTWorkActivity.this, PermitStatusActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else {

                    }

                }

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
                        }
                    }

                    if (contractorpos != -1) {
                        spinner_authorize2.setSelection(contractorpos);
                    } else {
                        spinner_authorize2.setSelection(0);
                    }


                    if (CommonClass.checkNet(HOTWorkActivity.this)) {
                        showProgress();
                        //Location Get
                        new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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
                        if (CommonClass.checkNet(HOTWorkActivity.this)) {
                            new StartSession(HOTWorkActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadWANo().execute(contractorId);
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    Toast.makeText(HOTWorkActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(HOTWorkActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    }


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_prevention_plan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (Mode.equals("E")) {

                } else {
                    if (WAArayList.size() > 0) {

                        WAH_No = WAArayList.get(position).getPermitNo();

                        if (WAH_No != "") {
                            if (CommonClass.checkNet(HOTWorkActivity.this)) {
                                showProgress();
                                //Location Get
                                new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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
                                Toast.makeText(HOTWorkActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
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
                if(contractorListActivityArrayList == null){

                }else{
                    if(permit!=null){
                        contractorId = contractorListActivityArrayList.get(position).getCustVendorMasterId();
                        WAH_No = contractorListActivityArrayList.get(position).getCustVendorCode();
                        edit_Prevention_plan.setText(WAH_No);
                    }else{
                        contractorId = contractorListActivityArrayList.get(position).getCustVendorMasterId();
                        WAH_No = contractorListActivityArrayList.get(position).getCustVendorCode();
                        edit_Prevention_plan.setText(WAH_No);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        spinner_operation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (operationArrayList == null) {

                } else {

                    NatureOperation = operationArrayList.get(position).getOperationMasterId();
                    OperationName = operationArrayList.get(position).getOperation();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (LocationArraylist == null) {

                } else {
                    LocationId = LocationArraylist.get(position).getLocationMasterId();
                    LocationName = LocationArraylist.get(position).getLocationDesc();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempImgVal = "2";
                if (ContextCompat.checkSelfPermission(HOTWorkActivity.this, Manifest.permission.CAMERA)
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


        spinner_permit_closed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                    if (txt_authorizeArrayList == null) {

                    } else {
                        PermitClosed = txt_authorizeArrayList.get(position).getAuthorizeid();
                        userloginId = txt_authorizeArrayList.get(position).getUserLoginId();
                        String Permitname = txt_authorizeArrayList.get(position).getAuthorizename();
                        if (ReasonVal.equals("1")) {
                            txt_permit_closed.setText(Permitname);
                            PermitclosedId = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (ReasonVal.equals("2")) {
                            txt_spot_check.setText(Permitname);
                            SpotcheckId = txt_authorizeArrayList.get(position).getAuthorizeid();
                        } else if (ReasonVal.equals("3")) {
                            txt_cancel.setText(Permitname);
                            cancelPermitId = txt_authorizeArrayList.get(position).getAuthorizeid();
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /*public class DownloadGETWA_PermitNoDetail extends AsyncTask<String, Void, String> {

        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String Id = params[0];

            String url = CompanyURL + WebAPIUrl.api_GETWA_PermitNoDetail + "?permitno=" + Id;


            res = CommonClass.OpenConnection(url, HOTWorkActivity.this);
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

                        NatureOperation = jorder.getString("fkOperationMasterId");

                        int operationpos = -1;
                        if (operationArrayList != null) {
                            for (int j = 0; j < operationArrayList.size(); j++) {
                                if (operationArrayList.get(j).getOperationMasterId().equals(NatureOperation))
                                    ;
                                operationpos = j;
                                break;
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
                            LocationId = jorder.getString("FkLocationMasterId");
                           // Mode = "A";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // WANoDetails waNoDetail = new WANoDetails();

                        *//*waNoDetail.setFkOperationMasterId(jorder.getString("FkOperationMasterId"));
                        waNoDetail.setFkLocationMasterId(jorder.getString("FkLocationMasterId"));
                        waNoDetail.setFkWareHouseMasterId(jorder.getString("FkWareHouseMasterId"));
                        //jorder.getString("permitno");
                        waNoDetails.add(waNoDetail);*//*
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(HOTWorkActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
            }


        }
    }*/


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


            res = CommonClass.OpenConnection(url, HOTWorkActivity.this);
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

                        NatureOperation = jorder.getString("fkOperationMasterId");

                        int operationpos = -1;
                        if (operationArrayList != null) {
                            for (int j = 0; j < operationArrayList.size(); j++) {
                                if (operationArrayList.get(j).getOperationMasterId().equals(NatureOperation)) {
                                    operationpos = j;
                                    break;
                                }
                            }

                            if (operationpos != -1)
                                spinner_operation.setSelection(operationpos);
                            else
                                spinner_operation.setSelection(0);
                        }

                        /*[{
	"fkOperationMasterId": "e815ed37-784b-4705-87d8-a89332853be3",
	"FkLocationMasterId": "C289E4C1-AD6E-4C8F-9463-767BBF12717D",
	"FkWareHouseMasterId": "94236509-034f-439f-9e44-055369fcb111",
	"PermitFromDate": "5-03-2021",
	"PermitTodate": "11-03-2021",
	"FromTime1": "9:00 AM",
	"ToTime2": "6:00 PM"
}]*/
                        if (modeefirst == -1) {
                            WAStartTime = jorder.getString("FromTime1");
                            WAEndTime1 = jorder.getString("ToTime2");
                            WAEndTime = jorder.getString("ToTime2");
                            edt_fromtime.setText(WAStartTime);
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
                                        Toast.makeText(HOTWorkActivity.this, "You cannot choose time greater than work authorization end time", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            edt_totime.setText(WAEndTime);
                            modeefirst = 1;
                        }
                        if (Mode.equals("A")) {

                            LocationId = jorder.getString("FkLocationMasterId");
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
                                if (CommonClass.checkNet(HOTWorkActivity.this)) {
                                    new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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
                        }

                        // Mode = "A";


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
                spinner_contractor.setEnabled(false);
                spinner_location.setEnabled(false);
                spinner_operation.setEnabled(false);

            } else {

                if (Mode.equals("A")) {
                    spinner_contractor.setEnabled(true);
                    //  spinner_location.setEnabled(true);
                    //  spinner_operation.setEnabled(true);

                    spinner_operation.setSelection(0);
                    spinner_location.setSelection(0);
                }
                Toast.makeText(HOTWorkActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
            }


        }
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

            String url = CompanyURL + WebAPIUrl.api_GetWANo + "?contractid=" + Id + "&permitOperationcode=HWPF";


            res = CommonClass.OpenConnection(url, HOTWorkActivity.this);
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
                    ArrayAdapter<PermitNoWA> permitNoArrayAdapter = new ArrayAdapter<>(HOTWorkActivity.this,
                            android.R.layout.simple_spinner_item, WAArayList);
                    spinner_prevention_plan.setAdapter(permitNoArrayAdapter);
                    spinner_contractor.setEnabled(false);
                    //spinner_permit_closed.setAdapter(authorizedPersonAdapter);
                    if (WAH_No != "") {
                        spinner_location.setEnabled(false);
                        spinner_operation.setEnabled(false);
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

                    if (CommonClass.checkNet(HOTWorkActivity.this)) {
                        showProgress();
                        //Location Get
                        new StartSession(HOTWorkActivity.this, new CallbackInterface() {
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
                        Toast.makeText(HOTWorkActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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
                        ArrayAdapter<PermitNoWA> permitNoArrayAdapter = new ArrayAdapter<>(HOTWorkActivity.this,
                                android.R.layout.simple_spinner_item, WAArayList);
                        spinner_prevention_plan.setAdapter(permitNoArrayAdapter);
                    }
                } else {

                    Toast.makeText(HOTWorkActivity.this, "No WorkAuthorization Present Against Selected Contractor", Toast.LENGTH_SHORT).show();
                    PermitNoWA permitNo = new PermitNoWA();
                    permitNo.setPermitNo("Select");
                    WAArayList.add(0, permitNo);
                    ArrayAdapter<PermitNoWA> permitNoArrayAdapter = new ArrayAdapter<>(HOTWorkActivity.this,
                            android.R.layout.simple_spinner_item, WAArayList);
                    spinner_prevention_plan.setAdapter(permitNoArrayAdapter);
                    spinner_operation.setSelection(0);
                    spinner_location.setSelection(0);
                }
            }


        }


    }


    public void dateListner() {

        edt_fromtime.setOnClickListener(new View.OnClickListener() {
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
               // UpdateTime.updateTime(hour, minute);

              //  edt_fromtime.setText(UpdateTime.updateTime(hour, minute));
               // edt_totime.setText(UpdateTime.updateTime(hour1, minute));

                mTimePicker = new TimePickerDialog(HOTWorkActivity.this,
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
                                    edt_fromtime.setText(time);
                                    if ((selectedHour + 4) > WAEndTimeHr) {
                                        edt_totime.setText(WAEndTime1);

                                    } else {
                                        if (selectedHour > 10 && selectedHour < 13) {
                                            time = time1 = UpdateTime.updateTime((selectedHour + 5), selectedMinute);
                                            edt_totime.setText(time);
                                        } else if (selectedHour == 13) {
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
                                            String msg = "Please select time between " + WAStartTime + " and " + WAEndTime1;
                                            Toast.makeText(HOTWorkActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        } else {
                                            edt_totime.setText(time1);
                                        }


                                    }


                                } else if (WAStartTimeHr > selectedHour) {
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
                                    String msg = "Please select time between " + WAStartTime + " and " + WAEndTime1;
                                    Toast.makeText(HOTWorkActivity.this, msg, Toast.LENGTH_SHORT).show();
                                } else {
                                    if (WAStartTimeHr < selectedHour) {
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
                                        String msg = "Please select time between " + WAStartTime + " and " + WAEndTime1;
                                        Toast.makeText(HOTWorkActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    } else if (WAStartTimeHr == selectedHour && selectedMinute >= WAStartTimemin) {
                                        edt_totime.setText(time1);
                                        edt_fromtime.setText(time);
                                    } else {
                                        if (WAEndTimeHr >= selectedHour && WAEndTimeMin >= selectedHour) {
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
                                            String msg = "Please select time between " + WAStartTime + " and " + WAEndTime1;

                                            Toast.makeText(HOTWorkActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }


                            }
                        }, hour, minute, false);// Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        edt_totime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                //int hour1 = hour + 4;

                TimePickerDialog mTimePicker;

                edt_totime.setText(UpdateTime.updateTime(hour, minute));
                //edt_totime.setText(hour1 + ":" + minute + " ");

                mTimePicker = new TimePickerDialog(HOTWorkActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                String time2 = "";
                                String time = UpdateTime.updateTime(selectedHour, selectedMinute);

                                boolean val = timedifference(time, to_time);
                                if (val) {
                                    edt_totime.setText(time);
                                } else {
                                    edt_totime.setText(to_time);
                                }


                            }
                        }, hour, minute, false); // Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

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
                datePickerDialog = new DatePickerDialog(HOTWorkActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                edt_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year));
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);

                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            edt_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            edt_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(HOTWorkActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            edt_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        edt_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        edt_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    edt_fromdate.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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


        edt_authorize_date.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(HOTWorkActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                edt_authorize_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year));
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            edt_authorize_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                                    + (month + 1) + "-" + Year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            edt_authorize_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                                    + (month + 1) + "-" + Year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(HOTWorkActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            edt_authorize_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                                    + (month + 1) + "-" + Year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        edt_authorize_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                                + (month + 1) + "-" + Year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        edt_authorize_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                                + (month + 1) + "-" + Year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    edt_authorize_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                            + (month + 1) + "-" + Year));
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

        edt_authorize_date1.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(HOTWorkActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                edt_authorize_date1.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);
                                edt_authorize_date1.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year));

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
                                            Toast.makeText(HOTWorkActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
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

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);


            }
        });

        edt_authorize_date2.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(HOTWorkActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                /*edt_authorize_date2.setText(dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);*/
                                edt_authorize_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year));

                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
                                        + (monthOfYear + 1) + " - " + year);


                                if (year >= Year) {
                                    if ((year == Year) && (monthOfYear >= month)) {
                                        if ((monthOfYear == month) && (dayOfMonth >= day)) {
                                            edt_authorize_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        } else if ((monthOfYear == month) && (dayOfMonth < day)) {
                                            edt_authorize_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                                    + (month + 1) + "-" + Year));
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(HOTWorkActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            edt_authorize_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                                    + (month + 1) + "-" + Year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        edt_authorize_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                                + (month + 1) + "-" + Year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        edt_authorize_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                                + (month + 1) + "-" + Year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    edt_authorize_date2.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                            + (month + 1) + "-" + Year));
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

        edt_spot_date.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(HOTWorkActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                edt_spot_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", day + "-"
                                        + (month + 1) + "-" + Year));
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
                                            Toast.makeText(HOTWorkActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
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
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);


            }
        });

        edt_cancel_date.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(HOTWorkActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                edt_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year));
                                String trnselectDate = year + " - " + (monthOfYear + 1)
                                        + "-" + dayOfMonth + " 00:00:00.000";

                                // String seldate = trnselectDate;
                                String SelectedDate = (dayOfMonth + " - "
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
                                            trnselectDate = Year + " - " + (month + 1)
                                                    + " - " + day + " 00:00:00.000";
                                            //2018-01-15 16:43:40.440
                                            Toast.makeText(HOTWorkActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                        } else if (monthOfYear > month) {
                                            edt_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                    + (monthOfYear + 1) + "-" + year));
                                            trnselectDate = year + " - " + (monthOfYear + 1)
                                                    + " - " + dayOfMonth + " 00:00:00.000";
                                        }
                                    } else if (year > Year) {
                                        edt_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        trnselectDate = year + " - " + (monthOfYear + 1)
                                                + " - " + dayOfMonth + " 00:00:00.000";
                                    } else if ((year == Year) && (monthOfYear < month)) {
                                        edt_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year));
                                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                                        trnselectDate = Year + " - " + (month + 1)
                                                + " - " + day + " 00:00:00.000";

                                        Toast.makeText(getApplicationContext(), "Past date is not accepted", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    edt_cancel_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
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

        edt_permit_date.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(HOTWorkActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                edt_permit_date.setText(DateFormatChange.formateDateFromstring("dd-MM-yyyy", "dd-MM-yyyy", dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year));
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
                                            Toast.makeText(HOTWorkActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
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

                /*datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());*/
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
                // edt_todate.setText(end_dayof_week);


            }
        });

        edt_permit_date.setOnClickListener(new View.OnClickListener() {
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
                datePickerDialog = new DatePickerDialog(HOTWorkActivity.this,
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
                                            Toast.makeText(HOTWorkActivity.this, "Past date is not accepted", Toast.LENGTH_SHORT).show();
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
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
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


    public void captureimg(int pos, String imgcode) {
        tempImgVal = "1";
        img_pos = pos;

        if (imgcode.equals("ACMB")) {

            if (ContextCompat.checkSelfPermission(HOTWorkActivity.this, Manifest.permission.CAMERA)
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
                    authorizedPersonAdapter = new AuthorizedPersonAdapter(HOTWorkActivity.this, txt_authorizeArrayList);
                    if (arrivalFrom.equalsIgnoreCase("fromReason")) {
                        spinner_permit_closed.setAdapter(authorizedPersonAdapter);
                    } else if (arrivalFrom.equalsIgnoreCase("fromPWD")) {
                        spinner_authorize.setAdapter(authorizedPersonAdapter);
                    }
                }
                //authorizedPersonAdapter.updateList(txt_authorizeArrayList);
                // spinner_authorize.setAdapter(authorizedPersonAdapter);
            }
        }
        else{
            AuthorizedPerson authorizedPerson = new AuthorizedPerson();
            authorizedPerson.setAuthorizename("Select");
            txt_authorizeArrayList.add(authorizedPerson);
            authorizedPersonAdapter = new AuthorizedPersonAdapter(HOTWorkActivity.this, txt_authorizeArrayList);
            spinner_permit_closed.setAdapter(authorizedPersonAdapter);
            spinner_authorize.setAdapter(authorizedPersonAdapter);
        }

    }


    private class DownloadAuthorizedPersonDataDepo extends AsyncTask<String,Void,String> {


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
            String url = CompanyURL + WebAPIUrl.api_GetUserListByDepo+"?DepoId="+id;

            try {
                res = CommonClass.OpenConnection(url, HOTWorkActivity.this);
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
                authorizedPersonAdapter = new AuthorizedPersonAdapter(HOTWorkActivity.this, authorizedPersonArrayList);
                spinner_authorize.setAdapter(authorizedPersonAdapter);
                spinner_permit_closed.setAdapter(authorizedPersonAdapter);


            }


        }
    }
}


/*************************************Not Used***************************/










