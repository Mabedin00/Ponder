package com.templum.ponder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity{ // implements View.OnClickListener{
    private EditText emailField;
    private EditText passwordField;
    private EditText passwordField1;
    private Button signUpBtn;
    private Button logInBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private static final String TAG = "SignUpActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailField = (EditText) findViewById(R.id.emailField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        passwordField1 = (EditText) findViewById(R.id.passwordField1);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        logInBtn = (Button) findViewById(R.id.logInBtn);
        mAuth = FirebaseAuth.getInstance();


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(v);

            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogInActivity();

            }
        });

    }
    private void openLogInActivity(){
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void registerUser(View v){
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String password1 = passwordField1.getText().toString().trim();

        if (email.isEmpty()){
            emailField.setError("Email is Required");
            emailField.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailField.setError("Email needs to be valid");
            emailField.requestFocus();
            return;
        }
        if (password.isEmpty()){
            passwordField.setError("Password is Required");
            passwordField.requestFocus();
            return;
        }

        if (password.length() < 6){
            passwordField.setError("Password needs to have at least 6 characters");
            passwordField.requestFocus();
            return;
        }

        if (!password1.equals(password)){
            passwordField1.setError("Passwords must match");
            passwordField1.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "You are Registered! Please verify your email.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

                else
                {
                    Log.w(TAG, "createUserWithEmail:failure");
                    try
                    {
                        throw task.getException();
                    }
                    catch (FirebaseAuthUserCollisionException existEmail)
                    {
                        emailField.setError("Email is already used.");
                        emailField.requestFocus();
                    }
                    catch (Exception e)
                    {
                        emailField.setError(e.getMessage());
                        emailField.requestFocus();
                    }

                }


            }
        });

    }


}
