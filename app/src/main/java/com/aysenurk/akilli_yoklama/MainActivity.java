package com.aysenurk.akilli_yoklama;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView teacherInfoView;
    private TextView teacherIdView;
    private Session session = null;

    LinearLayout myClassroomsContainer;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Öğretmen giriş yaptımı kontrol et
        // Giriş yapılmadı ise giriş ekranına yönlendir
        session = new Session(this);
        session.checkLogin();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myClassroomsContainer = findViewById(R.id.myClassrooms);
        progressBar = findViewById(R.id.loader);

        // Sınıflarımı getir
        FirebaseReadHandler<Lessons> firebaseReadHandler = new FirebaseReadHandler<Lessons>(Lessons.class, this);
        firebaseReadHandler.read("lessons", this::readCallback);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        handleNavigationTools(navigationView);
    }

    private Integer readCallback(ArrayList<Lessons> lessons) {
        TextView classroomInfoText = findViewById(R.id.classroomInfoText);
        classroomInfoText.setVisibility(View.VISIBLE);
        // Read is done, remove loader
        progressBar.setVisibility(View.INVISIBLE);

        String teacherId = session.getTeacherId();

        for (Lessons lesson : lessons) {
            if (lesson.teacher_id.toString().equals(teacherId)) {
                TextView textView = new TextView(this);
                textView.setText(lesson.classroom);
                textView.setTextSize(30);
                textView.setTextColor(Color.rgb(255, 255, 255));
                textView.setBackgroundColor(Color.rgb(174, 182, 191));
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setPadding(0, 5, 0, 50);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView obj = (TextView) v;
                        session.registerClass(obj.getText().toString());
                    }
                });
                myClassroomsContainer.addView(textView);

                textView = new TextView(this);
                textView.setPadding(0, 5, 0, 50);
                myClassroomsContainer.addView(textView);
            }
        }

        return 0;
    }

    public void handleNavigationTools(NavigationView navigationView) {
        // Nav header
        teacherInfoView = navigationView.getHeaderView(0).findViewById(R.id.teacherInfoView);
        teacherIdView = navigationView.getHeaderView(0).findViewById(R.id.teacherIdView);
        if (session.isLoggedIn()) {
            teacherInfoView.setText(session.getTeacherName());
            teacherIdView.setText("ID: " + session.getTeacherId());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            session.remove();
        } else if (id == R.id.nav_attendance_list) {
            IntentHandler.open(this, AttendanceListActivity.class, false);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
