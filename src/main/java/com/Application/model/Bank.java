package com.Application.model;

public class Bank {
    private String name; // Nom de la banque
    private String bic;  // Code BIC de la banque

    public Bank(String name, String bic) {
        this.name = name;
        this.bic = bic;
    }
    public Bank(String name) {
        this.name = name;
    }
    // Getters et setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBic() { return bic; }
    public void setBic(String bic) { this.bic = bic; }
}
