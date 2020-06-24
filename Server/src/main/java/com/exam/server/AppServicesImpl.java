package com.exam.server;

import com.exam.domain.*;
import com.exam.repository.GameRepository;
import com.exam.repository.UserRepository;
import com.exam.repository.WordRepository;
import com.exam.service.AppServiceException;
import com.exam.service.IAppObserver;
import com.exam.service.IAppServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class AppServicesImpl implements IAppServices {
    private final UserRepository userRepo;
    private final GameRepository gameRepository;
    private final WordRepository wordRepository;
    private final Map<Integer, LoggedUser> loggedUsers;
    private final Random rand;
    private Game game;
    private Round currentRound;
    private Integer roundNumber;
    private LetterSet currentLetterSet;

    @Autowired
    public AppServicesImpl(UserRepository userRepository, GameRepository gameRepository, WordRepository wordRepository) {
        this.userRepo = userRepository;
        this.gameRepository = gameRepository;
        this.wordRepository = wordRepository;
        this.loggedUsers = new ConcurrentHashMap<>();
        this.rand = new Random();
        addData();
    }

    private void addData() {
        userRepo.save(new Student(null, "ion1", "a", "Ionel1"));
        userRepo.save(new Student(null, "ion2", "a", "Ionel2"));
        userRepo.save(new Student(null, "ion3", "a", "Ionel3"));
        userRepo.save(new Student(null, "ion4", "a", "Ionel4"));

        LetterSet letterSet = new LetterSet(null, "['a','c','e','r']", null, new HashSet<>());
        Set<LetterSetValue> letterSetValue = new HashSet<>();
        letterSetValue.add(new LetterSetValue(null, letterSet, 4, "care"));
        letterSetValue.add(new LetterSetValue(null, letterSet, 6, "acre"));
        letterSetValue.add(new LetterSetValue(null, letterSet, 7, "arce"));
        letterSetValue.add(new LetterSetValue(null, letterSet, 3, "cer"));
        letterSetValue.add(new LetterSetValue(null, letterSet, 4, "arc"));
        letterSetValue.add(new LetterSetValue(null, letterSet, 3, "car"));
        letterSet.setLetterSetValue(letterSetValue);
        wordRepository.save(letterSet);

        LetterSet letterSet1 = new LetterSet(null, "['a','b','c','r']", null, new HashSet<>());
        Set<LetterSetValue> letterSetValue1 = new HashSet<>();
        letterSetValue1.add(new LetterSetValue(null, letterSet1, 4, "bac"));
        letterSetValue1.add(new LetterSetValue(null, letterSet1, 6, "rac"));
        letterSetValue1.add(new LetterSetValue(null, letterSet1, 7, "crab"));
        letterSet1.setLetterSetValue(letterSetValue1);
        wordRepository.save(letterSet1);
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
    public String getLetterSet() {
        return currentLetterSet.getLetter_Set();
    }

    @Override
    public synchronized void playerCountChanged(Integer id) {
        for (var user : loggedUsers.values()) {
            try {
                user.getObserver().playerCountUpdated(loggedUsers.size(), id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setNewLetterSet() {
        int value = rand.nextInt(2);
        for (var letterSet : wordRepository.findAll())
            if (value-- == 0) {
                this.currentLetterSet = letterSet;
                break;
            }
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
    public void notifyStartGame() {
        setNewLetterSet();
        game = new Game(null);
        currentRound = new Round(null, game, new HashSet<>(), currentLetterSet);
        roundNumber = 0;
        for (var user : loggedUsers.values()) {
            try {
                user.getObserver().startGame();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleRound() {
        setNewLetterSet();
        sendScores(currentRound);
        currentRound = new Round(null, game, new HashSet<>(), currentLetterSet);
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
    public void sendWord(User user, String text) {
        String strippedLetterSet = currentLetterSet.getLetter_Set().replace("[", "").replace(",", "").replace("]", "").replace("'", "");
        LetterSetValue found = null;
        for (var a : currentLetterSet.getLetterSetValue())
            if (a.getWord().equals(text))
                found = a;
        if (found == null)
            currentRound.getWords().add(new Word(null, (Student) user, currentRound, text, 0));
        else if (found.getWord().length() == strippedLetterSet.length())
            currentRound.getWords().add(new Word(null, (Student) user, currentRound, text, found.getValue()));
        else
            currentRound.getWords().add(new Word(null, (Student) user, currentRound, text, found.getValue() - strippedLetterSet.length() + text.length()));
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
}