package com.exam.domain;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Set;

@Entity
@Table(name = "Student")
public class Student extends User {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "student")
    private Set<Word> words;

    public Student() {
    }

    public Student(User user) {
        super(user);
        super.setUserType(UserType.Student);
    }

    public Student(Integer id, String username, String password, String name) {
        super(id, username, password, name, UserType.Student);
    }

    public Set<Word> getWords() {
        return words;
    }

    public void setWords(Set<Word> words) {
        this.words = words;
    }
}
