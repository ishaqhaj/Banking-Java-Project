package com.Application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import com.Application.service.UserService;
import com.Application.util.SessionManager;

public class AddBeneficiary {
    @FXML
    private TextField nameField;
    @FXML
    private TextField ibanField;

    @FXML
    public void addBeneficiary(ActionEvent event) {
        String name = nameField.getText().trim();
        String iban = ibanField.getText().trim();

        if (name.isEmpty() || iban.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        UserService userService = new UserService();
        try {
            boolean success = userService.addBeneficiary(name, iban);

            if (success) {
                showSuccess("Le bénéficiaire a été ajouté avec succès.");
            } else {
                if(userService.isBeneficiaryExists(SessionManager.getInstance().getSelectedAccount().getAccountNumber(),iban)){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Bénéficiaire existant");
                    alert.setHeaderText(null);
                    alert.setContentText("Ce bénéficiaire a déjà été ajouté.");
                    alert.showAndWait();
                }
            }
        } catch (IllegalArgumentException e) {
            // Afficher une alerte contenant le message de l'exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur - Bénéficiaire invalide");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
