package com.Application.model.iso20022;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
public class RemittanceInformation {

    @XmlElement(name = "Ustrd") // Correspond à "Unstructured" dans ISO 20022
    private String unstructured;

    // Getter pour unstructured
    public String getUnstructured() {
        return unstructured;
    }

    // Setter pour unstructured
    public void setUnstructured(String unstructured) {
        this.unstructured = unstructured;
    }
}
