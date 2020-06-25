package com.aysenurk.akilli_yoklama;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StudentLoginSuccessfulActivity extends AppCompatActivity {

    private Activity activity;
    public static String studentId = "";
    public static String studentName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login_successful);

        TextView studentIdText = findViewById(R.id.studentId);
        studentIdText.setText(studentId);

        TextView studentNameText = findViewById(R.id.studentName);
        studentNameText.setText(studentName);

        activity = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IntentHandler.open(activity,UserLoginActivity.class);
            }
        }, 1500);
    }
}
