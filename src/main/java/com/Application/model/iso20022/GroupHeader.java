package com.Application.model.iso20022;

import jakarta.xml.bind.annotation.*;

import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
public class GroupHeader {

    @XmlElement(name = "MessageId")
    private String messageId;

    @XmlElement(name = "CreationDateTime")
    private String creationDateTime;

    @XmlElement(name = "NumberOfTransactions")
    private String numberOfTransactions;

    @XmlElement(name = "ControlSum")
    private BigDecimal controlSum;

    @XmlElement(name = "InitgPty") // Élément XML pour InitiatingParty
    private PartyIdentification initiatingParty;

    // Getters and setters
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(String numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    public BigDecimal getControlSum() {
        return controlSum;
    }

    public void setControlSum(BigDecimal controlSum) {
        this.controlSum = controlSum;
    }

    public PartyIdentification getInitiatingParty() {
        return initiatingParty;
    }

    public void setInitiatingParty(PartyIdentification initiatingParty) {
        this.initiatingParty = initiatingParty;
    }
}
