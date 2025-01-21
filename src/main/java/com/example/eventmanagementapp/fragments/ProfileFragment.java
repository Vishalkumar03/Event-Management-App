package com.example.eventmanagementapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.eventmanagementapp.R;
import com.example.eventmanagementapp.auth.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private Button logoutButton;
    private TextView userEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        logoutButton = view.findViewById(R.id.logout_button);
        userEmail = view.findViewById(R.id.user_email);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userRef = database.getReference("users").child(user.getUid());

            userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        if (dataSnapshot.exists()) {
                            String email = String.valueOf(dataSnapshot.child("email").getValue());
                            userEmail.setText(email);
                            Log.d("ProfileFragment", "Email: " + email); // Debug log
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch user data", Toast.LENGTH_LONG).show();
                        Log.e("ProfileFragment", "Error fetching user data: " + task.getException()); // Error log
                    }
                }
            });
        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_LONG).show();
            Log.d("ProfileFragment", "User not authenticated"); // Debug log
        }

        return view;
    }
}
