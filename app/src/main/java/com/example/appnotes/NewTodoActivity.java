package com.example.appnotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NewTodoActivity extends AppCompatActivity {
    private EditText titledoes, descdoes, datedoes;
    private Button btnSaveTask, btnCancel;
    private DatabaseReference reference;
    private Integer TodoNum = new Random().nextInt();
    private String keytodo = Integer.toString(TodoNum);
    private Button btnSelectDate;
    private int mYear, mMonth, mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initView();
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c=Calendar.getInstance();
                mYear=c.get(Calendar.YEAR);
                mMonth=c.get(Calendar.MONTH);
                mDay=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(NewTodoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                datedoes.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                            }
                        },mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // doc vao csdl
                reference = FirebaseDatabase.getInstance().getReference().
                        child("mytodo").
                        child("Todo" + keytodo);
                Map<String, Object> data= new HashMap<>();
                data.put("title", titledoes.getText().toString());
                data.put("describe", descdoes.getText().toString());
                data.put("date", datedoes.getText().toString());
                data.put("key", keytodo);
                reference.setValue(data);
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        titledoes = findViewById(R.id.titledoes);
        descdoes = findViewById(R.id.descdoes);
        datedoes = findViewById(R.id.datedoes);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel = findViewById(R.id.btnCancel);
        btnSelectDate=findViewById(R.id.btnChosseDay);
    }
}