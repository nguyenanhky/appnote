package com.example.appnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {
    private EditText edtinputMobile;
    private Button btnGetOTP;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_o_t_p);
        // anh xa
        edtinputMobile = findViewById(R.id.edtinputMobile);
        btnGetOTP = findViewById(R.id.btnGetOTP);
        progressBar = findViewById(R.id.progressBar);
        btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtinputMobile.getText().toString().trim().isEmpty()){
                    Toast.makeText(SendOTPActivity.this, "Enter Mobile", Toast.LENGTH_SHORT).show();
                    return;
                }
//                // xac thuc
                progressBar.setVisibility(View.VISIBLE);
                btnGetOTP.setVisibility(View.INVISIBLE);
                // chuyen so dien thoai den phương thức dể yêu cầu firebase xác minh số điện thoại
                // của mình
                // ( phuong thuc nay duoc goi nhieu lần
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+84"+edtinputMobile.getText().toString(),// so diên thoai de xác minh
                        60,// thoi gian cho
                        TimeUnit.SECONDS,// thoi gian cho
                        SendOTPActivity.this,// Activity (for callback binding)
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){// chua các hàm xu ly kq của yêu cầu

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                // lẹnh này sẽ được gọi lại trong hai trường hợp
                                // trường hợp 1 :
                                // + xác minh ngay lập tức
                                // + đã xác minh mà không cần gửi hoặc nhập mã xác minh
                                // trường hợp 2 :
                                // + tự động truy xuất
                                // + trên một số thiết bị, các dịch vụ Google Play  có thể tự động phát hiện SMS
                                // + xác minh đến và thực hiện xác minh  mà khonong cần người dùng thực hiện

                                progressBar.setVisibility(View.GONE);
                                btnGetOTP.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                // phuong thuc xac thuc so dien thoai neu khong thay hop le
                                progressBar.setVisibility(View.GONE);
                                btnGetOTP.setVisibility(View.VISIBLE);
                                Toast.makeText(SendOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String vertificationid, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                btnGetOTP.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(getApplicationContext(),VertifiOTPActivity.class);
                                intent.putExtra("mobile",edtinputMobile.getText().toString());
                                intent.putExtra("vertificationid",vertificationid);
                                startActivity(intent);
                            }
                        }
                );
            }
        });
    }
}