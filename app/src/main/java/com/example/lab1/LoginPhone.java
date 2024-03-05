package com.example.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginPhone extends AppCompatActivity {
    EditText edtsdt, edtotp;
    Button btnotp, btnlogin;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String mVerti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_phone);

        edtsdt = findViewById(R.id.edtsdt);
        edtotp = findViewById(R.id.edtotp);
        btnotp = findViewById(R.id.btnotp);
        btnlogin = findViewById(R.id.btnloginotp);
        mAuth = FirebaseAuth.getInstance();



        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                edtotp.setText(phoneAuthCredential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String verti, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verti, forceResendingToken);
                mVerti = verti;
            }
        };
        btnotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edtsdt.getText().toString().trim();
                getOTP(phone);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = edtotp.getText().toString().trim();
                vertiOTP(otp);
            }
        });
    }
    private void getOTP(String phoneNumber){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84"+phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(LoginPhone.this)
                        .setCallbacks(mCallback)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void vertiOTP(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerti, code);
        signPhone(credential);
    }
    private  void signPhone(PhoneAuthCredential credential){
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(LoginPhone.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(LoginPhone.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = task.getResult().getUser();
                                startActivity(new Intent(LoginPhone.this, MainActivity.class));
                            }else {
                                Toast.makeText(LoginPhone.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
//                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
//        // the verification code entered was invalid
//                                    Toast.makeText(LoginPhone.this, "Đăng nhập không thất bại", Toast.LENGTH_SHORT).show();
//                                }
                            }
                        }
                    });
    }
}