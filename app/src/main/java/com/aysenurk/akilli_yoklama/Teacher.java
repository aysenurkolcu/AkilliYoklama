package com.aysenurk.akilli_yoklama;

public class Teacher {
    public Integer id;
    public String name;

    public Teacher() {

    }

    public Teacher(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
