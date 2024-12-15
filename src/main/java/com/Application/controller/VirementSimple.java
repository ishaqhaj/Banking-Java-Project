package com.Application.controller;

import com.Application.model.User;
import com.Application.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import com.Application.model.Virement;
import com.Application.service.VirementService;
import com.Application.util.VirementXMLGenerator;

import java.math.BigDecimal;

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
    @FXML
    private void initialize() {
        currencyBox.getItems().addAll("MAD", "EUR", "USD");
        paymentMethodBox.getItems().addAll("SEPA", "PRPT", "SDVA");
        User Beneficiary= SessionManager.getInstance().getSelectedBeneficiary();
        beneficiaireField.setText(Beneficiary.getName());
    }
    @FXML
    public void validerVirement() {
        String amountText = amountField.getText();
        String currency = currencyBox.getValue();
        String motif = motifField.getText(); // Champ facultatif
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

                // Créer un objet Virement
                Virement virement = new Virement(amount, currency, motif,"simple" ,payMethod);
                VirementService virementService = new VirementService();
                virementService.insertVirement(virement);

                // Afficher une alerte de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Succès");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Virement passé avec succès !");
                successAlert.showAndWait();
                try {
                    VirementXMLGenerator.generateXMLVirementSimple(virement);
                } catch (Exception ex) {
                    System.out.println("Erreur lors de la génération du fxml");
                    throw new RuntimeException(ex);
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le montant doit être un nombre valide.");
                alert.showAndWait();
            }
        }
    }



}
