package com.Application.model.iso20022;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class PartyIdentification {

    @XmlElement(name = "Nm") // Élément XML pour le nom
    private String name;

    // Getter et setter pour le nom
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
