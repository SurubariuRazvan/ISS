package com.exam.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WordPK implements Serializable {
    @Column(name = "student_id")
    private Integer student_id;

    @Column(name = "round_id")
    private Integer round_id;

    public WordPK() {
    }

    public WordPK(Integer student_id, Integer round_id) {
        this.student_id = student_id;
        this.round_id = round_id;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public Integer getRound_id() {
        return round_id;
    }

    public void setRound_id(Integer round_id) {
        this.round_id = round_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordPK wordPK = (WordPK) o;
        return Objects.equals(student_id, wordPK.student_id) &&
                Objects.equals(round_id, wordPK.round_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student_id, round_id);
    }
}
