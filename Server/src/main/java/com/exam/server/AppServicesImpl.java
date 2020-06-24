package com.exam.server;

import com.exam.domain.*;
import com.exam.repository.GameRepository;
import com.exam.repository.UserRepository;
import com.exam.service.AppServiceException;
import com.exam.service.IAppObserver;
import com.exam.service.IAppServices;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.hibernate.type.SortedMapType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;


@Service
public class AppServicesImpl implements IAppServices {
    private final UserRepository userRepo;
    private final GameRepository gameRepository;
    private final Map<Integer, LoggedUser> loggedUsers;
    private Game game;
    private Round currentRound;
    private Integer roundNumber;
    private Integer activePlayers = 0;
    private Map<Integer, String> gameStates = new HashMap<>();

    @Autowired
    public AppServicesImpl(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepo = userRepository;
        this.gameRepository = gameRepository;
        this.loggedUsers = new ConcurrentHashMap<>();
        addData();
    }

    private void addData() {
        userRepo.save(new Student(null, "ion1", "a", "Ionel1"));
        userRepo.save(new Student(null, "ion2", "a", "Ionel2"));
        userRepo.save(new Student(null, "ion3", "a", "Ionel3"));
        userRepo.save(new Student(null, "ion4", "a", "Ionel4"));
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
    public void sendNumbers(User user, Integer value1, Integer value2) {
        loggedUsers.get(user.getId()).setBomb1(value1);
        loggedUsers.get(user.getId()).setBomb2(value2);

        activePlayers++;
        for (var u : loggedUsers.values()) {
            try {
                u.getObserver().playerCountUpdated(activePlayers);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void logout(Integer userID) throws AppServiceException {
        LoggedUser loggedUser = loggedUsers.get(userID);
        if (loggedUser != null) {
            loggedUsers.remove(userID);
//            playerCountChanged();
        } else
            throw new AppServiceException("User isn't logged in");
    }

    @Override
    public void notifyStartGame() {
        game = new Game(null);
        currentRound = new Round(null, game, new HashSet<>());
        roundNumber = 0;
        for (var user : loggedUsers.values()) {
            try {
                user.getObserver().startGame();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        for (var id : loggedUsers.keySet())
            this.gameStates.put(id, "______");
    }

    public void handleRound() {
        sendScores(currentRound);
        currentRound = new Round(null, game, new HashSet<>());
    }

    private void sendScores(Round currentRound) {
        for (var user : loggedUsers.values()) {
            try {
                user.getObserver().setScores(currentRound);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendWord(User user, Integer toUser, Integer nr) {
        nr = nr - 1;
        char[] state = this.gameStates.get(toUser).toCharArray();
        int b1 = loggedUsers.get(toUser).getBomb1();
        int b2 = loggedUsers.get(toUser).getBomb2();
        if (b1 - 1 == nr || b2 - 1 == nr || b1 + 1 == nr || b2 + 1 == nr) {
            state[nr] = 'B';
            currentRound.getWords().add(new Word(null, (Student) user, toUser, currentRound, nr + 1, 5, loggedUsers.get(user.getId()).getBomb1(), loggedUsers.get(user.getId()).getBomb2()));
        } else if (b1 == nr || b2 == nr) {
            state[nr] = 'C';
            currentRound.getWords().add(new Word(null, (Student) user, toUser, currentRound, nr + 1, 3, loggedUsers.get(user.getId()).getBomb1(), loggedUsers.get(user.getId()).getBomb2()));
        } else {
            state[nr] = 'S';
            currentRound.getWords().add(new Word(null, (Student) user, toUser, currentRound, nr + 1, 0, loggedUsers.get(user.getId()).getBomb1(), loggedUsers.get(user.getId()).getBomb2()));
        }
        this.gameStates.put(toUser, String.valueOf(state));
        if (currentRound.getWords().size() == 3) {
            game.addRound(currentRound);
            handleRound();
            roundNumber++;
            if (roundNumber == 3)
                gameFinished();
        }
    }

    private void gameFinished() {
        gameRepository.save(game);
        for (var user : loggedUsers.values()) {
            try {
                user.getObserver().finishGame(game);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<Integer, String> getPlayers() {
        return this.gameStates;
    }
}