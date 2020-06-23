package com.exam.client.controller;

import com.exam.domain.User;
import com.exam.service.AppServiceException;
import com.exam.service.IAppServices;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;


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
    public void startGame() throws RemoteException {

    }

    @Override
    public void playerCountUpdated(Integer count) throws RemoteException {

    }

    @Override
    public void loggedIn(User user) throws RemoteException {

    }

    @Override
    public void loggedOut(User user) {
    }
}
