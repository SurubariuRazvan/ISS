package com.exam.domain;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "Word")
public class Word implements com.exam.domain.Entity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "to_student_id")
    private Integer toStudentID;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "round_id")
    private Round round;

    @Column(name = "position")
    private Integer position;

    @Column(name = "value")
    private Integer value;

    @Column(name = "bomb1")
    private Integer bomb1;

    @Column(name = "bomb2")
    private Integer bomb2;

    public Word() {
    }

    public Word(Integer id, Student student, Integer toStudentID, Round round, Integer position, Integer value, Integer bomb1, Integer bomb2) {
        this.id = id;
        this.student = student;
        this.toStudentID = toStudentID;
        this.round = round;
        this.position = position;
        this.value = value;
        this.bomb1 = bomb1;
        this.bomb2 = bomb2;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getToStudentID() {
        return toStudentID;
    }

    public void setToStudentID(Integer toStudentID) {
        this.toStudentID = toStudentID;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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
