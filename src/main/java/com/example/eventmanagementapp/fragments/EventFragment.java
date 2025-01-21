package com.example.eventmanagementapp.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventmanagementapp.R;
import com.example.eventmanagementapp.Utils;
import com.example.eventmanagementapp.model.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class EventFragment extends Fragment {

    private EditText eventName, eventTime, eventLocation, eventDetails;
    private TextView eventDate;
    private Button createEventButton;
    private String uniqueID = "";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("events");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        eventName = view.findViewById(R.id.event_name);
        eventDate = view.findViewById(R.id.event_date);
        eventTime = view.findViewById(R.id.event_time);
        eventLocation = view.findViewById(R.id.event_location);
        eventDetails = view.findViewById(R.id.event_details);
        createEventButton = view.findViewById(R.id.create_event_button);

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                eventDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });


        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String name = eventName.getText().toString();
               String date = eventDate.getText().toString();
               String time = eventTime.getText().toString();
               String location = eventLocation.getText().toString();
               String details = eventDetails.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time) ||
                        TextUtils.isEmpty(location) || TextUtils.isEmpty(details)) {
                    Toast.makeText(getContext(), "Please fill", Toast.LENGTH_LONG).show();
                } else {
                    Event event = new Event();
                    uniqueID = UUID.randomUUID().toString();
                    event.setEventId(uniqueID);
                    event.setEventName(eventName.getText().toString());
                    event.setEventDate(eventDate.getText().toString());
                    event.setEventTime(eventTime.getText().toString());
                    event.setEventLocation(eventLocation.getText().toString());
                    event.setEventDetails(eventDetails.getText().toString());

                    myRef.child(uniqueID).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(view.getContext(), "added", Toast.LENGTH_LONG).show();
                        }
                    });
                    Fragment fragment = new HomeFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_holder, fragment);
                    fragmentTransaction.commit();
                }
            }
        });

        return view;
    }
}