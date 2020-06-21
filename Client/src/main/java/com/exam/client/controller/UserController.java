package com.exam.client.controller;

import com.exam.domain.Employee;
import com.exam.domain.Employee_Paper;
import com.exam.domain.Paper;
import com.exam.domain.User;
import com.exam.service.AppServiceException;
import com.exam.service.IAppServices;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.List;


public abstract class UserController<U extends User> implements Initializable, IUserController {
    public StackPane rootPane;
    public BorderPane menuPane;
    protected IAppServices appService;
    protected User user;

    public void setService(IAppServices appService, User user) {
        this.appService = appService;
        this.user = user;
        postInitialization();
    }

    protected abstract void postInitialization();

    @Override
    public void logout() {
        try {
            appService.logout(user.getId());
        } catch (AppServiceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateWindows(List<Employee_Paper> papers) {
    }

    @Override
    public void loggedIn(Employee employee) {
    }

    @Override
    public void loggedOut(Employee employee) {
    }
}
