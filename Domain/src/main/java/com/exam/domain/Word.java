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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "round_id")
    private Round round;

    @Column(name = "word")
    private String word;

    @Column(name = "value")
    private Integer value;

    public Word() {
    }

    public Word(Integer id, Student student, Round round, String word, Integer value) {
        this.id = id;
        this.student = student;
        this.round = round;
        this.word = word;
        this.value = value;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", student=" + student +
                ", round=" + round +
                ", word='" + word + '\'' +
                ", value=" + value +
                '}';
    }
}
