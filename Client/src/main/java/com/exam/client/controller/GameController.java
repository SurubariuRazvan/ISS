package com.exam.client.controller;

import com.exam.client.Players;
import com.exam.client.gui.GuiUtility;
import com.exam.domain.*;
import com.exam.service.AppServiceException;
import com.exam.service.IAppObserver;
import com.exam.service.IAppServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.type.OrderedMapType;

import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.sum;

public class GameController extends UnicastRemoteObject implements Initializable, IAppObserver, Serializable {
    public TableView<Players> scoreTable;
    public TableColumn<Players, Integer> p1;
    public TableColumn<Players, Integer> p2;
    public TableColumn<Players, Integer> p3;
    public Label p1Field;
    public Spinner<Integer> p1nr;
    public Button p1Button;
    public Label p2Field;
    public Spinner<Integer> p2nr;
    public Button p2Button;
    private IAppServices appService;
    private User user;
    private Integer p1ID;
    private Integer p2ID;

    public GameController() throws RemoteException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        p1.setCellValueFactory(new PropertyValueFactory<>("p1Score"));
        p2.setCellValueFactory(new PropertyValueFactory<>("p2Score"));
        p3.setCellValueFactory(new PropertyValueFactory<>("p3Score"));
        GuiUtility.initSpinner(p1nr, 1, 6);
        GuiUtility.initSpinner(p2nr, 1, 6);
    }

    public void setService(IAppServices appService, User user) {
        this.appService = appService;
        this.user = user;
        updateWindow(appService, user);
    }


    @Override
    public void playerCountUpdated(Integer count) throws RemoteException {
    }

    @Override
    public void startGame() throws RemoteException {
    }

    @Override
    public void loggedIn(User user) throws RemoteException {
    }

    @Override
    public void loggedOut(User user) throws RemoteException {
    }

    @Override
    public void setScores(Round currentRound) {
        updateWindow(appService, user);
    }

    private void updateWindow(IAppServices appService, User user) {
        Map<Integer, String> gameState = appService.getPlayers();
        Platform.runLater(() -> {
            boolean p1IsSet = false;
            for (var a : gameState.keySet().stream().sorted().collect(Collectors.toList()))
                if (!a.equals(user.getId()))
                    if (!p1IsSet) {
                        p1Field.setText(a + ": " + gameState.get(a));
                        p1IsSet = true;
                        p1ID = a;
                    } else {
                        p2Field.setText(a + ": " + gameState.get(a));
                        p2ID = a;
                    }
        });
    }

    @Override
    public void finishGame(Game game) {
        List<Players> players = new ArrayList<>();
        Map<Integer, List<Integer>> scores = new TreeMap<>();
        for (var a : appService.getPlayers().keySet())
            scores.put(a, new ArrayList<>());
        for (Round currentRound : game.getRounds())
            for (var word : currentRound.getWords())
                scores.get(word.getStudent().getId()).add(word.getValue());
        List<Integer> finalScores = new ArrayList<>();
        for (var a : scores.values())
            finalScores.add(a.stream().mapToInt(Integer::intValue).sum());
        players.add(new Players(finalScores.get(0), finalScores.get(1), finalScores.get(2)));
        Platform.runLater(() -> {
            scoreTable.setItems(FXCollections.observableList(players));
        });
    }


    public void p1Send(ActionEvent actionEvent) {
        appService.sendWord(user, p1ID, p1nr.getValue());
    }

    public void p2Send(ActionEvent actionEvent) {
        appService.sendWord(user, p2ID, p2nr.getValue());
    }
}

