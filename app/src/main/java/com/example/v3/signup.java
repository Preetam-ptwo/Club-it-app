package com.example.v3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ComponentActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(View view){
        startActivity(new Intent(this, loginActivity.class));
    }

    public void register(View view){
        EditText ename=(EditText) findViewById(R.id.n1);
        EditText eage=(EditText)findViewById(R.id.age);
        EditText eemail=(EditText)findViewById(R.id.e1);
        EditText epassword=(EditText)findViewById(R.id.p1);
        ProgressBar pb=findViewById(R.id.progressBar);

        String name=ename.getText().toString().trim();
        String age=eage.getText().toString().trim();
        String email=eemail.getText().toString().trim();
        String password=epassword.getText().toString().trim();

        if(name.isEmpty()){
            ename.setError("Full name is required");
            ename.requestFocus();
            return;
        }

        if(email.isEmpty()){
            eemail.setError("Email is required");
            eemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            eemail.setError("Email is not Valid");
            eemail.requestFocus();
            return;
        }
        if(age.isEmpty()){
            eage.setError("Age is required");
            eage.requestFocus();
            return;
        }
        if(password.isEmpty()){
            epassword.setError("Password is required");
            epassword.requestFocus();
            return;
        }
        if(password.length()<5){
            epassword.setError("Password must be grater than 5");
            epassword.requestFocus();
            return;
        }

        pb.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user user=new user(name,age,email);

                            FirebaseDatabase.getInstance().getReference("user")
                                    .child(mAuth.getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(signup.this, "User has been Registered Successfully", Toast.LENGTH_SHORT).show();
                                        pb.setVisibility(View.GONE);
                                        startActivity(new Intent(signup.this, MainActivity.class));
                                    }
                                    else{
                                        Toast.makeText(signup.this, "Failed", Toast.LENGTH_SHORT).show();
                                        pb.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(signup.this, "Failed", Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.GONE);
                        }
                    }
                });
    }
}