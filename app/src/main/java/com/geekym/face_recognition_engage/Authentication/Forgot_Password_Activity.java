package com.geekym.face_recognition_engage.Authentication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.geekym.face_recognition_engage.R;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password_Activity extends AppCompatActivity {

    FirebaseAuth Auth2;
    EditText email;
    Button resetpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Initialization();

        resetpass.setOnClickListener(view -> {
            ForgotPassword();
        });
    }

    private void ForgotPassword() {
        String Email = email.getText().toString().trim();
        if (Email.isEmpty()) {
            email.setError("Field can't be empty");
            email.requestFocus();

        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError("Please enter a valid Email id");
            email.requestFocus();

        } else {
            Auth2.sendPasswordResetEmail(Email).addOnCompleteListener(task -> {
                try {
                    if (task.isSuccessful())
                        Toast.makeText(getApplicationContext(), "Password Reset email sent!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Email sent");
                }
            });
        }
    }

    private void Initialization() {
        Auth2 = FirebaseAuth.getInstance();
        email = findViewById(R.id.reset_email);
        resetpass = findViewById(R.id.reset_button);
    }
}