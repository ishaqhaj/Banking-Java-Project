package com.Application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import com.Application.model.User;
import com.Application.model.Virement;
import com.Application.service.VirementService;
import com.Application.util.SessionManager;
import com.Application.util.VirementXMLGenerator;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VirementSimple {
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
    
    private static final Logger LOGGER = Logger.getLogger(VirementSimple.class.getName());
    
    @FXML
    private void initialize() {
        currencyBox.getItems().addAll("MAD", "EUR", "USD");
        paymentMethodBox.getItems().addAll("SEPA", "PRPT", "SDVA");
        User beneficiary= SessionManager.getInstance().getSelectedBeneficiary();
        beneficiaireField.setText(beneficiary.getName());
    }
    @FXML
    public void validerVirement() {
        String amountText = amountField.getText();
        String currency = currencyBox.getValue();
        String motif = motifField.getText(); // Optional field
        String payMethod = paymentMethodBox.getValue();

        if (amountText.isEmpty() || currency == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de Validation");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs obligatoires marqués par *.");
            alert.showAndWait();
        } else {
            try {
                BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(amountText));

                // Create a Virement object
                Virement virement = new Virement(amount, currency, motif, "simple", payMethod);
                VirementService virementService = new VirementService();
                virementService.insertVirement(virement);

                // Generate XML for the virement
                generateVirementXML(virement);

                // Show success alert
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Virement passé avec succès !");
                successAlert.showAndWait();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le montant doit être un nombre valide.");
                alert.showAndWait();
            }
        }
    }

    private void generateVirementXML(Virement virement) {
        try {
            VirementXMLGenerator.generateXMLVirementSimple(virement);
        } catch (Exception ex) {
        	LOGGER.log(Level.SEVERE, "Erreur lors de la génération du xml: " , ex);
        }
    }

    
}
    




