package com.example.appnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VertifiOTPActivity extends AppCompatActivity {
    private TextView txtMobile;
    private EditText edtinputCode1, edtinputCode2, edtinputCode3, edtinputCode4, edtinputCode5, edtinputCode6;

    private Button btnVertify;
    private ProgressBar progressBar;
    private String vertificationid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertifi_o_t_p);

        // anh xa
        txtMobile = findViewById(R.id.txtMobile);
        edtinputCode1 = findViewById(R.id.edtinputCode1);
        edtinputCode2 = findViewById(R.id.edtinputCode2);
        edtinputCode3 = findViewById(R.id.edtinputCode3);
        edtinputCode4 = findViewById(R.id.edtinputCode4);
        edtinputCode5 = findViewById(R.id.edtinputCode5);
        edtinputCode6 = findViewById(R.id.edtinputCode6);

        btnVertify = findViewById(R.id.btnVerify);
        progressBar = findViewById(R.id.progressBar);

        txtMobile = findViewById(R.id.txtMobile);

        txtMobile.setText(String.format(
                "+84-%s", getIntent().getStringExtra("mobile")
        ));
        setupOTPInputs();
        vertificationid = getIntent().getStringExtra("vertificationid");
        btnVertify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtinputCode1.getText().toString().isEmpty()
                        || edtinputCode2.getText().toString().isEmpty()
                        || edtinputCode3.getText().toString().isEmpty()
                        || edtinputCode4.getText().toString().isEmpty()
                        || edtinputCode5.getText().toString().isEmpty()
                        || edtinputCode6.getText().toString().isEmpty()) {
                    Toast.makeText(VertifiOTPActivity.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = edtinputCode1.getText().toString() +
                        edtinputCode2.getText().toString() +
                        edtinputCode3.getText().toString() +
                        edtinputCode4.getText().toString() +
                        edtinputCode5.getText().toString() +
                        edtinputCode6.getText().toString();
                if (vertificationid != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    btnVertify.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            vertificationid,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    btnVertify.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        // xoa tat ca cac ngan xep phia sau  va khoi chay hoat dong moi
                                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(VertifiOTPActivity.this, "ma xac minh nhap sai ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    private void setupOTPInputs() {
        edtinputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    edtinputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtinputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    edtinputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtinputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    edtinputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtinputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    edtinputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtinputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    edtinputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}