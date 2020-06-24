package com.exam.repository.database;

import com.exam.domain.LetterSet;
import com.exam.repository.WordRepository;
import org.springframework.stereotype.Repository;

@Repository
public class WordDatabaseRepository extends AbstractDatabaseRepository<LetterSet, Integer> implements WordRepository {
    public WordDatabaseRepository() {
        super(LetterSet.class);
    }
}
