package com.aysenurk.akilli_yoklama;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AttendanceListActivity extends AppCompatActivity {

    Session session;
    ProgressBar progressBar;

    LinearLayout attendanceListContainer;

    ArrayList<String> classroomList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

        session = new Session(this);
        progressBar = findViewById(R.id.loader);
        attendanceListContainer = findViewById(R.id.attendanceList);

        // Sınıflarımı getir
        FirebaseReadHandler<Lessons> firebaseReadHandler1 = new FirebaseReadHandler<Lessons>(Lessons.class, this);
        firebaseReadHandler1.read("lessons", this::lessonsReadCallback);
    }

    private Integer lessonsReadCallback(ArrayList<Lessons> lessons) {
        String teacherId = session.getTeacherId();

        for (Lessons lesson : lessons) {
            if (lesson.teacher_id.toString().equals(teacherId)) {
                classroomList.add(lesson.classroom);
            }
        }

        // Bugünkü Yoklama listesini getir
        FirebaseReadHandler<Attendance> firebaseReadHandler2 = new FirebaseReadHandler<Attendance>(Attendance.class, this);
        firebaseReadHandler2.read("attendances", this::attendanceReadCallback);

        return 0;
    }

    private Integer attendanceReadCallback(ArrayList<Attendance> attendances) {
        // Yükleme tamamlandı, progressbar'ı kaldır
        progressBar.setVisibility(View.INVISIBLE);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        TextView textViewInfo = new TextView(this);
        textViewInfo.setText("Sınıf \t-\t Öğr. ID \t-\t Tarih");
        textViewInfo.setTextSize(12);
        textViewInfo.setTextColor(Color.rgb(255, 255, 255));
        textViewInfo.setBackgroundColor(Color.rgb(174, 182, 191));
        textViewInfo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textViewInfo.setPadding(0, 5, 0, 50);
        attendanceListContainer.addView(textViewInfo);

        // Boşluk ekle
        textViewInfo = new TextView(this);
        textViewInfo.setPadding(0, 5, 0, 50);
        attendanceListContainer.addView(textViewInfo);

        for (Attendance attendance : attendances) {
            // Bugünkü ve öğretmenin sınıfına ait olan yoklamaları getir
            if (attendance.date.equals(currentDate)
                    && classroomList.indexOf(attendance.classroom) > -1) {

                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                // Class room
                TextView textView = new TextView(this);
                textView.setText(attendance.classroom + " \t-\t " + attendance.student_id + " \t-\t " + attendance.date + " " + attendance.hour);
                textView.setTextSize(12);
                textView.setTextColor(Color.rgb(255, 255, 255));
                textView.setBackgroundColor(Color.rgb(174, 182, 191));
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setPadding(0, 5, 0, 50);

                attendanceListContainer.addView(textView);
            }
        }

        // Eğer yoklama yok ise mesaj göster
        if(attendances.isEmpty()){
             textViewInfo = new TextView(this);
            textViewInfo.setText("Bugün için yoklama bulunamadı");
            textViewInfo.setTextSize(12);
            textViewInfo.setTextColor(Color.rgb(255, 255, 255));
            textViewInfo.setBackgroundColor(Color.rgb(174, 182, 191));
            textViewInfo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textViewInfo.setPadding(0, 5, 0, 50);
            attendanceListContainer.addView(textViewInfo);
        }

        return 0;
    }
}
