package com.exam.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Employee_PaperPK implements Serializable {
    @Column(name = "employee_id")
    private Integer employee_id;

    @Column(name = "paper_id")
    private Integer paper_id;

    public Employee_PaperPK() {
    }

    public Employee_PaperPK(Integer employee_id, Integer paper_id) {
        this.employee_id = employee_id;
        this.paper_id = paper_id;
    }

    public Integer getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Integer employee_id) {
        this.employee_id = employee_id;
    }

    public Integer getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(Integer paper_id) {
        this.paper_id = paper_id;
    }
}
