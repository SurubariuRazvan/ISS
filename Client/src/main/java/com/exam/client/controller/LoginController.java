package com.exam.client.controller;

import com.exam.domain.Employee;
import com.exam.domain.Manager;
import com.exam.domain.Task;
import com.exam.domain.User;
import com.exam.service.AppServiceException;
import com.exam.service.IAppObserver;
import com.exam.service.IAppServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ResourceBundle;

public class LoginController extends UnicastRemoteObject implements Initializable, IAppObserver, Serializable {

    public TextField logInUsername;
    public PasswordField logInPassword;
    public Button logInButton;
    public StackPane rootPane;
    public HBox menuTable;
    private IAppServices loginService;
    private Stage primaryStage;
    private IUserController appController;

    public LoginController() throws RemoteException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setService(IAppServices loginService) {
        this.loginService = loginService;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void login(ActionEvent actionEvent) {
        String username = logInUsername.getText();
        String password = logInPassword.getText();

        try {
            User user = loginService.login(username, User.encodePassword(password), this);
            if (user == null)
                failedLogin();
            else
                successfulLogin(user);
        } catch (AppServiceException e) {
            GuiUtility.showError(this.rootPane, this.menuTable, "Login error", e.getMessage());
        }
    }

    private void failedLogin() {
        logInUsername.getStyleClass().add("wrong-credentials");
        logInUsername.setText("");
        logInPassword.getStyleClass().add("wrong-credentials");
        logInPassword.setText("");
    }

    private void successfulLogin(User user) {
        String controllerPath = "/view/" + user.getUserType().toString() + "View.fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(controllerPath));
            StackPane rootLayout = loader.load();
            appController = loader.getController();

            appController.setService(loginService, user);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(500);
            primaryStage.setTitle(user.getUserType().toString() + " Window: " + user.getName());
            primaryStage.setScene(new Scene(rootLayout));
            primaryStage.setOnCloseRequest(event -> {
                appController.logout();
                System.exit(0);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAdministratorWindow(Employee employee, Manager manager, Boolean isDelete) throws RemoteException {
        appController.updateAdministratorWindow(employee, manager, isDelete);
    }

    @Override
    public void updateWindows(Task task) throws RemoteException {
        appController.updateWindows(task);
    }

    @Override
    public void employeeLoggedIn(Employee employee) throws RemoteException {
        appController.employeeLoggedIn(employee);
    }

    @Override
    public void employeeLoggedOut(Employee employee) throws RemoteException {
        appController.employeeLoggedOut(employee);
    }
}
