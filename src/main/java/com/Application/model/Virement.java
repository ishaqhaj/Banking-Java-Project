package com.Application.model;

import com.Application.dao.impl.VirementDAOImpl;
import com.Application.util.SessionManager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

public class Virement {
    private String endToEndId;
    private Account debtorAccount;
    private Account creditorAccount;
    private BigDecimal amount;
    private String currency;
    private String timestamp;
    private String motif;
    private String type;
    private String payMethod;
    public Virement(BigDecimal amount,String currency,String motif,String type,String payMethod) {
        this.setEndToEndId();
        this.debtorAccount= SessionManager.getInstance().getSelectedAccount();
        this.creditorAccount= SessionManager.getInstance().getSelectedAccountBeneficiary();
        this.amount = amount;
        this.currency=currency;
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        // Convertir LocalDateTime en String
        this.timestamp= currentDateTime.format(formatter);
        this.motif=motif;
        this.payMethod=payMethod;
        this.type=type;
    }
    public Virement(String timestamp,Account creditorAccount,BigDecimal amount,String currency,String motif,String type,String payMethod) {
        this.timestamp=timestamp;
        this.creditorAccount=creditorAccount;
        this.amount=amount;
        this.currency=currency;
        this.motif=motif;
        this.type=type;
        this.payMethod=payMethod;
    }
    public void setEndToEndId(){
        while (true){
            this.endToEndId=generateEndToEndId();
            VirementDAOImpl virementDAO=new VirementDAOImpl();
            if(virementDAO.isEndToEndIdUnique(endToEndId)){
                break;
            }
        }
    }
    public static String generateEndToEndId() {
        // Étape 1 : Obtenir la date au format AAAAMMJJ
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDate = dateFormat.format(new Date());

        // Étape 2 : Générer une séquence alphanumérique aléatoire
        String randomSequence = generateRandomAlphanumeric(10); // 10 caractères pour compléter

        // Étape 3 : Concaténer la date et la séquence
        return currentDate + randomSequence;
    }

    private static String generateRandomAlphanumeric(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }

    public String getFormattedAmount() {
        String currencySymbol = switch (currency) {
            case "USD" -> "$";
            case "EUR" -> "£";
            default -> "DH";
        };
        return String.format("%.2f %s", amount, currencySymbol);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Account getCreditorAccount() {
        return creditorAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public Account getDebtorAccount() {
        return debtorAccount;
    }

    public String getEndToEndId() {
        return endToEndId;
    }

    public String getMotif() {
        return motif;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getPayMethod() {
        return payMethod;
    }
}
