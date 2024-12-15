package com.Application.controller;

import com.Application.model.Account;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import com.Application.dao.impl.AccountDAOImpl;
import com.Application.dao.impl.UserDAOImpl;
import com.Application.model.User;
import com.Application.service.UserService;
import com.Application.util.SessionManager;

import java.util.Map;

public class SelectBeneficiary {
    @FXML
    private AnchorPane anchor;
    @FXML
    private Text title;
    @FXML
    private TextField searchField; // A VBox in your FXML to contain the radio buttons.

    private ToggleGroup group;

    @FXML
    public void initialize() {
        String SelectedAccount = SessionManager.getInstance().getSelectedAccount().getAccountNumber();
        // Charger les bénéficiaires via UserService
        UserService userService = new UserService();
        Map<String, String> beneficiaries = userService.getUserBeneficiaries(SelectedAccount);
        if(!beneficiaries.isEmpty()) {
            group = new ToggleGroup();
            VBox beneficiaryContainer = new VBox(10); // Espacement vertical entre les boutons radio

            // Positionner le VBox en fonction du texte "title"
            beneficiaryContainer.setLayoutX(searchField.getLayoutX()); // Aligner horizontalement avec le titre
            beneficiaryContainer.setLayoutY(searchField.getLayoutY() + 40); // Placer sous le titre

            // Parcourir la map des bénéficiaires et créer un bouton radio pour chaque entrée
            for (Map.Entry<String, String> entry : beneficiaries.entrySet()) {
                String name = entry.getKey();  // Nom du bénéficiaire
                String iban = entry.getValue(); // IBAN du bénéficiaire

                // Créer un HBox pour aligner horizontalement le nom et l'IBAN
                HBox beneficiaryRow = new HBox(20); // Espacement horizontal entre le nom et l'IBAN

                // Créer le bouton radio pour le nom
                RadioButton radioButton = new RadioButton(name);
                radioButton.setToggleGroup(group);
                radioButton.setUserData(iban); // Associer l'IBAN en tant que donnée utilisateur
                // Créer un Label pour afficher l'IBAN
                Label ibanLabel = new Label(iban);
                // Ajouter le bouton radio et le label au HBox
                beneficiaryRow.getChildren().addAll(radioButton, ibanLabel);
                // Ajouter la ligne au VBox principal
                beneficiaryContainer.getChildren().add(beneficiaryRow);
            }
            // Ajouter le VBox au conteneur parent (AnchorPane ou autre)
            anchor.getChildren().add(beneficiaryContainer);
        }
    }

    @FXML
    public void handleSearch(ActionEvent event){
        String iban=searchField.getText().trim();
        AccountDAOImpl accountDAO = new AccountDAOImpl();
        UserDAOImpl userDAO = new UserDAOImpl();
        String userId=accountDAO.findUserIdByAccountNumber(iban);
        if(userId != null) {
            User beneficiary = userDAO.getUser(userId);
            SessionManager.getInstance().setSelectedBeneficiary(beneficiary);
            SessionManager.getInstance().setSelectedAccountBeneficiary(accountDAO.getAccount(iban));
            Account selectedAccount=SessionManager.getInstance().getSelectedAccount();
            boolean success= accountDAO.addBeneficiaryAccount(selectedAccount.getAccountNumber(),iban);
            Accueil accueilController = (Accueil) SessionManager.getInstance().getController("Accueil");
            accueilController.passerVirement();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Utilisateur introuvable");
            alert.setHeaderText(null);
            alert.setContentText("Cet IBAN ne correspond à aucun utilisateur");
            alert.showAndWait();
        }
    }
    @FXML
    public void selectBeneficiary() {
        RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();

        if (selectedRadioButton != null) {
            // Récupérer la clé (nom du bénéficiaire)
            String beneficiaryName = selectedRadioButton.getText();

            // Récupérer la valeur (IBAN) depuis UserData
            String beneficiaryIBAN = (String) selectedRadioButton.getUserData();
            UserDAOImpl userDAO = new UserDAOImpl();
            AccountDAOImpl accountDAO = new AccountDAOImpl();
            String beneficiaryId=accountDAO.findUserIdByAccountNumber(beneficiaryIBAN);
            User SelectedBeneficiary = userDAO.getUser(beneficiaryId);
            SessionManager.getInstance().setSelectedBeneficiary(SelectedBeneficiary);
            Account beneficiaryAccount=accountDAO.getAccount(beneficiaryIBAN);
            SessionManager.getInstance().setSelectedAccountBeneficiary(beneficiaryAccount);
            Accueil accueilController = (Accueil) SessionManager.getInstance().getController("Accueil");
            accueilController.passerVirement();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sélection requise");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un bénéficiare  avant de continuer.");
            alert.showAndWait();
        }
    }
}
