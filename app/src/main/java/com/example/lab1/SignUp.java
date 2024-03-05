package com.example.lab1;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText edtemail, edtpassword, edtcomfirm;
    Button btnsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
       mAuth = FirebaseAuth.getInstance();
       edtemail = findViewById(R.id.edtuser2);
       edtpassword = findViewById(R.id.edtpass2);
       edtcomfirm = findViewById(R.id.edtpasscomfirm);
       btnsignup = findViewById(R.id.btnsignup);


     btnsignup.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             String email =edtemail.getText().toString().trim();
             String password =edtpassword.getText().toString().trim();
             String confirm =edtcomfirm.getText().toString().trim();
             if (!confirm.equals(password)){
                 Toast.makeText(SignUp.this, "Password và confirmpass không trung nhau", Toast.LENGTH_SHORT).show();
             }else{

                 mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()){
                             //lấy thoo tin
                             FirebaseUser user = mAuth.getCurrentUser();
                             Toast.makeText(SignUp.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                             Intent intent = new Intent(SignUp.this, Login.class);
                             startActivity(intent);
                             finish();
                         }else {
                             Toast.makeText(SignUp.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
             }
         }
     });
    }
}