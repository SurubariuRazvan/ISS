package com.exam.server;

import com.exam.domain.*;
import com.exam.repository.GameRepository;
import com.exam.repository.UserRepository;
import com.exam.service.AppServiceException;
import com.exam.service.IAppObserver;
import com.exam.service.IAppServices;
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
    private Category currentCategory;
    private Game game;
    private Round currentRound;
    private Integer roundNumber;
    private final List<String> fruits = new ArrayList<>();
    private final List<String> clothes = new ArrayList<>();
    private final List<String> animals = new ArrayList<>();
    private final Random rand;
    private int roundNr = 1000;
    private int wordNr = 10000;

    @Autowired
    public AppServicesImpl(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepo = userRepository;
        this.gameRepository = gameRepository;
        this.loggedUsers = new ConcurrentHashMap<>();
        this.rand = new Random();

        fruits.add("apple");
        fruits.add("orange");
        fruits.add("mango");
        fruits.add("tomato");
        fruits.add("banana");
        fruits.add("lemon");

        clothes.add("pants");
        clothes.add("t-shirt");
        clothes.add("shoes");
        clothes.add("socks");

        animals.add("marten");
        animals.add("monkey");
        animals.add("dog");
        animals.add("cat");
        animals.add("wolf");

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
    public synchronized void playerCountChanged() {
        for (var user : loggedUsers.values()) {
            try {
                user.getObserver().playerCountUpdated(loggedUsers.size());
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
            playerCountChanged();
        } else
            throw new AppServiceException("User isn't logged in");
    }

    public void sendCategory() {
        for (var user : loggedUsers.values()) {
            try {
                user.getObserver().setCategory(this.currentCategory);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setNewCategory() {
        int value = rand.nextInt(3);
        switch (value) {
            case 0:
                this.currentCategory = Category.ANIMALS;
                break;
            case 1:
                this.currentCategory = Category.CLOTHES;
                break;
            case 2:
                this.currentCategory = Category.FRUITS;
                break;
        }
    }

    @Override
    public void notifyStartGame() {
        setNewCategory();
        game = new Game(null);
        currentRound = new Round(null, this.currentCategory, game, new HashSet<>());
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
        setNewCategory();
        sendCategory();
        sendScores(currentRound);
        currentRound = new Round(null, currentCategory, game, new HashSet<>());
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
        currentRound.getWords().add(new Word(null, (Student) user, currentRound, text, 5));
        if (currentRound.getWords().size() == 3) {
            for (Word currentWord : currentRound.getWords())
                switch (currentCategory) {
                    case ANIMALS:
                        if (animals.contains(currentWord.getWord())) {
                            for (Round round : game.getRounds())
                                for (Word word : round.getWords())
                                    if (word.getWord().equals(currentWord.getWord()))
                                        currentWord.setValue(2);
                        } else currentWord.setValue(0);
                        break;
                    case CLOTHES:
                        if (clothes.contains(currentWord.getWord())) {
                            for (var round : game.getRounds())
                                for (var word : round.getWords())
                                    if (word.getWord().equals(currentWord.getWord()))
                                        currentWord.setValue(2);
                        } else currentWord.setValue(0);
                        break;
                    case FRUITS:
                        if (fruits.contains(currentWord.getWord())) {
                            for (var round : game.getRounds())
                                for (var word : round.getWords())
                                    if (word.getWord().equals(currentWord.getWord()))
                                        currentWord.setValue(2);
                        } else currentWord.setValue(0);
                        break;
                }
            game.addRound(currentRound);
            handleRound();
            roundNumber++;
            if (roundNumber == 5)
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
    public String getCategory() {
        return currentCategory.toString();
    }
}