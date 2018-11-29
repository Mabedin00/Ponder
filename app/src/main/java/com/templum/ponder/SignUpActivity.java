package com.templum.ponder;

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

public class SignUpActivity extends AppCompatActivity{ // implements View.OnClickListener{
    private EditText emailField;
    private EditText passwordField;
    private EditText passwordField1;
    private EditText usernameField;
    private Button signUpBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailField = (EditText) findViewById(R.id.emailField);
        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        passwordField1 = (EditText) findViewById(R.id.passwordField1);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        mAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

            }
        });




    }
    public void registerUser(){
        String email = emailField.getText().toString().trim();
        String username = usernameField.getText().toString().trim();
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
        if (username.isEmpty()){
            usernameField.setError("Username is Required");
            usernameField.requestFocus();
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
                if (!task.isSuccessful())
                {
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
                else
                    Toast.makeText(getApplicationContext(), "You are Registered!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
