package com.Application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import com.Application.dao.impl.AccountDAOImpl;
import com.Application.dao.impl.UserDAOImpl;
import com.Application.model.Account;
import com.Application.model.User;
import com.Application.service.UserService;
import com.Application.util.SessionManager;

public class AddBeneficiary {
    @FXML
    private TextField nameField;
    @FXML
    private TextField ibanField;

    @FXML
    public void addBeneficiary() {
        String name = nameField.getText().trim();
        String iban = ibanField.getText().trim();
        if (name.isEmpty() || iban.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquant");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }
        UserService userService = new UserService();
        User authenticatedUser = SessionManager.getInstance().getAuthenticatedUser();
        boolean success = userService.addBeneficiary(authenticatedUser.getUserId(), name, iban);

        if (success) {
            showSuccess("Le bénéficiaire a été ajouté avec succès.");
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
