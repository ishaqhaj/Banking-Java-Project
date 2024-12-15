package com.Application.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import com.Application.model.User;
import com.Application.model.Virement;
import com.Application.model.iso20022.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class VirementXMLGenerator {

    public static void generateXMLVirementSimple(Virement virement) throws Exception {
        // Créer le message principal
        CstmrCdtTrfInitn cstmrCdtTrfInitn = new CstmrCdtTrfInitn();

        // Configurer les en-têtes de groupe
        GroupHeader groupHeader = new GroupHeader();
        groupHeader.setMessageId(virement.getEndToEndId());
        groupHeader.setCreationDateTime(virement.getTimestamp());
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
        debtorAccount.setId(new AccountId(virement.getDebtorAccount().getAccountNumber(), "IBAN"));
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
        creditorAccount.setId(new AccountId(virement.getCreditorAccount().getAccountNumber(), "IBAN"));
        paymentInformation.setCreditorAccount(creditorAccount);

        FinancialInstitution creditorAgent = new FinancialInstitution();
        creditorAgent.setBic(virement.getCreditorAccount().getBank().getBic());
        paymentInformation.setCreditorAgent(creditorAgent);

        // Montant
        Amount amount = new Amount();
        amount.setCurrency(virement.getCurrency());
        amount.setValue(String.format("%.2f", virement.getAmount()));
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
        document.setNamespace("urn:iso:std:iso:20022:tech:xsd:pain.001.001.03");

        // Générer le XML
        JAXBContext context = JAXBContext.newInstance(Document.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Générer un timestamp pour le fichier
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

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

        System.out.println("Fichier XML généré avec succès : " + outputFile.getAbsolutePath());
    }
    public static String generatePaymentInformationId(String userId) {
        // Obtenir l'heure actuelle au format ISO 8601 compact (année, mois, jour, heure, minute, seconde)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

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
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

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
