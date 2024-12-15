package com.Application.model.iso20022;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlRootElement(name = "Document")
public class Document {

    private CstmrCdtTrfInitn cstmrCdtTrfInitn;
    private String xmlns; // Pour stocker le namespace

    @XmlElement(name = "CstmrCdtTrfInitn")
    public CstmrCdtTrfInitn getCstmrCdtTrfInitn() {
        return cstmrCdtTrfInitn;
    }

    public void setCstmrCdtTrfInitn(CstmrCdtTrfInitn cstmrCdtTrfInitn) {
        this.cstmrCdtTrfInitn = cstmrCdtTrfInitn;
    }

    @XmlAttribute(name = "xmlns")
    public String getNamespace() {
        return xmlns;
    }

    public void setNamespace(String namespace) {
        this.xmlns = namespace;
    }
}
