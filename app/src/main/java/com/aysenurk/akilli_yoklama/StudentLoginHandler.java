package com.aysenurk.akilli_yoklama;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StudentLoginHandler {

    Session session;
    Context context;
    String type;
    Integer id;
    Student student;
    boolean hasAdded = false;

    public StudentLoginHandler(Context context, String result) {
        this.context = context;

        session = new Session(context);

        // Kodu parçaya ayır
        // 1.parça tipi verir öğretmen (teacher) ve öğrenci (student)
        // 2.parça ise ID sini verir
        String[] resultList = result.split("_");
        type = resultList[0];
        id = Integer.parseInt(resultList[1]);
    }

    public void handle() {

        hasAdded = false;

        // Öğrenci kartı değil ise hiç bir şey yapma
        // Ve hata mesajını göster
        if (!type.equals("student")) {
            Toast.makeText(context, "Yanlış kartı gösterdiniz. Lütfen bir öğrenci kartı gösterin.", Toast.LENGTH_LONG).show();
            return;
        }

        // Öğrenci ID sini oku
        // Eğer bu ID ile bir öğrenci var ise bilgilerini getir
        // ve öğrenciyi yoklama listesine ekle
        FirebaseReadHandler<Student> firebaseReadHandler = new FirebaseReadHandler<Student>(Student.class, (Activity) context);
        firebaseReadHandler.read("students", this::readCallback);
    }

    private Integer readCallback(ArrayList<Student> studentList) {

        if (studentList.isEmpty()) {
            Toast.makeText(context, "Öğrenci bilgisi bulunamadı", Toast.LENGTH_SHORT).show();
            return 0;
        }

        for (Student student : studentList) {
            if (student.id.equals(this.id)) {
                // Öğrenciyi bulduk
                this.student = student;
                checkStudentForAttendance();
                return 0;
            }
        }

        Toast.makeText(context, "Öğrenci bilgisi bulunamadı", Toast.LENGTH_SHORT).show();
        return 0;
    }

    private void checkStudentForAttendance() {
        FirebaseReadHandler<Attendance> firebaseReadHandler = new FirebaseReadHandler<Attendance>(Attendance.class, (Activity) context);
        firebaseReadHandler.read("attendances", this::attendanceReadCallback);
    }

    private Integer attendanceReadCallback(ArrayList<Attendance> attendances) {
        for (Attendance attendance : attendances) {
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            if (attendance.student_id.equals(this.id) && attendance.date.equals(currentDate) && attendance.classroom.equals(session.getClassroom())) {
                // Öğrencinin zaten yoklama kaydı var
                if (!hasAdded) {
                    Toast.makeText(context, "Öğrenci yoklama kaydı zaten var", Toast.LENGTH_SHORT).show();
                }
                return 0;
            }
        }

        // Öğrenciyi yoklama kaydına ekle
        hasAdded = true;
        addStudentToAttendances();
        return 0;
    }

    private void addStudentToAttendances() {
        Attendance attendance = new Attendance(this.id, session.getClassroom());

        FirebaseWriteHandler firebaseWriteHandler = new FirebaseWriteHandler();
        firebaseWriteHandler.add("attendances", attendance);

        IntentHandler.open(context, StudentLoginSuccessfulActivity.class, false);
        StudentLoginSuccessfulActivity.studentId = this.id.toString();
        StudentLoginSuccessfulActivity.studentName = student.name;
    }
}
