package com.Application.model.iso20022;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentInformation {

    @XmlElement(name = "PmtMtd")
    private String paymentMethod;

    @XmlElement(name = "Dbtr")
    private PartyIdentification debtor;

    @XmlElement(name = "DbtrAcct")
    private CashAccount debtorAccount;

    @XmlElement(name = "DbtrAgt")
    private FinancialInstitution debtorAgent;

    @XmlElement(name = "Cdtr")
    private PartyIdentification creditor;

    @XmlElement(name = "CdtrAcct")
    private CashAccount creditorAccount;

    @XmlElement(name = "CdtrAgt")
    private FinancialInstitution creditorAgent;

    @XmlElement(name = "Amt")
    private Amount amount;

    @XmlElement(name = "InstdAmt")  // Nouveau champ InstructedAmount
    private Amount instructedAmount;

    @XmlElement(name = "RemittanceInformation")
    private RemittanceInformation remittanceInformation;

    @XmlElement(name = "PmtInfId")
    private String paymentInformationId;

    @XmlElement(name = "BatchBooking")
    private boolean batchBooking;

    @XmlElement(name = "RequestedExecutionDate")
    private String requestedExecutionDate;

    @XmlElement(name = "InstructionId")
    private String instructionId;

    @XmlElement(name = "ChargeBearer")
    private String chargeBearer;

    @XmlElement(name = "EndToEndId")
    private String endToEndId;
    // Getters et setters pour DebtorAgent
    public FinancialInstitution getDebtorAgent() {
        return debtorAgent;
    }

    public void setDebtorAgent(FinancialInstitution debtorAgent) {
        this.debtorAgent = debtorAgent;
    }

    // Getters et setters pour CreditorAgent
    public FinancialInstitution getCreditorAgent() {
        return creditorAgent;
    }

    public void setCreditorAgent(FinancialInstitution creditorAgent) {
        this.creditorAgent = creditorAgent;
    }

    // Getters et setters pour InstructedAmount
    public Amount getInstructedAmount() {
        return instructedAmount;
    }

    public void setInstructedAmount(Amount instructedAmount) {
        this.instructedAmount = instructedAmount;
    }

    // Getters et setters existants
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PartyIdentification getDebtor() {
        return debtor;
    }

    public void setDebtor(PartyIdentification debtor) {
        this.debtor = debtor;
    }

    public CashAccount getDebtorAccount() {
        return debtorAccount;
    }

    public void setDebtorAccount(CashAccount debtorAccount) {
        this.debtorAccount = debtorAccount;
    }

    public PartyIdentification getCreditor() {
        return creditor;
    }

    public void setCreditor(PartyIdentification creditor) {
        this.creditor = creditor;
    }

    public CashAccount getCreditorAccount() {
        return creditorAccount;
    }

    public void setCreditorAccount(CashAccount creditorAccount) {
        this.creditorAccount = creditorAccount;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public RemittanceInformation getRemittanceInformation() {
        return remittanceInformation;
    }

    public void setRemittanceInformation(RemittanceInformation remittanceInformation) {
        this.remittanceInformation = remittanceInformation;
    }

    public String getPaymentInformationId() {
        return paymentInformationId;
    }

    public void setPaymentInformationId(String paymentId) {
        this.paymentInformationId = paymentId;
    }

    public boolean isBatchBooking() {
        return batchBooking;
    }

    public void setBatchBooking(boolean batchBooking) {
        this.batchBooking = batchBooking;
    }

    public String getRequestedExecutionDate() {
        return requestedExecutionDate;
    }

    public void setRequestedExecutionDate(String requestedExecutionDate) {
        this.requestedExecutionDate = requestedExecutionDate;
    }

    public void setInstructionId(String instructionId){
        this.instructionId=instructionId;
    }

    public void setChargeBearer(String chargeBearer){
        this.chargeBearer=chargeBearer;
    }
    public void setEndToEndId(String endToEndId){
        this.endToEndId=endToEndId;
    }
}
