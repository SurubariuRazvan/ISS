package com.exam.client.controller;

import com.exam.domain.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class StudentController extends UserController<Student> {

    public Label waitingLabel;
    public Button startGame;
    public GameController gameController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startGame.setDisable(true);
    }

    protected void postInitialization() {
        appService.playerCountChanged();
    }

    @Override
    public void playerCountUpdated(Integer count) throws RemoteException {
        Platform.runLater(() -> {
            waitingLabel.setText(count + "/3 players logged in");
            startGame.setDisable(count < 3);
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
    public void setCategory(Category currentCategory) throws RemoteException {
        gameController.setCategory(currentCategory);
    }

    @Override
    public void setScores(Round currentRound) {
        gameController.setScores(currentRound);
    }

    @Override
    public void finishGame(Game game) throws RemoteException {
        gameController.finishGame(game);
    }
}
