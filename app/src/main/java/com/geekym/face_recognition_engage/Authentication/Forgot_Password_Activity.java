package com.geekym.face_recognition_engage.Authentication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.geekym.face_recognition_engage.R;
import com.google.firebase.auth.FirebaseAuth;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class Forgot_Password_Activity extends AppCompatActivity {

    FirebaseAuth Auth2;
    EditText email;
    Button reset_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Initialization(); //Initialize

        reset_pass.setOnClickListener(view -> {
            if (isConnected()) {ForgotPassword(); }});
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
                    if (task.isSuccessful()) {
                        DynamicToast.make(this, "Password reset email sent!", getResources()
                                .getColor(R.color.white), getResources().getColor(R.color.green_desat)).show();
                        startActivity(new Intent(getApplicationContext(), SignIn_Activity.class));
                    }
                    else
                        DynamicToast.makeError(this, "Something went wrong").show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Email sent");
                }
            });
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            return true;

        DynamicToast.makeError(getApplicationContext(), "You're not connected to Internet!").show();
        return false;
    }

    private void Initialization() {
        Auth2 = FirebaseAuth.getInstance();
        email = findViewById(R.id.reset_email);
        reset_pass = findViewById(R.id.reset_button);
    }
}