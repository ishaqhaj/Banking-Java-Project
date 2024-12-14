package com.Application.controller;

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
    public void addBeneficiary() {
        loadPage("/GUI/Addbeneficiary.fxml");
    }
    @FXML
    public void virementSimple() {

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
