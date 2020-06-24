package com.exam.domain;


import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "Letter_Set_Value")
public class LetterSetValue implements Entity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "letter_set")
    private LetterSet letter_set;

    @Column(name = "value")
    private Integer value;

    @Column(name = "word")
    private String word;

    public LetterSetValue() {
    }

    public LetterSetValue(Integer id, LetterSet letter_set, Integer value, String word) {
        this.id = id;
        this.letter_set = letter_set;
        this.value = value;
        this.word = word;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public LetterSet getLetter_set() {
        return letter_set;
    }

    public void setLetter_set(LetterSet letter_set) {
        this.letter_set = letter_set;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
