package com.exam.domain;

import javax.persistence.*;
import java.util.Set;

@javax.persistence.Entity
@Table(name = "Letter_set")
public class LetterSet implements Entity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "letter_set")
    private String letter_Set;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "letter_set")
    private Set<LetterSetValue> letterSetValue;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "letterSet")
    private Set<Round> round;

    public LetterSet() {
    }

    public LetterSet(Integer id, String letter_Set, Set<LetterSetValue> letterSetValue, Set<Round> round) {
        this.id = id;
        this.letter_Set = letter_Set;
        this.letterSetValue = letterSetValue;
        this.round = round;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getLetter_Set() {
        return letter_Set;
    }

    public void setLetter_Set(String letter_Set) {
        this.letter_Set = letter_Set;
    }

    public Set<LetterSetValue> getLetterSetValue() {
        return letterSetValue;
    }

    public void setLetterSetValue(Set<LetterSetValue> letterSetValue) {
        this.letterSetValue = letterSetValue;
    }

    public Set<Round> getRound() {
        return round;
    }

    public void setRound(Set<Round> round) {
        this.round = round;
    }
}
