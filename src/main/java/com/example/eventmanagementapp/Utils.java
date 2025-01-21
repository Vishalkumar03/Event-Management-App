package com.example.eventmanagementapp;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static boolean compareDates(String date) {
        boolean isValid = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = sdf.parse(date);
            if (System.currentTimeMillis() > date1.getTime()) {
                isValid = false;
            } else {
                isValid = true;
                Log.d("TAG", "compareDates: ");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isValid;
    }
}
