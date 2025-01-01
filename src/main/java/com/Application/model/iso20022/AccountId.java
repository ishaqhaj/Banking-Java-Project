package com.Application.model.iso20022;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class AccountId {

    @XmlElement(name = "IBAN")  // Utilisation d'IBAN selon ISO 20022
    private String accountNumber;

    // Un constructeur vide obligatoire pour JAXB
    public AccountId() {}

    public AccountId(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    // Getters et setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
