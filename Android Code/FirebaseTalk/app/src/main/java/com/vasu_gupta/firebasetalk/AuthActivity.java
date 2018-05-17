package com.vasu_gupta.firebasetalk;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {

    EditText etEmail, etPass;
    Button btnSignUp, btnSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.emailSignUp);
        etPass = findViewById(R.id.passSignUp);

        btnSignUp = findViewById(R.id.btnSIgnUp);
        btnSignIn = findViewById(R.id.btnSignIn);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPass.getText().toString();
                if (!email.equals("") || !password.equals("")) {
                    final ProgressDialog ringProgressDialog = ProgressDialog.show(AuthActivity.this, "Please Wait", "Checking Credentials...", true);
                    ringProgressDialog.setCancelable(false);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (ringProgressDialog.isShowing())ringProgressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AuthActivity.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AuthActivity.this, "Authentication Failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String password = etPass.getText().toString();
                if (!email.equals("") || !password.equals("")) {
                    final ProgressDialog ringProgressDialog = ProgressDialog.show(AuthActivity.this, "Please Wait", "Checking Credentials...", true);
                    ringProgressDialog.setCancelable(false);
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (ringProgressDialog.isShowing())ringProgressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AuthActivity.this, "Sign In Successful!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AuthActivity.this, "Authentication Failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}
