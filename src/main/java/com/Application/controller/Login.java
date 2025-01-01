package com.Application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.Application.dao.impl.UserDAOImpl;
import com.Application.model.User;
import com.Application.service.UserService;
import com.Application.util.SessionManager;

import java.io.IOException;

public class Login {
    @FXML
    private TextField userIdField;
    @FXML
    private PasswordField passwordField;
    @FXML
    public void authenticateUser(){
        String userId=userIdField.getText();
        String password=passwordField.getText();
        if (userId != null && !userId.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
            UserService userService = new UserService();
            boolean isAuthenticated = userService.authenticateUser(userId, password);
            if (!isAuthenticated) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les informations entrées sont incorrectes");
            }
            else {
                UserDAOImpl userDAO = new UserDAOImpl();
                User authenticatedUser=userDAO.getUser(userId);
                SessionManager.getInstance().setAuthenticatedUser(authenticatedUser);
                closeWindow();
                // Charger l'interface Accueil.fxml
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Accueil.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Accueil");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
        }
    }
    @FXML
    public void createUser()  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/userRegistration.fxml"));
        try{
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Inscription");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) userIdField.getScene().getWindow(); // Récupération de la fenêtre actuelle
        stage.close(); // Fermeture de la fenêtre
    }

}
