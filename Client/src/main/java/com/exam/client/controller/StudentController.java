package com.exam.client.controller;

import com.exam.domain.*;
import com.exam.service.AppServiceException;
import com.exam.service.IAppServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class StudentController implements Initializable, IUserController {
    public StackPane rootPane;
    public BorderPane menuPane;
    public Label waitingLabel;
    public Button startGame;
    protected IAppServices appService;
    public GameController gameController;
    private User user;

    public void setService(IAppServices appService, User user) {
        this.appService = appService;
        this.user = user;
        appService.playerCountChanged(user.getId());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startGame.setDisable(true);
    }

    @Override
    public void playerCountUpdated(Integer count, Integer id) throws RemoteException {
        Platform.runLater(() -> {
            waitingLabel.setText(count + "/3 players logged in");
            if (count >= 3 && user.getId().equals(id))
                startGame.setDisable(false);
        });
    }

    public void start(ActionEvent actionEvent) {
        appService.notifyStartGame();
    }

    @Override
    public void startGame() {
        Platform.runLater(() -> {
            String controllerPath = "/view/" + "Game" + "View.fxml";
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(controllerPath));
                StackPane rootLayout = loader.load();
                gameController = loader.getController();

                gameController.setService(appService, user);
                Stage primaryStage = new Stage();
                primaryStage.setMinWidth(800);
                primaryStage.setMinHeight(500);
                primaryStage.setTitle("Window: " + user.getName());
                primaryStage.setScene(new Scene(rootLayout));
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void setScores(Round currentRound) {
        gameController.setScores(currentRound);
    }

    @Override
    public void finishGame(Game game) throws RemoteException {
        gameController.finishGame(game);
    }

    @Override
    public void logout() {
        try {
            appService.logout(user.getId());
        } catch (AppServiceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loggedIn(User user) throws RemoteException {

    }

    @Override
    public void loggedOut(User user) throws RemoteException {

    }
}
