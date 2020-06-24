package com.exam.client.controller;

import com.exam.client.Players;
import com.exam.client.gui.GuiUtility;
import com.exam.domain.*;
import com.exam.service.IAppObserver;
import com.exam.service.IAppServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class GameController extends UnicastRemoteObject implements Initializable, IAppObserver, Serializable {
    public TableView<Players> scoreTable;
    public TableColumn<Players, Integer> p1;
    public TableColumn<Players, Integer> p2;
    public TableColumn<Players, Integer> p3;
    public Label letterSet;
    public TextField newWord;
    public Button send;
    private IAppServices appService;
    private User user;

    public GameController() throws RemoteException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        p1.setCellValueFactory(new PropertyValueFactory<>("p1Score"));
        p2.setCellValueFactory(new PropertyValueFactory<>("p2Score"));
        p3.setCellValueFactory(new PropertyValueFactory<>("p3Score"));
    }

    public void setService(IAppServices appService, User user) {
        this.appService = appService;
        this.user = user;
        Platform.runLater(() -> {
            letterSet.setText(appService.getLetterSet());
        });
    }


    @Override
    public void playerCountUpdated(Integer count, Integer id) {
    }

    @Override
    public void startGame() {
    }

    @Override
    public void loggedIn(User user) {
    }

    @Override
    public void loggedOut(User user) {
    }

    @Override
    public void setScores(Round currentRound) {
        List<Players> players = new ArrayList<>();
        Map<Integer, List<Integer>> scores = new TreeMap<>();
        for (var a : currentRound.getWords())
            scores.put(a.getStudent().getId(), new ArrayList<>());
        for (var word : currentRound.getWords())
            scores.get(word.getStudent().getId()).add(word.getValue());
        List<Integer> finalScores = new ArrayList<>();
        for (var id : scores.keySet().stream().sorted().collect(Collectors.toList()))
            finalScores.add(scores.get(id).stream().mapToInt(Integer::intValue).sum());
        players.add(new Players(finalScores.get(0), finalScores.get(1), finalScores.get(2)));
        Platform.runLater(() -> {
            letterSet.setText(appService.getLetterSet());
            scoreTable.setItems(FXCollections.observableList(players));
        });
    }

    @Override
    public void finishGame(Game game) {
        List<Players> players = new ArrayList<>();
        Map<Integer, List<Integer>> scores = new TreeMap<>();
        for (Round currentRound : game.getRounds()) {
            for (var a : currentRound.getWords())
                scores.put(a.getStudent().getId(), new ArrayList<>());
            break;
        }
        for (Round currentRound : game.getRounds())
            for (var word : currentRound.getWords())
                scores.get(word.getStudent().getId()).add(word.getValue());
        List<Integer> finalScores = new ArrayList<>();
        for (var id : scores.keySet().stream().sorted().collect(Collectors.toList()))
            finalScores.add(scores.get(id).stream().mapToInt(Integer::intValue).sum());
        players.add(new Players(finalScores.get(0), finalScores.get(1), finalScores.get(2)));
        Platform.runLater(() -> {
            scoreTable.setItems(FXCollections.observableList(players));
        });
        send.setDisable(true);
    }

    public void send(ActionEvent actionEvent) {
        appService.sendWord(user, newWord.getText());
    }
}

