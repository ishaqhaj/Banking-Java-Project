package com.Application.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import com.Application.dao.impl.AccountDAOImpl;
import com.Application.dao.impl.UserDAOImpl;
import com.Application.model.User;
import com.Application.model.Virement;
import com.Application.model.iso20022.*;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.List;

public class VirementXMLGenerator {
	private static final String DATETIMEPATTERN="yyyy-MM-dd'T'HH:mm:ss";
	private static final String TIMESTAMPPATTERN="yyyyMMdd_HHmmss";
	private static final String AMOUNTPATTERN="#0.00";
	private static final String XMLIDENTIFICATOR="urn:iso:std:iso:20022:tech:xsd:pain.001.001.03";
	
	private static final Logger LOGGER = Logger.getLogger(VirementXMLGenerator.class.getName());
	
	private VirementXMLGenerator() {
        throw new UnsupportedOperationException("Cette classe ne peut pas être instanciée.");
    }
	
    public static void generateXMLVirementSimple(Virement virement) throws JAXBException {
        // Créer le message principal
        CstmrCdtTrfInitn cstmrCdtTrfInitn = new CstmrCdtTrfInitn();

        // Configurer les en-têtes de groupe
        GroupHeader groupHeader = new GroupHeader();
        groupHeader.setMessageId(virement.getEndToEndId());
        groupHeader.setCreationDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATETIMEPATTERN)));
        groupHeader.setNumberOfTransactions("1");
        groupHeader.setControlSum(virement.getAmount()); // Format avec 2 décimales

        // Ajouter l'initiateur
        PartyIdentification initiatingParty = new PartyIdentification();
        User authenticatedUser = SessionManager.getInstance().getAuthenticatedUser();
        initiatingParty.setName(authenticatedUser.getName());
        groupHeader.setInitiatingParty(initiatingParty);

        cstmrCdtTrfInitn.setGroupHeader(groupHeader);

        // Configurer les informations de paiement
        PaymentInformation paymentInformation = new PaymentInformation();
        paymentInformation.setPaymentInformationId(generatePaymentInformationId(authenticatedUser.getUserId()));
        paymentInformation.setPaymentMethod("TRF");
        paymentInformation.setBatchBooking(false);
        paymentInformation.setRequestedExecutionDate(virement.getTimestamp());

        // Débiteur
        PartyIdentification debtor = new PartyIdentification();
        debtor.setName(authenticatedUser.getName());
        paymentInformation.setDebtor(debtor);

        CashAccount debtorAccount = new CashAccount();
        debtorAccount.setId(new AccountId(virement.getDebtorAccount().getAccountNumber()));
        paymentInformation.setDebtorAccount(debtorAccount);

        FinancialInstitution debtorAgent = new FinancialInstitution();
        debtorAgent.setBic(virement.getDebtorAccount().getBank().getBic());
        paymentInformation.setDebtorAgent(debtorAgent);

        // Créancier
        PartyIdentification creditor = new PartyIdentification();
        User beneficiary = SessionManager.getInstance().getSelectedBeneficiary();
        creditor.setName(beneficiary.getName());
        paymentInformation.setCreditor(creditor);

        CashAccount creditorAccount = new CashAccount();
        creditorAccount.setId(new AccountId(virement.getCreditorAccount().getAccountNumber()));
        paymentInformation.setCreditorAccount(creditorAccount);

        FinancialInstitution creditorAgent = new FinancialInstitution();
        creditorAgent.setBic(virement.getCreditorAccount().getBank().getBic());
        paymentInformation.setCreditorAgent(creditorAgent);

        // Montant
        Amount amount = new Amount();
        amount.setCurrency(virement.getCurrency());
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat(AMOUNTPATTERN, symbols);
        String formattedAmount = decimalFormat.format(virement.getAmount());

        amount.setValue(formattedAmount);
        paymentInformation.setInstructedAmount(amount);

        // Informations de paiement
        paymentInformation.setInstructionId(generateInstructionId(authenticatedUser.getUserId()));
        paymentInformation.setEndToEndId(virement.getEndToEndId());
        paymentInformation.setChargeBearer("SLEV");

        // Motif de paiement
        RemittanceInformation remittanceInformation = new RemittanceInformation();
        remittanceInformation.setUnstructured(virement.getMotif());
        paymentInformation.setRemittanceInformation(remittanceInformation);

        cstmrCdtTrfInitn.addPaymentInformation(paymentInformation);

        // Créer le document avec namespace
        Document document = new Document();
        document.setCstmrCdtTrfInitn(cstmrCdtTrfInitn);
        document.setNamespace(XMLIDENTIFICATOR);

        // Générer le XML
        JAXBContext context = JAXBContext.newInstance(Document.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Générer un timestamp pour le fichier
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss"));

        // Chemin où sauvegarder le fichier XML
        String outputDirectory = "src/main/java/com/Application/Transactions_XML/VirementSimple/";
        String fileName = "VirementSimple_" + virement.getEndToEndId() + "_" + timestamp + ".xml";

        // Assurez-vous que le répertoire existe
        File directory = new File(outputDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Créez le fichier et sauvegardez-y le XML
        File outputFile = new File(outputDirectory + fileName);
        marshaller.marshal(document, outputFile);
        
        LOGGER.info("Fichier XML généré avec succès : " + outputFile.getAbsolutePath());
    }

    public static void generateXMLVirementMultiple(List<Virement> virements) throws Exception {
        if (virements == null || virements.isEmpty()) {
            throw new IllegalArgumentException("La liste des virements ne doit pas être vide.");
        }
        AccountDAOImpl accountDAO=new AccountDAOImpl();
        UserDAOImpl userDAO=new UserDAOImpl();
        // Créer le message principal ISO 20022
        CstmrCdtTrfInitn cstmrCdtTrfInitn = new CstmrCdtTrfInitn();

        // Configurer les en-têtes de groupe
        GroupHeader groupHeader = new GroupHeader();
        groupHeader.setMessageId(generateMessageId());
        groupHeader.setCreationDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATETIMEPATTERN)));
        groupHeader.setNumberOfTransactions(String.valueOf(virements.size()));

        // Calculer le total des montants pour ControlSum
        BigDecimal controlSum = virements.stream()
                .map(Virement::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        groupHeader.setControlSum(controlSum.setScale(2, RoundingMode.HALF_UP));

        // Récupérer l'utilisateur initiateur
        User authenticatedUser = SessionManager.getInstance().getAuthenticatedUser();
        PartyIdentification initiatingParty = new PartyIdentification();
        initiatingParty.setName(authenticatedUser.getName());
        groupHeader.setInitiatingParty(initiatingParty);

        cstmrCdtTrfInitn.setGroupHeader(groupHeader);

        // Parcourir chaque virement et l'ajouter dans le message
        for (Virement virement : virements) {
            PaymentInformation paymentInformation = new PaymentInformation();
            paymentInformation.setPaymentInformationId(generatePaymentInformationId(authenticatedUser.getUserId()));
            paymentInformation.setPaymentMethod("TRF");
            paymentInformation.setRequestedExecutionDate(virement.getTimestamp());
            // Débiteur
            PartyIdentification debtor = new PartyIdentification();
            debtor.setName(authenticatedUser.getName());
            paymentInformation.setDebtor(debtor);

            // Compte du débiteur
            CashAccount debtorAccount = new CashAccount();
            debtorAccount.setId(new AccountId(virement.getDebtorAccount().getAccountNumber()));
            paymentInformation.setDebtorAccount(debtorAccount);

            // Créancier
            PartyIdentification creditor = new PartyIdentification();
            String beneficiaryId=accountDAO.findUserIdByAccountNumber(virement.getCreditorAccount().getAccountNumber());
            User beneficiary = userDAO.getUser(beneficiaryId);
            creditor.setName(beneficiary.getName());
            paymentInformation.setCreditor(creditor);

            // Compte du créancier
            CashAccount creditorAccount = new CashAccount();
            creditorAccount.setId(new AccountId(virement.getCreditorAccount().getAccountNumber()));
            paymentInformation.setCreditorAccount(creditorAccount);

            // Montant
            Amount amount = new Amount();
            amount.setCurrency(virement.getCurrency());
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            DecimalFormat decimalFormat = new DecimalFormat(AMOUNTPATTERN, symbols);
            String formattedAmount = decimalFormat.format(virement.getAmount());

            amount.setValue(formattedAmount);
            paymentInformation.setInstructedAmount(amount);

            // Instruction ID
            paymentInformation.setInstructionId(generateInstructionId(authenticatedUser.getUserId()));
            paymentInformation.setEndToEndId(virement.getEndToEndId());
            paymentInformation.setChargeBearer("SLEV");

            // Motif
            RemittanceInformation remittanceInformation = new RemittanceInformation();
            remittanceInformation.setUnstructured(virement.getMotif());
            paymentInformation.setRemittanceInformation(remittanceInformation);

            // Ajouter le paiement dans le message principal
            cstmrCdtTrfInitn.addPaymentInformation(paymentInformation);
        }

        // Créer le document avec namespace
        Document document = new Document();
        document.setCstmrCdtTrfInitn(cstmrCdtTrfInitn);
        document.setNamespace(XMLIDENTIFICATOR);

        // Générer le XML
        JAXBContext context = JAXBContext.newInstance(Document.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Chemin pour sauvegarder le fichier XML
        String outputDirectory = "src/main/java/com/Application/Transactions_XML/VirementMultiple/";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss"));
        String fileName = "VirementMultiple_" + timestamp + ".xml";

        // Assurez-vous que le répertoire existe
        File directory = new File(outputDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Créer le fichier et sauvegarder le XML
        File outputFile = new File(outputDirectory + fileName);
        marshaller.marshal(document, outputFile);

        System.out.println("Fichier XML pour virement multiple généré avec succès : " + outputFile.getAbsolutePath());
    }

    public static void generateXMLVirementMasse(List<Virement> virements) throws Exception {
        if (virements == null || virements.isEmpty()) {
            throw new IllegalArgumentException("La liste des virements ne doit pas être vide.");
        }
        AccountDAOImpl accountDAO=new AccountDAOImpl();
        UserDAOImpl userDAO=new UserDAOImpl();
        // Créer le message principal ISO 20022
        CstmrCdtTrfInitn cstmrCdtTrfInitn = new CstmrCdtTrfInitn();

        // Configurer les en-têtes de groupe
        GroupHeader groupHeader = new GroupHeader();
        groupHeader.setMessageId(generateMessageId());
        groupHeader.setCreationDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATETIMEPATTERN)));
        groupHeader.setNumberOfTransactions(String.valueOf(virements.size()));

        // Calculer le total des montants pour ControlSum
        BigDecimal controlSum = virements.stream()
                .map(Virement::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        groupHeader.setControlSum(controlSum.setScale(2, RoundingMode.HALF_UP));

        // Récupérer l'utilisateur initiateur
        User authenticatedUser = SessionManager.getInstance().getAuthenticatedUser();
        PartyIdentification initiatingParty = new PartyIdentification();
        initiatingParty.setName(authenticatedUser.getName());
        groupHeader.setInitiatingParty(initiatingParty);

        cstmrCdtTrfInitn.setGroupHeader(groupHeader);

        // Parcourir chaque virement et l'ajouter dans le message
        for (Virement virement : virements) {
            PaymentInformation paymentInformation = new PaymentInformation();
            paymentInformation.setPaymentInformationId(generatePaymentInformationId(authenticatedUser.getUserId()));
            paymentInformation.setPaymentMethod("TRF");
            paymentInformation.setRequestedExecutionDate(virement.getTimestamp());
            // Débiteur
            PartyIdentification debtor = new PartyIdentification();
            debtor.setName(authenticatedUser.getName());
            paymentInformation.setDebtor(debtor);

            // Compte du débiteur
            CashAccount debtorAccount = new CashAccount();
            debtorAccount.setId(new AccountId(virement.getDebtorAccount().getAccountNumber()));
            paymentInformation.setDebtorAccount(debtorAccount);

            // Créancier
            PartyIdentification creditor = new PartyIdentification();
            String beneficiaryId=accountDAO.findUserIdByAccountNumber(virement.getCreditorAccount().getAccountNumber());
            User beneficiary = userDAO.getUser(beneficiaryId);
            creditor.setName(beneficiary.getName());
            paymentInformation.setCreditor(creditor);

            // Compte du créancier
            CashAccount creditorAccount = new CashAccount();
            creditorAccount.setId(new AccountId(virement.getCreditorAccount().getAccountNumber()));
            paymentInformation.setCreditorAccount(creditorAccount);

            // Montant
            Amount amount = new Amount();
            amount.setCurrency(virement.getCurrency());
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            DecimalFormat decimalFormat = new DecimalFormat(AMOUNTPATTERN, symbols);
            String formattedAmount = decimalFormat.format(virement.getAmount());

            amount.setValue(formattedAmount);
            paymentInformation.setInstructedAmount(amount);

            // Instruction ID
            paymentInformation.setInstructionId(generateInstructionId(authenticatedUser.getUserId()));
            paymentInformation.setEndToEndId(virement.getEndToEndId());
            paymentInformation.setChargeBearer("SLEV");

            // Motif
            RemittanceInformation remittanceInformation = new RemittanceInformation();
            remittanceInformation.setUnstructured(virement.getMotif());
            paymentInformation.setRemittanceInformation(remittanceInformation);

            // Ajouter le paiement dans le message principal
            cstmrCdtTrfInitn.addPaymentInformation(paymentInformation);
        }

        // Créer le document avec namespace
        Document document = new Document();
        document.setCstmrCdtTrfInitn(cstmrCdtTrfInitn);
        document.setNamespace(XMLIDENTIFICATOR);

        // Générer le XML
        JAXBContext context = JAXBContext.newInstance(Document.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Chemin pour sauvegarder le fichier XML
        String outputDirectory = "src/main/java/com/Application/Transactions_XML/VirementDeMasse/";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss"));
        String fileName = "VirementDeMasse_" + timestamp + ".xml";

        // Assurez-vous que le répertoire existe
        File directory = new File(outputDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Créer le fichier et sauvegarder le XML
        File outputFile = new File(outputDirectory + fileName);
        marshaller.marshal(document, outputFile);

        System.out.println("Fichier XML pour le virement de masse est généré avec succès : " + outputFile.getAbsolutePath());
    }

    private static String generateMessageId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIMESTAMPPATTERN));
        String uniquePart = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return "MSG_" + timestamp + "_" + uniquePart;
    }

    public static String generatePaymentInformationId(String userId) {
        // Obtenir l'heure actuelle au format ISO 8601 compact (année, mois, jour, heure, minute, seconde)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIMESTAMPPATTERN));

        // Générer un UUID aléatoire pour assurer l'unicité
        String uniquePart = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        // Construire l'identifiant avec ou sans l'identifiant utilisateur
        if (userId != null && !userId.isEmpty()) {
            return "PAY_" + userId.toUpperCase() + "_" + timestamp + "_" + uniquePart;
        } else {
            return "PAY_" + timestamp + "_" + uniquePart;
        }
    }
    public static String generateInstructionId(String userId) {
        // Obtenir l'horodatage actuel au format compact (ISO 8601)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIMESTAMPPATTERN));

        // Générer un UUID aléatoire pour assurer l'unicité
        String uniquePart = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        // Construire l'InstructionId avec ou sans identifiant utilisateur
        if (userId != null && !userId.isEmpty()) {
            return "INST_" + userId.toUpperCase() + "_" + timestamp + "_" + uniquePart;
        } else {
            return "INST_" + timestamp + "_" + uniquePart;
        }
    }
}
