package com.exam.repository.database;

import com.exam.domain.Game;
import com.exam.domain.User;
import com.exam.repository.GameRepository;
import com.exam.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class GameDatabaseRepository extends AbstractDatabaseRepository<Game, Integer> implements GameRepository {
    public GameDatabaseRepository() {
        super(Game.class);
    }
}
