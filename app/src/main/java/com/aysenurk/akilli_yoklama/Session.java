package com.aysenurk.akilli_yoklama;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Session {

    private Context context;

    private static final String PREF_NAME = "spAkilliYoklama";
    private static final int PRIVATE_MODE = 0;
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_TEACHER_ID = "teacher_id";
    public static final String KEY_TEACHER_NAME = "teacher_name";
    public static final String KEY_TEACHER_CLASS = "teacher_class";

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    public Session(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public boolean isClassSelected(){
        return !sharedPreferences.getString(KEY_TEACHER_CLASS, "").isEmpty();
    }

    public void removeClass(){
        editor.remove(KEY_TEACHER_CLASS);
        editor.commit();
        goTo("main");
    }
    public void registerClass(String classroom) {
        editor.putString(KEY_TEACHER_CLASS, classroom);
        editor.commit();
        goTo("student_login");
    }

    public void create(Integer id, String name) {
        editor.putBoolean(IS_LOGIN, true);

        // Save email and password
        editor.putString(KEY_TEACHER_ID, id.toString());
        editor.putString(KEY_TEACHER_NAME, name);

        editor.commit();

        // Go to main activity
        goTo("main");
    }

    public boolean checkLogin() {
        if (!this.isLoggedIn()) {
            goTo("login");
            return false;
        } else {
            if(this.isClassSelected()){
                goTo("student_login");
                return true;
            }

            return true;
        }
    }

    public void remove() {
        editor.clear();
        editor.commit();

        goTo("login");
    }

    private void goTo(String activity) {
        Class targetClass = null;

        switch (activity) {
            case "main":
                targetClass = MainActivity.class;
                break;
            case "login":
                targetClass = LoginActivity.class;
                break;
            case "student_login":
                targetClass = UserLoginActivity.class;
                break;
        }

        IntentHandler.open(context, targetClass);
    }

    public String getTeacherName() {
        if (!isLoggedIn()) {
            return "";
        }

        return sharedPreferences.getString(KEY_TEACHER_NAME, null);
    }

    public String getClassroom() {
        if (!isLoggedIn() || !isClassSelected()) {
            return "";
        }

        return sharedPreferences.getString(KEY_TEACHER_CLASS, null);
    }

    public String getTeacherId() {
        if (!isLoggedIn()) {
            return "";
        }

        return sharedPreferences.getString(KEY_TEACHER_ID, null);
    }
}
