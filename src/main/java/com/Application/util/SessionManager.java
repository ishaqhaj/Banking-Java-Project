package com.Application.util;

import com.Application.model.Account;
import com.Application.model.User;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static SessionManager instance;

    // Gestion des données de session
    private User authenticatedUser;
    private User selectedBeneficiary;
    private Account selectedAccountBeneficiary;
    private Account selectedAccount;

    // Gestion des contrôleurs
    private final Map<String, Object> controllers = new HashMap<>();

    private SessionManager() {}

    // Singleton pour obtenir l'instance unique de SessionManager
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Gestion des utilisateurs
    public void setAuthenticatedUser(User user) {
        this.authenticatedUser = user;
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    public User getSelectedBeneficiary() {
        return selectedBeneficiary;
    }

    public void setSelectedBeneficiary(User selectedBeneficiary) {
        this.selectedBeneficiary = selectedBeneficiary;
    }

    // Gestion des comptes
    public void setSelectedAccountBeneficiary(Account accountNumber) {
        this.selectedAccountBeneficiary = accountNumber;
    }

    public Account getSelectedAccountBeneficiary() {
        return selectedAccountBeneficiary;
    }

    public void setSelectedAccount(Account account) {
        this.selectedAccount = account;
    }

    public Account getSelectedAccount() {
        return this.selectedAccount;
    }

    // Gestion des contrôleurs
    public void registerController(String name, Object controller) {
        controllers.put(name, controller);
    }

    public Object getController(String name) {
        return controllers.get(name);
    }

    public <T> T getController(String name, Class<T> type) {
        Object controller = controllers.get(name);
        if (type.isInstance(controller)) {
            return type.cast(controller);
        }
        return null;
    }
    public void clear(){
        this.instance=null;
    }
}
