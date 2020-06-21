package com.exam.service;

import com.exam.domain.*;

import java.util.List;

public interface IAppServices {
    List<Employee_Paper> findAllTasksForEmployee(Employee employee) throws AppServiceException;

    User login(String username, String password, IAppObserver client) throws AppServiceException;

    void logout(Integer userID) throws AppServiceException;

    void updateGrade(Employee_Paper entity);
}
