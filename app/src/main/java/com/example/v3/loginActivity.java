package com.example.v3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class loginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }



    public void register(View view){
        startActivity(new Intent(this,signup.class));
    }

    public void login(View view){

        EditText eemail=(EditText) findViewById(R.id.e2);
        EditText epassword=(EditText) findViewById(R.id.p2);
        ProgressBar pb=findViewById(R.id.progressBar);
        mAuth= FirebaseAuth.getInstance();


        String email=eemail.getText().toString().toLowerCase(Locale.ROOT);
        String password=epassword.getText().toString().toLowerCase(Locale.ROOT);

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

        if(password.isEmpty()){
            epassword.setError("Password is required");
            epassword.requestFocus();
            return;
        }
        pb.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(loginActivity.this,MainActivity.class));
                }
                else{
                    Toast.makeText(loginActivity.this, "Failed to Log in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

