package com.exam.domain;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;
import java.util.Set;

@Entity
@Table(name = "Game")
public class Game implements com.exam.domain.Entity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "game")
    private Set<Round> rounds;

    public Game() {
    }

    public Game(Integer id) {
        this.id = id;
        this.rounds = new HashSet<>();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Round> getRounds() {
        return rounds;
    }

    public void setRounds(Set<Round> rounds) {
        this.rounds = rounds;
    }

    public void addRound(Round round){
        this.rounds.add(round);
    }
}
