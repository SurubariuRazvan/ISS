package com.exam.repository;

import com.exam.domain.User;
import com.exam.domain.UserType;

import java.util.List;


public interface UserRepository extends CRUDRepository<User, Integer> {
    User findByUsernameAndPassword(String username, String password);
}
