package com.exam.server;

import com.exam.domain.*;
import com.exam.repository.Employee_PaperRepository;
import com.exam.repository.PaperRepository;
import com.exam.repository.UserRepository;
import com.exam.service.AppServiceException;
import com.exam.service.IAppObserver;
import com.exam.service.IAppServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class AppServicesImpl implements IAppServices {
    private final UserRepository userRepo;
    private final PaperRepository paperRepo;
    private final Employee_PaperRepository employee_paperRepo;
    private final Map<Integer, LoggedUser> loggedUsers;

    @Autowired
    public AppServicesImpl(UserRepository userRepository, PaperRepository paperRepository, Employee_PaperRepository employee_paperRepo) {
        this.userRepo = userRepository;
        this.paperRepo = paperRepository;
        this.employee_paperRepo = employee_paperRepo;
        this.loggedUsers = new ConcurrentHashMap<>();
        addData();
    }

    private void addData() {
        userRepo.save(new Employee(null, "ion1", "a", "Ionel1"));
        userRepo.save(new Employee(null, "ion2", "a", "Ionel2"));
        userRepo.save(new Employee(null, "ion3", "a", "Ionel3"));
        userRepo.save(new Employee(null, "ion4", "a", "Ionel4"));
        Employee employee1 = (Employee) userRepo.findByUsernameAndPassword("ion1", User.encodePassword("a"));
        Employee employee2 = (Employee) userRepo.findByUsernameAndPassword("ion2", User.encodePassword("a"));
        Employee employee3 = (Employee) userRepo.findByUsernameAndPassword("ion3", User.encodePassword("a"));
        Employee employee4 = (Employee) userRepo.findByUsernameAndPassword("ion4", User.encodePassword("a"));


        paperRepo.save(new Paper(null, "Paper1", new Participant(null, "Marcel"), employee1, employee2));
        paperRepo.save(new Paper(null, "Paper2", new Participant(null, "Ionica"), employee2, employee3));
        paperRepo.save(new Paper(null, "Paper3", new Participant(null, "Gigel"), employee3, employee4));
        paperRepo.save(new Paper(null, "Paper4", new Participant(null, "Fancesco"), employee1, employee3));
    }

    @Override
    public synchronized User login(String username, String password, IAppObserver client) throws AppServiceException {
        User user = userRepo.findByUsernameAndPassword(username, password);
        if (user == null)
            return null;
        if (loggedUsers.containsKey(user.getId()))
            throw new AppServiceException("User already logged in");
        loggedUsers.put(user.getId(), new LoggedUser(user.getUserType(), client));
        return user;
    }

    @Override
    public synchronized void logout(Integer userID) throws AppServiceException {
        LoggedUser loggedUser = loggedUsers.get(userID);
        if (loggedUser != null) {
            loggedUsers.remove(userID);
        } else
            throw new AppServiceException("User isn't logged in");
    }

    @Override
    public List<Employee_Paper> findAllTasksForEmployee(Employee employee) {
        return employee.getPapers();
    }

    @Override
    public void updateGrade(Employee_Paper entity) {
        employee_paperRepo.update(entity);
        for (var id : loggedUsers.keySet()) {
            try {
                loggedUsers.get(id).getObserver().updateWindows(((Employee) userRepo.findByID(id)).getPapers());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}