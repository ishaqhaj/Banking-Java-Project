package com.Application.controller;

import com.Application.model.User;
import com.Application.model.Virement;
import com.Application.service.VirementService;
import com.Application.util.SessionManager;
import com.Application.util.VirementXMLGenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.*;

public class VirementMasse {
    private List<Virement> virements;
    @FXML
    private Text paneTitle;
    @FXML
    private TextField beneficiaireField;
    @FXML
    private TextField amountField;
    @FXML
    private ComboBox<String> currencyBox;
    @FXML
    private TextArea motifField;
    @FXML
    private ComboBox<String> paymentMethodBox;
    @FXML
    private DatePicker datePicker;
    
    private final static String type="masse";
    Accueil accueilController;
    
    private static final Logger LOGGER = Logger.getLogger(VirementMasse.class.getName());
    
    @FXML
    private void initialize() {
        this.virements=SessionManager.getInstance().getVirements();
        paneTitle.setText("Informations sur le virement n°"+(this.virements.size()+1));
        currencyBox.getItems().addAll("MAD", "EUR", "USD");
        paymentMethodBox.getItems().addAll("SEPA", "PRPT", "SDVA");
        User beneficiary= SessionManager.getInstance().getSelectedBeneficiary();
        beneficiaireField.setText(beneficiary.getName());
        accueilController = (Accueil) SessionManager.getInstance().getController("Accueil");
    }
    @FXML
    public void addBeneficiary(ActionEvent event){
        virements=SessionManager.getInstance().getVirements();
        if(virements.size()<5000) {
            String amountText = amountField.getText();
            String currency = currencyBox.getValue();
            String motif = motifField.getText(); // Champ facultatif
            String payMethod = paymentMethodBox.getValue();
            LocalDate dateValue = datePicker.getValue();
            if (amountText.isEmpty() || currency == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de Validation");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs obligatoires marqués par *.");
                alert.showAndWait();
            } else {
                BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(amountText));
                // Créer un objet Virement
                if(dateValue != null) {
                    String date=dateValue.toString();
                    Virement virement = new Virement(date, amount, currency, motif, type, payMethod);
                    virements.add(virement);
                }
                else{
                    Virement virement = new Virement(amount, currency, motif, type, payMethod);
                    virements.add(virement);
                }
                SessionManager.getInstance().setVirements(virements);
            }
        }
        if(virements.size()==5000){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nombre maximum de bénéficiaires");
            alert.setHeaderText(null);
            alert.setContentText("Vous avez atteint le nombre maximum de bénéficiaires pour un virement de masse !");
            alert.showAndWait();
        }
        else{
            accueilController.navigateAfterAccountSelectionMasse();
        }
    }
    @FXML
    public void validerVirement(ActionEvent event){
        if(virements.size()<5000) {
            String amountText = amountField.getText();
            String currency = currencyBox.getValue();
            String motif = motifField.getText(); // Champ facultatif
            String payMethod = paymentMethodBox.getValue();
            LocalDate dateValue = datePicker.getValue();
            if (amountText.isEmpty() || currency == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de Validation");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs obligatoires marqués par *.");
                alert.showAndWait();
            } else {
                BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(amountText));
                // Créer un objet Virement
                if(dateValue != null){
                    String date=dateValue.toString();
                    Virement virement = new Virement(date, amount, currency, motif,type , payMethod);
                    virements.add(virement);
                }
                else{
                    Virement virement = new Virement(amount, currency, motif, type, payMethod);
                    virements.add(virement);
                }
                VirementService virementService = new VirementService();
                virementService.insertVirements(virements);

                // Afficher une alerte de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Virement passé avec succès !");
                successAlert.showAndWait();
                try {
                    VirementXMLGenerator.generateXMLVirementMasse(virements);
                } catch (Exception ex) {
                	LOGGER.log(Level.SEVERE, "Erreur lors de la génération du xml: " , ex);
                }
                SessionManager.getInstance().setVirements(new ArrayList<>());
                virements = SessionManager.getInstance().getVirements();
                accueilController.loadHistory();
            }
        }
        else{
            VirementService virementService = new VirementService();
            virementService.insertVirements(virements);
            // Afficher une alerte de succès
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Virement passé avec succès !");
            successAlert.showAndWait();
            try {
                VirementXMLGenerator.generateXMLVirementMultiple(virements);
            } catch (Exception ex) {
            	LOGGER.log(Level.SEVERE, "Erreur lors de la génération du xml: " , ex);
            }
            SessionManager.getInstance().setVirements(new ArrayList<>());
            virements = SessionManager.getInstance().getVirements();
            accueilController.loadHistory();
        }

    }

}
