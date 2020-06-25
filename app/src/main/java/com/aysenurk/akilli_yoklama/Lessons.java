package com.aysenurk.akilli_yoklama;

public class Lessons {
    public Integer teacher_id;
    public String classroom;

    public Lessons() {
    }

    public Lessons(Integer teacherId, String classroom) {
        this.teacher_id = teacherId;
        this.classroom = classroom;
    }

    public Integer getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(Integer teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}
