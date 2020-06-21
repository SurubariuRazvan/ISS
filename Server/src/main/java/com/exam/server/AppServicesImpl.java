package com.exam.server;

import com.exam.domain.*;
import com.exam.repository.UserRepository;
import com.exam.service.AppServiceException;
import com.exam.service.IAppObserver;
import com.exam.service.IAppServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class AppServicesImpl implements IAppServices {
    private final UserRepository userRepo;
    private final Map<Integer, LoggedUser> loggedUsers;

    @Autowired
    public AppServicesImpl(UserRepository userRepository) {
        this.userRepo = userRepository;
        this.loggedUsers = new ConcurrentHashMap<>();
        addData();
    }

    private void addData() {
        userRepo.save(new Administrator(null, "ion1", "a", "Ionel1"));
        userRepo.save(new Manager(null, "ion2", "a", "Ionel2"));
        userRepo.save(new Manager(null, "ion3", "a", "X Æ A-12"));
        userRepo.save(new Employee(null, "ion4", "a", "\\\\null"));
        userRepo.save(new Employee(null, "ion5", "a", "\\\\0"));
        Employee employee = (Employee) userRepo.findByUsernameAndPassword("ion4", User.encodePassword("a"));
        employee.addTask(new Task(1, "Task1"));
        userRepo.update(employee);
    }

    @Override
    public synchronized User login(String username, String password, IAppObserver client) throws AppServiceException {
        User user = userRepo.findByUsernameAndPassword(username, password);
        if (user == null)
            return null;
        if (loggedUsers.containsKey(user.getId()))
            throw new AppServiceException("User already logged in");
        loggedUsers.put(user.getId(), new LoggedUser(user.getUserType(), client));
        if (user.getUserType().equals(UserType.Employee)) {
            Employee employee = (Employee) user;
            employee.setLastClockInTime(new Timestamp(System.currentTimeMillis()));
            employee.setEmployeeState(EmployeeState.ATWORK);
            userRepo.update(employee);
            notifyManagerEmployeeLoggedIn(employee);
        }
        return user;
    }

    @Override
    public synchronized void logout(Integer userID) throws AppServiceException {
        LoggedUser loggedUser = loggedUsers.get(userID);
        if (loggedUser != null) {
            if (loggedUser.getUserType().equals(UserType.Employee)) {
                Employee employee = (Employee) userRepo.findByID(userID);
                employee.setEmployeeState(EmployeeState.ATHOME);
                userRepo.update(employee);
                notifyManagerEmployeeLoggedOut(employee);
            }
            loggedUsers.remove(userID);
        } else
            throw new AppServiceException("User isn't logged in");
    }

    @Override
    public synchronized void sendTask(Employee employee, String taskDescription) {
        Task task = new Task(null, taskDescription);
        employee.addTask(task);
        userRepo.update(employee);

        if (loggedUsers.get(employee.getId()) != null)
            try {
                loggedUsers.get(employee.getId()).getObserver().updateWindows(task);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void addUser(User user) {
        notifyAdministrator(userRepo.save(user), false);
    }

    @Override
    public void updateUser(User user) {
        notifyAdministrator(userRepo.update(user), false);
    }

    @Override
    public void deleteUser(Integer userId) {
        notifyAdministrator(userRepo.delete(userId), true);
    }

    @Override
    public List<Task> findAllTasksForEmployee(Employee employee) {
        return employee.getTasks();
    }

    private void notifyAdministrator(User user, Boolean isDelete) {
        for (LoggedUser appObserver : loggedUsers.values())
            if (appObserver != null && appObserver.getUserType().equals(UserType.Administrator))
                try {
                    System.out.println("notifyAdministrator");
                    if (user.getUserType().equals(UserType.Employee))
                        appObserver.getObserver().updateAdministratorWindow((Employee) user, null, isDelete);
                    else
                        appObserver.getObserver().updateAdministratorWindow(null, (Manager) user, isDelete);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
    }

    private void notifyManagerEmployeeLoggedIn(Employee employee) {
        for (LoggedUser appObserver : loggedUsers.values())
            if (appObserver != null && appObserver.getUserType().equals(UserType.Manager))
                try {
                    appObserver.getObserver().employeeLoggedIn(employee);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
    }

    private void notifyManagerEmployeeLoggedOut(Employee employee) {
        for (LoggedUser appObserver : loggedUsers.values())
            if (appObserver != null && appObserver.getUserType().equals(UserType.Manager))
                try {
                    appObserver.getObserver().employeeLoggedOut(employee);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
    }

    @Override
    public synchronized List<Employee> findAllWorkingEmployees() {
        List<Employee> employees = new ArrayList<>();
        for (var employee : userRepo.findAllByUserType(UserType.Employee))
            if (((Employee) employee).getEmployeeState().equals(EmployeeState.ATWORK))
                employees.add((Employee) employee);
        return employees;
    }

    @Override
    public List<Employee> findAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        for (var employee : userRepo.findAllByUserType(UserType.Employee))
            employees.add((Employee) employee);
        return employees;
    }

    @Override
    public List<Manager> findAllManagers() {
        List<Manager> managers = new ArrayList<>();
        for (var manager : userRepo.findAllByUserType(UserType.Manager))
            managers.add((Manager) manager);
        return managers;
    }
}