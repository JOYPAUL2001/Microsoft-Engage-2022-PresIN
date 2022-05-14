package com.geekym.face_recognition_engage.HomeFragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.geekym.face_recognition_engage.Authentication.SignIn_Activity;
import com.geekym.face_recognition_engage.R;
import com.geekym.face_recognition_engage.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class profile_Fragment extends Fragment {

    ImageButton Logout;
    private DatabaseReference reference;
    private String userID;
    TextView Name, Email, CollegeID, CollegeName;
    Button EditProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);

        Initialization(view);

        Logout.setOnClickListener(view1 -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle("Confirm Logout");
            alertDialogBuilder.setIcon(R.drawable.logo);
            alertDialogBuilder.setMessage("Do you really want to logout?");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Logout", (dialogInterface, i) -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), SignIn_Activity.class));
                requireActivity().finish();
            });
            alertDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        reference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() { //To display user's data in card view
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userprofile = snapshot.getValue(Users.class);
                if (userprofile != null) {
                    String name = userprofile.name;
                    String email = userprofile.email;
                    String uid = userprofile.id;
                    String org = userprofile.college;

                    Name.setText("Name: " + name);
                    Email.setText("Email: " + email);
                    CollegeID.setText("College ID: " + uid);
                    CollegeName.setText("College Name: " + org);

                    EditProfile.setOnClickListener(view12 -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Enter Your New Name");

                        // Set up the input
                        final EditText input = new EditText(getContext());

                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("Update", (dialog, which) -> {
                            HashMap map = new HashMap();
                            map.put("name", input.getText().toString());
                            reference.child("Users").child(userID).updateChildren(map);

                        });
                        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                        builder.show();
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void Initialization(View view) {
        Logout = view.findViewById(R.id.logout_button);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        assert user != null;
        userID = user.getUid();
        Name = view.findViewById(R.id.name);
        Email = view.findViewById(R.id.email);
        CollegeID = view.findViewById(R.id.uid);
        CollegeName = view.findViewById(R.id.college_name);
        EditProfile = view.findViewById(R.id.edit);
    }

}