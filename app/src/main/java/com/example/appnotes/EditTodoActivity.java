package com.example.appnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appnotes.modelnotification.MyTodo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditTodoActivity extends AppCompatActivity {
    private static final String TAG = "Todo";
    EditText titledoes, descdoes, datedoes;
    Button btnEdit, btnDelete,btnCancel;
    DatabaseReference reference;
    Button btnSelectDate;
    private int mYear, mMonth, mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

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
                DatePickerDialog datePickerDialog=new DatePickerDialog(EditTodoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                datedoes.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                            }
                        },mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Intent intent=getIntent();
        if(intent.getSerializableExtra("todo")!=null) {
            final MyTodo myTodo = (MyTodo) intent.getSerializableExtra("todo");
            titledoes.setText(myTodo.getTitle());
            descdoes.setText(myTodo.getDescribe());
            datedoes.setText(myTodo.getDate());
            reference = FirebaseDatabase.getInstance().getReference().child("mytodo").
                    child("Todo" + myTodo.getKey());
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyTodo editTodo=new MyTodo();
                    editTodo.setTitle(titledoes.getText().toString());
                    editTodo.setDescribe(descdoes.getText().toString());
                    editTodo.setDate(datedoes.getText().toString());
                    editTodo.setKey(myTodo.getKey());
                    reference.setValue(editTodo).addOnSuccessListener(new
                                                                              OnSuccessListener<Void>() {
                                                                                  @Override
                                                                                  public void onSuccess(Void aVoid) {
                                                                                      Toast.makeText(getApplicationContext(),
                                                                                              "cập nhật thành công.",Toast.LENGTH_SHORT).show();
                                                                                      finish();
                                                                                  }
                                                                              }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),
                                    "Failed to update value.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(EditTodoActivity.this)
                            .setTitle("xóa lời nhắc")
                            .setMessage("bạn có chắc muốn xóa")
                            .setPositiveButton("có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Delete success.",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Delete failed.",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("không",null)
                            .show();
                }
            });

        }
    }

    private void initView() {
        titledoes = findViewById(R.id.titledoes);
        descdoes = findViewById(R.id.descdoes);
        datedoes = findViewById(R.id.datedoes);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnCancel=findViewById(R.id.btnCancelUpdate);
        btnSelectDate=findViewById(R.id.btnChosseDay);
    }
}