package com.Application.model.iso20022;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class FinancialInstitution {

    @XmlElement(name = "BIC")
    private String bic;

    // Constructeur par défaut (obligatoire pour JAXB)
    public FinancialInstitution() {
    }

    public FinancialInstitution(String bic) {
        this.bic = bic;
    }

    // Getter et setter pour BIC
    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }
}
