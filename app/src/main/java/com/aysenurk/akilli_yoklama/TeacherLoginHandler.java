package com.aysenurk.akilli_yoklama;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

public class TeacherLoginHandler {

    Context context;
    String type;
    Integer id;

    public TeacherLoginHandler(Context context, String result) {
        this.context = context;

        // Kodu parçaya ayır
        // 1.parça tipi verir öğretmen (teacher) ve öğrenci (student)
        // 2.parça ise ID sini verir
        String[] resultList = result.split("_");
        type = resultList[0];
        id = Integer.parseInt(resultList[1]);
    }

    public void handle() {
        // Öğretmen kartı değil ise hiç bir şey yapma
        // Ve hata mesajını göster
        if (!type.equals("teacher")) {
            Toast.makeText(context, "Yanlış kartı gösterdiniz. Lütfen bir öğretmen kartı gösterin.", Toast.LENGTH_LONG).show();
            return;
        }

        // Öğretmen ID sini oku
        // Eğer böyle bir id ile öğretmen var ise bilgilerini getir
        FirebaseReadHandler<Teacher> firebaseReadHandler = new FirebaseReadHandler<Teacher>(Teacher.class, (Activity) context);
        firebaseReadHandler.read("teachers", this::readCallback);
    }

    private Integer readCallback(ArrayList<Teacher> teacherList) {

        // Herhangi bir öğretmen kayıtlı değil ise hata ver
        if (teacherList.isEmpty()) {
            Toast.makeText(context, "Öğretmen bilgisi bulunamadı", Toast.LENGTH_SHORT).show();
            return 0;
        }

        for (Teacher teacher : teacherList) {
            if (teacher.id.equals(this.id)) {
                // Öğretmeni bulduk
                Session session = new Session(context);
                session.create(teacher.id, teacher.name);
                return 0;
            }
        }

        // Öğretmen bulunamadı hata ver
        Toast.makeText(context, "Öğretmen bilgisi bulunamadı", Toast.LENGTH_SHORT).show();

        return 0;
    }
}
