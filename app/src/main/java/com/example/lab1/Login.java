package com.example.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText edtemail, edtpass;
    TextView txtquen;
    Button btnlogin,btnsignup;
    ImageButton btnphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_login);
       mAuth = FirebaseAuth.getInstance();
       edtemail = findViewById(R.id.edtuser);
       edtpass = findViewById(R.id.edtpass);
       txtquen = findViewById(R.id.txtquen);
        btnsignup = findViewById(R.id.btnsignup);
       btnlogin = findViewById(R.id.btnlogin);
       btnphone = findViewById(R.id.btnPhone);

       btnphone.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(Login.this, LoginPhone.class));
           }
       });
       btnsignup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(Login.this, SignUp.class);
               startActivity(intent);
           }
       });
       txtquen.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String emailAddress = edtemail.getText().toString();
               mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Toast.makeText(Login.this, "Vui lòng kiểm tra hộp thư", Toast.LENGTH_SHORT).show();
                       }else {
                           Toast.makeText(Login.this, "Lỗi gửi email", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
           }
       });
       btnlogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String email =edtemail.getText().toString().trim();
               String password =edtpass.getText().toString().trim();


                   mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
                               //lấy thoo tin
                               FirebaseUser user = mAuth.getCurrentUser();
                               Toast.makeText(Login.this, "Đăng Nhập thành công", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(Login.this, MainActivity.class);
                               startActivity(intent);
                               finishAffinity();
                           }else {
                               Toast.makeText(Login.this, "Đăng Nhập thất bại", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });

           }
       });

    }
}