package com.exam.domain;

import java.io.Serializable;

public class StudentDTO implements Serializable {
    private User student;
    private Integer bomb1;
    private Integer bomb2;

    public StudentDTO(User student, Integer bomb1, Integer bomb2) {
        this.student = student;
        this.bomb1 = bomb1;
        this.bomb2 = bomb2;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getBomb1() {
        return bomb1;
    }

    public void setBomb1(Integer bomb1) {
        this.bomb1 = bomb1;
    }

    public Integer getBomb2() {
        return bomb2;
    }

    public void setBomb2(Integer bomb2) {
        this.bomb2 = bomb2;
    }
}
