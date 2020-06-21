package com.exam.service;

import com.exam.domain.Employee;
import com.exam.domain.Employee_Paper;
import com.exam.domain.Paper;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IAppObserver extends Remote {
    void updateWindows(List<Employee_Paper> papers) throws RemoteException;

    void loggedIn(Employee employee) throws RemoteException;

    void loggedOut(Employee employee) throws RemoteException;
}
