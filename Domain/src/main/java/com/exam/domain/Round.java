package com.exam.domain;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Set;

@Entity
@Table(name = "Round")
public class Round implements com.exam.domain.Entity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "round")
    private Set<Word> words;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "letter_set_id")
    private LetterSet letterSet;

    public Round() {
    }

    public Round(Integer id, Game game, Set<Word> words, LetterSet letterSet) {
        this.id = id;
        this.game = game;
        this.words = words;
        this.letterSet = letterSet;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Set<Word> getWords() {
        return words;
    }

    public void setWords(Set<Word> words) {
        this.words = words;
    }

    public LetterSet getLetterSet() {
        return letterSet;
    }

    public void setLetterSet(LetterSet letterSet) {
        this.letterSet = letterSet;
    }
}
