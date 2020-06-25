package com.aysenurk.akilli_yoklama;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Attendance {
    public Integer student_id;
    public String classroom;
    public String date;
    public String hour;

    public Attendance() {
    }

    public Attendance(Integer student_id, String classroom) {
        this.student_id = student_id;
        this.classroom = classroom;
        this.date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        this.hour = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
