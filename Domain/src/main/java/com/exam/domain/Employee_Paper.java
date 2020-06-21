package com.exam.domain;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Table(name = "Employee_Paper")
public class Employee_Paper implements com.exam.domain.Entity<Employee_PaperPK> {
    @EmbeddedId
    private Employee_PaperPK id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapsId("employee_id")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapsId("paper_id")
    @JoinColumn(name = "paper_id")
    private Paper paper;

    @Column(name = "grade")
    private Double grade;

    public Employee_Paper() {
    }

    public Employee_Paper(Employee employee, Paper paper) {
        this.id = new Employee_PaperPK(employee.getId(), paper.getId());
        this.employee = employee;
        this.paper = paper;
        this.grade = 0.0;
    }

    public Employee_PaperPK getId() {
        return id;
    }

    public void setId(Employee_PaperPK id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
