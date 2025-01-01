package com.Application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import com.Application.dao.impl.AccountDAOImpl;
import com.Application.model.Account;
import com.Application.util.SessionManager;

import java.util.Set;
import java.util.function.Consumer;

public class SelectAccount {

    @FXML
    private AnchorPane accountContainer;

    @FXML
    private Text instructionText;

    private ToggleGroup group;

    // Champ pour stocker l'action de redirection
    private Consumer<Account> onAccountSelected;

    @FXML
    public void initialize() {
        AccountDAOImpl accountDAO = new AccountDAOImpl();
        Set<String> userAccounts = accountDAO.getUserAccounts(SessionManager.getInstance().getAuthenticatedUser().getUserId());
        displayUserAccounts(userAccounts);
    }

    public void displayUserAccounts(Set<String> accounts) {
        group = new ToggleGroup();
        double yPosition = instructionText.getLayoutY() + 40;

        for (String account : accounts) {
            RadioButton radioButton = new RadioButton(account);
            radioButton.setToggleGroup(group);
            radioButton.setLayoutX(instructionText.getLayoutX());
            radioButton.setLayoutY(yPosition);
            accountContainer.getChildren().add(radioButton);
            yPosition += 30;
        }
    }
    // Setter pour définir l'action à exécuter après sélection
    public void setOnAccountSelected(Consumer<Account> onAccountSelected) {
        this.onAccountSelected = onAccountSelected;
    }

    @FXML
    public void handleSelection() {
        RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();

        if (selectedRadioButton != null) {
            String selectedAccountNumber = selectedRadioButton.getText();
            AccountDAOImpl accountDAO = new AccountDAOImpl();
            Account selectedAccount = accountDAO.getAccount(selectedAccountNumber);
            SessionManager.getInstance().setSelectedAccount(selectedAccount);
            // Vérifiez si une action de redirection est définie
            if (onAccountSelected != null) {
                onAccountSelected.accept(selectedAccount); // Exécute la redirection
            }
            // Appel correct à navigateAfterAccountSelection
            else {
                Accueil accueilController = (Accueil) SessionManager.getInstance().getController("Accueil");
                accueilController.navigateAfterAccountSelectionSimple();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sélection requise");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un compte avant de continuer.");
            alert.showAndWait();
        }
    }
}
