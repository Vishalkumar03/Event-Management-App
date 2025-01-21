package com.example.eventmanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventmanagementapp.model.Event;
import com.example.eventmanagementapp.model.EventUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class DetailActivity extends AppCompatActivity {

    TextView title, subtitle, eventStatus, eventTime, eventDate, eventLocation, registeredText;
    LinearLayout eventStatusLL, registeredLL;

    Button phonebutton;
    EditText phoneNumber, fullName, userAge;
    ImageView eventQr;
    String eventId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.event_name);
        subtitle = findViewById(R.id.event_details);
        eventStatus = findViewById(R.id.date_check);
        eventTime = findViewById(R.id.event_time);
        eventDate = findViewById(R.id.event_date);
        eventLocation = findViewById(R.id.event_location);
        eventStatusLL = findViewById(R.id.status_banner);
        phonebutton = findViewById(R.id.register_button);
        phoneNumber = findViewById(R.id.phone_number);

        registeredText = findViewById(R.id.register_text);
        registeredLL = findViewById(R.id.registered_ll);
        eventQr = findViewById(R.id.event_qr);
        fullName = findViewById(R.id.full_name);
        userAge = findViewById(R.id.user_age);

        registeredLL.setVisibility(View.GONE);


        Bundle bundle = getIntent().getExtras();

        title.setText(bundle.getString("event_name"));
        subtitle.setText(bundle.getString("event_details"));
        eventDate.setText(bundle.getString("event_date"));
        eventTime.setText(bundle.getString("event_time"));
        eventLocation.setText(bundle.getString("event_location"));

        eventId = bundle.getString("event_id");

        Log.d("event_id", "id : " + eventId);

        boolean isValid = bundle.getBoolean("event_status");
        if(isValid) {
            eventStatus.setText("Upcoming");
            eventStatusLL.setBackgroundColor(Color.parseColor("#30cf8f"));
            phonebutton.setEnabled(true);

        } else {
            eventStatus.setText("Expired");
            eventStatusLL.setBackgroundColor(Color.parseColor("#fc448b"));
            phonebutton.setEnabled(false);
        }

        phonebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSmsMessage(phoneNumber.getText().toString());

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userEventRef = database.getReference("user_event");

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                EventUsers eventUsers = new EventUsers();
                eventUsers.setUserId(user.getUid().toString());
/*
                userEventRef.child(eventId).child()setValue(eventUsers).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
                */
                userEventRef.child(eventId).child(user.getUid().toString()).setValue(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userEventRef = database.getReference("user_event");

        userEventRef.child(eventId).child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        Toast.makeText(DetailActivity.this, "Already Register", Toast.LENGTH_LONG).show();
                        phonebutton.setEnabled(false);
                        userAge.setVisibility(View.GONE);
                        fullName.setVisibility(View.GONE);
                        phoneNumber.setVisibility(View.GONE);
                        registeredLL.setVisibility(View.VISIBLE);
                        generateEventTicket(eventQr);
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "failed", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void makeSmsMessage(String phoneNumber) {
        if(!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == 10 && !TextUtils.isEmpty(fullName.getText()) && !TextUtils.isEmpty(userAge.getText())) {

            try {
                SmsManager smsManager = SmsManager.getDefault();
                String message = "Hi, you have successfully register for \n " + title.getText().toString();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Fill data", Toast.LENGTH_LONG).show();
        }

    }

    public void generateEventTicket(ImageView imageView) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(eventId, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

}