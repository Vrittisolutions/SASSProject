package com.vritti.sass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.sass.Interface.CallbackInterface;
import com.vritti.sass.adapter.AuthorizedPersonAdapter;
import com.vritti.sass.model.AuthorizedPerson;
import com.vritti.sass.model.CommonClass;
import com.vritti.sass.model.Question;
import com.vritti.sass.model.StartSession;
import com.vritti.sass.model.WebAPIUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.vritti.sass.LoginActivity.USERINFO;

public class ShiftHandOverDisplayActivity extends AppCompatActivity {


    TextView spinner_shift;
    CheckBox checkbox_condition,checkbox_hotwork,checkbox_permit_issue,
            checkbox_loto_permits,checkbox_plant_status;
    Button btn_hslevel,btn_seapot_pressure,btn_gantry_massflow,btn_submit;
    EditText edit_notes;
    String shift="",ConditionCheck="",HotWorkCheck = "",PermitIssueCheck="",LotoCheck="",PlantCheck="",Notes;
    private String Que1="",Que2="",Que3="",Ans1="",Ans2="",Ans3="",UserMasterId="";
    private SharedPreferences userpreferences;
    private String  FromTime="9 AM";
    private String  ToTime="10 PM";
    TextView txt_question,txt_ans,txt_question_1,txt_ans_1,txt_question_2,txt_ans_2,txt_question_3,txt_ans_3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_handover_mockup_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_logo);
        toolbar.setTitle("Shift Details");

        Intent intent = getIntent();


        spinner_shift = (TextView) findViewById(R.id.spinner_shift_details);
        checkbox_condition = (CheckBox)findViewById(R.id.checkbox_condition);
        checkbox_hotwork = (CheckBox)findViewById(R.id.checkbox_hotwork);
        checkbox_loto_permits = (CheckBox)findViewById(R.id.checkbox_loto_permits);
        checkbox_permit_issue = (CheckBox)findViewById(R.id.checkbox_permit_issue);
        checkbox_plant_status=findViewById(R.id.checkbox_plant_status);

       /* btn_hslevel = (Button)findViewById(R.id.btn_hslevel);
        btn_seapot_pressure = (Button)findViewById(R.id.btn_seapot_pressure);
        btn_gantry_massflow = (Button)findViewById(R.id.btn_gantry_massflow);*/
        btn_submit = (Button)findViewById(R.id.btn_submit);

        txt_question = (TextView) findViewById(R.id.txt_question);
        txt_ans = (TextView)findViewById(R.id.txt_ans);
        txt_question_1 = (TextView)findViewById(R.id.txt_question_1);
        txt_ans_1 = (TextView)findViewById(R.id.txt_ans_1);
        txt_question_2 = (TextView)findViewById(R.id.txt_question_2);
        txt_ans_2 = (TextView)findViewById(R.id.txt_ans_2);




        /*String s="Y O6C FRONT BLACK ";
        Log.d("B", String.valueOf(s.length()));
        String s1="B97-F5168-00 ";
        Log.d("B1", String.valueOf(s1.length()));
        String s2="QTY: 2 NOS ";
        Log.d("B2", String.valueOf(s2.length()));
        String s3="30012021.15:24.";
        Log.d("B2", String.valueOf(s3.length()));
*/

        edit_notes = (EditText)findViewById(R.id.edit_notes);


        ConditionCheck= getIntent().getStringExtra("PlantDowngardeCondition");
        HotWorkCheck=getIntent().getStringExtra("Hotwork");
        PermitIssueCheck=getIntent().getStringExtra("PermitIssue");
        LotoCheck=getIntent().getStringExtra("LotoPermit");
        PlantCheck=getIntent().getStringExtra("PlantStatus");
        Notes=getIntent().getStringExtra("Remark");
        Que1=getIntent().getStringExtra("Que1");
        Que2=getIntent().getStringExtra("Que2");
        Que3=getIntent().getStringExtra("Que3");
        Ans1=getIntent().getStringExtra("Ans1");
        Ans2=getIntent().getStringExtra("Ans2");
        Ans3=getIntent().getStringExtra("Ans3");
        shift=getIntent().getStringExtra("AddedBy");
        spinner_shift.setText(shift);
        edit_notes.setText(Notes);
        txt_question.setText(Que1);
        txt_question_1.setText(Que2);
        txt_question_2.setText(Que3);
        txt_ans.setText(Ans1);
        txt_ans_1.setText(Ans2);
        txt_ans_2.setText(Ans3);

        if (ConditionCheck.equals("Y")){
            checkbox_condition.setChecked(true);
        }else {
            checkbox_condition.setChecked(false);
        }
        if (PlantCheck.equals("Y")){
            checkbox_plant_status.setChecked(true);
        }else {
            checkbox_plant_status.setChecked(false);
        }
        if (PermitIssueCheck.equals("Y")){
            checkbox_permit_issue.setChecked(true);
        }else {
            checkbox_permit_issue.setChecked(false);
        }
        if (LotoCheck.equals("Y")){
            checkbox_loto_permits.setChecked(true);
        }else {
            checkbox_loto_permits.setChecked(false);
        }
        if (HotWorkCheck.equals("Y")){
            checkbox_hotwork.setChecked(true);
        }else {
            checkbox_hotwork.setChecked(false);
        }






        userpreferences = getSharedPreferences(USERINFO,
                Context.MODE_PRIVATE);
        UserMasterId = userpreferences.getString("UserMasterId", null);










    }








}
