package com.exam.repository.database;

import com.exam.domain.Employee_Paper;
import com.exam.domain.Employee_PaperPK;
import com.exam.domain.Paper;
import com.exam.repository.Employee_PaperRepository;
import com.exam.repository.PaperRepository;
import org.springframework.stereotype.Repository;

@Repository
public class Employee_PaperDatabaseRepository extends AbstractDatabaseRepository<Employee_Paper, Employee_PaperPK> implements Employee_PaperRepository {
    public Employee_PaperDatabaseRepository() {
        super(Employee_Paper.class);
    }
}
