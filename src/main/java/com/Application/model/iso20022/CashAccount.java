package com.Application.model.iso20022;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CashAccount {

    @XmlElement(name = "Id")  // ISO 20022 utilise "Id"
    private AccountId id;

    // Getters et setters
    public AccountId getId() {
        return id;
    }

    public void setId(AccountId id) {
        this.id = id;
    }
}
