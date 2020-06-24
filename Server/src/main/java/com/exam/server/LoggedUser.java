package com.exam.server;

import com.exam.domain.UserType;
import com.exam.service.IAppObserver;

public class LoggedUser {
    private UserType userType;
    private Integer bomb1;
    private Integer bomb2;
    private IAppObserver observer;

    public LoggedUser(UserType userType, IAppObserver observer) {
        this.userType = userType;
        this.observer = observer;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public IAppObserver getObserver() {
        return observer;
    }

    public void setObserver(IAppObserver observer) {
        this.observer = observer;
    }

    public Integer getBomb1() {
        return bomb1;
    }

    public void setBomb1(Integer bomb1) {
        this.bomb1 = bomb1;
    }

    public Integer getBomb2() {
        return bomb2;
    }

    public void setBomb2(Integer bomb2) {
        this.bomb2 = bomb2;
    }
}
