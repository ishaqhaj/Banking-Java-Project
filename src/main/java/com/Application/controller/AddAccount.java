package com.Application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import com.Application.dao.impl.AccountDAOImpl;
import com.Application.dao.impl.BankDAOImpl;
import com.Application.model.Account;
import com.Application.model.Bank;
import com.Application.util.SessionManager;

import java.math.BigDecimal;

public class AddAccount {
    @FXML
    private TextField ibanField;
    @FXML
    private ComboBox<String> accountTypeComboBox;
    @FXML
    private TextField bankField;
    @FXML
    private TextField bicField;
    
    private final static String error="Erreur";
    
    @FXML
    public void initialize() {
        accountTypeComboBox.getItems().addAll(
                "CACC : Compte courant",
                "SVGS : Compte d'épargne",
                "CASH : Compte en espèces",
                "LOAN : Compte de prêt",
                "MGLD : Compte de métaux précieux"
        );
    }
    @FXML
    public void addAccount(){
        String accountNumber = ibanField.getText();
        String accountType = accountTypeComboBox.getValue();
        if (accountType!=null) {
            accountType = accountType.substring(0, 4);
        }
        String bankName = bankField.getText();
        String bic = bicField.getText();
        if (!isValidIBAN(accountNumber)) {
            return; // Arrêter si l'IBAN est invalide
        }
        // Validation des champs obligatoires
        if (accountNumber.isEmpty() || bankName.isEmpty()||bic.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs obligatoires (*)", ButtonType.OK);
            alert.show();
            return;
        }
        BankDAOImpl bankDAO=new BankDAOImpl();
        Bank bank=new Bank(bankName,bic);
        // Création de l'objet Account
        try {
            bank = bankDAO.findBank(bank);
        }
        catch(IllegalArgumentException i){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur - Banque introuvable");
            alert.setHeaderText("Données de la Banque incorrectes");
            alert.setContentText("Les informations fournies pour la banque (Nom ou BIC) ne correspondent à aucune banque connue. Veuillez vérifier vos données.");
            alert.show();
        }

        Account account = new Account(accountNumber,accountType,bank);

        // Récupération de l'utilisateur authentifié
        SessionManager sessionManager = SessionManager.getInstance();
        account.setOwner(sessionManager.getAuthenticatedUser());

        // Insertion du compte
        AccountDAOImpl accountDAO=new AccountDAOImpl();
        accountDAO.insertAccount(account);

        // Confirmation d'ajout
        Alert confirmation = new Alert(Alert.AlertType.INFORMATION, "Compte ajouté avec succès!", ButtonType.OK);
        confirmation.show();
    }
    private boolean isValidIBAN(String iban) {
        // Supprimer les espaces et vérifier la longueur minimale et maximale (ISO 13616: de 15 à 34 caractères)
        String sanitizedIban = iban.replaceAll("\\s+", "");
        if (sanitizedIban.length() < 15 || sanitizedIban.length() > 34) {
            showAlert(Alert.AlertType.ERROR, error, "L'IBAN doit contenir entre 15 et 34 caractères.");
            return false;
        }

        // Vérifier que l'IBAN commence par deux lettres (le code pays) suivies de chiffres
        if (!sanitizedIban.matches("^[A-Z]{2}\\d{2}[A-Z0-9]+$")) {
            showAlert(Alert.AlertType.ERROR, error, "L'IBAN doit commencer par un code pays valide (ex: FR, DE).");
            return false;
        }

        // Réorganiser l'IBAN (déplacer les 4 premiers caractères à la fin)
        String rearrangedIban = sanitizedIban.substring(4) + sanitizedIban.substring(0, 4);

        // Convertir les lettres en chiffres (A=10, B=11, ..., Z=35)
        StringBuilder numericIban = new StringBuilder();
        for (char ch : rearrangedIban.toCharArray()) {
            if (Character.isLetter(ch)) {
                numericIban.append(Character.getNumericValue(ch));
            } else {
                numericIban.append(ch);
            }
        }

        // Vérifier si le reste de la division par 97 est égal à 1
        try {
            BigDecimal ibanValue = new BigDecimal(numericIban.toString());
            if (ibanValue.remainder(BigDecimal.valueOf(97)).intValue() != 1) {
                showAlert(Alert.AlertType.ERROR, error, "L'IBAN est invalide");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, error, "L'IBAN contient des caractères non valides.");
            return false;
        }

        return true; // L'IBAN est valide
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
