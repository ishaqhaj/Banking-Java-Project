package com.Application.model.iso20022;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

import java.math.BigDecimal;
@XmlAccessorType(XmlAccessType.FIELD)
public class Amount {

    @XmlAttribute(name = "Currency")
    private String currency;

    @XmlValue
    private String value;

    // Getters and setters
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

