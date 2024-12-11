package main.java.com.Application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;

public class Login {
    @FXML
    private TextField userIdField;
    @FXML
    private PasswordField passwordField;

    @FXML
    public void createUser()  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/userRegistration.fxml"));
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


    @FXML
    public void authenticateUser(){

    }
}
