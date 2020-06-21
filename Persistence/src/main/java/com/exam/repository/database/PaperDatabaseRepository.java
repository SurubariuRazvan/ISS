package com.exam.repository.database;

import com.exam.domain.Paper;
import com.exam.domain.User;
import com.exam.repository.PaperRepository;
import com.exam.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class PaperDatabaseRepository extends AbstractDatabaseRepository<Paper, Integer> implements PaperRepository {
    public PaperDatabaseRepository() {
        super(Paper.class);
    }
}
