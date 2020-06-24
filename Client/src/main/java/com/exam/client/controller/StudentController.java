package com.exam.client.controller;

import com.exam.client.gui.GuiUtility;
import com.exam.domain.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
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
    public Spinner<Integer> nr1;
    public Spinner<Integer> nr2;
    private Integer count = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startGame.setDisable(false);
        GuiUtility.initSpinner(nr1, 1, 6);
        GuiUtility.initSpinner(nr2, 1, 6);
    }

    protected void postInitialization() {
    }

    @Override
    public void playerCountUpdated(Integer count) throws RemoteException {
        this.count = count;
        Platform.runLater(() -> {
            waitingLabel.setText(count + "/3 players logged in");
            if (count >= 3) {
                startGame.setText("Start Game");
                startGame.setDisable(false);
            }
        });
    }

    public void start(ActionEvent actionEvent) {
        startGame.setDisable(true);
        if (count >= 3)
            appService.notifyStartGame();
        else
            appService.sendNumbers(user, nr1.getValue(), nr2.getValue());
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
}
