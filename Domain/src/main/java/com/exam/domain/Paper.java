package com.exam.domain;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Paper")
public class Paper implements com.exam.domain.Entity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "description")
    private String description;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Participant participant;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "paper")
    private List<Employee_Paper> employees;

    public Paper() {
    }

    public Paper(Integer id, String description, Participant participant, Employee employee1, Employee employee2) {
        this.id = id;
        this.description = description;
        this.participant = participant;
        this.employees = new ArrayList<>();
        this.employees.add(new Employee_Paper(employee1,this));
        this.employees.add(new Employee_Paper(employee2,this));
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public List<Employee_Paper> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee_Paper> employees) {
        this.employees = employees;
    }
}
