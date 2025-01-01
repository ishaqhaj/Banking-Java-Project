package com.Application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import com.Application.dao.impl.AccountDAOImpl;
import com.Application.model.Account;
import com.Application.model.User;
import com.Application.util.SessionManager;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.logging.*;

public class Accueil {
    @Getter
    @Setter

    @FXML
    private BorderPane mainPane; // Champ temporaire pour l'injection FXML
    Accueil accueilController;
    private final static String selectAccountInt="/gui/SelectAccount.fxml";
    private final static String selectBeneficiaryInt="/gui/SelectBeneficiary.fxml";
    
    private static final Logger LOGGER = Logger.getLogger(Accueil.class.getName());

    @FXML
    public void initialize() {
        // Initialisation manuelle de la variable statique
        SessionManager.getInstance().registerController("Accueil", this);
        accueilController = (Accueil) SessionManager.getInstance().getController("Accueil");
        loadPage("/gui/history.fxml");
    }
    @FXML
    public void addAccount() {
        // Implémentation pour ajouter un compte
        loadPage("/gui/AddAccount.fxml");
    }
    @FXML
    public void selectAccount(ActionEvent event) {
        User authenticatedUser = SessionManager.getInstance().getAuthenticatedUser();
        AccountDAOImpl accountDAO = new AccountDAOImpl();
        if(accountDAO.hasSingleAccount(authenticatedUser.getUserId())){
            SessionManager.getInstance().setSelectedAccount(accountDAO.getUserAccount(authenticatedUser.getUserId()));
            addBeneficiary();
        }
        else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(selectAccountInt));
            try {
                Pane selectAccountPane = loader.load();
                SelectAccount selectAccountController = loader.getController();
                // Définir la redirection personnalisée après sélection
                selectAccountController.setOnAccountSelected(selectedAccount -> 
                    // Rediriger vers l'ajout de bénéficiaire
                    addBeneficiary()
                );
                // Charger le contenu dans la fenêtre principale
                accueilController.getMainPane().setCenter(selectAccountPane);

            } catch (IOException e) {
            	LOGGER.log(Level.SEVERE, "Erreur lors du traitement", e);
            }

        }
    }
    public void addBeneficiary() {
        loadPage("/gui/Addbeneficiary.fxml");
    }
    @FXML
    public void virementSimple() {
        AccountDAOImpl accountDAO = new AccountDAOImpl();
        User authenticatedUser = SessionManager.getInstance().getAuthenticatedUser();

        if (!accountDAO.hasSingleAccount(authenticatedUser.getUserId())) {
            loadPage(selectAccountInt);
            return; // Arrêtez ici pour attendre que l'utilisateur sélectionne un compte
        }

        // Si l'utilisateur n'a qu'un seul compte, on le sélectionne automatiquement
        Account userAccount = accountDAO.getUserAccount(authenticatedUser.getUserId());
        SessionManager.getInstance().setSelectedAccount(userAccount);

        navigateAfterAccountSelectionSimple();
    }

    @FXML
    public void virementDeMasse(ActionEvent event) {
        AccountDAOImpl accountDAO = new AccountDAOImpl();
        User authenticatedUser = SessionManager.getInstance().getAuthenticatedUser();

        if (!accountDAO.hasSingleAccount(authenticatedUser.getUserId())) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(selectAccountInt));
            try {
                Pane selectAccountPane = loader.load();
                SelectAccount selectAccountController = loader.getController();
                // Définir la redirection personnalisée après sélection
                selectAccountController.setOnAccountSelected(selectedAccount -> 
                    // Rediriger vers l'ajout de bénéficiaire
                    navigateAfterAccountSelectionMasse()
                );
                // Charger le contenu dans la fenêtre principale
                accueilController.getMainPane().setCenter(selectAccountPane);
                

            } catch (IOException e) {
            	LOGGER.log(Level.SEVERE, "Erreur lors du traitement du virement de masse.", e);
            }
        }
        // Si l'utilisateur n'a qu'un seul compte, on le sélectionne automatiquement
        else {
            Account userAccount = accountDAO.getUserAccount(authenticatedUser.getUserId());
            SessionManager.getInstance().setSelectedAccount(userAccount);
            navigateAfterAccountSelectionMasse();
        }
    }

    @FXML
    public void virementMultiple(ActionEvent event){
        AccountDAOImpl accountDAO = new AccountDAOImpl();
        User authenticatedUser = SessionManager.getInstance().getAuthenticatedUser();

        if (!accountDAO.hasSingleAccount(authenticatedUser.getUserId())) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(selectAccountInt));
            try {
                Pane selectAccountPane = loader.load();
                SelectAccount selectAccountController = loader.getController();
                // Définir la redirection personnalisée après sélection
                selectAccountController.setOnAccountSelected(selectedAccount -> 
                    // Rediriger vers l'ajout de bénéficiaire
                    navigateAfterAccountSelectionMultiple()
                );
                // Charger le contenu dans la fenêtre principale
                accueilController.getMainPane().setCenter(selectAccountPane);
                
            } catch (IOException e) {
            	LOGGER.log(Level.SEVERE, "Erreur lors du traitement du virement multiple.", e);
            }
            return; // Arrêtez ici pour attendre que l'utilisateur sélectionne un compte
        }

        // Si l'utilisateur n'a qu'un seul compte, on le sélectionne automatiquement
        Account userAccount = accountDAO.getUserAccount(authenticatedUser.getUserId());
        SessionManager.getInstance().setSelectedAccount(userAccount);

        navigateAfterAccountSelectionMultiple();
    }

    public void navigateAfterAccountSelectionSimple() {
        loadPage(selectBeneficiaryInt);
    }
    public void navigateAfterAccountSelectionMultiple() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(selectBeneficiaryInt));
        try {
            Pane selectBeneficiaryPane = loader.load();
            SelectBeneficiary selectBeneficiaryController = loader.getController();
            // Définir la redirection personnalisée après sélection
            selectBeneficiaryController.setRedirection(selectedBeneficiary ->
                // Rediriger vers l'ajout de bénéficiaire
                passerVirementMultiple()
            );
            // Charger le contenu dans la fenêtre principale
            accueilController.getMainPane().setCenter(selectBeneficiaryPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void navigateAfterAccountSelectionMasse(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(selectBeneficiaryInt));
        try {
            Pane selectBeneficiaryPane = loader.load();
            SelectBeneficiary selectBeneficiaryController = loader.getController();
            // Définir la redirection personnalisée après sélection
            selectBeneficiaryController.setRedirection(selectedBeneficiary ->
                // Rediriger vers l'ajout de bénéficiaire
                passerVirementMasse()
            );
            // Charger le contenu dans la fenêtre principale
            accueilController.getMainPane().setCenter(selectBeneficiaryPane);
            
        } catch (IOException e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors du traitement du virement de masse.", e);
        }
    }
    public void passerVirementSimple(){
        loadPage("/gui/VirementSimple.fxml");
    }
    public void passerVirementMultiple(){
        loadPage("/gui/VirementMultiple.fxml");
    }

    public void passerVirementMasse(){
        loadPage("/gui/VirementMasse.fxml");
    }

    public void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Pane newContent = loader.load();
            // Remplacer uniquement le contenu central
            if (accueilController.getMainPane() != null) {
            	accueilController.getMainPane().setCenter(newContent);
           } else {
               LOGGER.severe("Erreur : mainPane est null. Vérifiez l'initialisation.");
           }

        } catch (IOException e) {
        	LOGGER.log(Level.SEVERE, "Erreur lors du chargement de la page : " + fxmlFile, e);
        }
    }

    public void loadHistory(){
        loadPage("/gui/history.fxml");
    }
    
	public BorderPane getMainPane() {
		return mainPane;
	}
	
    
    
}
