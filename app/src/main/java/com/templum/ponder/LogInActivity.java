package com.templum.ponder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText passwordField;
    private Button logInBtn;
    private FirebaseAuth mAuth;
    private static final String TAG = "LogInActivity";
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        emailField = (EditText) findViewById(R.id.emailField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        logInBtn = (Button) findViewById(R.id.logInBtn);
        mAuth = FirebaseAuth.getInstance();

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInUser(v);

            }

        });


    }

    public void logInUser(View v){
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        if (!email.equals("") && !password.equals("")){
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        makeToast("You are logged in!");
                        finish();
                        openProfileActivity();
                    }
                    else {
                        makeToast("Login Failed. Check your email and password.");
                    }
                }
            });

        }
        else{
            makeToast("Enter email and password");
        }


    }

    private void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void openProfileActivity(){
        Intent intent = new Intent(this, com.templum.ponder.ProfileActivity.class);
        startActivity(intent);
    }

}
