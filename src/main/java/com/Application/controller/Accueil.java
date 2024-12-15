package com.Application.controller;

import com.Application.dao.UserDAO;
import com.Application.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import com.Application.dao.impl.AccountDAOImpl;
import com.Application.dao.impl.UserDAOImpl;
import com.Application.model.Account;
import com.Application.model.User;
import com.Application.util.SessionManager;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Map;

public class Accueil {
    @Getter
    @Setter
    private static BorderPane statiqueMainApp; // Champ statique

    @FXML
    private BorderPane mainPane; // Champ temporaire pour l'injection FXML

    @FXML
    public void initialize() {
        // Initialisation manuelle de la variable statique
        statiqueMainApp = mainPane;
        SessionManager.getInstance().registerController("Accueil", this);
    }
    @FXML
    public void addAccount() {
        // Implémentation pour ajouter un compte
        loadPage("/GUI/AddAccount.fxml");
    }
    @FXML
    public void selectAccount(ActionEvent event) {
        UserService userService = new UserService();
        User authenticatedUser = SessionManager.getInstance().getAuthenticatedUser();
        AccountDAOImpl accountDAO = new AccountDAOImpl();
        UserDAO userDAO = new UserDAOImpl();
        if(accountDAO.hasSingleAccount(authenticatedUser.getUserId())){
            SessionManager.getInstance().setSelectedAccount(accountDAO.getUserAccount(authenticatedUser.getUserId()));
            addBeneficiary();
        }
        else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/SelectAccount.fxml"));
            try {
                Pane selectAccountPane = loader.load();
                SelectAccount selectAccountController = loader.getController();
                // Définir la redirection personnalisée après sélection
                selectAccountController.setOnAccountSelected(selectedAccount -> {
                    // Rediriger vers l'ajout de bénéficiaire
                    addBeneficiary();
                });
                // Charger le contenu dans la fenêtre principale
                statiqueMainApp.setCenter(selectAccountPane);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public void addBeneficiary() {
        loadPage("/GUI/Addbeneficiary.fxml");
    }
    @FXML
    public void virementSimple() {
        UserDAOImpl userDAO = new UserDAOImpl();
        AccountDAOImpl accountDAO = new AccountDAOImpl();
        User authenticatedUser = SessionManager.getInstance().getAuthenticatedUser();

        if (!accountDAO.hasSingleAccount(authenticatedUser.getUserId())) {
            loadPage("/GUI/SelectAccount.fxml");
            return; // Arrêtez ici pour attendre que l'utilisateur sélectionne un compte
        }

        // Si l'utilisateur n'a qu'un seul compte, on le sélectionne automatiquement
        Account userAccount = accountDAO.getUserAccount(authenticatedUser.getUserId());
        SessionManager.getInstance().setSelectedAccount(userAccount);

        navigateAfterAccountSelection();
    }
    public void navigateAfterAccountSelection() {
        loadPage("/GUI/SelectBeneficiary.fxml");
    }

    public void passerVirement(){
        loadPage("/GUI/VirementSimple.fxml");
    }
    public void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Pane newContent = loader.load();

            // Remplacer uniquement le contenu central
            if (statiqueMainApp != null) {
                statiqueMainApp.setCenter(newContent);
           } else {
                System.err.println("Erreur : mainPane est null. Vérifiez l'initialisation.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
