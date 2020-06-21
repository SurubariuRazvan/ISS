package com.exam.domain;

import javax.persistence.*;
import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Employee")
public class Employee extends User {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "employee")
    private List<Employee_Paper> papers;

    public Employee() {
        this.papers = new ArrayList<>();
    }

    public Employee(User user) {
        super(user);
        super.setUserType(UserType.Employee);
        this.papers = new ArrayList<>();
    }

    public Employee(Integer id, String username, String password, String name) {
        super(id, username, password, name, UserType.Employee);
        this.papers = new ArrayList<>();
    }

    public List<Employee_Paper> getPapers() {
        return papers;
    }

    public void setPapers(List<Employee_Paper> papers) {
        this.papers = papers;
    }
}
