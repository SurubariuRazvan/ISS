package com.exam.client.controller;

import com.exam.client.Players;
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

public class GameController extends UnicastRemoteObject implements Initializable, IAppObserver, Serializable {
    public Label categoryLabel;
    public TextField newWord;
    public Button sendButton;
    public TableView<Players> scoreTable;
    public TableColumn<Players, Integer> p1;
    public TableColumn<Players, Integer> p2;
    public TableColumn<Players, Integer> p3;
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
        Platform.runLater(() -> categoryLabel.setText(appService.getCategory()));
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
    public void setCategory(Category currentCategory) throws RemoteException {
        Platform.runLater(() -> categoryLabel.setText(currentCategory.name()));
    }

    @Override
    public void setScores(Round currentRound) {
        List<Players> players = new ArrayList<>();
        var pp = new ArrayList<>(currentRound.getWords());
        players.add(new Players(pp.get(0).getValue(), pp.get(1).getValue(), pp.get(2).getValue()));
        Platform.runLater(() -> {
            scoreTable.setItems(FXCollections.observableList(players));
        });
    }

    @Override
    public void finishGame(Game game) throws RemoteException {
        List<Players> players = new ArrayList<>();
        for (Round currentRound : game.getRounds()) {
            var pp = new ArrayList<>(currentRound.getWords());
            players.add(new Players(pp.get(0).getValue(), pp.get(1).getValue(), pp.get(2).getValue()));
        }
        Platform.runLater(() -> {
            scoreTable.setItems(FXCollections.observableList(players));
            sendButton.setDisable(true);
        });
    }

    public void send(ActionEvent actionEvent) {
        appService.sendWord(user, newWord.getText());
    }
}

