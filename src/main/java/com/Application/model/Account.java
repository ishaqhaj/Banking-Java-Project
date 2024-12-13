package com.Application.model;

public class Account {
    private String accountNumber; // Numéro de compte
    private String accountType;   // Type de compte (épargne, courant, etc.)
    private Bank bank;            // Association avec la classe Bank
    private User owner;
    public Account(String accountNumber, String accountType, Bank bank) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.bank = bank;
    }

    // Getters et setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public Bank getBank() { return bank; }
    public void setBank(Bank bank) { this.bank = bank; }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }
}
