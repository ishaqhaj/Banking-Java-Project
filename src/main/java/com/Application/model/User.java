package main.java.com.Application.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String idValue;
    private String userId;
    private String name;
    private String password;
    private String email;
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private List<Account> accounts; // Liste de comptes

    public User(String idValue, String userId,String name, String password, String email, String address, String city, String postalCode, String country,Account account) {
        this.idValue = idValue;
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.accounts = new ArrayList<>(); // Initialisation de la liste de comptes
        this.accounts.add(account);
    }
    public User(String idValue, String userId,String name, String password, String email, String address, String city, String postalCode, String country) {
        this.idValue = idValue;
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.accounts = new ArrayList<>(); // Initialisation de la liste de comptes
    }

    // Getters et setters
    public String getIdValue() {
        return idValue;
    }

    public void setIdValue(String idValue) {
        this.idValue = idValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException {
        // Validation: name must not be null, empty, or exceed 140 characters
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        if (name.length() > 140) {
            throw new IllegalArgumentException("Le nom ne peut pas dépasser 140 caractères.");
        }
        // Validation: name must contain only allowed characters
        if (!name.matches("[A-Za-z0-9\\-' ]+")) {
            throw new IllegalArgumentException("Le nom contient des caractères non autorisés.");
        }
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    // Ajouter un compte à la liste
    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    // Supprimer un compte de la liste
    public void removeAccount(Account account) {
        this.accounts.remove(account);
    }
}
