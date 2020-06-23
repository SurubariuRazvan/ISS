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

    @Column(name = "category")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "round")
    private Set<Word> words;

    public Round() {
    }

    public Round(Integer id, Category category, Game game, Set<Word> words) {
        this.id = id;
        this.category = category;
        this.game = game;
        this.words = words;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    @Override
    public String toString() {
        return "Round{" +
                "id=" + id +
                ", category=" + category +
                ", game=" + game +
                ", words=" + words +
                '}';
    }
}
