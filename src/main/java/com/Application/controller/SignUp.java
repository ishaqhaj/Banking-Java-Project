package main.java.com.Application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.com.Application.service.EmailService;
import main.java.com.Application.service.PDFGenerator;
import main.java.com.Application.service.UserService;

import java.math.BigDecimal;

public class SignUp {
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmedField;
    @FXML
    private TextField idField;
    @FXML
    private TextField adresseField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField codePostalField;
    @FXML
    private TextField countryField;
    @FXML
    private ComboBox<String> accountTypeComboBox;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField bankField;
    @FXML
    private TextField bicField;
    @FXML
    private void initialize() {
        // Ajouter les éléments au ComboBox accountTypeComboBox
        accountTypeComboBox.getItems().addAll(
                "CACC : Compte courant",
                "SVGS : Compte d'épargne",
                "CASH : Compte en espèces",
                "LOAN : Compte de prêt",
                "MGLD : Compte de métaux précieux"
        );
    }
    public void createUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmedField.getText();
        String idValue = idField.getText();
        String address = adresseField.getText();
        String city = cityField.getText();
        String postalCode = codePostalField.getText();
        String country = countryField.getText();
        String accountNumber = ibanField.getText();
        String selectedAccountType = accountTypeComboBox.getValue();
        if(selectedAccountType!=null)
            selectedAccountType = selectedAccountType.substring(0, 4);
        String bank = bankField.getText();
        String bic = bicField.getText();
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || idValue.isEmpty() || accountNumber.isEmpty() || bank.isEmpty()||bic.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }
        String nameValidationResult = isValidName(name);
        if (nameValidationResult != null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", nameValidationResult);
            return;
        }
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Adresse email invalide.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Les mots de passe ne correspondent pas.");
            return;
        }
        if (!isValidIBAN(accountNumber)) {
            return; // Arrêter si l'IBAN est invalide
        }
        try {
            final UserService userService = new UserService();
            String userId = userService.createUser(idValue, name, password, email, address, city, postalCode, country, accountNumber, selectedAccountType, bank, bic);

            if (!userId.equals("Error")) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte crée avec succès.Veuillez vérifier votre boîte email pour obtenir votre identifiant");
                String pdfPath = PDFGenerator.generateUserPdf(name, email, idValue, address, city, postalCode, country, accountNumber, selectedAccountType, bank, bic);
                // Envoyer l'email avec le PDF en pièce jointe
                EmailService emailService = new EmailService();
                emailService.sendEmail(email, userId, pdfPath);
                // Fermer la fenêtre après succès
                nameField.getScene().getWindow().hide(); // Ferme la fenêtre actuelle
            }
            else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Cet identifiant (CIN/Passeport) existe déjà. Veuillez réessayer avec un identifiant différent.");
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    private String isValidName(String name) {
        if (name.length() > 140) {
            return "Le nom ne peut pas dépasser 140 caractères.";
        }
        if (!name.matches("[A-Za-z0-9 .,'()\\-]*")) {
            return "Le nom contient des caractères non autorisés.";
        }
        return null; // Le nom est valide
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private boolean isValidIBAN(String iban) {
        // Supprimer les espaces et vérifier la longueur minimale et maximale (ISO 13616: de 15 à 34 caractères)
        String sanitizedIban = iban.replaceAll("\\s+", "");
        if (sanitizedIban.length() < 15 || sanitizedIban.length() > 34) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'IBAN doit contenir entre 15 et 34 caractères.");
            return false;
        }

        // Vérifier que l'IBAN commence par deux lettres (le code pays) suivies de chiffres
        if (!sanitizedIban.matches("^[A-Z]{2}\\d{2}[A-Z0-9]+$")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'IBAN doit commencer par un code pays valide (ex: FR, DE).");
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
                showAlert(Alert.AlertType.ERROR, "Erreur", "L'IBAN est invalide");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'IBAN contient des caractères non valides.");
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
